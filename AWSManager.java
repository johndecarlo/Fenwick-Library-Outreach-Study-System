import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ConditionalOperator;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

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
	public boolean editName( String id, String value ) { //change to update---------------
		if ( !exists( id ) ) return false;
		Table userTable = new DynamoDB( dynamoDB ).getTable( USER_TABLE );
		
		UpdateItemSpec spec = new UpdateItemSpec( ).withPrimaryKey( new PrimaryKey( USER_ID, id ) ).
				withUpdateExpression( "set " + USER_NAME + " = :val" ).
				withValueMap( new ValueMap( ).withString( ":val", value ) );
		
		userTable.updateItem( spec );
		
		return true;
	}

	@Override
	public boolean editPassword( String id, String newPassword ) { //change to update----------------------
		if ( !exists( id ) ) return false;
		Table userTable = new DynamoDB( dynamoDB ).getTable( USER_TABLE );
		
		UpdateItemSpec spec = new UpdateItemSpec( ).withPrimaryKey( new PrimaryKey( USER_ID, id ) ).
				withUpdateExpression( "set " + USER_PASSWORD + " = :val" ).
				withValueMap( new ValueMap( ).withString( ":val", newPassword ) );
		
		userTable.updateItem( spec );
		
		return true;
	}

	@Override
	public boolean startStudying( String id, int tableID, String message, String classInfo ) { //change to update
		if ( !exists( id ) ) return false;
		if ( isStudying( id ) ) return false;
		if ( tableIsTaken( tableID ) ) return false;
		
		Map<String, AttributeValue> userItem = new HashMap<String, AttributeValue>( ),
				tableItem = new HashMap<String, AttributeValue>( );
		
		userItem.put( USER_ID, new AttributeValue( id ) );
		userItem.put( USER_IS_STUDYING, new AttributeValue( ).withBOOL( true ) );
		userItem.put( USER_TABLE_AT, new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		
		tableItem.put( TABLE_ID, new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		tableItem.put( TABLE_MESSAGE, new AttributeValue( message ) );
		tableItem.put( TABLE_CLASS, new AttributeValue( classInfo ) );
		tableItem.put( TABLE_ACTIVE, new AttributeValue( ).withBOOL( true ) );
		tableItem.put( TABLE_FOUNDER, new AttributeValue( id ) );
		
		PutItemRequest userTableRequest = new PutItemRequest( USER_TABLE, userItem ),
				tableRequest = new PutItemRequest( TABLES_TABLE, tableItem );
		dynamoDB.putItem( userTableRequest );
		dynamoDB.putItem( tableRequest );
		
		return true;
	}
	
	@Override
	public boolean joinStudying( String id, int tableID ) { //change to update
		if ( !exists( id ) ) return false;
		if ( isStudying( id ) ) return false;
		if ( !tableIsTaken( tableID ) ) return false;
		
		Map<String, AttributeValue> userItem = new HashMap<String, AttributeValue>( );
		
		userItem.put( USER_ID, new AttributeValue( id ) );
		userItem.put( USER_IS_STUDYING, new AttributeValue( ).withBOOL( true ) );
		userItem.put( USER_TABLE_AT, new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		
		addStudyMate( tableID, id );
		
		PutItemRequest request = new PutItemRequest( USER_TABLE, userItem );
		dynamoDB.putItem( request );
		
		return true;
	}
	
	@Override
	public Map<Integer, String> fetchTableStatuses( ) {
		ScanRequest scanRequest = new ScanRequest( TABLES_TABLE );
		ScanResult scanResult = dynamoDB.scan( scanRequest );
		Map<Integer, String> tableStatuses = new TreeMap<Integer, String>( );
		
		int elems = scanResult.getCount( );
		List<Map<String, AttributeValue>> vals = scanResult.getItems( );
		for ( int i = 0; i < elems; i++ )
			tableStatuses.put( Integer.parseInt( vals.get( i ).get( TABLE_ID ).getN( ) ), 
					vals.get( i ).get( TABLE_ACTIVE ).getBOOL( )?vals.get( i ).get( TABLE_MESSAGE ).getS( ):null );
		
		return tableStatuses;
	}
	
	@Override
	public boolean stopStudying( String id ) { //change to update
		if ( !exists( id ) ) return false;
		if ( !isStudying( id ) ) return false;
		
		//update USER_TABLE to set the USER_TABLE_AT value to 0 and get the USER_TABLE_AT value
		Table userTable = new DynamoDB( dynamoDB ).getTable( USER_TABLE ),
				tablesTable = new DynamoDB( dynamoDB ).getTable( TABLES_TABLE );
		
		UpdateItemSpec spec = new UpdateItemSpec( ).withPrimaryKey( new PrimaryKey( USER_ID, id ) ).
				withUpdateExpression( "set " + USER_TABLE_AT + " = :val" ).
				withValueMap( new ValueMap( ).withNumber( ":val", 0 ) ).
				withReturnValues( ReturnValue.UPDATED_OLD );
		
		UpdateItemOutcome outcome = userTable.updateItem( spec );
		int tableID = outcome.getItem( ).getNumber( USER_TABLE_AT ).intValue( );
		
		//delete the table entry from TABLES_TABLE where the TABLE_ID == tableID
		tablesTable.deleteItem( new PrimaryKey( TABLE_ID, tableID ) );
		
		return true;
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

	private boolean addStudyMate( int tableID, String studyMate ) { //change to update
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		scanFilter.put( TABLE_ID, condition );
		ScanRequest scanRequest = new ScanRequest( TABLES_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		List<String> currStudyMates = result.getItems( ).get( 0 ).get( TABLE_STUDYMATES ).getSS( );
		
		String[] newStudyMates = new String[1 + currStudyMates.size( )];
		int i;
		for ( i = 0; i < currStudyMates.size( ); i++ )
			newStudyMates[i] = currStudyMates.get( i );
		newStudyMates[i] = studyMate;
		
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
	public boolean editStudyLocation( String id, int tableID ) { //change to update
		if ( !exists( id ) ) return false;
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>( );
		item.put( USER_ID, new AttributeValue( id ) );
		item.put( USER_TABLE_AT, new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		
		PutItemRequest request = new PutItemRequest( USER_TABLE, item );
		dynamoDB.putItem( request );
		
		List<String> currStudyMates = fetchStudyMates( id );
		useTable( tableID, id, currStudyMates.toArray( new String[0] ) );
		return true;
	}
	
	private boolean useTable( int tableID, String founderID, String... studyMates ) { //change to update
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
