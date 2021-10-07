package com.fyp.application;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class FinalYearProjectApplicationConfiguration {

  @Bean
  public ITemplateResolver svgTemplateResolver() {
    SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
    resolver.setPrefix("classpath:/templates/svg/");
    resolver.setSuffix(".svg");
    resolver.setTemplateMode("XML");

    return resolver;
  }

  @Bean
  public LocalValidatorFactoryBean localValidatorFactoryBean(MessageSource messageSource) {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource);
    return bean;
  }
}
