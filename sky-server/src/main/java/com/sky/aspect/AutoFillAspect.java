package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import org.aspectj.lang.annotation.Aspect;
/**
 * 自动填充切面
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")

    public void autoFillPointcut() {

    }
    /**
     * 前置通知 在通知中执行公共字段的赋值
     */
    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
         log.info("开始进行公共字段的填充");
         //获取数据库的操作类型
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();//获取方法签名
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解
        OperationType operationType = autoFill.value();//获得注解上的值
        //获取当前被拦截的方法参数
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object entity = args[0];
        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        //根据操作类型进行赋值
        if(OperationType.INSERT.equals(operationType)){
            //如果是新增操作，需要进行赋值
            //如果是更新操作，需要进行赋值
            Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUserId = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            Method setCreateUserId = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            //调用方法进行赋值
            setCreateTime.invoke(entity,now);
            setUpdateTime.invoke(entity,now);
            setUpdateUserId.invoke(entity,currentId);
            setCreateUserId.invoke(entity,currentId);

        }else if(OperationType.UPDATE.equals(operationType)){
            //如果是更新操作，需要进行赋值
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUserId = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            //调用方法进行赋值
            setUpdateTime.invoke(entity,now);
            setUpdateUserId.invoke(entity,currentId);
        }
    }
}
