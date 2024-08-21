package com.example.mybudget.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "user", schema = "my_budget", catalog = "")
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Basic
    @Column(name = "current_currency")
    private String currentCurrency;
    @Basic
    @Column(name="updated")
    private Byte updated;
}
