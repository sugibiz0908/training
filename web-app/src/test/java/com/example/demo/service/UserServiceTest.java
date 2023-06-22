package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import jakarta.persistence.OptimisticLockException;

@SpringBootTest
class UserServiceTest {
	 
	@Autowired
	UserService userService;
	
	@MockBean
	UserRepository userRepository;
	
	/**
	 * ユーザー更新で正常系をテストする
	 */
	@Test
	void testUpdateUser_Success() {
		
		//テストデータ
		User user = new User();
		user.setId(1L);
		user.setName("テスト太郎");
		LocalDateTime base = LocalDateTime.of(2021,1,1,12,0,0);
		user.setCreateDate(base);
		user.setUpdateDate(base);
		user.setDeleteDate(base);
		
		//モック設定
		//Mockito でデータベースに接続しないようにする
		when(userRepository.findOneForUpdate(user.getId())).thenReturn(user);
		when(userRepository.save(user)).thenReturn(user);
		
		//テスト実行(メソッド呼び出し)
		User actual =  userService.updateUser(user);
		
		
		//検証（確認する）
		
		//findOneForUpdateメソッドが呼び出されたか
		verify(userRepository, atLeastOnce()).findOneForUpdate(user.getId());
		verify(userRepository, atLeastOnce()).save(user);
		
		//実行後ユーザー検証
		assertEquals(user, actual);
		//日時が変化していないこと
		assertEquals(base, actual.getCreateDate());
		assertEquals(base, actual.getDeleteDate());
		//更新日時が変化していること
		assertTrue(actual.getUpdateDate().isAfter(base));
		
		
	
	}
	
	/**
	 * ユーザー更新で異常系をテストする
	 */
	@Test
	@DisplayName("ユーザー更新：異常系（OptimisticLockException）")
	void testUpdateUser_OptimisticLockException() {
		
		//テストデータ
		User user = new User();
		user.setId(1L);
		user.setName("テスト太郎");
		LocalDateTime base = LocalDateTime.of(2021,1,1,12,0,0);
		user.setCreateDate(base);
		user.setUpdateDate(base);
		user.setDeleteDate(base);
		// current user
		User currentUser = new User();
		currentUser.setId(1L);
		currentUser.setName("テスト太郎");
		currentUser.setUpdateDate(LocalDateTime.now());
;
		
		//モック設定
		//Mockito でデータベースに接続しないようにする
		when(userRepository.findOneForUpdate(user.getId())).thenReturn(currentUser);
		when(userRepository.save(user)).thenReturn(currentUser);
		
		//テスト実行(例外が発生するので補足が必要)
		Throwable e = assertThrows(OptimisticLockException.class, () -> userService.updateUser(user));
		
		
		//検証（確認する）
		
		//findOneForUpdateメソッドが呼び出されたか
		verify(userRepository, atLeastOnce()).findOneForUpdate(user.getId());
		verify(userRepository, never()).save(user);
		
		
		//実行後ユーザー検証
		assertEquals("データが他の方によって更新されたようです。一覧画面に戻ってから再実施してください。", e.getMessage());
		
		
	
	}

}
