package com.bootx.util;

import com.hankcs.hanlp.HanLP;
import org.jsoup.Jsoup;

import java.util.List;

public final class HanLPUtils {

    public static String getZhaiYao(String content) {
        // 需要去除富文本内容
        String content1 = Jsoup.parse(content).text();
        try {
            return HanLP.getSummary(content1, 200);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> extractKeyword(String content) {
        // 需要去除富文本内容
        String content1 = Jsoup.parse(content).text();
        try {
            return HanLP.extractKeyword(content1, 5);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
