
public class driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Student person1 = new Student();
		person1.setFirstName("Bob");
		System.out.println(person1.getFirstName());
		person1.setLastName("Tom");
		System.out.println(person1.getLastName());
		person1.setGNumber(10000000);
		System.out.println(person1.getGNumber());
		person1.setMajor("Biology");
		System.out.println(person1.getMajor());
		System.out.println(person1.getOccupyTable());
		person1.getMasonEmail("hjin4@masonlive.gmu.edu");
		System.out.println(person1.getMasonEmail());
		
		Student person2 = new Student();
		
		Group one = new Group();
		one.creatGroup();
		one.setTableSize(6);
		one.addMember(person1);
		one.addMember(person2);
		System.out.println(one.groupList.size());
	}

}
