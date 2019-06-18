package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.CreditInfoDao;
import com.scut.p2ploanplatform.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * @author: Light
 * @date: 2019/6/17 19:49
 * @description:
 */

@Service("creditService")
public class CreditServiceImpl implements CreditService {

    @Autowired
    private CreditInfoDao creditInfoDao;
    /**
     * 通过用户Id对用户进行征信
     * @param userId 被征信的用户Id
     * @return 用户的征信系数
     * @throws SQLException SQL异常
     * @throws  IllegalArgumentException 非法参数
     */
    @Override
    public Double creditReport(String userId) throws SQLException, IllegalArgumentException {
        return null;
    }

    /**
     * 通过用户Id对用户进行授信
     * @param userId 需要授信的用户Id
     * @return 用户的最大借款金额
     * @throws SQLException SQL异常
     * @throws  IllegalArgumentException 非法参数
     */
    @Override
    public Double creditGarant(String userId) throws SQLException, IllegalArgumentException {
        return null;
    }
}
