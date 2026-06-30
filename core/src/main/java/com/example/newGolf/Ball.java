package com.example.newGolf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ball {
    float x, y;
    float sideSpeed;
    float radius;
    float speed;
    boolean fired = false;
    boolean active = true;

    public Ball(float x, float y, float radius, float speed, float sideSpeed) {
        this.x = x;
        this.y = y;
        this.sideSpeed=sideSpeed;
        this.radius = radius;
        this.speed = speed;
    }

    public void update(float delta) {
        if (!fired || !active) {
            return;
        }
        y+= speed*delta;
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(x, y, radius);
    }

    public void fire(){
        fired = true;
    }

    public void reset(float startX, float startY){
        x=startX;
        y=startY;

        fired=false;
        active=true;
    }
}
