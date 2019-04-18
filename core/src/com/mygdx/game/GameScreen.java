package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameScreen implements Screen {
    MyGdxGame game; // Note it’s "MyGdxGame" not "Game"
    // constructor to keep a reference to the main Game class
    World world;
    Texture padTex;
    Texture ballTex;
    Texture brickTex;
    Sound hitSound;
    Texture texture;
    Fixture ballhall;
    Stage stage;
    Array<Body> hall;
    ObjectSprite paddle;
    ObjectSprite ball;
    Array<ObjectSprite> bricks;
    Array<ObjectSprite> remove;


    int screenWidth;
    int screenHeight;
    static final float WORLD_SCALE = 100f;
    static final float BALLSPEED = 0.5f;

    // collision
    static final short WORLD_BOUND = 1;
    static final short PADDLE = 1 << 1;
    static final short BRICK = 2;
    static final short BALL = 1;
    static final short bottomfail = 3;

    private int score = 0;
    private int add;
    private int lives = 0;

    private boolean mouse = false;
    private boolean placeball = false;
    private boolean ballStarted = false;

    GameScreen(MyGdxGame game) {
        this(game, 0, 1, 0);
    }

    GameScreen(MyGdxGame game, int score, int add, int lives) {
        this.game = game;
        this.game = game;
        this.score = score;
        this.add = add;
        this.lives = lives;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        stage = new Stage(new StretchViewport(MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT));
        padTex = new Texture("paddle.png");
        ballTex = new Texture("ball.png");
        brickTex = new Texture("blue.png");
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));
        texture = new Texture(Gdx.files.internal("背景图.png"));

        // create physics world
        world = new World(new Vector2(0, 0), true);

        bricks = new Array<ObjectSprite>();
        hall = new Array<Body>();
        remove = new Array<ObjectSprite>();

        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture fixa = contact.getFixtureA();
                Fixture fixb = contact.getFixtureB();

                for (ObjectSprite brick : bricks) {
                    if ((fixa == brick.fixture && fixb == ball.fixture)
                            || (fixa == ball.fixture && fixb == brick.fixture)) {
                        remove.add(brick);
                    }
                }

                if ((fixa == ballhall && fixb == ball.fixture)
                        || (fixa == ball.fixture && fixb == ballhall)) {
                    placeball = true;
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });

        createEdges();
        createPaddle();
        createBall();
        createBricks();
    }

    public void create() {


    }

    public void render(float delta) {

        if (placeball) {
            ball.setScreenPosition((Gdx.graphics.getWidth() / 2) - (ball.sprite.getWidth() / 2), 100, 0);
            ball.body.setAngularVelocity(0);
            ball.body.setLinearVelocity(0, 0);
            ball.body.setAwake(false);
            placeball = false;
            ballStarted = false;

            //trigger game over
            if (lives-- == 0) {
                game.setScreen(new OverScreen(game));
                dispose();
                return;
            }

            if(remove.size == 0){
                game.setScreen(new SuccessState(game));
                dispose();
                return;
            }
        }
         // control
        if (Gdx.input.isTouched()) {
            mouse = true;
        }if (Gdx.input.getDeltaX() != 0) {
            mouse = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            game.setScreen(new PausedScreen(game, this));
        }if (mouse) {
            float mousex = Gdx.input.getX() / WORLD_SCALE;
            float padx = paddle.body.getPosition().x;
            paddle.body.applyForceToCenter(mousex - padx, 0f, true);
        }

        // click to shot the ball
        if (!ballStarted && Gdx.input.justTouched()){
            ball.body.applyForceToCenter(

                    (float) (((Math.random() * 2) - 1) / 10),
                    (float) (((Math.random() * 2) - 1) / 10),
                    true);
            ball.body.applyAngularImpulse(

                    (float) (((Math.random() * 2) - 1) / 1000),

                    true);
            ballStarted = true;
        }// PHYSICS

        world.step(delta, 6, 2);
        // remove bri and add hit sound
        while (remove.size != 0) {
            ObjectSprite dead = remove.first();
            remove.removeIndex(0);
            world.destroyBody(dead.body);
            if (bricks.contains(dead, true)) {
                bricks.removeValue(dead, true);
                score += add;
            }
            hit();
        }

        //ensure the ball has a minimum velocity
        if (ballStarted) {
            Vector2 ballVel = ball.body.getLinearVelocity();
            if (Math.abs(ballVel.x) < BALLSPEED) {
                if (ballVel.x == 0f) {
                    ball.body.applyForceToCenter(BALLSPEED, 0, true);
                } else {
                    ball.body.applyForceToCenter(
                            BALLSPEED * Math.signum(ballVel.x), 0, true);
                }
            }
            if (Math.abs(ballVel.y) < BALLSPEED) {
                if (ballVel.y == 0f) {
                    ball.body.applyForceToCenter(
                            0, BALLSPEED, true);
                } else {
                    ball.body.applyForceToCenter(
                            0, BALLSPEED * Math.signum(ballVel.y), true);
                }
            }
            if (ball.body.getAngularVelocity() == 0.0f) {
                ball.body.applyAngularImpulse(
                        (float) (((Math.random() * 2) - 1) / 1000),

                        true);
            }
        }


        paddle.update();
        ball.update();
        for (ObjectSprite brick : bricks) {
            brick.update();
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(texture,0,0);
        paddle.draw(game.batch);
        ball.draw(game.batch);
        for (ObjectSprite brick : bricks) {
            brick.draw(game.batch);
        }
        drawScore();
        game.batch.end();
        stage.act();
        stage.draw();

    }
    public void hit() {
        hitSound.play();
    }
    @Override
    public void dispose() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen: ", " show called");
        create();
    }

    @Override
    public void hide() {
    }

    private void createBall() {
        float x = (screenWidth / 2) - 5;
        float y = 45;

        ObjectSprite.Defs defs = ObjectSprite.Defs.fromScreenCoordinates(
                ballTex, x, y, WORLD_SCALE);
        defs.bodyDef.type = BodyDef.BodyType.DynamicBody;

        defs.fixtureDef.friction = 0;
        defs.fixtureDef.density = 0.1f;
        defs.fixtureDef.restitution = 1f;
        defs.fixtureDef.filter.categoryBits = BALL;
        defs.fixtureDef.filter.maskBits = (PADDLE | BRICK | WORLD_BOUND | bottomfail);

        ball = new ObjectSprite(defs, world);
    }

    private void createPaddle() {
        float x = (screenWidth / 2) - (padTex.getWidth() / 2);
        float y = 32;
        ObjectSprite.Defs defs = ObjectSprite.Defs.fromScreenCoordinates(
                padTex, x, y, WORLD_SCALE);

        defs.bodyDef.type = BodyDef.BodyType.DynamicBody;
        defs.bodyDef.fixedRotation = true;

        defs.fixtureDef.density = 0.1f;
        defs.fixtureDef.filter.categoryBits = PADDLE;
        defs.fixtureDef.filter.maskBits = (BALL | WORLD_BOUND);

        paddle = new ObjectSprite(defs, world);
        paddle.body.setLinearDamping(2.0f);
    }


    private void createBricks() {

        int brickWidth = brickTex.getWidth();
        int brickHeight = brickTex.getHeight();
        int x = 20;
        int y = 4;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                int col = i * brickWidth;
                int row = screenHeight - brickHeight - (j * brickHeight);


                ObjectSprite.Defs defs = ObjectSprite.Defs.fromScreenCoordinates(
                        brickTex, col, row, WORLD_SCALE);

                defs.bodyDef.type = BodyDef.BodyType.DynamicBody;

                defs.fixtureDef.density = 0.1f;
                defs.fixtureDef.filter.categoryBits = BRICK;
                defs.fixtureDef.filter.maskBits = (BALL | WORLD_BOUND);

                ObjectSprite brick = new ObjectSprite(defs, world);

                bricks.add(brick);
            }

        }


    }

    private void createEdges() {
        float w = Gdx.graphics.getWidth() / WORLD_SCALE;
        float h = Gdx.graphics.getHeight() / WORLD_SCALE;
        for (Vector2[] points : new Vector2[][]{
                {new Vector2(0, 0), new Vector2(0, h)}, // left
                {new Vector2(w, 0), new Vector2(w, h)}, // right
                {new Vector2(0, 0), new Vector2(w, 0)}, // top
                {new Vector2(0, h), new Vector2(w, h)}} // bottom
        ) {
            Vector2 p1, p2;
            p1 = points[0];
            p2 = points[1];


            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(0f, 0f);

            EdgeShape shape = new EdgeShape();
            shape.set(p1, p2);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = WORLD_BOUND;

            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef);

            hall.add(body);
            shape.dispose();


        }

        //create bottomfail
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0f, 0f);

        EdgeShape shape = new EdgeShape();
        shape.set(0, 1 / WORLD_SCALE, w, 1 / WORLD_SCALE);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = bottomfail;

        Body body = world.createBody(bodyDef);
        ballhall = body.createFixture(fixtureDef);

        hall.add(body);
        shape.dispose();
    }

    private void drawScore() {
        game.font.setColor(1, 0, 0, 1);
        game.font.draw(game.batch, String.format("Score: %d", score), 0, 20);
    }
}