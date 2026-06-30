package com.example.newGolf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Hole {
    float x, y;
    float radius;
    float baseSpeed;
    int direction = 1;
    boolean stopped=false;

    public Hole(float x, float y, float radius, float baseSpeed) {
        this.x = x;
        this.y = y;
        this.baseSpeed = baseSpeed;
        this.radius = radius;
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(x, y, radius);
    }

    public boolean contains(float ballX, float ballY){
        float dx = ballX - x;
        float dy = ballY - y;
        float distanceSquared = dx*dx + dy*dy;
        return  distanceSquared <= radius*radius;
    }

    public void update(float delta, float screenWidth, int level) {
        if (level==1){
            return;
        }
        if (stopped){
            return;
        }

        float speed = baseSpeed + (level * 40);
        x += speed * direction * delta;

        if (x <= 0) {
            x = 0;
            direction = 1;
        }
        if (x + radius * 2 >= screenWidth) {
            direction = -1;
        }
    }
}
