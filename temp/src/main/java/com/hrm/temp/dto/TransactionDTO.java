package com.hrm.temp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {

    // js will use this constructor to create object that first and map field from json
    public TransactionDTO() {

    }

    @JsonProperty("cus_id")
    private String cus_id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("txn_date")
    private LocalDateTime txn_date;

    @JsonProperty("source_acct")
    private String source_acct;

    @JsonProperty("source_acct_name")
    private String source_acct_name;

    @JsonProperty("s_customer_type")
    private String s_customer_type;

    @JsonProperty("trans_type")
    private String trans_type;

    @JsonProperty("dest_acct")
    private String dest_acct;

    @JsonProperty("dest_acct_name")
    private String dest_acct_name;

    @JsonProperty("d_customer_type")
    private String d_customer_type;

    @JsonProperty("trans_amount")
    private BigDecimal trans_amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("txn_desc")
    private String txn_desc;

    @JsonProperty("purpose")
    private String purpose;

    @JsonProperty("relationship")
    private String relationship;

    @JsonProperty("risk_lv")
    private String risk_lv;

    @JsonProperty("score")
    private BigDecimal score;

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

    public String getRisk_lv() { return risk_lv; }
    public void setRisk_lv(String risk_lv) { this.risk_lv = risk_lv; }

    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
}
