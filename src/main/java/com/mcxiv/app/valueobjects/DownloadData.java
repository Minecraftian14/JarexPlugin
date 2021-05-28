package com.mcxiv.app.valueobjects;

import com.mcxiv.app.ui.DialogMan;
import com.mcxiv.app.util.CUD;
import com.mcxiv.app.util.GithubUtil;
import com.mcxiv.app.util.HttpDownloadUtility;
import games.rednblack.h2d.common.network.model.GithubReleaseData;

import java.io.File;

public class DownloadData {

    private final String latestUpdateKey;
    private final String pluginName;
    private final String cachePath;
    private final String jarPath;
    private GithubReleaseData releaseData = null;

    private final LinkData link;

    public DownloadData(LinkData _link, String _cachePath) {
        link = _link;
        pluginName = GithubUtil.displayName(link.getLink());
        latestUpdateKey = "latest_update:" + pluginName;
        cachePath = _cachePath;
        jarPath = cachePath + File.separator + pluginName + ".jar";

        releaseData = CUD.Try(() -> HttpDownloadUtility.getGithubReleaseData(link.getLink())).Default(() -> null);
    }

    public void setReleaseData(GithubReleaseData releaseData) {
        this.releaseData = releaseData;
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

    public GithubReleaseData getReleaseData() {
        return releaseData;
    }

    public LinkData getLink() {
        return link;
    }
}
