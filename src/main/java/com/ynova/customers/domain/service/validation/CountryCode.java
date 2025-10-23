package com.ynova.customers.domain.service.validation;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface CountryCode {
    String value();

    class Literal extends AnnotationLiteral<CountryCode> implements CountryCode {
        private final String value;
        public Literal(String value) { this.value = value; }
        public String value() { return value; }
    }
}