package com.asli.adun.web;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;
import com.google.common.collect.Sets;

@Configuration
public class ApplicationConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {
    protected final ThymeleafProperties thymeleafProperties;
    private ApplicationContext applicationContext;


    @Autowired
    public ApplicationConfig(ThymeleafProperties thymeleafProperties) {
        this.thymeleafProperties = thymeleafProperties;

    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ParanamerModule());
        mapper.registerModule(new Jdk8Module());
        return mapper;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.order(HIGHEST_PRECEDENCE);
        registry.viewResolver(viewResolver());
    }

    private ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding(thymeleafProperties.getEncoding().toString());
        return resolver;
    }

    private TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolver(templateResolver());
        engine.setAdditionalDialects(Sets.newHashSet(new SpringSecurityDialect()));
        return engine;
    }

    private ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix(getTemplatePrefix());
        resolver.setSuffix(thymeleafProperties.getSuffix());
        resolver.setTemplateMode(thymeleafProperties.getMode());
        resolver.setCharacterEncoding(thymeleafProperties.getEncoding().toString());
        resolver.setCacheable(thymeleafProperties.isCache());
        return resolver;
    }

    protected String getTemplatePrefix() {
        return thymeleafProperties.getPrefix();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
    }


}
