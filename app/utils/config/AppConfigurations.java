package utils.config;

public class AppConfigurations {

    public static class Facebook {
        public static final Integer API_TIMEOUT = 10000;
        public final static String GRAPH_URL = "https://graph.facebook.com/me?access_token=";
    }

    public static class Twitter {
        public final static String AUTH_CONSUMER_KEY_PROP = "twitter.auth.consumer.key";
        public final static String AUTH_CONSUMER_SECRET_PROP = "twitter.auth.consumer.secret";

        public static String getAuthConsumerKey() {
            return play.Play.application().configuration().getString(AUTH_CONSUMER_KEY_PROP);
        }

        public static String getAuthConsumerSecret() {
            return play.Play.application().configuration().getString(AUTH_CONSUMER_SECRET_PROP);
        }
    }

    public static class Mail {
        public final static String MAIL_FROM_ADDRESS_PROP = "mail.from.address";

        public static String getMailFromAddres() {
            return play.Play.application().configuration().getString(MAIL_FROM_ADDRESS_PROP);
        }
    }

    public static class Amazone {
        public static final String AWS_S3_BUCKET_PROP = "aws.s3.bucket";
        public static final String AWS_ACCESS_KEY_PROP = "aws.access.key";
        public static final String AWS_SECRET_KEY_PROP = "aws.secret.key";

        public static String awsS3Bucket() {
            return play.Play.application().configuration().getString(AWS_S3_BUCKET_PROP);
        }

        public static String awsAccessKey() {
            return play.Play.application().configuration().getString(AWS_ACCESS_KEY_PROP);
        }

        public static String awsSecretKey() {
            return play.Play.application().configuration().getString(AWS_SECRET_KEY_PROP);
        }
    }
}
