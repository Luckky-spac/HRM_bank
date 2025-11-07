package com.hrm.temp.service;

import org.springframework.stereotype.Service;
import com.hrm.temp.dto.TransactionDTO;
import com.hrm.temp.dto.TransactionResponseDTO;
import com.hrm.temp.entity.Transaction;
import com.hrm.temp.entity.TransactionRisk;
import com.hrm.temp.repository.TransactionRiskRepository;
import com.hrm.temp.repository.TransactionRepository;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final TransactionRiskRepository riskRepository;


    public TransactionService(TransactionRepository transactionRepository,
                              TransactionRiskRepository riskRepository) {
        this.transactionRepository = transactionRepository;
        this.riskRepository = riskRepository;
    }
    
    // Get all transactions
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    // Get transaction by id 
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getAllTransactionsWithRisk() {
        List<Transaction> transactions = transactionRepository.findAllWithRisk();

        return transactions.stream()
                .map(txn -> new TransactionResponseDTO(txn))
                .toList();

    }

    @Transactional
    public Transaction createTransaction(TransactionDTO dto) {
        //insert transaction
        Transaction txn = new Transaction();
        txn.setCus_id(dto.getCus_id()); 
        txn.setTxn_date(dto.getTxn_date() != null ? dto.getTxn_date() : LocalDateTime.now());
        txn.setSource_acct(dto.getSource_acct());
        txn.setSource_acct_name(dto.getSource_acct_name());    
        txn.sets_customer_type(dto.gets_customer_type());
        txn.setTrans_type(dto.getTrans_type());
        txn.setDest_acct(dto.getDest_acct());
        txn.setDest_acct_name(dto.getDest_acct_name());
        txn.setd_customer_type(dto.getd_customer_type());
        txn.setTrans_amount(dto.getTrans_amount());
        txn.setCurrency(dto.getCurrency());
        txn.setTxn_desc(dto.getTxn_desc());
        txn.setPurpose(dto.getPurpose());
        txn.setRelationship(dto.getRelationship());
        txn.setCreatedAt(LocalDateTime.now());

        txn = transactionRepository.save(txn);

        // save risk if provided
        if (dto.getRisk_lv() != null && dto.getScore() != null) {
            TransactionRisk risk = new TransactionRisk();
            risk.setTransaction(txn);
            risk.setRisk_lv(dto.getRisk_lv());
            risk.setScore(dto.getScore());
            riskRepository.save(risk);
        }

        return txn;
    }

    // Update transaction
    @Transactional
    public Transaction updateTransaction(Long id, TransactionDTO dto) {
        Transaction txn = transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

        txn.setSource_acct(dto.getSource_acct());
        txn.setSource_acct_name(dto.getSource_acct_name()); 
        
        txn.sets_customer_type(dto.gets_customer_type());
        txn.setTrans_type(dto.getTrans_type());
        txn.setDest_acct(dto.getDest_acct());
        txn.setDest_acct_name(dto.getDest_acct_name());
        txn.setd_customer_type(dto.getd_customer_type());
        txn.setTrans_amount(dto.getTrans_amount());
        txn.setCurrency(dto.getCurrency());
        txn.setTxn_desc(dto.getTxn_desc());
        txn.setPurpose(dto.getPurpose());
        txn.setRelationship(dto.getRelationship());

        txn = transactionRepository.save(txn);

        // Update risk if exists
        Optional<TransactionRisk> existingRisk = riskRepository.findByTransaction_Txn_id(id);
        if (existingRisk.isPresent()) {
            TransactionRisk risk = existingRisk.get();
            if (dto.getRisk_lv() != null) {
                risk.setRisk_lv(dto.getRisk_lv());
            }
            if (dto.getScore() != null) {
                risk.setScore(dto.getScore());
            }
            risk.setUpdateAt(LocalDateTime.now());
            riskRepository.save(risk);
        } else if (dto.getRisk_lv() != null && dto.getScore() != null){
            TransactionRisk risk = new TransactionRisk();
            risk.setRisk_lv(dto.getRisk_lv());
            risk.setScore(dto.getScore());
            risk.setCreateAt(LocalDateTime.now());
            riskRepository.save(risk);
        }

        return txn;
    }

    // Delete transaction
    @Transactional
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }
}
