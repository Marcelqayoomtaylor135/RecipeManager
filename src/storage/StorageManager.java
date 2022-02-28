/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Pantry;
import model.PantryItem;
import model.User;
import org.sqlite.SQLiteConfig;

public class StorageManager {
    
    
    
    // Make a connection to the db to use it
    public Connection connect(){
        
        String dbUrl = "jdbc:sqlite:C://sqlite/userdata.db";  
        
        // SQLite connection string  
        
        Connection conn = null;  
        
        try {  
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            conn = DriverManager.getConnection(dbUrl, config.toProperties());
        } catch (SQLException e) {  
            e.printStackTrace();
        }  
        return conn;      
        
    }
    
    public static void createNewDatabase(String fileName){
        
        String url = "jdbc:sqlite:C:/sqlite/" + fileName;  
        Connection conn = null;
   
        try {  
            conn = DriverManager.getConnection(url);  
            if (conn != null) {  
                DatabaseMetaData meta = conn.getMetaData();  
                System.out.println("The driver name is " + meta.getDriverName());  
                System.out.println("A new database has been created.");  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        } finally {
            try {
                conn.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        } 
        
    }
    
    public boolean fileExists (String fileName) throws SQLException{
                
        File tempFile = new File("C:/sqlite/" + fileName);
        return tempFile.isFile();
        
    }
    
    public static void createNewTable(String tableName, String query, String fileName) throws SQLException{
        
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/" + fileName;
        
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n" + " " + query; 
        Connection conn = null;
        Statement stmt = null;
        
        try {
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Table created successfully");
        } catch (SQLException e){
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e){
                e.printStackTrace();
                throw new SQLException(e.getMessage());
            }
        } 
        
    }
    
    public void insert(User user, String tableName) throws SQLException{
        
        System.out.println("user being inserted");
        
        String sql = "INSERT INTO " + tableName + "(username, PantryID) VALUES(?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        
        try {
            conn = this.connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setInt(2, getNumberOfRows(tableName)+1); //UPDATE: not sure if this is right
            pstmt.executeUpdate();
            System.out.println("Added " + user.getUsername() + " to database");
            
        } catch(SQLException e){
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        } 
    
    }
    
    public void insertPantry(User user, String tableName){
        
        System.out.println("pantry being inserted");
        
        String sql = "INSERT INTO " + tableName + "(name) VALUES(?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = this.connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername()+"'s Pantry");
            pstmt.executeUpdate();
            System.out.println("Added " + user.getUsername() + " to database");
        } catch(SQLException e){
            System.out.println(e.getMessage());
        } finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }     
        
    }
    

    public void insertIngredient(PantryItem ingredient, Pantry pantry){
        
        String sql = "INSERT INTO ingredients (name, PantryID) VALUES(?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = this.connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ingredient.getName());
            pstmt.setInt(2, getPantryKey(pantry));
            pstmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        } 
        
    }
    
    public void deductIngredient(PantryItem ingredient){
        
        String sql = "DELETE FROM ingredients WHERE name = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = this.connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ingredient.getName());
            pstmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        } 
        
    }
    
    public boolean userExists(String username) throws SQLException{
        
       String sql = "SELECT * FROM users "
                   + "WHERE username = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;
        
        try {
            conn = this.connect();
            pstmt  = conn.prepareStatement(sql);
            System.out.println("Username sent: " + username);
            pstmt.setString(1,username);
            rs  = pstmt.executeQuery();
            if (rs.next()){ 
                System.out.println("USER DOES EXIST");
                exists = true; 
            } else {
                System.out.println("USER DOESNT EXIST");
            }
            return exists;
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("catch 1");
            return exists;
        } finally {
            try {
                pstmt.close();
                rs.close();
                conn.close();
                return exists;
            } catch (SQLException e){
                e.printStackTrace();
                System.out.println("catch 2");
                return exists;
            }
        }    
        
    }
    
    public ResultSet getUserRow(String username) throws SQLException{
        
        String sql = "SELECT UserID, username, PantryID FROM users "
                   + "WHERE username = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = this.connect();
            pstmt  = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            rs  = pstmt.executeQuery();
            System.out.println("RESULTS: " + rs.next());
            return rs;  
        } catch (SQLException e){
            e.printStackTrace();
            return rs;
        } finally {
            try {
                pstmt.close();
                rs.close();
                conn.close();
            } catch (SQLException e){
                e.printStackTrace();
                return rs;
            }
        }   
        
    }

    // temp change to return int from rs for testing
    public int getPantryKey(Pantry pantry) throws SQLException{
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int index = 0;
        
        try {
            String sql = "SELECT * FROM pantries" + 
                         " WHERE name = ?";
            conn = this.connect();
            pstmt = conn.prepareStatement(sql);
            System.out.println("NAME: " + pantry.getName());
            pstmt.setString(1,pantry.getName());
            rs = pstmt.executeQuery();
            System.out.println("RESULTS:" + rs.next());
            System.out.println(rs.getInt(1));
            index = rs.getInt(1);
            return index;        
        } catch (SQLException e) {
            e.printStackTrace();
            return index;
        } finally {
            try {                
                pstmt.close();
                rs.close();
                conn.close();
                return index;
            } catch (SQLException e){
                e.printStackTrace();
                return index;
            }
        }  
                       
    }
    
    public int getUserKey(String username) throws SQLException{ // remove conn
 
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = null;
        int index = 0;
        
        try {
            String sql = "SELECT * FROM users "
            + "WHERE username = ?";
            conn = this.connect();
            pstmt  = conn.prepareStatement(sql);
            // set the value
            pstmt.setString(1,username);
            rs  = pstmt.executeQuery();
            System.out.println("USER KEY RESULTS: " + rs.next());
            index = rs.getInt("UserID"); // UPDATE: Could be sending empty rs, might be getting more than one and crashing
            return index;
        } catch (SQLException e){
            e.printStackTrace();
            return index;
        } finally {
            try {
                pstmt.close();
                rs.close();
                conn.close();
                return index;
            } catch (SQLException e){
                e.printStackTrace();
                return index;
            } 
        }
        
    }
    
    public void load(Pantry pantry, int key){
    
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
                        
        try {
            String sql = "SELECT * FROM ingredients" + 
             " WHERE PantryID = ?";
            conn = this.connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, key);
            rs = pstmt.executeQuery();
            
            System.out.println("INGREDIENTS EXIST: " + rs.next());
            
            if (rs.next()){
                pantry.add(rs.getString("name"));
                while(rs.next()){
                    pantry.add(rs.getString("name"));
                }
            }
            
            // Adds the very first row, then while there are rows remaining, add the rest

            
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
    }
    

    public void closeDb(Connection conn) throws SQLException{
        
        conn.close();
        
    }
    
    public int getNumberOfRows(String tableName){
        
        Connection conn = this.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int size = 0;
        
        try {
            String sql = "SELECT count(1) from " + tableName; 
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            size = rs.getInt(1);
            System.out.println("You have " + size + " number of rows");
            return size;
        } catch(SQLException e){
            e.printStackTrace();
            return size;
        } finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
                return size;
            } catch (SQLException e){
                e.printStackTrace();
                return size;
            }
        }
        
    }
    
}
