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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class StudySystemMap extends JPanel implements MouseListener, MouseMotionListener {

   private ArrayList<Table> floor1Tables = new ArrayList<Table>();   //List of tables on floor 1
   private ArrayList<Table> floor2Tables = new ArrayList<Table>();   //List of tables on floor 2
   private ArrayList<Table> floor3Tables = new ArrayList<Table>();   //List of tables on floor 3
         
   //Images for the floors
   private ImageIcon floor1 = new ImageIcon("Fenwick_Library_Maps/Floor1.png");  //Image for the map of floor 1
   private ImageIcon floor2 = new ImageIcon("Fenwick_Library_Maps/Floor2.png");  //Image for the map of floor 2
   private ImageIcon floor3 = new ImageIcon("Fenwick_Library_Maps/Floor3.png");  //Image for the map of floor 3
   
   //Images for displaying tables that have been selected
   private ImageIcon blockTable_unoccupied = new ImageIcon("table_images/block_unoccupied.png");                        //Block table display
   private ImageIcon circleTable_unoccupied = new ImageIcon("table_images/circle_unoccupied.png");                      //Circle table display
   private ImageIcon circleTable_halfLeft_unoccupied = new ImageIcon("table_images/circle_halfLeft_unoccupied.png");    //Half circle table (left)
   private ImageIcon circleTable_halfRight_unoccupied = new ImageIcon("table_images/circle_halfRight_unoccupied.png");  //Half circle table (right)
   private ImageIcon smallCircleTable_h_unoccupied = new ImageIcon("table_images/smallcircle_h_unoccupied.png");        //Small circle table (horizontal)
   private ImageIcon smallCircleTable_v_unoccupied = new ImageIcon("table_images/smallcircle_v_unoccupied.png");        //Small cirlce talbe (vertical)
   //Images for displaying tables that have been selected
   private ImageIcon blockTable_occupied = new ImageIcon("table_images/block_occupied.png");                         //Block table display
   private ImageIcon circleTable_occupied = new ImageIcon("table_images/circle_occupied.png");                       //Circle table display
   private ImageIcon circleTable_halfLeft_occupied = new ImageIcon("table_images/circle_halfLeft_occupied.png");     //Half circle table (left)
   private ImageIcon circleTable_halfRight_occupied = new ImageIcon("table_images/circle_halfRight_occupied.png");   //Half circle table (right)
   private ImageIcon smallCircleTable_h_occupied = new ImageIcon("table_images/smallcircle_h_occupied.png");         //Small circle table (horizontal)
   private ImageIcon smallCircleTable_v_occupied = new ImageIcon("table_images/smallcircle_v_occupied.png");         //Small cirlce talbe (vertical)
   //Images for displaying table that the user has selected
   private ImageIcon blockTable_selected = new ImageIcon("table_images/block_selected.png");                         //Block table display
   private ImageIcon circleTable_selected = new ImageIcon("table_images/circle_selected.png");                       //Circle table display
   private ImageIcon circleTable_halfLeft_selected = new ImageIcon("table_images/circle_halfLeft_selected.png");     //Half circle table (left)
   private ImageIcon circleTable_halfRight_selected = new ImageIcon("table_images/circle_halfRight_selected.png");   //Half circle table (right)
   private ImageIcon smallCircleTable_h_selected = new ImageIcon("table_images/smallcircle_h_selected.png");         //Small circle table (horizontal)
   private ImageIcon smallCircleTable_v_selected = new ImageIcon("table_images/smallcircle_v_selected.png");         //Small cirlce talbe (vertical)
   //Images for displaying table that the user is occupying
   private ImageIcon blockTable_user = new ImageIcon("table_images/block_user.png");                        //Block table display
   private ImageIcon circleTable_user = new ImageIcon("table_images/circle_user.png");                      //Circle table display
   private ImageIcon circleTable_halfLeft_user = new ImageIcon("table_images/halfLeft_user.png");           //Half circle table (left)
   private ImageIcon circleTable_halfRight_user = new ImageIcon("table_images/circle_halfRight_user.png");  //Half circle table (right)
   private ImageIcon smallCircleTable_h_user= new ImageIcon("table_images/smallcircle_h_user.png");         //Small circle table (horizontal)
   private ImageIcon smallCircleTable_v_user = new ImageIcon("table_images/smallcircle_v_user.png");        //Small cirlce talbe (vertical)
   //Images for displaying tables that match with what the user is searching for
   private ImageIcon blockTable_match = new ImageIcon("table_images/block_match.png");                         //Block table display
   private ImageIcon circleTable_match = new ImageIcon("table_images/circle_match.png");                       //Circle table display
   private ImageIcon circleTable_halfLeft_match = new ImageIcon("table_images/circle_halfLeft_match.png");     //Half circle table (left)
   private ImageIcon circleTable_halfRight_match = new ImageIcon("table_images/circle_halfRight_match.png");   //Half circle table (right)
   private ImageIcon smallCircleTable_h_match = new ImageIcon("table_images/smallcircle_h_match.png");         //Small circle table (horizontal)
   private ImageIcon smallCircleTable_v_match = new ImageIcon("table_images/smallcircle_v_match.png");         //Small cirlce talbe (vertical)
   //Images for displaying tables that a users friend is sitting at
   private ImageIcon blockTable_friend = new ImageIcon("table_images/block_friend.png");                       //Block table display
   private ImageIcon circleTable_friend = new ImageIcon("table_images/circle_friend.png");                     //Circle table display
   private ImageIcon circleTable_halfLeft_friend = new ImageIcon("table_images/circle_halfLeft_friend.png");   //Half circle table (left)
   private ImageIcon circleTable_halfRight_friend = new ImageIcon("table_images/circle_halfRight_friend.png"); //Half circle table (right)
   private ImageIcon smallCircleTable_h_friend = new ImageIcon("table_images/smallcircle_h_friend.png");       //Small circle table (horizontal)
   private ImageIcon smallCircleTable_v_friend = new ImageIcon("table_images/smallcircle_v_friend.png");       //Small cirlce talbe (vertical)
   
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
      userTable = new Table();            //Initialze the user table
      tableSelected = false;              //Initialize whether or not we have selected a table
      searchFeature = false;              //Initialize whether or not the search feature is active
      refreshTables( );                   //Refresh the tables display
   }
   
   public int getTableIndex( Table table ) {
	   if ( table.getFloor( ) == 1 ) return floor1Tables.indexOf( table );
	   if ( table.getFloor( ) == 2 ) return floor2Tables.indexOf( table );
	   if ( table.getFloor( ) == 3 ) return floor3Tables.indexOf( table );
	   return -1;
   }
   
   public void refreshTables( ) {
	   Map<Integer, String> allTables = FLOSSDriver.manager.fetchTableStatuses();
	      
	   String[] fullName; //add class data to table
	   for ( int tableID : allTables.keySet( ) ) {
		   char floor = Integer.toString( tableID ).charAt( 0 );
	       ArrayList<Table> tables = floor == '1'?floor1Tables:( floor == '2'?floor2Tables:floor3Tables );
	       int index = Integer.parseInt( Integer.toString( tableID ).substring( 1 ) );
	       tables.get( index ).setOccupied( );
	   	   tables.get( index ).setMessage( allTables.get( tableID ) );
	   	   String course = FLOSSDriver.getManager( ).getClassInfo( tableID );
	   	   tables.get( index ).setCourse( new Class( "", course.substring( 0, course.length( ) - 3 ), Integer.parseInt( course.substring( course.length( ) - 3 ) ), "" ) );
	    	   
	   	   for ( String id : FLOSSDriver.getManager( ).fetchStudyMates( tableID ) ) {
	   		  fullName = FLOSSDriver.getManager( ).getName( id ).split( " " );
	   		  tables.get( index ).addStudent( new Student( fullName[0], fullName[1], id, "", FLOSSDriver.getManager( ).getMajor( id ) ) );
	   	  }
	   }
   }
   
    //Get the floor number that the user is currently viewing
   public int getFloorNumber() {
      return this.floorNumber;
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
      if(button == MouseEvent.BUTTON1) {
         if(floorNumber == 1) {  //We are on floor 1
            for(int ind1 = 0; ind1 < floor1Tables.size(); ind1++) {
               table = floor1Tables.get(ind1);  //Create a temporary table
               if(clickedTable(table))          //Check to see if we clicked on an existing table location
                  break;
            }
         } else if(floorNumber == 2) {    //We are on floor 2
            for(int ind2 = 0; ind2 < floor2Tables.size(); ind2++) {
               table = floor2Tables.get(ind2);  //Create a temporary table
               if(clickedTable(table))          //Check to see if we clicked on an existing table location
                  break;
            }
         } else if(floorNumber == 3) {    //We are on floor 3
            for(int ind3 = 0; ind3 < floor3Tables.size(); ind3++) {
               table = floor3Tables.get(ind3);  //Create a temporary table
               if(clickedTable(table))          //Check to see if we clicked on an existing table location
                  break;
            }
         }
         repaint();  //Repaint our map of tables
      }  
   }
   
   //Check for what we should do when we click on a table
   public static boolean clickedTable(Table table) {
      int startRow, startCol, endRow, endCol;
      startRow = table.getRow();       //Get the starting row
      startCol = table.getCol();       //Get the starting column
      endRow = startRow + table.getRowSize();   //Get the end row
      endCol = startCol + table.getColSize();   //Get the end col
      if(mouseX >= startRow && mouseX <= endRow && mouseY >= startCol && mouseY <= endCol && table.getID() != userTable.getID()) {    //If it is between the start and end row and the start and end column
         if(table.getOccupied() == false) {  //If the table isn't occupied
            repaint();  //Update the side panel messages
            selectedTable = table;     //Set our selectedTable 
            tableSelected = true;      //Set our tableSelected boolean to true
            FLOSSDriver.displayCourseOptions(true);   //Display the course options to add to the unoccupied table
            FLOSSDriver.showJoinTable(false);         //Do not show the join table option
            return true;
         } else if(table.getNumStudents() <= table.getCapacity()) {  //If the table is occupied but under max capacity
            FLOSSDriver.displayCourseOptions(false);  //Do not display options to set the table course 
            FLOSSDriver.showErrorMessage(false);      //Do not show max occupied error message
            selectedTable = table;                    //Set our selectedTable
            tableSelected = true;                     //Set our tableSelected boolean to true            
            FLOSSDriver.displayStudentName(selectedTable.getStudents());   //Display the students occupying that table
            FLOSSDriver.showAddFriend(selectedTable, selectedTable.getNumStudents());  //Show the add friend options for the people at that table
            FLOSSDriver.displayMessage(selectedTable.getMessage());  //Display the message that the occupant has for the table
            if(table.getCourse().getNumber() != 0)    //If the table course is initialized
               FLOSSDriver.displayTableCourse(table.getCourse());    //Display the table course
            else
               FLOSSDriver.resetTableCourse();        //Don't display an instance of a table course
            if(table.getNumStudents() == table.getCapacity()) {   //If the capacity of the table is at it's max
               FLOSSDriver.showErrorMessage(true);    //Show the max occupancy error message
               FLOSSDriver.showJoinTable(false);      //Do not display join table option
            }
            else if(table.getID() != userTable.getID())  //If the table is not the user's current occupied table
               FLOSSDriver.showJoinTable(true);       //Display the option to join the table
            return true;
         } 
      } else {
         FLOSSDriver.displayCourseOptions(false);  //Display no options to choose a course for a table
         tableSelected = false;        //Set table selected to false
         selectedTable = userTable;    //Set the selected table to the user table
         repaint();                    //Update the side panel messages
         return false;
      }
      return false;
   }
   
   //When we move the mouse, we can see information of tables that our cursor is hovering over
   public void mouseMoved( MouseEvent e) {
      Table table = null;
      mouseX = e.getX();	//Get mouseX value
      mouseY = e.getY();	//Get mouseY value
      if(floorNumber == 1) {     //We are currently viewing the first floor
         for(int ind1 = 0; ind1 < floor1Tables.size(); ind1++) {
            table = floor1Tables.get(ind1);  //Create a temporary table
            if(movedMouse(table))   //Check to see if the mouse is moved over a table
               break;
         }
      }
      else if(floorNumber == 2) {   //We are currently viewing the second floor
         for(int ind2 = 0; ind2 < floor2Tables.size(); ind2++) {
            table = floor2Tables.get(ind2);  //Create a temporary table
            if(movedMouse(table))   //Check to see if the mouse is moved over a table
               break;
         }
      }
      else if(floorNumber == 3) {   //We are currently viewing the third floor
         for(int ind3 = 0; ind3 < floor3Tables.size(); ind3++) {
            table = floor3Tables.get(ind3);  //Create a temporary table
            if(movedMouse(table))   //Check to see if the mouse is moved over a table
               break;
         }
      }
      repaint();  //Repaint our map of tablee
   }
   
   //Method to check what should happen when we move the mouse over a table
   public static boolean movedMouse(Table table) {
      int startRow, startCol, endRow, endCol;
      startRow = table.getRow();       //Get the starting row
      startCol = table.getCol();       //Get the starting column
      endRow = startRow + table.getRowSize();   //Get the end row
      endCol = startCol + table.getColSize();   //Get the end col
      if(mouseX >= startRow && mouseX <= endRow && mouseY >= startCol && mouseY <= endCol && table.getOccupied() && table.getID() != selectedTable.getID()) {    //If it is between the start and end row and the start and end column
         FLOSSDriver.displayStudentName(table.getStudents());  //Display the students that are sitting at an occupied table
         FLOSSDriver.displayMessage(table.getMessage());       //Display the message of the table
         if(table.getCourse().getNumber() != 0)
            FLOSSDriver.displayTableCourse(table.getCourse());    //Display the course if it is initialized
         else
            FLOSSDriver.resetTableCourse();                       //Don't display the course if it's not initialized
         if(table.getNumStudents() == table.getCapacity())
            FLOSSDriver.showErrorMessage(true);             //Show the error message if the table is at max occupancy 
         else
            FLOSSDriver.showErrorMessage(false);            //Do not show the error message if the table is not at max occupancy 
         FLOSSDriver.showJoinTable(false);            //Do not show the join table option
         FLOSSDriver.showAddFriend(new Table(), 0);   //Do not show the add friend button options
         return true;
      } else if(tableSelected == true) {              //If we are not over a table but we have a table selected
         if(selectedTable.getOccupied()) {            //If the table we have selected is occupied
            FLOSSDriver.displayStudentName(selectedTable.getStudents());   //Display the students at the table
            FLOSSDriver.displayMessage(selectedTable.getMessage());        //Displat the table message
            if(selectedTable.getCourse().getNumber() != 0)                 
               FLOSSDriver.displayTableCourse(selectedTable.getCourse());  //Display the course if initialized
            else
               FLOSSDriver.resetTableCourse();                             //Do not display the course if not initalized
            if(selectedTable.getNumStudents() == selectedTable.getCapacity())
               FLOSSDriver.showErrorMessage(true);                         //Show the error message if the table is at max capacity
            if(selectedTable.getID() != userTable.getID() && selectedTable.getNumStudents() < selectedTable.getCapacity()) {  //Table selected is not user table and under max occupancy
               FLOSSDriver.showJoinTable(true);                            //Display the join table option
               FLOSSDriver.showErrorMessage(false);                        //Do not display the max occupancy message
            } else {
               FLOSSDriver.showJoinTable(false);                           //Do not display the join table option
               FLOSSDriver.showErrorMessage(true);                         //Display the max occupancy message
            }
            FLOSSDriver.showAddFriend(selectedTable, selectedTable.getNumStudents());  //Show the add friend options
            return false;
         } else {             //We are not over a current table and do not have a table selected
            FLOSSDriver.showJoinTable(false);   //Do not show the join table button
            repaint();  //Update the side panel messages
            return false;
         }
      } else {
         repaint();  //Update the side panel messages
         return false;
      }
   }

   //When we drag the mouse across the board
   public void mouseDragged( MouseEvent e) {
      mouseX = e.getX();	//Get mouseX value
      mouseY = e.getY();	//Get mouseY value
      repaint();  //Repaint our map of tables			//Refresh the screen
   }
    
   private class Listener implements ActionListener {
      public void actionPerformed(ActionEvent e)	{
         repaint();  //Repaint our map of tables
      }
   }

   public void mousePressed( MouseEvent e )
   {}

   public void mouseReleased( MouseEvent e )
   {}

   public void mouseEntered( MouseEvent e )
   {}
   
   //Mouse Exited
   public void mouseExited( MouseEvent e )
   {}
   
   //Call all the paint methods for displaying the image
   public void paintComponent(Graphics g) {
      g.setColor(Color.red);     //Set the background color to no pieces as red
      super.paintComponent(g); 	//Call super method
      paintMap(g, floorNumber);  //Paint the map image and the occupied or unoccupied tables
      paintMatchingTable(g);     //Paint the matching tables
      paintFriendTable(g);       //Paint the tables that friends are sitting at
      paintUserTable(g);         //Paint the table that the user currently occupies
      paintSelectedTable(g);     //Paint the table that the user has selected
   }
   
   //Paint out the image of the floors for Fenwick
   public void paintMap(Graphics g, int floor) {
      //Display floor 1 if the user is viewing that floor
      if(floor == 1) {
         g.drawImage(floor1.getImage(), 0, 0, 800, 800, null);
         for(int f1 = 0; f1 < floor1Tables.size(); f1++) {
            if(floor1Tables.get(f1).getOccupied() == true)
               paintOccupiedTable(g, floor1Tables.get(f1));
            else
               paintUnoccupiedTable(g, floor1Tables.get(f1));
         }
      //Display floor 2 if the user is viewing that floor
      } else if(floor == 2){
         g.drawImage(floor2.getImage(), 0, 0, 800, 800, null);
         for(int f2 = 0; f2 < floor2Tables.size(); f2++) {
            if(floor2Tables.get(f2).getOccupied() == true)
               paintOccupiedTable(g, floor2Tables.get(f2));
            else
               paintUnoccupiedTable(g, floor2Tables.get(f2));
         }
      //Display floor 3 if the user is viewing that floor
      } else if(floor == 3){
         g.drawImage(floor3.getImage(), 0, 0, 800, 800, null);
         for(int f3 = 0; f3 < floor3Tables.size(); f3++) {
            if(floor3Tables.get(f3).getOccupied() == true)
               paintOccupiedTable(g, floor3Tables.get(f3));
            else
               paintUnoccupiedTable(g, floor3Tables.get(f3));
         }
      } else
         g.drawImage(floor1.getImage(), 0, 0, 800, 800, null);
   }
   
   //Paint a basic instance of a table that isn't occupied
   public void paintUnoccupiedTable(Graphics g, Table t) {
      if(t.getShape().equals("BLOCK")) 
         g.drawImage(blockTable_unoccupied.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
      else if(t.getShape().equals("CIRCLE")) 
         g.drawImage(circleTable_unoccupied.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
      else if(t.getShape().equals("CIRCLE_HL")) 
         g.drawImage(circleTable_halfLeft_unoccupied.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
      else if(t.getShape().equals("CIRCLE_HR")) 
         g.drawImage(circleTable_halfRight_unoccupied.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
      else if(t.getShape().equals("SMALLCIRCLE_H")) 
         g.drawImage(smallCircleTable_h_unoccupied.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
      else if(t.getShape().equals("SMALLCIRCLE_V")) 
         g.drawImage(smallCircleTable_v_unoccupied.getImage(), t.getRow(), t.getCol(), t.getRowSize(), t.getColSize(), null); 
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
         //If table selected is equal to true and the shape matches the selected table
         if(selectedTable.getShape().equals("BLOCK"))
            g.drawImage(blockTable_selected.getImage(), selectedTable.getRow(), selectedTable.getCol(), selectedTable.getRowSize(), selectedTable.getColSize(), null); 
         else if(selectedTable.getShape().equals("CIRCLE")) 
            g.drawImage(circleTable_selected.getImage(), selectedTable.getRow(), selectedTable.getCol(), selectedTable.getRowSize(), selectedTable.getColSize(), null); 
         else if(selectedTable.getShape().equals("CIRCLE_HL")) 
            g.drawImage(circleTable_halfLeft_selected.getImage(), selectedTable.getRow(), selectedTable.getCol(), selectedTable.getRowSize(), selectedTable.getColSize(), null); 
         else if(selectedTable.getShape().equals("CIRCLE_HR")) 
            g.drawImage(circleTable_halfRight_selected.getImage(), selectedTable.getRow(), selectedTable.getCol(), selectedTable.getRowSize(), selectedTable.getColSize(), null); 
         else if(selectedTable.getShape().equals("SMALLCIRCLE_H")) 
            g.drawImage(smallCircleTable_h_selected.getImage(), selectedTable.getRow(), selectedTable.getCol(), selectedTable.getRowSize(), selectedTable.getColSize(), null); 
         else if(selectedTable.getShape().equals("SMALLCIRCLE_V")) 
            g.drawImage(smallCircleTable_v_selected.getImage(), selectedTable.getRow(), selectedTable.getCol(), selectedTable.getRowSize(), selectedTable.getColSize(), null);
      } 
   }
   
   //Paint the table that the user is currently occupying
   public void paintUserTable(Graphics g) {
      if(userTable != null) {
         //Check if the userTable is not equal to null and find the table shape and the floor number matches the one we are currently on
         if(userTable.getShape().equals("BLOCK") && userTable.getFloor() == floorNumber) 
            g.drawImage(blockTable_user.getImage(), userTable.getRow(), userTable.getCol(), userTable.getRowSize(), userTable.getColSize(), null); 
         else if(userTable.getShape().equals("CIRCLE") && userTable.getFloor() == floorNumber) 
            g.drawImage(circleTable_user.getImage(), userTable.getRow(), userTable.getCol(), userTable.getRowSize(), userTable.getColSize(), null); 
         else if(userTable.getShape().equals("CIRCLE_HL")) 
            g.drawImage(circleTable_halfLeft_user.getImage(), userTable.getRow(), userTable.getCol(), userTable.getRowSize(), userTable.getColSize(), null); 
         else if(userTable.getShape().equals("CIRCLE_HR")) 
            g.drawImage(circleTable_halfRight_user.getImage(), userTable.getRow(), userTable.getCol(), userTable.getRowSize(), userTable.getColSize(), null); 
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
            tables = floor1Tables;  //Print the matching tables from floor 1
         else if (floorNumber == 2)
            tables = floor2Tables;  //Print the matching tables from floor 2
         else if (floorNumber == 3)
            tables = floor3Tables;  //Print the matching tables from floor 3
         else
            tables = floor1Tables;
         String subjectCode = FLOSSDriver.getSearchSubject();
         int courseNumber = FLOSSDriver.getSearchNumber();
         for(int index = 0; index < tables.size(); index++) {
            Table table = tables.get(index);
            //Check if the table is the type, matches the subject code, matches the subject number, and is not the user table
            if(table.getShape().equals("BLOCK") && table.getCourse().getSubjectCode().equals(subjectCode) && table.getCourse().getNumber() == courseNumber && table.getID() != userTable.getID()) 
               g.drawImage(blockTable_match.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null); 
            else if(table.getShape().equals("CIRCLE") && table.getCourse().getSubjectCode().equals(subjectCode) && table.getCourse().getNumber() == courseNumber && table.getID() != userTable.getID()) 
               g.drawImage(circleTable_match.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null); 
            else if(table.getShape().equals("CIRCLE_HL") && table.getCourse().getSubjectCode().equals(subjectCode) && table.getCourse().getNumber() == courseNumber && table.getID() != userTable.getID()) 
               g.drawImage(circleTable_halfLeft_match.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null); 
            else if(table.getShape().equals("CIRCLE_HR") && table.getCourse().getSubjectCode().equals(subjectCode) && table.getCourse().getNumber() == courseNumber && table.getID() != userTable.getID()) 
               g.drawImage(circleTable_halfRight_match.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null);
            else if(table.getShape().equals("SMALLCIRCLE_H") && table.getCourse().getSubjectCode().equals(subjectCode) && table.getCourse().getNumber() == courseNumber && table.getID() != userTable.getID()) 
               g.drawImage(smallCircleTable_h_match.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null);  
            else if(table.getShape().equals("SMALLCIRCLE_V") && table.getCourse().getSubjectCode().equals(subjectCode) && table.getCourse().getNumber() == courseNumber && table.getID() != userTable.getID()) 
               g.drawImage(smallCircleTable_v_match.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null); 
         }
      } 
   }
   
    //Paint the table that matches with the users search feature
   public void paintFriendTable(Graphics g) {
      if(searchFeature == true) {
         ArrayList<Table> tables;
         if(floorNumber == 1)
            tables = floor1Tables;  //Print the matching tables from floor 1
         else if (floorNumber == 2)
            tables = floor2Tables;  //Print the matching tables from floor 2
         else if (floorNumber == 3)
            tables = floor3Tables;  //Print the matching tables from floor 3
         else
            tables = floor1Tables;
         String subjectCode = FLOSSDriver.getSearchSubject();
         int courseNumber = FLOSSDriver.getSearchNumber();
         for(int index = 0; index < tables.size(); index++) {
            Table table = tables.get(index);
            for(int friendIndex = 0; friendIndex < FLOSSDriver.getUser().getFriends().size(); friendIndex++) {
               String masonEmail = FLOSSDriver.getUser().getFriends().get(friendIndex);
               ArrayList<Student> students = table.getStudents();
               for(int tableIndex = 0; tableIndex < table.getNumStudents(); tableIndex++) {
                  if(masonEmail.equals(students.get(tableIndex).getMasonEmail())) {
                     if(table.getShape().equals("BLOCK")) 
                        g.drawImage(blockTable_friend.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null); 
                     else if(table.getShape().equals("CIRCLE")) 
                        g.drawImage(circleTable_friend.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null); 
                     else if(table.getShape().equals("CIRCLE_HL")) 
                        g.drawImage(circleTable_halfLeft_friend.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null); 
                     else if(table.getShape().equals("CIRCLE_HR")) 
                        g.drawImage(circleTable_halfRight_friend.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null);
                     else if(table.getShape().equals("SMALLCIRCLE_H")) 
                        g.drawImage(smallCircleTable_h_friend.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null);  
                     else if(table.getShape().equals("SMALLCIRCLE_V")) 
                        g.drawImage(smallCircleTable_v_friend.getImage(), table.getRow(), table.getCol(), table.getRowSize(), table.getColSize(), null); 
                  }
               }
            }
         }
      } 
   }
   
   public void resetTables( ) {
	   Table table;
	   for ( int i = 0; i < floor1Tables.size(); i++ ) {
		   table = floor1Tables.get(i);
		   table.clearStudents();
		   if ( table.getOccupied() ) table.setOccupied();
		   table.setMessage("");
		   table.setCourse(new Class());
	   }
	   for ( int i = 0; i < floor2Tables.size(); i++ ) {
		   table = floor2Tables.get(i);
		   table.clearStudents();
		   if ( table.getOccupied() ) table.setOccupied();		
		   table.setMessage("");
		   table.setCourse(new Class());
	   }
	   for ( int i = 0; i < floor3Tables.size(); i++ ) {
		   table = floor3Tables.get(i);
		   table.clearStudents();
		   if ( table.getOccupied() ) table.setOccupied();
		   table.setMessage("");
		   table.setCourse(new Class());
	   }
   }
   
   //Read in the tables.txt file and set the information for all the tables that a student can sit at
   public void initializeTables() throws IOException {
      File file = new File("dataFiles/tables.txt");
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line = "";
      int id_count = 0;
      while ((line = reader.readLine()) != null) {    //Read through the entire tables.txt file
         String[] table = line.split(",");
         if(table[0].equals("1"))               //Table belongs on floor 1
            floor1Tables.add(new Table(Integer.parseInt(table[0]), Integer.parseInt(table[1]), Integer.parseInt(table[2]), Integer.parseInt(table[3]), Integer.parseInt(table[4]), table[5], id_count, Integer.parseInt(table[6])));
         else if(table[0].equals("2"))          //Table belongs on floor 2
            floor2Tables.add(new Table(Integer.parseInt(table[0]), Integer.parseInt(table[1]), Integer.parseInt(table[2]), Integer.parseInt(table[3]), Integer.parseInt(table[4]), table[5], id_count, Integer.parseInt(table[6])));
         else if(table[0].equals("3"))          //Table belongs on floor 3
            floor3Tables.add(new Table(Integer.parseInt(table[0]), Integer.parseInt(table[1]), Integer.parseInt(table[2]), Integer.parseInt(table[3]), Integer.parseInt(table[4]), table[5], id_count, Integer.parseInt(table[6])));
         id_count++;
      }
      reader.close();
   }
   
   //Reset the JLabels so we don't display information about a table we are not over
   public static void updateMessages() {
      FLOSSDriver.displayStudentName(userTable.getStudents());    //Get the student at the user table
      FLOSSDriver.displayTableCourse(userTable.getCourse());      //Get the course at the user table
      FLOSSDriver.displayMessage(userTable.getMessage());         //Get the message at the user table
      FLOSSDriver.showErrorMessage(false);                        //Do not show message of table being max occupied
      FLOSSDriver.showAddFriend(selectedTable, selectedTable.getNumStudents());  //Show the added friends for the selected table
      FLOSSDriver.showJoinTable(false);                           //Do not show the 'Join Table' button
   }
}
