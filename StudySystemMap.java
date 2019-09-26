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

   public StudySystemMap() {
   
   }
   
   public void paintComponent(Graphics g) {
      g.setColor(Color.red);     //Set the background color to no pieces as red
      super.paintComponent(g); 	//Call super method
      paintBoard(g);
   }
   
   public void paintBoard(Graphics g) {
      g.drawImage(floor1.getImage(), 0, 0, 700, 700, null);
   }
}