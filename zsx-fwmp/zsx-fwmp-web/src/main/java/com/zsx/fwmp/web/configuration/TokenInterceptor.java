package com.zsx.fwmp.web.configuration;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.others.jwt.JWT;
import com.zsx.model.pojo.SysUser;

public class TokenInterceptor implements HandlerInterceptor{

    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception arg3)
            throws Exception {
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView model) throws Exception {
    }

    //拦截每个请求
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
    	Log.debug("欢迎来到拦截器", TokenInterceptor.class);
        response.setCharacterEncoding("utf-8");
        String token = request.getParameter("token");
        //token不存在
        if(null != token) {
            SysUser login = JWT.unsign(token, SysUser.class);
            String loginId = request.getParameter("loginId");
            //解密token后的loginId与用户传来的loginId不一致，一般都是token过期
            if(null != loginId && null != login) {
                if(Integer.parseInt(loginId) == login.getId()) {
                    return true;
                }
                else{
                    responseMessage(response, response.getWriter());
                    return false;
                }
            }
            else
            {
                responseMessage(response, response.getWriter());
                return false;
            }
        }
        else
        {
            responseMessage(response, response.getWriter());
            return false;
        }
    }

    //请求不通过，返回错误信息给客户端
    private void responseMessage(HttpServletResponse response, PrintWriter out) {
        response.setContentType("application/json; charset=utf-8");  
        out.print("---------------------token错误");
        out.flush();
        out.close();
    }

}