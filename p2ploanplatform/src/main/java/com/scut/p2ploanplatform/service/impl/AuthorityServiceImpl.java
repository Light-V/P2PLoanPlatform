package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.AuthorityDao;
import com.scut.p2ploanplatform.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    AuthorityDao authorityDao;

    @Override
    public BigDecimal getAuthorityAmount(Integer authorityId) {
        return authorityDao.selectAuthority(authorityId).getAuthorityAmount();
    }
}
