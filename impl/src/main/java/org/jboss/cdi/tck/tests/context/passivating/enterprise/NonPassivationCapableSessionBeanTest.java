/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.cdi.tck.tests.context.passivating.enterprise;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Verifies that a deployment which contains a non-passivation capable SFSB which is not bound to a passivating context, is
 * valid.
 * 
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class NonPassivationCapableSessionBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(NonPassivationCapableSessionBeanTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors()
                                .clazz(DigitalInterceptor.class.getName()).up()).build();
    }

    @Test
    @SpecAssertion(section = "6.6.4", id = "j")
    public void testDeployment() {
        // only verify deployment
    }
}
