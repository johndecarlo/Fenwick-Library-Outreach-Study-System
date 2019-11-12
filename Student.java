import java.util.*;


public class Student {
	
	//Student Fields
   private String firstName;
   private String lastName;
   private String masonEmail;
   private String password;
   private String major;
   private int gNumber;
   private ArrayList<Student> friends;
   private boolean occupyTable;
	
	//Student Constructor, create a default one.
   public Student() {
      this.firstName = "Bob";
      this.lastName = "Jones";
      this.masonEmail = "bjones1";
      this.password = "password";
      this.major = "Computer Science";
      this.gNumber = 01234567;
      this.friends = new ArrayList<Student>();
   }
   
   public Student(String first, String last, String email, String password, String major) {
      this.firstName = first;
      this.lastName = last;
      this.masonEmail = email;
      this.password = password;
      this.major = major;
      this.gNumber = 0;
      this.friends = new ArrayList<Student>();
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
   
   public void getMasonEmail(String email) {
      this.masonEmail = email;
   }
	
   //Getter for Gnumber
   public int getGNumber() {
      return gNumber;
   }
   
	//Setter for Gnumber
   public void setGNumber(int gNumber) {
      this.gNumber = gNumber;
   }
	
   //setter for student password
   public void setPassword(String password) {
      this.password = password;
   }
   
   //getter for student password
   public String getPassword() {
      return password;
   }
   
   public ArrayList<Student> getFriends() {
      return this.friends;
   }
   
   public void setFriends(ArrayList<Student> friends) {
      this.friends = friends;
   }
   
	//Add friend into friend list, and increase number of friend by one
   public void addFriend(Student friend) {
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
   public void removeFriends(Student friend) {
      if(friends.contains(friend)) {
         for(int i = 0; i < friends.size(); i++) {
            if(friends.get(i).equals(friend)) {
               friends.remove(i);
            }
         }
      }
   }
}
