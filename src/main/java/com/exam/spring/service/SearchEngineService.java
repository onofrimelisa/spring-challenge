package com.exam.spring.service;

import com.exam.spring.interfaces.ISearchEngineRepository;
import com.exam.spring.interfaces.ISearchEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchEngineService implements ISearchEngineService {
    @Autowired
    private ISearchEngineRepository searchEngineRepository;
}
