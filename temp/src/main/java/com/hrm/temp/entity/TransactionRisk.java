package com.hrm.temp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "transactions_risk")
public class TransactionRisk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "txn_risk_id")
    private Long txn_risk_id;

    @ManyToOne
    @JoinColumn(name = "txn_id")
    @JsonBackReference
    private Transaction transaction;

    @Column(name = "risk_lv")
    private String risk_lv;

    @Column(name = "score", precision = 10, scale = 2)
    private BigDecimal score;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    // Constructors
    public TransactionRisk() {}

    @PrePersist
    public void prePersist() {
        createAt = LocalDateTime.now();
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updateAt = LocalDateTime.now();
    }


     // Getters & Setters
    public Long getTxn_risk_id() { return txn_risk_id; }
    public void setTxn_risk_id(Long txn_risk_id) { this.txn_risk_id = txn_risk_id; }

    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }

    public String getRisk_lv() { return risk_lv; }
    public void setRisk_lv(String risk_lv) { this.risk_lv = risk_lv; }

    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }

    public LocalDateTime getCreateAt() { return createAt; }
    public void setCreateAt(LocalDateTime createAt) { this.createAt = createAt; }

    public LocalDateTime getUpdateAt() { return updateAt; }
    public void setUpdateAt(LocalDateTime updateAt) { this.updateAt = updateAt; }

}

