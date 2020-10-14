package com.mygdx.gragdx.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.gragdx.util.Constants;
import com.mygdx.gragdx.util.GamePreferences;

public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();

    private Stage stage;
    private Skin skinCanyonBunny;
    private Skin skinLibgdx;

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

        skinLibgdx = new Skin
                (Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
                        new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

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
        loadSettings();
        btnMenuPlay.setVisible(false);
        btnMenuOptions.setVisible(false);
        winOptions.setVisible(true);
    }
    //clicking the Exit button
    private void onExitClicked() {
        Gdx.app.exit();
    }


    private Table buildOptionsWindowLayer() {
        winOptions = new Window("Options", skinLibgdx);
        // + Audio Settings: Sound/Music CheckBox and Volume Slider
        winOptions.add(buildOptWinAudioSettings()).row();
        // + Debug: Show FPS Counter
        winOptions.add(buildOptWinDebug()).row();
        // + Separator and Buttons (Save, Cancel)
        winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);
        // Make options window slightly transparent
        winOptions.setColor(1, 1, 1, 0.8f);
        // Hide options window by default
        winOptions.setVisible(false);
        if (debugEnabled) winOptions.debug();
        // Let TableLayout recalculate widget sizes and positions
        winOptions.pack();
        winOptions.setSize(120, 470);
        // Move options window to bottom right corner
        winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 5, 5);
        return winOptions;
    }



    private void loadSettings() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.load();
        chkSound.setChecked(prefs.sound);
        sldSound.setValue(prefs.volSound);
        chkMusic.setChecked(prefs.music);
        sldMusic.setValue(prefs.volMusic);
        chkShowFpsCounter.setChecked(prefs.showFpsCounter);
    }
    private void saveSettings() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.sound = chkSound.isChecked();
        prefs.volSound = sldSound.getValue();
        prefs.music = chkMusic.isChecked();
        prefs.volMusic = sldMusic.getValue();
        prefs.showFpsCounter = chkShowFpsCounter.isChecked();
        prefs.save();
    }
    private void onSaveClicked() {
        saveSettings();
        onCancelClicked();
    }
    private void onCancelClicked() {
        btnMenuPlay.setVisible(true);
        btnMenuOptions.setVisible(true);
        winOptions.setVisible(false);
    }


    private Table buildOptWinAudioSettings () {
        Table tbl = new Table();
        // + Title: "Audio"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Audio", skinLibgdx, "default-font",
                Color.ORANGE)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // + Checkbox, "Sound" label, sound volume slider
        chkSound = new CheckBox("", skinLibgdx);
        tbl.add(chkSound);
        tbl.add(new Label("Sound", skinLibgdx));
        sldSound = new Slider(0.0f, 1.0f, 0.1f, true, skinLibgdx);
        tbl.add(sldSound);
        tbl.row();
        // + Checkbox, "Music" label, music volume slider
        chkMusic = new CheckBox("", skinLibgdx);
        tbl.add(chkMusic);
        tbl.add(new Label("Music", skinLibgdx));
        sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldMusic);
        tbl.row();
        return tbl;
    }
    private Table buildOptWinDebug () {
        Table tbl = new Table();
        // + Title: "Debug"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Debug", skinLibgdx, "default-font", Color.RED)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // + Checkbox, "Show FPS Counter" label
        chkShowFpsCounter = new CheckBox("", skinLibgdx);
        tbl.add(new Label("Show FPS Counter", skinLibgdx));
        tbl.add(chkShowFpsCounter);
        tbl.row();
        return tbl;
    }

    private Table buildOptWinButtons () {
        Table tbl = new Table();
        // + Separator
        Label lbl = null;
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.75f, 0.75f, 0.75f, 1);
        lbl.setStyle(new Label.LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
        tbl.row();
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.5f, 0.5f, 0.5f, 1);
        lbl.setStyle(new Label.LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
        tbl.row();
        // + Save Button with event handler
        btnWinOptSave = new TextButton("Save", skinLibgdx);
        tbl.add(btnWinOptSave).padRight(30);
        btnWinOptSave.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onSaveClicked();
            }
        });
        // + Cancel Button with event handler
        btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
        tbl.add(btnWinOptCancel);
        btnWinOptCancel.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onCancelClicked();
            }
        });
        return tbl;
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