package com.mygdx.gragdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.gragdx.util.Constants;
import com.mygdx.gragdx.util.GamePreferences;

public class WorldRenderer implements Disposable {
    private static final String TAG = WorldRenderer.class.getName();

    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private SpriteBatch batch;
    private final WorldController worldController;

    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    private void init() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();
    }

    public void render() {
        renderWorld(batch);
        renderGui(batch);
    }

    private void renderWorld (SpriteBatch batch) {
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.level.render(batch);
        batch.end();
    }

    private void renderGui (SpriteBatch batch) {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();

        // draw collected gold coins icon + text (anchored to top left edge)
        renderGuiScore(batch);
        // draw extra lives icon + text (anchored to top right edge)
        renderGuiExtraLive(batch);
        if (GamePreferences.instance.showFpsCounter)
            renderGuiFpsCounter(batch);
        // draw game over text
        renderGuiGameOverMessage(batch);

        batch.end();
    }

    private void renderGuiScore (SpriteBatch batch) {
        float x = -15;
        float y = -25;
        batch.draw(Assets.instance.point.pointa, x, y, 50, 50, 80, 80, 0.35f, -0.35f, 0);
        Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.score, x + 75, y + 42);    }

    private void renderGuiExtraLive (SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 20 - Constants.LIVES_START * 50;
        float y = -15;
        for (int i = 0; i < Constants.LIVES_START; i++) {
            if (worldController.lives <= i) batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
            batch.draw(Assets.instance.test.head, x + i * 40, y, 50, 50, 70, 100, 0.35f, -0.35f, 0);
            batch.setColor(1, 1, 1, 1);
        }
    }

    private void renderGuiFpsCounter (SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 55;
        float y = cameraGUI.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
        if (fps >= 45) {
            // 45 or more FPS show up in green
            fpsFont.setColor(0, 1, 0, 1);
        } else if (fps >= 30) {
            // 30 or more FPS show up in yellow
            fpsFont.setColor(1, 1, 0, 1);
        } else {
            // less than 30 FPS show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }

        fpsFont.draw(batch, "FPS: " + fps, x, y);
        fpsFont.setColor(1, 1, 1, 1); // white
    }

    private void renderGuiGameOverMessage (SpriteBatch batch) {
        float x = cameraGUI.viewportWidth / 2;
        float y = cameraGUI.viewportHeight / 2;
        if (worldController.isGameOver()) {
            BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
            fontGameOver.setColor(1, 0, 0, 1);
            fontGameOver.draw(batch, "GAME OVER", x / 1, y / 1);
            fontGameOver.setColor(1, 1, 1, 1);
        }
    }

    public void resize(int width, int height) {
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();
        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float)height) * (float)width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
