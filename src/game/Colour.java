package game;
public class Colour
{

    /*Each single color value represent an rgb color normalized to 5 */

    public static int get(int colour1, int colour2, int colour3, int colour4)
    {
        return (get(colour4) << 24) + (get(colour3) << 16) + (get(colour2) << 8) + get(colour1);
    }

    private static int get(int colour)
    {
        if(colour < 0) return 255;
        int red = colour/100%10;
        int green = colour/10%10;
        int blue = colour%10;
        return red * 36 + green * 6 + blue;
    }

    static
    {
        Colour.get(555,543,542,123);
    }
}
