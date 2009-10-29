package org.jboss.jsr299.tck.tests.context.passivating;

import java.io.Serializable;

import javax.decorator.Delegate;
import javax.decorator.Decorator;
import javax.enterprise.inject.Any;

@Decorator
class CityDecorator implements CityInterface, Serializable
{
   @Delegate @Any CityInterface city; 
   
   public void foo()
   {
      city.foo();      
   }
}
