import java.util.*;

public class Group {
	
	//Group fields
	ArrayList<Student> groupList;
	private int tableSize = 0;
	private int groupSize = 0;
	
	public Group() {
		
	}
	
	
	//Class constructor with limit of 10
	public void creatGroup() {
		groupList = new ArrayList<Student>();
	}
	
	public void tableSize(int size) {
		this.tableSize = size;
	}
	
	public int getTableSize() {
		return tableSize;
	}
	
	//Delete group
	public void deleteGroup() {
		groupList = null;
	}
	
	//Remove all members from group
	public void clearGroup() {
		groupList.clear();
	}
	
	//Add member into a group. If group is null, ask user to create group first. 
	public void addMember(Student member) {
		if(groupList.equals(null)) {
			System.out.println("Need to create group first");
		}else {
			if(groupSize < tableSize) {
				groupList.add(member);
				groupSize++;
			}else {
				System.out.println("Table cannot occupy anymore members.");
			}
			
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
