/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.model;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.fish.chat.common.constants.ProtocalConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Comments for ResumeMessage.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年11月14日 下午4:51:53
 */
public class ResumeMessage extends Message {

    /**
     * 
     */
    private static final long serialVersionUID = -167513829565604974L;

    private static final String JSON_PROPERTIES_USER = "user";

    private static final String JSON_PROPERTIES_WORK_YEAR_DESC = "workYearDesc";

    private static final String JSON_PROPERTIES_SALARY_TYPE = "salaryType";

    private static final String JSON_PROPERTIES_LOW_SALARY = "lowSalary";

    private static final String JSON_PROPERTIES_HIGH_SALARY = "highSalary";

    private static final String JSON_PROPERTIES_WANT_JOBS = "wantJobs";

    private static final String JSON_PROPERTIES_DID_JOBS = "didJobs";

    private static final String JSON_PROPERTIES_DECLARATION = "declaration";

    private ChatUser user;

    private String workYearDesc;

    private int salaryType;

    private int lowSalary;

    private int highSalary;

    private List<String> wantJobs;

    private List<String> didJobs;

    private String declaration;

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#getMediaType()
     */
    @Override
    public int getMediaType() {
        return MESSAGE_MEDIA_TYPE_RESUME;
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#getMediaBody()
     */
    @Override
    public String getMediaBody() {
        JSONObject jsonObject = new JSONObject();
        if (user != null) {
            jsonObject.put(JSON_PROPERTIES_USER, user.toJsonObject());
        }
        jsonObject.put(JSON_PROPERTIES_WORK_YEAR_DESC, StringUtils.defaultString(workYearDesc));
        jsonObject.put(JSON_PROPERTIES_SALARY_TYPE, salaryType);
        jsonObject.put(JSON_PROPERTIES_LOW_SALARY, lowSalary);
        jsonObject.put(JSON_PROPERTIES_HIGH_SALARY, highSalary);
        JSONArray arrayWant = new JSONArray();
        if (wantJobs != null && wantJobs.size() > 0) {
            for (String wantJob : wantJobs) {
                arrayWant.add(wantJob);
            }
        }
        jsonObject.put(JSON_PROPERTIES_WANT_JOBS, arrayWant);
        JSONArray arrayDid = new JSONArray();
        if (didJobs != null && didJobs.size() > 0) {
            for (String didJob : didJobs) {
                arrayDid.add(didJob);
            }
        }
        jsonObject.put(JSON_PROPERTIES_DID_JOBS, arrayDid);
        jsonObject.put(JSON_PROPERTIES_DECLARATION, StringUtils.defaultString(declaration));
        return jsonObject.toString();
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#setMediaBody(java.lang.String)
     */
    @Override
    public boolean setMediaBody(String json) {
        JSONObject jsonObject = JSONObject.fromObject(json);
        user = ChatUser.fromJson(jsonObject.optJSONObject(JSON_PROPERTIES_USER));
        workYearDesc = jsonObject.optString(JSON_PROPERTIES_WORK_YEAR_DESC);
        salaryType = jsonObject.optInt(JSON_PROPERTIES_SALARY_TYPE);
        lowSalary = jsonObject.optInt(JSON_PROPERTIES_LOW_SALARY);
        highSalary = jsonObject.optInt(JSON_PROPERTIES_HIGH_SALARY);
        JSONArray arrayWant = jsonObject.optJSONArray(JSON_PROPERTIES_WANT_JOBS);
        wantJobs = new ArrayList<String>();
        if (arrayWant != null && arrayWant.size() > 0) {
            for (int i = 0; i < arrayWant.size(); i++) {
                wantJobs.add(arrayWant.optString(i));
            }
        }
        JSONArray arrayDid = jsonObject.optJSONArray(JSON_PROPERTIES_DID_JOBS);
        didJobs = new ArrayList<String>();
        if (arrayDid != null && arrayDid.size() > 0) {
            for (int i = 0; i < arrayDid.size(); i++) {
                didJobs.add(arrayDid.optString(i));
            }
        }
        declaration = jsonObject.optString(JSON_PROPERTIES_DECLARATION);
        return true;
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#getPushMessage()
     */
    @Override
    public String getPushMessage() {
        return null;
    }

    @Override
    public String getPushUrl() {
        return ProtocalConstants.URL_PATH + "?type=f2";
    }

    /**
     * @return the user
     */
    public ChatUser getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(ChatUser user) {
        this.user = user;
    }

    public String getWorkYearDesc() {
        return workYearDesc;
    }

    public void setWorkYearDesc(String workYearDesc) {
        this.workYearDesc = workYearDesc;
    }

    public int getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(int salaryType) {
        this.salaryType = salaryType;
    }

    public int getLowSalary() {
        return lowSalary;
    }

    public void setLowSalary(int lowSalary) {
        this.lowSalary = lowSalary;
    }

    public int getHighSalary() {
        return highSalary;
    }

    public void setHighSalary(int highSalary) {
        this.highSalary = highSalary;
    }

    public List<String> getWantJobs() {
        return wantJobs;
    }

    public void setWantJobs(List<String> wantJobs) {
        this.wantJobs = wantJobs;
    }

    public List<String> getDidJobs() {
        return didJobs;
    }

    public void setDidJobs(List<String> didJobs) {
        this.didJobs = didJobs;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }
}
