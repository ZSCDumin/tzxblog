package com.tzx.blog.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tzx.blog.model.Userinfo;

/**
 * 自定义过滤器
 * 
 * @author tzx
 *
 */
@WebFilter
public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request2 = (HttpServletRequest) request;
		HttpSession session = request2.getSession();
		String url = request2.getRequestURI();
		// 注册和首页直接放行,静态文件直接放行
		System.out.println(url.contains("."));
		if (url.endsWith("registpage") || url.endsWith("tzxblog") || url.contains(".")) {
			chain.doFilter(request, response);
		} else {
			Userinfo userinfo = (Userinfo) session.getAttribute("user");
			// 如果已登录或用户名不存在，均跳转到首页
			System.out.println(userinfo != null && userinfo.getUserName() != null && !url.endsWith("login")
					&& !url.endsWith("loginpage"));
			if ((userinfo != null && userinfo.getUserName() != null
					&& (!url.endsWith("login") && !url.endsWith("loginpage")))
					|| ((userinfo == null || userinfo.getUserName() == null)
							&& (url.endsWith("login") || url.endsWith("loginpage")))) {
				chain.doFilter(request, response);
			} else {
				((HttpServletResponse) response).sendRedirect("/tzxblog");
			}
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
