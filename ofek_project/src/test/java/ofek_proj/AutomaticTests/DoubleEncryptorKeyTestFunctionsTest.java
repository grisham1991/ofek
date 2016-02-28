package ofek_proj.AutomaticTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

import ofek_proj.EncryptionAlgorithms.EncryptionAlgorithm;
import ofek_proj.Utils.DoubleKey;
import ofek_proj.exceptions.InvalidEncryptionKeyException;

public class DoubleEncryptorKeyTestFunctionsTest {
	
	Injector injector=Guice.createInjector(new AlgorithmInjector());
	@Inject
	@Named("Init")
	EncryptionAlgorithm<DoubleKey<Integer>> algorithm;
	
	
	@Test
	public void testKeyTestWithGeneratedKey() throws InvalidEncryptionKeyException
	{
		injector.injectMembers(this);
		algorithm.testkey(algorithm.GenerateKey());
	}
	@Test
	public void testKeyStringConversion() throws InvalidEncryptionKeyException
	{
		injector.injectMembers(this);
		assertEquals("key1:45 key2:12",algorithm.GetStringKey(new DoubleKey<Integer>(45,12)));
	}

	@Test
	public void testKeyStringConversionWithBadString() throws InvalidEncryptionKeyException
	{
		injector.injectMembers(this);
		try{
			DoubleKey<Integer> doubleKey=algorithm.GetKeyFromString("key2:1r2 key2:145");
			algorithm.testkey(doubleKey);
			fail("shold fail on bad key string");
		}catch (Exception e){
			
		}
	}
	@Test
	public void testKeyfromStringConversion() throws InvalidEncryptionKeyException
	{
		injector.injectMembers(this);
		DoubleKey<Integer> doubleKey=algorithm.GetKeyFromString("key1:234 key2:94");
		assertEquals(234,doubleKey.GetFirstKey().intValue());
		assertEquals(94,doubleKey.GetSecondKey().intValue());
	}
	
		
}



