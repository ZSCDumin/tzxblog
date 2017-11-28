package com.tzx.blog.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tzx.blog.model.Userinfo;
import com.tzx.blog.service.UserService;

/**
 * user控制层
 * 
 * @author tzx
 *
 */
@Controller
@RequestMapping("tzxblog")
public class UserController {
	@Autowired
	private UserService userService;

	/**
	 * 用户登录页面
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping("/loginpage")
	public String loginpage() {
		return "login";
	}

	/**
	 * 用户登录请求：@ResponseBody可以在使用template的情况下返回普通数据
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @param map
	 * @return 0:登录成功，1：用户名不能为空，2：用户名不存在，3：密码错误
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public int login(@RequestBody Userinfo user, HttpServletRequest request) {
		if (user == null || user.getUserAccount() == null || "".equals(user.getUserAccount())) {
			return 1;
		}
		Userinfo userinfo = userService.findUserByAccount(user.getUserAccount());
		if (userinfo == null) {
			return 2;
		} else if (!(userinfo.getUserPassword().equals(user.getUserPassword()))) {
			return 3;
		}
		request.getSession().setAttribute("user", userinfo);
		return 0;
	}

	/**
	 * 注册页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "registpage")
	public String registpage() {
		return "regist";
	}

	@RequestMapping(value = "regist", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Object regist(@RequestBody Userinfo user, HttpServletRequest request) {
		Map<String, Object> map = userService.addUser(user, request);
		return map;
	}

	@RequestMapping(value = "logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("user");
		try {
			response.sendRedirect("/tzxblog");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
