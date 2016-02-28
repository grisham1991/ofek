package ofek_proj.AutomaticTests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import ofek_proj.AutomaticTests.Configurations.Configuration1;
import ofek_proj.AutomaticTests.Configurations.Configuration10;
import ofek_proj.AutomaticTests.Configurations.Configuration2;
import ofek_proj.AutomaticTests.Configurations.Configuration3;
import ofek_proj.AutomaticTests.Configurations.Configuration4;
import ofek_proj.AutomaticTests.Configurations.Configuration5;
import ofek_proj.AutomaticTests.Configurations.Configuration6;
import ofek_proj.AutomaticTests.Configurations.Configuration7;
import ofek_proj.AutomaticTests.Configurations.Configuration8;
import ofek_proj.AutomaticTests.Configurations.Configuration9;
import ofek_proj.EncryptionAlgorithms.EncryptionAlgorithm;
import ofek_proj.Utils.DoubleKey;

public class EncryptionDecryptionTester {
	@Inject(optional=true)
	EncryptionAlgorithm<Integer> Algorithm1=null;
	@Inject(optional=true)
	EncryptionAlgorithm<DoubleKey<Integer>> Algorithm2=null;
	@Inject
	@Named("PlainText")
	byte [] PlainText;
	@Inject
	@Named("ExpectedCipherText")
	byte [] ExpectedCipherText;
	@Inject
	String Stringkey;
	@Inject(optional=true)
	Integer key1=null;
	@Inject (optional=true)
	DoubleKey<Integer> key2=null;
	
	
	@Test
	public void TestConfiguration1() throws IOException{
		Guice.createInjector(new Configuration1()).injectMembers(this);
		testEncryption();
		testDecryption();
	}
	@Test
	public void TestConfiguration2() throws IOException{
		Guice.createInjector(new Configuration2()).injectMembers(this);
		testEncryption();
		testDecryption();
	}
	@Test
	public void TestConfiguration3() throws IOException{
		Guice.createInjector(new Configuration3()).injectMembers(this);
		testEncryption();
		testDecryption();
	}
	@Test
	public void TestConfiguration4() throws IOException{
		Guice.createInjector(new Configuration4()).injectMembers(this);
		testEncryption();
		testDecryption();
	}
	@Test
	public void TestConfiguration5() throws IOException{
		Guice.createInjector(new Configuration5()).injectMembers(this);
		testEncryption();
		testDecryption();
	}
	@Test
	public void TestConfiguration6() throws IOException{
		Guice.createInjector(new Configuration6()).injectMembers(this);
		testEncryption();
		testDecryption();
	}
	@Test
	public void TestConfiguration7() throws IOException{
		Guice.createInjector(new Configuration7()).injectMembers(this);
		testEncryption();
		testDecryption();
	}
	@Test
	public void TestConfiguration8() throws IOException{
		Guice.createInjector(new Configuration8()).injectMembers(this);
		testEncryption();
		testDecryption();
	}
	@Test
	public void TestConfiguration9() throws IOException{
		Guice.createInjector(new Configuration9()).injectMembers(this);
		testEncryption();
		testDecryption();
	}
	@Test
	public void TestConfiguration10() throws IOException{
		Guice.createInjector(new Configuration10()).injectMembers(this);
		testEncryption();
		testDecryption();
	}
	public void testEncryption() throws IOException {
		ByteArrayOutputStream output=new ByteArrayOutputStream();
		ByteArrayInputStream input=new ByteArrayInputStream(PlainText);
		if (Algorithm1!=null)
			Algorithm1.Encrypt(key1, input, output);
		if (Algorithm2!=null)
			Algorithm2.Encrypt(key2, input, output);
		byte [] EncryptedBytes=output.toByteArray();
		for (int i=0;i<PlainText.length;i++){
			if (EncryptedBytes[i]!=ExpectedCipherText[i])
				fail("Encryption not equal on index "+i);
		}	
	}
	
	public void testDecryption() throws IOException{
		ByteArrayInputStream input=new ByteArrayInputStream(ExpectedCipherText);
		ByteArrayOutputStream output=new ByteArrayOutputStream();
		if (Algorithm1!=null)
			Algorithm1.Decrypt(key1, input, output);
		if (Algorithm2!=null)
			Algorithm2.Decrypt(key2, input, output);
		byte [] DecryptedBytes=output.toByteArray();
		for (int i=0;i<PlainText.length;i++){
			if (DecryptedBytes[i]!=PlainText[i])
				fail("Decryption not equal on index "+i);
		}
		
	}
	
	

}
