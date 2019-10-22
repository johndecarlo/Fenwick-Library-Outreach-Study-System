import java.util.*;
import java.io.*;

public class Table {

   private int floor;
   private int row;
   private int col;
   private int rowSize;
   private int colSize;
   private boolean occupied;
   private String shape;
   private String studentName;
   private Class course;
   private String message;
   
   //Constructor for Table class w/ no parameters
   public Table() {
      this.floor = 1;
      this.row = 0;
      this.col = 0;
      this.rowSize = 30;
      this.colSize = 30;
      this.occupied = true;
      this.shape = "BLOCK";
      this.studentName = "";
      this.course = new Class();
      this.message = "";
   }
   
   //Constructor for Table class w/ parameters
   public Table(int floor, int row, int col, int rowSize, int colSize, String shape){
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
   }
   
   public int getFloor() {
      return this.floor;
   }
   
   public void setFloor(int floor) {
      this.floor = floor;
   }
   
   public int getRow() {
      return this.row;
   }
   
   public void setRow(int row) {
      this.row = row;
   }
   
   public int getCol() {
      return this.col;
   }
   
   public void setCol(int col) {
      this.col = col;
   }
   
   public int getRowSize() {
      return this.rowSize;
   }
   
   public void setRowSize(int rowSize) {
      this.rowSize = rowSize;
   }
   
   public int getColSize() {
      return this.colSize;
   }
   
   public void setColSize(int colSize) {
      this.colSize = colSize;
   }
   
   public boolean getOccupied() {
      return this.occupied;
   }
   
   public void setOccupied() {
      if(this.occupied == true)
         this.occupied = false;
      else
         this.occupied = true;
   }
   
   public String getShape() {
      return this.shape;
   }
   
   public void setShape(String shape) {
      this.shape = shape;
   }
   
   public String getStudentName() {
      return this.studentName;
   }
   
   public void setStudentName(String name) {
      this.studentName = name;
   }
   
   public Class getCourse() {
      return this.course;
   }
   
   public void setCourse(Class course) {
      this.course = course;
   }
   
   public String getMessage() {
      return this.message;
   }
   
   public void setMessage(String message) {
      this.message = message;
   }
   
}