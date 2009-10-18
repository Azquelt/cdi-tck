package org.jboss.jsr299.tck.tests.interceptors.definition.broken.nonInterceptorClassInBeansXml;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DeploymentError.class)
@SpecVersion(spec="cdi", version="20091018")
@BeansXml("beans.xml")
public class NonInterceptorClassInBeansXmlTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "9.4", id = "cab")
   public void testNonInterceptorClassInBeansXmlNotOk()
   {
      assert false;
   }
}
