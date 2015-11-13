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
package org.jboss.cdi.tck.tests.interceptors.definition.broken.observer.async;

import javax.annotation.Priority;
import javax.enterprise.event.ObservesAsync;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.jboss.cdi.tck.util.SimpleLogger;

@Transactional
@Interceptor
@Priority(2500)
public class TransactionalInterceptor {

    private static final SimpleLogger logger = new SimpleLogger(TransactionalInterceptor.class);

    @AroundInvoke
    public Object alwaysReturnThis(InvocationContext ctx) throws Exception {
        return ctx.proceed();
    }

    public void observeFoo(@ObservesAsync String message) {
        logger.log("OBSERVED");
    }
}
