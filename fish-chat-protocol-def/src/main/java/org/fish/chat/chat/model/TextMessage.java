
package org.fish.chat.chat.model;


import org.fish.chat.common.log.LoggerManager;
import com.google.gson.annotations.Expose;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.fish.chat.common.constants.ChatConstant;
import org.fish.chat.common.constants.ProtocolConstants;
import org.fish.chat.common.utils.SecurityUtils;

import java.io.Serializable;

/**
 * Comments for TextMessage.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月14日 下午7:23:02
 */
public class TextMessage extends Message implements Serializable {

    private static final long serialVersionUID = -7877668704353175789L;
    private static final String JSON_KEY_TEXT = "text";
    private static final String JSON_KEY_ENCRYPT_TEXT = "encrypt_text";

    @Expose
    private String text;

    private boolean encrypted = true;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getMediaType() {
        return MESSAGE_MEDIA_TYPE_TEXT;
    }

    @Override
    public String getMediaBody() {
        JSONObject object = new JSONObject();
        if (getFrom().getUid() > ChatConstant.SYSTEM_USER_ID) {
            object.put(JSON_KEY_ENCRYPT_TEXT, SecurityUtils.rc4Encrypt(text, String.valueOf(getFrom().getUid())));
        } else {
            object.put(JSON_KEY_TEXT, text);
        }
        if (sinceVersion > 0) {
            object.put(JSON_PROPERTY_SINCE_VERSION, sinceVersion);
        }
        writeClientInfoJson(object);
        return object.toString();
    }

    @Override
    public void setTemplateId(int templateId) {
        if (getTemplateId() == Message.MESSAGE_TEMPLATE_MARK && templateId != MESSAGE_TEMPLATE_MARK
                && StringUtils.isNotBlank(text)) {
            text = getNormalString();
        }
        this.templateId = templateId;
    }

    @Override
    public boolean setMediaBody(String json) {
        try {
            JSONObject object = JSONObject.fromObject(json);
            if (object.has(JSON_KEY_TEXT)) {
                setText(object.optString(JSON_KEY_TEXT));
                setEncrypted(false);
            } else if (object.has(JSON_KEY_ENCRYPT_TEXT)) {
                setText(SecurityUtils.rc4Decrypt(object.optString(JSON_KEY_ENCRYPT_TEXT), String.valueOf(getFrom().getUid())));
            }

            sinceVersion = (float)object.optDouble(JSON_PROPERTY_SINCE_VERSION);
            readClientInfoJson(object);
        } catch (Exception e) {
            LoggerManager.error("", e);
            text = json;
        }
        return true;
    }

    @Override
    public String getPushMessage() {
        if (templateId == MESSAGE_TEMPLATE_SKILL) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(getFrom().getName());
        sb.append(":");
        if (getTemplateId() == Message.MESSAGE_TEMPLATE_MARK) {
            sb.append(getNormalString());
        } else {
            sb.append(text);
        }
        return sb.toString();
    }

    @Override
    public String getPushUrl() {
        return ProtocolConstants.URL_PATH + "?type=f2";
    }

    @Override
    public String toString() {
        return "[messageId=" + id + ", type=" + type + ",mediaType=" + MESSAGE_MEDIA_TYPE_TEXT
                + ",templateId=" + templateId + "，text=" + text + ",persistence=" + persistence
                + "]";
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    private String getNormalString() {
        return text.replace("<phone>", "").replace("</phone>", "").replace("<copy>", "").replace("</copy>", "");
    }

}
