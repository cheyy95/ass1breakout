package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

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
        sprite.setPosition((body.getPosition().x * scale) - sprite.getWidth() / 2, (body.getPosition().y * scale) - sprite.getHeight() / 2);
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
    }
    public void draw(SpriteBatch batch) {
        batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
    }

    /*------------*/
    public static class Defs{
        Texture tex;
        BodyDef bodyDef;
        FixtureDef fixtureDef;
        float x;
        float y;
        float scale;

        private Defs(){
            Defs defs = new Defs();
            defs.tex = tex;
            defs.x = x;
            defs.y = y;
            defs.scale = scale;

            defs.bodyDef = new BodyDef();
            defs.bodyDef.position.set(
                    (x + tex.getWidth() / 2) / scale,
                    (y + tex.getHeight() / 2) / scale);

            defs.fixtureDef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                    tex.getWidth() / 2 / scale,
                    tex.getHeight() / 2 / scale);
            defs.fixtureDef.shape = shape;

            Defs defs = new Defs();
            defs.tex = tex;
            defs.x = x * scale;
            defs.y = y * scale;
            defs.scale = scale;

            defs.bodyDef = new BodyDef();
            defs.bodyDef.position.set(x, y);

            defs.fixtureDef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                    tex.getWidth() / 2 / scale,
                    tex.getHeight() / 2 / scale);
            defs.fixtureDef.shape = shape;
        }

    }
}
