package com.mygdx.gragdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.gragdx.game.Assets;
import com.mygdx.gragdx.screens.DirectedGame;
import com.mygdx.gragdx.screens.StartScreen;
import com.mygdx.gragdx.screens.transitins.ScreenTransition;
import com.mygdx.gragdx.screens.transitins.ScreenTransitionFade;

public class MyGdxGame extends DirectedGame {
    private static final String TAG = MyGdxGame.class.getName();

    @Override
    public void create() {
        // Set Libgdx log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        // Load assets
        Assets.instance.init(new AssetManager());
        // Start game at menu screen
        ScreenTransition transition = ScreenTransitionFade.init(0.9f);
        setScreen(new StartScreen(this), transition);
    }
}