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
import com.mygdx.gragdx.util.Constants;

public class ToolsMenu {

    private Skin skinTools;
    private Image background;
    private Button test;

    public Table addTest(Stage stage, Stage stageBackground) {
        skinTools = new Skin(
                Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

        Table tableTest = new Table();
        tableTest.setFillParent(true);

        tableTest.center();

        // + Background
        background = new Image(skinTools, "tlomenu");
        background.setVisible(true);

        // + Background Button
        test = new Button(skinTools, "tlomenu");
        test.setScale(5);
        test.setColor(1, 1, 1, 0);
        test.setVisible(true);
        test.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                background.setVisible(false);
                test.setVisible(false);
            }
        });
        tableTest.add(test).padRight(600).padTop(300);


        // Add table
        stage.addActor(tableTest);
        stageBackground.addActor(background);

        return tableTest;
    }
}
