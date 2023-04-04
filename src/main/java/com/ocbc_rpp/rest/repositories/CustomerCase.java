package com.ocbc_rpp.rest.repositories;

import com.ocbc_rpp.rest.models.Customer;
import com.ocbc_rpp.rest.models.CustomerInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerCase {
    @Autowired
    EntityManagerFactory entityManagerFactory;

    public List<CustomerInfo> customerInfos(){
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            em.getTransaction().begin();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
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
            return toInfo(em.createQuery(query).getResultList());
        }

    }
    public List<CustomerInfo> toInfo(List<Object[]> list){
                return list.stream().map(objects -> {
            CustomerInfo info = new CustomerInfo();
            info.setAccountNo(Long.parseLong(objects[0].toString()));
            info.setName(objects[1].toString());
            info.setPhone_no(objects[2].toString());
            info.setBalance(Double.parseDouble(objects[3].toString()));
            info.setCode(objects[4].toString());
            return info;
        }).toList();
    }
}
