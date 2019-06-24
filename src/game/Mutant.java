package game;
import java.awt.*;

public class Mutant extends Mob
{
    private int colour;
    private int scale = 1;
    private int tickCount = 0;
    private int counter = 0;
    private int tickToDeath = 0;
    public int type; //0 = bad, 1 = good
    private Player player;
    public Rectangle bounds;
    private Rectangle detectionBounds;
    protected boolean isDrowning = false;
    protected boolean isSwimming = false;

    public Mutant(Level level, int x, int y, int speed, Player player, int id, int type)
    {
        super(level, "MUTANT", x, y, speed);
        this.player = player;
        this.bounds = new Rectangle(x,y, this.width, this.height);
        this.detectionBounds = new Rectangle(x, y, this.width * 8, this.height * 8);
        this.id = id;
        this.type = type;

        switch (this.type)
        {
            case 0 : this.colour = Colour.get(-1, 111, 200, 80); break;
            case 1 : this.colour = Colour.get(-1, 111, 200, 543); break;
        }
    }

    public Rectangle getBounds()
    {
        return this.bounds;
    }

    public Rectangle getDetectionBounds()
    {
        return this.detectionBounds;
    }

    public void updateBounds(int x, int y)
    {
        Rectangle newBounds = new Rectangle(x,y, width,height);
        this.bounds = newBounds;
        newBounds = new Rectangle(x,y,width * 8, height * 8);
        this.detectionBounds = newBounds;
    }


    public boolean hasCollided(int xa, int ya)
    {
        return this.hasCollided;
    }

    @Override
    public boolean hasCollidedWithTile(int xa, int ya) {

        //coordinates of collision box inside the sprite
        int xMin = 0;
        int xMax = 7;
        int yMin = 3;
        int yMax = 7;

        this.hasCollided = false;

        for(int x = xMin; x < xMax; x++)
        {
            if (isSolidTile(xa,ya,x,yMin)) return true;
        }

        for(int x = xMin; x < xMax; x++)
        {
            if (isSolidTile(xa,ya,x,yMax)) return true;
        }

        for(int y = yMin; y < yMax; y++)
        {
            if (isSolidTile(xa,ya,xMin,y)) return true;
        }

        for(int y = yMin; y < yMax; y++)
        {
            if (isSolidTile(xa,ya,xMax,y)) return true;
        }

        return false;
    }



    @Override
    public void tick()
    {
        tickCount++;
        counter++;
        counter = counter%80;

        if (isDrowning && tickCount > tickToDeath)
        {
            tickToDeath += 15;

            if (tickToDeath > tickCount)
            {
                isDrowning = false;
                isDead = true;
                tickToDeath = 0;
            }
        }

        if (isDead && tickCount > tickToDeath){
            tickToDeath += 15;
            if (tickToDeath > tickCount) this.toRemove = true;
            return; }

        int xa = 0;
        int ya = 0;

        if (player.getBounds().intersects(detectionBounds)) //chase the player
        {
            if (x < player.x) xa++;
            if (y > player.y) ya--;
            if (x > player.x) xa--;
            if (y < player.y) ya++;
        }

        else //move in circle
        {
            if (counter < 20) ya--;
            if (counter >= 20 && counter < 40) xa++;
            if (counter >= 40 && counter < 60) ya++;
            if (counter >= 60 && counter < 80) xa--;
        }

        System.out.println(this.counter);

        if (xa != 0 || ya != 0)
        {
            move(xa,ya);
            isMoving = true;
        }

        else
        {
            isMoving = false;
        }

        if (type == 0) isDrowning = (level.getTile((this.x >> 3), (this.y >> 3)).getId() == 3);
        else isSwimming = (level.getTile((this.x >> 3), (this.y >> 3)).getId() == 3);

    }

    public void renderName(Screen screen, int xOffset, int yOffset)
    {
        if (id != -1)
        {
            Font.render(Integer.toString(id), screen, xOffset, yOffset - 10, Colour.get(-1,-1,-1,555), 1);
        }
    }

    @Override
    public void render(Screen screen) {

        int xTile = 0;
        int yTile = 24;
        int walkingSpeed = 4;
        int flipTop = (numSteps >> walkingSpeed) & 1;
        int flipBottom = (numSteps >> walkingSpeed) & 1;

        int modifier = 8 * scale;
        int xOffset = x - modifier / 2;
        int yOffset = y - modifier / 2 - 4;

        if (movingDir == 1)
        {
            xTile += 2;
        }

        else if (movingDir > 1)
        {
            xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
            flipTop = (movingDir - 1) % 2;
        }

        if (isDead)
        {
            xTile = 8;
            flipBottom = flipTop = 0x00;
            screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, colour, flipTop, scale); //upper body
            screen.render(xOffset + modifier - (modifier * flipTop), yOffset, xTile + 1 + yTile * 32, colour, flipTop, scale);
            screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, colour, flipBottom, scale); //lower body
            screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, colour, flipBottom, scale);
        }

        else if (isDrowning || isSwimming)
        {
            int waterColour = 0;
            yOffset += 4;

            if (tickCount % 60 < 15)
            {
                waterColour = Colour.get(-1,-1,255,-1);
            }

            else if (15 <= tickCount%60 && tickCount % 60 < 30)
            {
                yOffset -= 1;
                waterColour = Colour.get(-1,225,115,-1);
            }

            else if (30 <= tickCount % 60 && tickCount % 60 < 45)
            {
                yOffset -= 1;
                waterColour = Colour.get(-1,115,-1,225);
            }

            else waterColour = Colour.get(-1,225,115,-1);

            screen.render(xOffset, yOffset + 3, 27 * 32, waterColour, 0x00, 1);
            screen.render(xOffset + 8, yOffset + 3, 27 * 32, waterColour, 0x01, 1);
            screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, colour, flipTop, scale); //upper body
            screen.render(xOffset + modifier - (modifier * flipTop), yOffset, xTile + 1 + yTile * 32, colour, flipTop, scale);
        }

        else
        {
            screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, colour, flipTop, scale); //upper body
            screen.render(xOffset + modifier - (modifier * flipTop), yOffset, xTile + 1 + yTile * 32, colour, flipTop, scale);
            screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, colour, flipBottom, scale); //lower body
            screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, colour, flipBottom, scale);
        }

        //renderName(screen, xOffset, yOffset);
    }
}
