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

package org.jboss.cdi.tck.tests.context.request;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

import org.jboss.cdi.tck.SimpleLogger;

/**
 * @author Martin Kouba
 */
@WebListener
public class TestServletRequestListener implements ServletRequestListener {

    private static final SimpleLogger logger = new SimpleLogger(TestServletRequestListener.class);

    @Inject
    private BeanManager beanManager;

    @Inject
    private SimpleRequestBean simpleBean;

    @Inject
    private RequestContextGuard guard;

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        logger.log("Request destroyed...");
        checkRequestContextActive();

        String mode = sre.getServletRequest().getParameter("guard");
        if (mode != null && mode.equals("collect")) {
            guard.setServletRequestListenerCheckpoint(System.currentTimeMillis());
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        // No-op
    }

    private void checkRequestContextActive() throws IllegalStateException {
        if (beanManager == null || !beanManager.getContext(RequestScoped.class).isActive() || simpleBean == null) {
            throw new IllegalStateException("Request context is not active");
        }
        // Check bean invocation
        simpleBean.getId();
    }
}
