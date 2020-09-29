package com.mygdx.gragdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.gragdx.game.Assets;

public class Point extends AbstractGameObject {
    private TextureRegion regPoint;
    public boolean collected;

    public Point() {
        init();
    }

    private void init() {
        dimension.set(0.5f, 0.5f);
        regPoint = Assets.instance.point.pointa;
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;
    }

    public void render(SpriteBatch batch) {
        if (collected) return;
        TextureRegion reg = null;
        reg = regPoint;
        batch.draw(reg.getTexture(), position.x, position.y,
                origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                rotation, reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }

    public int getScore() {
        return 100;
    }
}
