package top.dc.bk.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class SpringShiroConfig {
    /**
     * Shrio核心对象
     * @return
     */
    @Bean
    public SecurityManager securityManager(Realm realm, SessionManager sessionManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Autowired SecurityManager securityManager){
        ShiroFilterFactoryBean sfBean = new ShiroFilterFactoryBean();
        sfBean.setSecurityManager(securityManager);
        sfBean.setLoginUrl("/unAuth");
        //定义map指定请求过滤规则（哪些资源允许匿名访问，哪些资源不允许）
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        //允许匿名访问的资源anno
        map.put("/bkUser/doLogin","anon");
        map.put("/doLogout","logout");
        //出来上面的资源之外其他的都需要认证
        map.put("/**","authc");
        sfBean.setFilterChainDefinitionMap(map);
        return sfBean;
    }

    @Bean
    public DefaultWebSessionManager sessionManager(@Autowired @Qualifier("sessionIdCookie")Cookie cookie){
        DefaultWebSessionManager sManager = new DefaultWebSessionManager();
        sManager.setSessionIdCookie(cookie);
        sManager.setGlobalSessionTimeout(60*60*1000);
        return sManager;
    }

    @Bean("sessionIdCookie")
    public SimpleCookie simpleCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("JSESSIONID");
        simpleCookie.setHttpOnly(false);
        return simpleCookie;
    }

    /**
     * 以下是shrio权限授权所需要的配置
     */

    /**
     * 1.配置bean对象生命周期管理
     */

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 2.一下配置为目标对象创建代理对象（spring boot可以省略）
     */

//    @DependsOn("lifecycleBeanPostProcessor")
//    @Bean
//    public DefaultAdvisorAutoProxyCreator newDefaultAdvisorAutoProxyCreator(){
//        return new DefaultAdvisorAutoProxyCreator();
//    }

    /**
     * 3.配置advisor对象，shiro框架底层会通过此对象的matchs方法返回值决定是否创建代理对象,进行权限控制。
     */

    @Bean
    public AuthorizationAttributeSourceAdvisor
        newAuthorizationAttributeSourceAdvisor(@Autowired SecurityManager securityManager){
            AuthorizationAttributeSourceAdvisor advisor =
                    new AuthorizationAttributeSourceAdvisor();
            advisor.setSecurityManager(securityManager);
            return advisor;
    }

}
