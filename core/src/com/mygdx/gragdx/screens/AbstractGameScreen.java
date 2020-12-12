package com.mygdx.gragdx.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.mygdx.gragdx.game.Assets;

public abstract class AbstractGameScreen implements Screen {
    protected DirectedGame game;

    public AbstractGameScreen (DirectedGame game) {
        this.game = game;
    }

    public abstract void render(float deltaTime);

    public abstract void resize(int width, int height);

    public abstract void show();

    public abstract void hide();

    public abstract void pause();

    public abstract InputProcessor getInputProcessor ();

    public void resume() {}

    public void dispose() {
        Assets.instance.dispose();
    }
}