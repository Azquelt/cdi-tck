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
package org.jboss.cdi.tck.tests.definition.stereotype.inheritance;

import static org.jboss.cdi.tck.cdi.Sections.STEREOTYPES_WITH_ADDITIONAL_STEREOTYPES;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.Bean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * @author pmuir
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class StereotypeInheritenceTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(StereotypeInheritenceTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ANNOTATED))
                .build();
    }

    @Test
    @SpecAssertions({@SpecAssertion(section = STEREOTYPES_WITH_ADDITIONAL_STEREOTYPES, id = "a"), @SpecAssertion(section = STEREOTYPES_WITH_ADDITIONAL_STEREOTYPES, id = "b")})
    public void testInheritence() {
        Set<Bean<Horse>> beans = getBeans(Horse.class);
        assert beans.size() == 1;
        Bean<Horse> bean = beans.iterator().next();
        assert bean.getScope().equals(RequestScoped.class);
        assert bean.isAlternative();
        assert bean.getName().equals("horse");
    }

}
