
import dal.PlayerDAO;
import entity.Player;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ngọc Lan
 */
public class HighScoreDialog extends java.awt.Dialog {

    /**
     * Creates new form HighScoreDialog
     * @param parent
     * @param modal
     */
    public HighScoreDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();;
        loadPlayersToTable();
    }
    
    private void loadPlayersToTable() {
        Player p = new Player();
        PlayerDAO db = new PlayerDAO();
        ArrayList<Player> players = db.getPlayers();
        String[] columns = {"Rank", "Name", "Date", "Score"};
        Object[][] rows = new Object[players.size()][columns.length];
        for (int i = 0; i < players.size(); i++) {
            rows[i][0] = i+1;
            rows[i][1] = players.get(i).getName();
            rows[i][2] = players.get(i).getDate();
            rows[i][3] = players.get(i).getScore();
        }
        DefaultTableModel model = new DefaultTableModel(rows, columns);
        tblPlayer.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblPlayer = new javax.swing.JTable();

        setSize(new java.awt.Dimension(400, 400));
        setTitle("High Score");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblPlayer.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblPlayer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblPlayer);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPlayer;
    // End of variables declaration//GEN-END:variables
}
