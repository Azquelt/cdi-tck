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
package org.jboss.cdi.tck.tests.deployment.initialization;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessModule;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test application initialization lifecycle.
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
@Test(groups = INTEGRATION)
public class ApplicationInitializationLifecycleTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ApplicationInitializationLifecycleTest.class)
                .withExtension(LifecycleMonitoringExtension.class).build();
    }

    @Inject
    Foo foo;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "12.2", id = "b"), @SpecAssertion(section = "12.2", id = "c"),
            @SpecAssertion(section = "12.2", id = "da"), @SpecAssertion(section = "12.2", id = "f"),
            @SpecAssertion(section = "12.2", id = "g"), @SpecAssertion(section = "12.2", id = "ga"),
            @SpecAssertion(section = "12.2", id = "h"), @SpecAssertion(section = "12.2", id = "i") })
    public void testInitialization() {

        foo.ping();

        // Test lifecycle phases sequence
        List<String> correctSequenceData = new ArrayList<String>();
        // Extension registration
        correctSequenceData.add(LifecycleMonitoringExtension.class.getName());
        // BeforeBeanDiscovery
        correctSequenceData.add(BeforeBeanDiscovery.class.getName());
        // ProcessModule
        correctSequenceData.add(ProcessModule.class.getName());
        // Bean discovery
        correctSequenceData.add(ProcessAnnotatedType.class.getName());
        // AfterBeanDiscovery
        correctSequenceData.add(AfterBeanDiscovery.class.getName());
        // Validating bean dependencies and specialization - currently no portable way how to test
        // AfterDeploymentValidation
        correctSequenceData.add(AfterDeploymentValidation.class.getName());
        // Inject any enums declaring injection points
        correctSequenceData.add(Bar.class.getName());
        // Processing requests
        correctSequenceData.add(Foo.class.getName());

        assertEquals(ActionSequence.getSequenceData(), correctSequenceData);
    }
}
