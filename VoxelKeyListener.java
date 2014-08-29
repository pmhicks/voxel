

import java.awt.event.*;

public class VoxelKeyListener  implements KeyListener {
    private static final char LEFT = 'a';
    private static final char RIGHT = 'd';
    private static final char FORWARD = 'w';
    private static final char BACKWARD = 's';
    private static final char UP = 'q';
    private static final char DOWN = 'e';
    
    private static final char FOG = 'f';
      
    private VoxelPanel vp;
    
    public VoxelKeyListener(VoxelPanel panel) {
        this.vp = panel;
    }
    
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        //System.err.println(c);
        
        switch(c) {
            case LEFT:
                vp.turn_left = true;
                break;
            case RIGHT:
                vp.turn_right = true;
                break;
            case FORWARD:
                vp.dir_forward = true;
                break;
            case BACKWARD:
                vp.dir_backward = true;
                break;
            case UP:
                vp.dir_up = true;
                break;
            case DOWN:
                vp.dir_down = true;
                break;
        }
    }
    
    public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();
        switch(c) {
            case LEFT:
                vp.turn_left = false;
                break;
            case RIGHT:
                vp.turn_right = false;
                break;
            case FORWARD:
                vp.dir_forward = false;
                break;
            case BACKWARD:
                vp.dir_backward = false;
                break;
            case UP:
                vp.dir_up = false;
                break;
            case DOWN:
                vp.dir_down = false;
                break;
        }
        
    }
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        switch(c) {
            case FOG:
                vp.do_fog = ! vp.do_fog;
                break;
            case '1':
                vp.loadMap(0);
                break;
            case '2':
                vp.loadMap(1);
                break;
            case '3':
                vp.loadMap(2);
                break;
            case '4':
                vp.loadMap(3);
                break;
            case '5':
                vp.loadMap(4);
                break;

        }
    }

}
