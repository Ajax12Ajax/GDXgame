package com.mygdx.gragdx.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.gragdx.util.Constants;

public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();

    private Stage stage;
    private Skin skinCanyonBunny;

    // menu
    private Image imgBackground;
    private Image imgLogo;
    private Image imgBreak;
    private Image imgBreakR90;
    private Button btnMenuPlay;
    private Button btnMenuOptions;
    private Button btnMenuExit;

    // options
    private Window winOptions;
    private TextButton btnWinOptSave;
    private TextButton btnWinOptCancel;
    private CheckBox chkSound;
    private Slider sldSound;
    private CheckBox chkMusic;
    private Slider sldMusic;
    private Image imgCharSkin;
    private CheckBox chkShowFpsCounter;

    // debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private boolean debugEnabled = false;
    private float debugRebuildStage;


    public MenuScreen(Game game) {
        super(game);
    }


    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (debugEnabled) {
            debugRebuildStage -= deltaTime;
            if (debugRebuildStage <= 0) {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuildStage();
            }
        }
        stage.act(deltaTime);
        stage.draw();
    }


    private void rebuildStage() {
        skinCanyonBunny = new Skin(
                Gdx.files.internal(Constants.SKIN_STARTMENU_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_UI));

        // build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerLogos = buildLogosLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();

        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerLogos);
        stack.add(layerControls);
        stage.addActor(layerOptionsWindow);
    }



    private Table buildBackgroundLayer() {
        Table layer = new Table();
        // + Background
        imgBackground = new Image(skinCanyonBunny, "backgroundStart");
        layer.add(imgBackground);
        return layer;
    }

    private Table buildLogosLayer() {
        Table layer = new Table();
        // + Game Logo
        imgLogo = new Image(skinCanyonBunny, "hello");
        layer.addActor(imgLogo);
        imgLogo.setPosition(85, 205);
        return layer;
    }

    private Table buildControlsLayer() {
        Table layer = new Table();
        layer.right();

        // + Play Button
        btnMenuPlay = new Button(skinCanyonBunny, "play");
        layer.add(btnMenuPlay);
        btnMenuPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onPlayClicked();
            }
        });
        layer.row();

        // + Break
        imgBreak = new Image(skinCanyonBunny, "przerwa20");
        layer.add(imgBreak);
        imgBreak.setColor(0, 0, 0, 0);
        layer.row();

        // + Options Button
        btnMenuOptions = new Button(skinCanyonBunny, "options");
        layer.add(btnMenuOptions);
        btnMenuOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onOptionsClicked();
            }
        });

        // + Break Rotation 90
        imgBreakR90 = new Image(skinCanyonBunny, "przerwa20R90");
        layer.add(imgBreakR90);
        imgBreakR90.setColor(0, 0, 0, 0);
        layer.row();
        // + Break
        imgBreak = new Image(skinCanyonBunny, "przerwa20");
        layer.add(imgBreak);
        imgBreak.setColor(0, 0, 0, 0);
        layer.row();

        // + Exit Button
        btnMenuExit = new Button(skinCanyonBunny, "exit");
        layer.add(btnMenuExit);
        btnMenuExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onExitClicked();
            }
        });

        if (debugEnabled) layer.debug();
        return layer;
    }



    //clicking the Play button
    private void onPlayClicked() {
        game.setScreen(new GameScreen(game));
    }
    //clicking the Options button
    private void onOptionsClicked() {
    }
    //clicking the Exit button
    private void onExitClicked() {
        Gdx.app.exit();
    }


    private Table buildOptionsWindowLayer() {
        Table layer = new Table();
        return layer;
    }



    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
    }

    @Override
    public void hide() {
        stage.dispose();
        skinCanyonBunny.dispose();
    }

    @Override
    public void pause() {
    }
}