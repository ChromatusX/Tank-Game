package TankG;//package Tank;

import TankG.GameObj.*;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javax.swing.JFrame;


public class TankWorld implements Runnable{

    //Use for frame
    private JFrame window;
    private String title = "Tank Game";
    private int frameW = 800, frameH =700, mapW=1600, mapH=1600;

    //map info
    private String map = "Resources/Background.bmp";
    private final int rows=25, cols=25;
    private int[][] layout;

    //objects
    private ArrayList<Wall> wall;
    private ArrayList<BreakableWall> bwall;
    private ArrayList<Power> pow;
    private ArrayList<Bullet> pew;
    private ArrayList<Heal> heal;

    //
    private Thread thread;
    private boolean running = false;

    //window
    private Screen screen;

    //resources
    private String tank1img ="Resources/newtank.gif";
    private String tank2img = "Resources/newtank2.gif";
    //private String backSound = "Resources/Music.mp3";
    private String wallimg = "Resources/Wall1.gif";
    private String bwallimg = "Resources/Wall2.gif";
    private String buffimg = "Resources/Pickup.gif";
    private String healingimg = "Resources/Health.gif";
    private String bulletimg = "Resources/Weapon.gif";
    private String lifeicon1 = "Resources/Bouncing.gif";
    private String lifeicon2 = "Resources/Bouncing.gif";

    //players and controls
    private static Tank tank1;
    private static Tank tank2;
    private ControlKeys input1;
    private ControlKeys input2;

    //for randomizing map
    int mapnumber;


