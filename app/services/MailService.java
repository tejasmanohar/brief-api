package services;


import actors.MailActor;
import actors.messages.MailMessage;
import models.ConversationModel;
import models.UserModel;
import utils.config.AppConfigurations;

public class MailService {

    public void sendMail(String to, String subject, String body) {
        MailMessage mail = new MailMessage(
                AppConfigurations.Mail.getMailFromAddres(),
                to,
                subject,
                body
        );

        MailActor.MAIL_ACTOR.tell(mail, null);
    }

    public void sendMailToConversation(ConversationModel conversation, UserModel from, String message) {
        conversation.members.forEach(member -> {
            if(!member.id.equals(from.id)) {
                sendMail(
                        member.emailAddress,
                        "New Conversation Message",
                        "New message in " + conversation.title + " from " + from.toConversationMemberHelper().firstName + " :\n\n\n" + message
                );
            }
        });
    }

}
