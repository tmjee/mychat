package com.tmjee.mychat.service;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.tmjee.mychat.domain.RolesEnum;
import com.tmjee.mychat.rest.V1;
import com.tmjee.mychat.service.annotations.RolesAnnotation;
import com.tmjee.mychat.service.annotations.UserPreferencesAnnotation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.logging.Logger;

/**
 * @author tmjee
 */
public class RolesInterceptor implements MethodInterceptor {

    private static final Logger LOG = Logger.getLogger(RolesInterceptor.class.getName());

    private volatile Injector injector;
    private volatile Provider<UserPreferences> userPreferencesProvider;

    @Inject
    public void setUserPreferences(@UserPreferencesAnnotation Provider<UserPreferences> userPreferencesProvider) {
        this.userPreferencesProvider = userPreferencesProvider;
    }

    @Inject
    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        RolesAnnotation rolesAnnotation = invocation.getMethod().getAnnotation(RolesAnnotation.class);
        if (rolesAnnotation != null) {

            RolesEnum[] rolesEnum = rolesAnnotation.value();
            EnumSet<RolesEnum> rolesDeclared = EnumSet.copyOf(Arrays.asList(rolesEnum));

            if (rolesDeclared.contains(RolesEnum.ADMIN)) {
                return invocation.proceed();
            } else if (!rolesDeclared.isEmpty()) {
                Object arg0 = invocation.getArguments()[0];
                if (arg0 instanceof V1.RolesAware) {
                    EnumSet<RolesEnum> r = EnumSet.copyOf(userPreferencesProvider.get().getRoles());
                    r.retainAll(rolesDeclared); // effective roles, intersect of those declared and what the user has
                    V1.RolesAware rolesAware = ((V1.RolesAware)arg0);
                    injector.injectMembers(rolesAware);
                    rolesAware.action(r);
                }
            }
        }
        return invocation.proceed();
    }
}
