import java.util.List;
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

	@Override
	public boolean exists(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getID(int studentID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addUser(int id, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean editName(int id, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean editStudentID(int id, int studentID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean toggleStudying(int id, boolean val) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStudying(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Integer> fetchStudyMates(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addStudyMates(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int fetchStudyLocation(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean editStudyLocation(int id, int location) {
		// TODO Auto-generated method stub
		return false;
	}
    
}