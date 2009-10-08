package org.jboss.jsr299.tck.tests.definition.stereotype.broken.tooManyScopes;


import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DefinitionError.class)
@SpecVersion(spec="cdi", version="PFD2")
public class TooManyScopeTypesTest extends AbstractJSR299Test
{
   
   @Test
   @SpecAssertion(section = "2.7.1.1", id = "ab")
   public void testStereotypeWithTooManyScopeTypes()
   {
      assert false;
   }
   
}
