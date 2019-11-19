/*
   FLOSSDriver.java
   CS 321 - Section 001: Team 7
   John DeCarlo, Huiying Jin, John Radecki, Joshua Yuen
   ----------------------------------------------------
   Description: This is the executable driver for our software
*/

import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.floss.manager.*;

@SuppressWarnings("unchecked")
public class FLOSSDriver {

   public static JFrame display;
   public static StudySystemMap fenwickLibrary;    //JPanel that holds the study maps
   public static JPanel sidePanel;                 //JPanel that is the side information panel
   public static ArrayList<Class> classList = new ArrayList<Class>();       //List of most of the classes offered at GMU
   public static RemoteDBManager manager;
   public static Timer refresh;
   
   //JButtons to display the floors and the search feature
   public static JButton floor1;    //JButton when selected will display the first floor     
   public static JButton floor2;    //JButton when selected will display the second floor        
   public static JButton floor3;    //JButton when selected will display the third floor  
   public static JButton search;    //JButton when selected will perform the search option
   public static JButton removeUserTable; //JButton when selected will remove the table the user is at
   
   //JButtons to display whether to leave a table or not
   public static JDialog leaveTable;      //Box that will display without the ability to exit
   public static JPanel leaveContainer;   //Panel that holds JButtons and message   
   public static JButton yes;             //Yes button to switch tables
   public static JButton no;              //No button to stay at current table
   public static JLabel leaveMessage;     //Message displayed with the JButton
   
   //Floor Information text labels
   public static JLabel tableStudentName1;   //Displays student 1 name
   public static JButton addFriend1;         //Button to add student 1 as a friend
   public static JLabel tableStudentName2;   //Displays student 2 name
   public static JButton addFriend2;         //Button to add student 2 as a friend
   public static JLabel tableStudentName3;   //Displays student 3 name
   public static JButton addFriend3;         //Button to add student 3 as a friend
   public static JLabel tableStudentName4;   //Displays student 4 name
   public static JButton addFriend4;         //Button to add student 4 as a friend
   public static JLabel tableClass;          //Message that displays the class the table is studying
   public static JLabel tableMessage;        //Message that displays the personal message of the table
   public static JLabel errorMessage;        //Displays when the table is at max occupancy
   public static JButton joinTable;          //Button that allows one to join a table
   
   //Classes list ComboBox
   public static String[] subjectStrings = {"ACCT", "AFAM", "ANTH", "ARAB", "ARTH", "EDAT", "ASTR", "ATEP", "BENG", "BINF", "BENG", "BIOL", "BUS", "BULE", "CHEM", "CHIN", "CEIE", "CLAS", "CLIM", "COS", "CVPA", "COMM", "CDS", "GAME", "CS", "CONF", "CRIM", "CULT", "CYSE", "DANC", "DSGN", "ECED", "ECON", "EDIT", "EDUC", "EDPO", "EDPS", "ECE", "ELED", "ENGR", "ENGH", "ENVPP", "FAVS", "FNAN", "FRLN", "FRSC", "FREN", "GGS", "GEOL", "GERM", "GLOA", "GCH", "GOVT", "HEAL", "HAP", "HHS", "HEBR", "HIST", "HNRS", "HNRT", "HDFS", "IT", "INTS", "ITAL", "JAPA", "KINE", "KORE", "LATN", "LING", "MGMT", "MIS", "MKTG", "MATH", "ME", "MLAB", "MLSC", "MBUS", "NEUR", "NURS", "NUTR", "OM", "OR", "PRLS", "PERS", "PHIL", "PHED", "PHYS", "PORT", "PROV", "PSYC", "EDRD", "RHBS", "RELI", "RUSS", "SOCW", "SOCI", "SWE", "SPAN", "EDSE", "SPMT", "SRST", "STAT", "SYST", "THR", "TOUR", "TURK", "UNIV", "WMST"};
   public static JComboBox courseSubjects;   //Option to choose the course subject
   public static JComboBox courseNumbers;    //Option to choose the course number
   public static JLabel messageLabel;        //Displays 'message' right above the JTextField
   public static JTextField message;         //User inputs the message to display to the other students   
   public static JButton addTable;           //Button to add to a table
   
   //Search feature ComboBoxes
   public static JLabel searchTitle;         //Display the search feature title
   public static JComboBox searchSubjects;   //When searching for courses, display the course subject
   public static JComboBox searchNumbers;    //When searching for courses, display the course numbers
   
   //Profile JPanel text labels
   public static JLabel profileName;      //Profile display name
   public static JLabel profileYear;      //Profile display year
   public static JLabel profileMajor;     //Profile display major
   public static JLabel profileMinor;     //Profile display minor
   public static JLabel profileGnumber;   //Profile display g-number
   public static boolean enableSearch = false;  //Whether the search feature is enabled
   
   public static Student user;
   
