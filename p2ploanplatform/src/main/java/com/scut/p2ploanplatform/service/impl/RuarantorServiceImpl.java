package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.GuarantorDao;
import com.scut.p2ploanplatform.entity.Guarantor;
import com.scut.p2ploanplatform.service.GuarantorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.SQLException;

/**
 * @author: zrh
 * @date: 2019/6/26
 * @description:处理担保人登录注册活动
 */

@Service
public class RuarantorServiceImpl implements GuarantorService {

    @Autowired
    private GuarantorDao guarantorDao;

    @Override
    public int insertGuarantor(String guarantorId, String password, String name, String thirdPartyId, int authorityId)
            throws SQLException, IllegalArgumentException {
        Guarantor guarantor = guarantorDao.selectGuarantor(guarantorId);
        if( guarantor == null )
        {
            guarantor = new Guarantor();
            guarantor.setGuarantorId(guarantorId);
            guarantor.setPassword(password);
            guarantor.setThirdPartyId(thirdPartyId);
            guarantor.setAuthorityId(authorityId);
            guarantor.setName(name);
            return guarantorDao.insertGuarantor(guarantor);
        } else {
            return 0;
        }
    }

    @Override
    public Guarantor findGuarantor(String guarantorId) throws SQLException, IllegalArgumentException {
        Guarantor guarantor = guarantorDao.selectGuarantor(guarantorId);
        return guarantor;
    }

    @Override
    public int updateGuarantor(String userId) throws SQLException, IllegalArgumentException {
        //need to fix
        return 0;
    }

    @Override
    public int deleteGuarantor ( String guarantorId ) throws SQLException, IllegalArgumentException {
        int result = guarantorDao.deleteGuarantor(guarantorId);
        return result;
    }
}
