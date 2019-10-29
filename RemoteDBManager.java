import java.util.List;

//Basic methods needed for accessing and changing information per student in the remote database
public interface RemoteDBManager {
    boolean exists( String id );

    boolean addUser( String userID, String name, String password );
    boolean editName( String id, String value );
    boolean editPassword( String id, String newPassword );

    //manage studying status
    boolean toggleStudying( String id, boolean val, int tableID, String message );
    boolean isStudying( String id );

    //manage study mates
    List<String> fetchStudyMates( String id );
    boolean addStudyMates( String id, String... studyMates );

    //manage study location, int would be some combination of floor and table number
    //system would check if table is available, and would return false to editing if not
    int fetchStudyLocation( String id );
    boolean editStudyLocation( String id, int location );
}