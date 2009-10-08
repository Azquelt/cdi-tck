package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorOrder;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
@BeansXml("beans.xml")
public class InterceptorOrderTest extends AbstractJSR299Test
{
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "9.4", id = "b"),
      @SpecAssertion(section = "9.5", id = "ea")
   })
   public void testInterceptorsCalledInOrderDefinedByBeansXml()
   {
      FirstInterceptor.calledFirst = false;
      SecondInterceptor.calledFirst = false;
      
      Foo foo = getInstanceByType(Foo.class);
      foo.bar();
      
      assert SecondInterceptor.calledFirst;
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertion(section = "9.4", id = "f")
   public void testInterceptorsDeclaredUsingInterceptorsCalledBeforeInterceptorBinding()
   {
      TransactionalInterceptor.first = false;
      AnotherInterceptor.first = false;
      
      AccountTransaction transaction = getInstanceByType(AccountTransaction.class);
      transaction.execute();
      
      assert AnotherInterceptor.first;
   }
}
