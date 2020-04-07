package com.chanshiyu.yuko.component;

import com.chanshiyu.yuko.model.vo.CommonResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author SHIYU
 * @since 2020/3/31 16:49
 * @description HibernateValidator 错误结果处理切面
 */
@Aspect
@Component
@Order(2)
public class BindingResultAspect {

    @Pointcut("execution(public * com.chanshiyu.yuko.controller.*.*(..))")
    public void BindingResult() {
    }

    @Around("BindingResult()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult result = (BindingResult) arg;
                if (result.hasErrors()) {
                    FieldError fieldError = result.getFieldError();
                    if (fieldError != null) {
                        return CommonResult.validateFailed(fieldError.getDefaultMessage());
                    } else {
                        return CommonResult.validateFailed("参数验证错误");
                    }
                }
            }
        }
        return joinPoint.proceed();
    }

}
