
import java.awt.Dimension;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ngọc Lan
 */
public class GameFrame extends JFrame {

    GamePanel gp = new GamePanel();
    
    
    public GameFrame() {
        initComponents();
    }

    private void initComponents() {
        this.setTitle("Snake Xenzia");
        this.setContentPane(gp);
        this.setResizable(false);
        this.pack();
        this.setPreferredSize(new Dimension(GamePanel.WIDTHSCREEN, GamePanel.HEIGHTSCREEN));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    
}
