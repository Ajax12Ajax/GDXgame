package com.mygdx.gragdx.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.gragdx.game.objects.*;
import com.mygdx.gragdx.menu.objects.*;
import com.mygdx.gragdx.menu.objects.Rock;

public class Level {
    public static final String TAG = com.mygdx.gragdx.game.Level.class.getName();

    public Player player;

    // objects
    public Array<Rock> rocks;
    public Castle castle;
    public Smith smith;
    public Gym gym;
    public Wizard wizard;

    // decoration
    public Clouds clouds;
    public Mountains mountains;
    public WaterOverlay waterOverlay;

    public Level(String filename) {
        init(filename);
    }


    private void init(String filename) {
        // player character
        player = null;

        // objects
        rocks = new Array<Rock>();
        castle = new Castle();
        smith = new Smith();
        gym = new Gym();
        wizard = new Wizard();

        // load image file that represents the level data
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
        // scan pixels from top-left to bottom-right
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                AbstractGameObject obj = null;

                obj = new Rock();
                obj.position.set(pixelX, -1.5f);
                rocks.add((Rock)obj);
            }
        }


        AbstractGameObject obj = null;
        obj = new Player();
        obj.position.set(8, 0);
        player = (Player)obj;

        obj = new Castle();
        obj.position.set(0.5f, 0);
        castle = ((Castle) obj);

        obj = new Smith();
        obj.position.set(13, 0);
        smith = ((Smith) obj);

        obj = new Gym();
        obj.position.set(22, 0);
        gym = ((Gym) obj);

        obj = new Wizard();
        obj.position.set(31, 0);
        wizard = ((Wizard) obj);


        // decoration
        clouds = new Clouds(pixmap.getWidth());
        clouds.position.set(0, 2);
        mountains = new Mountains(pixmap.getWidth());
        mountains.position.set(-1, -1);
        waterOverlay = new WaterOverlay(pixmap.getWidth());
        waterOverlay.position.set(0, -3.75f);

        // free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "level '" + filename + "' loaded");
    }



    public void update (float deltaTime) {
        player.update(deltaTime);
        clouds.update(deltaTime);
    }


    public void render(SpriteBatch batch) {
        // Draw Mountains
        mountains.render(batch);

        // Draw Rocks
        for (Rock rock : rocks)
            rock.render(batch);

        // Drew Castle
        castle.render(batch);

        // Drew Smith
        smith.render(batch);

        // Drew Gym
        gym.render(batch);

        // Drew Gym
        wizard.render(batch);

        // Draw Player Character
        player.render(batch);

        // Draw Water Overlay
        waterOverlay.render(batch);

        // Draw Clouds
        clouds.render(batch);
    }
}
