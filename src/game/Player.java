package game;
import java.awt.*;

public class Player extends Mob
{
    private InputHandler input;
    private int colour = Colour.get(-1, 111, 321, 543);
    private int scale = 1;
    protected boolean isSwimming = false;
    protected boolean isShooting = false;
    private int tickCount = 0;
    //private String username;
    public Rectangle bounds;
    private int bullets;

    public Player(Level level, int x, int y, InputHandler input, String username, int bullets)
    {
        super(level, "Player", x, y, 2);
        this.input = input;
        //this.username = username;
        this.bounds = new Rectangle(x,y, this.width, this.height);
        this.bullets = bullets;
    }

    public Rectangle getBounds()
    {
        return this.bounds;
    }

    public void updateBounds(int x, int y)
    {
        Rectangle newBounds = new Rectangle(x,y, width, height);
        this.bounds = newBounds;
    }

    public int getBullets()
    {
        return this.bullets;
    }

    @Override
    public boolean hasCollided(int xa, int ya)
    {
        return this.hasCollided;
    }

    @Override
    public boolean hasCollidedWithTile(int xa, int ya)
    {

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

    private long lastShoot = 0;

    @Override
    public void tick()
    {

        int xa = 0;
        int ya = 0;

        long now = System.currentTimeMillis();

        if(input.up.isPressed()) ya--;
        if(input.down.isPressed()) ya++;
        if(input.left.isPressed()) xa--;
        if(input.right.isPressed()) xa++;

        if(input.spaceBar.isPressed())
        {
            if (now - lastShoot > 300)
            {
                if (!isSwimming)
                {
                    if (bullets > 0)
                    {
                        shootBullet(movingDir);
                        isShooting = true;
                        lastShoot = now;
                        bullets--;
                    }
                }

            }
        }

        else isShooting = false;

        if (xa != 0 || ya != 0)
        {
            move(xa,ya);
            isMoving = true;
        }

        else
        {
            isMoving = false;
        }

        if(level.getTile((this.x >> 3), (this.y >> 3)).getId() == 3)
        {
            isSwimming = true;
        }

        if (isSwimming && level.getTile((this.x >> 3), (this.y >> 3)).getId() != 3)
        {
            isSwimming = false;
        }

        tickCount++;
    }

    public void shootBullet(int movingDir)
    {
        int xBullet = 0;
        int yBullet = 0;
        switch (movingDir)
        {
            case 0 : yBullet = y - 12; xBullet = x + 4; break;
            case 1 : yBullet = y + 17; xBullet = x + 4; break;
            case 2 : xBullet = x - 12; yBullet = y + 5; break;
            case 3 : xBullet = x + 17; yBullet = y + 5; break;
        }

        Bullet b = new Bullet(level, xBullet, yBullet, movingDir);
        level.addEntity(b);
    }

    @Override
    public void render(Screen screen) {

        int xTile = 0;
        int yTile = 21;

        int walkingSpeed = 4;
        int flipTop = (numSteps >> walkingSpeed) & 1;
        int flipBottom = (numSteps >> walkingSpeed) & 1;

        if (movingDir == 1)
        {
            xTile += 2;
        }

        else if (movingDir > 1)
        {
            xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
            flipTop = (movingDir - 1) % 2;
            flipBottom = (movingDir - 1) % 2;
        }

        if (isShooting)
        {
            yTile = 19;
        }

        int modifier = 8 * scale;
        int xOffset = x - modifier / 2;
        int yOffset = y - modifier / 2 - 4;

        if(isSwimming)
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

            screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColour, 0x00, 1);
            screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColour, 0x01, 1);
        }

        screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, colour, flipTop, scale); //upper body
        screen.render(xOffset + modifier - (modifier * flipTop), yOffset, xTile + 1 + yTile * 32, colour, flipTop, scale);

        if (!isSwimming)
        {
            if (isShooting)
            {
                screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, colour, flipBottom, scale); //lower body
                screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, colour, flipBottom, scale);
            }

            else
            {
                screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, colour, flipBottom, scale); //lower body
                screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, colour, flipBottom, scale);

            }
        }

        /*

        if (username != null)
        {
            Font.render(username, screen, xOffset - ((username.length() - 1) / 2 * 8), yOffset - 10, Colour.get(-1,-1,-1,555), 1);
        }

        */

    }
}
