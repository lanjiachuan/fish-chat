package org.fish.chat.chat.model;


import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 * 图文消息item
 *
 * @author adre
 */
public class ArticleItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7244769529207009L;

    public static final int TYPE_BIG = 0;

    public static final int TYPE_SMALL = 1;

    public static final int TYPE_FULLSCREEN = 2;

    private static final String JSON_PROPERTY_TITLE = "title";

    private static final String JSON_PROPERTY_URL = "url";

    private static final String JSON_PROPERTY_DESC = "description";

    private static final String JSON_PROPERTY_PIC_URL = "pic_url";

    private static final String JSON_PROPERTY_TEMPLATE_ID = "template_id";

    private static final String JSON_PROPERTY_BOTTOM_TEXT = "bottom_text";

    private static final String JSON_PROPERTY_TIMEOUT = "timeout";

    private String title;

    private String url;

    private String description;

    private String picUrl;

    private int templateId = 0;

    private String bottomText;

    private long timeout;

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_PROPERTY_TITLE, title);
        jsonObject.put(JSON_PROPERTY_URL, url);
        jsonObject.put(JSON_PROPERTY_DESC, description);
        jsonObject.put(JSON_PROPERTY_PIC_URL, picUrl);
        jsonObject.put(JSON_PROPERTY_TEMPLATE_ID, templateId);
        jsonObject.put(JSON_PROPERTY_BOTTOM_TEXT, bottomText);
        jsonObject.put(JSON_PROPERTY_TIMEOUT, timeout);
        return jsonObject;
    }
    
    public static ArticleItem parseFrom(JSONObject jsonObject) {
        ArticleItem articleItem = new ArticleItem();
        articleItem.setTitle(jsonObject.optString(JSON_PROPERTY_TITLE));
        articleItem.setDescription(jsonObject.optString(JSON_PROPERTY_DESC));
        articleItem.setUrl(jsonObject.optString(JSON_PROPERTY_URL));
        articleItem.setPicUrl(jsonObject.optString(JSON_PROPERTY_PIC_URL));
        articleItem.setTemplateId(jsonObject.optInt(JSON_PROPERTY_TEMPLATE_ID));
        articleItem.setBottomText(jsonObject.optString(JSON_PROPERTY_BOTTOM_TEXT));
        articleItem.setTimeout(jsonObject.optLong(JSON_PROPERTY_TIMEOUT));
        return articleItem;
    }
    
    public static ArticleItem parseFrom(String json) {
        return parseFrom(JSONObject.fromObject(json));
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
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

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the picUrl
     */
    public String getPicUrl() {
        return picUrl;
    }

    /**
     * @param picUrl the picUrl to set
     */
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getTimeout() {
        return timeout;
    }
}
