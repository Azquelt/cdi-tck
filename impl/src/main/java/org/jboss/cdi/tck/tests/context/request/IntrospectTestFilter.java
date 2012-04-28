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
package org.jboss.cdi.tck.tests.context.request;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.jboss.cdi.tck.util.SimpleLogger;

@WebFilter(filterName = "DestroyTestFilter", urlPatterns = "/introspectRequest")
public class IntrospectTestFilter implements Filter {

    private static final SimpleLogger logger = new SimpleLogger(IntrospectTestFilter.class);

    @Inject
    private BeanManager beanManager;

    @Inject
    private SimpleRequestBean simpleBean;

    @Inject
    private RequestContextGuard guard;

    public void destroy() {
        beanManager = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        logger.log("Do filter...");
        checkRequestContextActive();
        chain.doFilter(request, response);
        checkRequestContextActive();

        String mode = request.getParameter("guard");
        if (mode != null && mode.equals("collect")) {
            guard.setFilterCheckpoint(System.currentTimeMillis());
        }
    }

    private void checkRequestContextActive() throws ServletException {
        if (beanManager == null || !beanManager.getContext(RequestScoped.class).isActive() || simpleBean == null) {
            throw new ServletException("Request context is not active");
        }
        // Check bean invocation
        simpleBean.getId();
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }

}
