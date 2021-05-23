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

class JarexSettingsTest extends ScreenAdapter {

    Stage stage;
    Table root;

    @Override
    public void show() {
        super.show();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        JarexSettings s = new JarexSettings(null, null);
        root = s.getContentTable();
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
            setScreen(new JarexSettingsTest());
        }

        @Override
        public void dispose() {
            super.dispose();
            VisUI.dispose();
        }
    }

}