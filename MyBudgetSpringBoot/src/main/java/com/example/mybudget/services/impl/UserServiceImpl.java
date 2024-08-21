package com.example.mybudget.services.impl;

import com.example.mybudget.exception.NotFoundException;
import com.example.mybudget.models.dtos.User;
import com.example.mybudget.models.entities.UserEntity;
import com.example.mybudget.repositories.UserRepository;
import com.example.mybudget.services.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Transactional
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    ModelMapper mapper;

    @Override
    public User getUser() {
        return mapper.map(userRepository.findUser().orElseThrow(NotFoundException::new), User.class);
    }

    @Override
    public User updateCurrency(String currency) {
        UserEntity userEntity = userRepository.findUser().orElseThrow(NotFoundException::new);
        userEntity.setCurrentCurrency(currency);
        userEntity.setUpdatedAt(Timestamp.from(Instant.now()));
        return mapper.map(userRepository.saveAndFlush(userEntity), User.class);
    }

    @Override
    public String getCurrency() {
        return userRepository.findCurrency().orElseThrow(NotFoundException::new);
    }
}
