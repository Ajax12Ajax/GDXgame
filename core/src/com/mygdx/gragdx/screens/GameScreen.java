package com.mygdx.gragdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.gragdx.game.WorldController;
import com.mygdx.gragdx.game.WorldRenderer;
import com.mygdx.gragdx.util.Constants;
import com.mygdx.gragdx.util.Preferences;

public class GameScreen extends AbstractGameScreen {
    private static final String TAG = GameScreen.class.getName();

    public final Stage stageGui = new Stage(new ExtendViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
    public Skin skin;

    private WorldController worldController;
    private WorldRenderer worldRenderer;
    private PauseMenu pauseMenu;
    private ToolsMenu toolsMenu;
    public boolean paused;
    private boolean pauseCheck = true;


    public GameScreen(DirectedGame game) {
        super(game);
    }

    @Override
    public void render(float deltaTime) {
        // Do not update game world when paused.
        if (!paused) {
            // Update game world by the time that has passed
            // since last rendered frame.
            worldController.update(deltaTime, stageGui);
        }
        // Sets the clear screen color to: Cornflower Blue
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed /
                255.0f, 0xff / 255.0f);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Render game world to screen
        worldRenderer.render();

        stageGui.getViewport().apply();
        stageGui.act(deltaTime);
        stageGui.draw();

        pauseMenu.draw(deltaTime);
        toolsMenu.draw(deltaTime);

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            paused = true;
            if (pauseCheck) {
                PauseAdd();
                pauseCheck = false;
            }
        }
    }


    @Override
    public void show() {
        pauseMenu = new PauseMenu();
        toolsMenu = new ToolsMenu();

        skin = new Skin(
                Gdx.files.internal(Constants.SKIN_HUD_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_HUD_UI));

        Preferences.instance.load();
        worldController = new WorldController(game);
        worldRenderer = new WorldRenderer(worldController);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);

        addGui();
    }

    private Table addGui() {
        Table table = new Table();
        table.setFillParent(true);
        table.top();

        final Button pauseButton = new Button(skin, "pauseButton");
        table.add(pauseButton).padTop(20);
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                paused = true;
                if (pauseCheck) {
                    PauseAdd();
                    pauseCheck = false;
                }
            }
        });

        worldController.addController(stageGui, skin);

        stageGui.addActor(table);
        return table;
    }

    private void PauseAdd() {
        pauseMenu.addPauseMenu();

        pauseMenu.addResumeButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseMenu.background.setVisible(false);
                pauseMenu.PauseTable.setVisible(false);
                paused = false;
                pauseCheck = true;
                Gdx.input.setInputProcessor(stageGui);
            }
        });

        pauseMenu.addtoolsButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toolsMenu.addTools(pauseMenu.stage);
            }
        });

        pauseMenu.addExitButton();
    }


    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
        stageGui.getViewport().update(width, height, true);
        pauseMenu.resize(width, height);
        toolsMenu.resize(width, height);
    }

    @Override
    public void hide() {
        worldRenderer.dispose();
        stageGui.dispose();
        pauseMenu.dispose();
        toolsMenu.dispose();
        skin.dispose();
        Gdx.input.setCatchKey(Input.Keys.BACK, false);
    }


    @Override
    public void pause() {
        Boolean first = false;
        if (first) {
            first = true;
            if (pauseCheck) {
                PauseAdd();
                pauseCheck = false;
            }
            paused = true;
        }
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public InputProcessor getInputProcessor () {
        return worldController;
    }
}