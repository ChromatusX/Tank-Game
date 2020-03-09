package TankG.GameObj;

import TankG.TankWorld;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Wall extends TankObjects{
    Rectangle wallrect;
    private int height, width;



    public Wall(int x, int y, int width, int height, BufferedImage img){
        super(x, y, width, height, img);
        this.height = img.getHeight();
        this.width = img.getWidth();
        wallrect = new Rectangle(x,y,width,height);

    }

    public void draw(Graphics g){

            g.drawImage(this.img, this.x, this.y, this);

    }

    public void WALLupdate(){
        Tank p1 = TankWorld.getTank(1);
        Tank p2 = TankWorld.getTank(2);

        if(p1.collision(this)){
            //System.out.println("player 1 hit wall");
            if(p1.x > (this.x)){
                p1.x +=1;

            }else if(p1.x < (this.x)){
                p1.x -=1;
            }

            if(p1.y > (this.y)){
                p1.y +=1;
            }else if(p1.y < this.y){
                p1.y -=1;
            }
        }

        if(p2.collision(this)){
            //System.out.println("player 2 hit wall");
            if(p2.x > (this.x)){
                p2.x +=1;

            }else if(p2.x < (this.x)){
                p2.x -=1;
            }

            if(p2.y > (this.y)){
                p2.y +=1;
            }else if(p2.y < this.y){
                p2.y -=1;
            }
        }


    }

}
