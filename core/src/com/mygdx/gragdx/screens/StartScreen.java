package com.mygdx.gragdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.gragdx.screens.transitins.ScreenTransition;
import com.mygdx.gragdx.screens.transitins.ScreenTransitionFade;
import com.mygdx.gragdx.util.Constants;

import java.util.HashSet;
import java.util.Set;

public class StartScreen extends AbstractGameScreen {
    private final Stage backgroundUi = new Stage(new FillViewport(684, 516));
    private final Stage textUi = new Stage(new ExtendViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));

    private final Set<Action> animationActions = new HashSet<Action>();

    private Skin skin;
    private Image titleImage;

    private ToolsMenu toolsMenu;

    public StartScreen(DirectedGame game) {
        super(game);
    }


    private void setupUi() {
        toolsMenu = new ToolsMenu();

        skin = new Skin(
                Gdx.files.internal(Constants.SKIN_MENU_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_UI));

        setupBackground();
        setupTitle(0);
        setupButtons(0);
    }


    private void setupBackground() {
        Image image = new Image(skin, "backgroundStart");
        backgroundUi.addActor(image);
    }


    private void setupTitle(float initialDelay) {
        Table titleTable = new Table();
        titleTable.setFillParent(true);

        titleTable.left();

        titleImage = new Image(skin, "hello");
        titleImage.setScale(1.25f);
        titleTable.add(titleImage).padTop(45).padLeft(140);
        textUi.addActor(titleTable);

        registerAction(titleTable, Actions.sequence(
                Actions.moveBy(-2000, 0, 0),
                Actions.delay(initialDelay), Actions.moveBy(2000, 0, 0.75f, Interpolation.exp5Out)));

    }


    private void setupButtons(float initialDelay) {
        Table menuTable = new Table();
        menuTable.setFillParent(true);

        Table menuSubTable = new Table();

        // + Play Button
        addButton(menuSubTable, skin, "play", initialDelay + 0.15f * 1).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenTransition transition = ScreenTransitionFade.init(0.15f);
                game.setScreen(new MenuScreen(game), transition);
            }
        });
        // + Options Button
        addButton(menuSubTable, skin, "options", initialDelay + 0.15f * 2).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setupToolsMenu();
            }
        });
        // + Exit Button
        addButton(menuSubTable, skin, "exit", initialDelay + 0.15f * 3).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });


        menuTable.right();
        menuTable.add(menuSubTable);
        textUi.addActor(menuTable);

    }

    private Button addButton(Table table, Skin typee, String name, float initialDelay) {
        Table button = new Button(typee, name);
        table.add(button).padRight(95).padTop(20).row();

        registerAction(button, Actions.sequence(
                Actions.visible(false),
                Actions.moveBy(1000, 0, 0.25f),
                Actions.delay(initialDelay),
                Actions.visible(true),
                Actions.moveBy(-1000, 0, 0.25f, Interpolation.exp5Out)));

        return (Button) button;
    }

    private void setupToolsMenu() {
        toolsMenu.addTools(textUi);
    }


    private void registerAction(Actor actor, Action action) {
        animationActions.add(action);
        actor.addAction(action);
    }



    @Override
    public void show() {
        setupUi();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.706f, 0.851f, 0.847f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isButtonJustPressed(0)) {
            for (Action action : animationActions) {
                //noinspection StatementWithEmptyBody
                while (!action.act(100))
                    ; // finish all animations, needs multiple calls since a sequence only steps the current action
            }
        }

        drawStage(backgroundUi, deltaTime);
        drawStage(textUi, deltaTime);
        toolsMenu.draw(deltaTime);
    }
    private void drawStage(Stage stage, Float deltaTime) {
        stage.getViewport().apply();
        stage.act(deltaTime);
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        backgroundUi.getViewport().update(width, height, true);
        textUi.getViewport().update(width, height, true);
        toolsMenu.resize(width, height);
    }

    @Override
    public void hide() {
        Dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void dispose() {
        Dispose();
    }

    private void Dispose() {
        textUi.dispose();
        backgroundUi.dispose();
        skin.dispose();
        toolsMenu.dispose();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return textUi;
    }
}