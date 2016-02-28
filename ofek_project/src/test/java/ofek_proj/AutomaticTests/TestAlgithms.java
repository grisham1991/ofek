package ofek_proj.AutomaticTests;



import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
//run all tests
@RunWith(Suite.class)
@SuiteClasses({ DoubleEncryptorKeyTestFunctionsTest.class,EncryptionDecryptionTester.class,
	keyFunctionTesterForIntegerAlgorithms.class })
public class TestAlgithms {
	
}
