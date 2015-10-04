package actors;

import actors.messages.MailMessage;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;

public class MailActor extends UntypedActor {

    public static ActorRef MAIL_ACTOR;

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof MailMessage) {
            sendMail((MailMessage) message);
        } else {
            unhandled(message);
        }

    }

    private void sendMail(MailMessage mail) {
        Email email = new Email();
        email.setSubject(mail.subject);
        email.setFrom(mail.from);
        email.addTo(mail.to);
        email.setBodyText(mail.body);

        MailerPlugin.send(email);
    }
}
