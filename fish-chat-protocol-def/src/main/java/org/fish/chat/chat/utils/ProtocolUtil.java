package org.fish.chat.chat.utils;


import org.fish.chat.common.log.LoggerManager;
import org.apache.commons.lang3.StringUtils;
import org.fish.chat.chat.ChatProtocol;
import org.fish.chat.chat.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Comments for ProtocolUtil.java
 *
 */
public class ProtocolUtil {

    public static byte[] getTextMessage(long from, long to, String text) {
        ChatProtocol.FishChatProtocol.Builder builder = ChatProtocol.FishChatProtocol.newBuilder();
        builder.setType(1);
        builder.setVersion("1");
        ChatProtocol.FishMessage.Builder messageBuilder = ChatProtocol.FishMessage.newBuilder();
        ChatProtocol.FishUser.Builder fromUser = ChatProtocol.FishUser.newBuilder();
        fromUser.setUid(from);
        ChatProtocol.FishUser.Builder toUser = ChatProtocol.FishUser.newBuilder();
        toUser.setUid(to);
        messageBuilder.setFrom(fromUser);
        messageBuilder.setTo(toUser);
        messageBuilder.setTime(System.currentTimeMillis());
        messageBuilder.setType(1);
        ChatProtocol.FishMessageBody.Builder messageBodyBuilder = ChatProtocol.FishMessageBody.newBuilder();
        messageBodyBuilder.setTemplateId(0);
        messageBodyBuilder.setText(text);
        messageBodyBuilder.setType(1);
        messageBuilder.setBody(messageBodyBuilder);
        builder.addMessages(messageBuilder);
        return builder.build().toByteArray();
    }

    public static Message convertMessage(ChatProtocol.FishMessage fishMessage) {
        int mediaType = fishMessage.getBody().getType();
        Message message = null;
        switch (mediaType) {
            case Message.MESSAGE_MEDIA_TYPE_TEXT:
                TextMessage textMessage = new TextMessage();
                textMessage.setText(fishMessage.getBody().getText());
                message = textMessage;
                break;
            case Message.MESSAGE_MEDIA_TYPE_SOUND:
                SoundMessage soundMessage = new SoundMessage();
                ChatProtocol.FishSound fishSound = fishMessage.getBody().getSound();
                soundMessage.setUrl(fishSound.getUrl());
                soundMessage.setDuration(fishSound.getDuration());
                message = soundMessage;
                break;
            case Message.MESSAGE_MEDIA_TYPE_IMAGE:
                ImageMessage imageMessage = new ImageMessage();
                ChatProtocol.FishImage fishImage = fishMessage.getBody().getImage();
                ChatProtocol.FishImageInfo tinyImageInfo = fishImage.getTinyImage();
                imageMessage.setTinyImage(new ImageInfo(tinyImageInfo.getUrl(), tinyImageInfo.getWidth(), tinyImageInfo.getHeight()));
                ChatProtocol.FishImageInfo originImageInfo = fishImage.getOriginImage();
                imageMessage.setOriginImage(new ImageInfo(originImageInfo.getUrl(), originImageInfo.getWidth(), originImageInfo.getHeight()));
                message = imageMessage;
                break;
            case Message.MESSAGE_MEDIA_TYPE_ACTION:
                ActionMessage actionMessage = new ActionMessage();
                ChatProtocol.FishAction fishAction = fishMessage.getBody().getAction();
                actionMessage.setActionType(fishAction.getAid());
                actionMessage.setExtend(fishAction.getExtend());
                message = actionMessage;
                break;
            case Message.MESSAGE_MEDIA_TYPE_ARTICLE:
                List<ChatProtocol.FishArticle> fishArticleList = fishMessage.getBody().getArticlesList();
                message = convert2ArticleMessage(fishArticleList);
                break;
            case Message.MESSAGE_MEDIA_TYPE_NOTIFY:
                NotifyMessage notifyMessage = new NotifyMessage();
                ChatProtocol.FishNotify notify = fishMessage.getBody().getNotify();
                notifyMessage.setText(notify.getText());
                notifyMessage.setUrl(notify.getUrl());
                message = notifyMessage;
                break;
            case Message.MESSAGE_MEDIA_TYPE_DIALOG:
                ChatProtocol.FishDialog dialog = fishMessage.getBody().getDialog();
                message = convert2DialogMessage(dialog);
                break;
            default:
                LoggerManager.warn("find undefined mediaType " + mediaType);
                return null;
        }
        message.setClientMsgId(fishMessage.getMid());
        message.setType(fishMessage.getType());
        message.setTemplateId(fishMessage.getBody().getTemplateId());
        message.setFrom(new ChatUser(fishMessage.getFrom().getUid()));
        message.setTo(new ChatUser(fishMessage.getTo().getUid()));
        message.setId(fishMessage.getMid());
        message.setReceived(fishMessage.getReceived());
        message.setHeadTitle(fishMessage.getBody().getHeadTitle());
        return message;
    }

