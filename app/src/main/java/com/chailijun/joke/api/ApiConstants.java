package com.chailijun.joke.api;



public class ApiConstants {

    public static final String HOST = "http://japi.juhe.cn/joke/";
    public static final String HOST2 = "http://v.juhe.cn/joke/";

    /**按更新时间查询笑话*/
    public static final String JOKE_LIST= "http://japi.juhe.cn/joke/content/list.from";

    /**最新笑话*/
    public static final String JOKE_NEWEST = "http://japi.juhe.cn/joke/content/text.from";

    /**按更新时间查询趣图*/
    public static final String PIC_LIST = "http://japi.juhe.cn/joke/img/list.from";

    /**最新趣图*/
    public static final String PIC_NEWEST = "http://japi.juhe.cn/joke/img/text.from";

    /**随机获取趣图/笑话*/
    public static final String RAND_JOKE = "http://v.juhe.cn/joke/randJoke.php";
}
