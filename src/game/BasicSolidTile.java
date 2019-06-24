package game;
public class BasicSolidTile extends BasicTile {

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        super.render(screen, level, x, y);

    }

    public BasicSolidTile(int id, int x, int y, int tileColour, int levelColour) {
        super(id, x, y, tileColour, levelColour);
        this.solid = true;
    }
}
