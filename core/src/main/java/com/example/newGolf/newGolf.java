package com.example.newGolf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public class newGolf extends ApplicationAdapter {
    SpriteBatch batch;
    BitmapFont font;
    Texture grass;
    Ball ball;
    Hole hole;
    ArrayList<Obstacle> obstacles;
    ShapeRenderer shapeRenderer;
    Music music;
    Sound sound;
    Sound winSound;
    String MatNo;

    float screenWidth;
    float screenHeight;

    boolean gameOver = false;
    boolean win = false;

    int wins = 0;
    int losses = 0;
    int level = 1;
    int winsThisLevel = 0;

    float restartTimer = 0;
    float restartDelay = 1.2f;

    private void resetGame(){
        ball.reset(screenWidth/2f,50);
        gameOver=false;
        win=false;
        restartTimer = 0;
        hole.stopped=false;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        grass = new Texture("newGrass.png");
        shapeRenderer = new ShapeRenderer();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        ball = new Ball(screenWidth / 2f, 30, 10, 380, 160);

        hole = new Hole(screenWidth / 2f, screenHeight - 15, 14, 80);

        obstacles = new ArrayList<>();
        for (int i = 0; i<4; i++){
            float x = (float) Math.random()*(screenWidth-80);
            float y = 110 + i * 100;

            obstacles.add(new Obstacle(x, y, 75, 20, 250f));
        }

        MatNo = "VUG/SEN/24/12519";

        music=Gdx.audio.newMusic(Gdx.files.internal("longMusic.mp3"));
        music.setLooping(true);
        music.setVolume(0.4f);
        music.play();

        sound=Gdx.audio.newSound(Gdx.files.internal("collision.wav"));
        winSound=Gdx.audio.newSound(Gdx.files.internal("winSound.wav"));
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(1,1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);

        batch.begin();

        batch.draw(grass,0,0);

        font.setColor(Color.BLACK);
        font.draw(batch, "WINS: " + wins, 10, screenHeight-20);
        font.draw(batch, "LOSSES: " + losses, 10, screenHeight-50);
        font.draw(batch,"LEVEL: " + level, 10, screenHeight-80);
        font.draw(batch,"MATRIC NUMBER: " + MatNo, 10, screenHeight-450);

        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        hole.render(shapeRenderer);
        ball.render(shapeRenderer);
        for (Obstacle o : obstacles) {
            o.render(shapeRenderer);
        }
        shapeRenderer.end();
    }

    private void update(float delta){
        hole.update(delta, screenWidth, level);

        if (gameOver || win){
            restartTimer+= delta;

            if (restartTimer>=restartDelay){
                resetGame();
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            ball.fire();
        }
        ball.speed = 380 + (level * 40);
        ball.sideSpeed = 160 + (level * 20);
        ball.update(delta);

        for (Obstacle o : obstacles){
            o.update(delta, screenWidth);

            if (!gameOver && !win && o.collides(ball.x, ball.y, ball.radius)){
                gameOver=true;
                losses++;
                sound.play();
            }
        }
        if (ball.y > screenHeight && !win){
            gameOver = true;
            losses++;
            sound.play();
        }

        if (!win && hole.contains(ball.x, ball.y)){
            win = true;
            wins++;
            winsThisLevel++;
            hole.stopped=true;
            winSound.play();

            if (winsThisLevel>= 3){
                level++;
                winsThisLevel=0;
            }
        }

        if (!ball.fired && !gameOver && !win) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                ball.x -= ball.sideSpeed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                ball.x += ball.sideSpeed * delta;
            }

            if (ball.x - ball.radius < 0) {
                ball.x = ball.radius;
            }
            if (ball.x + ball.radius > screenWidth) {
                ball.x = screenWidth - ball.radius;
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        grass.dispose();
        font.dispose();
        shapeRenderer.dispose();
        music.dispose();
        sound.dispose();
    }
}
