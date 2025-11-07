package com.hrm.temp.repository;

import com.hrm.temp.entity.TransactionRisk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TransactionRiskRepository extends JpaRepository<TransactionRisk, Long> {
    
    @Query("SELECT tr FROM TransactionRisk tr WHERE tr.transaction.txn_id = :txn_id")
    Optional<TransactionRisk> findByTransaction_Txn_id(Long txn_id);

}
