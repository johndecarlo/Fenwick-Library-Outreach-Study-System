import static org.junit.jupiter.api.Assertions.*;
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
	void test() {
		fail("Not yet implemented");
	}

}
