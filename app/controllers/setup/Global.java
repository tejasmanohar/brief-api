package controllers.setup;

import actors.GuiceInjectedActor;
import actors.MailActor;
import actors.WsConectionsActor;
import akka.actor.Props;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.inject.Injector;
import models.CountryModel;
import models.StateModel;
import play.*;
import play.db.jpa.JPA;
import play.libs.Akka;
import play.libs.F;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import utils.exceptions.BusinessValidationException;

import java.io.File;
import java.io.IOException;

public class Global extends GlobalSettings {

    private Injector injector;

    public Global(Injector injector) {
        this.injector = injector;
    }

    public Injector injector() {
        return injector;
    }

    @Override
    public <A> A getControllerInstance(Class<A> controllerClass) throws Exception {
        return injector.getInstance(controllerClass);
    }

    public void onStart(Application app) {

        insertData();

        MailActor.MAIL_ACTOR = Akka.system().actorOf(Props.create(MailActor.class), "MailActor");
        WsConectionsActor.WS_ACTOR = Akka.system().actorOf(Props.create(GuiceInjectedActor.class, injector, WsConectionsActor.class), "WsActor");
    }

    @Override
    public F.Promise<Result> onError(Http.RequestHeader requestHeader, Throwable throwable) {
        Throwable nestedThrowable = throwable.getCause();

        if (nestedThrowable != null) {
            if (nestedThrowable instanceof BusinessValidationException) {
                BusinessValidationException e = (BusinessValidationException) nestedThrowable;
                return F.Promise.<Result> pure(Results.status(e.getHttpResponseCode(), e.getJson()));
            }

        }

        ObjectNode result = Json.newObject();
        result.put("errorMessage", "Error executing function. Uncatched exception.");
        return F.Promise.<Result> pure(Results.internalServerError(result));
    }

    public void insertData() {
        try {
            final String path = Play.application().path().getAbsolutePath();
            final String addCountriesSQL = Files.toString(new File(path + "/conf/sql/countries.sql"), Charsets.UTF_8);
            final String addStateSQL = Files.toString(new File(path + "/conf/sql/states.sql"), Charsets.UTF_8);

            JPA.withTransaction(() -> {
                if(CountryModel.all().isEmpty()) {
                    Logger.info("Inserting countries data..........");
                    CountryModel.createSqlUpdate(addCountriesSQL).executeUpdate();
                }

                if(StateModel.all().isEmpty()) {
                    Logger.info("Inserting states data..........");
                    StateModel.createSqlUpdate(addStateSQL).executeUpdate();
                }
            });
        } catch (IOException e) {
            Logger.error("Can't insert countries data : " + e.getMessage());
        }
    }
}
