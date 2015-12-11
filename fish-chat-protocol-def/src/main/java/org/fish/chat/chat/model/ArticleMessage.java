/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.model;

import cn.techwolf.blue.common.constants.ProtocalConstants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Comments for ArticleMessage.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年9月11日 下午4:16:57
 */
public class ArticleMessage extends Message {

    private static final long serialVersionUID = 790741548580340829L;

    private List<ArticleItem> articleItemList;

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#getMediaType()
     */
    @Override
    public int getMediaType() {
        return MESSAGE_MEDIA_TYPE_ARTICLE;
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#getMediaBody()
     */
    @Override
    public String getMediaBody() {
        JSONArray jsonArray = new JSONArray();
        for (ArticleItem articleItem : articleItemList) {
            jsonArray.add(articleItem.toJSONObject());
        }
        return jsonArray.toString();
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#setMediaBody(java.lang.String)
     */
    @Override
    public boolean setMediaBody(String json) {
        JSONArray jsonArray = JSONArray.fromObject(json);
        List<ArticleItem> articleItemList = new ArrayList<ArticleItem>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject != null) {
                articleItemList.add(ArticleItem.parseFrom(jsonObject));
            }
        }
        this.articleItemList = articleItemList;
        return true;
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.model.Message#getPushMessage()
     */
    @Override
    public String getPushMessage() {
        if (CollectionUtils.isNotEmpty(articleItemList)) {
            ArticleItem articleItem = articleItemList.get(0);
            if (articleItem != null) {
                return articleItem.getTitle();
            }
        }
        return null;
    }

    @Override
    public String getPushUrl() {
        return ProtocalConstants.URL_PATH + "?type=f2";
    }

    /**
     * @return the articleItemList
     */
    public List<ArticleItem> getArticleItemList() {
        return articleItemList;
    }

    /**
     * @param articleItemList the articleItemList to set
     */
    public void setArticleItemList(List<ArticleItem> articleItemList) {
        this.articleItemList = articleItemList;
    }

}
