package org.jboss.jsr299.tck.tests.xml.declaration.deployment.notvalid.multipledeclaration;

import javax.inject.DefinitionException;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.tests.xml.declaration.deployment.foo.TestDeploymentType;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@Classes({TestDeploymentType.class })
@BeansXml("beans.xml")
@ExpectedDeploymentException(DefinitionException.class)
public class MultipleDeclarationTest extends AbstractJSR299Test 
{	
	@Test
	   @SpecAssertions({
	      @SpecAssertion(section="9.12.1", id="c")
	   })
	   public void testMultipleDeclaration()
	   {
	      assert false;
	   }
}