   public static void main(String[]args) throws IOException {
      manager = new AWSManager();
      user = new Student();
      display = new JFrame("FLOSS");     //Create our JFrame
      display.setSize(1112, 830);			      //Size of display window
      display.setLocation(250, 0);				   //Location of display window on the screen
      display.setResizable(false);              //Cannot change size of the screen
      display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //Exit when close out 
      //When the user exits out of the program, then they will leave their table
      display.addWindowListener(
               new WindowAdapter() {
                  @Override
                  public void windowClosing(WindowEvent e) {   
                     fenwickLibrary.getUserTable().removeStudent(user); //Remove the user from the table
                     if(fenwickLibrary.getUserTable().getNumStudents() == 0) {   //If the table has no students
                        fenwickLibrary.getUserTable().setCourse(new Class()); //Set the class to a basic object
                        fenwickLibrary.getUserTable().setMessage("");      //Set the message to blank
                        fenwickLibrary.getUserTable().setOccupied();       //Set occupied to false
                     }
                     fenwickLibrary.setUserTable(new Table());          //Set the user table to a blank option
                     manager.stopStudying(user.getMasonEmail());        //*** AWS IMPLEMENTATION ***
                     user.setOccupyTable();                             //Set user occupy table to false
                     fenwickLibrary.updateMessages();  //Update the side panel messages
                     fenwickLibrary.setTableSelected(false);            //Set selected table to false
                     fenwickLibrary.repaint();  //Repaint the map displaying the tables on the floor
                     removeUserTable.setVisible(false);  //Hide from being displayed                
                  }
               });
      
      JPanel container = new JPanel();          //Establish the JPanel that will hold all our JPanel's
      container.setLayout(null);                //Set the layout to null so we can set the bounds
   
      fenwickLibrary = new StudySystemMap();       //Initialize the interactive Fenwick library map
      fenwickLibrary.setBounds(0, 0, 800, 800);    //Set the bounds for the interactive map
      displaySidePanel();                          //Add all the buttons and options
      initializeClasses();                         //Initialize the class list
      initializeSidePanel();                       //Initialize the side panel
      initializeJoinTable();                       //Initialize the join table button
      initializeAddTable();                        //Initialize the add table button
      initializeRemoveUserTable();                 //Initalize the remove user table button
      initializeSearchFeature();                   //Initialize the search feature for the class
      
      container.add(fenwickLibrary);         //Add JPanel map to the main display screen 
      container.add(sidePanel);              //Add JPanel sidePanel to the main display screen 
      display.setContentPane(container);     //Set the contents of the JFrame display to the container holding all the display info              
      
      leaveTable = new JDialog();            //Create our JFrame
      leaveTable.setSize(300, 150);			   //Size of display window (600, 300)
      leaveTable.setLocation(600, 300);	   //Location of display window on the screen
      leaveTable.setResizable(false);        //Cannot change size of the screen
      leaveTable.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
      initializeLeaveTable();
      
      Login login = new Login(display);
      login.setVisible(true);  //Display in the side panel
      
      refresh = new Timer( 30000, 
           new ActionListener( ) {
              public void actionPerformed( ActionEvent e ) {
              //display refreshing info message
                 fenwickLibrary.resetTables( );
                 fenwickLibrary.refreshTables( );
                 fenwickLibrary.repaint( );
              //remove refreshing message
              }
           });
      refresh.start();
   }
   
   //Get the student user
   public static Student getUser() {
      return user;
   }
   
   //Get the Amazon manager
   public static RemoteDBManager getManager() {
      return manager;
   }
   
   //Get the search subject that the user is looking for
   public static String getSearchSubject() {
      return (String)searchSubjects.getSelectedItem();
   }
   
   //Get the search number of the course the user is looking for
   public static int getSearchNumber() {
      return Integer.parseInt((String)searchNumbers.getSelectedItem());
   }
         
