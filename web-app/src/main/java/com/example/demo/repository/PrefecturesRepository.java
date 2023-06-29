package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Prefectures;

import jakarta.persistence.LockModeType;
/**
 * 都道府県マスタ Repository
 */
@Repository
public interface PrefecturesRepository extends JpaRepository<Prefectures, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT u FROM Prefectures u WHERE u.code = :code")
	Prefectures findOneForUpdate(@Param("code") Long code);
}