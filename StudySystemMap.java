/*
   FLOSSDriver.java
   CS 321 - Section 001: Team 7
   John DeCarlo, Huiying Jin, John Radecki, Joshua Yuen
   ----------------------------------------------------
   Description: This is the executable driver for our software
*/

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Color;
import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StudySystemMap extends JPanel {

   private ArrayList<Table> floor1Tables = new ArrayList<Table>();
   private ArrayList<Table> floor2Tables = new ArrayList<Table>();
   private ArrayList<Table> floor3Tables = new ArrayList<Table>();
         
   private ImageIcon floor1 = new ImageIcon("Fenwick_Library_Maps/Floor1.png");
   private ImageIcon floor2 = new ImageIcon("Fenwick_Library_Maps/Floor2.png");
   private ImageIcon floor3 = new ImageIcon("Fenwick_Library_Maps/Floor3.png");
   private ImageIcon searchIcon = new ImageIcon("search_icon.png");
   private ImageIcon profileIcon = new ImageIcon("profile_icon.png");
   private ImageIcon blockTable = new ImageIcon("Occupied_Tables/green_block_occupied.png");
   private ImageIcon circleTable = new ImageIcon("Occupied_Tables/green_circle_occupied.png");
   private ImageIcon smallCircleTable_h = new ImageIcon("Occupied_Tables/green_small_circle_occupied.png");  
   private ImageIcon smallCircleTable_v = new ImageIcon("Occupied_Tables/green_smallcircle_occupied_v.png");
   private ImageIcon seat = new ImageIcon("Occupied_Tables/green_seat_occupied.png");
   
   private static int floorNumber;

   public StudySystemMap() throws IOException {
      floorNumber = 1;
      initializeTables();
   }
   
   public void test() {
      for(int i = 0; i < floor1Tables.size(); i++) {
         if(floor1Tables.get(i).getOccupied() == false)
            floor1Tables.get(i).setOccupied(true);
         else
            floor1Tables.get(i).setOccupied(false);
      }
   }
   
   public void setFloorNumber(int num) {
      this.floorNumber = num;
   }
   
   public void paintComponent(Graphics g) {
      g.setColor(Color.red);     //Set the background color to no pieces as red
      super.paintComponent(g); 	//Call super method
      paintBoard(g, floorNumber);
   }
   
   public void paintBoard(Graphics g, int floor) {
      if(floor == 1) {
         g.drawImage(floor1.getImage(), 0, 0, 800, 800, null);
         for(int f1 = 0; f1 < floor1Tables.size(); f1++) {
            if(floor1Tables.get(f1).getOccupied() == true) {
               paintOccupiedTable(g, floor1Tables.get(f1));
            }
         }
      } else if(floor == 2){
         g.drawImage(floor2.getImage(), 0, 0, 800, 800, null);
         for(int f2 = 0; f2 < floor2Tables.size(); f2++) {
            if(floor2Tables.get(f2).getOccupied() == true) {
               paintOccupiedTable(g, floor2Tables.get(f2));
            }
         }
      } else if(floor == 3){
         g.drawImage(floor3.getImage(), 0, 0, 800, 800, null);
         for(int f3 = 0; f3 < floor3Tables.size(); f3++) {
            if(floor3Tables.get(f3).getOccupied() == true) {
               paintOccupiedTable(g, floor3Tables.get(f3));
            }
         }
      } else
         g.drawImage(floor1.getImage(), 0, 0, 800, 800, null);
   }
   
   public void paintOccupiedTable(Graphics g, Table t) {
      if(t.getShape().equals("BLOCK")) 
         g.drawImage(blockTable.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
      else if(t.getShape().equals("CIRCLE")) 
         g.drawImage(circleTable.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
      else if(t.getShape().equals("SMALLCIRCLE_H")) 
         g.drawImage(smallCircleTable_h.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
      else if(t.getShape().equals("SMALLCIRCLE_V")) 
         g.drawImage(smallCircleTable_v.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
   }
   
   public void initializeTables() throws IOException {
      File file = new File("tables.txt");
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line = "";
      while ((line = reader.readLine()) != null) {
         String[] table = line.split(",");
         if(table[0].equals("1"))
            floor1Tables.add(new Table(Integer.parseInt(table[0]), Integer.parseInt(table[1]), Integer.parseInt(table[2]), Integer.parseInt(table[3]), Integer.parseInt(table[4]), table[5]));
         else if(table[0].equals("2"))
            floor2Tables.add(new Table(Integer.parseInt(table[0]), Integer.parseInt(table[1]), Integer.parseInt(table[2]), Integer.parseInt(table[3]), Integer.parseInt(table[4]), table[5]));
         else if(table[0].equals("3"))
            floor3Tables.add(new Table(Integer.parseInt(table[0]), Integer.parseInt(table[1]), Integer.parseInt(table[2]), Integer.parseInt(table[3]), Integer.parseInt(table[4]), table[5]));
      }
      reader.close();
   }
}