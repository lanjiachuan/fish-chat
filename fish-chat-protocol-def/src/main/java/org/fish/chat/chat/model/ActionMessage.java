package org.fish.chat.chat.model;

import org.fish.chat.common.log.LoggerManager;
import com.google.gson.annotations.Expose;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.fish.chat.common.constants.ProtocolConstants;

import java.io.Serializable;

/**
 * 动作消息
 *
 * @author adre
 */
public class ActionMessage extends Message implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6376505946229384204L;

    private static final String JSON_KEY_TYPE = "type";

    private static final String JSON_KEY_EXTEND = "extend";

    public enum ActionType {
        UNDEFINED(0),
        REMIND_REPLY(1),
        EXCHANGE_CONTACT(2),
        CONTACT_ACCEPT(3),
        CONTACT_REJECT(4),
        KICK_OFF(5),
        EXCHANGE_WEIXIN(6),
        WEIXIN_ACCEPT(7),
        WEIXIN_REJECT(8),
        IDENTITY_FREEZE(9);

        ActionType(int type) {
            this.type = type;
        }

        private int type;

        /**
         * @return the type
         */
        public int getType() {
            return type;
        }

        public static ActionType getActionType(int type) {
            LoggerManager.info("get action type : " + type);
            for (ActionType actionType : ActionType.values()) {
                if (actionType.getType() == type) {
                    return actionType;
                }
            }
            LoggerManager.info("not find action type : " + type);
            return UNDEFINED;
        }
    }

    @Expose
    private ActionType actionType;

    private String extend;

    public ActionMessage() {

    }

    public ActionMessage(int type) {
        this(ActionType.getActionType(type));
    }

    public ActionMessage(ActionType actionType) {
        this.actionType = actionType;
    }

    @Override
    public int getMediaType() {
        return Message.MESSAGE_MEDIA_TYPE_ACTION;
    }

    @Override
    public String getMediaBody() {
        JSONObject object = new JSONObject();
        object.put(JSON_KEY_TYPE, actionType.getType());
        object.put(JSON_KEY_EXTEND, StringUtils.defaultString(extend));
        return object.toString();
    }

    @Override
    public boolean setMediaBody(String json) {
        JSONObject object = JSONObject.fromObject(json);
        this.actionType = ActionType.getActionType(object.optInt(JSON_KEY_TYPE));
        this.extend = object.optString(JSON_KEY_EXTEND);
        return true;
    }

    public void setActionType(int type) {
        this.actionType = ActionType.getActionType(type);
    }

    /**
     * @param actionType the actionType to set
     */
    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    /**
     * @return the actionType
     */
    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public String getPushMessage() {
        StringBuffer sb = new StringBuffer();
        sb.append(getFrom().getName());
        switch (actionType) {
            case REMIND_REPLY:
                sb.append("提醒您回复");
                break;
            case EXCHANGE_CONTACT:
                sb.append("想要和你交换电话，是否同意");
                break;
            case CONTACT_ACCEPT:
                sb.append("接受与您交换联系方式");
                break;
            case CONTACT_REJECT:
                sb.append("拒绝与您交换联系方式");
                return "";
            case EXCHANGE_WEIXIN:
                sb.append("请求与您交换微信");
                break;
            case WEIXIN_ACCEPT:
                sb.append("接受与您交换微信");
                break;
            case WEIXIN_REJECT:
                sb.append("拒绝与您交换微信");
                return "";
            default:
                return "";
        }
        return sb.toString();
    }

    @Override
    public String getPushUrl() {
        return ProtocolConstants.URL_PATH + "?type=f2";
    }

    @Override
    public String toString() {
        return "[messageId=" + id + ", type=" + type + ",mediaType=" + MESSAGE_MEDIA_TYPE_ACTION
                + ",templateId=" + templateId + "action=" + actionType + ", extend=" + extend + "]";
    }

    /**
     * @return the extend
     */
    public String getExtend() {
        return extend;
    }

    /**
     * @param extend the extend to set
     */
    public void setExtend(String extend) {
        this.extend = extend;
    }

}
