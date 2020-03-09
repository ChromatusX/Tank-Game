package TankG.GameObj;

import TankG.TankWorld;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class BreakableWall extends TankObjects {
    Rectangle wallRect;
    private int height;
    private int width;
    private boolean isdestroy = false;

    public BreakableWall(int x, int y, int width, int height, BufferedImage img){
        super(x, y, width, height, img);
        this.height = img.getHeight();
        this.width = img.getWidth();
        wallRect = new Rectangle(x,y,width,height);
    }

    public void breakwall(){
        isdestroy = true;
    }

    public void draw(Graphics g){
        g.drawImage(this.img, this.x, this.y, this);
    }

    public void BWupdate(){
        Tank p1 = TankWorld.getTank(1);
        Tank p2 = TankWorld.getTank(2);
        if(!isdestroy){

            if(p1.collision(this)){
                //System.out.println("player 1 hit bwall");
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
                //System.out.println("player 2 hit bwall");
                if(p2.x > (this.x)){
                    p2.x +=1;

                }else if(p2.x < (this.x)){
                    p2.x -=1;
                }

                if(p2.y > (this.y)){
                    p2.y +=1;
                }else if(p1.y < this.y){
                    p2.y -=1;
                }
            }

        }
    }
}
