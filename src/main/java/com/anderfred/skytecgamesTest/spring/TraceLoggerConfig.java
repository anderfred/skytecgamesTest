package com.anderfred.skytecgamesTest.spring;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class TraceLoggerConfig {

    @Bean
    public CustomizableTraceInterceptor customizableTraceInterceptor() {
        CustomizableTraceInterceptor customizableTraceInterceptor = new CustomizableTraceInterceptor();
        customizableTraceInterceptor.setUseDynamicLogger(true);
        customizableTraceInterceptor.setEnterMessage("Entering $[methodName]($[arguments])");
        customizableTraceInterceptor.setExitMessage("Leaving  $[methodName](), returned $[returnValue]");
        return customizableTraceInterceptor;
    }

    @Bean
    public Advisor jpaRepositoryAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public *  org.springframework.data.repository.CrudRepository+.*(..))");
        return new DefaultPointcutAdvisor(pointcut, customizableTraceInterceptor());
    }

}