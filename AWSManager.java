import java.util.List;

public class AWSManager implements RemoteDBManager {

	@Override
	public boolean exists(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getID(int studentID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addUser(int id, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean editName(int id, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean editStudentID(int id, int studentID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean toggleStudying(int id, boolean val) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStudying(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Integer> fetchStudyMates(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addStudyMates(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int fetchStudyLocation(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean editStudyLocation(int id, int location) {
		// TODO Auto-generated method stub
		return false;
	}
    
}