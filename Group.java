import java.util.*;

public class Group {
	
	//Group fields
	ArrayList<Student> groupList;
	private int tableSize = 0;
	private int groupSize = 0;
	private int groupID = 0;
	
	public Group() {
		this.tableSize = 0;
		this.groupSize = 0;
		this.groupID = 0;
		this.groupList = new ArrayList();
	}
	
	public Group(int tSize, int gSize, int gID) {
		this.groupSize = gSize;
		this.groupID = gID;
		this.tableSize = tSize;
		this.groupList = new ArrayList();
	}
	
	
	//Class constructor
	public void creatGroup() {
		groupList = new ArrayList<Student>();
	}
	
	public int getGroupID() {
		return groupID;
	}
	
	public void setTableSize(int size) {
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
	
	public void getMembers() {
		for(int i = 0; i < groupList.size(); i++) {
			System.out.println(groupList.indexOf(i));
		}
	}
}
