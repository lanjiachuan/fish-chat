
package org.fish.chat.chat.model;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.fish.chat.common.constants.ProtocolConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Comments for DialogMessage.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年9月11日 下午4:17:21
 */
public class DialogMessage extends Message {

    private static final long serialVersionUID = 8566642767236503212L;

    public static final int DIALOG_TYEP_COMMON = 0;

    public static final int DIALOG_TYEP_WEIXIN = 1;

    private static final String JSON_PROPERTY_TEXT = "text";

    private static final String JSON_PROPERTY_TYPE = "type";

    private static final String JSON_PROPERTY_BUTTONS = "buttons";

    private static final String JSON_PROPERTY_OPERATED = "operated";

    private static final String JSON_PROPERTY_SELECT_INDEX = "select_index";

    private static final String JSON_PROPERTY_CLICK_MORE = "click_more";

    private int dialogType = 0;

    private String text;

    private List<DialogButton> buttons;

    private boolean operated;

    private int selectIndex;

    private boolean clickMore = false;

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#getMediaType()
     */
    @Override
    public int getMediaType() {
        return MESSAGE_MEDIA_TYPE_DIALOG;
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#getMediaBody()
     */
    @Override
    public String getMediaBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_PROPERTY_TEXT, text);
        jsonObject.put(JSON_PROPERTY_TYPE, dialogType);
        JSONArray jsonArray = new JSONArray();
        for (DialogButton button : buttons) {
            jsonArray.add(button.toJSONObject());
        }
        jsonObject.put(JSON_PROPERTY_BUTTONS, jsonArray);
        jsonObject.put(JSON_PROPERTY_OPERATED, operated);
        jsonObject.put(JSON_PROPERTY_SELECT_INDEX, selectIndex);
        jsonObject.put(JSON_PROPERTY_CLICK_MORE, clickMore);
        if (sinceVersion > 0) {
            jsonObject.put(JSON_PROPERTY_SINCE_VERSION, sinceVersion);
        }
        return jsonObject.toString();
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#setMediaBody(java.lang.String)
     */
    @Override
    public boolean setMediaBody(String json) {
        JSONObject jsonObject = JSONObject.fromObject(json);
        text = jsonObject.optString(JSON_PROPERTY_TEXT);
        type = jsonObject.optInt(JSON_PROPERTY_TYPE);
        operated = jsonObject.optBoolean(JSON_PROPERTY_OPERATED);

        JSONArray array = jsonObject.optJSONArray(JSON_PROPERTY_BUTTONS);
        List<DialogButton> buttons = new ArrayList<DialogButton>();
        if (array != null && array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                buttons.add(DialogButton.parseFrom(array.optJSONObject(i)));
            }
        }
        this.buttons = buttons;
        selectIndex = jsonObject.optInt(JSON_PROPERTY_SELECT_INDEX);
        clickMore = jsonObject.optBoolean(JSON_PROPERTY_CLICK_MORE, false);
        dialogType = jsonObject.optInt(JSON_PROPERTY_TYPE, 0);
        sinceVersion = (float) jsonObject.optDouble(JSON_PROPERTY_SINCE_VERSION);
        return true;
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#getPushMessage()
     */
    @Override
    public String getPushMessage() {
        return from.getName() + ":" + getText();
    }

    @Override
    public String getPushUrl() {
        return ProtocolConstants.URL_PATH + "?type=f2";
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the buttons
     */
    public List<DialogButton> getButtons() {
        return buttons;
    }

    /**
     * @param buttons the buttons to set
     */
    public void setButtons(List<DialogButton> buttons) {
        this.buttons = buttons;
    }

    /**
     * @return the operated
     */
    public boolean isOperated() {
        return operated;
    }

    /**
     * @param operated the operated to set
     */
    public void setOperated(boolean operated) {
        this.operated = operated;
    }

    /**
     * @return the selectIndex
     */
    public int getSelectIndex() {
        return selectIndex;
    }

    /**
     * @param selectIndex the selectIndex to set
     */
    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public boolean isClickMore() {
        return clickMore;
    }

    public void setClickMore(boolean clickMore) {
        this.clickMore = clickMore;
    }

    public int getDialogType() {
        return dialogType;
    }

    public void setDialogType(int dialogType) {
        this.dialogType = dialogType;
    }

}
