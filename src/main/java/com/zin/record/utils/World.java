package com.zin.record.utils;

import com.zin.record.utils.gson.GsonUtils;
import org.apache.http.util.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujinming on 2018/7/30.
 */
public enum World {

    INSTANCE;

    private static Map<String, String> worldMapDataToRegion = new HashMap<>();
    private static Map<String, String> worldMapNameByCode = new HashMap<>();
    private static Map<String, String> worldMapNameByEnName = new HashMap<>();


    private String json;

    {
        json = "{\"records\":[{\"code\":\"AF\",\"en_name\":\"Afghanistan\",\"cn_name\":\"阿富汗\",\"region\":\"亚洲\"},{\"code\":\"AL\",\"en_name\":\"Albania\",\"cn_name\":\"阿尔巴尼亚\",\"region\":\"欧洲\"},{\"code\":\"DZ\",\"en_name\":\"Algeria\",\"cn_name\":\"阿尔及利亚\",\"region\":\"非洲\"},{\"code\":\"AS\",\"en_name\":\"American \\r\\nSamoa\",\"cn_name\":\"美属萨摩亚\",\"region\":\"大洋洲\"},{\"code\":\"AD\",\"en_name\":\"Andorra\",\"cn_name\":\"安道尔\",\"region\":\"欧洲\"},{\"code\":\"AO\",\"en_name\":\"Angola\",\"cn_name\":\"安哥拉\",\"region\":\"非洲\"},{\"code\":\"AI\",\"en_name\":\"Anguilla\",\"cn_name\":\"安圭拉\",\"region\":\"其他\"},{\"code\":\"AQ\",\"en_name\":\"Antarctica\",\"cn_name\":\"南极洲\",\"region\":\"其他\"},{\"code\":\"AG\",\"en_name\":\"Antigua and \\r\\nBarbuda\",\"cn_name\":\"安提瓜和巴布达\",\"region\":\"北美洲\"},{\"code\":\"AR\",\"en_name\":\"Argentina\",\"cn_name\":\"阿根廷\",\"region\":\"南美洲\"},{\"code\":\"AM\",\"en_name\":\"Armenia\",\"cn_name\":\"亚美尼亚\",\"region\":\"欧洲\"},{\"code\":\"AW\",\"en_name\":\"Aruba\",\"cn_name\":\"阿鲁巴\",\"region\":\"其他\"},{\"code\":\"AU\",\"en_name\":\"Australia\",\"cn_name\":\"澳大利亚\",\"region\":\"大洋洲\"},{\"code\":\"AT\",\"en_name\":\"Austria\",\"cn_name\":\"奥地利\",\"region\":\"欧洲\"},{\"code\":\"AZ\",\"en_name\":\"Azerbaijan\",\"cn_name\":\"阿塞拜疆\",\"region\":\"欧洲\"},{\"code\":\"BS\",\"en_name\":\"Bahamas\",\"cn_name\":\"巴哈马\",\"region\":\"北美洲\"},{\"code\":\"BH\",\"en_name\":\"Bahrain\",\"cn_name\":\"巴林\",\"region\":\"亚洲\"},{\"code\":\"BD\",\"en_name\":\"Bangladesh\",\"cn_name\":\"孟加拉国\",\"region\":\"亚洲\"},{\"code\":\"BB\",\"en_name\":\"Barbados\",\"cn_name\":\"巴巴多斯\",\"region\":\"北美洲\"},{\"code\":\"BY\",\"en_name\":\"Belarus\",\"cn_name\":\"白俄罗斯\",\"region\":\"欧洲\"},{\"code\":\"BE\",\"en_name\":\"Belgium\",\"cn_name\":\"比利时\",\"region\":\"欧洲\"},{\"code\":\"BZ\",\"en_name\":\"Belize\",\"cn_name\":\"伯利兹\",\"region\":\"北美洲\"},{\"code\":\"BJ\",\"en_name\":\"Benin\",\"cn_name\":\"贝宁\",\"region\":\"非洲\"},{\"code\":\"BM\",\"en_name\":\"Bermuda\",\"cn_name\":\"百慕大\",\"region\":\"其他\"},{\"code\":\"BT\",\"en_name\":\"Bhutan\",\"cn_name\":\"不丹\",\"region\":\"亚洲\"},{\"code\":\"BO\",\"en_name\":\"Bolivia\",\"cn_name\":\"玻利维亚\",\"region\":\"南美洲\"},{\"code\":\"BA\",\"en_name\":\"Bosnia and \\r\\nHerzegovina\",\"cn_name\":\"波斯尼亚和黑塞哥维那\",\"region\":\"欧洲\"},{\"code\":\"BW\",\"en_name\":\"Botswana\",\"cn_name\":\"博茨瓦纳\",\"region\":\"非洲\"},{\"code\":\"BV\",\"en_name\":\"Bouvet Island\",\"cn_name\":\"布维特岛\",\"region\":\"其他\"},{\"code\":\"BR\",\"en_name\":\"Brazil\",\"cn_name\":\"巴西\",\"region\":\"南美洲\"},{\"code\":\"IO\",\"en_name\":\"British \\r\\nIndian Ocean Territory\",\"cn_name\":\"英属印度洋领地\",\"region\":\"其他\"},{\"code\":\"VG\",\"en_name\":\"British \\r\\nVirgin Islands\",\"cn_name\":\"英属维京群岛\",\"region\":\"北美洲\"},{\"code\":\"BN\",\"en_name\":\"Brunei\",\"cn_name\":\"文莱\",\"region\":\"亚洲\"},{\"code\":\"BG\",\"en_name\":\"Bulgaria\",\"cn_name\":\"保加利亚\",\"region\":\"欧洲\"},{\"code\":\"BF\",\"en_name\":\"Burkina Faso\",\"cn_name\":\"布基纳法索\",\"region\":\"非洲\"},{\"code\":\"BI\",\"en_name\":\"Burundi\",\"cn_name\":\"布隆迪\",\"region\":\"非洲\"},{\"code\":\"KH\",\"en_name\":\"Cambodia\",\"cn_name\":\"柬埔寨\",\"region\":\"亚洲\"},{\"code\":\"CM\",\"en_name\":\"Cameroon\",\"cn_name\":\"喀麦隆\",\"region\":\"非洲\"},{\"code\":\"CA\",\"en_name\":\"Canada\",\"cn_name\":\"加拿大\",\"region\":\"北美洲\"},{\"code\":\"CV\",\"en_name\":\"Cape Verde\",\"cn_name\":\"佛得角\",\"region\":\"非洲\"},{\"code\":\"KY\",\"en_name\":\"Cayman Islands\",\"cn_name\":\"开曼群岛\",\"region\":\"北美洲\"},{\"code\":\"CF\",\"en_name\":\"Central African Republic\",\"cn_name\":\"中非共和国\",\"region\":\"非洲\"},{\"code\":\"TD\",\"en_name\":\"Chad\",\"cn_name\":\"乍得\",\"region\":\"非洲\"},{\"code\":\"CL\",\"en_name\":\"Chile\",\"cn_name\":\"智利\",\"region\":\"南美洲\"},{\"code\":\"CN\",\"en_name\":\"China\",\"cn_name\":\"中国\",\"region\":\"亚洲\"},{\"code\":\"CX\",\"en_name\":\"Christmas Island\",\"cn_name\":\"圣诞岛\",\"region\":\"其他\"},{\"code\":\"CC\",\"en_name\":\"Cocos \\r\\n[Keeling] Islands\",\"cn_name\":\"科科斯群岛\",\"region\":\"其他\"},{\"code\":\"CO\",\"en_name\":\"Colombia\",\"cn_name\":\"哥伦比亚\",\"region\":\"南美洲\"},{\"code\":\"KM\",\"en_name\":\"Comoros\",\"cn_name\":\"科摩罗\",\"region\":\"非洲\"},{\"code\":\"CG\",\"en_name\":\"Congo - \\r\\nBrazzaville\",\"cn_name\":\"刚果（布）\",\"region\":\"非洲\"},{\"code\":\"CD\",\"en_name\":\"Congo - \\r\\nKinshasa\",\"cn_name\":\"刚果（金）\",\"region\":\"非洲\"},{\"code\":\"CK\",\"en_name\":\"Cook Islands\",\"cn_name\":\"库克群岛\",\"region\":\"大洋洲\"},{\"code\":\"CR\",\"en_name\":\"Costa Rica\",\"cn_name\":\"哥斯达黎加\",\"region\":\"北美洲\"},{\"code\":\"HR\",\"en_name\":\"Croatia\",\"cn_name\":\"克罗地亚\",\"region\":\"欧洲\"},{\"code\":\"CU\",\"en_name\":\"Cuba\",\"cn_name\":\"古巴\",\"region\":\"北美洲\"},{\"code\":\"CY\",\"en_name\":\"Cyprus\",\"cn_name\":\"塞浦路斯\",\"region\":\"欧洲\"},{\"code\":\"CZ\",\"en_name\":\"Czech Republic\",\"cn_name\":\"捷克共和国\",\"region\":\"欧洲\"},{\"code\":\"CI\",\"en_name\":\"Côte d’Ivoire\",\"cn_name\":\"象牙海岸\",\"region\":\"非洲\"},{\"code\":\"DK\",\"en_name\":\"Denmark\",\"cn_name\":\"丹麦\",\"region\":\"欧洲\"},{\"code\":\"DJ\",\"en_name\":\"Djibouti\",\"cn_name\":\"吉布提\",\"region\":\"非洲\"},{\"code\":\"DM\",\"en_name\":\"Dominica\",\"cn_name\":\"多米尼加\",\"region\":\"北美洲\"},{\"code\":\"DO\",\"en_name\":\"Dominican \\r\\nRepublic\",\"cn_name\":\"多米尼加共和国\",\"region\":\"北美洲\"},{\"code\":\"EC\",\"en_name\":\"Ecuador\",\"cn_name\":\"厄瓜多尔\",\"region\":\"南美洲\"},{\"code\":\"EG\",\"en_name\":\"Egypt\",\"cn_name\":\"埃及\",\"region\":\"非洲\"},{\"code\":\"SV\",\"en_name\":\"El Salvador\",\"cn_name\":\"萨尔瓦多\",\"region\":\"北美洲\"},{\"code\":\"GQ\",\"en_name\":\"Equatorial \\r\\nGuinea\",\"cn_name\":\"赤道几内亚\",\"region\":\"非洲\"},{\"code\":\"ER\",\"en_name\":\"Eritrea\",\"cn_name\":\"厄立特里亚\",\"region\":\"非洲\"},{\"code\":\"EE\",\"en_name\":\"Estonia\",\"cn_name\":\"爱沙尼亚\",\"region\":\"欧洲\"},{\"code\":\"ET\",\"en_name\":\"Ethiopia\",\"cn_name\":\"埃塞俄比亚\",\"region\":\"非洲\"},{\"code\":\"FK\",\"en_name\":\"Falkland \\r\\nIslands\",\"cn_name\":\"福克兰群岛\",\"region\":\"其他\"},{\"code\":\"FO\",\"en_name\":\"Faroe Islands\",\"cn_name\":\"法罗群岛\",\"region\":\"欧洲\"},{\"code\":\"FJ\",\"en_name\":\"Fiji\",\"cn_name\":\"斐济\",\"region\":\"大洋洲\"},{\"code\":\"FI\",\"en_name\":\"Finland\",\"cn_name\":\"芬兰\",\"region\":\"欧洲\"},{\"code\":\"FR\",\"en_name\":\"France\",\"cn_name\":\"法国\",\"region\":\"欧洲\"},{\"code\":\"GF\",\"en_name\":\"French Guiana\",\"cn_name\":\"法属圭亚那\",\"region\":\"南美洲\"},{\"code\":\"PF\",\"en_name\":\"French Polynesia\",\"cn_name\":\"法属波利尼西亚\",\"region\":\"其他\"},{\"code\":\"TF\",\"en_name\":\"French Southern Territories\",\"cn_name\":\"法属南部领土\",\"region\":\"其他\"},{\"code\":\"GA\",\"en_name\":\"Gabon\",\"cn_name\":\"加蓬\",\"region\":\"非洲\"},{\"code\":\"GM\",\"en_name\":\"Gambia\",\"cn_name\":\"冈比亚\",\"region\":\"非洲\"},{\"code\":\"GE\",\"en_name\":\"Georgia\",\"cn_name\":\"格鲁吉亚\",\"region\":\"欧洲\"},{\"code\":\"DE\",\"en_name\":\"Germany\",\"cn_name\":\"德国\",\"region\":\"欧洲\"},{\"code\":\"GH\",\"en_name\":\"Ghana\",\"cn_name\":\"加纳\",\"region\":\"非洲\"},{\"code\":\"GI\",\"en_name\":\"Gibraltar\",\"cn_name\":\"直布罗陀\",\"region\":\"其他\"},{\"code\":\"GR\",\"en_name\":\"Greece\",\"cn_name\":\"希腊\",\"region\":\"欧洲\"},{\"code\":\"GL\",\"en_name\":\"Greenland\",\"cn_name\":\"格陵兰\",\"region\":\"北美洲\"},{\"code\":\"GD\",\"en_name\":\"Grenada\",\"cn_name\":\"格林纳达\",\"region\":\"北美洲\"},{\"code\":\"GP\",\"en_name\":\"Guadeloupe\",\"cn_name\":\"瓜德罗普岛\",\"region\":\"其他\"},{\"code\":\"GU\",\"en_name\":\"Guam\",\"cn_name\":\"关岛\",\"region\":\"其他\"},{\"code\":\"GT\",\"en_name\":\"Guatemala\",\"cn_name\":\"危地马拉\",\"region\":\"北美洲\"},{\"code\":\"GG\",\"en_name\":\"Guernsey\",\"cn_name\":\"格恩西岛\",\"region\":\"其他\"},{\"code\":\"GN\",\"en_name\":\"Guinea\",\"cn_name\":\"几内亚\",\"region\":\"非洲\"},{\"code\":\"GW\",\"en_name\":\"Guinea-Bissau\",\"cn_name\":\"几内亚比绍\",\"region\":\"非洲\"},{\"code\":\"GY\",\"en_name\":\"Guyana\",\"cn_name\":\"圭亚那\",\"region\":\"南美洲\"},{\"code\":\"HT\",\"en_name\":\"Haiti\",\"cn_name\":\"海地\",\"region\":\"北美洲\"},{\"code\":\"HM\",\"en_name\":\"Heard Island and McDonald \\r\\nIslands\",\"cn_name\":\"赫德与麦克唐纳群岛\",\"region\":\"其他\"},{\"code\":\"HN\",\"en_name\":\"Honduras\",\"cn_name\":\"洪都拉斯\",\"region\":\"北美洲\"},{\"code\":\"HK\",\"en_name\":\"Hong Kong SAR China\",\"cn_name\":\"中国香港特别行政区\",\"region\":\"亚洲\"},{\"code\":\"HU\",\"en_name\":\"Hungary\",\"cn_name\":\"匈牙利\",\"region\":\"欧洲\"},{\"code\":\"IS\",\"en_name\":\"Iceland\",\"cn_name\":\"冰岛\",\"region\":\"欧洲\"},{\"code\":\"IN\",\"en_name\":\"India\",\"cn_name\":\"印度\",\"region\":\"亚洲\"},{\"code\":\"ID\",\"en_name\":\"Indonesia\",\"cn_name\":\"印度尼西亚\",\"region\":\"亚洲\"},{\"code\":\"IR\",\"en_name\":\"Iran\",\"cn_name\":\"伊朗\",\"region\":\"亚洲\"},{\"code\":\"IQ\",\"en_name\":\"Iraq\",\"cn_name\":\"伊拉克\",\"region\":\"亚洲\"},{\"code\":\"IE\",\"en_name\":\"Ireland\",\"cn_name\":\"爱尔兰\",\"region\":\"欧洲\"},{\"code\":\"IM\",\"en_name\":\"Isle of Man\",\"cn_name\":\"曼岛\",\"region\":\"其他\"},{\"code\":\"IL\",\"en_name\":\"Israel\",\"cn_name\":\"以色列\",\"region\":\"亚洲\"},{\"code\":\"IT\",\"en_name\":\"Italy\",\"cn_name\":\"意大利\",\"region\":\"欧洲\"},{\"code\":\"JM\",\"en_name\":\"Jamaica\",\"cn_name\":\"牙买加\",\"region\":\"北美洲\"},{\"code\":\"JP\",\"en_name\":\"Japan\",\"cn_name\":\"日本\",\"region\":\"亚洲\"},{\"code\":\"JE\",\"en_name\":\"Jersey\",\"cn_name\":\"泽西岛\",\"region\":\"其他\"},{\"code\":\"JO\",\"en_name\":\"Jordan\",\"cn_name\":\"约旦\",\"region\":\"亚洲\"},{\"code\":\"KZ\",\"en_name\":\"Kazakhstan\",\"cn_name\":\"哈萨克斯坦\",\"region\":\"亚洲\"},{\"code\":\"KE\",\"en_name\":\"Kenya\",\"cn_name\":\"肯尼亚\",\"region\":\"非洲\"},{\"code\":\"KI\",\"en_name\":\"Kiribati\",\"cn_name\":\"基里巴斯\",\"region\":\"大洋洲\"},{\"code\":\"KW\",\"en_name\":\"Kuwait\",\"cn_name\":\"科威特\",\"region\":\"亚洲\"},{\"code\":\"KG\",\"en_name\":\"Kyrgyzstan\",\"cn_name\":\"吉尔吉斯斯坦\",\"region\":\"亚洲\"},{\"code\":\"LA\",\"en_name\":\"Laos\",\"cn_name\":\"老挝人民民主共和国\",\"region\":\"亚洲\"},{\"code\":\"LV\",\"en_name\":\"Latvia\",\"cn_name\":\"拉脱维亚\",\"region\":\"欧洲\"},{\"code\":\"LB\",\"en_name\":\"Lebanon\",\"cn_name\":\"黎巴嫩\",\"region\":\"亚洲\"},{\"code\":\"LS\",\"en_name\":\"Lesotho\",\"cn_name\":\"莱索托\",\"region\":\"非洲\"},{\"code\":\"LR\",\"en_name\":\"Liberia\",\"cn_name\":\"利比里亚\",\"region\":\"非洲\"},{\"code\":\"LY\",\"en_name\":\"Libya\",\"cn_name\":\"利比亚\",\"region\":\"非洲\"},{\"code\":\"LI\",\"en_name\":\"Liechtenstein\",\"cn_name\":\"列支敦士登\",\"region\":\"欧洲\"},{\"code\":\"LT\",\"en_name\":\"Lithuania\",\"cn_name\":\"立陶宛\",\"region\":\"欧洲\"},{\"code\":\"LU\",\"en_name\":\"Luxembourg\",\"cn_name\":\"卢森堡\",\"region\":\"欧洲\"},{\"code\":\"MO\",\"en_name\":\"Macau SAR \\r\\nChina\",\"cn_name\":\"中国澳门特别行政区\",\"region\":\"亚洲\"},{\"code\":\"MK\",\"en_name\":\"Macedonia\",\"cn_name\":\"马其顿\",\"region\":\"欧洲\"},{\"code\":\"MG\",\"en_name\":\"Madagascar\",\"cn_name\":\"马达加斯加\",\"region\":\"非洲\"},{\"code\":\"MW\",\"en_name\":\"Malawi\",\"cn_name\":\"马拉维\",\"region\":\"非洲\"},{\"code\":\"MY\",\"en_name\":\"Malaysia\",\"cn_name\":\"马来西亚\",\"region\":\"亚洲\"},{\"code\":\"MV\",\"en_name\":\"Maldives\",\"cn_name\":\"马尔代夫\",\"region\":\"亚洲\"},{\"code\":\"ML\",\"en_name\":\"Mali\",\"cn_name\":\"马里\",\"region\":\"非洲\"},{\"code\":\"MT\",\"en_name\":\"Malta\",\"cn_name\":\"马耳他\",\"region\":\"欧洲\"},{\"code\":\"MH\",\"en_name\":\"Marshall Islands\",\"cn_name\":\"马绍尔群岛\",\"region\":\"大洋洲\"},{\"code\":\"MQ\",\"en_name\":\"Martinique\",\"cn_name\":\"马提尼克群岛\",\"region\":\"其他\"},{\"code\":\"MR\",\"en_name\":\"Mauritania\",\"cn_name\":\"毛里塔尼亚\",\"region\":\"非洲\"},{\"code\":\"MU\",\"en_name\":\"Mauritius\",\"cn_name\":\"毛里求斯\",\"region\":\"非洲\"},{\"code\":\"YT\",\"en_name\":\"Mayotte\",\"cn_name\":\"马约特\",\"region\":\"非洲\"},{\"code\":\"MX\",\"en_name\":\"Mexico\",\"cn_name\":\"墨西哥\",\"region\":\"北美洲\"},{\"code\":\"FM\",\"en_name\":\"Micronesia\",\"cn_name\":\"密克罗尼西亚联邦\",\"region\":\"大洋洲\"},{\"code\":\"MD\",\"en_name\":\"Moldova\",\"cn_name\":\"摩尔多瓦\",\"region\":\"欧洲\"},{\"code\":\"MC\",\"en_name\":\"Monaco\",\"cn_name\":\"摩纳哥\",\"region\":\"欧洲\"},{\"code\":\"MN\",\"en_name\":\"Mongolia\",\"cn_name\":\"蒙古\",\"region\":\"亚洲\"},{\"code\":\"ME\",\"en_name\":\"Montenegro\",\"cn_name\":\"黑山共和国\",\"region\":\"欧洲\"},{\"code\":\"MS\",\"en_name\":\"Montserrat\",\"cn_name\":\"蒙塞拉特群岛\",\"region\":\"其他\"},{\"code\":\"MA\",\"en_name\":\"Morocco\",\"cn_name\":\"摩洛哥\",\"region\":\"非洲\"},{\"code\":\"MZ\",\"en_name\":\"Mozambique\",\"cn_name\":\"莫桑比克\",\"region\":\"非洲\"},{\"code\":\"MM\",\"en_name\":\"Myanmar \\r\\n[Burma]\",\"cn_name\":\"缅甸\",\"region\":\"亚洲\"},{\"code\":\"NA\",\"en_name\":\"Namibia\",\"cn_name\":\"纳米比亚\",\"region\":\"非洲\"},{\"code\":\"NR\",\"en_name\":\"Nauru\",\"cn_name\":\"瑙鲁\",\"region\":\"大洋洲\"},{\"code\":\"NP\",\"en_name\":\"Nepal\",\"cn_name\":\"尼泊尔\",\"region\":\"亚洲\"},{\"code\":\"NL\",\"en_name\":\"Netherlands\",\"cn_name\":\"荷兰\",\"region\":\"欧洲\"},{\"code\":\"AN\",\"en_name\":\"Netherlands \\r\\nAntilles\",\"cn_name\":\"荷属安的列斯群岛\",\"region\":\"南美洲\"},{\"code\":\"NC\",\"en_name\":\"New Caledonia\",\"cn_name\":\"新喀里多尼亚\",\"region\":\"大洋洲\"},{\"code\":\"NZ\",\"en_name\":\"New Zealand\",\"cn_name\":\"新西兰\",\"region\":\"大洋洲\"},{\"code\":\"NI\",\"en_name\":\"Nicaragua\",\"cn_name\":\"尼加拉瓜\",\"region\":\"北美洲\"},{\"code\":\"NE\",\"en_name\":\"Niger\",\"cn_name\":\"尼日尔\",\"region\":\"非洲\"},{\"code\":\"NG\",\"en_name\":\"Nigeria\",\"cn_name\":\"尼日利亚\",\"region\":\"非洲\"},{\"code\":\"NU\",\"en_name\":\"Niue\",\"cn_name\":\"纽埃\",\"region\":\"其他\"},{\"code\":\"NF\",\"en_name\":\"Norfolk Island\",\"cn_name\":\"诺福克岛\",\"region\":\"其他\"},{\"code\":\"KP\",\"en_name\":\"North \\r\\nKorea\",\"cn_name\":\"朝鲜\",\"region\":\"亚洲\"},{\"code\":\"MP\",\"en_name\":\"Northern Mariana \\r\\nIslands\",\"cn_name\":\"北马里亚纳群岛\",\"region\":\"其他\"},{\"code\":\"NO\",\"en_name\":\"Norway\",\"cn_name\":\"挪威\",\"region\":\"欧洲\"},{\"code\":\"OM\",\"en_name\":\"Oman\",\"cn_name\":\"阿曼\",\"region\":\"亚洲\"},{\"code\":\"PK\",\"en_name\":\"Pakistan\",\"cn_name\":\"巴基斯坦\",\"region\":\"亚洲\"},{\"code\":\"PW\",\"en_name\":\"Palau\",\"cn_name\":\"帕劳\",\"region\":\"大洋洲\"},{\"code\":\"PS\",\"en_name\":\"Palestinian Territories\",\"cn_name\":\"巴勒斯坦领土\",\"region\":\"亚洲\"},{\"code\":\"PA\",\"en_name\":\"Panama\",\"cn_name\":\"巴拿马\",\"region\":\"北美洲\"},{\"code\":\"PG\",\"en_name\":\"Papua New Guinea\",\"cn_name\":\"巴布亚新几内亚\",\"region\":\"大洋洲\"},{\"code\":\"PY\",\"en_name\":\"Paraguay\",\"cn_name\":\"巴拉圭\",\"region\":\"南美洲\"},{\"code\":\"PE\",\"en_name\":\"Peru\",\"cn_name\":\"秘鲁\",\"region\":\"南美洲\"},{\"code\":\"PH\",\"en_name\":\"Philippines\",\"cn_name\":\"菲律宾\",\"region\":\"亚洲\"},{\"code\":\"PN\",\"en_name\":\"Pitcairn Islands\",\"cn_name\":\"皮特凯恩\",\"region\":\"大洋洲\"},{\"code\":\"PL\",\"en_name\":\"Poland\",\"cn_name\":\"波兰\",\"region\":\"欧洲\"},{\"code\":\"PT\",\"en_name\":\"Portugal\",\"cn_name\":\"葡萄牙\",\"region\":\"欧洲\"},{\"code\":\"PR\",\"en_name\":\"Puerto Rico\",\"cn_name\":\"波多黎各\",\"region\":\"北美洲\"},{\"code\":\"QA\",\"en_name\":\"Qatar\",\"cn_name\":\"卡塔尔\",\"region\":\"亚洲\"},{\"code\":\"RO\",\"en_name\":\"Romania\",\"cn_name\":\"罗马尼亚\",\"region\":\"欧洲\"},{\"code\":\"RU\",\"en_name\":\"Russia\",\"cn_name\":\"俄罗斯\",\"region\":\"亚洲\"},{\"code\":\"RW\",\"en_name\":\"Rwanda\",\"cn_name\":\"卢旺达\",\"region\":\"非洲\"},{\"code\":\"RE\",\"en_name\":\"Réunion\",\"cn_name\":\"留尼汪\",\"region\":\"非洲\"},{\"code\":\"BL\",\"en_name\":\"Saint \\r\\nBarthélemy\",\"cn_name\":\"圣巴泰勒米\",\"region\":\"其他\"},{\"code\":\"SH\",\"en_name\":\"Saint \\r\\nHelena\",\"cn_name\":\"圣赫勒拿\",\"region\":\"非洲\"},{\"code\":\"KN\",\"en_name\":\"Saint Kitts and \\r\\nNevis\",\"cn_name\":\"圣基茨和尼维斯\",\"region\":\"其他\"},{\"code\":\"LC\",\"en_name\":\"Saint Lucia\",\"cn_name\":\"圣卢西亚\",\"region\":\"北美洲\"},{\"code\":\"MF\",\"en_name\":\"Saint Martin\",\"cn_name\":\"圣马丁\",\"region\":\"其他\"},{\"code\":\"PM\",\"en_name\":\"Saint Pierre and Miquelon\",\"cn_name\":\"圣皮埃尔和密克隆\",\"region\":\"其他\"},{\"code\":\"VC\",\"en_name\":\"Saint Vincent and the Grenadines\",\"cn_name\":\"圣文森特和格林纳丁斯\",\"region\":\"北美洲\"},{\"code\":\"WS\",\"en_name\":\"Samoa\",\"cn_name\":\"萨摩亚\",\"region\":\"大洋洲\"},{\"code\":\"SM\",\"en_name\":\"San \\r\\nMarino\",\"cn_name\":\"圣马力诺\",\"region\":\"欧洲\"},{\"code\":\"SA\",\"en_name\":\"Saudi Arabia\",\"cn_name\":\"沙特阿拉伯\",\"region\":\"亚洲\"},{\"code\":\"SN\",\"en_name\":\"Senegal\",\"cn_name\":\"塞内加尔\",\"region\":\"非洲\"},{\"code\":\"RS\",\"en_name\":\"Serbia\",\"cn_name\":\"塞尔维亚\",\"region\":\"欧洲\"},{\"code\":\"SC\",\"en_name\":\"Seychelles\",\"cn_name\":\"塞舌尔群岛\",\"region\":\"非洲\"},{\"code\":\"SL\",\"en_name\":\"Sierra \\r\\nLeone\",\"cn_name\":\"塞拉利昂\",\"region\":\"非洲\"},{\"code\":\"SG\",\"en_name\":\"Singapore\",\"cn_name\":\"新加坡\",\"region\":\"亚洲\"},{\"code\":\"SK\",\"en_name\":\"Slovakia\",\"cn_name\":\"斯洛伐克\",\"region\":\"欧洲\"},{\"code\":\"SI\",\"en_name\":\"Slovenia\",\"cn_name\":\"斯洛文尼亚\",\"region\":\"欧洲\"},{\"code\":\"SB\",\"en_name\":\"Solomon \\r\\nIslands\",\"cn_name\":\"所罗门群岛\",\"region\":\"大洋洲\"},{\"code\":\"SO\",\"en_name\":\"Somalia\",\"cn_name\":\"索马里\",\"region\":\"非洲\"},{\"code\":\"ZA\",\"en_name\":\"South Africa\",\"cn_name\":\"南非\",\"region\":\"非洲\"},{\"code\":\"GS\",\"en_name\":\"South Georgia and the South Sandwich Islands\",\"cn_name\":\"南乔治亚岛和南桑威齐群岛\",\"region\":\"其他\"},{\"code\":\"KR\",\"en_name\":\"South Korea\",\"cn_name\":\"韩国\",\"region\":\"亚洲\"},{\"code\":\"ES\",\"en_name\":\"Spain\",\"cn_name\":\"西班牙\",\"region\":\"欧洲\"},{\"code\":\"LK\",\"en_name\":\"Sri \\r\\nLanka\",\"cn_name\":\"斯里兰卡\",\"region\":\"亚洲\"},{\"code\":\"SD\",\"en_name\":\"Sudan\",\"cn_name\":\"苏丹\",\"region\":\"非洲\"},{\"code\":\"SR\",\"en_name\":\"Suriname\",\"cn_name\":\"苏里南\",\"region\":\"南美洲\"},{\"code\":\"SJ\",\"en_name\":\"Svalbard and Jan Mayen\",\"cn_name\":\"斯瓦尔巴特和扬马延\",\"region\":\"其他\"},{\"code\":\"SZ\",\"en_name\":\"Swaziland\",\"cn_name\":\"斯威士兰\",\"region\":\"非洲\"},{\"code\":\"SE\",\"en_name\":\"Sweden\",\"cn_name\":\"瑞典\",\"region\":\"欧洲\"},{\"code\":\"CH\",\"en_name\":\"Switzerland\",\"cn_name\":\"瑞士\",\"region\":\"欧洲\"},{\"code\":\"SY\",\"en_name\":\"Syria\",\"cn_name\":\"叙利亚\",\"region\":\"亚洲\"},{\"code\":\"ST\",\"en_name\":\"São Tomé and \\r\\nPríncipe\",\"cn_name\":\"圣多美和普林西比\",\"region\":\"非洲\"},{\"code\":\"TW\",\"en_name\":\"Taiwan\",\"cn_name\":\"台湾\",\"region\":\"亚洲\"},{\"code\":\"TJ\",\"en_name\":\"Tajikistan\",\"cn_name\":\"塔吉克斯坦\",\"region\":\"亚洲\"},{\"code\":\"TZ\",\"en_name\":\"Tanzania\",\"cn_name\":\"坦桑尼亚\",\"region\":\"非洲\"},{\"code\":\"TH\",\"en_name\":\"Thailand\",\"cn_name\":\"泰国\",\"region\":\"亚洲\"},{\"code\":\"TL\",\"en_name\":\"Timor-Leste\",\"cn_name\":\"东帝汶\",\"region\":\"亚洲\"},{\"code\":\"TG\",\"en_name\":\"Togo\",\"cn_name\":\"多哥\",\"region\":\"非洲\"},{\"code\":\"TK\",\"en_name\":\"Tokelau\",\"cn_name\":\"托克劳\",\"region\":\"大洋洲\"},{\"code\":\"TO\",\"en_name\":\"Tonga\",\"cn_name\":\"汤加\",\"region\":\"大洋洲\"},{\"code\":\"TT\",\"en_name\":\"Trinidad and Tobago\",\"cn_name\":\"特立尼达和多巴哥\",\"region\":\"北美洲\"},{\"code\":\"TN\",\"en_name\":\"Tunisia\",\"cn_name\":\"突尼斯\",\"region\":\"非洲\"},{\"code\":\"TR\",\"en_name\":\"Turkey\",\"cn_name\":\"土耳其\",\"region\":\"亚洲\"},{\"code\":\"TM\",\"en_name\":\"Turkmenistan\",\"cn_name\":\"土库曼斯坦\",\"region\":\"亚洲\"},{\"code\":\"TC\",\"en_name\":\"Turks and Caicos Islands\",\"cn_name\":\"特克斯和凯科斯群岛\",\"region\":\"北美洲\"},{\"code\":\"TV\",\"en_name\":\"Tuvalu\",\"cn_name\":\"图瓦卢\",\"region\":\"大洋洲\"},{\"code\":\"UM\",\"en_name\":\"U.S. Minor \\r\\nOutlying Islands\",\"cn_name\":\"美国边远小岛\",\"region\":\"其他\"},{\"code\":\"VI\",\"en_name\":\"U.S. Virgin \\r\\nIslands\",\"cn_name\":\"美属维京群岛\",\"region\":\"北美洲\"},{\"code\":\"UG\",\"en_name\":\"Uganda\",\"cn_name\":\"乌干达\",\"region\":\"非洲\"},{\"code\":\"UA\",\"en_name\":\"Ukraine\",\"cn_name\":\"乌克兰\",\"region\":\"欧洲\"},{\"code\":\"AE\",\"en_name\":\"United Arab Emirates\",\"cn_name\":\"阿拉伯联合酋长国\",\"region\":\"亚洲\"},{\"code\":\"GB\",\"en_name\":\"United Kingdom\",\"cn_name\":\"英国\",\"region\":\"欧洲\"},{\"code\":\"US\",\"en_name\":\"United \\r\\nStates\",\"cn_name\":\"美国\",\"region\":\"北美洲\"},{\"code\":\"UY\",\"en_name\":\"Uruguay\",\"cn_name\":\"乌拉圭\",\"region\":\"南美洲\"},{\"code\":\"UZ\",\"en_name\":\"Uzbekistan\",\"cn_name\":\"乌兹别克斯坦\",\"region\":\"亚洲\"},{\"code\":\"VU\",\"en_name\":\"Vanuatu\",\"cn_name\":\"瓦努阿图\",\"region\":\"大洋洲\"},{\"code\":\"VA\",\"en_name\":\"Vatican \\r\\nCity\",\"cn_name\":\"梵蒂冈\",\"region\":\"欧洲\"},{\"code\":\"VE\",\"en_name\":\"Venezuela\",\"cn_name\":\"委内瑞拉\",\"region\":\"南美洲\"},{\"code\":\"VN\",\"en_name\":\"Vietnam\",\"cn_name\":\"越南\",\"region\":\"亚洲\"},{\"code\":\"WF\",\"en_name\":\"Wallis and Futuna\",\"cn_name\":\"瓦利斯和富图纳\",\"region\":\"其他\"},{\"code\":\"EH\",\"en_name\":\"Western\\r\\n Sahara\",\"cn_name\":\"西撒哈拉\",\"region\":\"非洲\"},{\"code\":\"YE\",\"en_name\":\"Yemen\",\"cn_name\":\"也门\",\"region\":\"亚洲\"},{\"code\":\"ZM\",\"en_name\":\"Zambia\",\"cn_name\":\"赞比亚\",\"region\":\"非洲\"},{\"code\":\"ZW\",\"en_name\":\"Zimbabwe\",\"cn_name\":\"津巴布韦\",\"region\":\"非洲\"},{\"code\":\"AX\",\"en_name\":\"Åland Islands\",\"cn_name\":\"奥兰群岛\",\"region\":\"其他\"}]}";
    }

