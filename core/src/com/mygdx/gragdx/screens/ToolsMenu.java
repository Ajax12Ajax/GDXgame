package com.mygdx.gragdx.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.gragdx.util.Constants;

public class ToolsMenu {

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


    public void addTools(Stage stage, Stage stageBackground, Stage stageT) {
        skinTools = new Skin(
                Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

        // + Background
        addBackground(stageBackground);

        // + Background Button
        addBackgroundButton(stage, stageT);

        // + Background Tools
        addBackgroundTools(stage);

        // + Tools
        addToolsTable(stage);
    }


    private Image addBackground(Stage stage) {
        // + Background
        background = new Image(skinTools, "background");
        background.setVisible(true);

        stage.addActor(background);

        return background;
    }

    private Table addBackgroundButton (Stage stage, final Stage stageT) {
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

    private Table addBackgroundTools (Stage stage) {
        Table table = new Table();
        table.setFillParent(true);
        table.right();

        // + Background Tools
        backgroundTools = new Image(skinTools, "backgroundTools");
        backgroundTools.setVisible(true);
        table.add(backgroundTools);

        stage.addActor(table);

        return table;
    }

    private Table addToolsTable (Stage stage) {
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


        stage.addActor(tableTools);

        return tableTools;
    }
    private void setSizeCheckBox(CheckBox checkBox) {
        int height = 60;
        int width = 50;
        checkBox.getStyle().checkboxOn.setMinHeight(height);
        checkBox.getStyle().checkboxOff.setMinHeight(height);
        checkBox.getStyle().checkboxOn.setMinWidth(width);
        checkBox.getStyle().checkboxOff.setMinWidth(width);
    }
}
