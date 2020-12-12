package com.mygdx.gragdx.menu;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.gragdx.game.objects.Player;
import com.mygdx.gragdx.menu.objects.*;
import com.mygdx.gragdx.screens.DirectedGame;
import com.mygdx.gragdx.screens.GameScreen;
import com.mygdx.gragdx.screens.MenuScreen;
import com.mygdx.gragdx.screens.transitins.ScreenTransition;
import com.mygdx.gragdx.screens.transitins.ScreenTransitionFade;
import com.mygdx.gragdx.util.CameraHelper;
import com.mygdx.gragdx.util.Constants;

public class MenuController extends InputAdapter {
    private static final String TAG = MenuController.class.getName();

    public Level level;
    private DirectedGame game;

    public CameraHelper cameraHelper;
    public MenuScreen menuScreen;

    boolean leftPressed, rightPressed;

    // Rectangles for collision detection
    private Rectangle playerCollision = new Rectangle();
    private Rectangle rockCollision = new Rectangle();
    private Rectangle borderCollision = new Rectangle();
    private Rectangle castleCollision = new Rectangle();
    private Rectangle smithCollision = new Rectangle();
    private Rectangle armorCollision = new Rectangle();
    private Rectangle gymCollision = new Rectangle();
    private Rectangle wizardCollision = new Rectangle();

    Button buttonUse;

    String overlaps;


    public MenuController (DirectedGame game) {
        this.game = game;
        init();
    }


    private void init() {
        menuScreen = new MenuScreen(game);
        cameraHelper = new CameraHelper();
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
        float x = level.player.position.x + (level.player.bounds.width / 2);
        float y = level.player.position.y + (level.player.bounds.height / 2);
        cameraHelper.setPosition(x, y);
    }


    public void testCollisions() {
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

        Castle castle = level.castle;
        castleCollision.set(castle.position.x, castle.position.y, castle.bounds.width, castle.bounds.height);

        Smith smith = level.smith;
        smithCollision.set(smith.position.x + 0.8f, smith.position.y, smith.bounds.width - 4, smith.bounds.height);
        armorCollision.set(smith.position.x + 4.2f, smith.position.y, smith.bounds.width - 4.5f, smith.bounds.height);

        Gym gym = level.gym;
        gymCollision.set(gym.position.x + 0.6f, gym.position.y, gym.bounds.width - 1.5f, gym.bounds.height);

        Wizard wizard = level.wizard;
        wizardCollision.set(wizard.position.x, wizard.position.y, wizard.bounds.width - 3.4f, wizard.bounds.height);


        if (playerCollision.overlaps(castleCollision)) {
            overlaps = "castle";
            buttonUse.setVisible(true);
        } else if (playerCollision.overlaps(smithCollision)) {
            overlaps = "smith";
            buttonUse.setVisible(true);
        } else if (playerCollision.overlaps(armorCollision)) {
            overlaps = "armor";
            buttonUse.setVisible(true);
        } else if (playerCollision.overlaps(gymCollision)) {
            overlaps = "gym";
            buttonUse.setVisible(true);
        }  else if (playerCollision.overlaps(wizardCollision)) {
            overlaps = "wizard";
            buttonUse.setVisible(true);
        } else {
            buttonUse.setVisible(false);
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

    public void onCollisionPlayerWithRock(Rock rock) {
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
        Table tableRight = new Table();
        tableRight.setFillParent(true);
        tableRight.bottom();
        tableRight.left();

        Button leftButton = new Button(skin, "leftArrow");
        tableRight.add(leftButton).padBottom(50).padLeft(50);
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
        tableRight.add(rightButton).padBottom(50).padLeft(60);
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

        Table tableLeft = new Table();
        tableLeft.setFillParent(true);
        tableLeft.bottom();
        tableLeft.right();

        buttonUse = new Button(skin, "rightArrow");
        tableLeft.add(buttonUse).padBottom(50).padRight(50);
        buttonUse.setVisible(false);
        buttonUse.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switch (overlaps) {
                    case "castle":
                        ScreenTransition transition = ScreenTransitionFade.init(0.15f);
                        game.setScreen(new GameScreen(game), transition);
                        break;
                    case "smith":
                        break;
                    case "armor":
                        break;
                    case "gym":
                        break;
                    case "wizard":
                        break;
                }
            }
        });

        stage.addActor(tableRight);
        stage.addActor(tableLeft);
        return tableRight;
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
            level.player.velocity.x = -level.player.terminalVelocity.x;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || rightPressed) {
            level.player.velocity.x = level.player.terminalVelocity.x;
        }
    }
}
