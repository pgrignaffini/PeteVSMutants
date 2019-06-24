package game;
import java.awt.*;

public class Bullet extends Entity
{
    private int colour = Colour.get(-1, 111, 530, 540);
    public int movingDir;
    public Rectangle bounds;
    private int speed = 4;

    public Bullet(Level level, int x, int y, int movingDir)
    {
        super(level);
        this.id = -2;
        this.movingDir = movingDir;
        this.x = x;
        this.y = y;
        this.width = 4;
        this.height = 4;
        this.bounds = new Rectangle(x,y,this.width,this.height);
    }


    public void move()
    {
        int step_size = 1;

        //0 up, 1 down, 2 left, 3 right
        switch (movingDir)
        {
            case 0 : this.y -= step_size * speed; break;
            case 1 : this.y += step_size * speed; break;
            case 2 : this.x -= step_size * speed; break;
            case 3 : this.x += step_size * speed; break;
        }

        if (x < 0 || y < 0 || x > Game.WIDTH * 5|| y > Game.HEIGHT * 5) this.toRemove = true;

        updateBounds(x,y);
    }


    public void updateBounds(int x, int y)
    {
        Rectangle newBounds = new Rectangle(x,y, 8,8);
        this.bounds = newBounds;
    }

    @Override
    public Rectangle getBounds()
    {
        return this.bounds;
    }

    @Override
    public void render(Screen screen)
    {

        int xTile = 0;
        int yTile = 26;
        int mirrorDir = 0x00;
        int scale = 1;

        int modifier = 8 * scale;
        int xOffset = x - modifier / 2;
        int yOffset = y - modifier / 2 - 4;

        //0 up, 1 down, 2 left, 3 right
        switch (movingDir)
        {
            case 0 : xTile = 1; break;
            case 1 : xTile = 1; mirrorDir = 0x02; break;
            case 2 : mirrorDir = 0x01; break;
            case 3 : break;
        }

        screen.render(xOffset, yOffset, xTile + yTile * 32, colour, mirrorDir, scale);
    }

    @Override
    public boolean hasCollidedWithTile(int xa, int ya) {
        return false;
    }

    @Override
    public boolean hasCollided(int xa, int ya) {
        return this.hasCollided;
    }


    @Override
    public void tick()
    {
        level.check_collisions(this, 0, 0);
        move();
     
    }
}
