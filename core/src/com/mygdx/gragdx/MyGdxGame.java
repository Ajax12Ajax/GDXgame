package com.mygdx.gragdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.gragdx.game.Assets;
import com.mygdx.gragdx.screens.DirectedGame;
import com.mygdx.gragdx.screens.StartScreen;
import com.mygdx.gragdx.screens.transitins.ScreenTransition;
import com.mygdx.gragdx.screens.transitins.ScreenTransitionFade;
import com.mygdx.gragdx.util.AudioManager;
import com.mygdx.gragdx.util.Preferences;

public class MyGdxGame extends DirectedGame {
    private static final String TAG = MyGdxGame.class.getName();

    @Override
    public void create() {
        // Set Libgdx log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        // Load assets
        Assets.instance.init(new AssetManager());
        // Load preferences for audio settings and start playing music
        Preferences.instance.load();
        AudioManager.instance.play(Assets.instance.music.song01);
        // Start game at menu screen
        ScreenTransition transition = ScreenTransitionFade.init(0.9f);
        setScreen(new StartScreen(this), transition);
    }
}