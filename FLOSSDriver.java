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
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyListener;
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
   
   //JButtons to display the floors and the search feature
   public static JButton floor1;       
   public static JButton floor2;          
   public static JButton floor3;       
   public static JButton search;
   public static JButton removeUserTable;       
   
   //JButtons to display whether to leave a table or not
   public static JDialog leaveTable;
   public static JPanel leaveContainer;
   public static JButton yes;
   public static JButton no;
   public static JLabel leaveMessage;
   
   //Floor Information text labels
   public static JLabel tableStudentName1;
   public static JButton addFriend1;
   public static JLabel tableStudentName2;
   public static JButton addFriend2;
   public static JLabel tableStudentName3;
   public static JButton addFriend3;
   public static JLabel tableStudentName4;
   public static JButton addFriend4;
   public static JLabel tableClass;
   public static JLabel tableMessage;
   public static JLabel errorMessage;
   public static JButton joinTable;
   
   //Classes list ComboBox
   public static String[] subjectStrings = {"ACCT", "AFAM", "ANTH", "ARAB", "ARTH", "EDAT", "ASTR", "ATEP", "BENG", "BINF", "BENG", "BIOL", "BUS", "BULE", "CHEM", "CHIN", "CEIE", "CLAS", "CLIM", "COS", "CVPA", "COMM", "CDS", "GAME", "CS", "CONF", "CRIM", "CULT", "CYSE", "DANC", "DSGN", "ECED", "ECON", "EDIT", "EDUC", "EDPO", "EDPS", "ECE", "ELED", "ENGR", "ENGH", "ENVPP", "FAVS", "FNAN", "FRLN", "FRSC", "FREN", "GGS", "GEOL", "GERM", "GLOA", "GCH", "GOVT", "HEAL", "HAP", "HHS", "HEBR", "HIST", "HNRS", "HNRT", "HDFS", "IT", "INTS", "ITAL", "JAPA", "KINE", "KORE", "LATN", "LING", "MGMT", "MIS", "MKTG", "MATH", "ME", "MLAB", "MLSC", "MBUS", "NEUR", "NURS", "NUTR", "OM", "OR", "PRLS", "PERS", "PHIL", "PHED", "PHYS", "PORT", "PROV", "PSYC", "EDRD", "RHBS", "RELI", "RUSS", "SOCW", "SOCI", "SWE", "SPAN", "EDSE", "SPMT", "SRST", "STAT", "SYST", "THR", "TOUR", "TURK", "UNIV", "WMST"};
   public static JComboBox courseSubjects;
   public static JComboBox courseNumbers;
   public static JLabel messageLabel;
   public static JTextField message;
   public static JButton addTable;
   
   //Search feature ComboBoxes
   public static JLabel searchTitle;
   public static JComboBox searchSubjects;
   public static JComboBox searchNumbers;
   
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
      
      JPanel container = new JPanel();          //Establish the JPanel that will hold all our JPanel's
      container.setLayout(null);                //Set the layout to null so we can set the bounds
   
      fenwickLibrary = new StudySystemMap();       //Initialize the interactive Fenwick library map
      fenwickLibrary.setBounds(0, 0, 800, 800);    //Set the bounds for the interactive map
      displaySidePanel();                          //Add all the buttons and options
      initializeClasses();                         //Initialize the class list
      initializeSidePanel();                       //Initialize the side panel
      initializeJoinTable();
      initializeAddTable();
      initializeRemoveUserTable();
      initializeSearchFeature();                   //Initialize the search feature for the class
   
      container.add(fenwickLibrary);         //Add JPanel map to the main display screen 
      container.add(sidePanel);              //Add JPanel sidePanel to the main display screen 
      display.setContentPane(container);     //Set the contents of the JFrame display to the container holding all the display info              
      
      leaveTable = new JDialog();            //Create our JFrame
      leaveTable.setSize(300, 150);			   //Size of display window (600, 300)
      leaveTable.setLocation(600, 300);	   //Location of display window on the screen
      leaveTable.setResizable(false);        //Cannot change size of the screen
      leaveTable.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  //Exit when close out
      initializeLeaveTable();
      
      Login login = new Login(display);
      login.setVisible(true);
   }
   
   public static Student getUser() {
      return user;
   }
   
   public static RemoteDBManager getManager() {
      return manager;
   }
   
   public static String getSearchSubject() {
      return (String)searchSubjects.getSelectedItem();
   }
   
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
               enableButtons();
               floor1.setEnabled(false);
               fenwickLibrary.setFloorNumber(1);
               fenwickLibrary.setTableSelected(false);
               fenwickLibrary.setSelectedTable(fenwickLibrary.getUserTable());
               fenwickLibrary.updateMessages();
               displayCourseOptions(false);
               fenwickLibrary.repaint();
            } });
      floor1.setEnabled(false);   
      floor2.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               enableButtons();
               floor2.setEnabled(false);
               fenwickLibrary.setFloorNumber(2);
               fenwickLibrary.setTableSelected(false);
               fenwickLibrary.setSelectedTable(fenwickLibrary.getUserTable());
               fenwickLibrary.updateMessages();
               displayCourseOptions(false);
               fenwickLibrary.repaint();
            } });    
      floor3.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               enableButtons();
               floor3.setEnabled(false);
               fenwickLibrary.setFloorNumber(3);
               fenwickLibrary.setTableSelected(false);
               fenwickLibrary.setSelectedTable(fenwickLibrary.getUserTable());
               fenwickLibrary.updateMessages();
               displayCourseOptions(false);
               fenwickLibrary.repaint();
            } });  
      search.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               enableButtons();
               enableSearch();
               if(fenwickLibrary.getFloorNumber() == 1) 
                  floor1.setEnabled(false);
               else if(fenwickLibrary.getFloorNumber() == 2) 
                  floor2.setEnabled(false);
               else if(fenwickLibrary.getFloorNumber() == 3) 
                  floor3.setEnabled(false);
               fenwickLibrary.repaint();
            } });  
      sidePanel.add(floor1);      //Add button to the JFrame
      sidePanel.add(floor2);      //Add button to the JFrame
      sidePanel.add(floor3);      //Add button to the JFrame
      sidePanel.add(search);      //Add button to the JFrame
   }
   
   public static void enableSearch() {
      if(enableSearch) {
         fenwickLibrary.setSearchEnabled(false);
         searchSubjects.setVisible(false);
         searchNumbers.setVisible(false);
         searchTitle.setVisible(false);
         enableSearch = false;
      } else {
         fenwickLibrary.setSearchEnabled(true);
         searchSubjects.setVisible(true);
         searchNumbers.setVisible(true);
         searchTitle.setVisible(true);
         enableSearch = true;
      }
   }
   
   //Enable the buttons for the floors
   public static void enableButtons() {
      floor1.setEnabled(true);
      floor2.setEnabled(true);
      floor3.setEnabled(true);
   }
   
   //Whether or not we want to display certain aspects of our side panel
   public static void displayCourseOptions(boolean display) {
      courseSubjects.setVisible(display);
      courseNumbers.setVisible(display);
      messageLabel.setVisible(display);
      message.setVisible(display);
      addTable.setVisible(display);
   }
   
   //Method that initializes the side panel and all the JLabel and JComboBox features
   public static void initializeSidePanel() {
      tableStudentName1 = new JLabel("Student 1");
      tableStudentName1.setBounds(25, 60, 200, 25);
      sidePanel.add(tableStudentName1);
      addFriend1 = new JButton("Follow");
      addFriend1.setBounds(200, 60, 85, 15);
      addFriend1.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(addFriend1.getText().equals("Follow")) {
                  user.addFriend(fenwickLibrary.getSelectedTable().getStudent(0).getMasonEmail());
                  addFriend1.setText("Unfollow");
               } else {
                  user.removeFriend(fenwickLibrary.getSelectedTable().getStudent(0).getMasonEmail());
                  addFriend1.setText("Follow");
               }
            } });
      addFriend1.setVisible(false);
      sidePanel.add(addFriend1);
      tableStudentName2 = new JLabel("Student 2");
      tableStudentName2.setBounds(25, 85, 200, 25);
      sidePanel.add(tableStudentName2);
      addFriend2 = new JButton("Follow");
      addFriend2.setBounds(200, 85, 85, 15);
      addFriend2.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(addFriend2.getText().equals("Follow")) {
                  user.addFriend(fenwickLibrary.getSelectedTable().getStudent(1).getMasonEmail());
                  addFriend2.setText("Unfollow");
               } else {
                  user.removeFriend(fenwickLibrary.getSelectedTable().getStudent(1).getMasonEmail());
                  addFriend2.setText("Follow");
               }
            } });
      addFriend2.setVisible(false);
      sidePanel.add(addFriend2);
      tableStudentName3 = new JLabel("Student 3");
      tableStudentName3.setBounds(25, 110, 200, 25);
      sidePanel.add(tableStudentName3);
      addFriend3 = new JButton("Follow");
      addFriend3.setBounds(200, 110, 85, 15);
      addFriend3.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(addFriend3.getText().equals("Follow")) {
                  user.addFriend(fenwickLibrary.getSelectedTable().getStudent(2).getMasonEmail());
                  addFriend3.setText("Unfollow");
               } else {
                  user.removeFriend(fenwickLibrary.getSelectedTable().getStudent(2).getMasonEmail());
                  addFriend3.setText("Follow");
               }
            } });
      addFriend3.setVisible(false);
      sidePanel.add(addFriend3);
      tableStudentName4 = new JLabel("Student 4");
      tableStudentName4.setBounds(25, 135, 200, 25);
      sidePanel.add(tableStudentName4);
      addFriend4 = new JButton("Follow");
      addFriend4.setBounds(200, 135, 85, 15);
      addFriend4.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(addFriend4.getText().equals("Follow")) {
                  user.addFriend(fenwickLibrary.getSelectedTable().getStudent(3).getMasonEmail());
                  addFriend4.setText("Unfollow");
               } else {
                  user.removeFriend(fenwickLibrary.getSelectedTable().getStudent(3).getMasonEmail());
                  addFriend4.setText("Follow");
               }
            } });
      addFriend4.setVisible(false);
      sidePanel.add(addFriend4);
      tableClass = new JLabel("");
      tableClass.setBounds(25, 165, 200, 25);
      sidePanel.add(tableClass);
      tableMessage = new JLabel("");
      tableMessage.setBounds(25, 195, 200, 80);
      sidePanel.add(tableMessage);
      errorMessage = new JLabel("<html>TABLE MAX OCCUPANCY REACHED</html>");
      errorMessage.setBounds(25, 285, 225, 25);
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
      courseSubjects.setBounds(50, 320, 75, 25);
      sidePanel.add(courseSubjects);
      courseNumbers = new JComboBox(initializeSubjectNumbers(classList.get(0).getSubjectCode()));
      courseNumbers.setBounds(175, 320, 75, 25);
      sidePanel.add(courseNumbers);
      messageLabel = new JLabel("User Description:");
      messageLabel.setBounds(50, 445, 200, 25);
      sidePanel.add(messageLabel);
      message = new JTextField(20);
      message.setBounds(50, 470, 200, 25);
      message.setDocument(new JTextFieldLimit(140));
      sidePanel.add(message);
   }
   
   public static void initializeJoinTable() {
      joinTable = new JButton("Join Table");
      joinTable.setBounds(25, 285, 225, 25);
      joinTable.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(user.getOccupyTable()) {
                  leaveTable.setVisible(true);
                  display.setEnabled(false);
               } else {
                  fenwickLibrary.getSelectedTable().addStudent(user);
                  manager.joinStudying(user.getMasonEmail(), Integer.parseInt(fenwickLibrary.getSelectedTable( ).getFloor() + "" + fenwickLibrary.getSelectedTable().getID()));                  
                  user.setOccupyTable();
                  displayCourseOptions(false);
                  fenwickLibrary.setUserTable(fenwickLibrary.getSelectedTable());
                  fenwickLibrary.setTableSelected(false);
                  removeUserTable.setVisible(true);
                  fenwickLibrary.updateMessages();
                  joinTable.setVisible(false);
                  fenwickLibrary.repaint();
               }
            } });  
      joinTable.setVisible(false);
      sidePanel.add(joinTable);
   }
   
   public static void initializeAddTable() {
      addTable = new JButton("Finish");
      addTable.setBounds(50, 500, 200, 25);
      addTable.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(user.getOccupyTable()) {
                  leaveTable.setVisible(true);
                  display.setEnabled(false);
               } else {
                  fenwickLibrary.getSelectedTable().setOccupied();
                  fenwickLibrary.getSelectedTable().addStudent(user);
                  user.setOccupyTable();
                  String course = (String)courseSubjects.getSelectedItem();
                  String number = (String)courseNumbers.getSelectedItem();
                  fenwickLibrary.getSelectedTable().setCourse(findClass(course, number));
                  courseSubjects.setSelectedIndex(0);
                  fenwickLibrary.getSelectedTable().setMessage(message.getText().toString());
                  String classInfo = course + number;
                  manager.startStudying(user.getMasonEmail(), Integer.parseInt( fenwickLibrary.getSelectedTable( ).getFloor( ) + "" + fenwickLibrary.getSelectedTable().getID()), 
                		  fenwickLibrary.getSelectedTable().getMessage(), classInfo);
                  message.setText("");
                  displayCourseOptions(false);
                  fenwickLibrary.setUserTable(fenwickLibrary.getSelectedTable());
                  fenwickLibrary.setTableSelected(false);
                  fenwickLibrary.updateMessages();
                  removeUserTable.setVisible(true);
                  fenwickLibrary.repaint();
               }
            } });  
      sidePanel.add(addTable);
      displayCourseOptions(false);
   }
   
   public static void initializeRemoveUserTable() {
      removeUserTable = new JButton("Leave Table");
      removeUserTable.setBounds(50, 750, 200, 25);
      removeUserTable.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               fenwickLibrary.getUserTable().removeStudent(user);
               System.out.println(fenwickLibrary.getUserTable().getNumStudents());
               if(fenwickLibrary.getUserTable().getNumStudents() == 0) {
                  fenwickLibrary.getUserTable().setCourse(new Class());
                  fenwickLibrary.getUserTable().setMessage("");
                  fenwickLibrary.getUserTable().setOccupied();
               }
               fenwickLibrary.setUserTable(new Table());
               manager.stopStudying(user.getMasonEmail());      //*** AWS IMPLEMENTATION ***
               user.setOccupyTable();
               fenwickLibrary.updateMessages();
               fenwickLibrary.setTableSelected(false);
               fenwickLibrary.repaint();
               removeUserTable.setVisible(false);
            } });  
      sidePanel.add(removeUserTable);
      removeUserTable.setVisible(false);
   }

      //Initialize the option to leave the table you are sitting at and choose another
   public static void initializeLeaveTable() {
      leaveContainer = new JPanel();
      leaveContainer.setLayout(null);
      yes = new JButton("YES");
      yes.setBounds(50, 75, 75, 25); 
      yes.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               fenwickLibrary.getUserTable().removeStudent(user);
               System.out.println(fenwickLibrary.getUserTable().getNumStudents());
               if(fenwickLibrary.getUserTable().getNumStudents() == 0) {
                  fenwickLibrary.getUserTable().setCourse(new Class());
                  fenwickLibrary.getUserTable().setMessage("");
                  fenwickLibrary.getUserTable().setOccupied();
               }
               manager.stopStudying(user.getMasonEmail());           //*** AWS IMPLEMENTATION ***
               fenwickLibrary.getSelectedTable().addStudent(user);
               if(!fenwickLibrary.getSelectedTable().getOccupied()) {
                  String course = (String)courseSubjects.getSelectedItem();
                  String number = (String)courseNumbers.getSelectedItem();
                  String classInfo = course + number;
                  fenwickLibrary.getSelectedTable().setCourse(findClass(course, number));
                  courseSubjects.setSelectedIndex(0);
                  fenwickLibrary.getSelectedTable().setMessage(message.getText().toString());
                  message.setText("");
                  fenwickLibrary.getSelectedTable().setOccupied();
                  displayCourseOptions(false);
                  manager.startStudying(user.getMasonEmail(), fenwickLibrary.getSelectedTable().getID(), fenwickLibrary.getSelectedTable().getMessage(), classInfo); //*** AWS IMPLEMENTATION ***
               } else {
                  manager.joinStudying(user.getMasonEmail(), fenwickLibrary.getSelectedTable().getID());  ////*** AWS IMPLEMENTATION ***
               }
               fenwickLibrary.setUserTable(fenwickLibrary.getSelectedTable());
               fenwickLibrary.setTableSelected(false);
               fenwickLibrary.updateMessages();
               removeUserTable.setVisible(true);
               leaveTable.setVisible(false);
               display.setEnabled(true);
               fenwickLibrary.repaint();
            } });        
      no = new JButton("NO");
      no.setBounds(175, 75, 75, 25);  
      no.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               leaveTable.setVisible(false);
               display.setEnabled(true);
            } });            
      leaveMessage = new JLabel("<html>Would you like to leave the table you are currenlty sitting at and move to another?</html>");
      leaveMessage.setBounds(25, 25, 250, 35);   
      leaveContainer.add(yes);
      leaveContainer.add(no);
      leaveContainer.add(leaveMessage);
      leaveTable.setContentPane(leaveContainer);
   }
   
   //Initialize the search features for the side panel
   public static void initializeSearchFeature() {
      searchTitle = new JLabel("Course Search");
      searchTitle.setBounds(100, 550, 100, 25);
      sidePanel.add(searchTitle);
      searchTitle.setVisible(false);
      searchSubjects = new JComboBox(subjectStrings);
      searchSubjects.setBounds(50, 575, 75, 25);
      searchSubjects.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               String subject = (String)searchSubjects.getSelectedItem();
               String[] list = initializeSubjectNumbers(subject);
               searchNumbers.removeAllItems();
               for(int i = 0; i < list.length; i++)
                  searchNumbers.addItem(list[i]);
               fenwickLibrary.repaint();
            } });
      sidePanel.add(searchSubjects);
      searchSubjects.setVisible(false);
      searchNumbers = new JComboBox(initializeSubjectNumbers(classList.get(0).getSubjectCode()));
      searchNumbers.setBounds(175, 575, 75, 25);
      searchNumbers.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               fenwickLibrary.repaint();
            } });         
      sidePanel.add(searchNumbers);
      searchNumbers.setVisible(false);
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
         tableStudentName2.setText("");
         tableStudentName3.setText("");
         tableStudentName4.setText("");
      } else if(names.size() == 2) {
         tableStudentName1.setText(names.get(0).getFirstName() + " " + names.get(0).getLastName() + " (" + names.get(0).getMasonEmail() + ")");
         tableStudentName2.setText(names.get(1).getFirstName() + " " + names.get(1).getLastName() + " (" + names.get(1).getMasonEmail() + ")");
         tableStudentName3.setText("");
         tableStudentName4.setText("");
      } else if(names.size() == 3) {
         tableStudentName1.setText(names.get(0).getFirstName() + " " + names.get(0).getLastName() + " (" + names.get(0).getMasonEmail() + ")");
         tableStudentName2.setText(names.get(1).getFirstName() + " " + names.get(1).getLastName() + " (" + names.get(1).getMasonEmail() + ")");
         tableStudentName3.setText(names.get(2).getFirstName() + " " + names.get(2).getLastName() + " (" + names.get(2).getMasonEmail() + ")");
         tableStudentName4.setText("");
      } else if(names.size() == 4) {
         tableStudentName1.setText(names.get(0).getFirstName() + " " + names.get(0).getLastName() + " (" + names.get(0).getMasonEmail() + ")");
         tableStudentName2.setText(names.get(1).getFirstName() + " " + names.get(1).getLastName() + " (" + names.get(1).getMasonEmail() + ")");
         tableStudentName3.setText(names.get(2).getFirstName() + " " + names.get(2).getLastName() + " (" + names.get(2).getMasonEmail() + ")");
         tableStudentName4.setText(names.get(3).getFirstName() + " " + names.get(3).getLastName() + " (" + names.get(3).getMasonEmail() + ")");
      } else {
         tableStudentName1.setText("");
         tableStudentName2.setText("");
         tableStudentName3.setText("");
         tableStudentName4.setText("");
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
      tableClass.setText("");
   }
   
   public static void showErrorMessage(boolean show) {
      errorMessage.setVisible(show);
   }
   
   public static void showAddFriend(Table table, int size) {
      if(size == 1) {
         if(!table.getStudents().get(0).getMasonEmail().equals(user.getMasonEmail())) {
            if(user.getFriends().contains(table.getStudents().get(0).getMasonEmail())) 
               addFriend1.setText("Unfollow");
            else
               addFriend1.setText("Follow");
            addFriend1.setVisible(true);
         }
         else 
            addFriend1.setVisible(false);
         addFriend2.setVisible(false);
         addFriend3.setVisible(false);
         addFriend4.setVisible(false);
      } else if(size == 2) {
         if(!table.getStudents().get(0).getMasonEmail().equals(user.getMasonEmail())) {
            if(user.getFriends().contains(table.getStudents().get(0).getMasonEmail())) 
               addFriend1.setText("Unfollow");
            else
               addFriend1.setText("Follow");
            addFriend1.setVisible(true);
         }
         else 
            addFriend1.setVisible(false);
         if(!table.getStudents().get(1).getMasonEmail().equals(user.getMasonEmail())) {
            if(user.getFriends().contains(table.getStudents().get(1).getMasonEmail())) 
               addFriend2.setText("Unfollow");
            else
               addFriend2.setText("Follow");
            addFriend2.setVisible(true);
         }
         else 
            addFriend2.setVisible(false);
         addFriend3.setVisible(false);
         addFriend4.setVisible(false);
      } else if(size == 3) {
         if(!table.getStudents().get(0).getMasonEmail().equals(user.getMasonEmail())) {
            if(user.getFriends().contains(table.getStudents().get(0).getMasonEmail())) 
               addFriend1.setText("Unfollow");
            else
               addFriend1.setText("Follow");
            addFriend1.setVisible(true);
         }
         else 
            addFriend1.setVisible(false);
         if(!table.getStudents().get(1).getMasonEmail().equals(user.getMasonEmail())) {
            if(user.getFriends().contains(table.getStudents().get(1).getMasonEmail())) 
               addFriend2.setText("Unfollow");
            else
               addFriend2.setText("Follow");
            addFriend2.setVisible(true);
         }
         else 
            addFriend2.setVisible(false);
         if(!table.getStudents().get(2).getMasonEmail().equals(user.getMasonEmail())) {
            if(user.getFriends().contains(table.getStudents().get(2).getMasonEmail())) 
               addFriend3.setText("Unfollow");
            else
               addFriend3.setText("Follow");
            addFriend3.setVisible(true);
         }
         else 
            addFriend3.setVisible(false);
         addFriend4.setVisible(false);
      } else if(size == 4) {
         if(!table.getStudents().get(0).getMasonEmail().equals(user.getMasonEmail())) {
            if(user.getFriends().contains(table.getStudents().get(0).getMasonEmail())) 
               addFriend1.setText("Unfollow");
            else
               addFriend1.setText("Follow");
            addFriend1.setVisible(true);
         }
         else 
            addFriend1.setVisible(false);            
         if(!table.getStudents().get(1).getMasonEmail().equals(user.getMasonEmail())) {
            if(user.getFriends().contains(table.getStudents().get(1).getMasonEmail())) 
               addFriend2.setText("Unfollow");
            else
               addFriend2.setText("Follow");
            addFriend2.setVisible(true);
         }
         else 
            addFriend2.setVisible(false);            
         if(!table.getStudents().get(2).getMasonEmail().equals(user.getMasonEmail())) {
            if(user.getFriends().contains(table.getStudents().get(2).getMasonEmail())) 
               addFriend3.setText("Unfollow");
            else
               addFriend3.setText("Follow");
            addFriend3.setVisible(true);
         }
         else 
            addFriend3.setVisible(false);
         if(!table.getStudents().get(3).getMasonEmail().equals(user.getMasonEmail())) {
            if(user.getFriends().contains(table.getStudents().get(3).getMasonEmail())) 
               addFriend4.setText("Unfollow");
            else
               addFriend4.setText("Follow");
            addFriend4.setVisible(true);
         }
         else 
            addFriend4.setVisible(false);
      } else  {
         addFriend1.setVisible(false);
         addFriend2.setVisible(false);
         addFriend3.setVisible(false);
         addFriend4.setVisible(false);
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
