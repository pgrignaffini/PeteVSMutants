package game;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Gui
{
    protected Game game;
    public InputHandler input;
    public int[] pixels;
    public int width;
    public int height;
    protected static int WHITE = Colour.get(-1,-1,-1,-1);
    protected static int BLACK = Colour.get(0,0,0,0);
    protected static int DEFAULT_COLOR = Colour.get(125, 100 , 300, -1);
    protected static int SKY = Colour.get(-1, 114 , 115, -1);
    protected BufferedImage image;

    public Gui(Game game, InputHandler input, int width, int height)
    {
        this.game = game;
        this.input = input;
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height] ;
    }

    public abstract void render(Screen screen);
    public abstract void tick();

    public void drawDefaultBackground(int DEFAULT_COLOR)
    {
        for (int i = 0; i < pixels.length; i++)
        {
            pixels[i] = DEFAULT_COLOR;
        }
    }

    public void drawRect(int xPos, int yPos, int width, int height, int color)
    {
        if (xPos > this.width)
            xPos = this.width - 1;
        if (yPos > this.height)
            yPos = this.height - 1;
        if (xPos + width > this.width)
            width = this.width - xPos;
        if (yPos + height > this.height)
            height = this.height - yPos;
        width -= 1;
        height -= 1;
        for (int x = xPos; x < xPos + width; x++) {
            pixels[x + yPos * this.width] = color;
        }
        for (int y = yPos; y < yPos + height; y++) {
            pixels[xPos + y * this.width] = color;
        }
        for (int x = xPos; x < xPos + width; x++) {
            pixels[x + (yPos + height) * this.width] = color;
        }
        for (int y = yPos; y < yPos + height; y++) {
            pixels[(xPos + width) + y * this.width] = color;
        }
    }

    public void drawImage(String imagePath, int xOffset, int yOffset, boolean centered)
    {
        try
        {
            image = ImageIO.read(GuiStart.class.getResource(imagePath));
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        int w = image.getWidth();
        int h = image.getHeight();
        int imgOffsetX = 0;
        int imgOffsetY = 0;

        if(centered)
        {
            imgOffsetX = w / 2;
            imgOffsetY = h / 2;
        }


        for (int row = 0; row < h; row++)
        {
            for(int col = 0; col < w; col++)
            {
                Color c = new Color(image.getRGB(col, row), true);
                if (c.getAlpha() > 128) pixels[(row + yOffset - imgOffsetY) * this.width + (col + xOffset - imgOffsetX)] = c.getRGB();
            }
        }
    }

}
