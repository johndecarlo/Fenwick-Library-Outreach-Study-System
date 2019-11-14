/*
   Student.java
   CS 321 - Section 001: Team 7
   John DeCarlo, Huiying Jin, John Radecki, Joshua Yuen
   ----------------------------------------------------
   Description: This class contains the constructor and the methods for the student class.
*/

import java.util.*;


public class Student {
	
	//Student Fields
   private String firstName;
   private String lastName;
   private String masonEmail;
   private String password;
   private String major;
   private ArrayList<String> friends;
   private boolean occupyTable;
	
	//Student Constructor, create a default one.
   public Student() {
      this.firstName = "First";
      this.lastName = "Last";
      this.masonEmail = "flast1";
      this.password = "password";
      this.major = "Computer Science";
      this.friends = new ArrayList<String>();
   }
   
   public Student(String first, String last, String email, String password, String major) {
      this.firstName = first;
      this.lastName = last;
      this.masonEmail = email;
      this.password = password;
      this.major = major;
      this.friends = new ArrayList<String>();
   }
	
   //Getter for FirsttName
   public String getFirstName() {
      return firstName;
   }
   
	//Setter for FirstName
   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }
	
    //Getter for LastName
   public String getLastName() {
      return lastName;
   }
   
	//Setter for LastName
   public void setLastName(String lastName) {
      this.lastName = lastName;
   }
   
   public String getMasonEmail() {
      return this.masonEmail;
   }
   
   public void setMasonEmail(String email) {
      this.masonEmail = email;
   }
	
   //setter for student password
   public void setPassword(String password) {
      this.password = password;
   }
   
   //getter for student password
   public String getPassword() {
      return password;
   }
   
   public ArrayList<String> getFriends() {
      return this.friends;
   }
   
   public void setFriends(ArrayList<String> friends) {
      this.friends = friends;
   }
   
	//Add friend into friend list, and increase number of friend by one
   public void addFriend(String friend) {
      friends.add(friend);
   }
	
	//Reture the major of the student
   public String getMajor() {
      return major;
   }
   
   	//Set the major of the student
   public void setMajor(String major) {
      this.major = major;
   }
   
   //Get whether or not the student is currently occupying a table
   public boolean getOccupyTable() {
      return this.occupyTable;
   }
   
   //Set whether the student is occupying a table
   public void setOccupyTable() {
      if(!occupyTable)
         this.occupyTable = true;
      else
         this.occupyTable = false;
   }
	
	//Remove friend from friends list, and decrease number of friend by one
   public void removeFriend(String username) {
      for(int i = 0; i < friends.size(); i++) {
         if(friends.get(i).equals(username)) {
            friends.remove(i);
         }
      }
   }
}
