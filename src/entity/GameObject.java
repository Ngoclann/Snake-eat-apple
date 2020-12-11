/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Ngọc Lan
 */
public class GameObject {
    private int x;
    private int y;
    private int size;

    public GameObject() {
    }

    public GameObject(int size) {
        this.size = size;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public Rectangle getBound() {
        return new Rectangle(x, y, size, size);
    }
    
    public void render(Graphics g) {
        g.fillRect(x, y, size, size);
    }

    public boolean isCollision(GameObject obj) {
        if (obj == this) {
            return false;
        }
        return getBound().intersects(obj.getBound());
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    
}
