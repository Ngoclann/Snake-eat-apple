
import dal.PlayerDAO;
import entity.Player;
import entity.GameObject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ngọc Lan
 */
public class GamePanel extends JPanel implements Runnable, KeyListener {

    Player p = new Player();
    PlayerDAO db = new PlayerDAO();
    GameFrame gameframe;

    ImageIcon iconGameover = new ImageIcon("image\\gameover.png");
    ImageIcon iconBackground = new ImageIcon("image\\background.png");
    private Clip clip;
    BufferedImage image;
    private Graphics graphic;

    public static final int WIDTHSCREEN = 400;
    public static final int HEIGHTSCREEN = 400;
    private final int SIZE = 10;

    private boolean up, down, right, left;
    private boolean gameover = false;
    private boolean running;
    private long waitTime;
    private int score;
    private int level;
    private int dx, dy;

    GameObject head;
    GameObject apple;
    ArrayList<GameObject> mushroom = new ArrayList<>();
    ArrayList<GameObject> snake = new ArrayList<>();

    public GamePanel() {
        initComponents();
    }

    private void initComponents() {
        setPreferredSize(new Dimension(WIDTHSCREEN, HEIGHTSCREEN));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        Thread t = new Thread(this);
        t.start();
    }

    public void setApple() {
        int x = (int) (Math.random() * (WIDTHSCREEN - SIZE));
        int y = (int) (Math.random() * (HEIGHTSCREEN - SIZE));
        x = x - (x % SIZE);
        y = y - (y % SIZE);
        apple.setPosition(x, y);
        
    }

    public void setMushroom() {
        for (int i = 0; i < 25; i++) {
            int x = (int) (Math.random() * (WIDTHSCREEN - SIZE));
            int y = (int) (Math.random() * (HEIGHTSCREEN - SIZE));
            x = x - (x % SIZE);
            y = y - (y % SIZE); 
            if(x != apple.getX() && y != apple.getY()){
                GameObject obj = new GameObject(SIZE);
                obj.setPosition(x, y);
                mushroom.add(obj);
            }
        }
    }

    public void setUplevel() {
        head = new GameObject(SIZE);
        head.setPosition(WIDTHSCREEN / 2, HEIGHTSCREEN / 2);
        snake.add(head);
        for (int i = 1; i < 3; i++) {
            GameObject obj = new GameObject(SIZE);
            obj.setPosition(head.getX() + (i * SIZE), head.getY());
            snake.add(obj);
        }
        apple = new GameObject(SIZE);
        setApple();
        setMushroom();
        score = 0;
        gameover = false;
        level = 1;
        dx = dy = 0;
    }

    public void update() {
        if (up && dy == 0) {
            dy = -SIZE;
            dx = 0;
        }
        if (down && dy == 0) {
            dy = SIZE;
            dx = 0;
        }
        if (left && dx == 0) {
            dy = 0;
            dx = -SIZE;
        }
        if (right && dx == 0 && dy != 0) {
            dy = 0;
            dx = SIZE;
        }
        if (dx != 0 || dy != 0) {
            for (int i = snake.size() - 1; i > 0; i--) {
                snake.get(i).setPosition(snake.get(i - 1).getX(), snake.get(i - 1).getY());
            }
            head.move(dx, dy);
        }

        for (GameObject snk : snake) {
            for (GameObject msr : mushroom) {
                if (snk.isCollision(head) || msr.isCollision(head)) {
                    playGameOverSound();
                    gameover = true;
                    break;
                }
            }
        }

        if (apple.isCollision(head)) {
            playEatAppleSound();
            score++;
            setApple();
            GameObject e = new GameObject(SIZE);
            e.setPosition(-100, -100);
            snake.add(e);
            if (score % 10 == 0) {
                level++;
            }
        }

        if (head.getX() < 0) {
            head.setX(WIDTHSCREEN);
        }
        if (head.getY() < 0) {
            head.setY(HEIGHTSCREEN);
        }
        if (head.getX() > WIDTHSCREEN) {
            head.setX(0);
        }
        if (head.getY() > HEIGHTSCREEN) {
            head.setY(0);
        }
    }

    @Override
    public void run() {
        image = new BufferedImage(WIDTHSCREEN, HEIGHTSCREEN, BufferedImage.TYPE_INT_ARGB);
        graphic = image.getGraphics();

        running = true;
        setUplevel();

        while (running) {

            update();
            requestRender();

            waitTime = 1000 / (level * 10);  //waitTime(millis); 1s=1000millis
            if (waitTime > 0) {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                }
            }

            if (gameover) {
                GameOverDialog endgame = new GameOverDialog(gameframe, true);
                endgame.setVisible(true);

                java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
                p.setName(endgame.txtPlayerName.getText());
                p.setDate(date);
                p.setScore(score);
                db.insert(p);
                break;
            }
        }
    }

    public void requestRender() {
        render(graphic);
        Graphics e = getGraphics();
        e.drawImage(image, 0, 0, null);
        e.dispose();
    }

    public void render(Graphics g) {
        g.drawImage(iconBackground.getImage(), 0, 0, null);
        g.setColor(Color.magenta);
        for (GameObject msr : mushroom) {
            msr.render(g);
        }

        g.setColor(Color.red);
        apple.render(g);

        if (gameover) {
            g.drawImage(iconGameover.getImage(), 100, 100, null);
        }
        g.setColor(Color.yellow);
        if (dx == 0 && dy == 0) {
            g.drawString("Don't eat pink mushroom~", 100, 150);
            g.drawString("Ready? Press any arrow key (except ->) to start!", 100, 300);
        }
        
        g.setColor(Color.white);
        g.drawString("Score: " + score + " Level: " + level, 10, 10);
        for (GameObject snk : snake) {
            snk.render(g);
        }
    }

    public void playGameOverSound() {
        try {
            String filepath = "sound\\gameover.wav";
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
        }
    }

    public void playEatAppleSound() {
        try {
            String filepath = "sound\\eatapple.wav";
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

}
