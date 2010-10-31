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
package org.jboss.jsr299.tck.tests.event.observer.enterprise;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

/**
 * Tests for event inheritence with enterprise beans
 * 
 * @author Shane Bryzak
 */
@Artifact
@Packaging(PackagingType.EAR)
@IntegrationTest
@SpecVersion(spec="cdi", version="20091101")
public class EnterpriseEventInheritenceTest extends AbstractJSR299Test
{
   @Test(groups = { "events", "inheritance" })
   @SpecAssertion(section = "4.2", id = "df")
   public void testNonStaticObserverMethodInherited() throws Exception
   {
      Egg egg = new Egg();
      getCurrentManager().fireEvent(egg);
      assert typeSetMatches(egg.getClassesVisited(), Farmer.class, LazyFarmer.class);
   }
   
   @Test(groups = { "events", "inheritance" })
   @SpecAssertion(section = "4.2", id = "dl")
   public void testNonStaticObserverMethodIndirectlyInherited() throws Exception
   {
      StockPrice stockPrice = new StockPrice();
      getCurrentManager().fireEvent(stockPrice);
      assert typeSetMatches(stockPrice.getClassesVisited(), StockWatcher.class, IndirectStockWatcher.class, IntermediateStockWatcher.class);
   }
}
