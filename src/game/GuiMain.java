package game;
public class GuiMain extends Gui
{
    private int choice = 0;
    private long lastPressed = 0;

    public GuiMain(Game game, InputHandler input, int width, int height)
    {
        super(game, input, width, height);
    }


    @Override
    public void tick()
    {
        long now = System.currentTimeMillis();

        if(input.up.isPressed())
        {
            if (now - lastPressed > 100)
            {
                switch (choice)
                {
                    case 0:
                        choice = 3;
                        break;
                    case 1:
                        choice = 0;
                        break;
                    case 2:
                        choice = 1;
                        break;
                    case 3:
                        choice = 2;
                        break;
                }
            }

            lastPressed = now;
        }

        if(input.down.isPressed())
        {
            if (now - lastPressed > 100)
            {
                switch (choice) {
                    case 0:
                        choice = 1;
                        break;
                    case 1:
                        choice = 2;
                        break;
                    case 2:
                        choice = 3;
                        break;
                    case 3:
                        choice = 0;
                        break;
                }
            }

            lastPressed = now;
        }

        if(input.enter.isPressed())
        {
            int mutants = 0;

            switch (choice)
            {
                case 0:
                    mutants = 9;
                    break;
                case 1:
                    mutants = 16;
                    break;
                case 2:
                    mutants = 25;
                    break;
                case 3:
                    mutants = 36;
                    break;
            }

            game.N_MUTANTS = mutants;
            game.restart();
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
        FontRenderer.drawCenteredString("CHOOSE DIFFICULTY", this, xPos , yPos, 555, 1);
        FontRenderer.drawCenteredString("Easy", this, xPos, yPos + 30, 555, 1);
        FontRenderer.drawCenteredString("Medium", this, xPos, yPos + 40, 555, 1);
        FontRenderer.drawCenteredString("Hard", this, xPos, yPos + 50, 555, 1);
        FontRenderer.drawCenteredString("Survivor", this, xPos, yPos + 60, 555, 1);

        int colorSelection = 333;

        drawImage("/background2.png", 0, 69, false);

        switch (choice)
        {
            case 0 :
                //FontRenderer.drawCenteredString(arrow, this, xPos - 30, yPos + 30, colorSelection, 2);
                drawImage("/gun.png", xPos - 55, yPos + 30, false);
                FontRenderer.drawCenteredString("Easy", this, xPos, yPos + 30, colorSelection, 1);
                break;
            case 1 :
                //FontRenderer.drawCenteredString(arrow, this, xPos - 30, yPos + 40, colorSelection, 2);
                drawImage("/gun.png", xPos - 55, yPos + 40, false);
                FontRenderer.drawCenteredString("Medium", this, xPos, yPos + 40, colorSelection, 1);
                break;
            case 2 :
                //FontRenderer.drawCenteredString(arrow, this, xPos - 30, yPos + 50, colorSelection, 2);
                drawImage("/gun.png", xPos - 55, yPos + 50, false);
                FontRenderer.drawCenteredString("Hard", this, xPos, yPos + 50, colorSelection, 1);
                break;
            case 3 :
                //FontRenderer.drawCenteredString(arrow, this, xPos - 30, yPos + 60, colorSelection, 2);
                drawImage("/gun.png", xPos - 55, yPos + 60, false);
                FontRenderer.drawCenteredString("Survivor", this, xPos, yPos + 60, colorSelection, 1);
                break;
        }
    }

}
