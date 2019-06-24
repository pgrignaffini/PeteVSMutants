package game;
import java.awt.*;

public class Cactus extends Entity{

    private int x;
    private int y;
    private int colour = Colour.get(-1, 111, 0, 80);
    private int scale = 1;
    public Rectangle bounds;
    private int type;

    public Cactus(Level level, int x, int y, int id, int type)
    {
        super(level);
        this.x = x;
        this.y = y;
        this.width = 10;

        if(type == 1) this.height = 8;
        else this.height = 12;

        this.bounds = new Rectangle(x,y, this.width, this.height);
        this.id = id;
        this.type = type;
    }

    public Rectangle getBounds()
    {
        return this.bounds;
    }

    public void updateBounds(int x, int y)
    {

    }


    public boolean hasCollided(int xa, int ya)
    {
        return this.hasCollided;
    }

    @Override
    public boolean hasCollidedWithTile(int xa, int ya) {

        return false;
    }

    @Override
    public void tick()
    {

        if(level.getTile((this.x >> 3), (this.y >> 3)).getId() == 3)
        {
            toRemove = true;
        }
    }

    @Override
    public void render(Screen screen) {

        int xTile = 0;

        if (type == 1)
        {
            xTile = 2;
            colour = Colour.get(-1, 111, 501, 80);
        }

        int yTile = 16;

        int modifier = 8 * scale;
        int xOffset = x - modifier / 2;
        int yOffset = y - modifier / 2 - 4;

        screen.render(xOffset, yOffset, xTile + yTile * 32, colour, 0x00, scale); //upper
        screen.render(xOffset + modifier, yOffset, xTile + 1 + yTile * 32, colour, 0x00, scale);
        screen.render(xOffset, yOffset + modifier, xTile + (yTile + 1) * 32, colour, 0x00, scale); //lower
        screen.render(xOffset + modifier, yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, colour, 0x00, scale);
    }
}
