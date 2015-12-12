/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.filter.impl;


import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.TextMessage;
import org.fish.chat.chat.model.UserSession;
import org.fish.chat.common.constants.ChatConstant;

/**
 * Comments for AntispamFilter.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年5月13日 上午10:33:26
 */
public class AntispamFilter extends ChatFilterAdapter {

    @Override
    public boolean beforeDeliver(UserSession fromUserSession, Message message) {

        if (message instanceof TextMessage && message.getFrom().getUid() != ChatConstant.SYSTEM_USER_ID && message.getType() == Message.MESSAGE_TYPE_SINGLE) {
//            TextMessage textMessage = (TextMessage) message;
//            List<SensitiveWord> sensitiveWordList = sensitiveWordService.listSensitiveWords(textMessage.getText());
//
//            if(CollectionUtils.isNotEmpty(sensitiveWordList)) {
//                StringBuffer stringBuffer = new StringBuffer();
//                Iterator<SensitiveWord> iterator = sensitiveWordList.iterator();
//                while (iterator.hasNext()) {
//                    SensitiveWord sensitiveWord = iterator.next();
//                    stringBuffer.append(sensitiveWord.toString());
//                    while (iterator.hasNext()) {
//                        stringBuffer.append(",");
//                    }
//                }
//            }

        }

        return true;
    }

}
