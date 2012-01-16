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
package org.jboss.jsr299.tck.tests.context.conversation.event;

import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

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
public class TransientConversationLifecycleEventTest extends AbstractJSR299Test {

    @ArquillianResource(Servlet.class)
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassDefinition(TransientConversationLifecycleEventTest.class)
                .withClasses(Servlet.class, ObservingBean.class, ConversationScopedBean.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6.7.4", id = "ba"), @SpecAssertion(section = "6.7.4", id = "bb") })
    public void testLifecycleEventFiredForTransientConversation() throws Exception {
        WebClient client = new WebClient();

        TextPage page = client.getPage(contextPath + "/display");
        assertTrue(page.getContent().contains("Initialized conversations:1")); // the current transient
        assertTrue(page.getContent().contains("Destroyed conversations:0")); // not destroyed yet

        page = client.getPage(contextPath + "/display");
        assertTrue(page.getContent().contains("Initialized conversations:2"));
        assertTrue(page.getContent().contains("Destroyed conversations:1"));
    }
}
