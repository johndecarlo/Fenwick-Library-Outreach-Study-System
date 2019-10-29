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

@SuppressWarnings("unchecked")
public class FLOSSDriver {

   public static StudySystemMap fenwickLibrary;    //JPanel that holds the study maps
   public static JPanel sidePanel;                 //JPanel that is the side information panel
   public static ArrayList<Class> classList = new ArrayList<Class>();       //List of most of the classes offered at GMU
   
   public static JButton floor1;       //When pressed will display first floor information
   public static JButton floor2;       //When pressed will display second floor information     
   public static JButton floor3;       //When pressed will display third floor information
   public static JButton search;       //When pressed will display search table information
   public static JButton profile;      //When pressed will display the profile information
   
   //Floor Information text labels
   public static JLabel tableStudentName;
   public static JLabel tableClass;
   public static JLabel tableMessage;
   public static JLabel messageLabel;
   public static JTextField message;
   
   //Classes list ComboBox
   public static String[] subjectStrings = {"ACCT", "AFAM", "ANTH", "ARAB", "ARTH", "EDAT", "ASTR", "ATEP", "BENG", "BINF", "BENG", "BIOL", "BUS", "BULE", "CHEM", "CHIN", "CEIE", "CLAS", "CLIM", "COS", "CVPA", "COMM", "CDS", "GAME", "CS", "CONF", "CRIM", "CULT", "CYSE", "DANC", "DSGN", "ECED", "ECON", "EDIT", "EDUC", "EDPO", "EDPS", "ECE", "ELED", "ENGR", "ENGH", "ENVPP", "FAVS", "FNAN", "FRLN", "FRSC", "FREN", "GGS", "GEOL", "GERM", "GLOA", "GCH", "GOVT", "HEAL", "HAP", "HHS", "HEBR", "HIST", "HNRS", "HNRT", "HDFS", "IT", "INTS", "ITAL", "JAPA", "KINE", "KORE", "LATN", "LING", "MGMT", "MIS", "MKTG", "MATH", "ME", "MLAB", "MLSC", "MBUS", "NEUR", "NURS", "NUTR", "OM", "OR", "PRLS", "PERS", "PHIL", "PHED", "PHYS", "PORT", "PROV", "PSYC", "EDRD", "RHBS", "RELI", "RUSS", "SOCW", "SOCI", "SWE", "SPAN", "EDSE", "SPMT", "SRST", "STAT", "SYST", "THR", "TOUR", "TURK", "UNIV", "WMST"};
   public static JComboBox courseSubjects;
   public static JComboBox courseNumbers;
   public static JButton addTable;
   
   //Search tool text labels
   public static JLabel searchTitle;
   
   //Profile JPanel text labels
   public static JLabel profileName;      //Profile display name
   public static JLabel profileYear;      //Profile display year
   public static JLabel profileMajor;     //Profile display major
   public static JLabel profileMinor;     //Profile display minor
   public static JLabel profileGnumber;   //Profile display g-number
   
   public static void main(String[]args) throws IOException {
      JFrame display = new JFrame("FLOSS");     //Create our JFrame
      display.setSize(1112, 830);			      //Size of display window
      display.setLocation(250, 0);				   //Location of display window on the screen
      display.setResizable(false);              //Cannot change size of the screen
      display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //Exit when close out 
      
      JPanel container = new JPanel();          //Establish the JPanel that will hold all our JPanel's
      container.setLayout(null);                //Set the layout to null so we can set the bounds
   
      fenwickLibrary = new StudySystemMap();       //Initialize the interactive Fenwick library map
      fenwickLibrary.setBounds(0, 0, 800, 800);    //Set the bounds for the interactive map
      displaySidePanel();                            //Add all the buttons and options
      initializeClasses();                         //Initialize the class list
      initializeSidePanel();
   
      container.add(fenwickLibrary);   //Add JPanel map to the main display screen 
      container.add(sidePanel);        //Add JPanel sidePanel to the main display screen 
      display.setContentPane(container);  //Set the contents of the JFrame display to the container holding all the display info
      display.setVisible(true);
      
      //Login login = new Login(display);
      //login.setVisible(true);
   }
   
