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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes.synthetic;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanAttributes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class BicycleExtension implements Extension {

    private boolean vetoed;
    private BeanAttributes<Bicycle> bicycleAttributesBeforeRegistering = null;

    // we do not want the default bicycle bean
    void vetoBicycleClass(@Observes ProcessAnnotatedType<Bicycle> event) {
        event.veto();
        vetoed = true;
    }

    void registerBicycle(@Observes AfterBeanDiscovery event, BeanManager manager) {
        bicycleAttributesBeforeRegistering = manager.createBeanAttributes(manager.createAnnotatedType(Bicycle.class));
        event.addBean(new BicycleBean(bicycleAttributesBeforeRegistering));
    }

    public boolean isVetoed() {
        return vetoed;
    }

    public BeanAttributes<Bicycle> getBicycleAttributesBeforeRegistering() {
        return bicycleAttributesBeforeRegistering;
    }

}