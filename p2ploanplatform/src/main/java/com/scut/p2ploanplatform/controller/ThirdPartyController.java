package com.scut.p2ploanplatform.controller;

import com.scut.p2ploanplatform.service.BankAccountService;
import com.scut.p2ploanplatform.service.P2pAccountService;
import com.scut.p2ploanplatform.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.SQLException;


@RestController
@RequestMapping("/thirdParty")
public class ThirdPartyController {
    @Autowired
    private P2pAccountService p2pAccountService;

    @Autowired
    private BankAccountService bankAccountService;

    @PutMapping("/purchase")
    public ResultVo transfer(HttpSession session) throws SQLException,IllegalArgumentException
    {
        ResultVo resultVo=new ResultVo();
        String payerId=(String) session.getAttribute("payerId");
        String payeeId=(String) session.getAttribute("payeeId");
        String payerPassword=(String) session.getAttribute("payerPassword");
        BigDecimal amount=(BigDecimal) session.getAttribute("amount");
        if (p2pAccountService.verifyPassword(payerId,payerPassword))
        {
            Boolean result=p2pAccountService.transfer(payerId,payeeId,amount);
            if (result)
            {
                resultVo.setMsg("Success!");
                resultVo.setCode(1);
            }
            else
            {
                resultVo.setMsg("The payer's balance is not enough to pay!");
                resultVo.setCode(0);
            }
        }
        else
        {
            resultVo.setMsg("The password is wrong!");
            resultVo.setCode(0);
        }
        return resultVo;
    }

    @PutMapping("/recharge")
    public ResultVo recharge(HttpSession session) throws SQLException,IllegalArgumentException
    {
        ResultVo resultVo=new ResultVo();
        String thirdPartyId=(String) session.getAttribute("thirdPartyId");
        String cardId=(String) session.getAttribute("cardId");
        BigDecimal amount=(BigDecimal) session.getAttribute("amount");
        String password=(String) session.getAttribute("paymentPassword");
        if (bankAccountService.verifyPassword(cardId,password))
        {
            Boolean result=p2pAccountService.recharge(thirdPartyId,cardId,amount);
            if (result)
            {
                resultVo.setCode(1);
                resultVo.setMsg("Success!");
            }
            else
            {
                resultVo.setCode(0);
                resultVo.setMsg("The balance is not enough to recharge!");
            }
        }
        else
        {
            resultVo.setMsg("The password is wrong!");
            resultVo.setCode(0);
        }
        return resultVo;
    }

    @PutMapping("/withdraw")
    public ResultVo withdraw(HttpSession session) throws SQLException,IllegalArgumentException
    {
        ResultVo resultVo=new ResultVo();
        String thirdPartyId=(String) session.getAttribute("thirdPartyId");
        String cardId=(String) session.getAttribute("cardId");
        BigDecimal amount=(BigDecimal) session.getAttribute("amount");
        String password=(String) session.getAttribute("paymentPassword");
        if (bankAccountService.verifyPassword(cardId,password))
        {
            Boolean result=p2pAccountService.withdraw(thirdPartyId,cardId,amount);
            if (result)
            {
                resultVo.setCode(1);
                resultVo.setMsg("Success!");
            }
            else
            {
                resultVo.setCode(0);
                resultVo.setMsg("The balance is not enough to withdraw!");
            }
        }
        else
        {
            resultVo.setCode(0);
            resultVo.setMsg("The password is wrong!");
        }
        return resultVo;
    }
}
