package com.mygdx.gragdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.gragdx.util.Constants;


public class Assets implements Disposable, AssetErrorListener {
    static final String TAG = Assets.class.getName();

    public static final Assets instance = new Assets();

    private AssetManager assetManager;

    public AssetFonts fonts;
    public AssetTest test;
    public AssetRock rock;
    public Assetpoint point;
    public AssetLevelDecoration levelDecoration;

    // singleton: prevent instantiation from other classes
    private Assets() {
    }


    public class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts () {
            // create three fonts using Libgdx's 15px bitmap font
            defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            // set font sizes
            defaultSmall.getData().setScale(.75f,.75f);
            defaultNormal.getData().setScale(1f,1f);
            defaultBig.getData().setScale(2f,2f);
            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }


    public class AssetTest {
        public final AtlasRegion head;

        public AssetTest(TextureAtlas atlas) { head = atlas.findRegion("LUDZIK TEST");
        }
    }

    public class AssetRock {
        public final AtlasRegion middle;
        public final AtlasRegion edge;

        public AssetRock(TextureAtlas atlas) {
            middle = atlas.findRegion("rock");
            edge = atlas.findRegion("rock");

        }
    }

    public class Assetpoint {
        public final AtlasRegion pointa;

        public Assetpoint(TextureAtlas atlas) {
            pointa = atlas.findRegion("test kulko");
        }
    }

       public class AssetLevelDecoration {
           public final AtlasRegion cloud01;
           public final AtlasRegion cloud02;
           public final AtlasRegion cloud03;
           public final AtlasRegion mountainLeft;
           public final AtlasRegion mountainRight;
           public final AtlasRegion waterOverlay;

           public AssetLevelDecoration(TextureAtlas atlas) {
               cloud01 = atlas.findRegion("chmura2");
               cloud02 = atlas.findRegion("chmury1");
               cloud03 = atlas.findRegion("chmura2");
               mountainLeft = atlas.findRegion("gury1");
               mountainRight = atlas.findRegion("gury2");
               waterOverlay = atlas.findRegion("woda");
           }
       }



    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "asset: " + a);
        }

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

        // enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        }

        // create game resource objects
        fonts = new AssetFonts();
        test = new AssetTest(atlas);
        rock = new AssetRock(atlas);
        point = new Assetpoint(atlas);
        levelDecoration = new AssetLevelDecoration(atlas);
    }



    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception) throwable);
    }
}