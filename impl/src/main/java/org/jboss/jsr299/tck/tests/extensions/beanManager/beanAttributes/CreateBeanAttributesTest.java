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
package org.jboss.jsr299.tck.tests.extensions.beanManager.beanAttributes;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanAttributes;
import javax.inject.Named;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.NamedLiteral;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.jsr299.tck.tests.extensions.alternative.metadata.AnnotatedTypeWrapper;
import org.jboss.jsr299.tck.tests.extensions.alternative.metadata.AnnotatedWrapper;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
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
@SpecVersion(spec = "cdi", version = "20091101")
public class CreateBeanAttributesTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(CreateBeanAttributesTest.class)
                .withClasses(AnnotatedTypeWrapper.class, AnnotatedWrapper.class).build();
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertion(section = "11.3.22", id = "a")
    public void testBeanAttributesForManagedBean() {
        AnnotatedType<Mountain> type = getCurrentManager().createAnnotatedType(Mountain.class);
        BeanAttributes<Mountain> attributes = getCurrentManager().createBeanAttributes(type);

        assertTrue(typeSetMatches(attributes.getTypes(), Landmark.class, Object.class));
        assertTrue(typeSetMatches(attributes.getStereotypes(), TundraStereotype.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Natural.class, Any.class));
        assertEquals(attributes.getScope(), ApplicationScoped.class);
        assertEquals(attributes.getName(), "mountain");
        assertTrue(attributes.isAlternative());
        assertTrue(attributes.isNullable());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertion(section = "11.3.22", id = "a")
    public void testBeanAttributesForManagedBeanWithModifiedAnnotatedType() {
        AnnotatedType<Mountain> type = getCurrentManager().createAnnotatedType(Mountain.class);
        AnnotatedType<Mountain> wrappedType = new AnnotatedTypeWrapper<Mountain>(type, false, new NamedLiteral("Mount Blanc"));
        BeanAttributes<Mountain> attributes = getCurrentManager().createBeanAttributes(wrappedType);

        assertTrue(typeSetMatches(attributes.getTypes(), Mountain.class, Landmark.class, Object.class));
        assertTrue(attributes.getStereotypes().isEmpty());
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Named.class, Any.class, Default.class));
        assertEquals(attributes.getScope(), Dependent.class);
        assertEquals(attributes.getName(), "Mount Blanc");
        assertFalse(attributes.isAlternative());
        assertTrue(attributes.isNullable());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertion(section = "11.3.22", id = "a")
    public void testBeanAttributesForSessionBean() {
        AnnotatedType<Lake> type = getCurrentManager().createAnnotatedType(Lake.class);
        BeanAttributes<Lake> attributes = getCurrentManager().createBeanAttributes(type);

        assertTrue(typeSetMatches(attributes.getTypes(), LakeLocal.class, WaterBody.class, Landmark.class, Object.class));
        assertTrue(typeSetMatches(attributes.getStereotypes(), TundraStereotype.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Natural.class, Any.class));
        assertEquals(attributes.getScope(), Dependent.class);
        assertEquals(attributes.getName(), "lake");
        assertTrue(attributes.isAlternative());
        assertTrue(attributes.isNullable());
    }

    @Test
    @SpecAssertion(section = "11.3.22", id = "b")
    public void testBeanAttributesForMethod() {
        AnnotatedType<Dam> type = getCurrentManager().createAnnotatedType(Dam.class);

        AnnotatedMethod<?> lakeFishMethod = null;
        AnnotatedMethod<?> damFishMethod = null;
        AnnotatedMethod<?> volumeMethod = null;

        for (AnnotatedMethod<?> method : type.getMethods()) {
            if (method.getJavaMember().getName().equals("getFish")
                    && method.getJavaMember().getDeclaringClass().equals(Dam.class)) {
                damFishMethod = method;
            }
            if (method.getJavaMember().getName().equals("getFish")
                    && method.getJavaMember().getDeclaringClass().equals(Lake.class)) {
                lakeFishMethod = method;
            }
            if (method.getJavaMember().getName().equals("getVolume")
                    && method.getJavaMember().getDeclaringClass().equals(Lake.class)) {
                volumeMethod = method;
            }
        }
        assertNotNull(lakeFishMethod);
        assertNotNull(damFishMethod);
        assertNotNull(volumeMethod);

        verifyLakeFish(getCurrentManager().createBeanAttributes(lakeFishMethod));
        verifyDamFish(getCurrentManager().createBeanAttributes(damFishMethod));
        verifyVolume(getCurrentManager().createBeanAttributes(volumeMethod));
    }

    @Test
    @SpecAssertion(section = "11.3.22", id = "b")
    public void testBeanAttributesForField() {
        AnnotatedType<Dam> type = getCurrentManager().createAnnotatedType(Dam.class);

        AnnotatedField<?> lakeFishField = null;
        AnnotatedField<?> damFishField = null;
        AnnotatedField<?> volumeField = null;

        for (AnnotatedField<?> field : type.getFields()) {
            if (field.getJavaMember().getName().equals("fish") && field.getJavaMember().getDeclaringClass().equals(Dam.class)) {
                damFishField = field;
            }
            if (field.getJavaMember().getName().equals("fish") && field.getJavaMember().getDeclaringClass().equals(Lake.class)) {
                lakeFishField = field;
            }
            if (field.getJavaMember().getName().equals("volume")
                    && field.getJavaMember().getDeclaringClass().equals(Lake.class)) {
                volumeField = field;
            }
        }
        assertNotNull(lakeFishField);
        assertNotNull(damFishField);
        assertNotNull(volumeField);

        verifyLakeFish(getCurrentManager().createBeanAttributes(lakeFishField));
        verifyDamFish(getCurrentManager().createBeanAttributes(damFishField));
        verifyVolume(getCurrentManager().createBeanAttributes(volumeField));
    }

    @SuppressWarnings("unchecked")
    private void verifyLakeFish(BeanAttributes<?> attributes) {
        assertTrue(typeSetMatches(attributes.getTypes(), Fish.class, Object.class));
        assertTrue(typeSetMatches(attributes.getStereotypes(), TundraStereotype.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Natural.class, Any.class, Named.class));
        assertEquals(attributes.getScope(), ApplicationScoped.class);
        assertEquals(attributes.getName(), "fish");
        assertTrue(attributes.isAlternative());
        assertTrue(attributes.isNullable());
    }

    @SuppressWarnings("unchecked")
    private void verifyDamFish(BeanAttributes<?> attributes) {
        assertTrue(typeSetMatches(attributes.getTypes(), Fish.class, Animal.class, Object.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Wild.class, Any.class));
        assertTrue(attributes.getStereotypes().isEmpty());
        assertEquals(attributes.getScope(), Dependent.class);
        assertNull(attributes.getName());
        assertFalse(attributes.isAlternative());
        assertTrue(attributes.isNullable());
    }

    @SuppressWarnings("unchecked")
    private void verifyVolume(BeanAttributes<?> attributes) {
        assertTrue(typeSetMatches(attributes.getTypes(), long.class, Object.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Any.class, Default.class, Named.class));
        assertTrue(attributes.getStereotypes().isEmpty());
        assertEquals(attributes.getScope(), Dependent.class);
        assertEquals(attributes.getName(), "volume");
        assertTrue(attributes.isAlternative());
        assertFalse(attributes.isNullable());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "11.3.22", id = "c")
    public void testInvalidMember() {
        AnnotatedConstructor<?> constructor = getCurrentManager().createAnnotatedType(InvalidBeanType.class).getConstructors()
                .iterator().next();
        getCurrentManager().createBeanAttributes(constructor);
    }
}
