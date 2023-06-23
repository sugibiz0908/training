package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Prefectures;
import com.example.demo.entity.User;
import com.example.demo.service.TestService;
import com.example.demo.service.UserService;

import jakarta.persistence.OptimisticLockException;

/**
 * ユーザーに関する画面の制御を行うコントローラークラスです。
 */

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	TestService testService;

	public UserController(TestService testService) {
		this.testService = testService;
	}

	/**
	 * ユーザーの一覧画面を表示する。
	 *
	 * @param model モデル
	 * @return ユーザー一覧画面
	 */

	@GetMapping(value = "/user/list")
	public String displayList(Model model) {
		List<User> userlist = userService.searchAll();
		model.addAttribute("userlist", userlist);
		return "user/list";
	}

	/**
	 * ユーザーの詳細画面を表示する。
	 *
	 * @param id ユーザーID
	 * @param model モデル
	 * @return ユーザー詳細画面
	 */

	@GetMapping("/user/{id}")
	public String displayDetail(@PathVariable Long id, Model model) {
		User user = userService.search(id);
		model.addAttribute("user", user);

		return "user/detail";
	}

	//	@GetMapping("/user/add")
	//	public String displayAdd(Model model) {
	//
	//		model.addAttribute("user", new User());
	//		List<Prefectures> prefecturesList = testService.getPrefecturesAll();
	//        model.addAttribute("prefecturesList", prefecturesList);
	//        
	//		return "user/add";
	//	}
	//	

	//
	//	@PostMapping("/user/create")
	//	public String createUser(@Validated User user,BindingResult result, Model model) {
	//
	//		if(result.hasErrors()) {
	//			return "user/add";
	//		}
	//		
	//		userService.createUser(user);
	//
	//		return "redirect:/user/list";
	//	}

	/**
	 * ユーザーの新規登録画面を表示する。
	 *
	 * @param model モデル
	 * @return ユーザー登録画面
	 */

	@GetMapping("/user/add")
	public String displayAdd(Model model) {
		if (!model.containsAttribute("user")) {
			model.addAttribute("user", new User());
		}

		if (!model.containsAttribute("prefecturesList")) {
			List<Prefectures> prefecturesList = testService.getPrefecturesAll();
			model.addAttribute("prefecturesList", prefecturesList);
		}

		return "user/add";
	}

	//	/**
	//	 * ユーザーを新規登録する。
	//	 * <p>
	//	 * 入力エラーがある場合は、もとの入力画面にエラー内容を表示する。
	//	 * </p>
	//	 *
	//	 * @param user ユーザー情報
	//	 * @param result 入力チェック結果
	//	 * @param model モデル
	//	 * @return ユーザー一覧画面にリダイレクトする
	//	 */
	@PostMapping("/user/create")
	public String createUser(@Validated User user, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
			redirectAttributes.addFlashAttribute("user", user);
			return "redirect:/user/add";
		}

		String prefectureName = user.getPrefectureName();
		user.setPrefectures(prefectureName); // フィールド名が `prefectures` であることに注意してください

		userService.createUser(user);

		return "redirect:/user/list";
	}

	// 他のメソッドは省略

	/**
	 * 指定したユーザーを削除する。
	 *
	 * @param id ユーザーID
	 * @param model モデル
	 * @return ユーザー一覧画面にリダイレクトする
	 */

	@GetMapping("/user/{id}/delete")
	public String deleteUser(@PathVariable Long id, Model model) {
		userService.deleteUser(id);

		return "redirect:/user/list";

	}

	/**
	 * ユーザーの編集画面を表示する。
	 *
	 * @param id ユーザーID
	 * @param model モデル
	 * @return ユーザー編集画面
	 */

	@GetMapping("/user/{id}/edit")
	public String displayEdit(@PathVariable Long id, Model model) {
		if (!model.containsAttribute("user")) {
			User user = userService.search(id);
			model.addAttribute("user", user);
		}

		if (!model.containsAttribute("prefecturesList")) {
			List<Prefectures> prefecturesList = testService.getPrefecturesAll();
			model.addAttribute("prefecturesList", prefecturesList);
		}

		return "user/edit";
	}

	/**
	 * 指定したユーザーの情報を更新する。
	 * <p>
	 * 入力エラー、または、排他制御エラーがある場合は、もとの入力画面にエラー内容を表示する。
	 * </p>
	 *
	 * @param user ユーザー情報
	 * @param result 入力値のチェック結果
	 * @param model モデル
	 * @return 一覧画面にリダイレクトする
	 */
	@PostMapping("/user/update")
	public String updateUser(@Validated User user, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
			redirectAttributes.addFlashAttribute("user", user);
			redirectAttributes.addFlashAttribute("prefecturesList", testService.getPrefecturesAll());
			return "redirect:/user/" + user.getId() + "/edit";
		}

		try {
			userService.updateUser(user);
			return "redirect:/user/list";
		} catch (OptimisticLockException e) {
			redirectAttributes.addAttribute("message", e.getMessage());
			return "user/edit";
		}
	}
}
