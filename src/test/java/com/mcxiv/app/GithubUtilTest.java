package com.mcxiv.app;

import com.mcxiv.app.util.GithubUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GithubUtilTest {

    @Test
    void test() {
        assertEquals("Skin Composer", GithubUtil.displayName("https://api.github.com/repos/raeleus/skin-composer/releases/latest"));
        assertEquals("Novix", GithubUtil.displayName("https://api.github.com/repos/Minecraftian14/Novix/releases/latest"));
        assertEquals("Skin Composer.jar", GithubUtil.applicationJarName("https://api.github.com/repos/raeleus/skin-composer/releases/latest"));
        assertEquals("Novix.jar", GithubUtil.applicationJarName("https://api.github.com/repos/Minecraftian14/Novix/releases/latest"));

        assertEquals("https://api.github.com/repos/a/b/releases/latest", GithubUtil.link("a b"));
    }


}