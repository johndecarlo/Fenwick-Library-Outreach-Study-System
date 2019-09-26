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
   public static JButton floor1;
   public static JButton floor2;
   public static JButton floor3;
   public static JButton search;
   public static JButton profile;
   
   public static void main(String[]args) throws IOException {
      JFrame display = new JFrame("FLOSS");     //Create our JFrame
      fenwickLibrary = new StudySystemMap();    //Initialize the shot fenwick library map
      displayButtons();                         //Add display option buttons
      display.setSize(1000, 850);			      //Size of display window
      display.setLocation(300, 0);				   //Location of display window on the screen
      display.setResizable(false);              //Cannot change size of the screen
      display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //Exit when close out 
      display.setContentPane(fenwickLibrary);                    //Set contentPanel to our board
      display.setVisible(true);                                //Make the screen visible
   }
   
   public static void displayButtons()
   {
      fenwickLibrary.setLayout(null);
      JButton floor1 = new JButton("1st Floor");
      JButton floor2 = new JButton("2nd Floor");
      JButton floor3 = new JButton("3rd Floor");
      JButton search = new JButton("Search");
      JButton profile = new JButton("My Profile");
      floor1.setBounds(0, 700, 140, 100);
      floor2.setBounds(140, 700, 140, 100);
      floor3.setBounds(280, 700, 140, 100);
      search.setBounds(420, 700, 140, 100);
      profile.setBounds(560, 700, 140, 100);
      fenwickLibrary.add(floor1);
      fenwickLibrary.add(floor2);
      fenwickLibrary.add(floor3);
      fenwickLibrary.add(search);
      fenwickLibrary.add(profile); 
   }
}