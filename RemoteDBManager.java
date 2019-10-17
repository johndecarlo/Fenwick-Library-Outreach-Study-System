import java.util.list;

//Basic methods needed for accessing and changing information per student in the remote database
public interface RemoteDBManager {
    boolean exists( int id );
    boolean getID( int studentID ); 
        //get the unique id which the database uses to identify students, different from student id for security

    boolean addUser( int id, String name /*add more values as student info decided on*/ );
    boolean editName( int id, String value );
    boolean editStudentID( int id, int studentID );

    //manage studying status
    boolean toggleStudying( int id, boolean val );
    boolean isStudying( int id );

    //manage study mates
    List<Integer> fetchStudyMates( int id );
    boolean addStudyMates( int id );

    //manage study location, int would be some combination of floor and table number
    //system would check if table is available, and would return false to editing if not
    int fetchStudyLocation( int id );
    boolean editStudyLocation( int id, int location );
}