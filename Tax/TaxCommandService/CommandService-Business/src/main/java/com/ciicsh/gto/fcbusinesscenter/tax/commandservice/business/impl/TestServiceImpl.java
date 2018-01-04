package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TestMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TestService;
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
