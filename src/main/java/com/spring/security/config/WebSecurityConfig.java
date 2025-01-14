package com.spring.security.config;

import com.spring.security.config.handler.*;
import com.spring.security.config.provider.CodeProvider;
import com.spring.security.config.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.annotation.Resource;

/**
 * @Author: Hutengfei
 * @Description:
 * @Date Create in 2019/8/28 20:15
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{


    @Resource
    private UserDetailsServiceImpl userDetailsService;

    //登录成功处理逻辑
    @Resource
    private CustomizeAuthenticationSuccessHandler authenticationSuccessHandler;

    //登录失败处理逻辑
    @Resource
    private CustomizeAuthenticationFailureHandler authenticationFailureHandler;

    //权限拒绝处理逻辑
    @Resource
    private CustomizeAccessDeniedHandler accessDeniedHandler;

    //匿名用户访问无权限资源时的异常
    @Resource
    private CustomizeAuthenticationEntryPoint authenticationEntryPoint;

    //会话失效(账号被挤下线)处理逻辑
    @Resource
    private CustomizeSessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    //登出成功处理逻辑
    @Resource
    private CustomizeLogoutSuccessHandler logoutSuccessHandler;

    //访问决策管理器
    @Resource
    private CustomizeAccessDecisionManager accessDecisionManager;

    //实现权限拦截
    @Resource
    private CustomizeFilterInvocationSecurityMetadataSource securityMetadataSource;

    @Resource
    private CustomizeAbstractSecurityInterceptor securityInterceptor;


    @Bean
    CodeProvider codeProvider() {
        CodeProvider codeProvider = new CodeProvider();
        codeProvider.setPasswordEncoder(passwordEncoder());
        codeProvider.setUserDetailsService(userDetailsService);
        return codeProvider;
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 设置默认的加密方式（强hash方式加密）
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //配置认证方式
        auth.userDetailsService(userDetailsService);
    }
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(codeProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ////http相关的配置，包括登入登出、异常处理、会话管理等
        http.cors().and().csrf().disable();
        http.authorizeRequests().
                //antMatchers("/getUser").hasAuthority("query_user").
                //antMatchers("/**").fullyAuthenticated()
                /**
                 * 这里自定义决策管理器和安全数据源 来实现spring-security使用我们自己数据库的权限列表
                  */
                withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        //决策管理器
                        o.setAccessDecisionManager(accessDecisionManager);
                        //安全元数据源
                        o.setSecurityMetadataSource(securityMetadataSource);
                        return o;
                    }
                }).
                //登出
                and().logout().
                    permitAll().//允许所有用户
                    logoutSuccessHandler(logoutSuccessHandler).//登出成功处理逻辑
                    deleteCookies("JSESSIONID").//登出之后删除cookie
                //登入
                and().formLogin()
                    .loginPage("/nrsc-login.html")
                .loginProcessingUrl("/login")
                    .permitAll().//允许所有用户
                    successHandler(authenticationSuccessHandler).//登录成功处理逻辑
                    failureHandler(authenticationFailureHandler).//登录失败处理逻辑
                //异常处理(权限拒绝、登录失效等)
                and().exceptionHandling().
                    accessDeniedHandler(accessDeniedHandler).//权限拒绝处理逻辑
                /**
                 * authenticationEntryPoint是从客户端请求凭据 这里利用这个特点 直接返回没有权限
                 */
                    //authenticationEntryPoint(authenticationEntryPoint).//匿名用户访问无权限资源时的异常处理
                //会话管理
                and().sessionManagement().
                    maximumSessions(1).//同一账号同时登录最大用户数
                    expiredSessionStrategy(sessionInformationExpiredStrategy);//会话失效(账号被挤下线)处理逻辑
        http.addFilterBefore(securityInterceptor, FilterSecurityInterceptor.class);
    }
}
