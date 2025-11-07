package com.hrm.temp.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "txn_id")
    private Long txn_id;

    @Column(name = "CUS_ID")
    private String cus_id;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "txn_date")
    private LocalDateTime txn_date;
    
    @Column(name = "source_acct")
    private String source_acct;
    
    @Column(name = "source_acct_name")
    private String source_acct_name;
    
    @Column(name = "s_customer_type")
    private String s_customer_type;
    
    @Column(name = "trans_type")
    private String trans_type;
    
    @Column(name = "dest_acct")
    private String dest_acct;
    
    @Column(name = "dest_acct_name")
    private String dest_acct_name;
    
    @Column(name = "d_customer_type")
    private String d_customer_type;
    
    @Column(name = "trans_amount", precision = 15, scale = 2)
    private BigDecimal trans_amount;
    
    @Column(name = "currency")
    private String currency;
    
    @Column(name = "txn_Desc")
    private String txn_desc;
    
    @Column(name = "purpose")
    private String purpose;
    
    @Column(name = "relationship")
    private String relationship;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<TransactionRisk> risks;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updateAt = LocalDateTime.now();
    }

    @OneToOne(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TransactionRisk risk;

    // Constructors
    public Transaction() {}

    // Getters & Setters
    public Long getTxn_id() { return txn_id; }
    public void setTxn_id(Long txn_id) { this.txn_id = txn_id; }

    public String getCus_id() { return cus_id; }
    public void setCus_id(String cus_id) { this.cus_id = cus_id; }

    public LocalDateTime getTxn_date() { return txn_date; }
    public void setTxn_date(LocalDateTime txn_date) { this.txn_date = txn_date; }

    public String getSource_acct() { return source_acct; }
    public void setSource_acct(String source_acct) { this.source_acct = source_acct; }

    public String getSource_acct_name() { return source_acct_name; }
    public void setSource_acct_name(String source_acct_name) { this.source_acct_name = source_acct_name; }

    public String gets_customer_type() { return s_customer_type; }
    public void sets_customer_type(String s_customer_type) { this.s_customer_type = s_customer_type; }

    public String getTrans_type() { return trans_type; }
    public void setTrans_type(String trans_type) { this.trans_type = trans_type; }

    public String getDest_acct() { return dest_acct; }
    public void setDest_acct(String dest_acct) { this.dest_acct = dest_acct; }

    public String getDest_acct_name() { return dest_acct_name; }
    public void setDest_acct_name(String dest_acct_name) { this.dest_acct_name = dest_acct_name; }

    public String getd_customer_type() { return d_customer_type; }
    public void setd_customer_type(String d_customer_type) { this.d_customer_type = d_customer_type; }

    public BigDecimal getTrans_amount() { return trans_amount; }
    public void setTrans_amount(BigDecimal trans_amount) { this.trans_amount = trans_amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getTxn_desc() { return txn_desc; }
    public void setTxn_desc(String txn_desc) { this.txn_desc = txn_desc; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdateAt() { return updateAt; }
    public void setUpdateAt(LocalDateTime updateAt) { this.updateAt = updateAt; }

    public TransactionRisk getRisk() { return risk; }
    public void setRisks(List<TransactionRisk> risks) { this.risks = risks; }
    public List<TransactionRisk> getRisks() { return risks; }
}
