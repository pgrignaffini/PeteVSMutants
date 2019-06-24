package game;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game extends Canvas implements  Runnable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 200;
    public static final int HEIGHT = WIDTH / 12 * 9;
    public static final int SCALE = 2;
    public static final String NAME = "PeteVSMutants";
    public static final int WHITE = Colour.get(-1,-1,-1,555);
    public static boolean NEXT = false;
    public static String MODE = "Program";

    public int N_MUTANTS;
    public int BAD_MUTANTS_ALIVE;
    public int GOOD_MUTANTS_ALIVE;
    public int good_mutants = 0;
    private static final int MUTANTS_SPEED = 1;
    public int N_CACTUS = 16;
    public int N_BULLETS = 30 ;
    private BufferedImage zombie_icon;
    //private BufferedImage good_zombie_icon;
    private BufferedImage bullet_icon;


    private JFrame frame;

    public boolean running = false;
    public int tickCount = 0;
    private Random randomNumber = new Random();

    private Graphics gfx;
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    public int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private int[] colours = new int[6*6*6];

    private Screen screen;
    public InputHandler input;
    public Level level;
    public Player player;
    public ArrayList<Entity> mutants = new ArrayList<>();
    public Gui currentGui;
    public boolean GuiShowing = false;

    public Game()
    {
        setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        frame = new JFrame(NAME);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void clean()
    {
        this.screen = null;
        this.input = null;
        this.level = null;
        this.player = null;
        this.mutants = new ArrayList<>();
        this.currentGui = null;
        this.GuiShowing = false;
    }

    private void initMenu()
    {
        clean();
        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/esprite_sheet.png"));
        currentGui = new GuiStart(this, new InputHandler(this), WIDTH, HEIGHT);//new GuiSuccess(this, new InputHandler(this), screen.width, screen.height);
        GuiShowing = true;
    }

    private BufferedImage loadIcon(String imagePath)
    {
        BufferedImage icon = null;

        try
        {
            icon = ImageIO.read(Level.class.getResource(imagePath));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return icon;
    }

    private void init()
    {
        clean();

        int index = 0;
        for (int r = 0; r < 6; r++)
        {
            for (int g = 0; g < 6; g++)
            {
                for (int b = 0; b < 6; b++)
                {
                    int rr = (r * 255/5);
                    int gg = (g * 255/5);
                    int bb = (b * 255/5);

                    colours[index++] = rr << 16 | gg << 8 | bb;
                }
            }
        }

        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/esprite_sheet.png"));
        input = new InputHandler(this);
        level = new Level("/sand_test_level_big.png");

        int playerXPos = randomNumber.nextInt(WIDTH);
        int playerYPos = randomNumber.nextInt(HEIGHT);

        player = new Player(level, playerXPos, playerYPos, input, "Pete", N_BULLETS);

        zombie_icon = loadIcon("/zombie_icon.png");
        //good_zombie_icon = loadIcon("/good_zombie_icon.png");
        bullet_icon = loadIcon("/bullet.png");

        System.out.println("N_MUTANTS " + N_MUTANTS);

        JsonParser parser = null;

        int mutantDst = 0;

        switch (N_MUTANTS)
        {
            case 9 : mutantDst = 200;  parser = new JsonParser("res/json/mutantsEasy.json"); MODE = "Program"; break;
            case 16 : mutantDst = 150; parser = new JsonParser("res/json/mutantsMedium.json"); MODE = "Person"; break;
            case 25 : mutantDst = 100; parser = new JsonParser("res/json/mutantsHard.json"); MODE = "Program"; break;
            case 36 : mutantDst = 100;  parser = new JsonParser("res/json/mutantsSurvivor.json"); MODE = "Person"; break;
        }

        this.good_mutants = parser.getMutants("good");
        System.out.println("Good mutants: " + this.good_mutants);
        int temp_mutants = this.good_mutants;

        int id = 0;
        int mutantType;

        ArrayList<Point> mutantsPos = new ArrayList<>();

        for (int y = 0; y < Math.sqrt(N_MUTANTS); y++)
        {
            for (int x = 0; x < Math.sqrt(N_MUTANTS); x++)
            {

                int mutantX = (x * mutantDst) % (WIDTH * 4) + 1;
                int mutantY = (y * mutantDst) % (HEIGHT * 4) + 1;
                Point mutantPos = new Point(mutantX, mutantY);

                if (player.getBounds().contains(mutantPos))
                {
                    mutantX += 50;
                    mutantPos = new Point(mutantX, mutantY);
                }

                mutantsPos.add(mutantPos);
            }
        }

        Collections.shuffle(mutantsPos);

        for (int i = 0; i < N_MUTANTS; i++)
        {
            if (temp_mutants > 0)
            {
                mutantType = 1;
                temp_mutants--;
            }
            else mutantType = 0;

            id++;
            Mutant m = new Mutant(level, mutantsPos.get(i).x, mutantsPos.get(i).y, MUTANTS_SPEED, player, id, mutantType);
            mutants.add(m);
        }



        int cactusType = 0;
        int cactusDst = 100;

        for (int y = 0; y < Math.sqrt(N_CACTUS); y++)
        {
            for (int x = 0; x < Math.sqrt(N_CACTUS); x++)
            {
                cactusType = (cactusType+1)%2;
                int cactusX = (x * cactusDst) % (WIDTH * 4) + randomNumber.nextInt(80);
                int cactusY = (y * cactusDst) % (HEIGHT * 4) + randomNumber.nextInt(80);

                Point cactusPos = new Point(cactusX, cactusY);
                if (player.getBounds().contains(cactusPos))
                {
                    cactusY += 100;
                    cactusX += 100;
                }

                Cactus c = new Cactus(level, cactusX, cactusY, -3, cactusType);
                level.addEntity(c);
            }
        }

        level.addEntity(player);
        level.addEntities(mutants);
    }

    public synchronized void start()
    {
        running = true;
        new Thread(this).start();
    }

    public synchronized void restart()
    {
        running = false;
        init();
        running = true;
    }

    public synchronized void stop()
    {
        running = false;
    }

    public synchronized void continueGame()
    {
        GuiShowing = false;
        currentGui = null;
        running = true;
    }

    @Override
    public void run()
    {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D/60D; //how many nanoseconds are in one tick
        int frames = 0;
        int ticks = 0;
        long lastTimer = System.currentTimeMillis();
        double delta = 0; //how many nanoseconds have gone by so far

        initMenu();

        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while(delta >= 1)
            {
                ticks++;
                tick(); //update logic
                delta -= 1;
                shouldRender = true;
            }

            try
            {
                Thread.sleep(2);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }


            if (shouldRender)
            {
                frames++;
                render(); //update graphics
            }

            if (System.currentTimeMillis() - lastTimer > 1000)
            {
                lastTimer += 1000;
                System.out.println(ticks + "ticks, " + frames + " frames");
                frames = 0;
                ticks = 0;
            }
        }

    }

    //updates the logic of the game
    public void tick()
    {
        tickCount++;

        if(!GuiShowing && input.pause.isPressed())
        {
            currentGui = new GuiPause(this, new InputHandler(this), screen.width, screen.height);
            GuiShowing = true;
        }

        if (!GuiShowing)
        {
            level.tick();
            this.BAD_MUTANTS_ALIVE = Level.BAD_MUTANTS;
            this.GOOD_MUTANTS_ALIVE = Level.GOOD_MUTANTS;
        }

        else currentGui.tick();
    }

    //renders the logic
	public void render()
    {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null)
        {
            createBufferStrategy(3);
            return;
        }

        gfx = bs.getDrawGraphics();


        if (Level.GameOver && currentGui == null)
        {
            currentGui = new GuiDead(this, new InputHandler(this), screen.width, screen.height);
            GuiShowing = true;
            Level.GameOver = false;
        }

        if (Level.Success && currentGui == null)
        {
            currentGui = new GuiSuccess(this, new InputHandler(this), screen.width, screen.height);
            GuiShowing = true;
            Level.Success = false;
        }


        if (!GuiShowing)
        {

            int xOffset = player.x - (screen.width / 2);
            int yOffset = player.y - (screen.height / 2);

            level.renderTiles(screen, xOffset, yOffset);
            level.renderEntities(screen);

            String bad_mut = Integer.toString(BAD_MUTANTS_ALIVE);
            String bulls = Integer.toString(player.getBullets());
           // String good_mut = Integer.toString(GOOD_MUTANTS_ALIVE);

            Font.render(bad_mut, this.screen, this.screen.xOffset + 20, this.screen.yOffset + 5, WHITE, 1);
           // Font.render(good_mut, this.screen, this.screen.xOffset + 20, this.screen.yOffset + 17, WHITE, 1);
            Font.render(bulls, this.screen, this.screen.xOffset + WIDTH - 20, this.screen.yOffset + 5, WHITE, 1);

            for (int y = 0; y < screen.height; y++)
            {
                for (int x = 0; x < screen.width; x++)
                {
                    int colourCode = screen.pixels[x+y * screen.width];
                    if (colourCode < 255) pixels[x+y * WIDTH] = colours[colourCode];
                }
            }

        }


        else
        {
            currentGui.render(screen);

            for (int y = 0; y < screen.height; y++)
            {
                for (int x = 0; x < screen.width; x++)
                {
                    int colorCode = currentGui.pixels[x + y * currentGui.width];
                    pixels[x + y * WIDTH] = colorCode + (0xFF << 24);
                }
            }
        }


        gfx.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        if(!GuiShowing)
        {
            // Draw bounding boxes
            /*
            for (Entity e : new ArrayList<>(level.entities))
            {
                //if (e instanceof Mutant) gfx.drawRect((int)((Mutant) e).getDetectionBounds().getX(), (int)((Mutant) e).getDetectionBounds().getY(), e.width * 8, e.height * 8);
                gfx.drawRect((int)e.getBounds().getX(), (int)e.getBounds().getY(), e.width, e.height);
            }*/

            gfx.drawImage(zombie_icon, 10, 10, null);
            //gfx.drawImage(good_zombie_icon, 10, 60, null);
            gfx.drawImage(bullet_icon, 670, 10, null);
        }

        gfx.dispose();

        bs.show();
			
 
    }

    public static void main (String[] args)
    {
        new Game().start();
    }
}
