package game;
public abstract class Tile
{

    public static final Tile[] tiles = new Tile[256];
    public static final Tile VOID = new BasicSolidTile(0,0,0,Colour.get(000,-1,-1,-1), 0xFF000000);
    public static final Tile STONE = new BasicSolidTile(1,1,0,Colour.get(-1,333,-1,-1), 0xFF555555);
    public static final Tile GRASS = new BasicTile(2,2,0,Colour.get(-1,131,141,-1), 0xFF00FF00);
    public static final Tile WATER = new AnimatedTile(3, new int[][]{{0,5},{1,5},{2,5},{1,5}}, Colour.get(-1, 004, 115, -1), 0xFF0000FF, 1000);
    public static final Tile SAND = new BasicTile(4,3,0,Colour.get(-1,541,531,-1), 0xFFEFC314);

    protected byte id;
    protected boolean solid;
    protected boolean emitter;
    private int levelColour;

    public Tile(int id, boolean isSolid, boolean isEmitter, int colour)
    {
        this.id = (byte) id;
        if (tiles[id] != null) throw new RuntimeException("Duplicate tile id on " + id);
        this.solid = isSolid;
        this.emitter = isEmitter;
        this.levelColour = colour;
        tiles[id] = this;
    }

    public byte getId()
    {
        return id;
    }

    public boolean isSolid()
    {
        return this.solid;
    }

    public boolean isEmitter()
    {
        return this.emitter;
    }

    public abstract void render(Screen screen, Level level, int x, int y);

    public abstract void tick();

    public int getLevelColour()
    {
        return this.levelColour;
    }
}
