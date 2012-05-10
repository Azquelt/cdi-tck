package org.jboss.cdi.tck.tests.implementation.enterprise.newBean;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Secure
public class OrderInterceptor {

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        return true;
    }
}