    public void init() {

        Resources resources = GsonUtils.jsonToObject(json, Resources.class);

        List<WorldPoJo> records = resources.getRecords();

        for (WorldPoJo worldPoJo:
                records) {
            String key = worldPoJo.getCn_name();
            String value = worldPoJo.getRegion();
            worldMapDataToRegion.put(key, value);
        }

        for (WorldPoJo worldPoJo:
                records) {
            String key = worldPoJo.getCode();
            String value = worldPoJo.getCn_name();
            worldMapNameByCode.put(key, value);
        }

        for (WorldPoJo worldPoJo:
                records) {
            String key = worldPoJo.getEn_name();
            String value = worldPoJo.getCn_name();
            worldMapNameByEnName.put(key, value);
        }
    }

    /**
     * 获取洲级
     * @param countrie
     * @return
     */
    public String getContinentToCountrie(String countrie) {

        String continent = worldMapDataToRegion.get(countrie);
        return TextUtils.isEmpty(continent) ? "未知" : continent;
    }

    /**
     * 根据 code 获取中文国家
     * @param code
     * @return
     */
    public String getCnNameToCode(String code) {

        String continent = worldMapNameByCode.get(code);
        return TextUtils.isEmpty(continent) ? "未知" : continent;
    }

    /**
     * 根据英文名称获取中文名国家
     * @param enName
     * @return
     */
    public String getCnNameToEnName(String enName) {

        String continent = worldMapNameByEnName.get(enName);
        return TextUtils.isEmpty(continent) ? "未知" : continent;
    }

    public class Resources {

        private List<WorldPoJo> records;

        public List<WorldPoJo> getRecords() {
            return records;
        }

        public void setRecords(List<WorldPoJo> records) {
            this.records = records;
        }
    }

    public class WorldPoJo {

        private String code;

        private String en_name;

        private String cn_name;

        private String region;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getEn_name() {
            return en_name;
        }

        public void setEn_name(String en_name) {
            this.en_name = en_name;
        }

        public String getCn_name() {
            return cn_name;
        }

        public void setCn_name(String cn_name) {
            this.cn_name = cn_name;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }
    }
}
