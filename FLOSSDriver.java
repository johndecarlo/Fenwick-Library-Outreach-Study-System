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
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FLOSSDriver {

   public static StudySystemMap fenwickLibrary;    //JPanel that holds the study maps
   public static JPanel buttons;                   //JPanel that holds the JButtons
   public static JPanel sidePanel;                 //JPanel that is the side information panel
   public static ArrayList<Class> classList;       //List of most of the classes offered at GMU
   
   public static JPanel tableInformationPanel;     //Information about a table that someone's mouse is over
   public static JPanel searchPanel;               //Implemented when searching for someone
   public static JPanel profileInformationPanel;   //Profile information displayed 
   
   public static JButton floor1;       //When pressed will display first floor information
   public static JButton floor2;       //When pressed will display second floor information     
   public static JButton floor3;       //When pressed will display third floor information
   public static JButton search;       //When pressed will display search table information
   public static JButton profile;      //When pressed will display the profile information
   
   //Floor Information text labels
   public static JLabel tableStudentName;
   public static JLabel tableClass;
   public static JLabel messageLabel;
   public static JTextField message;
   
   //Search tool text labels
   public static JLabel searchTitle;
   public static JTextField searchBar;
   
   //Profile JPanel text labels
   public static JLabel profileName;      //Profile display name
   public static JLabel profileYear;      //Profile display year
   public static JLabel profileMajor;     //Profile display major
   public static JLabel profileMinor;     //Profile display minor
   public static JLabel profileGnumber;   //Profile display g-number
   
   public static void main(String[]args) throws IOException {
      JFrame display = new JFrame("FLOSS");     //Create our JFrame
      display.setSize(1200, 830);			      //Size of display window
      display.setLocation(150, 25);				   //Location of display window on the screen
      display.setResizable(false);              //Cannot change size of the screen
      display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //Exit when close out 
      
      JPanel container = new JPanel();          //Establish the JPanel that will hold all our JPanel's
      container.setLayout(null);                //Set the layout to null so we can set the bounds
   
      fenwickLibrary = new StudySystemMap();       //Initialize the interactive Fenwick library map
      fenwickLibrary.setBounds(150, 0, 800, 800);    //Set the bounds for the interactive map
      buttons = new JPanel();                      //Initialize the buttons listing all the options
      buttons.setBounds(0, 0, 150, 800);         //Set the bounds for the interactive map
      displayButtons();                            //Add all the buttons and options
      sidePanel = new JPanel();                    //Initialize the side panel that shows all our options
      sidePanel.setBounds(900, 0, 250, 800);       //Set the bounds for the interactive map
      initializeSidePanels();                      //Initialize the side panel
      initializeClasses();
   
      container.add(fenwickLibrary);   //Add JPanel map to the main display screen 
      container.add(buttons);          //Add JPanel buttons to the main display screen 
      container.add(sidePanel);        //Add JPanel sidePanel to the main display screen 
      
      display.setContentPane(container);  //Set the contents of the JFrame display to the container holding all the display info
      display.setVisible(true);           //Set the JFrame visible
   }
   
   public static void displayButtons()
   {
      buttons.setLayout(null);
      floor1 = new JButton("1st Floor");   //Initialize the JButton
      floor2 = new JButton("2nd Floor");   //Initialize the JButton
      floor3 = new JButton("3rd Floor");   //Initialize the JButton
      ImageIcon searchIcon = new ImageIcon("images/search_icon.png");    //Initialize the image to add to the JButton
      ImageIcon profileIcon = new ImageIcon("images/profile_icon.png");  //Initialize the image to add to the JButton
      search = new JButton(searchIcon);    //Add button to the JFrame
      profile = new JButton(profileIcon);  //Add button to the JFrame
      floor1.setBounds(0, 0, 150, 160);       //Set the bounds to the floor 1 button
      floor2.setBounds(0, 160, 150, 160);     //Set the bounds to the floor 2 button
      floor3.setBounds(0, 320, 150, 160);     //Set the bounds to the floor 3 button
      search.setBounds(0, 480, 150, 160);     //Set the bounds to the search button
      profile.setBounds(0, 640, 150, 160);    //Set the bounds to the profile button
      floor1.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               enableButtons();
               floor1.setEnabled(false);
               loadSidePanel(1);
               fenwickLibrary.setFloorNumber(1);
               fenwickLibrary.repaint();
            } });   
      floor2.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               enableButtons();
               floor2.setEnabled(false);
               loadSidePanel(2);
               fenwickLibrary.setFloorNumber(2);
               fenwickLibrary.repaint();
            } });    
      floor3.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               enableButtons();
               floor3.setEnabled(false);
               loadSidePanel(3);
               fenwickLibrary.setFloorNumber(3);
               fenwickLibrary.repaint();
            } });  
      search.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               enableButtons();
               search.setEnabled(false);
               loadSidePanel(4);
            } });  
      profile.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               enableButtons();
               profile.setEnabled(false);
               loadSidePanel(5);
            } });  
      buttons.add(floor1);      //Add button to the JFrame
      buttons.add(floor2);      //Add button to the JFrame
      buttons.add(floor3);      //Add button to the JFrame
      buttons.add(search);      //Add button to the JFrame
      buttons.add(profile);     //Add button to the JFrame
   }
   
   public static void loadSidePanel(int option) {
      if(option == 1 || option == 2 || option == 3) {
         sidePanel = tableInformationPanel;
      }
      else if(option == 4) {
         sidePanel = searchPanel;
      }
      else {
         sidePanel = profileInformationPanel;
      }
   }
   
   public static void enableButtons() {
      floor1.setEnabled(true);
      floor2.setEnabled(true);
      floor3.setEnabled(true);
      search.setEnabled(true);
      profile.setEnabled(true);
   }
   
   public static void initializeSidePanels() {
      initializeTablePanel();
      initializeSearchPanel();
      initializeProfilePanel();
      loadSidePanel(1);                            //Load the side panel for information display
   }
   
   public static void initializeTablePanel() {
      tableInformationPanel = new JPanel();
      tableInformationPanel.setBounds(950, 0, 250, 800);
      tableInformationPanel.setLayout(null);
      tableStudentName = new JLabel("Name: John Paul Jones");
      tableStudentName.setBounds(25, 0, 300, 100);
      tableInformationPanel.add(tableStudentName);
      tableClass = new JLabel("Class:");
      tableClass.setBounds(25, 50, 300, 100);
      tableInformationPanel.add(tableClass);
      messageLabel = new JLabel("Other information");
      messageLabel.setBounds(25, 100, 100, 100);
      tableInformationPanel.add(messageLabel);
      message = new JTextField(20);
      message.setBounds(25, 175, 200, 25);
      tableInformationPanel.add(message);
   }
   
   public static void initializeSearchPanel() {
      searchPanel = new JPanel();
      searchPanel.setBounds(950, 0, 250, 800);
      searchPanel.setLayout(null);
      searchTitle = new JLabel("Search for Student");
      searchTitle.setBounds(25, 100, 300, 100);
      searchPanel.add(searchTitle);     
   }
   
   public static void initializeProfilePanel() {
      profileInformationPanel = new JPanel();
      profileInformationPanel.setBounds(950, 0, 250, 800);
      profileInformationPanel.setLayout(null);
      profileName = new JLabel("John Paul Jones");
      profileName.setBounds(25, 0, 300, 100);
      profileInformationPanel.add(profileName);
      profileYear = new JLabel("Freshman");
      profileYear.setBounds(25, 50, 300, 100);
      profileInformationPanel.add(profileYear);
      profileMajor = new JLabel("Mathematics & Statistics");
      profileMajor.setBounds(25, 100, 300, 120);
      profileInformationPanel.add(profileMajor);
      profileMinor = new JLabel("Interpretive Dance");
      profileMinor.setBounds(25, 150, 300, 120);
      profileInformationPanel.add(profileMinor);
      profileGnumber = new JLabel("G########");
      profileGnumber.setBounds(25, 200, 300, 120);
      profileInformationPanel.add(profileGnumber);
   }
   
   public static void initializeClasses() throws IOException {
      classList = new ArrayList<Class>();
      File file = new File("classes.txt");
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