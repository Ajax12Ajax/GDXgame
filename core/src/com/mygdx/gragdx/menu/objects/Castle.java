package com.mygdx.gragdx.menu.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.gragdx.game.Assets;
import com.mygdx.gragdx.game.objects.AbstractGameObject;

public class Castle extends AbstractGameObject {
    private TextureRegion regMiddle;

    private int length;

    public Castle() {
        init();
    }

    private void init() {
        dimension.set(6f, 3.5f);

        regMiddle = Assets.instance.castle.castle;

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
        reg = regMiddle;

        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y,
                scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }
}
