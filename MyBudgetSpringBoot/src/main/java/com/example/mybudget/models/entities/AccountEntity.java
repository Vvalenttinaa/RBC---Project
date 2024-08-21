package com.example.mybudget.models.entities;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "account", schema = "my_budget", catalog = "")
@XmlRootElement(name = "Account")
public class AccountEntity {
    @GeneratedValue(strategy = GenerationType.UUID)
   // @Type(type = "org.hibernate.type.UUIDCharType")
    @JdbcTypeCode(Types.VARCHAR)
    @Column(columnDefinition = "BINARY(16)", name = "id")


    @Id
//    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "currency")
    private String currency;
    @Basic
    @Column(name = "balance")
    private BigDecimal balance;
    @Basic
    @Column(name = "deleted")
    private Byte deleted;
    @OneToMany(mappedBy = "account")
    private List<TransactionEntity> transactions;
    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }
    @XmlAttribute(name = "currency")
    public String getCurrency() {
        return currency;
    }
    @XmlElement(name = "Balance")
    public BigDecimal getBalance() {
        return balance;
    }
    @XmlElementWrapper(name = "Transactions")
    @XmlElement(name = "Transaction")
    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }
    @Override
    public String toString() {
        return "AccountEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
