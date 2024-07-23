package com.sulimann.testeum.configs;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;

@Configuration
public class ResolverConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
      argumentResolvers.add(new SpecificationArgumentResolver());
      argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
    }

}
