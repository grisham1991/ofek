package ofek_proj.AutomaticTests;

import static org.junit.Assert.*;

import org.junit.Test;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

import ofek_proj.EncryptionAlgorithms.AlgorithmComparator;
import ofek_proj.EncryptionAlgorithms.EncryptionAlgorithm;
import ofek_proj.EncryptionAlgorithms.RepeatEncryption;
import ofek_proj.EncryptionAlgorithms.ShiftUpEncryptionAlgorithm;
import ofek_proj.EncryptionAlgorithms.XorEncryptionAlgorithm;
import ofek_proj.exceptions.InvalidEncryptionKeyException;

public class keyFunctionTesterForIntegerAlgorithms {
	
	Injector injector=Guice.createInjector(new AlgorithmInjector());
	@Inject
	@Named("Init")
	EncryptionAlgorithm<Integer> algorithm;
	
	
	//test if generated key passes key test
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
		assertEquals("234",algorithm.GetStringKey(234));
	}
	@Test
	public void testKeyStringConversionWithBadString() throws InvalidEncryptionKeyException
	{
		injector.injectMembers(this);
		try{
			algorithm.GetKeyFromString("234@@3");
			fail("shold fail on bad string");
		}catch (Exception e){
			
		}
	}
	@Test
	public void testKeyfromStringConversion() throws InvalidEncryptionKeyException
	{
		injector.injectMembers(this);
		Integer intkey=algorithm.GetKeyFromString("234");
		assertEquals(234,intkey.intValue());
	}
	//test if exception is thrown on negative key
	@Test(expected = InvalidEncryptionKeyException.class)
	public void testKeyTestWithNegativeKey() throws InvalidEncryptionKeyException {
		injector.injectMembers(this);
		Integer errkey=-300;
		algorithm.testkey(errkey);
	}
	//test if exception is thrown on large key
	@Test(expected = InvalidEncryptionKeyException.class)
	public void testKeyTestWithBigKey() throws InvalidEncryptionKeyException {
		injector.injectMembers(this);
		Integer errkey=1223;
		algorithm.testkey(errkey);
	}
	@Test
	public void ComparatorTest(){
		injector.injectMembers(this);
		assertEquals(new AlgorithmComparator<Integer>().compare(new XorEncryptionAlgorithm(), new RepeatEncryption<Integer>(new ShiftUpEncryptionAlgorithm(),3)),0);
	}

}
