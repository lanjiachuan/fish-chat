/**
 * $Id$
 * Copyright 2013 Sparta. All rights reserved.
 */
package org.fish.chat.common.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Comments for RegexUtils.java
 *
 * @author <a href="mailto:jun.liu@opi-corp.com">刘军</a>
 * @createTime 2013-10-5 下午05:10:17
 */
public class RegexUtils {

    public static final String EMAIL_PATTERN_STRING = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
    public static final String PHONE_PATTERN_STRING = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";
    public static final String CHINESE_PATTERN_STRING = "^[\\u4E00-\\u9FA5]+$";
    public static final String ENGLISH_PATTERN_STRING = "^[A-Za-z]+$";
    public static final String QQ_PATTERN_STRING = "^[1-9][0-9]{4,}$";
    public static final String NUMBER_PATTERN_STRING = "^\\d+$";
    public static final String CONTAINS_CHINESE_PATTERN_STRING = "[\u4e00-\u9fa5]";


    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_PATTERN_STRING);
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_PATTERN_STRING);
    private static final Pattern CHINESE_PATTERN = Pattern.compile(CHINESE_PATTERN_STRING);
    private static final Pattern ENGLISH_PATTERN = Pattern.compile(ENGLISH_PATTERN_STRING);
    private static final Pattern QQ_PATTERN = Pattern.compile(QQ_PATTERN_STRING);
    private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_PATTERN_STRING);
    public static final Pattern CONTAINS_CHINESE_PATTERN = Pattern.compile(CONTAINS_CHINESE_PATTERN_STRING);

    /*加密ID正则表达式*/
    public static final String SECURITY_ID_PATTTERN = "^[A-Za-z0-9\\_\\-\\~\\=\\/]+$";

    //验证零和非零开头的整数
    public static final String POSITIVE_INTEGER_PATTTERN = "^(0|[1-9][0-9]*)$";

    public static boolean isEmail(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(value).find();
    }

    public static boolean isNotEmail(String value) {
        return !isEmail(value);
    }

    public static boolean isPhone(String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return PHONE_PATTERN.matcher(value).find();
    }

    public static boolean isNotPhone(String value) {
        return !isPhone(value);
    }

    public static boolean isChinese(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return CHINESE_PATTERN.matcher(value).find();
    }

    public static boolean isNotChinese(String value) {
        return !isChinese(value);
    }

    public static boolean isContainsChinese(String value) {
        Matcher matcher = CONTAINS_CHINESE_PATTERN.matcher(value);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    public static boolean isNotContainsChinese(String value) {
        return !isContainsChinese(value);
    }

    public static boolean isEnglish(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return ENGLISH_PATTERN.matcher(value).find();
    }

    public static boolean isNotEnglish(String value) {
        return !isEnglish(value);
    }

    public static boolean isQQ(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return QQ_PATTERN.matcher(value).find();
    }

    public static boolean isNotQQ(String value) {
        return !isQQ(value);
    }

    public static boolean isNumber(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return NUMBER_PATTERN.matcher(value).find();
    }

    public static boolean isNotNumber(String value) {
        return !isNumber(value);
    }

    public static boolean isMatch(String desc, String regex) {
        if (StringUtils.isEmpty(desc) || StringUtils.isEmpty(regex)) {
            return false;
        }
        return Pattern.compile(regex).matcher(desc).find();
    }

    public static String removeRepeatChar(String source) {
        source = StringUtils.trimToEmpty(source);
        if (StringUtils.isNotBlank(source)) {
            return source.replaceAll("(?s)(.)(?=.*\\1)", "");
        } else {
            return source;
        }
    }

    public static int removeRepeatCharCount(String source) {
        source = StringUtils.trimToEmpty(source);
        if (StringUtils.isNotBlank(source)) {
            return StringUtils.length(source.replaceAll("(?s)(.)(?=.*\\1)", ""));
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        //String r = "(?i)(.*)(物业|电工|银行客户专员|前台|行政专员|地产经纪人|电话销售|呼叫中心代表|检查工|售后服务专员|装饰美容主管|汽车改装工程师|高级维修技工|维修工|洗车工|喷漆主管|设备维修主管|设备维修工程师|装配主管|铸造主管|注塑主管|夹具工程师|焊接工程师|冲压工程师|锅炉工程师|电气工程师|气动工程师|班组长|操作工|质检员|注塑|印刷工|纺织工|仓管员|搬运工|综合维修工|电梯工|手机维修|锅炉工|钣金工|钳工|切割工|车工|铣工|铸造工|注塑工程师|模具工|印刷机长|叉车工|水工|手把焊|制冷工|样衣工|缝纫工|车板工|木工|模板工|瓦工|钢筋工|油漆工|管道工|A照司机|自卸车司机|水泥泵车司机|平地机司机|铲车司机|塔吊司机|工艺技术员|晒版工|模切工|高级排版师|凸印机长|铜版印刷机长|丝印机长|糊盒班组长|高级打样师|高级调墨师|造纸工艺经理|造纸研发经理|制浆生产主任|直印技术人员|菲林技术人员|后道检验组长|后道检验员|印花主管|施工员|重机械操作员|首席钻井工程师|首席固井工程师|首席完井工程师|首席测井工程师|首席录井工程师|客房|行李员|保洁主管|礼宾领班|服务台组长|服务生|宴会经理|机场代表（酒店机场事务代表）|预订中心经理|预定中心领班|收银部经理|客房总监|接待员|厨工|配菜|传菜员|送餐员|洗碗工|杂工|酒吧|餐饮领班|调酒师|茶艺师|咖啡师|健身房经理|健身房教练|宾客服务经理|高级美容师|资深美容顾问|发型顾问|美发|美甲师主管|资深瑜伽|资深瘦身顾问|足疗店长|救生员|店长|品类经理|营业组长|导购组长|商品陈列经理|门店招商租售经理|奢侈品销售|面点师|加工班长|熟食厨师|陈列督导主管|促销经理|促销督导助理|保安队长|防损组长|店面体系总监|店面体系经理|高端月嫂|保安经理|保安领班|高级保姆|高级宠物美容师|宠物医师|婚庆司仪|婚庆策划师|资深婚礼顾问|形象顾问|红娘顾问|婚姻家庭咨询师|代驾司机|打工|设备保管员|水暖工|银行客户代表|接待|行政助理|销售代表|高级汽车装调检查工|装饰美容专员|汽车改装技师|维修技工|高级喷漆技工|设备保养主管|设备维修工|装配工程师|铸造工程师|夹具工|焊工|冲压工|锅炉技工|气动技工|生产班长|作业员|质检专员|注塑领班|印刷学徒|纺织辅助工|仓管|仓库搬运工|家电维修|电梯安装工|手机维修员|锅炉操作工|汽车钣金工|装配钳工|切割技工|数控车床操作工|普通铣工|注塑技术员|模具制造工|印刷机械机长|叉车司机|装配电工|水暖维修工|二保焊|维修技师|制冷工程师|服装样衣工|缝纫车工|车板师|大木工|架子工|泥水工|钢筋工长|油漆技师|管工|B照司机|大车司机|泵车司机|铲车工|塔吊机械手|拼版工|啤机工|排版师|印刷|柔印机长|凹印机长|移印机长|糊盒工|打样师|调墨师|高级造纸工艺工程师|高级造纸研发工程师|制浆生产班组长|印花班组长|重机械驾驶员|高级钻井工程师|高级固井工程师|高级完井工程师|高级测井工程师|高级录井工程师|楼层服务员|门童|保洁领班|前厅领班|服务台班长|文员|宴会主管|预订中心主管（预订主管）|预定中心接线员（预订员）|收银主管|值班经理|初级服务生|打荷工|咖啡厅经理|高级侍酒师|健身房主管|健身房服务生|宾客主管|中级美容师|柜长|发型师|发型助理|美甲督导|舞蹈|瘦身顾问|足疗师|副店长|品类分管组长|营业员|高级导购员|商品陈列主管|门店招商租售主管|顾问|糕点师|加工杂工|陈列督导专员|督导|促销员|安防主管|防损员|店面体系主管|育婴师|保安主管|保姆|宠物美容师|宠物医师助理|婚礼顾问|电话红娘|抄表员|大堂|中级汽车装调检查工|喷漆技工|设备保全主管|设备保养工程师|装配工|注塑工|液压工程师|车间班组长|组装工|印花工|仓库管理员|搬运工场务|电梯安装学徒|手机维修工程师|高级锅炉技工|钣金学徒|模具钳工|激光切割操作工|CNC|铣工学徒|铸造工艺工程师|注塑工艺员|模具工程师|印刷技术员|叉车驾驶员|机修电工|空调工|氩弧焊|机械工程师|制冷维修工|样衣师|熟练缝纫工|车板|木工师傅|油漆中工|管道安装工|C照司机|货车司机|塔吊指挥|装订工|压痕工|高级制版师|粘盒班组长|打佯工|调墨工|造纸工艺工程师|造纸研发工程师|制浆工|印花技工|钻井工程师|固井工程师|完井工程师|测井工程师|录井工程师|保洁员|前厅经理|服务台|宴会领班|收银领班|值班主管|侍酒师|宾客专员|美容师|美容顾问|学徒|美甲师|美体教练|按摩师|区域店长|导购员|商品陈列专员|门店招商租售专员|督导经理|促销督导经理|兼职促销员|保安|店面体系专员|月嫂|保安员|宠物美容学徒|送电工|维修工人|初级汽车装调检查工|设备保全专员|设备保养工|锻造主管|液压技工|车间班长|生产作业员|仓管人员|搬运|电梯维保人员|钣金师傅|机修钳工|激光切割机操作员|CNC操作工|龙门铣工|模具设计|仓库叉车工|高级电工|制冷技术员|样衣组长|车板组长|木工工长|油漆主管|管道维修工|行政司机|送货司机|烫金工|制版师|粘盒工|高级刀模师|整烫主管|初级钻井工程师|初级固井工程师|初级完井工程师|初级测井工程师|初级录井工程师|清洁工|前厅主管|礼仪员|宴会服务生|收银员（收银主管,收银员）|收银员|咖啡厅主管|吧台员|宾客代表|高级化妆师|美甲学徒|瑜伽|加盟事务主管|切档|防损|促销督导主管|长期促销员|店面体系助理|发动机检查工|设备维修员|锻造工程师|普工|仓管专员|电梯维保|钣金工程师|钳工师傅|激光切割机操作工|数控车床|模具设计师|叉车|物业电工|中央空调工|气保焊工|制冷维修工程师|样衣裁剪|车板员|木工学徒|油漆学徒|公交车司机|刀模师|整烫班组长|钻井工人|固井工人|完井工人|测井工人|录井工人|化妆师|地区总经理|加盟事务专员|外卖|督导主管|大区促销经理|初级发动机检查工|设备巡检主管|锻造工|生产普工|电梯维保员|钣金中工|切割师傅|数控车床工|氩弧焊焊工|制冷操作工|样衣车板工|商务司机|刀模工|整烫技工|礼仪班长|宾客|化妆助理|陈列规划设计主管|加盟事务助理|变速器检查工|设备巡检专员|车间操作工|数控车床师傅|氩氟焊工|司机|包装主管|陈列规划设计专员|加盟|督导专员|初级变速器检查工|设备保全经理|生产辅助工|数控车床操作员|焊工学徒|包装班组长|理货员|初级防损|汽车性能检查工|包装技工|理货班长|汽车外观检查工|陈列|汽修|高级电气设备维修工程师|电气设备维修工程师|初级电气设备维修工|钣金主管|高级钣金工|汽机维修工程师|汽机维修工|高级锅炉维修工程师|锅炉维修工程师|锅炉维修工|热控检修工程师|热控检修工|内燃机检修工\"|2000左右|IQC|QE工程师|smc|SMT|师傅|安装工|班车|包吃住|包装工|包装工程师|保养|饼房|不到3000|才三千|裁剪工|餐厅|餐饮部|操作员|厂∩班|厂∩招人|厂里住|厂招聘|车床|车间|乘务员|初中|船员|辞职后还想再进去|大专∩实习|导购|电焊工|电气设计|电子厂|电子维修员工|店∩招|二次进厂|二线线工|缝纫机|服务员|服装押金|富士康|干满一月|工衣|工资∩低|工资结算|管住∩伙食|话务员|机车|机电人员|机械|几班倒|驾驶员|兼职|收银|检验员|剪工|建筑工人|介绍费|进富士康|客服|扣∩工资|苦点也行|快运∩分配|垃圾厂子|劳力工|两千多∩厂子|流水线|没有休息|门卫|磨床技工|磨床师傅|男的招吗|派遣∩转正|派收员|培训费|培训期间|培训生|品保|品管|品检|普通工人|普通员工|汽配|前厅|上船|上夜班|少爷|设备维修|生产跟单|生产工|售后|熟练工|水电工|送快递|台面组|提供住宿|体检费|挖掘机|网上∩兼职|维修人员|维修售后|无尘衣|物流管理|物流普工|物业维修|西点|铣床工|线切割|小时工|修车|押几天|烟疤|要人|业务员|一个月拿多少钱|一线员工|有高点的吗|员工|站着上班|长白班|招∩暑期工|招工|招开|招女工|制冷设备|质检|置业顾问|中专|装配|装修学徒|自离|总装车间|组装|KTV|安装|酒店|邦威|包装|包装材料|宾馆|超市|车厂|传动|船舶|船务|德邦|地产经纪|电器|电梯|电子|房产经纪|富士|管桩|光电|锅炉|海底捞|家乐福|建筑|精密|快递|蓝沛|门窗|模具|能源|牛奶|配件|汽车|三星|食品|手袋厂|塑胶厂|外婆家|微密|五金|影城|友达|制冷|制品厂|自控|找工|高中|倒壁|工资发不出来|欺负|被迫自离|伙食|((黑|烂)(厂子|公司)))(.*)";
        //String r = "(?i)(.*)(苦点也行|快运(.*)分配|分配(.*)快运)(.*)";
        //String r = "(?i)(.*)(早班|工作时间|管食宿|面试地点|猎头|去应聘|答疑解惑|从什么做起|分公司|座机|违约金|为什么没有|电话号码|地址|看不起|工作地点|培训期|晚班|国企|国营|私营|包住|吃住|你觉得|人事(邮箱|电话)|HR(.*)电话|是否(.*)(可靠|可信|真的)|(太|这么|很|真是)(难|麻烦))(.*)";
        //Pattern p = Pattern.compile("(.*)(五险一金|加班|补助|全勤奖|包吃包住|餐补)(.*)");
        //Pattern p = Pattern.compile("?<name>(五险一金|加班|补助|全勤奖|包吃包住|餐补)");
        //Matcher matcher = p.matcher("afds五险一金发送到加班发送到补助发送全勤奖发送包吃包住");

        //while(matcher.find()){
        //    System.out.println(matcher.group(0)); // 整个匹配到的内容
        //    System.out.println(matcher.group(1)); // (<textarea.*?>)
        //    System.out.println(matcher.group(2)); // (.*?)
        //    System.out.println(matcher.group(3)); // (</textarea>)
        //}
        //String r = ".*(?=(.*(五险一金|加班|补助|全勤奖|包吃包住|餐补|房补|压力大|任务重|收入|付出|晋升|工作多长|工作餐|双休|节假|24小时|加班|总部|薪资|工作内容|住宿|销售|几天制|十三薪).*){4})+.*";
        //String r = "(?i).*(.*(五险一金|加班|补助|全勤奖|包吃包住|餐补).*){4}.*";
        //System.out.println("五险一金fds加班ffsd补助发送全勤奖包吃1包住".matches(r));
        //String r = "^(?i).*(毕业证(.*)(找不见|丢|掉)|(找不见|丢|掉)(.*)毕业证).*$";
        //String r = "(?i).*(发展前途.*(如何|怎么样|怎样)|(如何|怎么样|怎样).*发展前途).*";
        /*String r = "(java|开发工程师).*(做什么|要什么|负责什么|负责什么|是什么岗位|要求|做什么|工作职责)";
        Pattern p = Pattern.compile(r, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher("我先知道Java开发工程师是做什么的");
        while (matcher.find()){
            if(matcher.groupCount()>0){
                System.out.println(matcher.group(1));
                break;
            }
        }*/
        //String r = "(?i).*想.*(Java|开发工程师)工作.*";
        //String r = "(?i).*(要不要人|(要|请|缺|找|招)人(不|么|吗|嘛|麼|呢)).*";
        String r = "(?i).*(早班|夜班|工作时间|管食宿|面试地点|猎头|去应聘|答疑解惑|从什么做起|分公司|座机|违约金|为什么没有|电话号码|地址|看不起|工作地点|培训期|晚班|国企|国营|私营|包住|吃住|你觉得|适合(什么|哪个|哪些)|人事(邮箱|电话)|HR.*电话|是否.*(可靠|可信|真的)|(太|这么|很|真是)(难|麻烦)).*";
        System.out.println("现在里面许要人吗？想去应聘的".matches(r));

        Pattern p = Pattern.compile(r, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher("现在里面许要人吗？想去应聘的");
        while (matcher.find()) {
            if (matcher.groupCount() > 0) {
                System.out.println(matcher.group(1));
                break;
            }
        }

        //String r = ".*[\\u4e00-\\u9fa5].*";
        //System.out.println("fsd#4國234@ ".matches(r));
    }
}
