/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.model;

import cn.techwolf.blue.common.constants.ProtocalConstants;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Comments for JobDescMessage.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年9月13日 下午3:03:50
 */
public class JobDescMessage extends Message {

    private static final long serialVersionUID = 2348088444454784886L;

    //jobId
    private long jobId;
    //标题
    private String title;
    //职位code
    private int code;
    //职位名称
    private String codeDesc;
    //职位类型
    private int kind;
    //职位类型描述
    private String kindDesc;
    //薪资类型
    private int salaryType;
    //最低薪资
    private int lowSalary;
    //最高薪资
    private int highSalary;
    //学历
    private int degree;
    //学历描述
    private String degreeDesc;
    //工作经验
    private int experience;
    //工作经验描述
    private String experienceDesc;
    //最低年龄
    private int lowAge;
    //最高年龄
    private int highAge;
    //发布日期
    private String jobCreateTime;
    //boss个人信息
    private ChatUser boss;

    @Override
    public int getMediaType() {
        return MESSAGE_MEDIA_TYPE_JD;
    }

    @Override
    public String getMediaBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jobId", jobId);
        jsonObject.put("title",StringUtils.defaultString(title));
        jsonObject.put("code", code);
        jsonObject.put("codeDesc",StringUtils.defaultString(codeDesc));
        jsonObject.put("kind", kind);
        jsonObject.put("kindDesc",StringUtils.defaultString(kindDesc));
        jsonObject.put("salaryType", salaryType);
        jsonObject.put("lowSalary", lowSalary);
        jsonObject.put("highSalary", highSalary);
        jsonObject.put("degree", degree);
        jsonObject.put("degreeDesc",StringUtils.defaultString(degreeDesc));
        jsonObject.put("experience", experience);
        jsonObject.put("experienceDesc",StringUtils.defaultString(experienceDesc));
        jsonObject.put("lowAge", lowAge);
        jsonObject.put("highAge", highAge);
        jsonObject.put("jobCreateTime",StringUtils.defaultString(jobCreateTime));
        if (boss != null) {
            jsonObject.put("boss", boss.toJsonObject());
        }
        return jsonObject.toString();
    }

    @Override
    public boolean setMediaBody(String json) {
        JSONObject jsonObject = JSONObject.fromObject(json);
        jobId = jsonObject.optLong("jobId");
        title = jsonObject.optString("title");
        code = jsonObject.optInt("code");
        codeDesc = jsonObject.optString("codeDesc");
        kind = jsonObject.optInt("kind");
        kindDesc = jsonObject.optString("kindDesc");
        salaryType = jsonObject.optInt("salaryType");
        lowSalary = jsonObject.optInt("lowSalary");
        highSalary = jsonObject.optInt("highSalary");
        degree = jsonObject.optInt("degree");
        degreeDesc = jsonObject.optString("degreeDesc");
        experience = jsonObject.optInt("experience");
        experienceDesc = jsonObject.optString("experienceDesc");
        lowAge = jsonObject.optInt("lowAge");
        highAge = jsonObject.optInt("highAge");
        jobCreateTime = jsonObject.optString("jobCreateTime");
        JSONObject bossObject = jsonObject.optJSONObject("boss");
        if (bossObject != null) {
            boss = ChatUser.fromJson(bossObject);
        }
        return true;
    }

    @Override
    public String getPushMessage() {
        return null;
    }

    @Override
    public String getPushUrl() {
        return ProtocalConstants.URL_PATH + "?type=f2";
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getKindDesc() {
        return kindDesc;
    }

    public void setKindDesc(String kindDesc) {
        this.kindDesc = kindDesc;
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

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public String getDegreeDesc() {
        return degreeDesc;
    }

    public void setDegreeDesc(String degreeDesc) {
        this.degreeDesc = degreeDesc;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getExperienceDesc() {
        return experienceDesc;
    }

    public void setExperienceDesc(String experienceDesc) {
        this.experienceDesc = experienceDesc;
    }

    public int getLowAge() {
        return lowAge;
    }

    public void setLowAge(int lowAge) {
        this.lowAge = lowAge;
    }

    public int getHighAge() {
        return highAge;
    }

    public void setHighAge(int highAge) {
        this.highAge = highAge;
    }

    public String getJobCreateTime() {
        return jobCreateTime;
    }

    public void setJobCreateTime(String jobCreateTime) {
        this.jobCreateTime = jobCreateTime;
    }

    public ChatUser getBoss() {
        return boss;
    }

    public void setBoss(ChatUser boss) {
        this.boss = boss;
    }
}