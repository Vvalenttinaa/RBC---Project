package com.example.mybudget.models.entities;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction", schema = "my_budget", catalog = "")
@XmlRootElement(name = "Transaction")

public class TransactionEntity {
    @GeneratedValue(strategy = GenerationType.UUID)
    // @Type(type = "org.hibernate.type.UUIDCharType")
    @JdbcTypeCode(Types.VARCHAR)
    @Id
//    @Column(name = "id")
    @Column(columnDefinition = "BINARY(16)", name = "id")

    private UUID id;
    @Basic
    @Column(name = "amount")
    private BigDecimal amount;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "account_id")
    private UUID accountId;
    @Basic
    @Column(name = "deleted")
    private Byte deleted;
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private AccountEntity account;

    @XmlElement(name = "Description")
    public String getDescription() {
        return description;
    }
    @XmlElement(name = "Amount")
    public BigDecimal getAmount() {
        return amount;
    }

}
