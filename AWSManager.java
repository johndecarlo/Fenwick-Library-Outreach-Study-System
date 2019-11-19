package com.floss.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfilesConfigFile;
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
	private static final String USER_FRIENDS = "FriendList";
	
	//Column Names - TABLES_TABLE
	private static final String TABLE_ID = "id";
	private static final String TABLE_GROUP = "PeopleStudying";
	private static final String TABLE_MESSAGE = "StudyMessage";
	private static final String TABLE_CLASS = "ClassStudying";
	
	public AWSManager( ) {
		
		try {
			File configFile = File.createTempFile( "credentials", null );
			configFile.deleteOnExit( );
			configFile.setWritable( true );
		
			String fileData = "[default]\n" + 
					"aws_access_key_id=AKIAXWQVTLUZDFVPFHGF\n" + 
					"aws_secret_access_key=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";
		
			BufferedWriter writer = new BufferedWriter( new FileWriter( configFile ) );
			writer.write( fileData );
			writer.close( );
		
			ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider( new ProfilesConfigFile( configFile ), "default" );
            credentialsProvider.getCredentials();
            dynamoDB = AmazonDynamoDBClientBuilder.standard()
                    .withCredentials(credentialsProvider)
                    .withRegion("us-west-2")
                    .build();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location.", e);
        }
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
	public String getClassInfo( int tableID ) {
		if ( !tableIsTaken( tableID ) ) return null;
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		scanFilter.put( TABLE_ID, condition );
		ScanResult result = dynamoDB.scan( new ScanRequest( TABLES_TABLE ).withScanFilter( scanFilter ) );
		return result.getItems( ).get( 0 ).get( TABLE_CLASS ).getS( );
	}
	
	@Override
	public String getName( String id ) {
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withS( id ) );
		scanFilter.put( USER_ID, condition );
		ScanRequest scanRequest = new ScanRequest( USER_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		return result.getCount( ) != 0?result.getItems( ).get( 0 ).get( USER_NAME ).getS( ):null;
	}
	
	@Override
	public String getMajor( String id ) {
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withS( id ) );
		scanFilter.put( USER_ID, condition );
		ScanRequest scanRequest = new ScanRequest( USER_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		return result.getCount( ) != 0?result.getItems( ).get( 0 ).get( USER_MAJOR ).getS( ):null;
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
	public boolean addFriend( String myID, String... friendID ) {
		if ( !exists( myID ) ) return false;
		for( String id : friendID )
			if ( !exists( id ) ) return false;
		
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withS( myID ) );
		scanFilter.put( USER_ID, condition );
		ScanResult result = dynamoDB.scan( new ScanRequest( USER_TABLE ).withScanFilter( scanFilter ) );
		String[] updatedFriendsList;
		AttributeValue attVal = result.getItems( ).get( 0 ).get( USER_FRIENDS );
		if ( attVal == null )
			updatedFriendsList = friendID;
		else {
			List<String> resultList = attVal.getSS( );
			updatedFriendsList = new String[friendID.length + resultList.size( )];
			int i = 0;
			for ( String n : resultList ) {
				updatedFriendsList[i] = n;
				i++;
			}
			for ( String n : friendID ) {
				updatedFriendsList[i] = n;
				i++;
			}
		}
		
		UpdateItemSpec spec = new UpdateItemSpec( ).withPrimaryKey( new PrimaryKey( USER_ID, myID ) )
				.withUpdateExpression( "set " + USER_FRIENDS + " = :newList" )
				.withValueMap( new ValueMap( ).withStringSet( ":newList", updatedFriendsList ) );
					
		new DynamoDB( dynamoDB ).getTable( USER_TABLE ).updateItem( spec );
		return true;
	}
	
	@Override
	public boolean removeFriend( String myID, String friendID ) {
		if ( !exists( myID ) && !exists( friendID ) ) return false;
		
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withS( myID ) );
		scanFilter.put( USER_ID, condition );
		ScanResult result = dynamoDB.scan( new ScanRequest( USER_TABLE ).withScanFilter( scanFilter ) );
		if ( result.getItems( ).get( 0 ).get( USER_FRIENDS ) == null ) return false;
		
		List<String> resultList = result.getItems( ).get( 0 ).get( USER_FRIENDS ).getSS( );
		
		if ( !resultList.remove( friendID ) ) return false; //friendID not in list, stop execution without changing anything
		if ( resultList.size() == 0 ) resultList.add( "-------PlaceHolder-------" );
		
		UpdateItemSpec spec = new UpdateItemSpec( ).withPrimaryKey( new PrimaryKey( USER_ID, myID ) )
				.withUpdateExpression( "set " + USER_FRIENDS + " = :newList" )
				.withValueMap( new ValueMap( ).withStringSet( ":newList", resultList.toArray( new String[0] ) ) );
		
		new DynamoDB( dynamoDB ).getTable( USER_TABLE ).updateItem( spec );
		return true;
	}

	@Override
	public List<String> getFriendList( String myID ) {
		if ( !exists( myID ) ) return null;
		
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withS( myID ) );
		scanFilter.put( USER_ID, condition );
		ScanResult result = dynamoDB.scan( new ScanRequest( USER_TABLE ).withScanFilter( scanFilter ) );
		AttributeValue val = result.getItems( ).get( 0 ).get( USER_FRIENDS );
		return val == null?null:val.getSS( );
	}
	
	@Override
	public boolean editName( String id, String value ) { 
		if ( !exists( id ) ) return false;
		Table userTable = new DynamoDB( dynamoDB ).getTable( USER_TABLE );
		
		UpdateItemSpec spec = new UpdateItemSpec( ).withPrimaryKey( new PrimaryKey( USER_ID, id ) ).
				withUpdateExpression( "set " + USER_NAME + " = :val" ).
				withValueMap( new ValueMap( ).withString( ":val", value ) );
		
		userTable.updateItem( spec );
		
		return true;
	}

	@Override
	public boolean editPassword( String id, String newPassword ) { 
		if ( !exists( id ) ) return false;
		Table userTable = new DynamoDB( dynamoDB ).getTable( USER_TABLE );
		
		UpdateItemSpec spec = new UpdateItemSpec( ).withPrimaryKey( new PrimaryKey( USER_ID, id ) ).
				withUpdateExpression( "set " + USER_PASSWORD + " = :val" ).
				withValueMap( new ValueMap( ).withString( ":val", newPassword ) );
		
		userTable.updateItem( spec );
		
		return true;
	}

	@Override
	public boolean startStudying( String id, int tableID, String message, String classInfo ) { 
		if ( !exists( id ) )
			return false;
		if ( isStudying( id ) )
			return false;
		if ( tableIsTaken( tableID ) ) 
			return false;
		if ( message == null )
			return false;
		
		//update data in User table
		Table userTable = new DynamoDB( dynamoDB ).getTable( USER_TABLE );
		
		UpdateItemSpec spec = new UpdateItemSpec( ).withPrimaryKey( new PrimaryKey( USER_ID, id ) ).
				withUpdateExpression( "set " + USER_TABLE_AT + " = :table, " + USER_IS_STUDYING + " = :status" ).
				withValueMap( new ValueMap( ).withNumber( ":table", tableID ).withBoolean( ":status", true ) );
		
		userTable.updateItem( spec );
		
		//update data in Tables table
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>( );
		item.put( TABLE_GROUP, new AttributeValue( ).withSS( id ) );
		item.put( TABLE_CLASS, new AttributeValue( classInfo ) );
		item.put( TABLE_ID, new AttributeValue( ).withN( Integer.toString( tableID ) ) );
		item.put( TABLE_MESSAGE, new AttributeValue( message ) );

		PutItemRequest request = new PutItemRequest( TABLES_TABLE, item );
		dynamoDB.putItem( request );
		
		return true;
	}
	
	@Override
	public boolean joinStudying( String id, int tableID ) { 
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
	public boolean stopStudying( String id ) { 
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
		
		List<String> group = fetchStudyMates( tableID );
		if ( group.size( ) == 1 )
			tablesTable.deleteItem( new PrimaryKey( TABLE_ID, tableID ) );
		else {
			group.remove( id );
			spec = new UpdateItemSpec( ).withPrimaryKey( new PrimaryKey( TABLE_ID, tableID ) )
					.withUpdateExpression( "set " + TABLE_GROUP + " = :newList" )
					.withValueMap( new ValueMap( ).withStringSet( ":newList", group.toArray( new String[0] ) ) );
			
			tablesTable.updateItem( spec );
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
				
		AttributeValue attVal = result.getItems( ).get( 0 ).get( TABLE_GROUP );
		return attVal == null?null:attVal.getSS( );
	}

	private boolean addStudyMate( int tableID, String studyMate ) { 
		Table tables = new DynamoDB( dynamoDB ).getTable( TABLES_TABLE );
		List<String> newStudyMate = fetchStudyMates( tableID );
		if ( newStudyMate == null ) newStudyMate = new ArrayList<String>( );
		newStudyMate.add( studyMate );
		
		UpdateItemSpec spec = new UpdateItemSpec( )
				.withPrimaryKey( TABLE_ID, tableID )
				.withUpdateExpression( "set " + TABLE_GROUP + " = :newVal" )
				.withValueMap( new ValueMap(  ).withStringSet( ":newVal", newStudyMate.toArray( new String[0] ) ) );
		
		tables.updateItem( spec );
		
		return true;
	}

	@Override
	public int fetchStudyLocation( String id ) {
		if ( !isStudying( id ) ) return 0;
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>( );
		Condition condition = new Condition( ).withComparisonOperator( ComparisonOperator.EQ.toString( ) )
				.withAttributeValueList( new AttributeValue( ).withS( id ) );
		scanFilter.put( USER_ID, condition );
		ScanRequest scanRequest = new ScanRequest( USER_TABLE ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		return Integer.parseInt( result.getItems( ).get( 0 ).get( USER_TABLE_AT ).getN( ) );
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
