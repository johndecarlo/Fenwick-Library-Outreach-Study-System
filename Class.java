/*
   Class.java
   CS 321 - Section 001: Team 7
   John DeCarlo, Huiying Jin, John Radecki, Joshua Yuen
   ----------------------------------------------------
   Description: Class class that holds information about courses students can take
*/

public class Class {

   private String subject;       //Subject name
   private String subjectCode;   //Subject code
   private int number;           //Course number
   private String title;         //Course title
   
   //Base constructor for Class
   public Class() {
      this.subject = "";
      this.subjectCode = "";
      this.number = 0;
      this.title = "";
   }
   
   //Class Constructor with parameters
   public Class(String subject, String subjectCode, int number, String title) {
      this.subject = subject;
      this.subjectCode = subjectCode;
      this.number = number;
      this.title = title;
   }
   
   //Get the course subject
   public String getSubject() {
      return this.subject;
   }
   
   //Set the course subject
   public void setSubject(String subject) {
      this.subject = subject;
   }
   
   //Get the subject code
   public String getSubjectCode() {
      return this.subjectCode;
   }
   
   //Set the subject code
   public void setSubjectCode(String subjectCode) {
      this.subjectCode = subjectCode;
   }
   
   //Get the course number
   public int getNumber() {
      return this.number;
   }
   
   //Set the course number
   public void setNumber(int number) {
      this.number = number;
   }
   
   //Get the course title
   public String getTitle() {
      return title;
   }
   
   //Set the course title
   public void setTitle(String title) {
      this.title = title;
   }
}