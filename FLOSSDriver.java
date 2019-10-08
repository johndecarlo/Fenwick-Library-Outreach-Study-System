/*
   FLOSSDriver.java
   CS 321 - Section 001: Team 7
   John DeCarlo, Huiying Jin, John Radecki, Joshua Yuen
   ----------------------------------------------------
   Description: This is the executable driver for our software
*/

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FLOSSDriver {

   public static StudySystemMap fenwickLibrary;
   public static JPanel buttons;
   public static JPanel sidePanel;
   
   
   public static JPanel tableInformationPanel;
   public static JPanel searchPanel;
   public static JPanel profileInformationPanel;
   
   public static JButton floor1;
   public static JButton floor2;
   public static JButton floor3;
   public static JButton search;
   public static JButton profile;
   
   public static void main(String[]args) throws IOException {
      JFrame display = new JFrame("FLOSS");     //Create our JFrame
      display.setSize(1000, 850);			      //Size of display window
      display.setLocation(300, 0);				   //Location of display window on the screen
      display.setResizable(false);              //Cannot change size of the screen
      display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //Exit when close out 
      
      JPanel container = new JPanel();          //Establish the JPanel that will hold all our JPanel's
      container.setLayout(null);                //Set the layout to null so we can set the bounds
   
      fenwickLibrary = new StudySystemMap();    //Initialize the interactive Fenwick library map
      fenwickLibrary.setBounds(0, 0, 700, 700);  //Set the bounds for the interactive map
      buttons = new JPanel();                      //Initialize the buttons listing all the options
      buttons.setBounds(0, 700, 700, 120);         //Set the bounds for the interactive map
      displayButtons();                            //Add all the buttons and options
      sidePanel = new JPanel();                    //Initialize the side panel that shows all our options
      sidePanel.setBounds(700, 0, 140, 120);       //Set the bounds for the interactive map
      initializeSizePanels();
      loadSidePanel(1);
   
      container.add(fenwickLibrary);
      container.add(buttons);
      container.add(sidePanel);
      
      display.setContentPane(container);
      display.setVisible(true);
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
      floor1.setBounds(0, 0, 140, 120);       //Set the bounds to the floor 1 button
      floor2.setBounds(140, 0, 140, 120);     //Set the bounds to the floor 2 button
      floor3.setBounds(280, 0, 140, 120);     //Set the bounds to the floor 3 button
      search.setBounds(420, 0, 140, 120);     //Set the bounds to the search button
      profile.setBounds(560, 0, 140, 120);    //Set the bounds to the profile button
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
   
   public static void initializeSizePanels() {
      initializeInfoPanel();
      initializeSearchPanel();
      initializeProfilePanel();
   }
   
   public static void initializeInfoPanel() {
      tableInformationPanel = new JPanel();
   }
   
   public static void initializeSearchPanel() {
      searchPanel = new JPanel();
   }
   
   public static void initializeProfilePanel() {
      profileInformationPanel = new JPanel();
   }
   
   
}