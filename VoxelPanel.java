import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class VoxelPanel extends JPanel implements Runnable {
    
    //Constants
    private static final int VIEW_WIDTH = 800;
    private static final int VIEW_HEIGHT = 600;
    
    private static final double HOR_SPEED = 2;                //relative speed moving fw/bw
    private static final double VERT_SPEED = 4;               //relative speed moving up/down
    private static final int TURN_DEGREES = 5;
    private static final int HFOV = 90;                       //Horizontal Field of View in degrees
    private static final int DELAY = 20;                      //delay in ms between frames
    
    private static final int K_SCALE = 100;                   //Constant to scale projection
    private static final double K_CENTER = VIEW_HEIGHT * .7;  //Constant to center projection (slightly above half-height)
    
    
    private SwingAnimator animator;
    
    private double distance;  //distance from vp to screen, calc based on FOV
    
    private Map map;
    private BufferedImage image;
    private int[] pixels;
    
    private int fogColor;
    
    //calculated
    private double playerX, playerY; //x,y position on map
    private int cameraH;             //cameraH: height of camera
    private int direction = 0;       //direction of camera in degrees
    private double dirSin;           //sin/cos of direction
    private double dirCos;


    //set by keylistener
    boolean dir_forward = false;
    boolean dir_backward = false;
    boolean turn_left = false;
    boolean turn_right = false;
    boolean dir_up = false;
    boolean dir_down = false;
    boolean do_fog = true;
    
    public VoxelPanel() {
        this.animator = new SwingAnimator(this, DELAY);
        
        //create buffered image and get raw array
        this.image = new BufferedImage(VIEW_WIDTH, VIEW_HEIGHT, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = image.getRaster();
        DataBufferInt buffer = (DataBufferInt)raster.getDataBuffer();
        this.pixels = buffer.getData();
        
        this.fogColor = Util.packColor(200, 215, 230);
        //fogColor = packColor(135, 206, 250);
        
        distance = ((VIEW_WIDTH / 2) / Math.tan(Math.toRadians(HFOV /2)));
        System.err.println("distance=" + distance);
    
        map = new Map();

        initPanel();
    }
    
    
    
    
    private void updatePosition() {
        if(this.dir_forward) {
            playerX -= HOR_SPEED * dirSin;
            playerY += HOR_SPEED * dirCos;
        }
        if(this.dir_backward) {
            playerX += HOR_SPEED * dirSin;
            playerY -= HOR_SPEED * dirCos;
        }
        if(this.turn_left) {
            setDirection(direction + TURN_DEGREES);
        }
        if(this.turn_right) {
            setDirection(direction - TURN_DEGREES);
        }
        if(this.dir_up) {
            cameraH += VERT_SPEED;
        }
        if(this.dir_down) {
            cameraH -= VERT_SPEED;
        }
    }
    private void setDirection(int degrees) {
        this.direction = degrees;
        double rads = Math.toRadians(degrees);
        this.dirSin = Math.sin(rads);
        this.dirCos = Math.cos(rads);
    }
    
    
    
    private void initPanel() {
        //setBackground(Color.BLACK);
        setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
        setDoubleBuffered(true);
    
        loadMap(0);
        System.err.println("init completed");

    }
    
    void loadMap(int mapNumber) {
        //set initial positions
        playerX = 512;
        playerY = 512;
        cameraH = 300;
        setDirection(0);
        
        //load height + texture files
        try {
            map.load(mapNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    void start() {
        animator.start();
    }
    
    @Override
    public void run() {
        updatePosition();
        repaint();
        Toolkit.getDefaultToolkit().sync();
    }

    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render((Graphics2D)g);
    }
    
    
    private void render(Graphics2D g2) {
        for (int i = 0; i < VIEW_WIDTH * VIEW_HEIGHT; i++) {
            pixels[i] = fogColor;
        }
        
        double vpY = distance;
        for (int i = 0; i < VIEW_WIDTH; i++) {
            double vpX = (i - VIEW_WIDTH / 2) + .5;
            
            double rotx = vpX * dirCos - vpY * dirSin;
            double roty = vpX * dirSin + vpY * dirCos;
            
            drawline(i, playerX, playerY, rotx + playerX, roty + playerY);
        }
        g2.drawImage(image, 0, 0, this);
        
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("x: %1$.1f y: %2$.1f h:%3$d dir: %4$d  height:%5$d FPS:%6$d", playerX, playerY, cameraH, direction,  map.getHeight(playerX, playerY), animator.fps);
        g2.setColor(Color.YELLOW);
        g2.drawString(sb.toString(), 1, 15);
     }
    
    /*
     *  line: vertical line to compute
     *  posX,posY: start point on map for ray - View Point Coordinates
     *  endX, endY: end point on map for ray - View Plane Intersection
     *
     *  Note: we sample beyond the initial ray length
     */
    private void drawline(int line, double posX, double posY, double endX, double endY) {

        double stepX = endX - posX;
        double stepY = endY - posY;
        
        //calc magnitude (distance between start and end)
        double r = Math.sqrt(stepX * stepX + stepY * stepY);
        
        //calc stepsize in x and y (normalize)
        stepX = stepX / r;
        stepY = stepY / r;
        
        //draw front to back
        int bottom_y = 0;
        double distZ = 0;
        int fogFactor = 0;
        
        
        //sample (r * 4) times or until fog reaches max value
        for (int i = 0; i < r * 4 && fogFactor < 256; i++) {
            //inc by step size
            posX += stepX;
            posY += stepY;
            distZ += 1;
            
            //get map height + color
            int map_height = map.getHeight(posX, posY);
            int colorRGB = map.getColor(posX,posY);
            
            //add fog
            if(do_fog && distZ > r) {
                fogFactor = (int)( (distZ - r) * .25); //arbitrary value that looked good
                if(fogFactor > 256) {
                    fogFactor = 256;
                }
                colorRGB = Util.blendColor(colorRGB, fogColor, fogFactor);
            }
            
            //perspective calc
            double top_y = (map_height - cameraH) * K_SCALE / distZ + K_CENTER;

            
            //if above top height, then only to top
            if(top_y >= VIEW_HEIGHT) {
                top_y = VIEW_HEIGHT - 1;
            }
            
            
            int offset = (VIEW_HEIGHT - bottom_y - 1) * VIEW_WIDTH + line;
            for (int k = bottom_y; k < top_y; k++) {
                pixels[offset] = colorRGB;
                offset -= VIEW_WIDTH;
            }
            
            //keep track of last pixel
            if(top_y > bottom_y) {
                bottom_y = (int)top_y;
            }
         }
        
    }

    
    
    
    
}

