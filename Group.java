import java.util.*;

public class Group {
	
	//Group fields
	ArrayList<Student> groupList;
	
	public Group() {
		
	}
	
	//Class constructor with limit of 10
	public void creatGroup() {
		groupList = new ArrayList<Student>(10);
	}
	
	//Delete group
	public void deleteGroup() {
		groupList = null;
	}
	
	//Remove all members from group
	public void clearGroup() {
		groupList.clear();
	}
	
	//Add member into a group. If group is null, 
	public void addMember(Student member) {
		if(groupList.equals(null)) {
			System.out.println("Need to create group first");
		}else {
			groupList.add(member);
		}
	}
	
	//Check if person is in group return true or false if person is in group or not.
	public boolean isMemberInGroup(Student member) {
		boolean inGroup = false;
		inGroup = groupList.contains(member);
		return inGroup;
	}
	
	//Remove the member from group. 
	public void removeMember(Student member) {
		for(int i = 0; i < groupList.size(); i++ ) {
			if(groupList.get(i).equals(member)) {
				groupList.remove(i);
			}
		}
	}	
}
