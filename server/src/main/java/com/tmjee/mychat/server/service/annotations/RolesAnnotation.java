package com.tmjee.mychat.server.service.annotations;

import com.tmjee.mychat.common.domain.RolesEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author tmjee
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RolesAnnotation {
    RolesEnum[] value();
}
