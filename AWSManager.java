import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
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
	private static final String TABLE_FOUNDER = "FounderID";
	private static final String TABLE_STUDYMATES = "StudyMates";
	private static final String TABLE_MESSAGE = "StudyMessage";
	private static final String TABLE_CLASS = "ClassStudying";
	
	public AWSManager( ) {
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\J316R\\.aws\\credentials), and is in valid format.",
                    e);
        }
        dynamoDB = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(credentialsProvider)
            .withRegion("us-west-2")
            .build();
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
	public boolean startStudying( String id, int tableID, String message, String classInfo ) { //change to update--------------
		if ( !exists( id ) ) return false;
		if ( isStudying( id ) ) return false;
		if ( tableIsTaken( tableID ) ) return false;
		if ( message == null ) return false;
		
		//update data in User table
		Table userTable = new DynamoDB( dynamoDB ).getTable( USER_TABLE );
		
		UpdateItemSpec spec = new UpdateItemSpec( ).withPrimaryKey( new PrimaryKey( USER_ID, id ) ).
				withUpdateExpression( "set " + USER_TABLE_AT + " = :table, " + USER_IS_STUDYING + " = :status" ).
				withValueMap( new ValueMap( ).withNumber( ":table", tableID ).withBoolean( ":status", true ) );
		
		userTable.updateItem( spec );
		
		//update data in Tables table
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>( );
		item.put( TABLE_FOUNDER, new AttributeValue( id ) );
		item.put( TABLE_CLASS, new AttributeValue( classInfo ) );
		item.put( TABLE_ID, new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		item.put( TABLE_MESSAGE, new AttributeValue( message ) );

		PutItemRequest request = new PutItemRequest( USER_TABLE, item );
		dynamoDB.putItem( request );
		
		return true;
	}
	
	@Override
	public boolean joinStudying( String id, int tableID ) { //change to update-----------------------
		if ( !exists( id ) ) return false;
		if ( isStudying( id ) ) return false;
		if ( !tableIsTaken( tableID ) ) return false;
		
		Table userTable = new DynamoDB( dynamoDB ).getTable( USER_TABLE );
		
		UpdateItemSpec spec = new UpdateItemSpec( ).withPrimaryKey( new PrimaryKey( USER_ID, id ) ).
				withUpdateExpression( "set " + USER_TABLE_AT + " = :table, " + USER_IS_STUDYING + " = :status" ).
				withValueMap( new ValueMap( ).withNumber( ":table", tableID ).withBoolean( ":status", true ) );
		
		userTable.updateItem( spec );
				
		return addStudyMate( tableID, id );
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
					vals.get( i ).get( TABLE_MESSAGE ).getS( ) );
		
		return tableStatuses;
	}
	
	@Override
	public boolean stopStudying( String id ) { //change to update--------------
		if ( !exists( id ) ) return false;
		if ( !isStudying( id ) ) return false;
		
		//update USER_TABLE to set the USER_TABLE_AT value to 0 and get the USER_TABLE_AT value
		Table userTable = new DynamoDB( dynamoDB ).getTable( USER_TABLE ),
				tablesTable = new DynamoDB( dynamoDB ).getTable( TABLES_TABLE );
		
		UpdateItemSpec spec = new UpdateItemSpec( ).withPrimaryKey( new PrimaryKey( USER_ID, id ) ).
				withUpdateExpression( "set " + USER_TABLE_AT + " = :val, " + USER_IS_STUDYING + " = :study" ).
				withValueMap( new ValueMap( ).withNumber( ":val", 0 ).withBoolean( ":study", false ) ).
				withReturnValues( ReturnValue.UPDATED_OLD );

		UpdateItemOutcome outcome = userTable.updateItem( spec );
		int tableID = outcome.getItem( ).getNumber( USER_TABLE_AT ).intValue( );
		
		//check to see if id is founder
		HashMap<String, Condition> filter = new HashMap<String, Condition>( );
		filter.put( TABLE_FOUNDER, new Condition( )
				.withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withS( id ) ) );
		ScanRequest request = new ScanRequest( TABLES_TABLE )
				.withScanFilter( filter );
		ScanResult result = dynamoDB.scan( request );
		
		if ( result.getCount( ) == 0 ) { //i am studymate, remove myself from studymates list
			filter.clear( );
			filter.put( TABLE_ID, new Condition( )
					.withComparisonOperator( ComparisonOperator.EQ.toString( ) )
					.withAttributeValueList( new AttributeValue( ).withN( Integer.toString( tableID ) ) ) );
			request = new ScanRequest( TABLES_TABLE )
					.withScanFilter( filter );
			result = dynamoDB.scan( request );
			
			List<String> studyMates = result.getItems( ).get( 0 ).get( TABLE_STUDYMATES )
					.getSS( );
			studyMates.remove( id );
			
			spec = new UpdateItemSpec( ).withPrimaryKey( new PrimaryKey( TABLE_ID, tableID ) ).
					withUpdateExpression( "set " + TABLE_STUDYMATES + " = :newSet" ).
					withValueMap( new ValueMap( ).withStringSet( ":newSet", studyMates
							.toArray( new String[1] ) ) );
			userTable.updateItem( spec );
		}
		else { //I am founder, kill the study location and remove all references for studymates to this location
			filter.clear( );
			Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
					.withAttributeValueList( new AttributeValue( ).withN( Integer.toString( tableID ) ) );
			filter.put( TABLE_ID, condition );
			request = new ScanRequest( TABLES_TABLE ).withScanFilter( filter );
			result = dynamoDB.scan( request );
			
			if ( result.getCount( ) == 0 ) return false;
			List<String> idsAtTable = result.getItems( ).get( 0 ).get( TABLE_STUDYMATES ).getSS( );
			
			for ( String studyMate : idsAtTable ) {
				spec = new UpdateItemSpec( ).withPrimaryKey( new PrimaryKey( USER_ID, studyMate ) ).
					withUpdateExpression( "set " + USER_TABLE_AT + " = :val, " + USER_IS_STUDYING + " = :study" ).
					withValueMap( new ValueMap( ).withNumber( ":val", 0 ).withBoolean( ":study", false ) );
				userTable.updateItem( spec );
			}
			
			tablesTable.deleteItem( new PrimaryKey( TABLE_ID, tableID ) );
		}
			
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
	public List<String> fetchStudyMates( int tableID ) {
		if ( !tableIsTaken( tableID ) ) return null;
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		scanFilter.put( TABLE_ID, condition );
		ScanRequest scanRequest = new ScanRequest( TABLES_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		
		List<String> peopleStudying = result.getItems( ).get( 0 ).get( TABLE_STUDYMATES ).getSS( );
		peopleStudying.add( result.getItems( ).get( 0 ).get( TABLE_FOUNDER ).getS( ) );
		return peopleStudying;
	}

	private boolean addStudyMate( int tableID, String studyMate ) { //change to update------------
		Table tables = new DynamoDB( dynamoDB ).getTable( TABLES_TABLE );
		List<String> newStudyMate = fetchStudyMates( tableID );
		newStudyMate.add( studyMate );
		
		UpdateItemSpec spec = new UpdateItemSpec( )
				.withPrimaryKey( TABLE_ID, tableID )
				.withUpdateExpression( "set " + TABLE_STUDYMATES + " = :newVal" )
				.withValueMap( new ValueMap(  ).withStringSet( ":newVal", newStudyMate.toArray( new String[1] ) ) );
		
		tables.updateItem( spec );
		
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
	
	private boolean tableIsTaken( int tableID ) {
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		scanFilter.put( TABLE_ID, condition );
		ScanRequest scanRequest = new ScanRequest( TABLES_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		return result.getCount( ) != 0;
	}
	
	private static Map<String, AttributeValue> newItem( String userID, String name, String password, String major ) {
        	Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        	item.put( USER_ID, new AttributeValue( userID ) );
        	item.put( USER_NAME, new AttributeValue( name ) );
        	item.put( USER_PASSWORD, new AttributeValue( password ) );
        	item.put( USER_MAJOR, new AttributeValue( major ) );
        	item.put( USER_IS_STUDYING, new AttributeValue( ).withBOOL( false ) );
        	item.put( USER_TABLE_AT, new AttributeValue( ).withN( Integer.toString( 0 ) ) );

        	return item;
    	}
}
