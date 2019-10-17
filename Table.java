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
   
   //Constructor for Table class w/ no parameters
   public Table() {
      this.floor = 1;
      this.row = 55;
      this.col = 729;
      this.rowSize = 7;
      this.colSize = 7;
      this.occupied = false;
      this.shape = "SEAT";
   }
   
   //Constructor for Table class w/ parameters
   public Table(int floor, int row, int col, int rowSize, int colSize, String shape){
      this.floor = floor;
      this.row = row;
      this.col = col;
      this.rowSize = rowSize;
      this.colSize = colSize;
      this.occupied = true;
      this.shape = shape;
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
   
   public void setOccupied(boolean occupied) {
      this.occupied = occupied;
   }
   
   public String getShape() {
      return this.shape;
   }
   
   public void setShape(String shape) {
      this.shape = shape;
   }
}