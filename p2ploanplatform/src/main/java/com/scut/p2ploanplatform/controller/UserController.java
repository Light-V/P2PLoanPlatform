package com.scut.p2ploanplatform.controller;


import com.scut.p2ploanplatform.entity.Guarantor;
import com.scut.p2ploanplatform.service.GuarantorService;
import com.scut.p2ploanplatform.service.P2pAccountService;
import com.scut.p2ploanplatform.vo.ResultVo;
import com.scut.p2ploanplatform.entity.User;
import com.scut.p2ploanplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * @author: zrh
 * @date: 2019/6/20
 * @description:用户功能控制器,功能包括 注册 登录 修改密码 修改资料
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private P2pAccountService p2pAccountService;
    @Autowired
    private GuarantorService guarantorService;

    @RequestMapping("/login")
    public ResultVo login(HttpServletRequest request, HttpSession session)
    throws SQLException  {
        ResultVo vo = new ResultVo();
        String userId = request.getParameter("user_id");
        String password = request.getParameter("password");
        String userType = request.getParameter("user_type");
        int type = Integer.parseInt(userType);
        if(type==1){
            User user = userService.findUser(userId);
            if (user != null ) {
                if( user.getPassword().equals(password) ){
                    session.setAttribute("user", userId);
                    session.setAttribute("user_type",userType);
                    session.setAttribute("third_party_id", user.getThirdPartyId());
                    Map<String,Object> data=new HashMap<String,Object>();
                    data.put("user_id",user.getUserId());
                    data.put("third_party_id",user.getThirdPartyId());
                    vo.setData(data);
                    vo.setCode(0);
                    vo.setMsg("登录成功");
                } else {
                    vo.setCode(1);
                    vo.setMsg("密码错误");
                }
            } else {
                vo.setCode(1);
                vo.setMsg("账号不存在");
            }
        } else {
            Guarantor user = guarantorService.findGuarantor(userId);
            if (user != null ) {
                if( user.getPassword().equals(password) ){
                    session.setAttribute("user", userId);
                    session.setAttribute("user_type",userType);
                    session.setAttribute("third_party_id", user.getThirdPartyId());
                    Map<String,Object> data=new HashMap<String,Object>();
                    data.put("user_id",user.getGuarantorId());
                    data.put("third_party_id",user.getThirdPartyId());
                    vo.setData(data);
                    vo.setCode(0);
                    vo.setMsg("登录成功");
                } else {
                    vo.setCode(1);
                    vo.setMsg("密码错误");
                }
            } else {
                vo.setCode(1);
                vo.setMsg("账号不存在");
            }
        }
        return vo;
    }

    @RequestMapping("/logout")
    public ResultVo logout(HttpServletRequest request, HttpSession session) {
        ResultVo vo = new ResultVo();
        session.invalidate();
        vo.setCode(0);
        vo.setMsg("退出账号成功");
        return vo;
    }

    @RequestMapping("/signup")
    public ResultVo signup(HttpServletRequest request )throws SQLException {
        ResultVo vo = new ResultVo();

        String userId = request.getParameter("user_id");
        String password = request.getParameter("password");
        String passwordRepeat = request.getParameter("password_repeat");
        String departmentId = request.getParameter("department_id");
        String phone = request.getParameter("phone");
        String idCard = request.getParameter("id_card");
        String thirdPartyId = request.getParameter("third_party_id");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        int departmentIdInt = Integer.parseInt(departmentId);

        if( userId == null || userId.equals("")){
            vo.setCode(1);
            vo.setMsg("错误！账号为空。");
            return vo;
        } else if(userId.length()!= 12) {
            vo.setCode(1);
            vo.setMsg("错误！账号不符合规范。");
            return vo;
        }
        if(  password == null || password.equals("")){
            vo.setCode(1);
            vo.setMsg("错误！密码为空。");
            return vo;
        } else if(!password.equals(passwordRepeat)) {
            vo.setCode(1);
            vo.setMsg("错误！密码与重复输入不符。");
            return vo;
        }
        if( departmentId == null||departmentId.equals("")){
            vo.setCode(1);
            vo.setMsg("错误！部门编号为空。");
            return vo;
        }
        if( phone == null||phone.equals("")){
            vo.setCode(1);
            vo.setMsg("错误！联系方式为空。");
            return vo;
        } else if(!isMobile(phone)){
            vo.setCode(1);
            vo.setMsg("手机号码不符合规范");
            return vo;
        }
        if(idCard == null||idCard.equals("")){
            vo.setCode(1);
            vo.setMsg("错误！身份证号码为空。");
            return vo;
        } else if(!isIDNumber(idCard)){
            vo.setCode(1);
            vo.setMsg("身份证号码不符合规范");
            return vo;
        }
        if(thirdPartyId == null||thirdPartyId.equals("")){
            vo.setCode(1);
            vo.setMsg("错误！第三方账号为空。");
            return vo;
        } else if(thirdPartyId.length()!=12) {
            vo.setCode(1);
            vo.setMsg("错误！第三方账号不符合规范。");
            return vo;
        } else if(p2pAccountService.verifyIfExists(thirdPartyId))
        {
            vo.setCode(1);
            vo.setMsg("错误！第三方账号已存在。");
            return vo;
        }
        if(name == null||name.equals("")){
            vo.setCode(1);
            vo.setMsg("错误！姓名为空。");
            return vo;
        }
        if(address == null||address.equals("")){
            vo.setCode(1);
            vo.setMsg("错误！地址为空。");
            return vo;
        }

        int success = userService.insertUser(userId,departmentIdInt,password,phone,idCard,thirdPartyId,name,address);
        if(success == 1){
            success = p2pAccountService.addP2pAccount(thirdPartyId,"",new BigDecimal(10000),1,0);
            if(success==1) {
                vo.setCode(0);
                vo.setMsg("成功");
            } else {
                userService.deleteUser(userId);
                vo.setCode(1);
                vo.setMsg("创建第三方账户失败");
            }
        } else {
            vo.setCode(1);
            vo.setMsg("用户已存在");
        }
        return vo;
    }

    @RequestMapping("/update_password")
    public ResultVo updataPassword(HttpServletRequest request, HttpSession session)throws SQLException {
        ResultVo vo = new ResultVo();
        String userId = (String) session.getAttribute("user");
        String old_password = request.getParameter("old_password");
        String password = request.getParameter("password");
        String passwordRepeat = request.getParameter("password_repeat");
        User user = userService.findUser(userId);
        if(  old_password == null || old_password.equals("")){
            vo.setCode(1);
            vo.setMsg("错误！密码为空。");
            return vo;
        } else if(!old_password.equals(user.getPassword())) {
            vo.setCode(1);
            vo.setMsg("错误！原密码与不正确。");
            return vo;
        }

        if(  password == null || password.equals("")){
            vo.setCode(1);
            vo.setMsg("错误！密码为空。");
            return vo;
        } else if(!password.equals(passwordRepeat)) {
            vo.setCode(1);
            vo.setMsg("错误！密码与重复输入不符。");
            return vo;
        }

        int success = userService.updatePassword(userId,password);
        if(success == 1){
            vo.setCode(0);
            vo.setMsg("成功");
        } else {
            vo.setCode(1);
            vo.setMsg("修改失败");
        }
        return vo;
    }

        @RequestMapping("/update_user")
    public ResultVo updataUser(HttpServletRequest request, HttpSession session)throws SQLException {
        ResultVo vo = new ResultVo();

        String userId = (String) session.getAttribute("user");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        if( phone == null||phone.equals("")){
            vo.setCode(1);
            vo.setMsg("错误！联系方式为空。");
            return vo;
        } else if(!isMobile(phone)){
            vo.setCode(1);
            vo.setMsg("手机号码不符合规范");
            return vo;
        }
        if(address == null||address.equals("")){
            vo.setCode(1);
            vo.setMsg("错误！地址为空。");
            return vo;
        }

        int success = userService.updateUser(userId,phone,address);
        if(success == 1){
            vo.setCode(0);
            vo.setMsg("成功");
        } else {
            vo.setCode(1);
            vo.setMsg("修改失败。");
        }
        return vo;
    }

    @RequestMapping("/user_message")
    public ResultVo getUserMessage(HttpServletRequest request, HttpSession session)throws SQLException {
        ResultVo vo = new ResultVo();

        String userId = (String) session.getAttribute("user");
        User user = userService.findUser(userId);
        if(user == null){
            vo.setCode(1);
            vo.setMsg("找不到用户");
        } else {
            vo.setCode(0);
            vo.setMsg("成功");
            Map<String,Object> data=new HashMap<String,Object>();
            data.put("user_id",user.getUserId());
            data.put("name",user.getName());
            data.put("address",user.getAddress());
            data.put("phone",user.getPhone());
            data.put("id_card",user.getIdCard());
            data.put("department_id",user.getDepartmentId());
            data.put("third_party_id",user.getThirdPartyId());
            vo.setData(data);
        }
        return vo;
    }
    private boolean isIDNumber(String IDNumber) {
        if (IDNumber == null || "".equals(IDNumber)) {
            return false;
        }
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        boolean matches = IDNumber.matches(regularExpression);

        if (matches) {
            if (IDNumber.length() == 18) {
                try {
                    char[] charArray = IDNumber.toCharArray();
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        System.out.println("身份证最后一位:" + String.valueOf(idCardLast).toUpperCase() +
                                "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase());
                        return false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("异常:" + IDNumber);
                    return false;
                }
            }
        }
        return matches;
    }

    private boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean isMatch = false;
        String regex1 = "^[1][3,4,5,7,8][0-9]{9}$";
        String regex2 = "^((13[0-9])|(14[579])|(15([0-3,5-9]))|(16[6])|(17[0135678])|(18[0-9]|19[89]))\\d{8}$";

        p = Pattern.compile(regex2);
        m = p.matcher(str);
        isMatch = m.matches();
        return isMatch;
    }
}



