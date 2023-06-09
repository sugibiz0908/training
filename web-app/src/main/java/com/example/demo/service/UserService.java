package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public List<User> searchAll() {
		return userRepository.findAll();
	}

	public User search(Long id) {
		// TODO 自動生成されたメソッド・スタブ
		return userRepository.findById(id).get();
	}
	
	public User createUser(User user) {
		LocalDateTime now = LocalDateTime.now();
		user.setCreateDate(now);
		user.setUpdateDate(now);
		return userRepository.save(user);
	}
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
	@Transactional
	public User updateUser(User user) {
		User currentUser = userRepository.findOneForUpdate(user.getId());

		if (currentUser.getUpdateDate().equals(user.getUpdateDate())) {
			LocalDateTime now = LocalDateTime.now();
			user.setUpdateDate(now);
			return userRepository.save(user);

		} else {
			String message = "データが他の方によって更新されたようです。一覧画面に戻ってから再実施してください。";
			throw new OptimisticLockException(message);
		}
	}
	
	
	
}
