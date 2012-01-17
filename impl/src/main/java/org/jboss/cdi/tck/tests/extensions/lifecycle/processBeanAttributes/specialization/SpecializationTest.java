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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes.specialization;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import javax.enterprise.inject.spi.BeanAttributes;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.literals.NamedLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class SpecializationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(SpecializationTest.class)
                .withClasses(Alpha.class, Bar.class, Baz.class, Bravo.class, Foo.class, Charlie.class, VerifyingExtension.class)
                .withExtension(VerifyingExtension.class).build();
    }

    @Inject
    private VerifyingExtension extension;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "aa"), @SpecAssertion(section = "4.3", id = "ca") })
    public void testProcessBeanAttributesFiredProperlyForSpecializedBean() {
        assertNull(extension.getAlpha());
        assertNull(extension.getBravo());
        BeanAttributes<Charlie> charlieAttributes = extension.getCharlie();
        assertNotNull(charlieAttributes);
        annotationSetMatches(charlieAttributes.getQualifiers(), Foo.Literal.INSTANCE, Bar.Literal.INSTANCE,
                Baz.Literal.INSTANCE, AnyLiteral.INSTANCE, new NamedLiteral("alpha"));
        assertEquals(charlieAttributes.getName(), "alpha");
    }

}
