package com.mygdx.gragdx.menu;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.gragdx.game.objects.Test;
import com.mygdx.gragdx.menu.objects.Rock;
import com.mygdx.gragdx.screens.MenuScreen;
import com.mygdx.gragdx.util.CameraHelper;
import com.mygdx.gragdx.util.Constants;

public class MenuController extends InputAdapter {
    private static final String TAG = MenuController.class.getName();

    public Level level;
    private Game game;

    public CameraHelper cameraHelper;
    public MenuScreen menuScreen;

    boolean leftPressed, rightPressed;

    // Rectangles for collision detection
    private Rectangle playerCollision = new Rectangle();
    private Rectangle rockCollision = new Rectangle();
    private Rectangle borderCollision = new Rectangle();


    public MenuController(Game game) {
        this.game = game;
        init();
    }


    private void init() {
        menuScreen = new MenuScreen(game);
        cameraHelper = new CameraHelper();
        initLevel();
    }

    private void initLevel() {
        level = new Level(Constants.LEVEL_01);
    }


    public void update(float deltaTime, Stage stage) {
        Gdx.input.setInputProcessor(stage);
        handleDebugInput(deltaTime);
        handleInputGame();
        level.update(deltaTime);
        testCollisions();
        level.mountains.updateScrollPosition(cameraHelper.getPosition());
        cameraMove();

    }

    private void cameraMove() {
        float x = level.test.position.x + (level.test.bounds.width / 2);
        float y = level.test.position.y + (level.test.bounds.height / 2);
        cameraHelper.setPosition(x, y);
    }


    public void testCollisions() {
        playerCollision.set(level.test.position.x, level.test.position.y, level.test.bounds.width, level.test.bounds.height);
        // Test collision: Player <-> Rocks
        for (Rock rock : level.rocks) {
            rockCollision.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);

            if (playerCollision.overlaps(rockCollision)) {
                onCollisionPlayerWithRock(rock);
            }
        }
        borderCollision.set(5, -3, 1, 50);

        if (playerCollision.overlaps(borderCollision)) {
            onCollisionPlayerWithBorder();
        }

        borderCollision.set(50, -3, 1, 50);

        if (playerCollision.overlaps(borderCollision)) {
            onCollisionPlayerWithBorder();
        }
    }

    public void onCollisionPlayerWithBorder() {
        Test player = level.test;
        float heightDifference = Math.abs(player.position.y - (borderCollision.getX() + borderCollision.getHeight()));
        if (heightDifference > 0.25f) {
            boolean hitRightEdge = player.position.x > (borderCollision.getX() + borderCollision.getWidth() / 2.0f);
            if (hitRightEdge) {
                player.position.x = borderCollision.getX() + borderCollision.getWidth();
            } else {
                player.position.x = borderCollision.getX() - player.bounds.width;
            }
            return;
        }
    }

    public void onCollisionPlayerWithRock(Rock rock) {
        Test player = level.test;
        float heightDifference = Math.abs(player.position.y - (rock.position.y + rock.bounds.height));
        if (heightDifference > 0.25f) {
            boolean hitRightEdge = player.position.x > (rock.position.x + rock.bounds.width / 2.0f);
            if (hitRightEdge) {
                player.position.x = rock.position.x + rock.bounds.width;
            } else {
                player.position.x = rock.position.x - player.bounds.width;
            }
            return;
        }

        switch (player.jumpState) {
            case GROUNDED:
                break;
            case FALLING:
            case JUMP_FALLING:
                player.position.y = rock.position.y + player.bounds.height + player.origin.y;
                player.jumpState = Test.JUMP_STATE.GROUNDED;
                break;
            case JUMP_RISING:
                player.position.y = rock.position.y + player.bounds.height + player.origin.y;
                break;
        }
    }


    public Table addController(Stage stage, Skin skin) {
        Table table = new Table();
        table.setFillParent(true);
        table.bottom();
        table.right();

        Button leftButton = new Button(skin, "leftArrow");
        table.add(leftButton).padBottom(50).padRight(60);
        leftButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        Button rightButton = new Button(skin, "rightArrow");
        table.add(rightButton).padBottom(50).padRight(50);
        rightButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        stage.addActor(table);
        return table;
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
        if (Gdx.input.isKeyPressed(Input.Keys.R)) init();
    }

    private void handleInputGame() {
        // Player Movement
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || leftPressed) {
            level.test.velocity.x = -level.test.terminalVelocity.x;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || rightPressed) {
            level.test.velocity.x = level.test.terminalVelocity.x;
        }
    }
}
