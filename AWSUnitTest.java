import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import com.floss.manager.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AWSUnitTest {
	private RemoteDBManager manager;

	@BeforeEach
	void setUp() throws Exception {
		manager = new AWSManager( );
	}

	@Test
	void addUserTest( ) {
		if ( !manager.addUser( "billyBob", "Bill Bobby", "secret", "CS" ) )
			fail( "Could not add" );
		assertTrue( manager.exists( "billyBob" ) );
		assertTrue( new String( "Bill Bobby" ).equals( manager.getName( "billyBob" ) ) );
		assertTrue( new String( "CS" ).equals( manager.getMajor( "billyBob" ) ) );
		assertTrue( new String( "secret" ).equals( manager.getPassword( "billyBob" ) ) );
		manager.removeUser( "billyBob" );
	}
	
	@Test
	void editUserTest( ) {
		if ( !manager.addUser( "billyBob", "Bill Bobby", "secret", "CS" ) )
			fail( "Could not add" );
		assertTrue( manager.editName( "billyBob", "Bobby Billy" ) );
		assertTrue( manager.getName( "billyBob" ).equals( "Bobby Billy" ) );
		assertTrue( manager.editPassword( "billyBob", "password" ) );
		assertTrue( manager.getPassword( "billyBob" ).equals( "password" ) );
		manager.removeUser( "billyBob" );
	}
	
	@Test
	void friendTest( ) {
		if ( !manager.addUser( "billyBob", "Bill Bobby", "secret", "CS" ) )
			fail( "Could not add" );
		assertNull( manager.getFriendList( "billyBob" ) );
		if ( !manager.addUser( "roberto", "Bill Bobby", "secret", "CS" ) )
			fail( "Could not add" );
		assertTrue( manager.addFriend( "billyBob", "roberto" ) );
		assertTrue( manager.getFriendList( "billyBob" ).get( 0 ).equals( "roberto" ) );
		assertTrue( manager.removeFriend( "billyBob", "roberto" ) );
		assertTrue( manager.getFriendList( "billyBob" ).size( ) == 1 );
		assertTrue( manager.getFriendList( "billyBob" ).get( 0 ).equals( "-------PlaceHolder-------" ) );
		manager.removeUser( "billyBob" );
		manager.removeUser( "roberto" );
	}
	
	@Test
	void startStopStudyingTest( ) {
		if ( !manager.addUser( "billyBob", "Bill Bobby", "secret", "CS" ) )
			fail( "Could not add" );
		assertFalse( manager.isStudying( "billyBob" ) );
		assertTrue( manager.startStudying( "billyBob", 11, "Wassup", "UNIV100" ) );
		assertTrue( manager.isStudying( "billyBob" ) );
		assertTrue( manager.fetchStudyLocation( "billyBob" ) == 11 );
		assertTrue( manager.fetchStudyMates( 11 ).size( ) == 1 &&
				manager.fetchStudyMates( 11 ).get( 0 ).equals( "billyBob" ) );
		assertTrue( manager.stopStudying( "billyBob" ) );
		assertTrue( manager.fetchStudyLocation( "billyBob" ) == 0 );
		assertNull( manager.fetchStudyMates( 11 ) );
		manager.removeUser( "billyBob" );
	}
	
	@Test
	void joinStudyingTest( ) {
		if ( !manager.addUser( "billyBob", "Bill Bobby", "secret", "CS" ) )
			fail( "Could not add" );
		if ( !manager.addUser( "roberto", "Bill Bobby", "secret", "CS" ) )
			fail( "Could not add" );
		assertTrue( manager.startStudying( "billyBob", 153, "HowdyDoody", "CS105" ) );
		assertTrue( manager.joinStudying( "roberto", 153 ) );
		List<String> mates = manager.fetchStudyMates( 153 );
		assertTrue( mates.size( ) == 2 );
		assertTrue( mates.contains( "billyBob" ) );
		assertTrue( mates.contains( "roberto" ) );
		assertTrue( manager.isStudying( "billyBob" ) );
		assertTrue( manager.isStudying( "roberto" ) );
		
		manager.removeUser( "billyBob" );
		manager.removeUser( "roberto" );
	}
	
	@Test
	void tableStatusTest( ) {
		if ( !manager.addUser( "billyBob", "Bill Bobby", "secret", "CS" ) )
			fail( "Could not add" );
		if ( !manager.addUser( "roberto", "Bill Bobby", "secret", "CS" ) )
			fail( "Could not add" );
		assertTrue( manager.startStudying( "billyBob", 134, "Wassup", "UNIV100" ) );
		assertTrue( manager.startStudying( "roberto", 33, "Howdy", "CS300" ) );
		Map<Integer,String> map = manager.fetchTableStatuses( );
		assertTrue( map.containsKey( 134 ) );
		assertTrue( map.containsKey( 33 ) );
		assertTrue( map.containsValue( "Wassup" ) );
		assertTrue( map.containsValue( "Howdy" ) );
		manager.removeUser( "billyBob" );
		manager.removeUser( "roberto" );
	}
}
