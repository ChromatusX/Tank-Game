package TankG.GameObj;

import TankG.TankWorld;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Tank extends TankObjects {
    protected int health = 100;
    protected int life = 2;
    protected int dmg = 10;
    private int tankspeed = 18;
    private int angle = 0;
    private int up, down, left, right;
    private int shootkey;
    private boolean moveU, moveD, moveL, moveR, pewpew;
    private boolean dead;  //used
    private Tank player1, player2;  //used
    private TankWorld obj;
    private int spawnX;  //respawn points
    private int spawnY;


    private int shootCoolDown = 0;

   // private final int ROTATIONSPEED = 4;

    public Tank(){

    }

    public Tank(TankWorld obj,BufferedImage img, int x, int y, int speed, int up, int down, int left, int right, int shoot ){
        super(img, x, y, speed);
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.shootkey = shoot;
        this.moveU = false;
        this.moveD = false;
        this.moveL = false;
        this.moveR = false;
        this.pewpew = false;
        this.dead = false;
        this.spawnX = x;
        this.spawnY = y;
        this.obj = obj;
        this.setBounds(8,10,49,44);
    }

    public void setSecondTank(Tank tank2){
        this.player1 = new Tank();
        this.player1 = this;
        this.player2 = new Tank();
        this.player2 = tank2;
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


    public boolean collision(TankObjects a){
        box = new Rectangle(this.x, this.y, this.width, this.height);
        Rectangle secondbox = new Rectangle(a.getX(), a.getY(), a.getWidth(), a.getHeight());
        if(this.box.intersects(secondbox)){
            //System.out.println("hit");
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g){  ///check for movement
        player1 = TankWorld.getTank(1);
        player2 = TankWorld.getTank(2);
        this.shootCoolDown -=1;

        if(this.health <= 0){
            life--;
            this.dmg = 10;   //resets dmg when respawn after being buff
            health = 100;
            this.x = spawnX;
            this.y = spawnY;
        }

        if((health>0) && (life>=0)){
            dead = false;
            AffineTransform move = AffineTransform.getTranslateInstance(x,y);
            move.rotate(Math.toRadians(angle), img.getWidth(null)/2,img.getHeight(null)/2  );
            g.drawImage(img, move, null);
            //g.draw(this.getWallRectangle());  //to check hitbox
        }

    }


    public void toggleUpPressed() {
        this.moveU = true;
    }

    public void toggleDownPressed() {
        this.moveD = true;
    }

    public void toggleRightPressed() {
        this.moveR = true;
    }

    public void toggleLeftPressed() {
        this.moveL = true;
    }

    public void unToggleUpPressed() {
        this.moveU = false;
    }

    public void unToggleDownPressed() {
        this.moveD = false;
    }

    public void unToggleRightPressed() {
        this.moveR = false;
    }

    public void unToggleLeftPressed() {
        this.moveL = false;
    }

    public void toggleShoot(){
        this.pewpew = true;
    }
    public void unToggleshoot(){
        this.pewpew = false;
    }

    //getting keys

    public int getUpKey(){
        return this.up;
    }

    public int getDownKey(){
        return this.down;
    }

    public int getLeftKey(){
        return this.left;
    }

    public int getRightKey(){
        return this.right;
    }

    public int getShootkey(){
        return this.shootkey;
    }

    public int getTankCenterX(){
        return x + img.getWidth(null)/2;
    }

    public int getTankCenterY(){
        return y + img.getHeight(null)/2;
    }

    public int getAngle(){
        return this.angle;
    }

    public int getHealth(){
        return health;
    }

    public int getLife(){
        return life;
    }

    public void update() {
        shoot(this);
        if (moveL == true){
            angle -= 3;
        }
        if (moveR == true){
            angle += 3;
        }
        if (moveU == true){
            x = ((int) (x + Math.round(speed * Math.cos(Math.toRadians(angle)))));
            y = ((int) (y + Math.round(speed * Math.sin(Math.toRadians(angle)))));

        }
        if (moveD == true){
            x = ((int) (x - Math.round(speed * Math.cos(Math.toRadians(angle)))));
            y = ((int) (y - Math.round(speed * Math.sin(Math.toRadians(angle)))));

        }

        if (angle == -1) {
            angle = 359;
        } else if (angle == 361) {
            angle = 1;
        }


    }

    public void setAngle(int a){
        this.angle = a;
    }



    private void shoot(Tank a){
        if (pewpew && shootCoolDown <= 0 && life >= 0) {
            Bullet newbullet = new Bullet(this.obj, obj.getBulletimg(), tankspeed, this, dmg);
            obj.getBullet().add(newbullet);
            this.shootCoolDown = 10;
            //System.out.println(dmg);  test to see dmg
        }
    }

    public void bulletDamage(int dmg) {
            this.health -= dmg;
    }


}

