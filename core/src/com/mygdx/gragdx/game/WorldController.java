package com.mygdx.gragdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.gragdx.game.objects.Player;
import com.mygdx.gragdx.game.objects.Rock;
import com.mygdx.gragdx.screens.DirectedGame;
import com.mygdx.gragdx.util.CameraHelper;
import com.mygdx.gragdx.util.Constants;

public class WorldController extends InputAdapter {
    private static final String TAG = WorldController.class.getName();

    public Level level;
    public int lives;
    private DirectedGame game;

    boolean leftPressed, rightPressed, upPressed;

    public CameraHelper cameraHelper;

    // Rectangles for collision detection
    private Rectangle playerCollision = new Rectangle();
    private Rectangle rockCollision = new Rectangle();
    private Rectangle borderCollision = new Rectangle();




    public WorldController (DirectedGame game) {
        this.game = game;
        init();
    }


    private void init() {
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START;
        initLevel();
    }

    private void initLevel() {
        level = new Level(Constants.LEVEL_01);
    }


    public void update(float deltaTime, Stage stage) {
        Gdx.input.setInputProcessor(stage);
        handleDebugInput(deltaTime);
        if (isGameOver()) {
        } else {
            handleInputGame();
        }
        level.update(deltaTime);
        testCollisions();
        level.mountains.updateScrollPosition(cameraHelper.getPosition());
        cameraMove();
    }

    private void cameraMove() {
        float x = level.player.position.x + (level.player.bounds.width / 2);
        float y = level.player.position.y + (level.player.bounds.height / 2);
        cameraHelper.setPosition(x, y);
    }

    public boolean isGameOver() {
        return lives <= 0;
    }


    private void testCollisions() {
        playerCollision.set(level.player.position.x, level.player.position.y, level.player.bounds.width, level.player.bounds.height);
        // Test collision: Player <-> Rocks
        for (Rock rock : level.rocks) {
            rockCollision.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);

            if (playerCollision.overlaps(rockCollision)) {
                onCollisionPlayerWithRock(rock);
            }
        }
        borderCollision.set(5.1f, -3, 1, 50);

        if (playerCollision.overlaps(borderCollision)) {
            onCollisionPlayerWithBorder();
        }

        borderCollision.set(50, -3, 1, 50);

        if (playerCollision.overlaps(borderCollision)) {
            onCollisionPlayerWithBorder();
        }
    }

    public void onCollisionPlayerWithBorder() {
        Player player = level.player;
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

    private void onCollisionPlayerWithRock(Rock rock) {
        Player player = level.player;
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
                player.jumpState = Player.JUMP_STATE.GROUNDED;
                break;
            case JUMP_RISING:
                player.position.y = rock.position.y + player.bounds.height + player.origin.y;
                break;
        }
    }



    public Table addController(Stage stage, Skin skin) {
        Table tableLeft = new Table();
        tableLeft.setFillParent(true);
        tableLeft.bottom();
        tableLeft.left();

        Button leftButton = new Button(skin, "leftArrow");
        tableLeft.add(leftButton).padBottom(50).padLeft(50);
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
        tableLeft.add(rightButton).padBottom(50).padLeft(60);
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

        Table tableRight = new Table();
        tableRight.setFillParent(true);
        tableRight.bottom();
        tableRight.right();

        Button upButton = new Button(skin, "upArrow");
        tableRight.add(upButton).padBottom(50).padRight(60);
        upButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        stage.addActor(tableRight);
        stage.addActor(tableLeft);
        return tableLeft;
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
        // Player Movement
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || leftPressed) {
            level.player.velocity.x = -level.player.terminalVelocity.x;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || rightPressed) {
            level.player.velocity.x = level.player.terminalVelocity.x;
        }
        // Jump
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || upPressed) {
            level.player.setJumping(true);
        } else {
            level.player.setJumping(false);
        }
    }
}