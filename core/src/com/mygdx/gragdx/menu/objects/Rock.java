package com.mygdx.gragdx.menu.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.gragdx.game.Assets;
import com.mygdx.gragdx.game.objects.AbstractGameObject;

public class Rock extends AbstractGameObject {

    private TextureRegion regMiddle;

    private int length;

    public Rock() {
        init();
    }

    private void init() {
        dimension.set(1.005f, 1.5f);

        regMiddle = Assets.instance.rock.middle;

        // Start length of this rock
        setLength(1);
    }

    public void setLength(int length) {
        this.length = length;
        // Update bounding box for collision detection
        bounds.set(0, 0, dimension.x * length, dimension.y);
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion reg = null;

        // Draw middle
        reg = regMiddle;
        batch.draw(reg.getTexture(), position.x , position.y, origin.x, origin.y, dimension.x, dimension.y,
                scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }
}