    public static ChatProtocol.FishMessage convertFishMessage(Message message) {
        ChatProtocol.FishMessage.Builder builder = ChatProtocol.FishMessage.newBuilder();

        ChatProtocol.FishUser.Builder fromBuilder = ChatProtocol.FishUser.newBuilder();
        fromBuilder.setUid(message.getFrom().getUid());
        fromBuilder.setName(StringUtils.defaultString(message.getFrom().getName()));
        fromBuilder.setAvatar(StringUtils.defaultString(message.getFrom().getAvatar()));


        ChatProtocol.FishUser.Builder toBuilder = ChatProtocol.FishUser.newBuilder();
        toBuilder.setUid(message.getTo().getUid());
        toBuilder.setName(StringUtils.defaultString(message.getTo().getName()));
        toBuilder.setAvatar(StringUtils.defaultString(message.getTo().getAvatar()));


        builder.setFrom(fromBuilder);
        builder.setTo(toBuilder);
        int mediaType = message.getMediaType();
        ChatProtocol.FishMessageBody.Builder bodyBuilder = ChatProtocol.FishMessageBody.newBuilder();
        bodyBuilder.setType(mediaType);
        bodyBuilder.setTemplateId(message.getTemplateId());
        bodyBuilder.setHeadTitle(StringUtils.defaultString(message.getHeadTitle()));
        switch (mediaType) {
            case Message.MESSAGE_MEDIA_TYPE_TEXT:
                TextMessage textMessage = (TextMessage) message;
                bodyBuilder.setText(textMessage.getText());
                break;
            case Message.MESSAGE_MEDIA_TYPE_SOUND:
                SoundMessage soundMessage = (SoundMessage) message;
                ChatProtocol.FishSound.Builder soundBuilder = ChatProtocol.FishSound.newBuilder();
                soundBuilder.setDuration(soundMessage.getDuration());
                soundBuilder.setUrl(soundMessage.getUrl());
                bodyBuilder.setSound(soundBuilder);
                break;
            case Message.MESSAGE_MEDIA_TYPE_IMAGE:
                ImageMessage imageMessage = (ImageMessage) message;
                ChatProtocol.FishImage.Builder imageBuilder = ChatProtocol.FishImage.newBuilder();
                ChatProtocol.FishImageInfo.Builder tinyImageInfoBuilder = ChatProtocol.FishImageInfo.newBuilder();
                tinyImageInfoBuilder.setUrl(imageMessage.getTinyImage().getUrl());
                tinyImageInfoBuilder.setWidth(imageMessage.getTinyImage().getWidth());
                tinyImageInfoBuilder.setHeight(imageMessage.getTinyImage().getHeight());
                imageBuilder.setTinyImage(tinyImageInfoBuilder);
                ChatProtocol.FishImageInfo.Builder originImageInfoBuilder = ChatProtocol.FishImageInfo.newBuilder();
                originImageInfoBuilder.setUrl(imageMessage.getOriginImage().getUrl());
                originImageInfoBuilder.setWidth(imageMessage.getOriginImage().getWidth());
                originImageInfoBuilder.setHeight(imageMessage.getOriginImage().getHeight());
                imageBuilder.setOriginImage(originImageInfoBuilder);
                bodyBuilder.setImage(imageBuilder);
                break;
            case Message.MESSAGE_MEDIA_TYPE_ACTION:
                ActionMessage actionMessage = (ActionMessage) message;
                ChatProtocol.FishAction.Builder actionBuilder = ChatProtocol.FishAction.newBuilder();
                actionBuilder.setAid(actionMessage.getActionType().getType());
                if (StringUtils.isNotBlank(actionMessage.getExtend())) {
                    actionBuilder.setExtend(actionMessage.getExtend());
                }
                bodyBuilder.setAction(actionBuilder);
                break;
            case Message.MESSAGE_MEDIA_TYPE_ARTICLE:
                ArticleMessage articleMessage = (ArticleMessage) message;
                bodyBuilder.addAllArticles(convert2FishArticleList(articleMessage));
                break;
            case Message.MESSAGE_MEDIA_TYPE_NOTIFY:
                NotifyMessage notifyMessage = (NotifyMessage) message;
                ChatProtocol.FishNotify.Builder notifyBuilder = ChatProtocol.FishNotify.newBuilder();
                notifyBuilder.setText(notifyMessage.getText());
                if (StringUtils.isNotEmpty(notifyMessage.getUrl())) {
                    notifyBuilder.setUrl(notifyMessage.getUrl());
                }
                bodyBuilder.setNotify(notifyBuilder);
                break;
            case Message.MESSAGE_MEDIA_TYPE_DIALOG:
                DialogMessage dialogMessage = (DialogMessage) message;
                bodyBuilder.setDialog(convert2FishDialog(dialogMessage));
                break;
        }
        Date date = message.getCreateTime();
        builder.setTime(date != null ? date.getTime() : System.currentTimeMillis());
        builder.setMid(message.getId());
        builder.setBody(bodyBuilder);
        builder.setOffline(message.isOffline());
        builder.setReceived(message.isReceived());
        builder.setType(message.getType());
        if (StringUtils.isNotBlank(message.getPushMessage())) {
            builder.setPushText(message.getPushMessage());
        }
        return builder.build();
    }

