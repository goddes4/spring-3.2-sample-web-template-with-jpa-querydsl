package net.octacomm.sample.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	private static final String LOGIN_URL = "/login";
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		HttpSession session = request.getSession(true);

		if (!isLogin(session)) {
			response.sendRedirect(request.getContextPath() + LOGIN_URL);
			return false;
		} else {
			return true;
		}
	}

	private boolean isLogin(HttpSession session) {
		String memberId = (String) session.getAttribute("userId");
		return memberId != null;
	}

}