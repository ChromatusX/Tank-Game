package TankG.GameObj;

import java.awt.*;
import java.awt.image.BufferedImage;


import javax.swing.JComponent;

public abstract class TankObjects extends JComponent {
    public int x;
    public int y;
    protected int width;
    protected int height;
    protected BufferedImage img;
    protected int speed;

    protected Rectangle box;


    public TankObjects(){   //constructor

    }
/*
    public TankObjects(int x, int y , BufferedImage pic , ImageObserver Ob){

    }
*/
    public TankObjects(int x, int y, int width, int height, BufferedImage img ) { //unmovable
        this.x = x;
        this.y = y;
        this.img = img;
        this.height = width;
        this.width = height;

        this.box = new Rectangle(this.x, this.y, this.width, this.height);
    }

    public TankObjects(BufferedImage img, int x, int y, int speed){ //movable
        this.img = img;
        this.x = x;
        this.y = y;
        this.speed = speed;

        try {
            this.width = img.getWidth();
            this.height = img.getHeight();
        } catch (NullPointerException e) {
            System.out.println("NullPointerException for GameObject(int, int, Image, ImageObserver)\n");
            this.width = 0;
            this.height = 0;
        }

        this.box = new Rectangle(this.x, this.y, this.width, this.height);
    }


        public Rectangle getWallRectangle() {
            return box;
        }
/*
    public boolean collision(int x, int y , int w, int h){
        box = new Rectangle(this.x, this.y, this.width, this.height);
        Rectangle secbox = new Rectangle(x,y,w,h);
        if(this.box.intersects(secbox)){
            System.out.println("hit");
            return true;
        }
        return false;
    }
*/

    @Override
    public int getX(){
        return this.x;
    }

    @Override
    public int getY(){
        return this.y;
    }

    @Override
    public int getWidth(){
        return this.width;
    }

    @Override
    public int getHeight(){
        return this.height;
    }

}
