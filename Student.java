import java.util.*;


public class Student {
	
	//Student Fields
	private String FirstName, LastName, password, major;
	private int G_number, numberOfFriends = 0;
	private ArrayList<Student> friends;
	private boolean isLeader = false;
	
	
	//Scanner to read user's input
	Scanner input = new Scanner(System.in);
	
	
	//Student Constructor, create a default one.
	public Student() {
	}
	
	//Setter for FirstName
	public void setFirstName(String firstName) {
		this.FirstName = firstName;
	}
	
	//Getter for FirsttName
	public String getFirstName() {
		return FirstName;
	}
	
	//Setter for LastName
	public void setLastName(String lastName) {
		this.LastName = lastName;
	}
	
	//Getter for LastName
	public String getLastName() {
		return LastName;
	}
	
	//Setter for Gnumber
	public void setG_number(int G_number) {
		this.G_number = G_number;
	}
	
	//Getter for Gnumber
	public int get_Gnumber() {
		return G_number;
	}
	
	//Add friend into friend list, and increase number of friend by one
	public void addFriends(Student friend) {
		friends.add(friend);
		numberOfFriends++;
	}
	
	//Set the major of the student
	public void setMajor(String major) {
		this.major = major;
	}
	
	//Reture the major of the student
	public String getMajor() {
		return major;
	}
	
	//Remove friend from friends list, and decrease number of friend by one
	public void removeFriends(Student friend) {
		if(!friends.contains(friend)) {
			System.out.println("The person is not in your friendList");
		}else {
			for(int i = 0; i < friends.size(); i++) {
				if(friends.get(i).equals(friend)) {
					friends.remove(i);
					numberOfFriends--;
				}
			}
		}
	}
	
	//Get number of friends the person have.
	public int getNumberOfFriends() {
		return numberOfFriends;
	}
	
	public void setLeaderTrue() {
		isLeader = true;
	}
	
	public void setLeaderFalse() {
		isLeader = false;
	}
	
	public boolean isLeader() {
		return isLeader;
	}
	
	//String output for class.
	public String toString() {
		String output = "First Name: " + FirstName + "\nLast Name: " + LastName + "\nG_number: " + G_number;
		return output;
	}
	
}
