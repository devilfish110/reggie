package com.study.reggie.filter;

import com.alibaba.fastjson2.JSON;
import com.study.reggie.common.BaseContext;
import com.study.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author That's all
 * 检查后端用户是否登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    /**
     * 路径匹配器，支持通配符
     */
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUri = request.getRequestURI();
        log.info("拦截到的请求:{}", requestUri);
        //判断urI是否拦截
        if (checkUrI(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }
        //判断后端用户登录了没
        Long employeeId = (Long) request.getSession().getAttribute("employee");
        if (employeeId != null) {
            //半小时后重新登陆
            request.getSession().setMaxInactiveInterval(30 * 60);
            //存入线程中后controller使用
            BaseContext.setCurrentId(employeeId);
            filterChain.doFilter(request, response);
            return;
        }
        //判断前端用户登陆了没
        Long userId = (Long) request.getSession().getAttribute("user");
        if (userId != null) {
            //半小时后重新登陆
            request.getSession().setMaxInactiveInterval(30 * 60);
            //存入线程中后controller使用
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }
        //没有登陆，则返回R给前端判断
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配，判断是否放行
     *
     * @param requestUri 请求的URI
     * @return
     */
    public boolean checkUrI(String requestUri) {
        //不拦截的请求
        String[] urls = new String[]{
                //后端放行的请求
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/common/**",
                //前端的放行
                "/user/sendemail",
                "/user/login",
                "/front/**"
        };
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUri);
            //判断是否匹配urls的内容
            if (match) {
                return true;
            }
        }
        return false;
    }
}
