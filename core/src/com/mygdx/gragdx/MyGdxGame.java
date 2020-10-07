package com.mygdx.gragdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.gragdx.game.Assets;
import com.mygdx.gragdx.screens.MenuScreen;

public class MyGdxGame extends Game {

    @Override
    public void create() {
        // Set Libgdx log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        // Load assets
        Assets.instance.init(new AssetManager());
        // Start game at menu screen
        setScreen(new MenuScreen(this));
    }
}