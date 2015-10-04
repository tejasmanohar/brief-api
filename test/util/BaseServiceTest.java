package util;

import javax.persistence.EntityManager;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import models.CountryModel;
import models.StateModel;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import suite.BriefSuite;

import java.io.File;
import java.io.IOException;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({ServicesTestModule.class})
public abstract class BaseServiceTest extends BaseTest {

    @Before
    public void setUp() {
        EntityManager manager = BriefSuite.jpaPlugin.get().em("default");
        JPA.bindForCurrentThread(manager);
        JPA.em().getTransaction().begin();
        insertTestData();
    }

    @After
    public void tearDown() {
        DbCleaner.cleanDb();
        JPA.em().getTransaction().commit();
        JPA.bindForCurrentThread(null);
    }

    public void insertTestData() {
        try {
            final String path = Play.application().path().getAbsolutePath();
            final String addCountriesSQL = Files.toString(new File(path + "/conf/sql/countries.sql"), Charsets.UTF_8);
            final String addStateSQL = Files.toString(new File(path + "/conf/sql/states.sql"), Charsets.UTF_8);

            if(CountryModel.all().isEmpty()) {
                CountryModel.createSqlUpdate(addCountriesSQL).executeUpdate();
            }

            if(StateModel.all().isEmpty()) {
                StateModel.createSqlUpdate(addStateSQL).executeUpdate();
            }

        } catch (IOException e) {
            Logger.error("Can't insert test data : " + e.getMessage());
        }
    }

}