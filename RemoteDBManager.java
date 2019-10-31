import java.util.List;
import java.util.Map;

//Basic methods needed for accessing and changing information per student in the remote database
public interface RemoteDBManager {
    boolean exists( String id );

    boolean addUser( String userID, String name, String password, String major );
    boolean editName( String id, String value );
    boolean editPassword( String id, String newPassword );
    String getPassword( String id );

    //manage studying status
    boolean startStudying( String id, int tableID, String message, String classInfo );
    boolean joinStudying( String id, int tableID );
    boolean stopStudying( String id );
    boolean isStudying( String id );

    List<String> fetchStudyMates( String id );
    
    Map<Integer, Boolean> fetchTableStatuses( );
    
    //manage study location, int would be some combination of floor and table number
    //system would check if table is available, and would return false to editing if not
    int fetchStudyLocation( String id );
    boolean editStudyLocation( String id, int location );
}
