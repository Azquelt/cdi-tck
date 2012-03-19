/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.implementation.disposal.method.definition.inheritance;

import static org.testng.Assert.assertEquals;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class DisposerMethodInheritanceTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DisposerMethodInheritanceTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "4.2", id = "db"), @SpecAssertion(section = "4.2", id = "dh") })
    public void testManagedBeanDisposerMethodNotInherited() {
        Bean<Apple> bean = getUniqueBean(Apple.class);
        CreationalContext<Apple> ctx = getCurrentManager().createCreationalContext(bean);
        Apple apple = bean.create(ctx);
        assertEquals(apple.getTree().getClass(), AppleTree.class);
        bean.destroy(apple, ctx);
        assertEquals(Apple.disposedBy.size(), 1);
        assertEquals(Apple.disposedBy.get(0), AppleTree.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "4.2", id = "de"), @SpecAssertion(section = "4.2", id = "dk") })
    public void testSessionBeanDisposerMethodNotInherited() {
        Bean<Egg> bean = getUniqueBean(Egg.class);
        CreationalContext<Egg> ctx = getCurrentManager().createCreationalContext(bean);
        Egg egg = bean.create(ctx);
        assertEquals(egg.getChicken().getClass(), Chicken.class);
        bean.destroy(egg, ctx);
        assertEquals(Egg.disposedBy.size(), 1);
        assertEquals(Egg.disposedBy.get(0), Chicken.class);
    }

}
