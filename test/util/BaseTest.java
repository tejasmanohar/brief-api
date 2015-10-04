package util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import suite.BriefSuite;

public class BaseTest {

    public static boolean startedServerLocally;

    @BeforeClass
    public static void setUpBeforeClass() {
        if(!BriefSuite.isServerRunning()) {
            BriefSuite.setUpBeforeClass();
            startedServerLocally = true;
        }
    }

    @AfterClass
    public static void tearDownAfterClass() {
        if(startedServerLocally) {
            BriefSuite.tearDownAfterClass();
            startedServerLocally = false;
        }
    }
}
