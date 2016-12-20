package org.fish.chat.chat.model;

import net.sf.json.JSONObject;
import org.fish.chat.common.constants.ProtocolConstants;

/**
 * Comments for NotifyMessage.java
 *
 */
public class NotifyMessage extends Message {

    private static final long serialVersionUID = 2080081615321396799L;

    private static final String JSON_KEY_TEXT = "text";

    private static final String JSON_KEY_URL = "url";

    private String text;

    private String url;

    @Override
    public int getMediaType() {
        return MESSAGE_MEDIA_TYPE_NOTIFY;
    }

    @Override
    public String getMediaBody() {
        JSONObject object = new JSONObject();
        object.put(JSON_KEY_URL, url);
        object.put(JSON_KEY_TEXT, text);
        return object.toString();
    }

    @Override
    public boolean setMediaBody(String json) {
        JSONObject object = JSONObject.fromObject(json);
        this.text = object.optString(JSON_KEY_TEXT);
        this.url = object.optString(JSON_KEY_URL);
        return true;
    }

    @Override
    public String getPushMessage() {
        return text;
    }

    @Override
    public String getPushUrl() {
        return ProtocolConstants.URL_PATH + "?type=f2";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
