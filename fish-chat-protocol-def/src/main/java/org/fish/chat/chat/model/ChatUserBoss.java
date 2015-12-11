package org.fish.chat.chat.model;

import com.google.gson.annotations.Expose;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created by guo on 15-11-5.
 */
public class ChatUserBoss implements Serializable {

    private static final long serialVersionUID = 28354246548422973L;

    private static final String JSON_PROPERTIES_UID = "uid";
    private static final String JSON_PROPERTIES_COMPANY_NAME = "companyName";
    private static final String JSON_PROPERTIES_ADDRESS = "address";
    private static final String JSON_PROPERTIES_APPROVE_STATUS = "approveStatus";
    private static final String JSON_PROPERTIES_JOB_TITLE = "jobTitle";

    @Expose
    private long uid;
    @Expose
    private String companyName;
    @Expose
    private String address;
    @Expose
    private int approveStatus;
    @Expose
    private String jobTitle;

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_PROPERTIES_UID, uid);
        jsonObject.put(JSON_PROPERTIES_COMPANY_NAME, StringUtils.defaultString(companyName));
        jsonObject.put(JSON_PROPERTIES_ADDRESS, StringUtils.defaultString(address));
        jsonObject.put(JSON_PROPERTIES_APPROVE_STATUS, approveStatus);
        jsonObject.put(JSON_PROPERTIES_JOB_TITLE, StringUtils.defaultString(jobTitle));
        return jsonObject;
    }

    public static ChatUserBoss fromJson(JSONObject jsonObject) {
        if (jsonObject != null) {
            ChatUserBoss chatUserBoss = new ChatUserBoss();
            chatUserBoss.setUid(jsonObject.optLong(JSON_PROPERTIES_UID));
            chatUserBoss.setCompanyName(jsonObject.optString(JSON_PROPERTIES_COMPANY_NAME));
            chatUserBoss.setAddress(jsonObject.optString(JSON_PROPERTIES_ADDRESS));
            chatUserBoss.setApproveStatus(jsonObject.optInt(JSON_PROPERTIES_APPROVE_STATUS));
            chatUserBoss.setJobTitle(jsonObject.optString(JSON_PROPERTIES_JOB_TITLE));
            return chatUserBoss;
        }
        return null;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(int approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
