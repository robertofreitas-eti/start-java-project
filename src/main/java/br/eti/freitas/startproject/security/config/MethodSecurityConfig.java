package br.eti.freitas.startproject.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import br.eti.freitas.startproject.security.custom.CustomPermissionEvaluator;
import br.eti.freitas.startproject.security.handler.MethodSecurityExpressionHandlerJwt;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {

        final MethodSecurityExpressionHandlerJwt expressionHandler = new MethodSecurityExpressionHandlerJwt();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return expressionHandler;

    }
}