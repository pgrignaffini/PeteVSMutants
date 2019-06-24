package game;
public class GuiDead extends Gui
{
    private int choice = 0;

    public GuiDead(Game game, InputHandler input, int width, int height)
    {

        super(game, input, width, height);
    }

    @Override
    public void tick()
    {
        // 0: go to Main menu
        // 1: replay

        if(input.left.isPressed()) this.choice = 0;
        if(input.right.isPressed()) this.choice = 1;

        if (input.enter.isPressed())
        {
            switch (choice)
            {
                case 0 : game.currentGui = new GuiMain(this.game, new InputHandler(this.game), this.width, this.height); break;
                case 1 : game.restart(); break;
            }
        }

    }

    @Override
    public void render(Screen screen)
    {
        int xPos = width / 2 + 2;
        int yPos = 40;
        //String arrow = ">";

        this.drawDefaultBackground(Colour.get(0,0,0,0));
        this.drawRect(0, 0, this.width, this.height, BLACK);
        drawImage("/peteDead.png", 85, 40, false);


        FontRenderer.drawCenteredString("YOU LOST!", this, xPos, yPos - 20, 555, 2);
        FontRenderer.drawCenteredString("Main menu", this, xPos - 40, yPos + 70, 555, 1);
        FontRenderer.drawCenteredString("Play again", this, xPos + 50, yPos + 70, 555, 1);

        int colorSelection = 500;

        switch (choice)
        {
            case 0 :
               // FontRenderer.drawCenteredString(arrow, this, xCenter - 70, yCenter + 40, colorSelection, 2);
                drawImage("/gun.png", xPos - 100, yPos + 70, false);
                FontRenderer.drawCenteredString("Main menu", this, xPos - 40, yPos + 70, colorSelection, 1);
                break;
            case 1 :
               // FontRenderer.drawCenteredString(arrow, this, xPos + 10, yPos + 40, colorSelection, 2);
                drawImage("/gun.png", xPos - 10, yPos + 70, false);
                FontRenderer.drawCenteredString("Play again", this, xPos + 50, yPos + 70, colorSelection, 1);
                break;
        }

    }

}
