package game;


public class GuiStart extends Gui
{
    public GuiStart(Game game, InputHandler input, int width, int height)
    {
        super(game, input, width, height);
    }


    @Override
    public void drawRect(int xPos, int yPos, int width, int height, int color)
    {
        super.drawRect(xPos, yPos, width, height, color);
    }

    @Override
    public void drawDefaultBackground(int DEFAULT_COLOR)
    {
        super.drawDefaultBackground(DEFAULT_COLOR);

    }

    @Override
    public void tick()
    {
        if (input.enter.isPressed())
            game.currentGui = new GuiMain(this.game, new InputHandler(this.game), this.width, this.height);
    }

    private long now = 0;

    @Override
    public void render(Screen screen)
    {
        this.drawDefaultBackground(DEFAULT_COLOR);
        drawImage("/petevsmutants.png", 0, 0, false);
        drawImage("/pete.png", 50, 30, false);
        drawImage("/background.png", 0, 69, false);

        int xPos = width / 2 + 2;
        int yPos = height / 2 + 20;

        long lastBlink = System.currentTimeMillis();

        if (lastBlink - now > 500)
        {
            FontRenderer.drawCenteredString("Press ENTER", this, xPos , yPos - 20, BLACK, 1);
            if (lastBlink - now > 1000) now = lastBlink + 500;
        }

    }

}
