/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.tests.lookup.injection.non.contextual;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY;

import java.util.EventListener;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test verifies that ProcessAnnotatedType event is fired for various Java EE components and tests the AnnotatedType
 * implementation.
 *
 * It's placed in this package because it works with the same Java EE components as
 * {@link InjectionIntoNonContextualComponentTest} does.
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class ContainerEventTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(ContainerEventTest.class)
                .withWebXml("web.xml")
                .withClasses(Farm.class, ProcessAnnotatedTypeObserver.class, Sheep.class, TagLibraryListener.class,
                        TestFilter.class, TestListener.class, TestServlet.class, TestTagHandler.class)
                .withExtension(ProcessAnnotatedTypeObserver.class)
                .withWebResource("ManagedBeanTestPage.jsp", "ManagedBeanTestPage.jsp")
                .withWebResource("TagPage.jsp", "TagPage.jsp").withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml")
                .withWebResource("TestLibrary.tld", "WEB-INF/TestLibrary.tld").build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = BEAN_DISCOVERY, id = "be")
    public void testProcessAnnotatedTypeEventFiredForServletListener() {
        assert ProcessAnnotatedTypeObserver.getListenerEvent() != null;
        validateServletListenerAnnotatedType(ProcessAnnotatedTypeObserver.getListenerEvent().getAnnotatedType());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = BEAN_DISCOVERY, id = "bf")
    public void testProcessAnnotatedTypeEventFiredForTagHandler() {
        assert ProcessAnnotatedTypeObserver.getTagHandlerEvent() != null;
        validateTagHandlerAnnotatedType(ProcessAnnotatedTypeObserver.getTagHandlerEvent().getAnnotatedType());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = BEAN_DISCOVERY, id = "bg")
    public void testProcessAnnotatedTypeEventFiredForTagLibraryListener() {
        assert ProcessAnnotatedTypeObserver.getTagLibraryListenerEvent() != null;
        validateTagLibraryListenerAnnotatedType(ProcessAnnotatedTypeObserver.getTagLibraryListenerEvent().getAnnotatedType());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = BEAN_DISCOVERY, id = "bj")
    public void testProcessAnnotatedTypeEventFiredForServlet() {
        assert ProcessAnnotatedTypeObserver.getServletEvent() != null;
        validateServletAnnotatedType(ProcessAnnotatedTypeObserver.getServletEvent().getAnnotatedType());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = BEAN_DISCOVERY, id = "bk")
    public void testProcessAnnotatedTypeEventFiredForFilter() {
        assert ProcessAnnotatedTypeObserver.getFilterEvent() != null;
        validateFilterAnnotatedType(ProcessAnnotatedTypeObserver.getFilterEvent().getAnnotatedType());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = BEAN_DISCOVERY, id = "bd")
    public void testProcessAnnotatedTypeEventFiredForJsfManagedBean() {
        assert ProcessAnnotatedTypeObserver.getJsfManagedBeanEvent() != null;
        validateJsfManagedBeanAnnotatedType(ProcessAnnotatedTypeObserver.getJsfManagedBeanEvent().getAnnotatedType());
    }

    private void validateServletListenerAnnotatedType(AnnotatedType<TestListener> type) {
        assert type.getBaseType().equals(TestListener.class);
        assert type.getAnnotations().isEmpty();
        assert type.getFields().size() == 2;
        assert type.getMethods().size() == 3;

        int initializers = 0;
        for (AnnotatedMethod<?> method : type.getMethods()) {
            assert method.getParameters().size() == 1;
            assert method.getBaseType().equals(void.class);
            if (method.isAnnotationPresent(Inject.class)) {
                initializers++;
            }
        }
        assert initializers == 1;
    }

    private void validateTagHandlerAnnotatedType(AnnotatedType<TestTagHandler> type) {
        assert type.getBaseType().equals(TestTagHandler.class);
        assert rawTypeSetMatches(type.getTypeClosure(), TestTagHandler.class, SimpleTagSupport.class, SimpleTag.class,
                JspTag.class);
        assert type.getAnnotations().size() == 1;
        assert type.isAnnotationPresent(Any.class);
    }

    private void validateTagLibraryListenerAnnotatedType(AnnotatedType<TagLibraryListener> type) {
        assert type.getBaseType().equals(TagLibraryListener.class);
        assert rawTypeSetMatches(type.getTypeClosure(), TagLibraryListener.class, ServletContextListener.class,
                EventListener.class, Object.class);
        assert type.getFields().size() == 2;
        assert type.getConstructors().size() == 1;
        assert type.getMethods().size() == 3;
    }

    private void validateServletAnnotatedType(AnnotatedType<TestServlet> type) {
        assert type.getBaseType().equals(TestServlet.class);
        assert rawTypeSetMatches(type.getTypeClosure(), TestServlet.class, HttpServlet.class, GenericServlet.class,
                Servlet.class, ServletConfig.class, Object.class);
        assert type.getAnnotations().isEmpty();
    }

    private void validateFilterAnnotatedType(AnnotatedType<TestFilter> type) {
        assert type.getBaseType().equals(TestFilter.class);
        assert rawTypeSetMatches(type.getTypeClosure(), TestFilter.class, Filter.class, Object.class);
        assert type.getFields().size() == 6;
        assert type.getConstructors().size() == 1;
        assert type.getConstructors().iterator().next().getParameters().isEmpty();
        assert type.getMethods().size() == 4;
    }

    private void validateJsfManagedBeanAnnotatedType(AnnotatedType<Farm> type) {
        assert type.getFields().size() == 2;
        for (AnnotatedField<?> field : type.getFields()) {
            if (field.getJavaMember().getName().equals("sheep")) {
                assert field.isAnnotationPresent(Inject.class);
                assert !field.isStatic();
            } else if (field.getJavaMember().getName().equals("initializerCalled")) {
                assert !field.isStatic();
                assert field.getBaseType().equals(boolean.class);
            } else {
                assert false; // there is no other field
            }
        }
        assert type.getMethods().size() == 3;
    }
}
