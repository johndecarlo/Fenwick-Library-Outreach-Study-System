import java.util.*;


public class Student {
	
	private String FirstName, LastName, password;
	private int G_number, numberOfFriends;
	private ArrayList<friend> friends;
	
	
	Scanner input = new Scanner(System.in);
	
	
	
	public Student() {
		System.out.println("Thank you for creating an account.");
		System.out.println("Please enter your your first name: ");
		System.out.println("Please enter your last name: ");
		System.out.println("Please enter your password");
		this.FirstName = input.next();
		this.LastName = input.next();
		this.password = input.next();
	}
	
	
	public void setFirstName(String firstName) {
		this.FirstName = firstName;
	}
	
	public String getFirstName() {
		return FirstName;
	}
	
	public void setLastName(String lastName) {
		this.LastName = lastName;
	}
	
	public String getLastName() {
		return LastName;
	}
	
	
	public void setG_number(int G_number) {
		this.G_number = G_number;
	}
	
	public int get_Gnumber() {
		return G_number;
	}
	
	public void addFriends(friend friend) {
		friends.add(friend);
		numberOfFriends++;
	}
	
	public void removeFriends(friend friend) {
		if(!friends.contains(friend)) {
			System.out.println("The person is not in your friendList");
		}else {
			for(int i = 0; i < friends.size(); i++) {
				
			}
		}
	}
	
	public int getNumberOfFriends() {
		return numberOfFriends;
	}
	
	public String toString() {
		String output = "First Name: " + FirstName + "\nLast Name: " + LastName + "\nG_number: " + G_number;
		return output;
	}
	
}
