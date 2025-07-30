package com.hxc.aicode.aop;

import com.hxc.aicode.annotation.AuthCheck;
import com.hxc.aicode.exception.BusinessException;
import com.hxc.aicode.exception.ErrorCode;
import com.hxc.aicode.model.entity.User;
import com.hxc.aicode.model.enums.UserRoleEnum;
import com.hxc.aicode.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * z执行拦截
     * @param joinPoint
     * @param authCheck
     * @return
     * @throws Throwable
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        //获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        //不需要权限，直接放行
        if(mustRoleEnum == null){
            return joinPoint.proceed();
        }
        //需要权限，判断用户角色是否满足
        UserRoleEnum loginUserRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        //没有权限，直接拒绝
        if(loginUserRoleEnum == null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //要求必须有管理员权限,且用户没有权限
        if(UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(loginUserRoleEnum)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
//        //要求必须有普通用户权限,且用户没有权限
//        if(UserRoleEnum.USER.equals(mustRoleEnum) && !UserRoleEnum.USER.equals(loginUserRoleEnum)){
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
        //通过普通用户的权限校验，放行
        return joinPoint.proceed();
    }
}
