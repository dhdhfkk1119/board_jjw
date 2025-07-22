package com.jjw.tboard._core.config;

import com.jjw.tboard._core.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Ioc 처리 (싱글톤 패턴 관리)
@RequiredArgsConstructor
public class WebMcvConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

   @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/board/**", "/recruit/**")
                .excludePathPatterns(
                        "/recruit/{id:\\d+}",
                        "/member/**",
                        "/sign-form",
                        "/member/login",
                        "/member/sign",
                        "/corp/corp-sign",
                        "/corp/login",
                        "/corp/login-form",
                        "/login-form",
                        "/logout",
                        "/board/comments",
                        "/board/list",
                        "/corp/{id:\\d+}/update"
                );

    }
}
