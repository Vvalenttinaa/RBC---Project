package com.example.mybudget.services.impl;

import com.example.mybudget.exception.NotFoundException;
import com.example.mybudget.models.dtos.Account;
import com.example.mybudget.models.dtos.Currency;
import com.example.mybudget.models.entities.AccountEntity;
import com.example.mybudget.models.entities.UserEntity;
import com.example.mybudget.models.requests.AccountRequest;
import com.example.mybudget.models.responses.AccountInfo;
import com.example.mybudget.repositories.AccountRepository;
import com.example.mybudget.repositories.TransactionRepository;
import com.example.mybudget.repositories.UserRepository;
import com.example.mybudget.services.AccountService;
import com.example.mybudget.services.CurrencyService;
import com.example.mybudget.util.XmlReader;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.JAXBException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    AccountRepository accountRepository;
    TransactionRepository transactionRepository;
    UserRepository userRepository;
    CurrencyService currencyService;
    ModelMapper mapper;

    @Override
    public List<Account> getAll(Pageable pageable) {
        return accountRepository.findAllByDeleted((byte)0,pageable).stream().map(a-> mapper.map(a, Account.class)).collect(Collectors.toList());
    }

    @Override
    public List<AccountInfo>getAllAccountInfo(){
        return accountRepository.findAllAccountInfo();
    }

    @Override
    public Account insert(AccountRequest accountRequest) {
        AccountEntity accountEntity = mapper.map(accountRequest, AccountEntity.class);
        accountEntity.setDeleted((byte)0);
        return mapper.map(accountRepository.saveAndFlush(accountEntity), Account.class);
    }

    @Override
    public void deleteAll() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Override
    public Currency getTotalBalance() {
        //Must be in user default currency
        //Every account has different currency
        //Sum in EUR
        //Must convert them to user defaul
        List<Currency> currencies = accountRepository.findAllBalances();
        BigDecimal sum = BigDecimal.valueOf(0);
        for (Currency c:currencies
             ) {
            if(!("EUR").equals(c.getName().toUpperCase())){
                c.setValue(currencyService.convertToEuro(new Currency(c.getName(),c.getValue())));
                c.setName("EUR");
            }
            sum = sum.add(c.getValue());
        }
        String userDefaultCurrency = userRepository.findCurrency().orElseThrow(NotFoundException::new);
        if(("EUR").equals(userDefaultCurrency.toUpperCase())){
            return new Currency("EUR", sum);
        }
        BigDecimal value = currencyService.convertFromEuro(new Currency(userDefaultCurrency, sum));
        return new Currency(userDefaultCurrency, value);
    }

    @Override
    public List<String>getAllNames(){
        return accountRepository.findAllNames();
    }

    @Override
    public String getCurrency(String id) {
        return accountRepository.findById(UUID.fromString(id)).orElseThrow(NotFoundException::new).getCurrency();
    }

    @Override
    public Account setBalance(BigDecimal value, UUID id){
        AccountEntity accountEntity = accountRepository.findById(id).orElseThrow(NotFoundException::new);
        accountEntity.setBalance(accountEntity.getBalance().add(value));
        accountEntity = accountRepository.saveAndFlush(accountEntity);
        return mapper.map(accountEntity, Account.class);
    }

    @Override
    public void readOldData() throws JAXBException, IOException {
        if(userRepository.findUpdated().orElseThrow(NotFoundException::new) == 0){
            List<AccountEntity> accountEntities = XmlReader.parseXMLData();
            for (AccountEntity account:accountEntities
                 ) {
                account.setId(null);
                account.setDeleted((byte)0);
                AccountEntity newAccount = accountRepository.saveAndFlush(account);
                if (account.getTransactions() != null) {
                    account.getTransactions().forEach(transaction -> {
                        transaction.setId(null);
                        transaction.setDeleted((byte)0);
                        transaction.setAccount(newAccount);
                        transaction.setAccountId(newAccount.getId());
                        transactionRepository.save(transaction);
                    });
                }
            }
            UserEntity userEntity = userRepository.findUser().orElseThrow(NotFoundException::new);
            userEntity.setUpdated((byte)1);
            userRepository.saveAndFlush(userEntity);
        }
    }
}

