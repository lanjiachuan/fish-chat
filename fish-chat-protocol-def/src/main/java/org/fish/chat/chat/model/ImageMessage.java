package org.fish.chat.chat.model;

import com.google.gson.annotations.Expose;
import net.sf.json.JSONObject;
import org.fish.chat.common.constants.ProtocolConstants;

import java.io.Serializable;

/**
 * 图片消息
 *
 * @author adre
 */
public class ImageMessage extends Message implements Serializable{

    private static final long serialVersionUID = 8717449128122017015L;

    private static final String JSON_KEY_TINY_URL = "tinyImage";

    private static final String JSON_KEY_ORIGIN_URL = "originImage";

    @Expose
    private ImageInfo tinyImage;

    @Expose
    private ImageInfo originImage;

    @Override
    public int getMediaType() {
        return MESSAGE_MEDIA_TYPE_IMAGE;
    }

    @Override
    public String getMediaBody() {
        JSONObject object = new JSONObject();
        object.put(JSON_KEY_TINY_URL, tinyImage.getJsonObject());
        object.put(JSON_KEY_ORIGIN_URL, originImage.getJsonObject());
        return object.toString();
    }

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

    @Override
    public String getPushMessage() {
        StringBuffer sb = new StringBuffer();
        sb.append(getFrom().getName());
        sb.append("发来一张图片");
        return sb.toString();
    }

    @Override
    public String getPushUrl() {
        return ProtocolConstants.URL_PATH + "?type=f2";
    }

}
