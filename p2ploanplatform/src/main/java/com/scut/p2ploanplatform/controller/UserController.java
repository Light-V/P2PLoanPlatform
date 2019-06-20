package com.scut.p2ploanplatform.controller;


import com.scut.p2ploanplatform.vo.ResultVo;
import com.scut.p2ploanplatform.entity.User;
import com.scut.p2ploanplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;


/**
 * @author: zrh
 * @date: 2019/6/20
 * @description:用户登录注册功能控制器
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public ResultVo login(HttpServletRequest request, HttpSession session)
    throws SQLException  {
        ResultVo vo = new ResultVo();
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        User user = userService.findUser(userId);
        if (user != null ) {
            if( user.getPassword().equals(password) ){
                session.setAttribute("user", userId);
                vo.setData(userId);
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
        return vo;
    }

    @RequestMapping("/signup")
    public ResultVo signup(HttpServletRequest request )throws SQLException {
        ResultVo vo = new ResultVo();

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String departmentId = request.getParameter("departmentId");
        String phone = request.getParameter("phone");
        String idCard = request.getParameter("idCard");
        String thirdPartyId = request.getParameter("thirdPartyId");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        int departmentid = Integer.parseInt(departmentId);
        if(phone.length() != 11){
            vo.setCode(1);
            vo.setMsg("手机号码不符合规范");
            return vo;
        } else {
            int success = userService.insertUser(userId,departmentid,password,phone,idCard,thirdPartyId,name,address);
            if(success == 1){
                vo.setCode(0);
                vo.setMsg("成功");
            } else {
                vo.setCode(1);
                vo.setMsg("用户已存在");
            }
        }
        return vo;
    }

}
