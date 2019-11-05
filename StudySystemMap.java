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
         
   //Images for the floors
   private ImageIcon floor1 = new ImageIcon("Fenwick_Library_Maps/Floor1.png");  //Image for the map of floor 1
   private ImageIcon floor2 = new ImageIcon("Fenwick_Library_Maps/Floor2.png");  //Image for the map of floor 2
   private ImageIcon floor3 = new ImageIcon("Fenwick_Library_Maps/Floor3.png");  //Image for the map of floor 3
   //Images for displaying tables that have been selected
   private ImageIcon blockTable_occupied = new ImageIcon("table_images/green_block_occupied.png");                   //Block table display
   private ImageIcon circleTable_occupied = new ImageIcon("table_images/green_circle_occupied.png");                 //Circle table display
   private ImageIcon circleTable_halfLeft_occupied = new ImageIcon("table_images/green_circle_halfLeft_occupied.png");
   private ImageIcon circleTable_halfRight_occupied = new ImageIcon("table_images/green_circle_halfRight_occupied.png");
   private ImageIcon smallCircleTable_h_occupied = new ImageIcon("table_images/green_small_circle_occupied.png");    //Small circle table (horizontal)
   private ImageIcon smallCircleTable_v_occupied = new ImageIcon("table_images/green_smallcircle_occupied_v.png");   //Small cirlce talbe (vertical)
   //Images for displaying table that the user has selected
   private ImageIcon blockTable_selected = new ImageIcon("table_images/green_block_selected.png");                   //Block table display
   private ImageIcon circleTable_selected = new ImageIcon("table_images/green_circle_selected.png");                 //Circle table display
   private ImageIcon smallCircleTable_h_selected = new ImageIcon("table_images/green_small_circle_selected.png");    //Small circle table (horizontal)
   private ImageIcon smallCircleTable_v_selected = new ImageIcon("table_images/green_smallcircle_selected_v.png");   //Small cirlce talbe (vertical)
   //Images for displaying table that the user is occupying
   private ImageIcon blockTable_user = new ImageIcon("table_images/green_block_user.png");                    //Block table display
   private ImageIcon circleTable_user = new ImageIcon("table_images/green_circle_user.png");                  //Circle table display
   private ImageIcon smallCircleTable_h_user= new ImageIcon("table_images/green_small_circle_user.png");      //Small circle table (horizontal)
   private ImageIcon smallCircleTable_v_user = new ImageIcon("table_images/green_smallcircle_user_v.png");    //Small cirlce talbe (vertical)
   //Images for displaying tables that match with what the user is searching for
   private ImageIcon blockTable_match = new ImageIcon("table_images/green_block_match.png");                   //Block table display
   private ImageIcon circleTable_match = new ImageIcon("table_images/green_circle_match.png");                 //Circle table display
   private ImageIcon smallCircleTable_h_match = new ImageIcon("table_images/green_small_circle_match.png");    //Small circle table (horizontal)
   private ImageIcon smallCircleTable_v_match = new ImageIcon("table_images/green_smallcircle_match_v.png");   //Small cirlce talbe (vertical)
   
   private static int floorNumber;     //Floor number that the user is currently viewing 
   protected static int mouseX;			//location for the mouse pointer X
   protected static int mouseY;        //location for the mouse pointer Y
   
   private static Table userTable;        //Table that the user is currently occupying
   private static Table selectedTable;    //Table that the user has selected
   private static boolean tableSelected;  //Whether or not we have selected a table
   private static boolean searchFeature;  //Whether or not the search feature is active

   //Basic constructor for the StudySystemMap w/ no parameters
   public StudySystemMap() throws IOException {
      floorNumber = 1;                    //Initialize the floor number
      initializeTables();                 //Initialize the table arrays
      addMouseListener( this );           //Initalize mouseListener
      addMouseMotionListener( this );     //Initalize mouseMotionListener
      mouseX = 0;                         //Set X location of the mouse
      mouseY = 0;                         //Set Y location of the mouse
      selectedTable = new Table();        //Initialize the table we are selected
      userTable = null;                   //Initialze the user table
      tableSelected = false;              //Initialize whether or not we have selected a table
      searchFeature = false;              //Initialize whether or not the search feature is active
   }
   
   //Set the floor number that the user is currently viewing
   public void setFloorNumber(int num) {
      this.floorNumber = num;
   }
   
   //Get the table that we have selected
   public Table getSelectedTable() {
      return this.selectedTable;
   }
   
   //Set our selectedTable variable
   public void setSelectedTable(Table table) {
      this.selectedTable = table;
   }
   
      //Get the table that we have selected
   public Table getUserTable() {
      return this.userTable;
   }
   
   //Set our selectedTable variable
   public void setUserTable(Table table) {
      this.userTable = table;
   }
   
   //Set our tableSelected variable
   public void setTableSelected(boolean tableSelected) {
      this.tableSelected = tableSelected;
   }
   
   //Set our search feature variable
   public void setSearchEnabled(boolean searchFeature) {
      this.searchFeature = searchFeature;
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
               if(mouseX >= startRow && mouseX <= endRow && mouseY >= startCol && mouseY <= endCol && table.getOccupied() == false) {    //If it is between the start and end row and the start and end column
                  selectedTable = table;
                  tableSelected = true;
                  FLOSSDriver.displayCourseOptions(true);
                  break;
               } else {
                  FLOSSDriver.displayCourseOptions(false);
                  tableSelected = false;
               }
            }
         } else if(floorNumber == 2) {
            for(int ind2 = 0; ind2 < floor2Tables.size(); ind2++) {
               table = floor2Tables.get(ind2);  //Create a temporary table
               startRow = table.getRow();       //Get the starting row
               startCol = table.getCol();       //Get the starting column
               endRow = startRow + table.getRowSize();   //Get the end row
               endCol = startCol + table.getColSize();   //Get the end col
               if(mouseX >= startRow && mouseX <= endRow && mouseY >= startCol && mouseY <= endCol && table.getOccupied() == false) {    //If it is between the start and end row and the start and end column
                  selectedTable = table;
                  tableSelected = true;
                  FLOSSDriver.displayCourseOptions(true);
                  break;
               } else {
                  FLOSSDriver.displayCourseOptions(false);
                  tableSelected = false;
               }
            }
         } else if(floorNumber == 3) {
            for(int ind3 = 0; ind3 < floor3Tables.size(); ind3++) {
               table = floor3Tables.get(ind3);  //Create a temporary table
               startRow = table.getRow();       //Get the starting row
               startCol = table.getCol();       //Get the starting column
               endRow = startRow + table.getRowSize();   //Get the end row
               endCol = startCol + table.getColSize();   //Get the end col
               if(mouseX >= startRow && mouseX <= endRow && mouseY >= startCol && mouseY <= endCol && table.getOccupied() == false) {    //If it is between the start and end row and the start and end column
                  selectedTable = table;
                  tableSelected = true;
                  FLOSSDriver.displayCourseOptions(true);
                  break;
               } else {
                  FLOSSDriver.displayCourseOptions(false);
                  tableSelected = false;
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
               FLOSSDriver.displayMessage(table.getMessage());
               if(table.getCourse().getNumber() != 0)
                  FLOSSDriver.displayTableCourse(table.getCourse());
               else
                  FLOSSDriver.resetTableCourse();  
               break;
            } else {
               resetMessages();
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
               resetMessages();
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
               resetMessages();
            }  
         }
      }
      repaint();			//Refresh the screen
   }

   //When we drag the mouse across the board
   public void mouseDragged( MouseEvent e) {
      mouseX = e.getX();	//Get mouseX value
      mouseY = e.getY();	//Get mouseY value
      repaint();			//Refresh the screen
   }
   
   //Mouse Exited
   public void mouseExited( MouseEvent e )
   {}
   
   //Call all the paint methods for displaying the image
   public void paintComponent(Graphics g) {
      g.setColor(Color.red);     //Set the background color to no pieces as red
      super.paintComponent(g); 	//Call super method
      paintMap(g, floorNumber);
      paintSelectedTable(g);
      paintUserTable(g);
      paintMatchingTable(g);
   }
   
   //Paint out the image of the floors for Fenwick
   public void paintMap(Graphics g, int floor) {
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
         g.drawImage(blockTable_occupied.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
      else if(t.getShape().equals("CIRCLE")) 
         g.drawImage(circleTable_occupied.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
      else if(t.getShape().equals("CIRCLE_HL")) 
         g.drawImage(circleTable_halfLeft_occupied.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
      else if(t.getShape().equals("CIRCLE_HR")) 
         g.drawImage(circleTable_halfRight_occupied.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
      else if(t.getShape().equals("SMALLCIRCLE_H")) 
         g.drawImage(smallCircleTable_h_occupied.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
      else if(t.getShape().equals("SMALLCIRCLE_V")) 
         g.drawImage(smallCircleTable_v_occupied.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
   }
   
   //Paint the table if the user has selected it
   public void paintSelectedTable(Graphics g) {
      if(tableSelected == true) {
         if(selectedTable.getShape().equals("BLOCK")) 
            g.drawImage(blockTable_selected.getImage(), selectedTable.getRow(), selectedTable.getCol(), selectedTable.getRowSize(), selectedTable.getColSize(), null); 
         else if(selectedTable.getShape().equals("CIRCLE")) 
            g.drawImage(circleTable_selected.getImage(), selectedTable.getRow(), selectedTable.getCol(), selectedTable.getRowSize(), selectedTable.getColSize(), null); 
         else if(selectedTable.getShape().equals("SMALLCIRCLE_H")) 
            g.drawImage(smallCircleTable_h_selected.getImage(), selectedTable.getRow(), selectedTable.getCol(), selectedTable.getRowSize(), selectedTable.getColSize(), null); 
         else if(selectedTable.getShape().equals("SMALLCIRCLE_V")) 
            g.drawImage(smallCircleTable_v_selected.getImage(), selectedTable.getRow(), selectedTable.getCol(), selectedTable.getRowSize(), selectedTable.getColSize(), null);
      } 
   }
   
   //Paint the table that the user is currently occupying
   public void paintUserTable(Graphics g) {
      if(userTable != null) {
         if(userTable.getShape().equals("BLOCK") && userTable.getFloor() == floorNumber) 
            g.drawImage(blockTable_user.getImage(), userTable.getRow(), userTable.getCol(), userTable.getRowSize(), userTable.getColSize(), null); 
         else if(userTable.getShape().equals("CIRCLE") && userTable.getFloor() == floorNumber) 
            g.drawImage(circleTable_user.getImage(), userTable.getRow(), userTable.getCol(), userTable.getRowSize(), userTable.getColSize(), null); 
         else if(userTable.getShape().equals("SMALLCIRCLE_H") && userTable.getFloor() == floorNumber) 
            g.drawImage(smallCircleTable_h_user.getImage(), userTable.getRow(), userTable.getCol(), userTable.getRowSize(), userTable.getColSize(), null); 
         else if(userTable.getShape().equals("SMALLCIRCLE_V") && userTable.getFloor() == floorNumber) 
            g.drawImage(smallCircleTable_v_user.getImage(), userTable.getRow(), userTable.getCol(), userTable.getRowSize(), userTable.getColSize(), null);
      } 
   }
   
   //Paint the table that matches with the users search feature
   public void paintMatchingTable(Graphics g) {
      if(searchFeature == true) {
         ArrayList<Table> tables;
         if(floorNumber == 1)
            tables = floor1Tables;
         else if (floorNumber == 2)
            tables = floor2Tables;
         else if (floorNumber == 3)
            tables = floor3Tables;
         else
            tables = floor1Tables;
         String subjectCode = FLOSSDriver.getSearchSubject();
         int courseNumber = FLOSSDriver.getSearchNumber();
         for(int index = 0; index < tables.size(); index++) {
            Table table = tables.get(index);
            if(table.getShape().equals("BLOCK") && table.getCourse().getSubjectCode().equals(subjectCode) && table.getCourse().getNumber() == courseNumber && table.getID() != userTable.getID()) 
               g.drawImage(blockTable_match.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null); 
            else if(table.getShape().equals("CIRCLE") && table.getCourse().getSubjectCode().equals(subjectCode) && table.getCourse().getNumber() == courseNumber) 
               g.drawImage(circleTable_match.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null); 
            else if(table.getShape().equals("SMALLCIRCLE_H") && table.getCourse().getSubjectCode().equals(subjectCode) && table.getCourse().getNumber() == courseNumber) 
               g.drawImage(smallCircleTable_h_match.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null);  
            else if(table.getShape().equals("SMALLCIRCLE_V") && table.getCourse().getSubjectCode().equals(subjectCode) && table.getCourse().getNumber() == courseNumber) 
               g.drawImage(smallCircleTable_v_match.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null); 
         }
      } 
   }
   
   //Read in the tables.txt file and set the information for all the tables that a student can sit at
   public void initializeTables() throws IOException {
      File file = new File("dataFiles/tables.txt");
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line = "";
      int id_count = 0;
      while ((line = reader.readLine()) != null) {
         String[] table = line.split(",");
         if(table[0].equals("1"))
            floor1Tables.add(new Table(Integer.parseInt(table[0]), Integer.parseInt(table[1]), Integer.parseInt(table[2]), Integer.parseInt(table[3]), Integer.parseInt(table[4]), table[5], id_count));
         else if(table[0].equals("2"))
            floor2Tables.add(new Table(Integer.parseInt(table[0]), Integer.parseInt(table[1]), Integer.parseInt(table[2]), Integer.parseInt(table[3]), Integer.parseInt(table[4]), table[5], id_count));
         else if(table[0].equals("3"))
            floor3Tables.add(new Table(Integer.parseInt(table[0]), Integer.parseInt(table[1]), Integer.parseInt(table[2]), Integer.parseInt(table[3]), Integer.parseInt(table[4]), table[5], id_count));
         id_count++;
      }
      reader.close();
   }
   
   //Reset the JLabels so we don't display information about a table we are not over
   public static void resetMessages() {
      FLOSSDriver.displayStudentName("");
      FLOSSDriver.resetTableCourse();  
      FLOSSDriver.displayMessage(""); 
   }
}