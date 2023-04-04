package com.ocbc_rpp.rest.repositories;

import com.ocbc_rpp.rest.models.Transaction;
import com.ocbc_rpp.rest.models.TransactionReportSum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionGroup {

    @Autowired
    EntityManagerFactory entityManagerFactory;

    public List<TransactionReportSum> groupByFilterIdAndAmount(Long id, double amount){
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String c = "creator";
            String acc = "accountNo";
            em.getTransaction().begin();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
            Root<Transaction> root = query.from(Transaction.class);
            List<Selection<?>> selectList = new ArrayList<>();
            selectList.add(root.get(c).get("name"));
            selectList.add(root.get(c).get(acc));
            selectList.add(cb.function("date", LocalDate.class, root.get("transactionDate")));
            selectList.add(cb.sumAsDouble(root.get("amount")));
            List<Expression<?>> groupByList = new ArrayList<>();
            groupByList.add(root.get(c).get("name"));
            groupByList.add(root.get(c).get(acc));
            groupByList.add(cb.function("date", LocalDate.class, root.get("transactionDate")));

            Predicate equalAccount = cb.equal(root.get(c).get(acc), id);
            Predicate amountGreater = cb.greaterThanOrEqualTo(root.get("amount"), amount);
            query.multiselect(selectList).groupBy(groupByList).where(cb.and(equalAccount, amountGreater));
            List<Object[]> list = em.createQuery(query).getResultList();
            return objectToReport(list);
        }

    }

    private List<TransactionReportSum> objectToReport(List<Object[]> list){
        return list.stream().map(objects -> {
            TransactionReportSum report = new TransactionReportSum();
            report.setName(objects[0].toString());
            report.setId(Long.parseLong(objects[1].toString()));
            report.setDate((LocalDate) objects[2]);
            report.setAmount(Double.parseDouble(objects[3].toString()));
            return report;
        }).toList();
    }

}
