package game;
public class GuiSuccess extends Gui
{
    private int choice = 0;

    public GuiSuccess(Game game, InputHandler input, int width, int height)
    {
        super(game, input, width, height);
    }


    @Override
    public void tick()
    {
        if(input.left.isPressed())
        {
            choice = 0;
        }

        if(input.right.isPressed())
        {
            choice = 1;
        }
        
        if(input.down.isPressed())
        {
            choice = 2;
        }

        if(input.enter.isPressed())
        {
            switch (choice)
            {
                case 0:
                    game.restart();
                    break;
                case 1:
                    game.currentGui = new GuiMain(this.game, new InputHandler(this.game), this.width, this.height);
                    break;
                case 2:
                	Game.NEXT = true;
                	break;
            }

        }

    }

    @Override
    public void render(Screen screen)
    {
        int xPos = width / 2 + 2;
        int yPos = 10;
        //String arrow = ">";

        this.drawDefaultBackground(DEFAULT_COLOR);
        this.drawRect(0, 0, this.width, this.height, DEFAULT_COLOR);
        FontRenderer.drawCenteredString("SUCCESS!", this, xPos , yPos, 555, 2);
        FontRenderer.drawCenteredString("Play again", this, xPos - 40, yPos + 70, 555, 1);
        FontRenderer.drawCenteredString("Main menu", this, xPos + 50, yPos + 70, 555, 1);
        

        int colorSelection = 333;

        switch (choice)
        {
            case 0 :
                //FontRenderer.drawCenteredString(arrow, this, xPos - 70, yPos + 100, colorSelection, 2);
                drawImage("/gun.png", xPos - 100, yPos + 85, false);
                FontRenderer.drawCenteredString("Play again", this, xPos - 40, yPos + 70, colorSelection, 1);
                break;

            case 1 :
               // FontRenderer.drawCenteredString(arrow, this, xPos + 20, yPos + 100, colorSelection, 2);
                drawImage("/gun.png", xPos - 10, yPos + 85, false);
                FontRenderer.drawCenteredString("Main menu", this, xPos + 50, yPos + 70, colorSelection, 1);
                break;
        }

        drawImage("/cup.png", this.width / 2, (this.height / 2) - 10, true);
    }

}
