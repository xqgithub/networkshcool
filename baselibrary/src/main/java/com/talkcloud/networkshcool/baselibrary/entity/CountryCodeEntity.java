package com.talkcloud.networkshcool.baselibrary.entity;

import java.util.List;

/**
 * Date:2021/5/13
 * Time:15:44
 * author:joker
 * 国家区域号实体类
 */
public class CountryCodeEntity {


    /**
     * result : 0
     * data : [{"country":"中国","locale":"CN","code":"86"},{"country":"中国香港","locale":"HK","code":"852"},{"country":"中国澳门","locale":"MO","code":"853"},{"country":"中国台湾","locale":"TW","code":"886"},{"country":"阿尔巴尼亚","locale":"AL","code":"355"},{"country":"阿尔及利亚","locale":"DZ","code":"213"},{"country":"阿富汗","locale":"AF","code":"93"},{"country":"阿根廷","locale":"AR","code":"54"},{"country":"阿拉伯联合酋长国","locale":"AE","code":"971"},{"country":"阿鲁巴","locale":"AW","code":"297"},{"country":"阿曼","locale":"OM","code":"968"},{"country":"阿塞拜疆","locale":"AZ","code":"994"},{"country":"阿森松岛","locale":"AC","code":"247"},{"country":"埃及","locale":"EG","code":"20"},{"country":"埃塞俄比亚","locale":"ET","code":"251"},{"country":"爱尔兰","locale":"IE","code":"353"},{"country":"爱沙尼亚","locale":"EE","code":"372"},{"country":"安道尔","locale":"AD","code":"376"},{"country":"安哥拉","locale":"AO","code":"244"},{"country":"安提瓜和巴布达","locale":"AG","code":"1"},{"country":"奥地利","locale":"AT","code":"43"},{"country":"澳大利亚","locale":"AU","code":"61"},{"country":"巴巴多斯","locale":"BB","code":"1"},{"country":"巴布亚新几内亚","locale":"PG","code":"675"},{"country":"巴哈马","locale":"BS","code":"1"},{"country":"巴基斯坦","locale":"PK","code":"92"},{"country":"巴拉圭","locale":"PY","code":"595"},{"country":"巴勒斯坦民族权力机构","locale":"PS","code":"970"},{"country":"巴林","locale":"BH","code":"973"},{"country":"巴拿马","locale":"PA","code":"507"},{"country":"巴西","locale":"BR","code":"55"},{"country":"白俄罗斯","locale":"BY","code":"375"},{"country":"百慕大群岛","locale":"BM","code":"1"},{"country":"保加利亚","locale":"BG","code":"359"},{"country":"北马里亚纳群岛","locale":"MP","code":"1"},{"country":"北马其顿","locale":"MK","code":"389"},{"country":"贝宁","locale":"BJ","code":"229"},{"country":"比利时","locale":"BE","code":"32"},{"country":"冰岛","locale":"IS","code":"354"},{"country":"波兰","locale":"PL","code":"48"},{"country":"波斯尼亚和黑塞哥维那","locale":"BA","code":"387"},{"country":"玻利维亚","locale":"BO","code":"591"},{"country":"伯利兹","locale":"BZ","code":"501"},{"country":"博茨瓦纳","locale":"BW","code":"267"},{"country":"博内尔","locale":"BQ","code":"599"},{"country":"不丹","locale":"BT","code":"975"},{"country":"布基纳法索","locale":"BF","code":"226"},{"country":"布隆迪","locale":"BI","code":"257"},{"country":"布维岛","locale":"BV","code":"47"},{"country":"赤道几内亚","locale":"GQ","code":"240"},{"country":"丹麦","locale":"DK","code":"45"},{"country":"德国","locale":"DE","code":"49"},{"country":"东帝汶","locale":"TL","code":"670"},{"country":"多哥","locale":"TG","code":"228"},{"country":"多米尼加共和国","locale":"DO","code":"1"},{"country":"多米尼克","locale":"DM","code":"1"},{"country":"俄罗斯","locale":"RU","code":"7"},{"country":"厄瓜多尔","locale":"EC","code":"593"},{"country":"厄立特里亚","locale":"ER","code":"291"},{"country":"法国","locale":"FR","code":"33"},{"country":"法罗群岛","locale":"FO","code":"298"},{"country":"法属波利尼西亚","locale":"PF","code":"689"},{"country":"法属圭亚那","locale":"GF","code":"594"},{"country":"梵蒂冈","locale":"VA","code":"379"},{"country":"菲律宾","locale":"PH","code":"63"},{"country":"斐济","locale":"FJ","code":"679"},{"country":"芬兰","locale":"FI","code":"358"},{"country":"佛得角","locale":"CV","code":"238"},{"country":"福克兰群岛","locale":"FK","code":"500"},{"country":"冈比亚","locale":"GM","code":"220"},{"country":"刚果（布）","locale":"CG","code":"242"},{"country":"刚果（金）","locale":"CD","code":"243"},{"country":"哥伦比亚","locale":"CO","code":"57"},{"country":"哥斯达黎加","locale":"CR","code":"506"},{"country":"格恩西岛","locale":"GG","code":"44"},{"country":"格林纳达","locale":"GD","code":"1"},{"country":"格陵兰","locale":"GL","code":"299"},{"country":"格鲁吉亚","locale":"GE","code":"995"},{"country":"古巴","locale":"CU","code":"53"},{"country":"瓜德罗普岛","locale":"GP","code":"590"},{"country":"关岛","locale":"GU","code":"1"},{"country":"圭亚那","locale":"GY","code":"592"},{"country":"哈萨克斯坦","locale":"KZ","code":"7"},{"country":"海地","locale":"HT","code":"509"},{"country":"韩国","locale":"KR","code":"82"},{"country":"荷兰","locale":"NL","code":"31"},{"country":"荷属安的列斯(前)","locale":"AN","code":"599"},{"country":"黑山","locale":"ME","code":"382"},{"country":"洪都拉斯","locale":"HN","code":"504"},{"country":"基里巴斯","locale":"KI","code":"686"},{"country":"吉布提","locale":"DJ","code":"253"},{"country":"吉尔吉斯斯坦","locale":"KG","code":"996"},{"country":"几内亚","locale":"GN","code":"224"},{"country":"几内亚比绍","locale":"GW","code":"245"},{"country":"加拿大","locale":"CA","code":"1"},{"country":"加纳","locale":"GH","code":"233"},{"country":"加蓬","locale":"GA","code":"241"},{"country":"柬埔寨","locale":"KH","code":"855"},{"country":"捷克","locale":"CZ","code":"420"},{"country":"津巴布韦","locale":"ZW","code":"263"},{"country":"喀麦隆","locale":"CM","code":"237"},{"country":"卡塔尔","locale":"QA","code":"974"},{"country":"开曼群岛","locale":"KY","code":"1"},{"country":"科科斯群岛(基灵群岛)","locale":"CC","code":"61"},{"country":"科摩罗联盟","locale":"KM","code":"269"},{"country":"科索沃","locale":"XK","code":"383"},{"country":"科特迪瓦","locale":"CI","code":"225"},{"country":"科威特","locale":"KW","code":"965"},{"country":"克罗地亚","locale":"HR","code":"385"},{"country":"肯尼亚","locale":"KE","code":"254"},{"country":"库可群岛","locale":"CK","code":"682"},{"country":"库拉索","locale":"CW","code":"599"},{"country":"拉脱维亚","locale":"LV","code":"371"},{"country":"莱索托","locale":"LS","code":"266"},{"country":"老挝","locale":"LA","code":"856"},{"country":"黎巴嫩","locale":"LB","code":"961"},{"country":"立陶宛","locale":"LT","code":"370"},{"country":"利比里亚","locale":"LR","code":"231"},{"country":"利比亚","locale":"LY","code":"218"},{"country":"列支敦士登","locale":"LI","code":"423"},{"country":"留尼汪","locale":"RE","code":"262"},{"country":"卢森堡","locale":"LU","code":"352"},{"country":"卢旺达","locale":"RW","code":"250"},{"country":"罗马尼亚","locale":"RO","code":"40"},{"country":"马达加斯加","locale":"MG","code":"261"},{"country":"马恩岛","locale":"IM","code":"44"},{"country":"马尔代夫","locale":"MV","code":"960"},{"country":"马耳他","locale":"MT","code":"356"},{"country":"马拉维","locale":"MW","code":"265"},{"country":"马来西亚","locale":"MY","code":"60"},{"country":"马里","locale":"ML","code":"223"},{"country":"马绍尔群岛","locale":"MH","code":"692"},{"country":"马提尼克岛","locale":"MQ","code":"596"},{"country":"马约特","locale":"YT","code":"262"},{"country":"毛里求斯","locale":"MU","code":"230"},{"country":"毛利塔尼亚","locale":"MR","code":"222"},{"country":"美国","locale":"US","code":"1"},{"country":"美属外岛","locale":"UM","code":"1"},{"country":"美属维尔京群岛","locale":"VI","code":"1"},{"country":"蒙古","locale":"MN","code":"976"},{"country":"蒙特塞拉特","locale":"MS","code":"1"},{"country":"孟加拉国","locale":"BD","code":"880"},{"country":"秘鲁","locale":"PE","code":"51"},{"country":"密克罗尼西亚","locale":"FM","code":"691"},{"country":"缅甸","locale":"MM","code":"95"},{"country":"摩尔多瓦","locale":"MD","code":"373"},{"country":"摩洛哥","locale":"MA","code":"212"},{"country":"摩纳哥","locale":"MC","code":"377"},{"country":"莫桑比克","locale":"MZ","code":"258"},{"country":"墨西哥","locale":"MX","code":"52"},{"country":"纳米比亚","locale":"NA","code":"264"},{"country":"南非","locale":"ZA","code":"27"},{"country":"南极洲","locale":"AQ","code":"672"},{"country":"南苏丹","locale":"SS","code":"211"},{"country":"瑙鲁","locale":"NR","code":"674"},{"country":"尼泊尔","locale":"NP","code":"977"},{"country":"尼加拉瓜","locale":"NI","code":"505"},{"country":"尼日尔","locale":"NE","code":"227"},{"country":"尼日利亚","locale":"NG","code":"234"},{"country":"纽埃","locale":"NU","code":"683"},{"country":"挪威","locale":"NO","code":"47"},{"country":"帕劳群岛","locale":"PW","code":"680"},{"country":"葡萄牙","locale":"PT","code":"351"},{"country":"日本","locale":"JP","code":"81"},{"country":"瑞典","locale":"SE","code":"46"},{"country":"瑞士","locale":"CH","code":"41"},{"country":"萨尔瓦多","locale":"SV","code":"503"},{"country":"萨摩亚","locale":"WS","code":"685"},{"country":"塞尔维亚共和国","locale":"RS","code":"381"},{"country":"塞拉利昂","locale":"SL","code":"232"},{"country":"塞内加尔","locale":"SN","code":"221"},{"country":"塞浦路斯","locale":"CY","code":"357"},{"country":"塞舌尔","locale":"SC","code":"248"},{"country":"沙巴岛","locale":"XS","code":"599"},{"country":"沙特阿拉伯","locale":"SA","code":"966"},{"country":"圣诞岛","locale":"CX","code":"61"},{"country":"圣多美和普林西比","locale":"ST","code":"239"},{"country":"圣赫勒拿、阿森松与特里斯坦达库尼亚","locale":"SH","code":"290"},{"country":"圣基茨和尼维斯岛","locale":"KN","code":"1"},{"country":"圣卢西亚","locale":"LC","code":"1"},{"country":"圣马力诺","locale":"SM","code":"378"},{"country":"圣皮埃尔和密克隆群岛","locale":"PM","code":"508"},{"country":"圣文森特和格林纳丁斯","locale":"VC","code":"1"},{"country":"圣尤斯特歇斯岛","locale":"XE","code":"599"},{"country":"斯里兰卡","locale":"LK","code":"94"},{"country":"斯洛伐克","locale":"SK","code":"421"},{"country":"斯洛文尼亚","locale":"SI","code":"386"},{"country":"斯瓦尔巴岛","locale":"SJ","code":"47"},{"country":"斯威士兰","locale":"SZ","code":"268"},{"country":"苏丹","locale":"SD","code":"249"},{"country":"苏里南","locale":"SR","code":"597"},{"country":"所罗门群岛","locale":"SB","code":"677"},{"country":"索马里","locale":"SO","code":"252"},{"country":"塔吉克斯坦","locale":"TJ","code":"992"},{"country":"泰国","locale":"TH","code":"66"},{"country":"坦桑尼亚","locale":"TZ","code":"255"},{"country":"汤加","locale":"TO","code":"676"},{"country":"特克斯和凯科斯群岛","locale":"TC","code":"1"},{"country":"特里斯坦达昆哈","locale":"TA","code":"290"},{"country":"特立尼达和多巴哥","locale":"TT","code":"1"},{"country":"突尼斯","locale":"TN","code":"216"},{"country":"图瓦卢","locale":"TV","code":"688"},{"country":"土耳其","locale":"TR","code":"90"},{"country":"土库曼斯坦","locale":"TM","code":"993"},{"country":"托克劳","locale":"TK","code":"690"},{"country":"瓦利斯和富图纳","locale":"WF","code":"681"},{"country":"瓦努阿图","locale":"VU","code":"678"},{"country":"危地马拉","locale":"GT","code":"502"},{"country":"委内瑞拉","locale":"VE","code":"58"},{"country":"文莱","locale":"BN","code":"673"},{"country":"乌干达","locale":"UG","code":"256"},{"country":"乌克兰","locale":"UA","code":"380"},{"country":"乌拉圭","locale":"UY","code":"598"},{"country":"乌兹别克斯坦","locale":"UZ","code":"998"},{"country":"西班牙","locale":"ES","code":"34"},{"country":"希腊","locale":"GR","code":"30"},{"country":"新加坡","locale":"SG","code":"65"},{"country":"新喀里多尼亚","locale":"NC","code":"687"},{"country":"新西兰","locale":"NZ","code":"64"},{"country":"匈牙利","locale":"HU","code":"36"},{"country":"叙利亚","locale":"SY","code":"963"},{"country":"牙买加","locale":"JM","code":"1"},{"country":"亚美尼亚","locale":"AM","code":"374"},{"country":"扬马延岛","locale":"XJ","code":"47"},{"country":"也门","locale":"YE","code":"967"},{"country":"伊拉克","locale":"IQ","code":"964"},{"country":"伊朗","locale":"IR","code":"98"},{"country":"以色列","locale":"IL","code":"972"},{"country":"意大利","locale":"IT","code":"39"},{"country":"印度","locale":"IN","code":"91"},{"country":"印度尼西亚","locale":"ID","code":"62"},{"country":"英国","locale":"UK","code":"44"},{"country":"英属维尔京群岛","locale":"VG","code":"1"},{"country":"英属印度洋领地","locale":"IO","code":"44"},{"country":"约旦","locale":"JO","code":"962"},{"country":"越南","locale":"VN","code":"84"},{"country":"赞比亚","locale":"ZM","code":"260"},{"country":"泽西","locale":"JE","code":"44"},{"country":"乍得","locale":"TD","code":"235"},{"country":"直布罗陀","locale":"GI","code":"350"},{"country":"智利","locale":"CL","code":"56"},{"country":"中非共和国","locale":"CF","code":"236"}]
     * msg : success
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * code : 86
         * tw : 中國
         * en : China
         * locale : CN
         * zh : 中国
         */

        private int code;
        private String tw;
        private String en;
        private String locale;
        private String zh;
        private boolean isTop;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getTw() {
            return tw;
        }

        public void setTw(String tw) {
            this.tw = tw;
        }

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getZh() {
            return zh;
        }

        public void setZh(String zh) {
            this.zh = zh;
        }

        public boolean isTop() {
            return isTop;
        }

        public void setTop(boolean top) {
            isTop = top;
        }
    }


}
