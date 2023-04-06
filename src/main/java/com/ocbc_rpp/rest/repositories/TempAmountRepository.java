package com.ocbc_rpp.rest.repositories;

import com.ocbc_rpp.rest.models.TempAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;

public interface TempAmountRepository extends JpaRepository<TempAmount, Long> {
    @Procedure
    List<TempAmount> my_temp();
}
