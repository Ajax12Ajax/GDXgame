package com.mygdx.gragdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.gragdx.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;


public class Assets implements Disposable, AssetErrorListener {

    static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;

    public AssetTest test;
    public AssetRock rock;
    public Assetpoint point;
 // public AssetLevelDecoration levelDecoration;


    // singleton: prevent instantiation from other classes
    private Assets() {
    }

    public class AssetTest {
        public final AtlasRegion head;

        public AssetTest(TextureAtlas atlas) {
            head = atlas.findRegion("LUDZIK TEST");
        }
    }

    public class AssetRock {
        public final AtlasRegion middle;

        public AssetRock(TextureAtlas atlas) {
            middle = atlas.findRegion("rock_middle");
        }
    }

    public class Assetpoint {
        public final AtlasRegion point;

        public Assetpoint(TextureAtlas atlas) {
            point = atlas.findRegion("test kulko");
        }
    }


 //   public class AssetLevelDecoration {
 //       public final AtlasRegion cloud01;
 //       public final AtlasRegion cloud02;
 //       public final AtlasRegion cloud03;
 //       public final AtlasRegion mountainLeft;
 //       public final AtlasRegion mountainRight;
 //       public final AtlasRegion waterOverlay;
 //
 //       public AssetLevelDecoration(TextureAtlas atlas) {
 //           cloud01 = atlas.findRegion("cloud01");
 //           cloud02 = atlas.findRegion("cloud02");
 //           cloud03 = atlas.findRegion("cloud03");
 //           mountainLeft = atlas.findRegion("mountain_left");
 //           mountainRight = atlas.findRegion("mountain_right");
 //           waterOverlay = atlas.findRegion("water_overlay");
 //       }
 //   }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,
                TextureAtlas.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: "
                + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

        // enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }

        // create game resource objects
        test = new AssetTest(atlas);
        rock = new AssetRock(atlas);
        point = new Assetpoint(atlas);
        //levelDecoration = new AssetLevelDecoration(atlas)
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" +
                asset.fileName + "'", (Exception) throwable);
    }
}