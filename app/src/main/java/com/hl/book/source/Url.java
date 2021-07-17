package com.hl.book.source;

public class Url {
    public static class UrlBiQuGe extends Url{
        private static String BaseUrl = "https://www.biquge.com.cn";
        public static String BookUrl = BaseUrl;
        public static String ChapterUrl = BaseUrl;
        public static String SearchUrl = BaseUrl+"/search.php?q=";
    }
    public static class UrlBiQuYue extends Url{
        private static String BaseUrl = "https://www.biquyue.com";
        public static String BookUrl = BaseUrl;
        public static String ChapterUrl = BaseUrl;
        public static String SearchUrl = BaseUrl+"/search.php?q=";
    }
}
