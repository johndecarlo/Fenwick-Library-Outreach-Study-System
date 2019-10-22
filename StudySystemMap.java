/*
   StudySystemMap.java
   CS 321 - Section 001: Team 7
   John DeCarlo, Huiying Jin, John Radecki, Joshua Yuen
   ----------------------------------------------------
   Description: Interactive map for the user that allows users
   to select table and input information about what they are studying for
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

public class StudySystemMap extends JPanel implements MouseListener, MouseMotionListener {

   private ArrayList<Table> floor1Tables = new ArrayList<Table>();   //List of tables on floor 1
   private ArrayList<Table> floor2Tables = new ArrayList<Table>();   //List of tables on floor 2
   private ArrayList<Table> floor3Tables = new ArrayList<Table>();   //List of tables on floor 3
         
   private ImageIcon floor1 = new ImageIcon("Fenwick_Library_Maps/Floor1.png");  //Image for the map of floor 1
   private ImageIcon floor2 = new ImageIcon("Fenwick_Library_Maps/Floor2.png");  //Image for the map of floor 2
   private ImageIcon floor3 = new ImageIcon("Fenwick_Library_Maps/Floor3.png");  //Image for the map of floor 3
   private ImageIcon searchIcon = new ImageIcon("search_icon.png");     //Icon for the search button
   private ImageIcon profileIcon = new ImageIcon("profile_icon.png");   //Icon for the profile button
   private ImageIcon blockTable = new ImageIcon("Occupied_Tables/green_block_occupied.png");    //Block table display
   private ImageIcon circleTable = new ImageIcon("Occupied_Tables/green_circle_occupied.png");  //Circle table display
   private ImageIcon smallCircleTable_h = new ImageIcon("Occupied_Tables/green_small_circle_occupied.png");    //Small circle table (horizontal)
   private ImageIcon smallCircleTable_v = new ImageIcon("Occupied_Tables/green_smallcircle_occupied_v.png");   //Small cirlce talbe (vertical)
   
   private static int floorNumber;     //Floor number that the user is currently viewing 
   protected static int mouseX;			//location for the mouse pointer X
   protected static int mouseY;        //location for the mouse pointer Y

   //Basic constructor for the StudySystemMap w/ no parameters
   public StudySystemMap() throws IOException {
      floorNumber = 2;                    //Initialize the floor number
      initializeTables();                 //Initialize the table arrays
      addMouseListener( this );           //Initalize mouseListener
      addMouseMotionListener( this );     //Initalize mouseMotionListener
      mouseX = 0;                         //Set X location of the mouse
      mouseY = 0;                         //Set Y location of the mouse
      
      //Tester to test if data is implemented
      floor1Tables.get(0).setOccupied();
      floor1Tables.get(0).setStudentName("John Jones");
      floor1Tables.get(0).setCourse(new Class("Accounting", "ACCT", 203, "courseTitle"));
   }
   
   //Set the floor number that the user is currently viewing
   public void setFloorNumber(int num) {
      this.floorNumber = num;
   }
   
    //Mouse ActionListener
   public void mouseClicked( MouseEvent e ) {
      int button = e.getButton();  
      Table table = null;
      int startRow, startCol, endRow, endCol;
      if(button == MouseEvent.BUTTON1) {
         if(floorNumber == 1) {
            for(int ind1 = 0; ind1 < floor1Tables.size(); ind1++) {
               table = floor1Tables.get(ind1);  //Create a temporary table
               startRow = table.getRow();       //Get the starting row
               startCol = table.getCol();       //Get the starting column
               endRow = startRow + table.getRowSize();   //Get the end row
               endCol = startCol + table.getColSize();   //Get the end col
               if(mouseX >= startRow && mouseX <= endRow && mouseY >= startCol && mouseY <= endCol) {    //If it is between the start and end row and the start and end column
                  floor1Tables.get(ind1).setOccupied();           //Switch the table from occupied to non-occupied 
               }
            }
         } else if(floorNumber == 2) {
            for(int ind2 = 0; ind2 < floor2Tables.size(); ind2++) {
               table = floor2Tables.get(ind2);  //Create a temporary table
               startRow = table.getRow();       //Get the starting row
               startCol = table.getCol();       //Get the starting column
               endRow = startRow + table.getRowSize();   //Get the end row
               endCol = startCol + table.getColSize();   //Get the end col
               if(mouseX >= startRow && mouseX <= endRow) {    //If it is between the start and end row and the start and end column
                  floor2Tables.get(ind2).setOccupied();           //Switch the table from occupied to non-occupied 
               }
            }
         } else if(floorNumber == 3) {
            for(int ind3 = 0; ind3 < floor3Tables.size(); ind3++) {
               table = floor3Tables.get(ind3);  //Create a temporary table
               startRow = table.getRow();       //Get the starting row
               startCol = table.getCol();       //Get the starting column
               endRow = startRow + table.getRowSize();   //Get the end row
               endCol = startCol + table.getColSize();   //Get the end col
               if(mouseX >= startRow && mouseX <= endRow) {    //If it is between the start and end row and the start and end column
                  floor3Tables.get(ind3).setOccupied();           //Switch the table from occupied to non-occupied 
               }
            }
         }
         repaint();
      }  
   }
    
   private class Listener implements ActionListener {
      public void actionPerformed(ActionEvent e)	{
         repaint();
      }
   }

   public void mousePressed( MouseEvent e )
   {}

   public void mouseReleased( MouseEvent e )
   {}

   public void mouseEntered( MouseEvent e )
   {}
   
   //When we move the mouse, we can see information of tables that our cursor is hovering over
   public void mouseMoved( MouseEvent e) {
      int startRow, startCol, endRow, endCol;
      Table table = null;
      
      mouseX = e.getX();	//Get mouseX value
      mouseY = e.getY();	//Get mouseY value
      if(floorNumber == 1) {
         for(int ind1 = 0; ind1 < floor1Tables.size(); ind1++) {
            table = floor1Tables.get(ind1);  //Create a temporary table
            startRow = table.getRow();       //Get the starting row
            startCol = table.getCol();       //Get the starting column
            endRow = startRow + table.getRowSize();   //Get the end row
            endCol = startCol + table.getColSize();   //Get the end col
            if(mouseX >= startRow && mouseX <= endRow && mouseY >= startCol && mouseY <= endCol) {    //If it is between the start and end row and the start and end column
               FLOSSDriver.displayStudentName(table.getStudentName());
               if(table.getCourse().getNumber() != 0)
                  FLOSSDriver.displayTableCourse(table.getCourse());
               else
                  FLOSSDriver.resetTableCourse();  
               break;
            } else {
               FLOSSDriver.displayStudentName("");
               FLOSSDriver.resetTableCourse();  
            }  
         }
      }
      else if(floorNumber == 2) {
         for(int ind2 = 0; ind2 < floor2Tables.size(); ind2++) {
            table = floor2Tables.get(ind2);  //Create a temporary table
            startRow = table.getRow();       //Get the starting row
            startCol = table.getCol();       //Get the starting column
            endRow = startRow + table.getRowSize();   //Get the end row
            endCol = startCol + table.getColSize();   //Get the end col
            if(mouseX >= startRow && mouseX <= endRow && mouseY >= startCol && mouseY <= endCol) {    //If it is between the start and end row and the start and end column
               FLOSSDriver.displayStudentName(table.getStudentName());
               if(table.getCourse().getNumber() != 0)
                  FLOSSDriver.displayTableCourse(table.getCourse());
               else
                  FLOSSDriver.resetTableCourse();  
               break;
            } else {
               FLOSSDriver.displayStudentName("");
               FLOSSDriver.resetTableCourse();  
            }  
         }
      }
      else if(floorNumber == 3) {
         for(int ind3 = 0; ind3 < floor3Tables.size(); ind3++) {
            table = floor3Tables.get(ind3);  //Create a temporary table
            startRow = table.getRow();       //Get the starting row
            startCol = table.getCol();       //Get the starting column
            endRow = startRow + table.getRowSize();   //Get the end row
            endCol = startCol + table.getColSize();   //Get the end col
            if(mouseX >= startRow && mouseX <= endRow && mouseY >= startCol && mouseY <= endCol) {    //If it is between the start and end row and the start and end column
               FLOSSDriver.displayStudentName(table.getStudentName());
               if(table.getCourse().getNumber() != 0)
                  FLOSSDriver.displayTableCourse(table.getCourse());
               else
                  FLOSSDriver.resetTableCourse();  
               break;
            } else {
               FLOSSDriver.displayStudentName("");
               FLOSSDriver.resetTableCourse();  
            }  
         }
      }
      repaint();			//Refresh the screen
   }

   public void mouseDragged( MouseEvent e) {
      mouseX = e.getX();	//Get mouseX value
      mouseY = e.getY();	//Get mouseY value
      repaint();			//Refresh the screen
   }

   public void mouseExited( MouseEvent e )
   {}
   
   public void paintComponent(Graphics g) {
      g.setColor(Color.red);     //Set the background color to no pieces as red
      super.paintComponent(g); 	//Call super method
      paintBoard(g, floorNumber);
   }
   
   public void paintBoard(Graphics g, int floor) {
      //Display floor 1 if the user is viewing that floor
      if(floor == 1) {
         g.drawImage(floor1.getImage(), 0, 0, 800, 800, null);
         for(int f1 = 0; f1 < floor1Tables.size(); f1++) {
            if(floor1Tables.get(f1).getOccupied() == true) {
               paintOccupiedTable(g, floor1Tables.get(f1));
            }
         }
      //Display floor 2 if the user is viewing that floor
      } else if(floor == 2){
         g.drawImage(floor2.getImage(), 0, 0, 800, 800, null);
         for(int f2 = 0; f2 < floor2Tables.size(); f2++) {
            if(floor2Tables.get(f2).getOccupied() == true) {
               paintOccupiedTable(g, floor2Tables.get(f2));
            }
         }
      //Display floor 3 if the user is viewing that floor
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
   
   //Paint the table if it is being occupied by the user
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
   
   //Read in the tables.txt file and set the information for all the tables that a student can sit at
   public void initializeTables() throws IOException {
      File file = new File("dataFiles/tables.txt");
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