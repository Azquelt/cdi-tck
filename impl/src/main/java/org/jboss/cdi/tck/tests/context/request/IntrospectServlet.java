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

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.cdi.tck.util.SimpleLogger;

@WebServlet(name = "RequestIntrospector", urlPatterns = "/introspectRequest")
public class IntrospectServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final SimpleLogger logger = new SimpleLogger(IntrospectServlet.class);

    @Inject
    private SimpleRequestBean simpleBean;

    @Inject
    private RequestContextGuard guard;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/text");
        String mode = req.getParameter("guard");

        if (mode == null) {
            resp.getWriter().print(simpleBean.getId());
        } else if (mode.equals("collect")) {
            guard.setServletCheckpoint(System.currentTimeMillis());
        } else if (mode.equals("verify")) {
            logger.log("Verify guard: {0}", guard.isCheckpointSequenceOk());
            resp.getWriter().print(guard.isCheckpointSequenceOk());
        } else {
            throw new ServletException("Unknown guard mode");
        }
    }

}
