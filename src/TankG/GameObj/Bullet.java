package TankG.GameObj;

import TankG.TankWorld;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;


public class Bullet extends TankObjects{
    private final Tank p1 = TankWorld.getTank(1);
    private final Tank p2 = TankWorld.getTank(2);
    private int damage;
    private final BufferedImage ammo;
    private  TankWorld obj;
    public int xsize;
    public int ysize;
    public int bulletangle;
    public static Tank currentTank;
    public boolean visible;

    public Bullet(TankWorld tw, BufferedImage img, int speed, Tank t, int dmg) {
        super(img, t.getTankCenterX(), t.getTankCenterY(), speed);
        ammo = img;
        damage = dmg;
        xsize = img.getWidth(null);
        ysize = img.getHeight(null);
        this.obj = tw;
        currentTank = t;
        bulletangle = t.getAngle();
        visible = true;
    }

    public boolean isVisible(){
        return this.visible;
    }

    /*
    public void setDamage(int a){
        this.damage = a;
    }
*/
    public void draw(ImageObserver iobs, Graphics2D g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(bulletangle), 0, 0);
        g.drawImage(ammo, rotation, iobs);
        update();
    }


    public void update(){
        y += Math.round(speed * Math.sin(Math.toRadians(bulletangle)));
        x += Math.round(speed * Math.cos(Math.toRadians(bulletangle)));

        //player 1 interaction with bullet
        if (p1.collision(this) && currentTank!= p1) {
            if (visible) {

            }
            visible = false;
            p1.bulletDamage(damage);
        }
        //player 2 interaction with bullet
        if(p2.collision(this) && currentTank!= p2){
            visible = false;
            p2.bulletDamage(damage);
        }

            //breakable wall interaction with bullet
        for(int j=0; j<obj.getBreakableWallSize(); j++){
            BreakableWall bwall2 = obj.getBreakableWalls().get(j);
            if(bwall2.getWallRectangle().intersects(this.x, this.y, this.width, this.height)){
                obj.getBreakableWalls().remove(j);
                bwall2.breakwall();
                this.visible = false;
            }
        }
            //wall interaction with bullet
        for(int a = 0; a<obj.getWallSize();a++){
            Wall wall2 = obj.getWalls().get(a);
            if(wall2.getWallRectangle().intersects(this.x, this.y, this.width, this.height) && visible){
                this.visible = false;
            }
        }

    }
}
