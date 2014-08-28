import java.awt.image.*;
import java.io.*;
import javax.imageio.*;


/*
 * Represents a Voxel Map with height and texture values
 *
 * (0,0) is bottom left
 *
 *       Y+
 *       |
 *       |
 *       |
 * (0,0) ------ X+
 */

public class Map {
    private static String[] names = {"greenhills", "desert", "artic", "tropic"};
    
    public byte[] heightMap;
    public int[] textureMap;
    
    public Map() {
        this.heightMap = new byte[1024 * 1024];
        this.textureMap = new int[1024 * 1024];
    }
    
    public void load(int map) throws IOException {
        this.loadHeightMap(map);
        this.loadTextureMap(map);
    }
    
    public int getHeight(double x1, double y1) {
        int map_offset = ((int) y1 & 1023) * 1024 + ((int) x1 & 1023);
        return (heightMap[map_offset] & 0xFF);
        
    }
    public int getColor(double x1, double y1) {
        int map_offset = ((int) y1 & 1023) * 1024 + ((int) x1 & 1023);
        return (textureMap[map_offset]);
    }
    
    
    private void loadHeightMap(int map) throws IOException {
        InputStream is = this.getClass().getResourceAsStream("maps/" + names[map] + "/height.png");
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedImage image = ImageIO.read(bis);
        
        for (int y = 0; y < 1024; y++) {
            for (int x = 0; x < 1024; x++) {
                int offset = (1023 - y) * 1024 + x;
                heightMap[offset] = (byte)(image.getRGB(x,y) & 0xFF);
            }
        }
        System.err.println("Loaded Height Map");
    }
    
    private void loadTextureMap(int map) throws IOException {
        InputStream is = this.getClass().getResourceAsStream("maps/" + names[map] + "/texture.png");
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedImage image = ImageIO.read(bis);
        
        for (int y = 0; y < 1024; y++) {
            for (int x = 0; x < 1024; x++) {
                int offset = (1023 - y) * 1024 + x;
                textureMap[offset] = image.getRGB(x,y);
            }
        }
        System.err.println("Loaded Texture Map ");
    }

}
