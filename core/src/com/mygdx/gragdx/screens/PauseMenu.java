package com.mygdx.gragdx.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.gragdx.util.Constants;

public class PauseMenu {
    final Stage stageBackground = new Stage(new FillViewport(684, 516));
    final Stage stage = new Stage(new ExtendViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));

    private Skin skinPause;

    public Table PauseTable;

    public Image background;
    private Button resumeButton;
    private Button toolsButton;
    private Button exitButton;


    public void addPauseMenu() {
        Gdx.input.setInputProcessor(stage);

        skinPause = new Skin(
                Gdx.files.internal(Constants.SKIN_MENU_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_UI));

        // + Background
        addBackground();

        // + Tools
        addPauseTable();


    }


    private Image addBackground() {
        // + Background
        background = new Image(skinPause, "background");
        background.setVisible(true);

        stageBackground.addActor(background);
        return background;
    }


    private Table addPauseTable () {
        PauseTable = new Table();
        PauseTable.setFillParent(true);
        PauseTable.setVisible(true);

        PauseTable.center();

        stage.addActor(PauseTable);

        return PauseTable;
    }

    public Button addResumeButton() {
        resumeButton = new Button(skinPause, "resume");
        PauseTable.add(resumeButton).padLeft(7).padBottom(16).row();

        return resumeButton;
    }
    public Button addtoolsButton() {
        toolsButton = new Button(skinPause, "options");
        PauseTable.add(toolsButton).row();

        return toolsButton;
    }
    public Button addExitButton() {
        exitButton = new Button(skinPause, "exit");
        PauseTable.add(exitButton).padTop(16).row();
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        return toolsButton;
    }



    public void resize(int width, int height){
        stageBackground.getViewport().update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    public void dispose(){
        stageBackground.dispose();
        stage.dispose();
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
