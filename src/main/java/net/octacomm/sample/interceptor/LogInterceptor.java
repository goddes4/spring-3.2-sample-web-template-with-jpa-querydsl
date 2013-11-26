package net.octacomm.sample.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.octacomm.logger.Log;

import org.slf4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LogInterceptor extends HandlerInterceptorAdapter {

	@Log
	private Logger logger;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		logger.info("Request URL : {}", 
				request.getAttribute("org.springframework.web.servlet.HandlerMapping.pathWithinHandlerMapping"));
		return true;
	}

}