   //Method that initializes the buttons for the side panel
   public static void displaySidePanel() {
      sidePanel = new JPanel();
      sidePanel.setBounds(800, 0, 300, 800);
      sidePanel.setLayout(null);
      floor1 = new JButton("1F");   //Initialize the JButton
      floor2 = new JButton("2F");   //Initialize the JButton
      floor3 = new JButton("3F");   //Initialize the JButton
      ImageIcon searchIcon = new ImageIcon("icon_images/search_icon.png");    //Initialize the image to add to the JButton
      search = new JButton(searchIcon);    //Add button to the JFrame
      floor1.setBounds(0, 0, 75, 50);       //Set the bounds to the floor 1 button
      floor2.setBounds(75, 0, 75, 50);     //Set the bounds to the floor 2 button
      floor3.setBounds(150, 0, 75, 50);     //Set the bounds to the floor 3 button
      search.setBounds(225, 0, 75, 50);     //Set the bounds to the search button
      floor1.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               enableButtons(); //Set all the buttons to working          
               floor1.setEnabled(false);  //Disable the JButton
               fenwickLibrary.setFloorNumber(1);   //Display the first floor
               fenwickLibrary.setTableSelected(false);   //Set the selected table to false when changing floors
               fenwickLibrary.setSelectedTable(fenwickLibrary.getUserTable());   //Set the selected table to the user table
               fenwickLibrary.updateMessages();  //Update the side panel messages    
               displayCourseOptions(false);  //Do not display the possible course select options
               fenwickLibrary.repaint();  //Repaint the map displaying the tables on the floor
            } });
      floor1.setEnabled(false);  //Disable the JButton   
      floor2.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               enableButtons(); //Set all the buttons to working        
               floor2.setEnabled(false);  //Disable the JButton
               fenwickLibrary.setFloorNumber(2);   //Display the second floor
               fenwickLibrary.setTableSelected(false);   //Set the selected table to false when changing floors
               fenwickLibrary.setSelectedTable(fenwickLibrary.getUserTable());
               fenwickLibrary.updateMessages();  //Update the side panel messages
               displayCourseOptions(false);  //Do not display the possible course select options
               fenwickLibrary.repaint();  //Repaint the map displaying the tables on the floor
            } });    
      floor3.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               enableButtons(); //Set all the buttons to working           //Enable all buttons but disable the floor3 button
               floor3.setEnabled(false);  //Disable the JButton
               fenwickLibrary.setFloorNumber(3);   //Display the third floor
               fenwickLibrary.setTableSelected(false);   //Set the selected table to false when changing floors
               fenwickLibrary.setSelectedTable(fenwickLibrary.getUserTable());
               fenwickLibrary.updateMessages();  //Update the side panel messages
               displayCourseOptions(false);  //Do not display the possible course select options
               fenwickLibrary.repaint();  //Repaint the map displaying the tables on the floor
            } });  
      search.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               enableButtons(); //Set all the buttons to working
               enableSearch();  //Display the search option
               if(fenwickLibrary.getFloorNumber() == 1) 
                  floor1.setEnabled(false);  //Disable the JButton
               else if(fenwickLibrary.getFloorNumber() == 2) 
                  floor2.setEnabled(false);  //Disable the JButton
               else if(fenwickLibrary.getFloorNumber() == 3) 
                  floor3.setEnabled(false);  //Disable the JButton 
               fenwickLibrary.repaint();  //Repaint the map displaying the tables on the floor
            } });  
      sidePanel.add(floor1);      //Add button to the JFrame
      sidePanel.add(floor2);      //Add button to the JFrame
      sidePanel.add(floor3);      //Add button to the JFrame
      sidePanel.add(search);      //Add button to the JFrame
   }
   
   //Enable the search feature of the table
   public static void enableSearch() {
      if(enableSearch) {
         fenwickLibrary.setSearchEnabled(false);  //Disable the search feature
         searchSubjects.setVisible(false);  //Hide from being displayed  
         searchNumbers.setVisible(false);  //Hide from being displayed 
         searchTitle.setVisible(false);  //Hide from being displayed  
         enableSearch = false;              //Set search enabled to false
      } else {
         fenwickLibrary.setSearchEnabled(true); //Enable the search feature
         searchSubjects.setVisible(true);  //Display in the side panel       
         searchNumbers.setVisible(true);  //Display in the side panel        
         searchTitle.setVisible(true);  //Display in the side panel       
         enableSearch = true;                   //Set the search enabled to true
      }
   }
   
   //Enable the buttons for the floors
   public static void enableButtons() {
      floor1.setEnabled(true);  //Enable the JButton
      floor2.setEnabled(true);  //Enable the JButton
      floor3.setEnabled(true);  //Enable the JButton
   }
   
   //Whether or not we want to display certain aspects of our side panel
   public static void displayCourseOptions(boolean display) {
      courseSubjects.setVisible(display);    //Display the course subjects
      courseNumbers.setVisible(display);     //Display the course numbers
      messageLabel.setVisible(display);      //Display the message labe
      message.setVisible(display);           //Display the user message
      addTable.setVisible(display);          //Display the add table button
   }
   
   //Method that initializes the side panel and all the JLabel and JComboBox features
   public static void initializeSidePanel() {
      tableStudentName1 = new JLabel("Student 1"); //Initialize the JLabel
      tableStudentName1.setBounds(25, 60, 200, 25);   //Set the bounds for where it's displayed
      sidePanel.add(tableStudentName1);
      addFriend1 = new JButton("Follow"); //Intialize the JButton
      addFriend1.setBounds(200, 60, 85, 15); //Set the bounds for where it's displayed
      addFriend1.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(addFriend1.getText().equals("Follow")) {  //If addFriend is set to follow
                  user.addFriend(fenwickLibrary.getSelectedTable().getStudent(0).getMasonEmail());
                  manager.addFriend( user.getMasonEmail( ), fenwickLibrary.getSelectedTable().getStudent(0).getMasonEmail() );
                  addFriend1.setText("Unfollow");  //Set the text to unfollow
               } else {
                  user.removeFriend(fenwickLibrary.getSelectedTable().getStudent(0).getMasonEmail());
                  manager.removeFriend( user.getMasonEmail(), fenwickLibrary.getSelectedTable().getStudent(0).getMasonEmail() );
                  addFriend1.setText("Follow");      //Set the text to follow
               }
            } });
      addFriend1.setVisible(false);  //Hide from being displayed
      sidePanel.add(addFriend1);
      tableStudentName2 = new JLabel("Student 2"); //Initialize the JLabel
      tableStudentName2.setBounds(25, 85, 200, 25);   //Set the bounds for where it's displayed
      sidePanel.add(tableStudentName2);
      addFriend2 = new JButton("Follow"); //Intialize the JButton
      addFriend2.setBounds(200, 85, 85, 15); //Set the bounds for where it's displayed
      addFriend2.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(addFriend2.getText().equals("Follow")) {  //If addFriend is set to follow
                  user.addFriend(fenwickLibrary.getSelectedTable().getStudent(1).getMasonEmail());
                  manager.addFriend( user.getMasonEmail( ), fenwickLibrary.getSelectedTable().getStudent(1).getMasonEmail() );
                  addFriend2.setText("Unfollow");  //Set the text to unfollow
               } else {
                  user.removeFriend(fenwickLibrary.getSelectedTable().getStudent(1).getMasonEmail());
                  manager.removeFriend( user.getMasonEmail(), fenwickLibrary.getSelectedTable().getStudent(1).getMasonEmail() );
                  addFriend2.setText("Follow");      //Set the text to follow
               }
            } });
      addFriend2.setVisible(false);  //Hide from being displayed
      sidePanel.add(addFriend2);
      tableStudentName3 = new JLabel("Student 3"); //Initialize the JLabel
      tableStudentName3.setBounds(25, 110, 200, 25);  //Set the bounds for where it's displayed
      sidePanel.add(tableStudentName3);
      addFriend3 = new JButton("Follow"); //Intialize the JButton
      addFriend3.setBounds(200, 110, 85, 15);   //Set the bounds for where it's displayed
      addFriend3.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(addFriend3.getText().equals("Follow")) {  //If addFriend is set to follow
                  user.addFriend(fenwickLibrary.getSelectedTable().getStudent(2).getMasonEmail());
                  manager.addFriend( user.getMasonEmail( ), fenwickLibrary.getSelectedTable().getStudent(2).getMasonEmail() );
                  addFriend3.setText("Unfollow");  //Set the text to unfollow
               } else {
                  user.removeFriend(fenwickLibrary.getSelectedTable().getStudent(2).getMasonEmail());
                  manager.removeFriend( user.getMasonEmail(), fenwickLibrary.getSelectedTable().getStudent(2).getMasonEmail() );
                  addFriend3.setText("Follow");      //Set the text to follow
               }
            } });
      addFriend3.setVisible(false);  //Hide from being displayed
      sidePanel.add(addFriend3);
      tableStudentName4 = new JLabel("Student 4");
      tableStudentName4.setBounds(25, 135, 200, 25);  //Set the bounds for where it's displayed
      sidePanel.add(tableStudentName4);
      addFriend4 = new JButton("Follow"); //Intialize the JButton
      addFriend4.setBounds(200, 135, 85, 15);   //Set the bounds for where it's displayed
      addFriend4.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(addFriend4.getText().equals("Follow")) {  //If addFriend is set to follow
                  user.addFriend(fenwickLibrary.getSelectedTable().getStudent(3).getMasonEmail());
                  manager.addFriend( user.getMasonEmail( ), fenwickLibrary.getSelectedTable().getStudent(3).getMasonEmail() );
                  addFriend4.setText("Unfollow");  //Set the text to unfollow
               } else {
                  user.removeFriend(fenwickLibrary.getSelectedTable().getStudent(3).getMasonEmail());
                  manager.removeFriend( user.getMasonEmail(), fenwickLibrary.getSelectedTable().getStudent(3).getMasonEmail() );
                  addFriend4.setText("Follow");      //Set the text to follow
               }
            } });
      addFriend4.setVisible(false);  //Hide from being displayed
      sidePanel.add(addFriend4); //Set the bounds for where it's displayed
      tableClass = new JLabel("");     //Initialize the JLabel
      tableClass.setBounds(25, 165, 200, 25);   //Set the bounds for where it's displayed
      sidePanel.add(tableClass);
      tableMessage = new JLabel("");   //Initialize the JLabel
      tableMessage.setBounds(25, 195, 200, 80); //Set the bounds for where it's displayed 
      sidePanel.add(tableMessage);
      errorMessage = new JLabel("<html>TABLE MAX OCCUPANCY REACHED</html>");  //Initialize the JLabel
      errorMessage.setBounds(25, 285, 225, 25); //Set the bounds for where it's displayed
      sidePanel.add(errorMessage);
      courseSubjects = new JComboBox(subjectStrings);
      courseSubjects.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               String subject = (String)courseSubjects.getSelectedItem();
               String[] list = initializeSubjectNumbers(subject);
               courseNumbers.removeAllItems();
               for(int i = 0; i < list.length; i++)
                  courseNumbers.addItem(list[i]);
            } });
      courseSubjects.setBounds(50, 320, 75, 25);   //Set the bounds for where it's displayed
      sidePanel.add(courseSubjects);
      courseNumbers = new JComboBox(initializeSubjectNumbers(classList.get(0).getSubjectCode()));
      courseNumbers.setBounds(175, 320, 75, 25);   //Set the bounds for where it's displayed
      sidePanel.add(courseNumbers);
      messageLabel = new JLabel("User Description:"); //Initialize the JLabel
      messageLabel.setBounds(50, 445, 200, 25); //Set the bounds for where it's displayed
      sidePanel.add(messageLabel);
      message = new JTextField(20);
      message.setBounds(50, 470, 200, 25);   //Set the bounds for where it's displayed
      message.setDocument(new JTextFieldLimit(140));  //Set the character limit to 140 characters
      sidePanel.add(message);
   }
   
   public static void initializeJoinTable() {
      joinTable = new JButton("Join Table");
      joinTable.setBounds(25, 285, 225, 25); //Set the bounds for where it's displayed
      joinTable.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(user.getOccupyTable()) {
                  leaveTable.setVisible(true);  //Display in the side panel
                  display.setEnabled(false);  //Disable the JButton
               } else {
                  System.out.println("1");
                  fenwickLibrary.getSelectedTable().addStudent(user);
                  manager.joinStudying(user.getMasonEmail(), Integer.parseInt(fenwickLibrary.getSelectedTable( ).getFloor() + "" + 
                       fenwickLibrary.getTableIndex(fenwickLibrary.getSelectedTable())));                  
                  user.setOccupyTable();
                  displayCourseOptions(false);  //Do not display the possible course select options
                  fenwickLibrary.setUserTable(fenwickLibrary.getSelectedTable());
                  fenwickLibrary.setTableSelected(false);
                  removeUserTable.setVisible(true);  //Display in the side panel
                  fenwickLibrary.updateMessages();  //Update the side panel messages
                  joinTable.setVisible(false);  //Hide from being displayed
                  fenwickLibrary.repaint();  //Repaint the map displaying the tables on the floor
               }
            } });  
      joinTable.setVisible(false);  //Hide from being displayed
      sidePanel.add(joinTable);
   }
   
   public static void initializeAddTable() {
      addTable = new JButton("Finish");
      addTable.setBounds(50, 500, 200, 25);  //Set the bounds for where it's displayed
      addTable.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(user.getOccupyTable()) {
                  leaveTable.setVisible(true);  //Display in the side panel
                  display.setEnabled(false);  //Disable the JButton
               } else {
                  fenwickLibrary.getSelectedTable().setOccupied();
                  System.out.println("2");
                  fenwickLibrary.getSelectedTable().addStudent(user);
                  user.setOccupyTable();
                  String course = (String)courseSubjects.getSelectedItem();
                  String number = (String)courseNumbers.getSelectedItem();
                  fenwickLibrary.getSelectedTable().setCourse(findClass(course, number));
                  courseSubjects.setSelectedIndex(0);
                  fenwickLibrary.getSelectedTable().setMessage(message.getText().toString());
                  String classInfo = course + number;
                  manager.startStudying(user.getMasonEmail(), Integer.parseInt( fenwickLibrary.getSelectedTable( ).getFloor( ) + "" + 
                       fenwickLibrary.getTableIndex(fenwickLibrary.getSelectedTable())), 
                       fenwickLibrary.getSelectedTable().getMessage(), classInfo);
                  message.setText("");   //Set the text to blank
                  displayCourseOptions(false);  //Do not display the possible course select options
                  fenwickLibrary.setUserTable(fenwickLibrary.getSelectedTable());
                  fenwickLibrary.setTableSelected(false);
                  fenwickLibrary.updateMessages();  //Update the side panel messages
                  removeUserTable.setVisible(true);  //Display in the side panel
                  fenwickLibrary.repaint();  //Repaint the map displaying the tables on the floor
               }
            } });  
      sidePanel.add(addTable);
      displayCourseOptions(false);  //Do not display the possible course select options
   }
   
   public static void initializeRemoveUserTable() {
      removeUserTable = new JButton("Leave Table");   //Intialize the JButton
      removeUserTable.setBounds(50, 750, 200, 25);    //Set the bounds for where it's displayed
      removeUserTable.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               fenwickLibrary.getUserTable().removeStudent(user); //Remove the user from the table
               if(fenwickLibrary.getUserTable().getNumStudents() == 0) {   //If the table has no students
                  fenwickLibrary.getUserTable().setCourse(new Class()); //Set the class to a basic object
                  fenwickLibrary.getUserTable().setMessage("");      //Set the message to blank
                  fenwickLibrary.getUserTable().setOccupied();       //Set occupied to false
               }
               fenwickLibrary.setUserTable(new Table());          //Set the user table to a blank option
               manager.stopStudying(user.getMasonEmail());        //*** AWS IMPLEMENTATION ***
               user.setOccupyTable();                             //Set user occupy table to false
               fenwickLibrary.updateMessages();  //Update the side panel messages
               fenwickLibrary.setTableSelected(false);            //Set selected table to false
               fenwickLibrary.repaint();  //Repaint the map displaying the tables on the floor
               removeUserTable.setVisible(false);  //Hide from being displayed                 //Hide the removeUserTable button
            } });  
      sidePanel.add(removeUserTable);
      removeUserTable.setVisible(false);  //Hide from being displayed
   }

      //Initialize the option to leave the table you are sitting at and choose another
   public static void initializeLeaveTable() {
      leaveContainer = new JPanel();
      leaveContainer.setLayout(null);
      yes = new JButton("YES");
      yes.setBounds(50, 75, 75, 25);   //Set the bounds for where it's displayed
      yes.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               fenwickLibrary.getUserTable().removeStudent(user);
               if(fenwickLibrary.getUserTable().getNumStudents() == 0) {
                  fenwickLibrary.getUserTable().setCourse(new Class());
                  fenwickLibrary.getUserTable().setMessage("");
                  fenwickLibrary.getUserTable().setOccupied();
               }
               manager.stopStudying(user.getMasonEmail());           //*** AWS IMPLEMENTATION ***
               System.out.println("3");
               fenwickLibrary.getSelectedTable().addStudent(user);
               if(!fenwickLibrary.getSelectedTable().getOccupied()) {
                  String course = (String)courseSubjects.getSelectedItem();
                  String number = (String)courseNumbers.getSelectedItem();
                  String classInfo = course + number;
                  fenwickLibrary.getSelectedTable().setCourse(findClass(course, number));
                  courseSubjects.setSelectedIndex(0);
                  fenwickLibrary.getSelectedTable().setMessage(message.getText().toString());
                  message.setText("");   //Set the text to blank
                  fenwickLibrary.getSelectedTable().setOccupied();
                  displayCourseOptions(false);  //Do not display the possible course select options
                  manager.startStudying(user.getMasonEmail(), Integer.parseInt( fenwickLibrary.getSelectedTable( ).getFloor( ) + "" + 
                       fenwickLibrary.getTableIndex(fenwickLibrary.getSelectedTable())), fenwickLibrary.getSelectedTable().getMessage(), classInfo); //*** AWS IMPLEMENTATION ***
               } else {
                  manager.joinStudying(user.getMasonEmail(), Integer.parseInt( fenwickLibrary.getSelectedTable( ).getFloor( ) + "" + 
                       fenwickLibrary.getTableIndex(fenwickLibrary.getSelectedTable())));  ////*** AWS IMPLEMENTATION ***
               }
               fenwickLibrary.setUserTable(fenwickLibrary.getSelectedTable());
               fenwickLibrary.setTableSelected(false);
               fenwickLibrary.updateMessages();  //Update the side panel messages
               removeUserTable.setVisible(true);  //Display in the side panel
               leaveTable.setVisible(false);  //Hide from being displayed
               display.setEnabled(true);  //Enable the JButton
               fenwickLibrary.repaint();  //Repaint the map displaying the tables on the floor
            } });        
      no = new JButton("NO");
      no.setBounds(175, 75, 75, 25);  //Set the bounds for where it's displayed
      no.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               leaveTable.setVisible(false);  //Hide from being displayed
               display.setEnabled(true);  //Enable the JButton
            } });            
      leaveMessage = new JLabel("<html>Would you like to leave the table you are currenlty sitting at and move to another?</html>");
      leaveMessage.setBounds(25, 25, 250, 35);  //Set the bounds for where it's displayed   
      leaveContainer.add(yes);   //Add to the JPanel
      leaveContainer.add(no);    //Add to the JPanel
      leaveContainer.add(leaveMessage);   //Add to the JPanel
      leaveTable.setContentPane(leaveContainer);
   }
   
   //Initialize the search features for the side panel
   public static void initializeSearchFeature() {
      searchTitle = new JLabel("Course Search");
      searchTitle.setBounds(100, 550, 100, 25);  //Set the bounds for where it's displayed
      sidePanel.add(searchTitle);   //Add to the JPanel
      searchTitle.setVisible(false);  //Hide from being displayed
      searchSubjects = new JComboBox(subjectStrings);
      searchSubjects.setBounds(50, 575, 75, 25); //Set the bounds for where it's displayed
      searchSubjects.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               String subject = (String)searchSubjects.getSelectedItem();
               String[] list = initializeSubjectNumbers(subject);
               searchNumbers.removeAllItems();
               for(int i = 0; i < list.length; i++)
                  searchNumbers.addItem(list[i]);
               fenwickLibrary.repaint();  //Repaint the map displaying the tables on the floor
            } });
      sidePanel.add(searchSubjects);   //Add to the JPanel
      searchSubjects.setVisible(false);  //Hide from being displayed  //Hide the search subjects feature
      searchNumbers = new JComboBox(initializeSubjectNumbers(classList.get(0).getSubjectCode()));
      searchNumbers.setBounds(175, 575, 75, 25);   //Set the bounds for where it's displayed
      searchNumbers.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               fenwickLibrary.repaint();  //Repaint the map displaying the tables on the floor
            } });         
      sidePanel.add(searchNumbers);
      searchNumbers.setVisible(false);  //Hide from being displayed
   }
   
   //Determine if the provided course and number are that of a real class
   public static Class findClass(String course, String number) {
      for(int i = 0; i < classList.size(); i++) {
         if(classList.get(i).getSubjectCode().equals(course) && classList.get(i).getNumber() == Integer.parseInt(number)) {
            return classList.get(i);
         }
      }
      return new Class();
   }
   
   //Display the name of the student
   public static void displayStudentName(ArrayList<Student> names) {
      if(names.size() == 1) {
         tableStudentName1.setText(names.get(0).getFirstName() + " " + names.get(0).getLastName() + " (" + names.get(0).getMasonEmail() + ")");
         tableStudentName2.setText("");   //Set the text to blank
         tableStudentName3.setText("");   //Set the text to blank
         tableStudentName4.setText("");   //Set the text to blank
      } else if(names.size() == 2) {
         tableStudentName1.setText(names.get(0).getFirstName() + " " + names.get(0).getLastName() + " (" + names.get(0).getMasonEmail() + ")");
         tableStudentName2.setText(names.get(1).getFirstName() + " " + names.get(1).getLastName() + " (" + names.get(1).getMasonEmail() + ")");
         tableStudentName3.setText("");   //Set the text to blank
         tableStudentName4.setText("");   //Set the text to blank
      } else if(names.size() == 3) {
         tableStudentName1.setText(names.get(0).getFirstName() + " " + names.get(0).getLastName() + " (" + names.get(0).getMasonEmail() + ")");
         tableStudentName2.setText(names.get(1).getFirstName() + " " + names.get(1).getLastName() + " (" + names.get(1).getMasonEmail() + ")");
         tableStudentName3.setText(names.get(2).getFirstName() + " " + names.get(2).getLastName() + " (" + names.get(2).getMasonEmail() + ")");
         tableStudentName4.setText("");   //Set the text to blank
      } else if(names.size() == 4) {
         tableStudentName1.setText(names.get(0).getFirstName() + " " + names.get(0).getLastName() + " (" + names.get(0).getMasonEmail() + ")");
         tableStudentName2.setText(names.get(1).getFirstName() + " " + names.get(1).getLastName() + " (" + names.get(1).getMasonEmail() + ")");
         tableStudentName3.setText(names.get(2).getFirstName() + " " + names.get(2).getLastName() + " (" + names.get(2).getMasonEmail() + ")");
         tableStudentName4.setText(names.get(3).getFirstName() + " " + names.get(3).getLastName() + " (" + names.get(3).getMasonEmail() + ")");
      } else {
         tableStudentName1.setText("");   //Set the text to blank
         tableStudentName2.setText("");   //Set the text to blank
         tableStudentName3.setText("");   //Set the text to blank
         tableStudentName4.setText("");   //Set the text to blank
      }
   }
   
   //Display the message that the user wants to send to other users
   public static void displayMessage(String message) {
      tableMessage.setText("<html>" + message + "</html>");
   }
   
   //Display the course that the user/student is studying for
   public static void displayTableCourse(Class course) {
      if(course.getNumber() == 0)
         tableClass.setText(course.getSubjectCode());
      else
         tableClass.setText(course.getSubjectCode() + " " + course.getNumber());
   }
   
   //Reset the JLabel for the table display
   public static void resetTableCourse() {
      tableClass.setText("");   //Set the text to blank
   }
   
   //Display the error message that the table is at max occupancy
   public static void showErrorMessage(boolean show) {
      errorMessage.setVisible(show);
   }
   
   //Show the add friend buttons
   public static void showAddFriend(Table table, int size) {
      if(size == 1) {
         if(!table.getStudents().get(0).getMasonEmail().equals(user.getMasonEmail())) {   //If student Mason email is not equal to the user mason email
            if(user.getFriends().contains(table.getStudents().get(0).getMasonEmail()))    //If friend list contains the students mason email
               addFriend1.setText("Unfollow");  //Set the text to unfollow 
            else
               addFriend1.setText("Follow");      //Set the text to follow
            addFriend1.setVisible(true);  //Display in the side panel
         }
         else 
            addFriend1.setVisible(false);  //Hide from being displayed
         addFriend2.setVisible(false);  //Hide from being displayed
         addFriend3.setVisible(false);  //Hide from being displayed
         addFriend4.setVisible(false);  //Hide from being displayed
      } else if(size == 2) {
         if(!table.getStudents().get(0).getMasonEmail().equals(user.getMasonEmail())) {   //If student Mason email is not equal to the user mason email
            if(user.getFriends().contains(table.getStudents().get(0).getMasonEmail())) 
               addFriend1.setText("Unfollow");  //Set the text to unfollow
            else
               addFriend1.setText("Follow");      //Set the text to follow
            addFriend1.setVisible(true);  //Display in the side panel
         }
         else 
            addFriend1.setVisible(false);  //Hide from being displayed
         if(!table.getStudents().get(1).getMasonEmail().equals(user.getMasonEmail())) {   //If student Mason email is not equal to the user mason email
            if(user.getFriends().contains(table.getStudents().get(1).getMasonEmail())) 
               addFriend2.setText("Unfollow");  //Set the text to unfollow 
            else
               addFriend2.setText("Follow");      //Set the text to follow
            addFriend2.setVisible(true);  //Display in the side panel
         }
         else 
            addFriend2.setVisible(false);  //Hide from being displayed
         addFriend3.setVisible(false);  //Hide from being displayed
         addFriend4.setVisible(false);  //Hide from being displayed
      } else if(size == 3) {
         if(!table.getStudents().get(0).getMasonEmail().equals(user.getMasonEmail())) {   //If student Mason email is not equal to the user mason email
            if(user.getFriends().contains(table.getStudents().get(0).getMasonEmail())) 
               addFriend1.setText("Unfollow");  //Set the text to unfollow
            else
               addFriend1.setText("Follow");      //Set the text to follow
            addFriend1.setVisible(true);  //Display in the side panel
         }
         else 
            addFriend1.setVisible(false);  //Hide from being displayed
         if(!table.getStudents().get(1).getMasonEmail().equals(user.getMasonEmail())) {   //If student Mason email is not equal to the user mason email
            if(user.getFriends().contains(table.getStudents().get(1).getMasonEmail())) 
               addFriend2.setText("Unfollow");  //Set the text to unfollow 
            else
               addFriend2.setText("Follow");      //Set the text to follow
            addFriend2.setVisible(true);  //Display in the side panel
         }
         else 
            addFriend2.setVisible(false);  //Hide from being displayed
         if(!table.getStudents().get(2).getMasonEmail().equals(user.getMasonEmail())) {   //If student Mason email is not equal to the user mason email
            if(user.getFriends().contains(table.getStudents().get(2).getMasonEmail())) 
               addFriend3.setText("Unfollow");  //Set the text to unfollow  //Set the text to unfollow
            else
               addFriend3.setText("Follow");      //Set the text to follow
            addFriend3.setVisible(true);  //Display in the side panel
         }
         else 
            addFriend3.setVisible(false);  //Hide from being displayed
         addFriend4.setVisible(false);  //Hide from being displayed
      } else if(size == 4) {
         if(!table.getStudents().get(0).getMasonEmail().equals(user.getMasonEmail())) {   //If student Mason email is not equal to the user mason email
            if(user.getFriends().contains(table.getStudents().get(0).getMasonEmail())) 
               addFriend1.setText("Unfollow");  //Set the text to unfollow
            else
               addFriend1.setText("Follow");      //Set the text to follow
            addFriend1.setVisible(true);  //Display in the side panel
         }
         else 
            addFriend1.setVisible(false);  //Hide from being displayed //Hide the addFriend1 button           
         if(!table.getStudents().get(1).getMasonEmail().equals(user.getMasonEmail())) {   //If student Mason email is not equal to the user mason email
            if(user.getFriends().contains(table.getStudents().get(1).getMasonEmail())) 
               addFriend2.setText("Unfollow");  //Set the text to unfollow
            else
               addFriend2.setText("Follow");      //Set the text to follow
            addFriend2.setVisible(true);  //Display in the side panel
         }
         else 
            addFriend2.setVisible(false);  //Hide from being displayed //Hide the addFriend2 button           
         if(!table.getStudents().get(2).getMasonEmail().equals(user.getMasonEmail())) {   //If student Mason email is not equal to the user mason email
            if(user.getFriends().contains(table.getStudents().get(2).getMasonEmail())) 
               addFriend3.setText("Unfollow");  //Set the text to unfollow
            else
               addFriend3.setText("Follow");      //Set the text to follow
            addFriend3.setVisible(true);  //Display in the side panel
         }
         else 
            addFriend3.setVisible(false);  //Hide from being displayed
         if(!table.getStudents().get(3).getMasonEmail().equals(user.getMasonEmail())) {   //If student Mason email is not equal to the user mason email
            if(user.getFriends().contains(table.getStudents().get(3).getMasonEmail())) 
               addFriend4.setText("Unfollow");  //Set the text to unfollow
            else
               addFriend4.setText("Follow");      //Set the text to follow
            addFriend4.setVisible(true);  //Display in the side panel
         }
         else 
            addFriend4.setVisible(false);  //Hide from being displayed
      } else  {
         addFriend1.setVisible(false);  //Hide from being displayed //Hide the addFriend1 button
         addFriend2.setVisible(false);  //Hide from being displayed
         addFriend3.setVisible(false);  //Hide from being displayed
         addFriend4.setVisible(false);  //Hide from being displayed
      }
   }
   
   public static void showJoinTable(boolean show) {
      joinTable.setVisible(show);
   }
   
   //Initialize the classes that are provided from the text file
   public static void initializeClasses() throws IOException {
      File file = new File("dataFiles/classes.txt");
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line = "";
      while ((line = reader.readLine()) != null) {
         String[] masonClass = line.split("=");
         if(!line.substring(0,2).equals("//")) {
            classList.add(new Class(masonClass[0], masonClass[1], Integer.parseInt(masonClass[2]), masonClass[3]));
         }
      }
      reader.close();
   }
   
   //Initialize the subject numbers for each of the courses
   public static String[] initializeSubjectNumbers(String code) {
      ArrayList<Integer> codes = new ArrayList<Integer>();
      for(int i = 0; i < classList.size(); i++) {
         if(code.equals(classList.get(i).getSubjectCode())) {
            codes.add(classList.get(i).getNumber());
         }
      }
      String[] finalCodes = new String[codes.size()];
      for(int i = 0; i < codes.size(); i++) {
         finalCodes[i] = String.valueOf(codes.get(i));
      }
      return finalCodes;
   }
   
   public static class listen implements KeyListener 
   { 
      public void keyTyped(KeyEvent e)
      {
         
      }
      
      public void keyPressed(KeyEvent e)
      {
         
      }
      
      public void keyReleased(KeyEvent e)
      {
      
      }
   }

}
