public class Util {
    
    public static int packColor(int red, int green, int blue) {
        //red = (red <<16 ) & 0x00FF0000;
        //green = (green << 8) & 0x0000FF00;
        //blue = blue & 0xFF;
        //return (0xFF000000 | red | green | blue);
        
        return ( 0xFF000000 | ((red <<16 ) & 0x00FF0000) | ((green << 8) & 0x0000FF00) | (blue = blue & 0xFF) ) ;
    }

    
    /*
     * Blend two RGB colors using a blend factor
     *
     * 0 => return color 1
     * n => return color2*(n/256) + color1*(256-n)/256
     * 256 => return color 2
     *
     * From: http://www.java-gaming.org/topics/fastest-rgb-24bit-color-blend-and-others/21434/msg/175034/view.html#msg175034
     */
    public static int blendColor(int c1, int c2, int factor) {
        return ( ( ( (c1&0xFF00FF)*(256-factor) + (c2&0xFF00FF)*factor )  &0xFF00FF00)  | (( (c1&0x00FF00)*(256-factor) + (c2&0x00FF00)*factor)  &0x00FF0000  )   ) >>>8;
    }
    
    public static int gray(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b =  rgb & 0xFF;
        
        int gray = ((int)(r * 0.2125 + g * 0.7154 + b * 0.0721)) & 0xFF; //BT709 values
        
        return ( 0xFF000000 | (gray << 16) | (gray << 8) | gray );
        
    }

}
