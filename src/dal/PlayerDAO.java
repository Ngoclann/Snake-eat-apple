/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import entity.Player;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ngọc Lan
 */
public class PlayerDAO extends BaseDAO {
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        String sql = "SELECT TOP (25) [name]\n" +
                        "      ,[date]\n" +
                        "      ,[score]\n" +
                        "  FROM [Player].[dbo].[Player]\n" + 
                        "ORDER BY [score] desc";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Player p = new Player();
                p.setName(rs.getString("name"));
                p.setDate(rs.getDate("date"));
                p.setScore(rs.getInt("score"));
                players.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return players;
    }
    
    public void insert(Player p) {
        String sql = "INSERT INTO [Player]\n"
                   + " (name,date,score)\n"
                   + "VALUES \n"
                   + "(?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, p.getName());
            statement.setDate(2, (Date) p.getDate());
            statement.setInt(3, p.getScore());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
