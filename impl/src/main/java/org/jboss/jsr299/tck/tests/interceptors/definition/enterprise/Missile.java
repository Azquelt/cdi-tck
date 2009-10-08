package org.jboss.jsr299.tck.tests.interceptors.definition.enterprise;

import javax.ejb.Stateful;
import javax.interceptor.Interceptors;

@Stateful
@Interceptors(MissileInterceptor.class)
public class Missile implements MissileLocal
{
   public void fire() {}
}
