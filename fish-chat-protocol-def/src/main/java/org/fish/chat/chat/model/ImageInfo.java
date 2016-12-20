
package org.fish.chat.chat.model;

import com.google.gson.annotations.Expose;
import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 * Comments for ImageInfo.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月24日 上午10:49:11
 */
public class ImageInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7417491336204331071L;

    private static final String JSON_KEY_URL = "url";

    private static final String JSON_KEY_WIDTH = "width";

    private static final String JSON_KEY_HEIGHT = "height";

    @Expose
    private String url;

    @Expose
    private int width;

    @Expose
    private int height;

    public ImageInfo() {
    }

    public ImageInfo(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public ImageInfo(String json) {
        this(JSONObject.fromObject(json));
    }

    public ImageInfo(JSONObject object) {
        url = object.optString(JSON_KEY_URL);
        width = object.optInt(JSON_KEY_WIDTH);
        height = object.optInt(JSON_KEY_HEIGHT);
    }

    public String getJsonString() {
        return getJsonObject().toString();
    }

    public JSONObject getJsonObject() {
        JSONObject object = new JSONObject();
        object.put(JSON_KEY_URL, url);
        object.put(JSON_KEY_WIDTH, width);
        object.put(JSON_KEY_HEIGHT, height);
        return object;
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
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

}
