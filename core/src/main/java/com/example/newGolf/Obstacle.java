package com.example.newGolf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Obstacle {
    float x, y;
    float width, height;
    float speed;
    int direction = 1;

    public Obstacle(float x, float y, float width, float height, float speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public void update(float delta, float screenWidth) {
        x += speed * direction * delta;

        if (x <= 0) {
            x = 0;
            direction = 1;
        }
        if (x + width >= screenWidth) {
            x = screenWidth - width;
            direction = -1;
        }
    }

    public void render(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, width, height);
    }

    private float clamp(float value, float min, float max){
        return Math.max(min, Math.min(max, value));
    }

    public boolean collides(float ballX, float ballY, float ballRadius){
        float closestX = clamp(ballX, x, x+width);
        float closestY = clamp(ballY, y, y+height);

        float dx = ballX - closestX;
        float dy = ballY - closestY;

        float distanceSquared = dx*dx + dy*dy;
        return distanceSquared < (ballRadius*ballRadius);
    }
}
