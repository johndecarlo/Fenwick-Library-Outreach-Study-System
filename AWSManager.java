import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

public class AWSManager implements RemoteDBManager {
	private AmazonDynamoDB dynamoDB;
	
	//Table Names
	private static final String USER_TABLE = "Users";
	private static final String TABLES_TABLE = "Tables";
	
	//Column Names - USER_TABLE
	private static final String USER_ID = "UserID";
	private static final String USER_IS_STUDYING = "IsStudying";
	private static final String USER_TABLE_AT = "MyTable";
	private static final String USER_NAME = "Name";
	private static final String USER_PASSWORD = "Password";
	private static final String USER_MAJOR = "Major";
	
	//Column Names - TABLES_TABLE
	private static final String TABLE_ID = "id";
	private static final String TABLE_ACTIVE = "IsActive";
	private static final String TABLE_FOUNDER = "FounderID";
	private static final String TABLE_STUDYMATES = "StudyMates";
	private static final String TABLE_MESSAGE = "StudyMessage";
	private static final String TABLE_CLASS = "ClassStudying";
	
	public AWSManager( ) {
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider( "./credentials", "default" );
        try {
            credentialsProvider.getCredentials( );
        } catch ( Exception e ) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location, and is in valid format.",
                    e);
        }
        dynamoDB = AmazonDynamoDBClientBuilder.standard( )
            .withCredentials( credentialsProvider )
            .withRegion( "us-west-2" )
            .build( );
	}

	@Override
	public boolean exists( String id ) {
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withS( id ) );
		scanFilter.put( USER_ID, condition );
		ScanRequest scanRequest = new ScanRequest( USER_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		return result.getCount( ) != 0;
	}
	
	@Override
	public String getPassword( String id ) {
		if ( !exists( id ) ) return null;
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withS( id ) );
		scanFilter.put( USER_ID, condition );
		ScanRequest scanRequest = new ScanRequest( USER_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		return result.getItems( ).get( 0 ).get( USER_PASSWORD ).getS( );
	}

	@Override
	public boolean addUser( String userID, String name, String password, String major ) {
		if ( exists( userID ) ) return false;
		Map<String, AttributeValue> item = newItem( userID, name, password, major );
		PutItemRequest request = new PutItemRequest( USER_TABLE, item );
		dynamoDB.putItem( request );
		return true;
	}

	@Override
	public boolean editName( String id, String value ) {
		if ( !exists( id ) ) return false;
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>( );
		item.put( USER_ID, new AttributeValue( id ) );
		item.put( USER_NAME, new AttributeValue( value ) );
		
		PutItemRequest request = new PutItemRequest( USER_TABLE, item );
		dynamoDB.putItem( request );
		return true;
	}

	@Override
	public boolean editPassword( String id, String newPassword ) {
		if ( !exists( id ) ) return false;
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>( );
		item.put( USER_ID, new AttributeValue( id ) );
		item.put( USER_PASSWORD, new AttributeValue( newPassword ) );
		
		PutItemRequest request = new PutItemRequest( USER_TABLE, item );
		dynamoDB.putItem( request );
		return true;
	}

	@Override
	public boolean startStudying( String id, int tableID, String message, String classInfo ) {
		if ( !exists( id ) ) return false;
		if ( isStudying( id ) ) return false;
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>( );
		item.put( USER_ID, new AttributeValue( id ) );
		item.put( USER_IS_STUDYING, new AttributeValue( ).withBOOL( true ) );
		
		PutItemRequest request = new PutItemRequest( USER_TABLE, item );
		dynamoDB.putItem( request );
		
		
		return true;
	}
	
	@Override
	public boolean joinStudying( String id, int tableID ) {
		
	}
	
	@Override
	public boolean stopStudying( String id ) {
		
	}

	@Override
	public boolean isStudying( String id ) {
		if ( !exists( id ) ) return false;
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withS( id ) );
		scanFilter.put( USER_ID, condition );
		ScanRequest scanRequest = new ScanRequest( USER_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		return result.getItems( ).get( 0 ).get( USER_IS_STUDYING ).getBOOL( );
	}
	
	@Override
	public List<String> fetchStudyMates( String id ) {
		if ( !exists( id ) ) return null;
		if ( !isStudying( id ) ) return null;
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withS( id ) );
		scanFilter.put( TABLE_FOUNDER, condition );
		ScanRequest scanRequest = new ScanRequest( TABLES_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		return result.getItems( ).get( 0 ).get( TABLE_STUDYMATES ).getSS( );
	}

	@Override
	public boolean addStudyMates( String id, String... studyMates ) {
		if ( !exists( id ) ) return false;
		if ( !isStudying( id ) ) return false;
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withS( id ) );
		scanFilter.put( TABLE_FOUNDER, condition );
		ScanRequest scanRequest = new ScanRequest( TABLES_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		List<String> currStudyMates = result.getItems( ).get( 0 ).get( TABLE_STUDYMATES ).getSS( );
		
		String[] newStudyMates = new String[studyMates.length + currStudyMates.size( )];
		int i;
		for ( i = 0; i < studyMates.length; i++ )
			newStudyMates[i] = studyMates[i];
		for ( int j = 0; j < currStudyMates.size( ); j++ ) {
			newStudyMates[i] = currStudyMates.get( j );
			i++;
		}
		
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>( );
		item.put( TABLE_ID, new AttributeValue( ).withN( result.getItems( ).get( 0 ).get( TABLE_ID ).getN( ) ) );
		item.put( TABLE_STUDYMATES, new AttributeValue( ).withSS( newStudyMates ) );
		
		PutItemRequest request = new PutItemRequest( TABLES_TABLE, item );
		dynamoDB.putItem( request );
		
		return true;
	}

	@Override
	public int fetchStudyLocation( String id ) {
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withS( id ) );
		scanFilter.put( USER_ID, condition );
		ScanRequest scanRequest = new ScanRequest( USER_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		return Integer.parseInt( result.getItems( ).get( 0 ).get( id ).getN( ) );
	}

	@Override
	public boolean editStudyLocation( String id, int tableID ) {
		if ( !exists( id ) ) return false;
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>( );
		item.put( USER_ID, new AttributeValue( id ) );
		item.put( USER_TABLE_AT, new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		
		PutItemRequest request = new PutItemRequest( USER_TABLE, item );
		dynamoDB.putItem( request );
		
		List<String> currStudyMates = fetchStudyMates( id );
		releaseTable( fetchStudyLocation( id ) );
		useTable( tableID, id, currStudyMates.toArray( new String[0] ) );
		return true;
	}
	
	private boolean useTable( int tableID, String founderID, String... studyMates ) {
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		scanFilter.put( "id", condition );
		ScanRequest scanRequest = new ScanRequest( TABLES_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		if ( result.getCount( ) == 0 ) {
			
		}
		else {
			
		}
		return true;
	}
	
	private boolean releaseTable( int tableID ) {
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		scanFilter.put( TABLE_ID, condition );
		ScanRequest scanRequest = new ScanRequest( TABLES_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		if ( result.getCount( ) == 0 ) return false;
		else {
			Map<String, AttributeValue> item = new HashMap<String, AttributeValue>( );
			item.put( TABLE_ID, new AttributeValue( ).withN( Integer.toString( tableID ) ) );
			item.put( TABLE_ACTIVE, new AttributeValue( ).withBOOL( false ) );
			
			PutItemRequest request = new PutItemRequest( USER_TABLE, item );
			dynamoDB.putItem( request );
		}
		return true;
	}
	
	private boolean tableIsTaken( int tableID ) {
		if ( !tableExists( tableID ) ) return false;
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		scanFilter.put( TABLE_ID, condition );
		ScanRequest scanRequest = new ScanRequest( TABLES_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		return result.getItems( ).get( 0 ).get( TABLE_ACTIVE ).getBOOL( );
	}
	
	private boolean tableExists( int tableID ) {
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		scanFilter.put( TABLE_ID, condition );
		ScanRequest scanRequest = new ScanRequest( TABLES_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		return result.getCount( ) == 1;
	}
	
	private static Map<String, AttributeValue> newItem( String userID, String name, String password, String major ) {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put( USER_ID, new AttributeValue( userID ) );
        item.put( USER_NAME, new AttributeValue( name ) );
        item.put( USER_PASSWORD, new AttributeValue( password ) );
        item.put( USER_MAJOR, new AttributeValue( major ) );

        return item;
    }
}
