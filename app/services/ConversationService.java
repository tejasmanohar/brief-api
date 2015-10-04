package services;

import com.google.inject.Inject;
import models.ConversationModel;
import models.JobApplyModel;
import models.UserModel;
import models.helpers.ConversationHelper;
import models.helpers.ConversationInviteHelper;
import models.helpers.ConversationMemberHelper;
import models.helpers.PublicCandidateInfoHelper;
import play.Logger;
import play.db.jpa.JPA;
import utils.exceptions.BusinessValidationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConversationService implements IConversationService {

    @Inject
    IUserService userService;

    @Inject
    IMessageService messageService;

    @Override
    public ConversationHelper create(UserModel owner, String title, Long applicationId, List<Long> membersIds) {
        JobApplyModel application = JobApplyModel.find.byId(applicationId);

        if(application == null) {
            throw new BusinessValidationException("applicationId", "Can't find application");
        }

        if(ConversationModel.find.where().eq("jobApplication.id", applicationId).findUnique()!= null) {
            throw new BusinessValidationException("applicationId", "This conversation already created");
        }

        ConversationModel conversation = new ConversationModel();
        final Set<UserModel> members = new HashSet<>();
        members.add(owner);

        membersIds.forEach(id -> {
            UserModel member = userService.findById(id);
            if (member != null) {
                members.add(member);
            }
        });

        conversation.title = title;
        conversation.members = members;
        conversation.jobApplication = application;
        conversation.save();

        return getConversationHelper(owner, conversation);
    }

    @Override
    public List<ConversationHelper> list(Long userId) {
        UserModel user = UserModel.find.where().eq("id", userId).findUnique();

        List<ConversationHelper> helperList = new ArrayList<>();

        user.conversations.forEach(conversation -> helperList.add(getConversationHelper(user, conversation)));

        return helperList;
    }

    @Override
    public ConversationModel get(UserModel user, Long id) {
        ConversationModel conversation = ConversationModel.find.byId(id);

        if(conversation == null) {
            throw new BusinessValidationException("id", "Can't find conversation by id");
        }

        if(!containsMemberById(user.id, conversation)) {
            throw new BusinessValidationException("id", "Access denied");
        }

        return conversation;
    }

    @Override
    public ConversationHelper show(UserModel user, Long id) {
        ConversationModel conversation = ConversationModel.find.byId(id);

        if(conversation == null) {
            throw new BusinessValidationException("id", "Can't find conversation by id");
        }

       return getConversationHelper(user,conversation);
    }

    @Override
    public ConversationHelper invite(UserModel user, Long id, ConversationInviteHelper helper) {
        ConversationModel conversation = ConversationModel.find.byId(id);

        if(conversation == null) {
            throw new BusinessValidationException("id", "Can't find conversation by id");
        }

        if(!containsMemberById(user.id, conversation)) {
            throw new BusinessValidationException("id", "Access denied");
        }

        final Set<UserModel> members = new HashSet<>();

        helper.membersIds.forEach(memberId -> {
            UserModel member = userService.findById(id);
            if(member != null && !containsMemberById(user.id, conversation)) {
                members.add(member);
            }
        });

        conversation.members.addAll(members);
        conversation.save();

        return getConversationHelper(user, conversation);
    }

    private Boolean containsMemberById(Long memberId,  ConversationModel conversation) {
        return conversation.members.stream().filter(member -> member.id.equals(memberId)).findFirst().isPresent();
    }


    private ConversationHelper getConversationHelper(UserModel user, ConversationModel conversation) {
        if(!containsMemberById(user.id, conversation)) {
            throw new BusinessValidationException("id", "Access denied");
        }

        Long unread = messageService.getUnreadMessages(user, conversation);

        List<ConversationMemberHelper> members = new ArrayList<>();

        conversation.members.forEach(member -> members.add(member.toConversationMemberHelper()));

        return new ConversationHelper(conversation.id, conversation.title, members, unread);
    }
}
