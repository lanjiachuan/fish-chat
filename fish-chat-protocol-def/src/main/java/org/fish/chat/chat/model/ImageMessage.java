/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.model;

import cn.techwolf.blue.common.constants.ProtocalConstants;
import com.google.gson.annotations.Expose;
import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 * Comments for ImageMessage.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月14日 下午7:30:51
 */
public class ImageMessage extends Message implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 8717449128122017015L;

    private static final String JSON_KEY_TINY_URL = "tinyImage";

    private static final String JSON_KEY_ORIGIN_URL = "originImage";

    @Expose
    private ImageInfo tinyImage;

    @Expose
    private ImageInfo originImage;

    /* (non-Javadoc)
     * @see cn.techwolf.chat.protocol.message.AbstractMessage#getMessageMediaType()
     */
    @Override
    public int getMediaType() {
        return MESSAGE_MEDIA_TYPE_IMAGE;
    }

    /* (non-Javadoc)
     * @see cn.techwolf.chat.protocol.message.Message#getMediaJsonBody()
     */
    @Override
    public String getMediaBody() {
        JSONObject object = new JSONObject();
        object.put(JSON_KEY_TINY_URL, tinyImage.getJsonObject());
        object.put(JSON_KEY_ORIGIN_URL, originImage.getJsonObject());
        return object.toString();
    }

    /* (non-Javadoc)
     * @see cn.techwolf.chat.protocol.message.Message#setMediaJsonBody(java.lang.String)
     */
    @Override
    public boolean setMediaBody(String json) {
        JSONObject object = JSONObject.fromObject(json);
        tinyImage = new ImageInfo(object.optJSONObject(JSON_KEY_TINY_URL));
        originImage = new ImageInfo(object.optString(JSON_KEY_ORIGIN_URL));
        return true;
    }

    /**
     * @return the tinyImage
     */
    public ImageInfo getTinyImage() {
        return tinyImage;
    }

    /**
     * @param tinyImage the tinyImage to set
     */
    public void setTinyImage(ImageInfo tinyImage) {
        this.tinyImage = tinyImage;
    }

    /**
     * @return the originImage
     */
    public ImageInfo getOriginImage() {
        return originImage;
    }

    /**
     * @param originImage the originImage to set
     */
    public void setOriginImage(ImageInfo originImage) {
        this.originImage = originImage;
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#getPushMessage()
     */
    @Override
    public String getPushMessage() {
        StringBuffer sb = new StringBuffer();
        sb.append(getFrom().getName());
        sb.append("发来一张图片");
        return sb.toString();
    }

    @Override
    public String getPushUrl() {
        return ProtocalConstants.URL_PATH + "?type=f2";
    }

}
