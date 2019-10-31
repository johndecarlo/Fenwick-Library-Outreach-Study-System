import java.util.*;

public class Group {
	
	//Limit of arraylist?
	ArrayList<Student> groupList;
	
	public Group() {
		
	}
	
	public void creatGroup() {
		groupList = new ArrayList<Student>();
	}
	
	public void deleteGroup() {
		groupList = null;
	}
	
	public void clearGroup() {
		groupList.clear();
	}
	
	public void addMember(Student member) {
		if(groupList.equals(null)) {
			System.out.println("Need to create group first");
		}else {
			groupList.add(member);
		}
	}
	
	
	public boolean isMemberInGroup(Student member) {
		boolean inGroup = false;
		inGroup = groupList.contains(member);
		return inGroup;
	}
	
	public void removeMember(Student member) {
		for(int i = 0; i < groupList.size(); i++ ) {
			if(groupList.get(i).equals(member)) {
				groupList.remove(i);
			}
		}
	}
	
	
	
}
