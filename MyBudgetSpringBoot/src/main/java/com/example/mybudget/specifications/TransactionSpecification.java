package com.example.mybudget.specifications;

import com.example.mybudget.models.SearchCriteria;
import com.example.mybudget.models.dtos.Account;
import com.example.mybudget.models.dtos.Transaction;
import com.example.mybudget.models.entities.AccountEntity;
import com.example.mybudget.models.entities.TransactionEntity;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
@NoArgsConstructor
public class TransactionSpecification implements Specification<TransactionEntity> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<TransactionEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getKey().equalsIgnoreCase("accountName")) {
            Join<TransactionEntity, AccountEntity> accountJoin = root.join("account", JoinType.INNER);
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return builder.like(
                        accountJoin.get("name"), "%" + criteria.getValue() + "%");
            }
        }else if (criteria.getKey().equalsIgnoreCase("deleted")) {
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return builder.equal(root.get("deleted"), criteria.getValue());
            }
        }
        return null;
    }
}
