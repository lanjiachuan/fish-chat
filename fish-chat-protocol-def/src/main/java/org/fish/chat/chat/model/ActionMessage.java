
package org.fish.chat.chat.model;

import org.fish.chat.common.log.LoggerManager;
import com.google.gson.annotations.Expose;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.fish.chat.common.constants.ProtocolConstants;

import java.io.Serializable;

/**
 * Comments for ActionMessage.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月24日 上午11:19:35
 */
public class ActionMessage extends Message implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6376505946229384204L;

    private static final String JSON_KEY_TYPE = "type";

    private static final String JSON_KEY_EXTEND = "extend";

    public enum ActionType {
        UNDEFINED(0), REMIND_REPLY(1), INSTERESTED(2), NOTINSTERESTED(3), REQUEST_REVIEW(4), //NL
        REVIEW_ACCEPT(5), REVIEW_REJECT(6),
        EXCHANGE_CONTACT(7), CONTACT_ACCEPT(8), CONTACT_REJECT(9),
        KICK_OFF(10), CANCEL_SAY_HELLO(11), CAN_PHONE_CALL_GRAY(12), //NL
        CAN_PHONE_CALL(13), PHONE_INTERVIEW(14), ANSWER_QUESTION(15), READ_ANSWER(16), //NL
        ACCEPT_ANSWER(17), REJECT_ANSWER(18), BOSS_DIRECT_CHAT(19), DIALOG_ACTION(20), //NL
        REQUEST_RESUME(21), RESUME_ACCEPT(22), RESUME_REJECT(23), //NL
        REQUEST_REVIEW3(24), SOMEBODY_VIEW(30), SOMEBODY_INSTEREST(31),//NL
        EXCHANGE_WEIXIN(32), WEIXIN_ACCEPT(33) , WEIXIN_REJECT(34), SOMEBODY_NEW_GEEK_OR_JOB(35),
        CHOICE_QUESTION_ITEM(36),
        REQUEST_GET_RESUME(37),GET_RESUME_ACCEPT(38),GET_RESUME_REJECT(39),
        REQUEST_SEND_RESUME(40),SEND_RESUME_ACCEPT(41),SEND_RESUME_REJECT(42), MATE_SHARE_GEEK(43), IDENTITY_FREEZE(44), BOSS_NOTICE(45), GEEK_NOTICE(46);

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

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#getMediaType()
     */
    @Override
    public int getMediaType() {
        return Message.MESSAGE_MEDIA_TYPE_ACTION;
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#getMediaBody()
     */
    @Override
    public String getMediaBody() {
        JSONObject object = new JSONObject();
        object.put(JSON_KEY_TYPE, actionType.getType());
        object.put(JSON_KEY_EXTEND, StringUtils.defaultString(extend));
        return object.toString();
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#setMediaBody(java.lang.String)
     */
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

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#getPushMessage()
     */
    @Override
    public String getPushMessage() {
        StringBuffer sb = new StringBuffer();
        sb.append(getFrom().getName());
        switch (actionType) {
            case REMIND_REPLY:
                sb.append("提醒您回复");
                break;
            case REQUEST_REVIEW:
            case REQUEST_REVIEW3:
                sb.append("邀请您参加面试");
                break;
            case REVIEW_ACCEPT:
                sb.append("接受了您的面试请求");
                break;
            case REVIEW_REJECT:
                sb.append("拒绝了您的面试请求");
                break;
            case EXCHANGE_CONTACT:
                sb.append("想要和你交换电话，是否同意");
                break;
            case CONTACT_ACCEPT:
                sb.append("接受与您交换联系方式");
                break;
            case CONTACT_REJECT:
                //拒绝后会发送提示文案，所以会重复，取消此处
                //sb.append("拒绝与您交换联系方式");
                return "";
            case EXCHANGE_WEIXIN:
                sb.append("请求与您交换微信");
                break;
            case WEIXIN_ACCEPT:
                sb.append("接受与您交换微信");
                break;
            case WEIXIN_REJECT:
                //拒绝后会发送提示文案，所以会重复，取消此处
                //sb.append("拒绝与您交换微信");
                return "";
            case REQUEST_RESUME:
                sb.append("请求下载您的简历");
                break;
            default:
                return "";
        }
        return sb.toString();
    }

    @Override
    public String getPushUrl() {
        return ProtocolConstants.URL_PATH + "?type=f2";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
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
