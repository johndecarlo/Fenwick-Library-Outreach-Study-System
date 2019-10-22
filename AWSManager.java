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
	private static final String TABLE_NAME = "Users";
	
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
		scanFilter.put( "UserID", condition );
		ScanRequest scanRequest = new ScanRequest( TABLE_NAME ).withScanFilter( scanFilter );
		ScanResult result = dynamoDB.scan( scanRequest );
		return result.getCount( ) != 0;
	}

	@Override
	public boolean addUser( String userID, String name, String password ) {
		if ( exists( userID ) ) return false;
		Map<String, AttributeValue> item = newItem( userID, name, password );
		PutItemRequest request = new PutItemRequest( TABLE_NAME, item );
		dynamoDB.putItem( request );
		return true;
	}

	@Override
	public boolean editName( String id, String value ) {
		if ( !exists( id ) ) return false;
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>( );
		item.put( "UserID", new AttributeValue( id ) );
		item.put( "Name", new AttributeValue( value ) );
		
		PutItemRequest request = new PutItemRequest( TABLE_NAME, item );
		dynamoDB.putItem( request );
		return true;
	}

	@Override
	public boolean editPassword( String id, String newPassword ) {
		if ( !exists( id ) ) return false;
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>( );
		item.put( "UserID", new AttributeValue( id ) );
		item.put( "Password", new AttributeValue( newPassword ) );
		
		PutItemRequest request = new PutItemRequest( TABLE_NAME, item );
		dynamoDB.putItem( request );
		return true;
	}

	@Override
	public boolean toggleStudying( String id, boolean val ) {
		if ( !exists( id ) ) return false;
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>( );
		item.put( "UserID", new AttributeValue( id ) );
		item.put( "IsStuddying", new AttributeValue( ).withBOOL( val ) );
		
		PutItemRequest request = new PutItemRequest( TABLE_NAME, item );
		dynamoDB.putItem( request );
		return true;
	}

	@Override
	public boolean isStudying( String id ) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public List<String> fetchStudyMates( String id ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addStudyMates( String id, String... studyMates ) {
		if ( !exists( id ) ) return false;
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>( );
		item.put( "UserID", new AttributeValue( id ) );
		item.put( "StudyMates", new AttributeValue( ).withSS( studyMates ) );
		
		PutItemRequest request = new PutItemRequest( TABLE_NAME, item );
		dynamoDB.putItem( request );
		return true;
	}

	@Override
	public int fetchStudyLocation( String id ) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean editStudyLocation( String id, int location ) {
		if ( !exists( id ) ) return false;
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>( );
		item.put( "UserID", new AttributeValue( id ) );
		item.put( "StudyLocation", new AttributeValue( ).withN( Integer.toString( location ) ) );
		
		PutItemRequest request = new PutItemRequest( TABLE_NAME, item );
		dynamoDB.putItem( request );
		return true;
	}
	
	private static Map<String, AttributeValue> newItem( String userID, String name, String password ) {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put( "UserID", new AttributeValue( userID ) );
        item.put("Name", new AttributeValue( name ) );
        item.put("Password", new AttributeValue( password ) );

        return item;
    }
}