package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Prefectures;
import com.example.demo.repository.PrefecturesRepository;
/**
 * テスト用 Service
 */
@Service
public class TestService {
    /**
     * 都道府県マスタ Repository
     */
    @Autowired
    private PrefecturesRepository prefecturesRepository;
    /**
     * 都道府県マスタ 全検索
     * @return 検索結果
     */
    public List<Prefectures> getPrefecturesAll() {
        return prefecturesRepository.findAll();
    }
}
