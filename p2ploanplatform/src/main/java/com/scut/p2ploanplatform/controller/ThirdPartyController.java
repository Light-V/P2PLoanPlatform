package com.scut.p2ploanplatform.controller;

import com.scut.p2ploanplatform.entity.BankAccount;
import com.scut.p2ploanplatform.service.impl.BankAccountServiceImpl;
import com.scut.p2ploanplatform.service.impl.P2pAccountServiceImpl;
import com.scut.p2ploanplatform.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: tzz
 * @date: 2019/6/27
 * @description:第三方平台功能控制器 查询余额 设置密码 修改密码 转账 充值 提现
 */
@RestController
@RequestMapping("/third_party")
public class ThirdPartyController {
    @Autowired
    private P2pAccountServiceImpl p2pAccountService;

    @Autowired
    private BankAccountServiceImpl bankAccountService;

    @GetMapping("/find_balance")
    public ResultVo findBalance(HttpSession session) throws SQLException,IllegalArgumentException
    {
        ResultVo resultVo=new ResultVo();
        String thirdPartyId=(String) session.getAttribute("third_party_id");
        if (!p2pAccountService.verifyIfExists(thirdPartyId))
        {
            resultVo.setMsg(String.format("id为 %s 的账户还未创建！",thirdPartyId));
            resultVo.setCode(1);
        }
        else
        {
            Map<String,Object> data=new HashMap<String, Object>();
            BigDecimal balance=p2pAccountService.findBalance(thirdPartyId);
            resultVo.setMsg("查询成功！");
            resultVo.setCode(0);
            data.put("balance",balance);
            resultVo.setData(data);
        }
        return resultVo;
    }

    @GetMapping("/find_card")
    public ResultVo findCard(HttpSession session) throws SQLException,IllegalArgumentException
    {
        ResultVo resultVo=new ResultVo();
        String thirdPartyId=(String) session.getAttribute("third_party_id");
        BankAccount bankAccount=bankAccountService.findCardByThirdPartyId(thirdPartyId);
        if (bankAccount==null)
        {
            resultVo.setCode(1);
            resultVo.setMsg("无已绑定的银行卡！");
        }
        else
        {
            resultVo.setCode(0);
            resultVo.setMsg("查询成功！");
            Map<String,Object> data=new HashMap<String, Object>();
            data.put("card_number",bankAccount.getCardID());
            resultVo.setData(data);
        }
        return resultVo;
    }

    @DeleteMapping("/untie_bank_card")
    public ResultVo untieBankCard(HttpSession session, HttpServletRequest request) throws SQLException,IllegalArgumentException
    {
        ResultVo resultVo=new ResultVo();
        String thirdPartyId=(String) session.getAttribute("third_party_id");
        if (bankAccountService.findCardByThirdPartyId(thirdPartyId)==null)
        {
            resultVo.setCode(1);
            resultVo.setMsg("该用户未绑卡！");
        }
        else
        {
            String paymentPassword=request.getParameter("payment_password");
            String cardId=bankAccountService.findCardByThirdPartyId(thirdPartyId).getCardID();
            if (bankAccountService.verifyPassword(cardId,paymentPassword))
            {
                bankAccountService.untieBankAccount(thirdPartyId,cardId);
                resultVo.setCode(0);
                resultVo.setMsg("解绑成功！");
            }
            else
            {
                resultVo.setCode(1);
                resultVo.setMsg("密码错误！");
            }
        }
        return resultVo;
    }

    @GetMapping("/verify_password_set")
    public ResultVo verifyPasswordSet(HttpSession session) throws SQLException
    {
        ResultVo resultVo=new ResultVo();
        String thirdPartyId=(String) session.getAttribute("third_party_id");
        Map<String,Boolean> data=new HashMap<String, Boolean>();
        if (p2pAccountService.verifyPasswordIsSet(thirdPartyId))
        {
            resultVo.setMsg("支付密码已经设置");
            data.put("isSet",true);
            resultVo.setData(data);
            resultVo.setCode(0);
        }
        else
        {
            resultVo.setMsg("支付密码还未设置");
            data.put("isSet",false);
            resultVo.setData(data);
            resultVo.setCode(1);
        }
        return resultVo;
    }

    @PutMapping("/set_password")
    public ResultVo setPassword(HttpSession session, HttpServletRequest request) throws SQLException
    {
        ResultVo resultVo=new ResultVo();
        String thirdPartyId=(String) session.getAttribute("third_party_id");
        String paymentPassword=request.getParameter("payment_password");
        if (!p2pAccountService.verifyIfExists(thirdPartyId))
        {
            resultVo.setMsg(String.format("id为 %s 的账户还未创建！",thirdPartyId));
            resultVo.setCode(1);
        }
        else
        {
            p2pAccountService.updatePassword(thirdPartyId,paymentPassword);
            resultVo.setMsg("设置成功！");
            resultVo.setCode(0);
        }
        return resultVo;
    }

    @PutMapping("/modify_password")
    public ResultVo modifyPassword(HttpSession session, HttpServletRequest request) throws SQLException
    {
        ResultVo resultVo=new ResultVo();
        String thirdPartyId=(String) session.getAttribute("third_party_id");
        String oldPassword=request.getParameter("old_password");
        String newPassword=request.getParameter("new_password");
        if (!p2pAccountService.verifyIfExists(thirdPartyId))
        {
            resultVo.setMsg(String.format("id为 %s 的账户还未创建！",thirdPartyId));
            resultVo.setCode(1);
        }
        else
        {
            if (!p2pAccountService.verifyPassword(thirdPartyId,oldPassword))
            {
                resultVo.setMsg("密码错误！");
                resultVo.setCode(1);
            }
            else
            {
                p2pAccountService.updatePassword(thirdPartyId,newPassword);
                resultVo.setMsg("修改成功！");
                resultVo.setCode(0);
            }
        }
        return resultVo;
    }

