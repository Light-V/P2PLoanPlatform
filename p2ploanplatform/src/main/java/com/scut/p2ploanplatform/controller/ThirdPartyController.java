package com.scut.p2ploanplatform.controller;

import com.scut.p2ploanplatform.service.BankAccountService;
import com.scut.p2ploanplatform.service.P2pAccountService;
import com.scut.p2ploanplatform.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/findBalance")
    public ResultVo findBalance(HttpSession session) throws SQLException,IllegalArgumentException
    {
        ResultVo resultVo=new ResultVo();
        String thirdPartyId=(String) session.getAttribute("thirdPartyId");
        if (!p2pAccountService.verifyIfExists(thirdPartyId))
        {
            resultVo.setMsg(String.format("id为%s的账户还未创建！",thirdPartyId));
            resultVo.setCode(0);
        }
        else
        {
            BigDecimal balance=p2pAccountService.findBalance(thirdPartyId);
            resultVo.setMsg("查询成功！");
            resultVo.setCode(1);
            resultVo.setData(balance);
        }
        return resultVo;
    }

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
                resultVo.setMsg("交易成功!");
                resultVo.setCode(1);
            }
            else
            {
                resultVo.setMsg("余额不足!");
                resultVo.setCode(0);
            }
        }
        else
        {
            resultVo.setMsg("密码错误!");
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
                resultVo.setMsg("充值成功!");
            }
            else
            {
                resultVo.setCode(0);
                resultVo.setMsg("余额不足!");
            }
        }
        else
        {
            resultVo.setMsg("密码错误!");
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
                resultVo.setMsg("提现成功!");
            }
            else
            {
                resultVo.setCode(0);
                resultVo.setMsg("余额不足！");
            }
        }
        else
        {
            resultVo.setCode(0);
            resultVo.setMsg("密码错误!");
        }
        return resultVo;
    }
}
