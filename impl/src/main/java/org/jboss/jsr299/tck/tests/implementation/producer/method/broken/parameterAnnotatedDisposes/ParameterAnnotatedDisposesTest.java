package org.jboss.jsr299.tck.tests.implementation.producer.method.broken.parameterAnnotatedDisposes;


import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DefinitionError.class)
@SpecVersion(spec="cdi", version="20091018")
public class ParameterAnnotatedDisposesTest extends AbstractJSR299Test
{
   

   @Test(groups = "producerMethod")
   @SpecAssertion(section = "3.3.2", id = "da")
   public void testProducerMethodWithParameterAnnotatedDisposes() throws Exception
   {
      assert false;
   }
   
}
