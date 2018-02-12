package org.fish.chat.chat.utils;

import org.fish.chat.chat.model.*;
import org.fish.chat.common.constants.ChatConstant;

import java.util.Date;

/**
 * 消息工具类
 *
 * @author adre
 */
public class MessageUtil {

    public static TextMessage createTextMessage(long msgId, long fromId, int identity, long toId, int toIdentity, int templateId, int type, String text) {
        TextMessage message = new TextMessage();
        message.setFrom(fromId, identity);
        message.setTo(toId, toIdentity);
        message.setCreateTime(new Date());
        message.setTemplateId(templateId);
        message.setType(type);
        message.setText(text);
        message.setId(msgId);
        return message;
    }

    public static ImageMessage createImgMessage(long fromUid, long toUid, int templateId, int type, ImageInfo imageInfo, ImageInfo tinyImageInfo) {
        ImageMessage message = new ImageMessage();
        message.setFrom(fromUid);
        message.setTo(toUid);
        message.setCreateTime(new Date());
        message.setTemplateId(templateId);
        message.setType(type);
        message.setOriginImage(imageInfo);
        message.setTinyImage(tinyImageInfo);
        return message;
    }

    public static ActionMessage createKickOffMessage(long toUid) {
        ActionMessage actionMessage = new ActionMessage(ActionMessage.ActionType.KICK_OFF);
        actionMessage.setTo(toUid);
        actionMessage.setFrom(ChatConstant.SYSTEM_USER_ID);
        actionMessage.setType(Message.MESSAGE_TYPE_SYSTEM);
        actionMessage.setCreateTime(new Date());
        actionMessage.setId(System.currentTimeMillis());
        return actionMessage;
    }
}
