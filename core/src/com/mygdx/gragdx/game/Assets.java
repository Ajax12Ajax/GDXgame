package com.mygdx.gragdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.gragdx.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;


public class Assets implements Disposable, AssetErrorListener {

    static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;


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