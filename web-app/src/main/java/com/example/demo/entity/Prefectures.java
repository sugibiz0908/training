package com.example.demo.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
/**
 * 都道府県マスタ Entity
 */
@Entity
@Data
@Table(name = "prefectures")
public class Prefectures implements Serializable {
    /**
     * 都道府県コード
     */
    @Id
    @Column(name = "code")
    private int code;
    /**
     * 都道府県名
     */
    @Column(name = "name")
    private String name;
}
