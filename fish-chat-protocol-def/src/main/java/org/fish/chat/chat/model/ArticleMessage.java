package org.fish.chat.chat.model;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.fish.chat.common.constants.ProtocolConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Comments for ArticleMessage.java
 *
 */
public class ArticleMessage extends Message {

    private static final long serialVersionUID = 790741548580340829L;

    private List<ArticleItem> articleItemList;

    @Override
    public int getMediaType() {
        return MESSAGE_MEDIA_TYPE_ARTICLE;
    }

    @Override
    public String getMediaBody() {
        JSONArray jsonArray = new JSONArray();
        for (ArticleItem articleItem : articleItemList) {
            jsonArray.add(articleItem.toJSONObject());
        }
        return jsonArray.toString();
    }

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
        return ProtocolConstants.URL_PATH + "?type=f2";
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
