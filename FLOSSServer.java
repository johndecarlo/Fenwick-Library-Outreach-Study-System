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

public class FLOSSServer extends JFrame {
   public static void main(String[]args) throws IOException {
      ServerSocket server = new ServerSocket(4999);
      Socket socket = server.accept();
      System.out.println("Connected");

   }
}