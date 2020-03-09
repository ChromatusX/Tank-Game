package TankG.GameObj;

import TankG.TankWorld;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Heal extends TankObjects {

    boolean show = true;

    public Heal(int x, int y, int width, int height, BufferedImage img) {
        super(x, y, width, height, img);
        //show = true;

    }

    public void draw(Graphics g){
        //update();
        if(show){
            g.drawImage(this.img, this.x, this.y, this);
        }
    }

    public void HEALupdate(){
        Tank p1 = TankWorld.getTank(1);
        Tank p2 = TankWorld.getTank(2);

        if(p1.collision(this)){
            if(show == true && p1.health < 100) {

                System.out.println("player 1 power up: full heal");
                p1.health = 100;
                show = false;
            }
        }
        else if(p2.collision(this)){
            if(show == true && p2.health < 100) {

                System.out.println("player 2 power up: full heal");
                p2.health = 100;
                show = false;
            }
        }

    }
}