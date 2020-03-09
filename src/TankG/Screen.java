package TankG;//package TankGame;

import TankG.GameObj.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import java.util.ConcurrentModificationException;

import javax.swing.JPanel;

public class Screen extends JPanel {

    private BufferedImage backimg;
    private BufferedImage lifeIcon1, lifeIcon2;
    private int frameW, frameH, mapW, mapH;

    BufferedImage p1 , p2;
    Image minimap;

    //
    private int minimapW; //= 200;
    private int minimapH; //= 200;

    //bound Windows--used
    private int p1X,p1Y,p2X,p2Y;

    //tanks
    private Tank tank1;
    private Tank tank2;

    //objects
    private ArrayList<Wall> wall;
    private ArrayList<BreakableWall> bwall;
    private ArrayList<Power> pow;
    private ArrayList<Bullet> pew;
    private ArrayList<Heal> heal;

    public Screen() {

    }

    public Screen(int mapW, int mapH, int frameW, int frameH, String backimg){
        super();
        this.mapW=mapW;
        this.mapH=mapH;
        this.frameW=frameW;
        this.frameH=frameH;
        this.backimg=setImage(backimg);

        this.minimapH = 200;
        this.minimapW = 200;

        this.setSize(mapW,mapH);
        this.setPreferredSize(new Dimension(mapW, mapH));

        wall = new ArrayList<>();
        bwall = new ArrayList<>();
        pow = new ArrayList<>();
        pew = new ArrayList<>();
        heal = new ArrayList<>();
    }

    private BufferedImage setImage(String src){
        BufferedImage img = null;
        try{
            img= ImageIO.read(new File(src));
        }catch(IOException e){
            System.out.println("Error on backimg");
        }

        return img;
    }

    @Override
    public void paint(Graphics a){
        getGameImage();
        super.paint(a);

        a.drawImage(p1,0,0,this);
        a.drawImage(p2, frameW/2, 0 , this);

        drawStatus(a);

        a.setColor(Color.blue);
        a.draw3DRect(0,0, (frameW/2)-1 , frameH -22 , true);
        a.draw3DRect(frameW/2, 0 , (frameW/2)-1, frameH-2, true);

        a.drawImage(minimap, (frameW/2) - (minimapW/2),0, this);
        a.draw3DRect((frameW/2)-(minimapW/2), 0 , minimapW, minimapH,true);

        a.setColor(Color.BLACK);
        if (tank1.getLife() < 0) {
            a.setFont(new Font(a.getFont().getFontName(), Font.CENTER_BASELINE, 84));
            a.drawString("Player 2 wins!", 70, frameH/2);
        }

        a.setColor(Color.BLACK);
        if (tank2.getLife() < 0) {
            a.setFont(new Font(a.getFont().getFontName(), Font.CENTER_BASELINE, 84));
            a.drawString("Player 1 wins!", 70, frameH/2);
        }
    }


    public void getGameImage(){
        BufferedImage backimg = new BufferedImage(mapW, mapH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bpic= backimg.createGraphics();

        drawMap(bpic);
        drawtanks(bpic);
        drawMapObj(bpic);
        drawBullet(bpic);

        playerViewCheck();
        p1 = backimg.getSubimage(this.p1X, this.p1Y,frameW/2,frameH);
        p2 = backimg.getSubimage(this.p2X, this.p2Y, frameW/2, frameH);
        minimap = backimg.getScaledInstance(minimapW, minimapH, Image.SCALE_SMOOTH);
    }


    private void drawMap(Graphics2D map){
        for(int a =0; a < 6; a++){
            for(int b=0; b< 8; b++){
                map.drawImage(this.backimg, this.backimg.getWidth()*a,this.backimg.getHeight()*b, this);
            }
        }
    }

    private void drawMapObj(Graphics2D g){
       wall.forEach((curr) ->{
           curr.draw(g);
       });

       bwall.forEach((curr) ->{
           curr.draw(g);
       });

       pow.forEach((curr) ->{
           curr.draw(g);
       });

       heal.forEach((curr) ->{
           curr.draw(g);
       });

    }

    private synchronized void drawBullet(Graphics2D g){
        Graphics2D g2 = (Graphics2D) g;
        try{
            pew.forEach((curr) ->{
                if(curr.isVisible()){
                    curr.draw(this, g2);
                }
            });

        }catch(ConcurrentModificationException e){

        }
    }

    public void drawStatus(Graphics g){
        int p1health = this.tank1.getHealth() *2;
        int p2health = this.tank2.getHealth() *2;

        int p1life = this.tank1.getLife();
        int p2life = this.tank2.getLife();

        int p1healthx = 22;
        int p1healthy = 650;

        int p2healthx = 578;
        int p2healthy = 650;

        int healthw = 200;
        int healthh = 20;

        int coffset = 4;
        int soffset = 8;

        //Health bar information
        g.setColor(Color.DARK_GRAY);
        g.fillRect(p1healthx, p1healthy, healthw, healthh);
        g.fillRect(p2healthx, p2healthy, healthw, healthh);

        g.setColor(Color.RED);
        g.fillRect(p1healthx+coffset,p1healthy+coffset, healthw-soffset, healthh-soffset);
        g.fillRect(p2healthx + coffset, p2healthy+coffset, healthw-soffset,healthh-soffset);

        g.setColor(Color.GREEN);
        g.fillRect(p1healthx+coffset,p1healthy+coffset,p1health-soffset, healthh-soffset);
        g.fillRect(p2healthx+(healthw-p2health) + coffset, p2healthy+coffset, p2health- soffset, healthh-soffset);

        for (int i = 0; i < p1life; i++) {
            g.drawImage(lifeIcon1, 100 + (i * 40), 620, this);
        }

        for (int i = 0; i < p2life; i++) {
            g.drawImage(lifeIcon2, 638 + (i * 40), 620, this);
        }
    }

    public void setobj(ArrayList<Wall> w, ArrayList<BreakableWall> b, ArrayList<Power> p, ArrayList<Heal> h){
        this.wall = w;
        this.bwall = b;
        this.pow = p;
        this.heal = h;
    }

    public void setTanks(Tank t1, Tank t2){
        this.tank1 = t1;
        this.tank2 = t2;
    }

    public void setIcons(BufferedImage a, BufferedImage b){
        this.lifeIcon1 = a;
        this.lifeIcon2 = b;
    }

    public void setBullet(ArrayList<Bullet> b){
        this.pew = b;
    }

    private void drawtanks(Graphics2D g){
        Graphics2D a = (Graphics2D)g;
        this.tank1.draw(a);
        this.tank2.draw(a);
    }

    //camera view
    private void playerViewCheck() {
        if ((this.p1X = tank1.getTankCenterX() - frameW / 4) < 0) {
            this.p1X = 0;
        } else if (this.p1X >= mapW - frameW / 2) {
            this.p1X = (mapW - frameW / 2);
        }

        if ((this.p1Y = tank1.getTankCenterY() - frameH / 2) < 0) {
            this.p1Y = 0;
        } else if (this.p1Y >= mapH - frameH) {
            this.p1Y = (mapH - frameH);
        }

        if ((this.p2X = tank2.getTankCenterX() - frameW / 4) < 0) {
            this.p2X = 0;
        } else if (this.p2X >= mapW - frameW / 2) {
            this.p2X = (mapW - frameW / 2);
        }

        if ((this.p2Y = tank2.getTankCenterY() - frameH / 2) < 0) {
            this.p2Y = 0;
        } else if (this.p2Y >= mapH - frameH) {
            this.p2Y = (mapH - frameH);
        }
    }
}