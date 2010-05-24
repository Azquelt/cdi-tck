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
package org.jboss.jsr299.tck.tests.context.application.ejb;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

/**
 * EJB and related tests with the built-in application context.
 * 
 * @author David Allen
 */
@Artifact
@IntegrationTest
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091101")
public class ApplicationContextSharedTest extends AbstractJSR299Test
{

   @Test(groups = { "contexts", "ejb3", "integration", "rewrite" })
   @SpecAssertion(section = "6.7.3", id = "e")
   public void testApplicationContextShared() throws Exception
   {
      FMSModelIII.reset();
      FMS flightManagementSystem = getInstanceByType(FMS.class);
      flightManagementSystem.climb();
      waitForClimbed();
      flightManagementSystem.descend();
      waitForDescended();
      assert flightManagementSystem.isSameBean();
   }
   
   private void waitForClimbed() throws Exception
   {
      for (int i = 0; !FMSModelIII.isClimbed() && i < 2000; i++)
      {
         Thread.sleep(10);
      }
   }
   
   @Test(groups = { "contexts", "ejb3", "integration" })
   @SpecAssertion(section = "6.7.3", id = "dc")
   public void testApplicationScopeActiveDuringCallToEjbTimeoutMethod() throws Exception
   {
      FMSModelIII.reset();
      FMS flightManagementSystem = getInstanceByType(FMS.class);
      flightManagementSystem.climb();
      waitForClimbed();
      assert flightManagementSystem.isApplicationScopeActive();
   }
   
   private void waitForDescended() throws Exception
   {
      for (int i = 0; !FMSModelIII.isDescended() && i < 2000; i++)
      {
         Thread.sleep(10);
      }
   }

}
