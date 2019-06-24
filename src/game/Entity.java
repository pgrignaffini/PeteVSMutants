package game;
import java.awt.*;

public abstract class Entity
{
    public int x;
    public int y;
    protected int width;
    protected int height;
    public int id = -1;
    public boolean toRemove = false;
    public boolean hasCollided = false;

    protected Level level;

    public Entity(Level level)
    {
        init(level);
    }

    public final void init(Level level)
    {
        this.level = level;
    }

    public abstract void tick();
    public abstract void render(Screen screen);

    public abstract boolean hasCollided(int xa, int ya);
    public abstract boolean hasCollidedWithTile(int xa, int ya);

    public abstract Rectangle getBounds();
    public abstract void updateBounds(int x, int y);

}
