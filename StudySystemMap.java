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

public class StudySystemMap extends JPanel {

   private ImageIcon floor1 = new ImageIcon("Fenwick_Library_Maps/Floor1.png");
   private ImageIcon floor2 = new ImageIcon("Fenwick_Library_Maps/Floor2.png");
   private ImageIcon floor3 = new ImageIcon("Fenwick_Library_Maps/Floor3.png");
   private ImageIcon searchIcon = new ImageIcon("search_icon.png");
   private ImageIcon profileIcon = new ImageIcon("profile_icon.png");
   private static int floorNumber;

   public StudySystemMap() {
      floorNumber = 1;
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
      if(floor == 1)
         g.drawImage(floor1.getImage(), 0, 0, 700, 700, null);
      else if(floor == 2)
         g.drawImage(floor2.getImage(), 0, 0, 700, 700, null);
      else if(floor == 3)
         g.drawImage(floor3.getImage(), 0, 0, 700, 700, null);
      else
         g.drawImage(floor1.getImage(), 0, 0, 700, 700, null);
   }
}