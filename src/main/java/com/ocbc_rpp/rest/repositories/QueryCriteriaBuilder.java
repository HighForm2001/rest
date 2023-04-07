package com.ocbc_rpp.rest.repositories;

import com.ocbc_rpp.rest.models.*;
import com.ocbc_rpp.rest.models.dto.CustomerDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QueryCriteriaBuilder {

    @Autowired
    EntityManagerFactory entityManagerFactory;

    public List<TransactionReportSum> transactionGroupByFilterIdAndAmount(Long id, double amount){
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String c = "creator";
            String acc = "accountNo";
//            em.getTransaction().begin();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TransactionReportSum> query = cb.createQuery(TransactionReportSum.class);
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
            return em.createQuery(query).getResultList();
        }

    }

    private List<TransactionReportSum> transactionToReport(List<Object[]> list){
        return list.stream().map(objects -> {
            TransactionReportSum report = new TransactionReportSum();
            report.setName(objects[0].toString());
            report.setId(Long.parseLong(objects[1].toString()));
            report.setDate((LocalDate) objects[2]);
            report.setAmount(Double.parseDouble(objects[3].toString()));
            return report;
        }).toList();
    }
    public List<CustomerInfo> customerInfos(){
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
//            em.getTransaction().begin();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<CustomerInfo> query = cb.createQuery(CustomerInfo.class);
            Root<Customer> root = query.from(Customer.class);
            query.multiselect(root.get("accountNo")
                    , root.get("name")
                    , root.get("phoneNo")
                    , root.get("balance")
                    , cb.selectCase(cb.substring(root.get("phoneNo"), 1, 2))
                            .when("60", "MY")
                            .when("61", "AU")
                            .when("65", "SG")
                            .otherwise("Other"));
//            return customerToInfo(em.createQuery(query).getResultList());
            return em.createQuery(query).getResultList();
        }

    }
    public List<CustomerInfo> customerToInfo(List<Object[]> list){
        return list.stream().map(objects -> {
            CustomerInfo info = new CustomerInfo();
            info.setId(Long.parseLong(objects[0].toString()));
            info.setName(objects[1].toString());
            info.setPhone_no(objects[2].toString());
            info.setBalance(Double.parseDouble(objects[3].toString()));
            info.setCode(objects[4].toString());
            return info;
        }).toList();
    }

    @Transactional
    public List<TempAmount> createAndUseTemporaryTable(){
        String sql = "CREATE TEMPORARY TABLE temp_amount ON COMMIT DROP " +
                "AS SELECT t.from_acc_id as account_no, sum(t.amount) as total_amount from transaction_t t " +
                "group by t.from_acc_id";
        try (EntityManager em = entityManagerFactory.createEntityManager())
        {
            em.getTransaction().begin();
            em.createNativeQuery(sql).executeUpdate();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
            Root<TempAmount> root = query.from(TempAmount.class);
            List<Selection<?>> selections = new ArrayList<>();
            Join<TempAmount, CustomerDto> customerJoin = root.join("customer",JoinType.INNER);
            customerJoin.on(cb.equal(customerJoin.get("accountNo"),root.get("accountNo")));
            selections.add(root.get("accountNo"));
            selections.add(root.get("total_amount"));
            selections.add(customerJoin.get("name"));
            query.multiselect(selections);

            List<TempAmount> balances = em.createQuery(query).getResultList().stream().map(s->
                new TempAmount((Long)s[0],(BigDecimal) s[1],s[2].toString())).toList();
            em.getTransaction().commit();
            return balances;

        }

    }

}