    private synchronized void start(){
        if(running){
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop(){
        if(!running){
            return;
        }
        running = false;
        try{
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        TankWorld tankgame = new TankWorld();
        tankgame.start();
    }


    @Override
    public void run() {
        initiate();
        try{
            while(running){
                tank1.update();
                tank2.update();

                wall.forEach((curr) ->{
                    curr.WALLupdate();
                });

                bwall.forEach((curr) ->{
                    curr.BWupdate();
                });

                ////
                pow.forEach((curr) ->{
                    curr.POWupdate();
                });

                heal.forEach((curr) ->{
                    curr.HEALupdate();
                });


                this.screen.repaint();
                this.screen.setBullet(pew);
                Thread.sleep(1000/144);
            }
        }catch(InterruptedException e){
            Logger.getLogger(TankWorld.class.getName()).log(Level.SEVERE,null,e);
        }
        stop();
    }

    public void initiate(){

        this.screen = new Screen(mapW, mapH, frameW, frameH, map);
        mapnumber = getRandomInteger(3,0);
        setMapLayout();
        drawobj();
        setupPlayers();
        //setSounds();

        window = new JFrame();
        this.window.setTitle(title);
        this.window.setSize(frameW,frameH);
        this.window.setPreferredSize(new Dimension(frameW,frameH));
        this.window.setResizable(false);
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setLocationRelativeTo(null);
        this.window.add(this.screen);

        this.window.addKeyListener(input1);
        this.window.addKeyListener(input2);

        this.window.pack();
        this.window.setVisible(true);

        //this.window.pack();
    }


    public static int getRandomInteger(int max, int min){   //use to random generate a number for which map
        int a= ((int) (Math.random()*(max-min)))+min;
        //System.out.println(a);  //check number
        return a;
    }

    private void setMapLayout() {
        if (mapnumber == 0 || mapnumber == 1) {  //less chance for special map
            System.out.println("Welcome!");
            //0 is empty space, 1 is buff, 2 is wall, 3 is breakable wall, 4 is heal
            this.layout = new int[][]{
                   //1, 2, 3, 4, 5 ,6 ,7, 8, 9, 10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25
                    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, //1
                    {2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 4, 2}, //2
                    {2, 0, 0, 2, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 3, 2}, //3
                    {2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2}, //4
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //5
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //6
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //7
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //8
                    {2, 2, 2, 0, 0, 0, 0, 0, 0, 2, 3, 3, 2, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //9
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 2, 2}, //10
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //11
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 3, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //12
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 3, 1, 3, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //13
                    {2, 2, 2, 0, 0, 0, 0, 0, 0, 2, 0, 2, 3, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //14
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //15
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //16
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 3, 2, 3, 3, 2, 0, 0, 0, 0, 0, 0, 2, 2, 2}, //17
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //18
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //19
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //20
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //21
                    {2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2}, //22
                    {2, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 0, 0, 2}, //23
                    {2, 4, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2}, //24
                    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2} //25

            };
        }else {
            System.out.println("Welcome to special map");//in progress
                    //0 is empty space, 1 is buff, 2 is wall, 3 is breakable wall, 4 is heal
                    //teleporter?? //speed boost?? //machinegun??
            this.layout = new int[][]{
                   //1, 2, 3, 4, 5 ,6 ,7, 8, 9, 10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25
                    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, //1
                    {2, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 3, 1, 1, 1, 2}, //2
                    {2, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 3, 1, 1, 1, 2}, //3
                    {2, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 3, 1, 1, 1, 2}, //4
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 2}, //5
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 2}, //6
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 2, 2, 2}, //7
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //8
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //9
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //10
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //11
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //12
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //13
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //14
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //15
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //16
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //17
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //18
                    {2, 2, 2, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //19
                    {2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //20
                    {2, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, //21
                    {2, 1, 1, 1, 3, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 2}, //22
                    {2, 1, 1, 1, 3, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 2}, //23
                    {2, 1, 1, 1, 3, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 2}, //24
                    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2} //25

            };
        }

    }

    //0 is empty space, 1 is buff, 2 is wall, 3 is breakable wall, 4 is heal
    private void drawobj(){
        wall = new ArrayList<>();
        bwall = new ArrayList<>();
        pow = new ArrayList<>();
        heal = new ArrayList<>();
        BufferedImage objectimg;
        int size = 64;
        int extra = 32;

        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                if(this.layout[row][col] == 2) {
                    objectimg = setImage(wallimg);
                    wall.add(new Wall(col*size, row*size,
                            objectimg.getWidth(), objectimg.getHeight(), objectimg));
                    wall.add(new Wall((col*size)+extra, row*size,
                            objectimg.getWidth(), objectimg.getHeight(), objectimg));
                    wall.add(new Wall(col * size, (row * size) + extra,
                            objectimg.getWidth(), objectimg.getHeight(), objectimg));
                    wall.add(new Wall((col * size) + extra, (row * size) + extra,
                            objectimg.getWidth(), objectimg.getHeight(), objectimg));
                }

                if(this.layout[row][col] == 3){
                    objectimg = setImage(bwallimg);
                    bwall.add(new BreakableWall(col*size, row*size,
                            objectimg.getWidth(), objectimg.getHeight(), objectimg));
                    bwall.add(new BreakableWall((col*size)+extra, row*size,
                            objectimg.getWidth(), objectimg.getHeight(), objectimg));
                    bwall.add(new BreakableWall(col * size, (row * size) + extra,
                            objectimg.getWidth(), objectimg.getHeight(), objectimg));
                    bwall.add(new BreakableWall((col * size) + extra, (row * size) + extra,
                            objectimg.getWidth(), objectimg.getHeight(), objectimg));
                }

                if(this.layout[row][col] == 1){
                    objectimg = setImage(buffimg);

                    pow.add(new Power((col*size)+(extra/2), (row*size)+(extra/2),
                            objectimg.getWidth(), objectimg.getHeight(), objectimg));
                    }

                if(this.layout[row][col] == 4) {
                    objectimg = setImage(healingimg);
                    heal.add(new Heal((col*size)+(extra/2), (row*size)+(extra/2),
                            objectimg.getWidth(), objectimg.getHeight(), objectimg));
                }
            }
        }
        this.screen.setobj(this.wall,this.bwall,this.pow, this.heal);

    }

    private BufferedImage setImage(String path){
        BufferedImage img = null;
        try{
            img = ImageIO.read(new File(path));
        } catch (IOException e){
            System.out.println("Error on img");
        }
        return img;
    }

    private void setupPlayers(){
        BufferedImage p1img = setImage(tank1img);
        BufferedImage p2img = setImage(tank2img);


        int tspeed = 2;

        tank1 = new Tank(this, p1img , 100, 100, tspeed, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);

        tank2 = new Tank(this, p2img, 1440, 1440, tspeed, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);

        //tank 2 fair spawn point 1440,1440

        tank1.setSecondTank(tank2);
        tank2.setSecondTank(tank1);
        tank2.setAngle(180);

        this.screen.setTanks(tank1, tank2);
        this.input1 = new ControlKeys(tank1);
        this.input2 = new ControlKeys(tank2);

        this.pew = new ArrayList<>();

        this.screen.setIcons(setImage(this.lifeicon1), setImage(this.lifeicon2));
    }

    public static Tank getTank(int number){
        switch(number){
            case 1:
                return tank1;
            case 2:
                return tank2;
            default:
                System.out.println("no tank");
                return null;
        }
    }

    //breaking the wall
    public ArrayList<BreakableWall> getBreakableWalls() {
        return this.bwall;
    }

    public int getBreakableWallSize() {
        return bwall.size();
    }
    //

    //normal wall with bullet interaction
    public int getWallSize() {
        return wall.size();
    }

    public ArrayList<Wall> getWalls(){
        return this.wall;
    }

    public BufferedImage getBulletimg(){
        BufferedImage b = setImage(bulletimg);
        return b;
    }

    public ArrayList<Bullet> getBullet(){
        return pew;
    }



}


