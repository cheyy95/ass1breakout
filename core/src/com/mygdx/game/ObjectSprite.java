package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class ObjectSprite {

    Sprite sprite;
    Body body;
    float scale;

    public ObjectSprite(){

    }


    public void setScreenPosition(float x, float y, float angle) {
        sprite.setX(x);
        sprite.setY(y);
        body.setTransform(new Vector2((x + sprite.getWidth() / 2) / scale, (y + sprite.getHeight() / 2) / scale), angle);
    }

    public void setWorldPosition(float x, float y, float angle) {
        body.setTransform(x, y, angle);
    }
}
