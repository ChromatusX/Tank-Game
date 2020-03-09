package TankG;

import TankG.GameObj.Tank;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ControlKeys implements KeyListener {

    private final Tank tank;
    private final int up;
    private final int down;
    private final int left;
    private final int right;
    private final int shoot;

/*
    public ControlKeys(Tank tank, int up, int down, int left, int right, int shoot){
        this.tank = tank;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.shoot = shoot;
    }
    */

    public ControlKeys(Tank tank){
        this.tank = tank;
        this.up = tank.getUpKey();
        this.down = tank.getDownKey();
        this.left = tank.getLeftKey();
        this.right = tank.getRightKey();
        this.shoot = tank.getShootkey();
    }




    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        if (keyReleased  == up) {
            this.tank.unToggleUpPressed();
        }
        if (keyReleased == down) {
            this.tank.unToggleDownPressed();
        }
        if (keyReleased  == left) {
            this.tank.unToggleLeftPressed();
        }
        if (keyReleased  == right) {
            this.tank.unToggleRightPressed();
        }
        if (keyReleased == shoot){
            this.tank.unToggleshoot();
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent ke){
        int keyPressed = ke.getKeyCode();
        if(keyPressed == up){
            this.tank.toggleUpPressed();
        }

        if(keyPressed == down){
            this.tank.toggleDownPressed();
        }

        if(keyPressed == left){
            this.tank.toggleLeftPressed();
        }

        if(keyPressed == right){
            this.tank.toggleRightPressed();
        }

        if(keyPressed == shoot){
            this.tank.toggleShoot();
        }
    }
}
