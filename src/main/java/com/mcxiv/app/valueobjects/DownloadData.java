package com.mcxiv.app.valueobjects;

import com.mcxiv.app.util.GithubUtil;
import games.rednblack.h2d.common.network.model.GithubReleaseData;

import java.io.File;

public class DownloadData {

    private final String latestUpdateKey;
    private final String pluginName;
    private final String cachePath;
    private final String jarPath;
    private GithubReleaseData data = null;

    private final String link;

    public DownloadData(String _link, String _cachePath) {
        link = _link;
        pluginName = GithubUtil.displayName(link);
        latestUpdateKey = "latest_update:"+pluginName;
        cachePath = _cachePath;
        jarPath = cachePath + File.separator + pluginName + ".jar";
    }

    public void setData(GithubReleaseData data) {
        this.data = data;
    }

    public String getLatestUpdateKey() {
        return latestUpdateKey;
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getCachePath() {
        return cachePath;
    }

    public String getJarPath() {
        return jarPath;
    }

    public GithubReleaseData getData() {
        return data;
    }

    public String getLink() {
        return link;
    }
}
