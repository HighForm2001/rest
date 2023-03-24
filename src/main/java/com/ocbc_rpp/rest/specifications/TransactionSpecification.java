package com.ocbc_rpp.rest.specifications;

import com.ocbc_rpp.rest.models.Transaction;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {
    public static Specification<Transaction> createdBy(Long id){
        return new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("creator").get("accountNo"),id);
            }
        };
    }
    public static Specification<Transaction> createdOn(LocalDate date){
        return new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get("transactionDate"),date.atStartOfDay(),date.atTime(LocalTime.MAX));
            }
        };
    }
    public static Specification<Transaction> group(Long id, double amount){
        return new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Expression<?>> groupByList = new ArrayList<>();
                groupByList.add(root.get("creator").get("name"));
                groupByList.add(root.get("creator").get("accountNo"));
                groupByList.add(root.get("transactionDate"));
                List<Selection<?>> selectList = new ArrayList<>();
                selectList.add(root.get("creator").get("name"));
                selectList.add(root.get("creator").get("accountNo"));
                selectList.add(root.get("transactionDate"));
                query.multiselect(selectList);
                query.groupBy(groupByList);

                Predicate accountEqual = criteriaBuilder.equal(root.get("creator").get("accountNo"),id);
                Predicate amountGreater = criteriaBuilder.greaterThanOrEqualTo(root.get("amount"),amount);
                return criteriaBuilder.and(amountGreater,accountEqual);
            }
        };
    }
}
