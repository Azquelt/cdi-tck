package org.jboss.cdi.tck.tests.context.passivating.dependency.builtin;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_DEPENDENCY;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class BuiltinBeanPassivationDependencyTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BuiltinBeanPassivationDependencyTest.class).build();
    }

    @Inject
    Worker worker;

    @Inject
    Boss boss;

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "ea") })
    public void testInstance() throws IOException, ClassNotFoundException {
        assertNotNull(worker);
        assertNotNull(worker.getInstance());
        Hammer hammer = worker.getInstance().get();
        assertNotNull(hammer);

        String workerId = worker.getId();
        String hammerId = hammer.getId();

        byte[] serializedWorker = passivate(worker);
        Worker workerCopy = (Worker) activate(serializedWorker);

        assertNotNull(workerCopy);
        assertNotNull(workerCopy.getInstance());
        assertEquals(workerCopy.getId(), workerId);
        assertEquals(workerCopy.getInstance().get().getId(), hammerId);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "ed") })
    public void testBeanManager() throws IOException, ClassNotFoundException {
        assertNotNull(boss);
        assertNotNull(boss.getBeanManager());

        String bossId = boss.getId();

        byte[] serializedBoss = passivate(boss);
        Boss bossCopy = (Boss) activate(serializedBoss);

        assertNotNull(bossCopy);
        assertNotNull(bossCopy.getBeanManager());
        assertEquals(bossCopy.getId(), bossId);
        assertEquals(bossCopy.getBeanManager().getBeans(Boss.class).size(), 1);
    }

}
