package com.mcxiv.app;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.mcxiv.app.valueobjects.JarexSettingsData;
import com.mcxiv.app.valueobjects.LinkData;
import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.data.ProjectInfoVO;
import games.rednblack.h2d.common.IItemCommand;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;
import games.rednblack.h2d.common.plugins.PluginAPI;
import games.rednblack.h2d.common.view.tools.Tool;
import games.rednblack.h2d.common.vo.CursorData;
import games.rednblack.h2d.common.vo.EditorConfigVO;
import games.rednblack.h2d.common.vo.ProjectVO;
import games.rednblack.h2d.common.vo.SceneConfigVO;
import org.lwjgl.Sys;
import org.puremvc.java.interfaces.IFacade;
import org.puremvc.java.patterns.facade.Facade;

import java.util.HashMap;
import java.util.HashSet;

public class PureMVCUtil {

    public static H2DPluginAdapter plugin = new JarexPlugin();

    public static IFacade facade = new Facade();

    private static EditorConfigVO editorConfigVO = new EditorConfigVO();

    static {
        plugin.setAPI(new PluginAPI() {
            @Override
            public SceneLoader getSceneLoader() {
                return null;
            }

            @Override
            public IFacade getFacade() {
                return facade;
            }

            @Override
            public PooledEngine getEngine() {
                return null;
            }

            @Override
            public Stage getUIStage() {
                return GdxApp.instance.stage;
            }

            @Override
            public String getPluginDir() {
                return null;
            }

            @Override
            public String getCacheDir() {
                return "D:\\AppData\\Roaming\\.hyperlap2d\\cache";
            }

            @Override
            public String getProjectPath() {
                return null;
            }

            @Override
            public TextureAtlas getProjectTextureAtlas() {
                return null;
            }

            @Override
            public void addMenuItem(String menu, String subMenuName, String notificationName) {

            }

            @Override
            public void addTool(String toolName, VisImageButton.VisImageButtonStyle toolBtnStyle, boolean addSeparator, Tool tool) {

            }

            @Override
            public void toolHotSwap(Tool tool) {

            }

            @Override
            public void toolHotSwapBack() {

            }

            @Override
            public void setDropDownItemName(String action, String name) {

            }

            @Override
            public void reLoadProject() {

            }

            @Override
            public void saveProject() {

            }

            @Override
            public void revertibleCommand(IItemCommand command, Object body) {

            }

            @Override
            public void removeFollower(Entity entity) {

            }

            @Override
            public Entity drawImage(String regionName, Vector2 position) {
                return null;
            }

            @Override
            public HashSet<Entity> getProjectEntities() {
                return null;
            }

            @Override
            public boolean isEntityVisible(Entity entity) {
                return false;
            }

            @Override
            public void showPopup(HashMap<String, String> actionsSet, Object observable) {

            }

            @Override
            public void setCursor(CursorData cursorData, TextureRegion region) {

            }

            @Override
            public String getCurrentSelectedLayerName() {
                return null;
            }

            @Override
            public EditorConfigVO getEditorConfig() {
                return editorConfigVO;
            }

            @Override
            public SceneConfigVO getCurrentSceneConfigVO() {
                return null;
            }

            @Override
            public ProjectVO getCurrentProjectVO() {
                return null;
            }

            @Override
            public ProjectInfoVO getCurrentProjectInfoVO() {
                return null;
            }
        });

        JarexSettingsData data = new JarexSettingsData() {{
            registeredLinks.add(
                    new LinkData("https://api.github.com/repos/raeleus/skin-composer/releases/latest", true),
                    new LinkData("https://api.github.com/repos/Minecraftian14/Novix/releases/latest", false)
            );
        }};
        data.toStorage(plugin.getStorage());
    }

}
