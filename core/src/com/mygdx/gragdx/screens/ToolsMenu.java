package com.mygdx.gragdx.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.gragdx.util.Constants;

import java.util.HashSet;
import java.util.Set;

public class ToolsMenu {
    private final Set<Action> animationActions = new HashSet<Action>();
    final Stage stageBackground = new Stage(new FillViewport(684, 516));
    final Stage stage = new Stage(new ExtendViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));

    private Skin skinTools;

    private Table tableTools;

    private Image background;
    private Button backgroundButton;
    private Image backgroundTools;
    private Image vp;
    private Slider slider;
    private Image vm;
    private CheckBox checkBoxM;
    private CheckBox checkBox;


    public void addTools(Stage stageT) {
        Gdx.input.setInputProcessor(stage);

        skinTools = new Skin(
                Gdx.files.internal(Constants.SKIN_TOOLS_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_TOOLS_UI));

        // + Background
        addBackground();

        // + Background Button
        addBackgroundButton(stageT);

        // + Background Tools
        addBackgroundTools();

        // + Tools
        addToolsTable();
    }


    private Image addBackground() {
        // + Background
        background = new Image(skinTools, "background");
        background.setVisible(true);

        registerAction(background, Actions.sequence(
                Actions.visible(false),
                Actions.alpha(0, 0.17f),
                Actions.visible(true),
                Actions.alpha(1, 0.17f, Interpolation.exp5Out)));

        stageBackground.addActor(background);
        return background;
    }

    private Table addBackgroundButton (final Stage stageT) {
        final Table table = new Table();
        table.setFillParent(true);
        table.top();
        table.left();

        // + Background Button
        backgroundButton = new Button(skinTools, "background");
        backgroundButton.setScale(55);
        backgroundButton.setRotation(-90);
        backgroundButton.setColor(1, 1, 1, 0);
        backgroundButton.setVisible(true);

        backgroundButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                background.setVisible(false);
                backgroundButton.setVisible(false);
                backgroundTools.setVisible(false);
                tableTools.setVisible(false);
                Gdx.input.setInputProcessor(stageT);
            }
        });
        table.add(backgroundButton).padTop(-445);

        // Add table
        stage.addActor(table);

        return table;
    }

    private Table addBackgroundTools () {
        Table table = new Table();
        table.setFillParent(true);
        table.right();

        // + Background Tools
        backgroundTools = new Image(skinTools, "backgroundTools");
        backgroundTools.setVisible(true);
        table.add(backgroundTools);

        registerAction(backgroundTools, Actions.sequence(
                Actions.visible(false),
                Actions.moveBy(1000, 0, 0.15f),
                Actions.visible(true),
                Actions.moveBy(-1000, 0, 0.15f, Interpolation.exp5Out)));

        stage.addActor(table);

        return table;
    }

    private Table addToolsTable () {
        tableTools = new Table();
        tableTools.setFillParent(true);
        tableTools.setVisible(true);

        tableTools.right();

        vp = new Image(skinTools, "vp");
        vp.setScale(0.65f);
        tableTools.add(vp).padRight(-8.5f).padBottom(16).row();

        slider = new Slider(0.0f, 1.0f, 0.1f, true, skinTools);
        slider.getStyle().background.setMinWidth(9.5f);
        slider.getStyle().knob.setMinWidth(24);
        slider.getStyle().knob.setMinHeight(12);
        tableTools.add(slider).padRight(34).row();

        vm = new Image(skinTools, "vm");
        vm.setScale(0.65f);
        tableTools.add(vm).padRight(-8.5f).padTop(-10).row();

        checkBoxM = new CheckBox("", skinTools, "muteM");
        setSizeCheckBox(checkBoxM);
        tableTools.add(checkBoxM).padRight(35.5f).padTop(20).row();

        checkBox = new CheckBox("", skinTools, "mute");
        setSizeCheckBox(checkBox);
        tableTools.add(checkBox).padRight(35.5f).padBottom(20).padTop(20);

        registerAction(tableTools, Actions.sequence(
                Actions.visible(false),
                Actions.moveBy(1000, 0, 0.15f),
                Actions.visible(true),
                Actions.moveBy(-1000, 0, 0.15f, Interpolation.exp5Out)));

        stage.addActor(tableTools);

        return tableTools;
    }
    private void registerAction(Actor actor, Action action) {
        animationActions.add(action);
        actor.addAction(action);
    }
    private void setSizeCheckBox(CheckBox checkBox) {
        int height = 60;
        int width = 50;
        checkBox.getStyle().checkboxOn.setMinHeight(height);
        checkBox.getStyle().checkboxOff.setMinHeight(height);
        checkBox.getStyle().checkboxOn.setMinWidth(width);
        checkBox.getStyle().checkboxOff.setMinWidth(width);
    }


    public void resize(int width, int height){
        stageBackground.getViewport().update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    public void dispose(){
        stageBackground.dispose();
        stage.dispose();
        skinTools.dispose();
    }

    public void draw(float deltaTime){
        stageBackground.getViewport().apply();
        stageBackground.act(deltaTime);
        stageBackground.draw();
        stage.getViewport().apply();
        stage.act(deltaTime);
        stage.draw();
    }
}
