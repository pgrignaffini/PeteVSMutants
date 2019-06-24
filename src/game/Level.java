package game;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Level
{
    private byte[] tiles;
    public int width;
    public int height;
    public List<Entity> entities = new ArrayList<Entity>();
    private String imagePath;
    private BufferedImage image;
    public static boolean GameOver = false;
    public static boolean Success = false;
    public static int GOOD_MUTANTS;
    public static int BAD_MUTANTS;


    public Level(String imagePath)
    {
        if (imagePath != null)
        {
            this.imagePath = imagePath;
            this.loadLevelfromFile();
        }

        else {
            tiles = new byte[height * width];
            this.width = 64;
            this.height = 64;
            this.generateLevel();
        }
    }

    private void loadLevelfromFile()
    {
        try
        {
            this.image = ImageIO.read(Level.class.getResource(this.imagePath));
            this.width = image.getWidth();
            this.height = image.getHeight();
            tiles = new byte[width * height];
            this.loadTiles();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void loadTiles()
    {
        int[] tileColours = this.image.getRGB(0,0, width, height, null, 0, width); //translate image data into int
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
               tileCheck : for (Tile t : Tile.tiles)
                {
                    if (t != null && t.getLevelColour() == tileColours[x + y * width])
                    {
                        this.tiles[x + y * width] = t.getId();
                        break tileCheck;
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void saveLeveltoFile()
    {
        try
        {
            ImageIO.write(image, "png", new File(Level.class.getResource(imagePath).getFile()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void alterTile(int x, int y, Tile newTile)
    {
        this.tiles[x + y * width] = newTile.getId();
        image.setRGB(x,y,newTile.getLevelColour());

    }

    public void generateLevel()
    {
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (x*y % 10 < 7) {
                    tiles[x + y * width] = Tile.GRASS.getId();
                }

                else
                {
                    tiles[x + y * width] = Tile.STONE.getId();
                }
            }
        }

    }

    private  void remove(Entity e)
    {
        this.entities.remove(e);
    }

    protected  void add(Entity e)
    {
        this.entities.add(e);
    }

    public void tick()
    {

        int badMutantCounter = 0;
        int goodMutantCounter = 0;
        int mutantCounter = 0;

        for (Entity e : new ArrayList<>(entities))
        {
            if (e.toRemove) remove(e);
        }

        for (Entity e : new ArrayList<>(entities))
        {
            if (e instanceof Mutant)
            {
                if (((Mutant) e).type == 1) goodMutantCounter++;
                else badMutantCounter++;
                mutantCounter++;
            }
            e.tick();
        }

        for (Tile t : Tile.tiles)
        {
            if (t == null) break;
            else t.tick();
        }

        if (goodMutantCounter == mutantCounter) Success = true;
        if (mutantCounter == 0) Success = true;
        GOOD_MUTANTS = goodMutantCounter;
        BAD_MUTANTS = badMutantCounter;
    }

    public void renderTiles(Screen screen, int xOffset, int yOffset)
    {
        if (xOffset < 0) xOffset = 0;
        if (xOffset > ((width << 3) - screen.width)) xOffset = ((width << 3) - screen.width);
        if (yOffset < 0) yOffset = 0;
        if (yOffset > ((height << 3) - screen.height)) yOffset = ((height << 3) - screen.height);

        screen.setOffset(xOffset, yOffset);

        for (int y = (yOffset >> 3); y < (yOffset + screen.height >> 3) + 1; y++)
        {
            for (int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++)
            {
                getTile(x,y).render(screen, this, x << 3, y << 3);
            }
        }

    }

    public void renderEntities(Screen screen)
    {
        for (Entity e : entities)
        {
            e.render(screen);
        }
    }

    public Tile getTile(int x, int y)
    {
        if (x < 0 || x >= width || y < 0 || y >= height) return Tile.VOID;
        return Tile.tiles[tiles[x+y*width]];

    }

    public void addEntity(Entity entity)
    {
        this.entities.add(entity);
    }

    public void addEntities(ArrayList<Entity> entities)
    {
        for (Entity entity : entities)
        {
            this.entities.add(entity);
        }
    }

    public void check_collisions(Entity entity, int xa, int ya)
    {

        Rectangle nextBounds = new Rectangle((int)entity.getBounds().getX() + xa, (int)entity.getBounds().getY() + ya, 16, 16);

            for (Entity entity1 : entities)
            {
                if (!entity.equals(entity1))
                {
                    if (nextBounds.intersects(entity1.getBounds()))
                    {
                        System.out.println(entity + " collision with " + entity1);

                        if((entity instanceof Bullet && entity1 instanceof Bullet)) //two bullets collided
                        {
                            entity.toRemove = true;
                            entity1.toRemove = true;
                        }

                        if(entity instanceof Mutant && entity1 instanceof Player)//player collided with mutant
                        {
                            if(!((Mutant) entity).isDead)
                            {
                                ((Player) entity1).isDead = true;
                                GameOver = true;
                            }

                        }

                        if(entity1 instanceof Mutant && entity instanceof Player)//player collided with mutant
                        {
                            if(!((Mutant) entity1).isDead)
                            {
                                ((Player) entity).isDead = true;
                                GameOver = true;
                            }
                        }


                        if((entity instanceof Bullet && entity1 instanceof Mutant)) //bullets collided with mutants
                        {
                            if (((Mutant) entity1).type == 1)
                            {
                                GameOver = true;
                                break;
                            }

                            entity.toRemove = true;
                            ((Mutant) entity1).isDead = true;
                            entity1.updateBounds(-10,-10);
                        }

                        else if(entity1 instanceof Bullet && entity instanceof Mutant)
                        {
                            if (((Mutant) entity).type == 1)
                            {
                                GameOver = true;
                                break;
                            }

                            entity1.toRemove = true;
                            ((Mutant) entity).isDead = true;
                            entity.updateBounds(-10,-10);
                        }

                        else
                        {
                            entity.hasCollided = true;
                            entity1.hasCollided = true;
                        }

                       return;
                    }

                    else
                        {
                        entity.hasCollided = false;
                        entity1.hasCollided = false;
                    }
                }
                //System.out.println(entities.get(j).hasCollided);
            }

    }
}
