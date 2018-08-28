package com.zin.record.dto;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhujinming on 2017/11/28.
 */
public class ImageDto implements Serializable {

    private static Timer timer;

    public static void main(String[] args) {

//        while (true) {
//            try {
//                Thread.sleep(1000);
//            } catch (Exception e) {
//            }
//            Random random = new Random();
//            int number = random.nextInt(2) + 1;
//            System.out.println(number);
//        }

        String p = "12-13";
        String[] p1 = p.split(",");
        System.out.println(p1);

        long currentTime = System.currentTimeMillis();
        long oldTime = Long.valueOf("1527133420448");

        long day = (currentTime - oldTime) / (1000 * 60);

//        if (day > 0)
//        System.out.println(day);

//        contrast();
        isContain("【我分享给你了一个淘宝页面，快来看看吧】http://m.tb.cn/h.30m0vgs 点击链接，再选择浏览器咑閞；或復·制这段描述€zfa80xp4nFC€后咑閞\uD83D\uDC49淘♂寳♀\uD83D\uDC48[来自超级会员的分享]");


        Timer timer = getTimer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(getTimer().purge());
                return;
            }
        }, 1000, 1000);
//
//
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Timer timer1 = getTimer();
//                timer1.cancel();
//            }
//        }, 10000);
//
//        System.out.println(getTimer().purge());
//        String s = "http://c.gdt.qq.com/gdt_trace_a.fcg?actionid=5&targettype=6&tagetid=1106806122&clickid=5vvlywtibyaaagnvjjqq";
//         /* 找出指定的2个字符在 该字符串里面的 位置 */
//        int strStartIndex = s.indexOf("tagetid=");
//        String party = s.substring(strStartIndex, s.length());
//        int strEndIndex = party.indexOf("&");
//
//        /* index 为负数 即表示该字符串中 没有该字符 */
//        if (strStartIndex < 0) {
//            System.out.println("字符串 :---->" + s + "<---- 中不存在 " + "tagetid=" + ", 无法截取目标字符串");
//        }
//        if (strEndIndex < 0) {
//            System.out.println("字符串 :---->" + s + "<---- 中不存在 " + "&" + ", 无法截取目标字符串");
//        }
//        /* 开始截取 */
//        String result = party.substring(0, strEndIndex).substring("tagetid=".length());
//        System.out.println(result);
//
//        Random random = new Random();
//        int number = random.nextInt(1000);
//        System.out.println(number);
//

//        String World = "s=_AZMX_&ss=_AZMY_";
//        World = World.replaceAll("_AZMX_", "xx");
//        World = World.replaceAll("_AZMY_", "xxss");
//        String xx = String.format(World, "World", "xx");
//        System.out.println(World);




//        // 要验证的字符串
//        String str = "快来领支付宝红包！人人可领，天天可领！复制此消息，打开最新版支付宝就能领取！tlxTLD62rx";
//        // 正则表达式规则
//        String regEx = "[party-zA-Z0-9]{10}?";
//
//        // 编译正则表达式
//        Pattern pattern = Pattern.compile(regEx);
//        // 忽略大小写的写法
//        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(str);
//        // 查找字符串中是否有匹配正则表达式的字符/字符串
//        boolean rs = matcher.find();
//        boolean ra = str.contains("支付宝");
//        System.out.println(rs + " : " + ra);
//        Date date = getNextDay(new Date());
//        long party = System.currentTimeMillis();
//        long b = date.getTime();
//        System.out.println(party-b + " : " + party + " : " + b);
    }

    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -2);//+1今天的时间加一天
        date = calendar.getTime();
        return date;
    }

    private static void contrast() {
        for (int i = 1; i < 25; i++) {
            System.out.print(i + " : ");
            System.out.println(test(01,02, i));
        }
    }

    private static boolean isContain(String str) {
        // 编译正则表达式
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]{11}?");
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        // 查找字符串中是否有匹配正则表达式的字符/字符串
        boolean rs = matcher.find();
//        boolean ra = str.contains(aliPay);

        String[] strings = pattern.split(str);
        int stringLength = strings.length;
        if (stringLength != 2) {
            return false;
        }
        char startStr = strings[0].charAt(strings[0].length() - 1);
        char endStr = strings[1].charAt(0);

        return rs && startStr == endStr;
    }

    static int test(int begin, int end, int c){
        if(begin==c || end==c)
            return 1;
        if((begin > c) != ( end > c))
            return 1;
        return -1;

    }

    private static Timer getTimer() {

        if (timer == null) {
            timer = new Timer();
        }
        return timer;
    }

    private String id;

    private int width;

    private int height;

    private String url;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
