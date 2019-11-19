package com.floss.manager;

import java.util.List;
import java.util.Map;

//Basic methods needed for accessing and changing information per student in the remote database
public interface RemoteDBManager {
	
	/**
	 * Check if a given username already exists in the system
	 * @param id The username to search for
	 * @return True if exists, false if not
	 */
    boolean exists( String id );
    
    String getName( String id );
    String getMajor( String id );
    String getClassInfo( int tableID );

    /**
     * Adds a new user to the total table of users
     * @param userID The user id, "username" to associate with this user, must
     * be unique among all users
     * @param name The name of this individual
     * @param password The password for logging onto app
     * @param major The current major of this student
     * @return True if successful, false if invalid, most likely due to
     * a non-unique username
     */
    boolean addUser( String userID, String name, String password, String major );
    
    /**
     * Add one or many friends to my friends list
     * @param myID The user id to add friends to
     * @param friendID The friend(s) to append to the friends list
     * @return True if successful, false if any data is invalid
     */
    boolean addFriend( String myID, String... friendID );
    
    /**
     * Remove a friend from my friends list
     * @param myID The user id to remove a friend from
     * @param friendID The friend to remove
     * @return True if successful, false if any data is invalid
     */
    boolean removeFriend( String myID, String friendID );
    
    /**
     * Fetch a list of the user ids of all of your friends
     * @param myID The id of the user to fetch friends for
     * @return The list of user ids
     */
    List<String> getFriendList( String myID );
    
    /**
     * Alter the stored name value of a given user
     * @param id The user to change
     * @param value The new value for name
     * @return True if successful, false if invalid
     */
    boolean editName( String id, String value );
    
    /**
     * Alter the password of a given user to a new value
     * @param id The user to change password of
     * @param newPassword The new value for password
     * @return True if successful, false if invalid
     */
    boolean editPassword( String id, String newPassword );
    
    /**
     * Fetch the password associated with a given user
     * to check for validity, insecure but effective
     * @param id The user to fetch password for
     * @return The password
     */
    String getPassword( String id );

    /**
     * Begin studying at a provided location, for a provided topic, claiming
     * a table and setting a founder of this new study group
     * @param id The soon to be founder of a new study group
     * @param tableID The table to study at
     * @param message The message to display to fellow users, CANNOT be null
     * @param classInfo The information about the class to study for, given as CODE####
     * without spaces
     * @return True if successful, false if any data is invalid
     */
    boolean startStudying( String id, int tableID, String message, String classInfo );
    
    /**
     * Add id to the list of study mates for a given group, thereby joining the group
     * @param id The user to add
     * @param tableID The location of the study group to join
     * @return True if successful, false if invalid id or tableID
     */
    boolean joinStudying( String id, int tableID );
    
    /**
     * Halt studying and remove id from the group, if id is the founder, then
     * the entire group is absolved, and table released
     * @param id The id of the user who wants to stop studying
     * @return True if successful, false if invalid id
     */
    boolean stopStudying( String id );
    
    /**
     * Check if a given user is currently studying
     * @param id The user id of who to search for
     * @return True if studying, false if not, or if DNE
     */
    boolean isStudying( String id );

    /**
     * Attain a listing of all of the fellow study mates given
     * a specific individual
     * @param id The user id of who to find study mates for
     * @return A list of user id's of fellow study mates, if the given id is 
     * currently studying, if the user given by id is not studying, returns null
     */
    List<String> fetchStudyMates( int tableID );
    
    /**
     * Creates a map of all active tables to the study group's message,
     * if a table is not included then it is open
     * @return Mapping of id's to the study group's message, which has
     * to have a value
     */
    Map<Integer, String> fetchTableStatuses( );
    
    /**
     * Attain the current study location of a given user
     * @param id The user to find the study location for
     * @return The table id, or 0 if user is not studying
     */
    int fetchStudyLocation( String id );
}
