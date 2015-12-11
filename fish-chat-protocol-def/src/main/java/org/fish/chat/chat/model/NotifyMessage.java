/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.model;

import cn.techwolf.blue.common.constants.ProtocalConstants;
import net.sf.json.JSONObject;

/**
 * Comments for NotifyMessage.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年9月4日 下午6:55:35
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
        return ProtocalConstants.URL_PATH + "?type=f2";
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
