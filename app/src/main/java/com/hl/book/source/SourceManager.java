package com.hl.book.source;

import com.hl.book.source.source.Source;
import com.hl.book.source.source.SourceBiQuGe;
import com.hl.book.source.source.SourceBiQuGe1;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局小说源管理类
 */
public class SourceManager {
    private static SourceManager instance;

    private List<Source> sourceList;
    public static SourceManager getInstance() {
        if (instance == null){
            instance = new SourceManager();
        }
        return instance;
    }

    private SourceManager() {
        sourceList = new ArrayList<>();
        sourceList.add(new SourceBiQuGe1());
        sourceList.add(new SourceBiQuGe());
    }

    public List<Source> getSourceList() {
        return sourceList;
    }
    public Source getDefaultSource() {
        return sourceList.get(0);
    }
    public Source getSourceByLink(String link){
        if (!link.startsWith("http")){
            return getDefaultSource();
        }
        URL url;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return getDefaultSource();
        }
        String host = url.getHost();
        for (Source source:sourceList) {
            if (source.bookUrl.contains(host)){
                return source;
            }
        }
        return getDefaultSource();
    }
}
