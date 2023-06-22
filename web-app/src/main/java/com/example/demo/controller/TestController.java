package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Prefectures;
import com.example.demo.service.TestService;

/**
 * テスト用 Controller
 */
@Controller
public class TestController {

    /**
     * テスト用 Service
     */
    @Autowired
    private TestService testService;

    /**
     * テスト画面表示
     * @param model Model
     * @return テスト画面
     */
    @GetMapping(value = "/test")
    public String displayList(Model model) {

        List<Prefectures> prefecturesList = testService.getPrefecturesAll();
        model.addAttribute("prefecturesList", prefecturesList);

         //プルダウンの初期値を設定する場合は指定
        model.addAttribute("selectedValue", "01");

        return "user/add";
    }
}