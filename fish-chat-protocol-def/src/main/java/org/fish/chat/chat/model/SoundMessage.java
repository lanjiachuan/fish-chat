package org.fish.chat.chat.model;

import com.google.gson.annotations.Expose;
import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 * Comments for SoundMessage.java
 *
 */
public class SoundMessage extends Message implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5704749623015842360L;

    private static final String JSON_KEY_URL = "url";

    private static final String JSON_KEY_DURATION = "duration";

    @Expose
    private String url;

    @Expose
    private int duration;

    /* (non-Javadoc)
     * @see cn.techwolf.chat.protocol.message.AbstractMessage#getMessageMediaType()
     */
    @Override
    public int getMediaType() {
        return MESSAGE_MEDIA_TYPE_SOUND;
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
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /* (non-Javadoc)
     * @see cn.techwolf.chat.protocol.message.Message#getMediaJsonBody()
     */
    @Override
    public String getMediaBody() {
        JSONObject object = new JSONObject();
        object.put(JSON_KEY_URL, url);
        object.put(JSON_KEY_DURATION, duration);
        return object.toString();
    }

    /* (non-Javadoc)
     * @see cn.techwolf.chat.protocol.message.Message#setMediaJsonBody(java.lang.String)
     */
    @Override
    public boolean setMediaBody(String json) {
        JSONObject object = JSONObject.fromObject(json);
        url = object.optString(JSON_KEY_URL);
        duration = object.optInt(JSON_KEY_DURATION);
        return true;
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#getPushMessage()
     */
    @Override
    public String getPushMessage() {
        StringBuffer sb = new StringBuffer();
        sb.append(getFrom().getName());
        sb.append("发来一段语音");
        return sb.toString();
    }

    @Override
    public String getPushUrl() {
        return null;
    }

}