    @PostMapping("/add_bank_account")
    public ResultVo addBankAccount(HttpSession session, HttpServletRequest request) throws SQLException,IllegalArgumentException
    {
        ResultVo resultVo=new ResultVo();
        String thirdPartyId=(String) session.getAttribute("third_party_id");
        String cardId=request.getParameter("card_id");
        String paymentPassword=request.getParameter("payment_password");
        if (bankAccountService.verifyCardIsAdded(cardId))
        {
            resultVo.setMsg("该卡已被别的账户添加！");
            resultVo.setCode(1);
        }
        else
        {
            bankAccountService.addBankAccount(cardId,thirdPartyId,paymentPassword,new BigDecimal(10000));
            resultVo.setMsg("添加成功！");
            resultVo.setCode(0);
        }
        return resultVo;
    }

    @PutMapping("/transfer")
    public ResultVo transfer(HttpServletRequest request) throws SQLException,IllegalArgumentException
    {
        ResultVo resultVo=new ResultVo();
        String payerId=request.getParameter("payer_id");
        String payeeId=request.getParameter("payee_id");
        BigDecimal amount=new BigDecimal(request.getParameter("amount"));
        String apiKey=p2pAccountService.getApiKey();
        String signature=request.getParameter("signature");
        String currSignature=p2pAccountService.getSHA(payerId+payeeId+request.getParameter("amount")+apiKey);
        if (currSignature.equals(signature))
        {
            Boolean result=p2pAccountService.transfer(payerId,payeeId,amount);
            if (result)
            {
                resultVo.setMsg("转账成功！");
                resultVo.setCode(0);
            }
            else
            {
                resultVo.setMsg("余额不足！");
                resultVo.setCode(1);
            }
        }
        else
        {
            resultVo.setMsg("警告！系统可能遭受攻击！信息可能遭到篡改！");
            resultVo.setCode(1);
        }
        return resultVo;
    }

    @PutMapping("/purchase")
    public ResultVo purchase(HttpServletRequest request) throws SQLException,IllegalArgumentException
    {
        ResultVo resultVo=new ResultVo();
        String payerId=request.getParameter("payer_id");
        String payeeId=request.getParameter("payee_id");
        BigDecimal amount=new BigDecimal(request.getParameter("amount"));
        String paymentPassword=request.getParameter("payment_password");
        if (p2pAccountService.verifyPassword(payerId,paymentPassword))
        {
            Boolean result=p2pAccountService.transfer(payerId,payeeId,amount);
            if (result)
            {
                resultVo.setMsg("购买成功！");
                resultVo.setCode(0);
            }
            else
            {
                resultVo.setMsg("余额不足！");
                resultVo.setCode(1);
            }
        }
        else
        {
            resultVo.setMsg("密码错误！");
            resultVo.setCode(1);
        }
        return resultVo;
    }

    @PutMapping("/recharge")
    public ResultVo recharge(HttpSession session,HttpServletRequest request) throws SQLException,IllegalArgumentException
    {
        ResultVo resultVo=new ResultVo();
        String thirdPartyId=(String) session.getAttribute("third_party_id");
        BigDecimal amount=new BigDecimal(request.getParameter("amount"));
        String password=request.getParameter("payment_password");
        String cardId=bankAccountService.findCardByThirdPartyId(thirdPartyId).getCardID();
        if (bankAccountService.findCardByThirdPartyId(thirdPartyId)==null)
        {
            resultVo.setCode(1);
            resultVo.setMsg("您未绑定银行卡！");
            return resultVo;
        }
        if (bankAccountService.verifyPassword(cardId,password))
        {
            Boolean result=p2pAccountService.recharge(thirdPartyId,cardId,amount);
            if (result)
            {
                resultVo.setCode(0);
                resultVo.setMsg("充值成功!");
            }
            else
            {
                resultVo.setCode(1);
                resultVo.setMsg("余额不足!");
            }
        }
        else
        {
            resultVo.setMsg("密码错误!");
            resultVo.setCode(1);
        }
        return resultVo;
    }

    @PutMapping("/withdraw")
    public ResultVo withdraw(HttpSession session, HttpServletRequest request) throws SQLException,IllegalArgumentException
    {
        ResultVo resultVo=new ResultVo();
        String thirdPartyId=(String) session.getAttribute("third_party_id");
        String cardId=bankAccountService.findCardByThirdPartyId(thirdPartyId).getCardID();
        BigDecimal amount=new BigDecimal(request.getParameter("amount"));
        String password=request.getParameter("payment_password");
        if (bankAccountService.findCardByThirdPartyId(thirdPartyId)==null)
        {
            resultVo.setCode(1);
            resultVo.setMsg("您未绑定银行卡！");
            return resultVo;
        }
        if (p2pAccountService.verifyPassword(thirdPartyId,password))
        {
            Boolean result=p2pAccountService.withdraw(thirdPartyId,cardId,amount);
            if (result)
            {
                resultVo.setCode(0);
                resultVo.setMsg("提现成功!");
            }
            else
            {
                resultVo.setCode(1);
                resultVo.setMsg("余额不足！");
            }
        }
        else
        {
            resultVo.setCode(1);
            resultVo.setMsg("密码错误!");
        }
        return resultVo;
    }
}