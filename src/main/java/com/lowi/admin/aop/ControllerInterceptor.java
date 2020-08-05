package com.lowi.admin.aop;


import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.lowi.admin.utils.LoginValidaUtils;
import com.lowi.admin.utils.Result;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.lang.reflect.Field;
import java.util.*;

@Aspect
@Component
public class ControllerInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 定义拦截规则：拦截com.lyx.oaserver.controller包下面的所有类中，有@RequestMapping注解的方法。
     */
    @Pointcut("execution(* com.lowi.admin.controller..*(..))")
    public void controllerMethodPointcut() {
    }

    @Around(value = "controllerMethodPointcut()")
    public Result invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        Result result;
        result = (Result) joinPoint.proceed();
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String requestURI = request.getRequestURI();
            System.out.println("requestURI = " + requestURI);
            boolean contains = LIST.contains(requestURI);
            if (contains) {
                return result;
            }
            // 构造参数组集合
            List<Object> argList = new ArrayList<>();
            for (Object arg : joinPoint.getArgs()) {
                if (arg instanceof HttpServletRequest) {
                } else if (arg instanceof HttpServletResponse) {
                } else {
                    argList.add(JSONUtil.toJsonStr(arg));
                }
            }
            Object o = argList.get(0);
            String valueOf = String.valueOf(o);
            boolean json = JSONUtil.isJson(valueOf);
            String token = null;
            if (json) {
                JSON parse = JSONUtil.parse(o);
                token = (String) parse.getByPath("token");
                boolean loginValida = LoginValidaUtils.loginValida(token);
                if (!loginValida) {
                    result.setCode(1);
                    result.setMsg("请登录");
                    return result;
                }
            } else {
                token = valueOf;
            }
            if (token == null) {
                logger.info("请求方法-->{},token为空,请登录", requestURI);
                result.setCode(1);
                result.setMsg("请登录");
                return result;
            }
            boolean loginValida = LoginValidaUtils.loginValida(token);
            if (!loginValida) {
                logger.info("请求方法-->{},token验证失败,请登录", requestURI);
                result.setCode(1);
                result.setMsg("请登录");
                return result;
            }
            logger.info("请求方法-->{},请求参数-->{}", requestURI, JSONUtil.toJsonStr(argList));
            logger.info("请求方法-->{},请求返回参数-->{}", requestURI, JSONUtil.toJsonStr(result));
        } catch (Exception e) {
            logger.error("参数获取失败: {}", e.getMessage());
        }
        return result;
    }

    private final static List<String> LIST = Arrays.asList("/user/login", "/user/logout","/product/upload","/es/test","/test/test1");

}
