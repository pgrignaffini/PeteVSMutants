package game;
import java.awt.*;

public abstract class Mob extends Entity
{

    protected String name;
    protected int speed;
    protected int numSteps = 0;
    protected boolean isMoving;
    protected int movingDir = 1; //0 up, 1 down, 2 left, 3 right
    protected int scale = 1;
    protected boolean isDead = false;


    public Mob(Level level, String name, int x, int y, int speed)
    {
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = 10;
        this.height = 12;
        this.speed = speed;

    }

    public abstract Rectangle getBounds();
    public abstract void updateBounds(int x, int y);

    public void move(int xa, int ya)
    {

        if (xa != 0 && ya != 0) //handle diagonal moves
        {
            move(xa, 0);
            move(0, ya);
            numSteps--;
            return;
        }

        level.check_collisions(this, xa, ya);
        numSteps++;

        if(!hasCollided(xa, ya) && !hasCollidedWithTile(xa,ya))
        {
            if (ya < 0) movingDir = 0;
            if (ya > 0) movingDir = 1;
            if (xa < 0) movingDir = 2;
            if (xa > 0) movingDir = 3;

            this.x += xa * speed;
            this.y += ya * speed;

            updateBounds(this.x, this.y);
        }
    }

    protected boolean isSolidTile(int xa, int ya, int x, int y)
    {
        if (level == null) return false;
        Tile lastTile = level.getTile((this.x + x) >> 3, ((this.y+y))>>3);
        Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
        if (!lastTile.equals(newTile) && newTile.isSolid()) return true;

        return false;
    }


    public String getName()
    {
        return this.name;
    }

}
