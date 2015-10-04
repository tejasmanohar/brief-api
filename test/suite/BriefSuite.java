package suite;

import controllers.setup.Global;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import play.Logger;
import play.db.jpa.JPAPlugin;
import play.test.FakeApplication;
import play.test.TestServer;
import scala.Option;

import static play.test.Helpers.*;
import static play.test.Helpers.stop;
import util.GlobalTest;
import util.ServicesTestModule;

@RunWith(ClasspathSuite.class)
public class BriefSuite {

    private static final int PORT = 3336;
    private static Global global;
    private static FakeApplication fakeApp;
    private static TestServer testServer;
    public static Option<JPAPlugin> jpaPlugin;

    @BeforeClass
    public static void setUpBeforeClass() {

        global = new GlobalTest(new ServicesTestModule());
        fakeApp = fakeApplication(inMemoryDatabase(), global);
        testServer = testServer(PORT, fakeApp);
        Logger.info("START SERVER>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> PORT: " + PORT);

        start(testServer);
        jpaPlugin = fakeApp.getWrappedApplication().plugin(JPAPlugin.class);
        jpaPlugin.get().onStart();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        Logger.info("STOP SERVER>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> PORT = "+PORT);
        stop(testServer);
        fakeApp = null;
    }

    public static Global getGlobal() {
        return global;
    }

    public static boolean isServerRunning() {
        return fakeApp != null;
    }

}
