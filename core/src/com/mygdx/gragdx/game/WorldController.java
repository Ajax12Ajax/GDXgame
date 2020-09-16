package com.mygdx.gragdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.gragdx.util.CameraHelper;

public class WorldController extends InputAdapter {
    private static final String TAG =
            WorldController.class.getName();

    public Sprite[] testSprites;
    public int selectedSprite;
    public CameraHelper cameraHelper;

    public WorldController() {
        init();
    }

    private void init() {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        initTestObjects();
    }

    @Override
    public boolean keyUp(int keycode) {
        // Reset game world
        if (keycode == Input.Keys.R) {
            init();
            Gdx.app.debug(TAG, "Game world resetted");
        }
        // Select next sprite
        else if (keycode == Input.Keys.SPACE) {
            selectedSprite = (selectedSprite + 1) % testSprites.length;
            // Update camera's target to follow the currently
            // selected sprite
            if (cameraHelper.hasTarget()) {
                cameraHelper.setTarget(testSprites[selectedSprite]);
            }
            Gdx.app.debug(TAG, "Sprite #" + selectedSprite + " selected");
            // Toggle camera follow
        } else if (keycode == Input.Keys.ENTER) {
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null :
                    testSprites[selectedSprite]);
            Gdx.app.debug(TAG, "Camera follow enabled: " +
                    cameraHelper.hasTarget());
        }

        return false;
    }

    private void initTestObjects() {
        // Create new array for 5 sprites
        testSprites = new Sprite[2];
        Texture texture = new
                Texture(Gdx.files.internal("test.png"));
        // Create new sprites using the just created texture
        for (int i = 0; i < testSprites.length; i++) {
            Sprite spr = new Sprite(texture);
            // Define sprite size to be 1m x 1m in game world
            spr.setSize(1, 1);
            // Set origin to sprite's center
            spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
            // Calculate random position for sprite
            float randomX = MathUtils.random(-2.0f, 2.0f);
            float randomY = MathUtils.random(-2.0f, 2.0f);
            spr.setPosition(randomX, randomY);
            // Put new sprite into array
            testSprites[i] = spr;
        }
        // Set first sprite as selected one
        selectedSprite = 0;
    }

    public void update(float deltaTime) {
        handleDebugInput(deltaTime);
        cameraHelper.update(deltaTime);
    }

    private void handleDebugInput(float deltaTime) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

// Selected Sprite Controls
        float sprMoveSpeed = 5 * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) moveSelectedSprite(
                -sprMoveSpeed, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.D)) moveSelectedSprite(
                sprMoveSpeed, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.W)) moveSelectedSprite(0,
                sprMoveSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.S)) moveSelectedSprite(0,
                -sprMoveSpeed);


//---------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------

        // Camera Controls (move)
        float camMoveSpeed = 5 * deltaTime;
        float camMoveSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camMoveSpeed *=
                camMoveSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveCamera(camMoveSpeed,
                0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveCamera(-camMoveSpeed,
                0);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveCamera(0, -camMoveSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveCamera(0,
                camMoveSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
            cameraHelper.setPosition(0, 0);



        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camZoomSpeed *=
                camZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA))
            cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(
                -camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) cameraHelper.setZoom(1);

//---------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------
    }

    private void moveCamera (float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }


    private void moveSelectedSprite(float x, float y) {
        testSprites[selectedSprite].translate(x, y);
    }
}
