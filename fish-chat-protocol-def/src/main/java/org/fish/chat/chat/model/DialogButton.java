
package org.fish.chat.chat.model;

import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 * Comments for DialogButton.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年9月11日 下午4:51:39
 */
public class DialogButton implements Serializable {

    private static final long serialVersionUID = 8898363992522055814L;

    private static final String JSON_PROPERTIES_TEXT = "text";

    private static final String JSON_PROPERTIES_URL = "url";

    private String text;

    private String url;

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_PROPERTIES_TEXT, text);
        jsonObject.put(JSON_PROPERTIES_URL, url);
        return jsonObject;
    }

    public static DialogButton parseFrom(JSONObject jsonObject) {
        DialogButton dialogButton = new DialogButton();
        dialogButton.setText(jsonObject.optString(JSON_PROPERTIES_TEXT));
        dialogButton.setUrl(jsonObject.optString(JSON_PROPERTIES_URL));
        return dialogButton;
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
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
