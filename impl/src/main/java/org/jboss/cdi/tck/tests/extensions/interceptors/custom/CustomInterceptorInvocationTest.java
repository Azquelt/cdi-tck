/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cdi.tck.tests.extensions.interceptors.custom;

import static org.testng.Assert.assertTrue;

import javax.inject.Inject;
import javax.interceptor.Interceptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.HierarchyDiscovery;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Registers an extension-provided implementation of the {@link Interceptor} interface and verifies that the implementation is
 * invoked upon invocation of an intercepted method.
 * 
 * <p>
 * This test was originally part of Weld test suite - WELD-997.
 * <p>
 * 
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class CustomInterceptorInvocationTest extends AbstractTest {

    @Inject
    private InterceptedBean bean;

    @Deployment
    public static WebArchive createTestArchive() {

        return new WebArchiveBuilder()
                .withTestClassPackage(CustomInterceptorInvocationTest.class)
                .withClasses(HierarchyDiscovery.class)
                .withExcludedClasses(CustomInterceptorRegistrationTest.class.getName(),
                        InterceptedSerializableBean.class.getName())
                .withExtension(CustomInterceptorExtension.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors().clazz(FooInterceptor.class.getName())
                                .up()).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.2", id = "dd"), @SpecAssertion(section = "11.1.2", id = "d") })
    public void testCustomInterceptorInvocation() {
        CustomInterceptor.reset();
        FooInterceptor.reset();
        bean.foo();
        assertTrue(CustomInterceptor.isInvoked());
        assertTrue(FooInterceptor.isInvoked());
    }
}
