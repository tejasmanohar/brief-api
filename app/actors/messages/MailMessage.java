package actors.messages;

public class MailMessage {
    public String from;
    public String to;
    public String subject;
    public String body;

    public MailMessage(String from, String to, String subject, String body) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }
}
