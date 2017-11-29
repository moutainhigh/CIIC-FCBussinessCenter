package com.ciicsh.gto.fcsupportcenter.tax.queryservice.business.impl;

import com.ciicsh.gto.fcsupportcenter.tax.queryservice.business.TestService;
import com.ciicsh.gto.fcsupportcenter.tax.commandservice.dao.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@SuppressWarnings("all")
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;
}