   public static void displaySidePanel()
   {
      sidePanel = new JPanel();
      sidePanel.setBounds(800, 0, 300, 800);
      sidePanel.setLayout(null);
      floor1 = new JButton("1F");   //Initialize the JButton
      floor2 = new JButton("2F");   //Initialize the JButton
      floor3 = new JButton("3F");   //Initialize the JButton
      ImageIcon searchIcon = new ImageIcon("icon_images/search_icon.png");    //Initialize the image to add to the JButton
      ImageIcon profileIcon = new ImageIcon("icon_images/profile_icon.png");  //Initialize the image to add to the JButton
      search = new JButton(searchIcon);    //Add button to the JFrame
      profile = new JButton(profileIcon);  //Add button to the JFrame
      floor1.setBounds(0, 0, 75, 75);       //Set the bounds to the floor 1 button
      floor2.setBounds(75, 0, 75, 75);     //Set the bounds to the floor 2 button
      floor3.setBounds(150, 0, 75, 75);     //Set the bounds to the floor 3 button
      search.setBounds(225, 0, 75, 75);     //Set the bounds to the search button
      floor1.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               enableButtons();
               floor1.setEnabled(false);
               fenwickLibrary.setFloorNumber(1);
               fenwickLibrary.setTableSelected(false);
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
               displayCourseOptions(false);
               fenwickLibrary.repaint();
            } });  
      search.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               enableButtons();
               search.setEnabled(false);
               fenwickLibrary.setTableSelected(false);
               displayCourseOptions(false);
            } });  
      sidePanel.add(floor1);      //Add button to the JFrame
      sidePanel.add(floor2);      //Add button to the JFrame
      sidePanel.add(floor3);      //Add button to the JFrame
      sidePanel.add(search);      //Add button to the JFrame
   }
   
   //Enable the buttons for the floors
   public static void enableButtons() {
      floor1.setEnabled(true);
      floor2.setEnabled(true);
      floor3.setEnabled(true);
      search.setEnabled(true);
      profile.setEnabled(true);
   }
   
   //Whether or not we want to display certain aspects of our side panel
   public static void displayCourseOptions(boolean display) {
      courseSubjects.setVisible(display);
      courseNumbers.setVisible(display);
      messageLabel.setVisible(display);
      message.setVisible(display);
      addTable.setVisible(display);
   }
   
   public static void initializeSidePanel() {
      tableStudentName = new JLabel("");
      tableStudentName.setBounds(25, 90, 300, 25);
      sidePanel.add(tableStudentName);
      tableClass = new JLabel("");
      tableClass.setBounds(25, 115, 300, 25);
      sidePanel.add(tableClass);
      tableMessage = new JLabel("");
      tableMessage.setBounds(25, 140, 200, 80);
      sidePanel.add(tableMessage);
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
      courseSubjects.setBounds(50, 235, 75, 25);
      sidePanel.add(courseSubjects);
      courseNumbers = new JComboBox(initializeSubjectNumbers(classList.get(0).getSubjectCode()));
      courseNumbers.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            } });
      courseNumbers.setBounds(175, 235, 75, 25);
      sidePanel.add(courseNumbers);
      messageLabel = new JLabel("User Description:");
   
      messageLabel.setBounds(50, 360, 200, 25);
      sidePanel.add(messageLabel);
      message = new JTextField(20);
      message.setBounds(50, 385, 200, 25);
      message.setDocument(new JTextFieldLimit(140));
      sidePanel.add(message);
      addTable = new JButton("Finish");
      addTable.setBounds(75, 415, 150, 25);
      addTable.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               fenwickLibrary.getSelectedTable().setOccupied();
               fenwickLibrary.getSelectedTable().setStudentName("Mr. Jones");
               String course = (String)courseSubjects.getSelectedItem();
               String number = (String)courseNumbers.getSelectedItem();
               fenwickLibrary.getSelectedTable().setCourse(findClass(course, number));
               courseSubjects.setSelectedIndex(0);
               fenwickLibrary.getSelectedTable().setMessage(message.getText().toString());
               message.setText("");
               displayCourseOptions(false);
               fenwickLibrary.setTableSelected(false);
               fenwickLibrary.repaint();
            } });  
      sidePanel.add(addTable);
      displayCourseOptions(false);
   }
   
   public static Class findClass(String course, String number) {
      for(int i = 0; i < classList.size(); i++) {
         if(classList.get(i).getSubjectCode().equals(course) && classList.get(i).getNumber() == Integer.parseInt(number)) {
            return classList.get(i);
         }
      }
      return new Class();
   }
   
   public static void displayStudentName(String name) {
      tableStudentName.setText(name);
   }
   
   public static void displayMessage(String message) {
      tableMessage.setText("<html>" + message + "</html>");
   }
   
   public static void displayTableCourse(Class course) {
      tableClass.setText(course.getSubjectCode() + " " + course.getNumber());
   }
   
   public static void resetTableCourse() {
      tableClass.setText("");
   }
   
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