package util;

import models.*;
import play.Logger;
import play.db.jpa.JPA;

public class DbCleaner {

    public static void cleanDb() {
        ConversationModel.find.all().forEach(data -> data.delete());
        JobApplyModel.find.all().forEach(data -> data.delete());
        JobModel.find.all().forEach(data -> data.delete());
        EmployerModel.find.all().forEach(data -> data.delete());
        CompanyModel.find.all().forEach(data -> data.delete());
        UserModel.find.all().forEach(data -> data.delete());
        JPA.em().flush();
    }
}
