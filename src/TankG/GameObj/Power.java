package TankG.GameObj;

import TankG.TankWorld;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Power extends TankObjects {

    boolean show = true;

    public Power(int x, int y, int width, int height, BufferedImage img) {
        super(x, y, width, height, img);
        //show = true;

    }

    public void draw(Graphics g){
        if(show){
            g.drawImage(this.img, this.x, this.y, this);
        }
    }

    public void POWupdate(){
        Tank p1 = TankWorld.getTank(1);
        Tank p2 = TankWorld.getTank(2);

        if(p1.collision(this)){
            if(show == true) {

                System.out.println("player 1 power up increase dmg");
                p1.dmg +=5;
                show = false;
            }
        }
        else if(p2.collision(this)){
            if(show == true) {

                System.out.println("player 2 power up increase dmg");
                p2.dmg +=5;
                show = false;
            }
        }

    }
}