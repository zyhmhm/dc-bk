package top.dc.bk.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.dc.bk.annotationn.RequiredLog;
import top.dc.bk.service.BkLogsService;
import top.dc.bk.utils.IPUtils;
import top.dc.bk.utils.ShiroUtils;
import top.dc.pojo.bk.pojo.BkLogs;

import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Slf4j
@Component
public class LogAspect {

    //@Pointcut注解用于描述方法,定义切入点
    //bean(sysUserServiceImpl)为一种切入点表达式
    //sysUserServiceImpl为spring容器中的一个bean的名字
    //切入点表达式中的bean表达式
    //@Pointcut("bean(sysUserServiceImpl)")
    //@Pointcut("bean("*ServiceImpl")
    //切入点表达式中的注解表达式
    //由此注解描述的方法作为切入点.
//    @Pointcut("@annotation(com.cy.pj.common.annotation.RequiredLog)")
//    public void logPointCut() {}

    //@Around为一个环绕通知
    //特点
    //1)可以在目标方法执行之前和之后添加自己的拓展业务
    //2)方法返回值类型Object
    //3)方法参数类型为ProceedingJoinPoint,此对象中封装了目标方法信息
    //4)方法异常抛出为Throwable类型
    @Around("@annotation(top.dc.bk.annotationn.RequiredLog)")
    public Object around(ProceedingJoinPoint jp) throws Throwable {

        //1.记录目标方法开始执行时间
        long start = System.currentTimeMillis();
        log.info("start:" + start);
        //获取方法名称
        String methodName = jp.getSignature().getName();
        log.info("Method Name: " + methodName);
        ////2.执行目标方法并获取结果
        Object result = jp.proceed();
        //3.记录目标方法结束执行时间
        long after = System.currentTimeMillis();
        log.info("after:" + after);
        //记录用户行为日志
        saveLog(jp,(after-start));
        return result;
    }
    @Autowired
    private BkLogsService logsService;


    //获取用户行为日志信息,然后将日志写入到数据库
    private void saveLog(ProceedingJoinPoint joinPoint,long time)throws Exception {
        //1.获取日志信息
        //获取目标方法对象
        Method targetMethod = getTargetMethod(joinPoint);
        //获取目标方法名:目标类全名+方法名
        String classMethodName=getTargetMethodName(targetMethod);
        //获取操作名
        String operation= getOperation(targetMethod);
        //String params=Arrays.toString(joinPoint.getArgs());
        //获取方法执行时的实际参数
        String params=//借助了jackson中的api
                new ObjectMapper().writeValueAsString(joinPoint.getArgs());
        //2.封装日志信息
        BkLogs bkLog=new BkLogs(null,
                ShiroUtils.getUsername(),//登录用户名
                operation,
                classMethodName,
                params,
                time,
                IPUtils.getIpAddr());
        bkLog.setCreateTime(new Date());
        //3.持久化日志信息
//	    new Thread() {
//	    	public void run() {
//	    		logService.saveObject(sysLog);
//	    	};
//	    }.start();
        logsService.saveObject(bkLog);
    }

    private String getOperation(Method targetMethod) {
        RequiredLog rLog=
                targetMethod.getAnnotation(RequiredLog.class);
        return rLog.value();
    }

    private String getTargetMethodName(Method targetMethod) {
        return targetMethod.getDeclaringClass().getName()
                +"."+targetMethod.getName();
    }

    private Method getTargetMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Class<?> targetClass=joinPoint.getTarget().getClass();
        MethodSignature s=(MethodSignature)joinPoint.getSignature();//方法签名
        Method targetMethod=
                targetClass.getMethod(
                        s.getName(),
                        s.getParameterTypes());
        return targetMethod;
    }
}

