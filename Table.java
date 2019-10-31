/*
   Table.java
   CS 321 - Section 001: Team 7
   John DeCarlo, Huiying Jin, John Radecki, Joshua Yuen
   ----------------------------------------------------
   Description: Table class that holds information about tables
   where students can sit and study
*/

import java.util.*;
import java.io.*;

public class Table {

   private int floor;   //Floor number
   private int row;     //Starting row location
   private int col;     //Starting column location
   private int rowSize;    //Size of the table (x-axis based)
   private int colSize;    //Size of the table (y-axis based)
   private boolean occupied;  //Table is occupied by a student or not
   private String shape;      //What shape is the table (BLOCK, CIRCLE, etc.)
   private String studentName;   //Name of student sitting at the table
   private Class course;         //Course that the student is studying for
   private String message;       //Message that the student can send out describing their studies
   private int id;               //ID to identify our table by (Will be index in the array)
   
   //Constructor for Table class w/ no parameters
   public Table() {
      this.floor = 1;
      this.row = 0;
      this.col = 0;
      this.rowSize = 0;
      this.colSize = 0;
      this.occupied = true;
      this.shape = "BLOCK";
      this.studentName = "";
      this.course = new Class();
      this.message = "";
      this.id = -1;
   }
   
   //Constructor for Table class w/ parameters
   public Table(int floor, int row, int col, int rowSize, int colSize, String shape, int id) {
      this.floor = floor;
      this.row = row;
      this.col = col;
      this.rowSize = rowSize;
      this.colSize = colSize;
      this.occupied = false;
      this.shape = shape;
      this.studentName = "";
      this.course = new Class();
      this.message = "";
      this.id = id;
   }
   
   //Get the floor number
   public int getFloor() {
      return this.floor;
   }
   
   //Set the floor number
   public void setFloor(int floor) {
      this.floor = floor;
   }
   
   //Get the starting row of the table
   public int getRow() {
      return this.row;
   }
   
   //Set the starting row of the table
   public void setRow(int row) {
      this.row = row;
   }
   
   //Get the starting column of the table
   public int getCol() {
      return this.col;
   }
   
   //Set the starting column of the table
   public void setCol(int col) {
      this.col = col;
   }
   
   //Get the size of the table for the x-axis
   public int getRowSize() {
      return this.rowSize;
   }
   
   //Set the size of the table for the x-axis
   public void setRowSize(int rowSize) {
      this.rowSize = rowSize;
   }
   
    //Get the size of the table for the y-axis
   public int getColSize() {
      return this.colSize;
   }
   
    //Set the size of the table for the y-axis
   public void setColSize(int colSize) {
      this.colSize = colSize;
   }
   
   //Get the occupied status of the table
   public boolean getOccupied() {
      return this.occupied;
   }
   
   //Set the occupied status of the table
   public void setOccupied() {
      if(this.occupied == true)
         this.occupied = false;
      else
         this.occupied = true;
   }
   
   //Get the shape of the table
   public String getShape() {
      return this.shape;
   }
   
   //Set the shape of the table
   public void setShape(String shape) {
      this.shape = shape;
   }
   
   //Get the name of the student sitting at the table
   public String getStudentName() {
      return this.studentName;
   }
   
   //Set the name of the student sitting at the table
   public void setStudentName(String name) {
      this.studentName = name;
   }
   
   //Get the course the student is studying for
   public Class getCourse() {
      return this.course;
   }
   
   //Set the course the student is studying for
   public void setCourse(Class course) {
      this.course = course;
   }
   
   //Get the message describing the student studies
   public String getMessage() {
      return this.message;
   }
   
   //Set the message describing the student studies
   public void setMessage(String message) {
      this.message = message;
   }
   
   //Get the id of the table
   public int getID() {
      return this.id;
   }
   
   //Set the id of the table
   public void setID(int id) {
      this.id = id;
   }
   
}