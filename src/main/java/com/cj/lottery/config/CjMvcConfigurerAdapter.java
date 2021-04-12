package com.cj.lottery.config;

import com.cj.lottery.aop.LoginInterceptor;
import com.cj.lottery.aop.TraceIdInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Slf4j
@Configuration
public class CjMvcConfigurerAdapter implements WebMvcConfigurer {

    private static final String PREFIX_PATH_SEPARATOR = "/api/**";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(traceIdInterceptor()).addPathPatterns(PREFIX_PATH_SEPARATOR);

        //登录验证
        registry.addInterceptor(loginInterceptor())
//                .addPathPatterns(PREFIX_PATH_SEPARATOR)
//                .excludePathPatterns("/api/cj/test")
//                .excludePathPatterns("/api/cj/login/**")
//                .addPathPatterns("/api/cj/draw/**")
                .addPathPatterns("/api/pay/**")
                .addPathPatterns("/api/prize/send-prize")
                .excludePathPatterns("/api/pay/callbacks/**");


    }

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Bean
    public TraceIdInterceptor traceIdInterceptor(){
        return new TraceIdInterceptor();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").
                allowedOriginPatterns("*").
                allowCredentials(true).
                allowedMethods("*").
                maxAge(3600);
    }

}