    public static ChatProtocol.FishMessage getKickOffMessage(long userId) {
        return convertFishMessage(MessageUtil.createKickOffMessage(userId));
    }

    public static ChatProtocol.FishChatProtocol getMessageProtocol(ChatProtocol.FishMessage fishMessage) {
        ChatProtocol.FishChatProtocol.Builder builder = ChatProtocol.FishChatProtocol.newBuilder();
        builder.addMessages(fishMessage);
        builder.setType(1);
        return builder.build();
    }

    public static ChatProtocol.FishChatProtocol getMessageSyncProtocol(long clientMsgId, long msgId) {
        ChatProtocol.FishChatProtocol.Builder builder = ChatProtocol.FishChatProtocol.newBuilder();

        ChatProtocol.FishMessageSync.Builder messageSyncBuilder = ChatProtocol.FishMessageSync.newBuilder();
        messageSyncBuilder.setClientMid(clientMsgId);
        messageSyncBuilder.setServerMid(msgId);

        builder.addMessageSync(messageSyncBuilder);
        builder.setType(5);
        return builder.build();
    }

    public static ArticleMessage convert2ArticleMessage(List<ChatProtocol.FishArticle> fishArticleList) {
        ArticleMessage articleMessage = new ArticleMessage();
        List<ArticleItem> articleItemList = new ArrayList<ArticleItem>();
        for (ChatProtocol.FishArticle fishArticle : fishArticleList) {
            ArticleItem articleItem = new ArticleItem();
            articleItem.setTitle(fishArticle.getTitle());
            articleItem.setUrl(fishArticle.getUrl());
            articleItem.setDescription(fishArticle.getDescription());
            articleItem.setPicUrl(fishArticle.getPicUrl());
            articleItem.setTemplateId(fishArticle.getTemplateId());
            articleItem.setBottomText(fishArticle.getBottomText());
            articleItem.setTimeout(fishArticle.getTimeout());
            articleItemList.add(articleItem);
        }
        articleMessage.setArticleItemList(articleItemList);
        return articleMessage;
    }

    public static List<ChatProtocol.FishArticle> convert2FishArticleList(ArticleMessage articleMessage) {
        List<ChatProtocol.FishArticle> fishArticleList = new ArrayList<ChatProtocol.FishArticle>();
        for (ArticleItem articleItem : articleMessage.getArticleItemList()) {
            ChatProtocol.FishArticle.Builder builder = ChatProtocol.FishArticle.newBuilder();
            builder.setTitle(articleItem.getTitle());
            builder.setDescription(articleItem.getDescription());
            builder.setUrl(articleItem.getUrl());
            builder.setPicUrl(articleItem.getPicUrl());
            builder.setTemplateId(articleItem.getTemplateId());
            builder.setBottomText(articleItem.getBottomText());
            builder.setTimeout(articleItem.getTimeout());
            fishArticleList.add(builder.build());
        }
        return fishArticleList;
    }

    public static DialogMessage convert2DialogMessage(ChatProtocol.FishDialog dialog) {
        DialogMessage dialogMessage = new DialogMessage();
        dialogMessage.setText(dialog.getText());
        List<ChatProtocol.FishButton> buttonList = dialog.getButtonsList();
        List<DialogButton> dialogButtonList = new ArrayList<DialogButton>();
        for (ChatProtocol.FishButton fishButton : buttonList) {
            DialogButton button = new DialogButton();
            button.setText(fishButton.getText());
            button.setUrl(fishButton.getUrl());
            dialogButtonList.add(button);
        }
        dialogMessage.setDialogType(dialog.getType());
        dialogMessage.setButtons(dialogButtonList);
        dialogMessage.setOperated(dialog.getOperated());
        dialogMessage.setClickMore(dialog.getClickMore());
        return dialogMessage;
    }

    public static ChatProtocol.FishDialog convert2FishDialog(DialogMessage dialog) {
        ChatProtocol.FishDialog.Builder builder = ChatProtocol.FishDialog.newBuilder();
        builder.setText(dialog.getText());
        builder.setClickMore(dialog.isClickMore());
        builder.setType(dialog.getDialogType());
        builder.setOperated(dialog.isOperated());
        for (DialogButton button : dialog.getButtons()) {
            ChatProtocol.FishButton.Builder buttonBuilder = ChatProtocol.FishButton.newBuilder();
            buttonBuilder.setText(button.getText());
            buttonBuilder.setUrl(button.getUrl());
            builder.addButtons(buttonBuilder);
        }
        return builder.build();
    }
}
