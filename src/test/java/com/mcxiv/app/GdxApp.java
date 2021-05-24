package com.mcxiv.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.mcxiv.app.util.ThreadUtil;
import com.mcxiv.app.views.settings.ViewJarexSettings;

public class GdxApp extends ScreenAdapter {

    public static GdxApp instance = null;

    Stage stage;
    Table root;

    @Override
    public void show() {
        super.show();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        instance = this;
    }

    public void setTable(Table table) {

        if (root != null || table == null)
            return;

        root = table;
        root.setFillParent(true);
        stage.addActor(root);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "JarexUITest";
        config.width = 800;
        config.height = 480;
        new LwjglApplication(new Pack(), config);
    }

    static class Pack extends Game {

        @Override
        public void create() {
            VisUI.load();
            setScreen(new GdxApp());
        }

        @Override
        public void dispose() {
            super.dispose();
            VisUI.dispose();
        }
    }

    public static void test(Runnable runnable) throws InterruptedException {

        ThreadUtil.launch(() -> GdxApp.main(new String[0]));

        while (GdxApp.instance==null) {
            System.out.println("Lapse");
            Thread.sleep(1000);
        }

        runnable.run();
    }

}
