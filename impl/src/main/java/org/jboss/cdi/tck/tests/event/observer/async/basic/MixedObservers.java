/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.observer.async.basic;

import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;

import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.weld.experimental.Priority;

public class MixedObservers {

    public static class MassachusettsInstituteObserver {

        public static final AtomicInteger threadId = new AtomicInteger();

        public void observes(@ObservesAsync @Priority(2000) Experiment experiment) {
            experiment.addUniversity(getClass());
            ActionSequence.addAction(getClass().getSimpleName());
            threadId.set((int) Thread.currentThread().getId());
        }
    }

    public static class OxfordUniversityObserver {

        public static final AtomicInteger threadId = new AtomicInteger();

        public void observes(@Observes Experiment experiment) {
            experiment.addUniversity(getClass());
            ActionSequence.addAction(getClass().getSimpleName());
            threadId.set((int) Thread.currentThread().getId());
        }
    }

    public static class YaleUniversityObserver {

        public void observes(@ObservesAsync @American Experiment experiment) {
            experiment.addUniversity(getClass());
            ActionSequence.addAction(getClass().getSimpleName());
        }

    }

    public static class StandfordUniversityObserver {

        public static final AtomicInteger threadId = new AtomicInteger();

        public void observes(@ObservesAsync Experiment experiment) {
            experiment.addUniversity(getClass());
            ActionSequence.addAction(getClass().getSimpleName());
            threadId.set((int) Thread.currentThread().getId());
        }

    }

}
