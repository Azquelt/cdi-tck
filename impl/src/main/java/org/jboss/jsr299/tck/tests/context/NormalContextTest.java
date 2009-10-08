package org.jboss.jsr299.tck.tests.context;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.impl.MockCreationalContext;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * 
 * @author Nicklas Karlsson
 * @author Pete Muir
 * @author David Allen
 */
@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class NormalContextTest extends AbstractJSR299Test
{

   @Test(groups = { "contexts" })
   @SpecAssertions( {
      @SpecAssertion(section = "6.2", id = "j"),
      @SpecAssertion(section = "6.2", id = "l"),
      @SpecAssertion(section = "6.3", id = "c")
   } )
   public void testGetReturnsExistingInstance()
   {
      Bean<MySessionBean> mySessionBean = getBeans(MySessionBean.class).iterator().next();
      CreationalContext<MySessionBean> creationalContext = getCurrentManager().createCreationalContext(mySessionBean);
      MySessionBean first = getCurrentManager().getContext(SessionScoped.class).get(mySessionBean, creationalContext);
      first.setId(10);
      MySessionBean second = getCurrentManager().getContext(SessionScoped.class).get(mySessionBean, creationalContext);
      assert second.getId() == 10;
      MySessionBean third = getCurrentManager().getContext(SessionScoped.class).get(mySessionBean);
      assert third.getId() == 10;
      MySessionBean fourth = getCurrentManager().getContext(SessionScoped.class).get(mySessionBean, getCurrentManager().createCreationalContext(mySessionBean));
      assert fourth.getId() == 10;
   }

   @Test(groups = { "contexts", "rewrite" })
   @SpecAssertions( {
      @SpecAssertion(section = "6.2", id = "l")
   } )
   public void testGetWithCreationalContextReturnsNewInstance()
   {
      MyContextual bean = new MyContextual(getCurrentManager());
      bean.setShouldReturnNullInstances(false);
      // TODO Remove use of this deprecated API
      getCurrentManager().addBean(bean);

      CreationalContext<MySessionBean> creationalContext = new MockCreationalContext<MySessionBean>();
      MySessionBean newBean = getCurrentManager().getContext(SessionScoped.class).get(bean, creationalContext);
      assert newBean != null;
      assert bean.isCreateCalled();
   }

   @Test(groups = { "contexts" , "rewrite"})
   @SpecAssertion(section = "6.2", id = "nb")
   public void testGetMayNotReturnNullUnlessContextualCreateReturnsNull()
   {
      // The case of no creational context is already tested where a null is
      // returned. Here we just test that the contextual create can return null.
      MyContextual bean = new MyContextual(getCurrentManager());
      bean.setShouldReturnNullInstances(true);
      // TODO Remove use of this deprecated API
      getCurrentManager().addBean(bean);

      CreationalContext<MySessionBean> creationalContext = new MockCreationalContext<MySessionBean>();
      assert getCurrentManager().getContext(SessionScoped.class).get(bean, creationalContext) == null;
      assert bean.isCreateCalled();
   }

   @Test(groups = { "contexts", "rewrite" })
   @SpecAssertions( {
      @SpecAssertion(section = "6.2", id = "p"),
      @SpecAssertion(section = "6.3", id = "d")
   })
   public void testContextDestroysBeansWhenDestroyed()
   {
      MyContextual bean = new MyContextual(getCurrentManager());
      bean.setShouldReturnNullInstances(false);
      // TODO Remove use of this deprecated API
      getCurrentManager().addBean(bean);

      Context sessionContext = getCurrentManager().getContext(SessionScoped.class);
      CreationalContext<MySessionBean> creationalContext = getCurrentManager().createCreationalContext(bean);
      MySessionBean instance = sessionContext.get(bean, creationalContext);
      instance.ping();
      assert instance != null;
      assert bean.isCreateCalled();
      
      destroyContext(sessionContext);
      assert bean.isDestroyCalled();
   }
   
   @Test
   @SpecAssertion(section = "6.2", id = "r")
   public void testDestroyForSameCreationalContextOnly()
   {
      // Check that the mock cc is called (via cc.release()) when we request a context destroyed
      // Note that this is an indirect effect
      Context sessionContext = getCurrentManager().getContext(SessionScoped.class);
      Context requestContext = getCurrentManager().getContext(RequestScoped.class);
      Context appContext = getCurrentManager().getContext(ApplicationScoped.class);
      
      Bean<AnotherSessionBean> sessionBean = getBeans(AnotherSessionBean.class).iterator().next();
      
      MockCreationalContext.reset();
      CreationalContext<AnotherSessionBean> creationalContext = new MockCreationalContext<AnotherSessionBean>();
      AnotherSessionBean instance = sessionContext.get(sessionBean, creationalContext);
      instance.ping();
      
      destroyContext(sessionContext);
      assert MockCreationalContext.isReleaseCalled();
      
      // We also test this directly using a custom contextual, and ensuring that the same contextual is passed to both methods
      DummyContextual contextual = new DummyContextual();
      
      sessionContext.get(contextual, getCurrentManager().createCreationalContext(contextual));
      destroyContext(sessionContext);
      assert contextual.getCreationalContextPassedToCreate() == contextual.getCreationalContextPassedToDestroy();
      
      // Do it for other contexts
      contextual = new DummyContextual();
      appContext.get(contextual, getCurrentManager().createCreationalContext(contextual));
      destroyContext(appContext);
      assert contextual.getCreationalContextPassedToCreate() == contextual.getCreationalContextPassedToDestroy();
      
      contextual = new DummyContextual();
      requestContext.get(contextual, getCurrentManager().createCreationalContext(contextual));
      destroyContext(requestContext);
      assert contextual.getCreationalContextPassedToCreate() == contextual.getCreationalContextPassedToDestroy();
      
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertions( {
      @SpecAssertion(section = "6.3", id = "e")
   })
   public void testSameNormalScopeBeanInjectedEverywhere()
   {
      SimpleBeanA instanceOfA = getInstanceByType(SimpleBeanA.class);
      SimpleBeanB instanceOfB = getInstanceByType(SimpleBeanB.class);
      assert instanceOfA.getZ() == instanceOfB.getZ();
   }
}
