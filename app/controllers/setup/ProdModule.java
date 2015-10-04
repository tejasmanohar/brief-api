package controllers.setup;

import com.google.inject.AbstractModule;
import services.*;

public class ProdModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IAuthenticationService.class).to(AuthenticationService.class);
        bind(ICandidateService.class).to(CandidateService.class);
        bind(IUserService.class).to(UserService.class);
        bind(IConversationService.class).to(ConversationService.class);
        bind(IJobService.class).to(JobService.class);
        bind(IMessageService.class).to(MessageService.class);
    }
}
