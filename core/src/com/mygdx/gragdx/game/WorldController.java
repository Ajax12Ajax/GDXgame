package com.mygdx.gragdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.gragdx.game.objects.Point;
import com.mygdx.gragdx.game.objects.Rock;
import com.mygdx.gragdx.game.objects.Test;
import com.mygdx.gragdx.screens.StartScreen;
import com.mygdx.gragdx.util.CameraHelper;
import com.mygdx.gragdx.util.Constants;

public class WorldController extends InputAdapter {
    private static final String TAG = WorldController.class.getName();

    public Level level;
    public int lives;
    public int score;
    private Game game;

    public CameraHelper cameraHelper;

    private float timeLeftGameOverDelay;

    // Rectangles for collision detection
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();



    public WorldController (Game game) {
        this.game = game;
        init();
    }


    private void init() {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START;
        timeLeftGameOverDelay = 0;
        initLevel();
    }

    private void initLevel() {
        score = 0;
        level = new Level(Constants.LEVEL_01);
        cameraHelper.setTarget(level.test);
    }


    public void update(float deltaTime) {
        handleDebugInput(deltaTime);
        if (isGameOver()) {
            timeLeftGameOverDelay -= deltaTime;
            if (timeLeftGameOverDelay < 0)
                backToMenu();
        } else {
            handleInputGame(deltaTime);
        }
        level.update(deltaTime);
        testCollisions();
        cameraHelper.update(deltaTime, 55, 5.55f);
        if (!isGameOver() && isPlayerInWater()) {
            lives--;
            if (isGameOver())
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            else
                initLevel();
        }
        level.mountains.updateScrollPosition(cameraHelper.getPosition());
    }


    public boolean isGameOver() {
        return lives <= 0;
    }

    public boolean isPlayerInWater() {
        return level.test.position.y < -5;
    }

    private void onCollisionBunnyHeadWithRock(Rock rock) {
        Test test = level.test;
        float heightDifference = Math.abs(test.position.y
                - (rock.position.y + rock.bounds.height));
        if (heightDifference > 0.25f) {
            boolean hitRightEdge = test.position.x > (
                    rock.position.x + rock.bounds.width / 2.0f);
            if (hitRightEdge) {
                test.position.x = rock.position.x + rock.bounds.width;
            } else {
                test.position.x = rock.position.x -
                        test.bounds.width;
            }
            return;
        }
        switch (test.jumpState) {
            case GROUNDED:
                break;
            case FALLING:
            case JUMP_FALLING:
                test.position.y = rock.position.y +
                        test.bounds.height + test.origin.y;
                test.jumpState = Test.JUMP_STATE.GROUNDED;
                break;
            case JUMP_RISING:
                test.position.y = rock.position.y +
                        test.bounds.height + test.origin.y;
                break;
        }
    }

    private void onCollisionBunnyWithGoldCoin(Point point) {
        point.collected = true;
        score += point.getScore();
    }



    private void testCollisions() {
        r1.set(level.test.position.x, level.test.position.y,
                level.test.bounds.width, level.test.bounds.height);
        // Test collision: Bunny Head <-> Rocks
        for (Rock rock : level.rocks) {
            r2.set(rock.position.x, rock.position.y, rock.bounds.width,
                    rock.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyHeadWithRock(rock);
            // IMPORTANT: must do all collisions for valid
            // edge testing on rocks.
        }
        // Test collision: Bunny Head <-> Gold Coins
        for (Point point : level.points) {
            if (point.collected) continue;
            r2.set(point.position.x, point.position.y,
                    point.bounds.width, point.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyWithGoldCoin(point);
            break;
        }
    }

    private void handleDebugInput(float deltaTime) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) cameraHelper.setZoom(1);
        if (Gdx.input.isKeyPressed(Input.Keys.T)) {
            lives--;
            if (isGameOver())
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            else
                initLevel();
        }

    }

    private void handleInputGame(float deltaTime) {
        //if (cameraHelper.hasTarget(level.test)) {
        // Player Movement
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            level.test.velocity.x = -level.test.terminalVelocity.x;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            level.test.velocity.x = level.test.terminalVelocity.x;
        } else {
            // Execute auto-forward movement on non-desktop platform
            if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
                level.test.velocity.x = level.test.terminalVelocity.x;
            }
        }

        // Bunny Jump
        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            level.test.setJumping(true);
        } else {
            level.test.setJumping(false);
        }
        //}
    }

    private void moveCamera(float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }

    private void backToMenu() {
        // switch to menu screen
        game.setScreen(new StartScreen(game));
    }

    @Override
    public boolean keyUp(int keycode) {
        // Reset game world
        if (keycode == Input.Keys.R) {
            init();
            Gdx.app.debug(TAG, "Game world resetted");
        }
        // Toggle camera follow
        else if (keycode == Input.Keys.ENTER) {
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.test);
            Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
        }
        // Back to Menu
        else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            backToMenu();
        }
        return false;
    }
}