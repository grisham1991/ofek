package ofek_proj.AutomaticTests.Configurations;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import ofek_proj.EncryptionAlgorithms.EncryptionAlgorithm;
import ofek_proj.Utils.DoubleKey;

public abstract class prepareTest extends AbstractModule {
	//bind elements
	void prepare(EncryptionAlgorithm<Integer> Algorithm1,EncryptionAlgorithm<DoubleKey<Integer>> Algorithm2,String PlainTextFile,String ExpectedCipherTextFile,String keyFile){
		try{
			byte [] PlainText;
			byte [] ExpectedCipherText;
			Integer key1 = null;
			DoubleKey<Integer> key2 = null;
			String PlainTextPath="src/test/resources/"+PlainTextFile;
			String ExpectedCipherTextPath="src/test/resources/"+ExpectedCipherTextFile;
			String keyPath="src/test/resources/"+keyFile;
			String Stringkey=new String(Files.readAllBytes(Paths.get(keyPath)));
			if (Algorithm1!=null)
				key1=Algorithm1.GetKeyFromString(Stringkey);
			if (Algorithm2!=null)
				key2=Algorithm2.GetKeyFromString(Stringkey);
			PlainText=Files.readAllBytes(Paths.get(PlainTextPath));
			ExpectedCipherText=Files.readAllBytes(Paths.get( ExpectedCipherTextPath));
			if (Algorithm1!=null)
				bind(new TypeLiteral<EncryptionAlgorithm<Integer>>() {}).toInstance(Algorithm1);
			if (Algorithm2!=null)
				bind(new TypeLiteral<EncryptionAlgorithm<DoubleKey<Integer>>>() {}).toInstance(Algorithm2);
			//else bind(new TypeLiteral<EncryptionAlgorythm<DoubleKey<Integer>>>() {}).toInstance(null);
			bind(String.class).toInstance(Stringkey);
			if (key2!=null)
				bind(new TypeLiteral<DoubleKey<Integer>>() {}).toInstance(key2);
			if (key1!=null)
				bind(new TypeLiteral<Integer>() {}).toInstance(key1);
			bind(byte [].class).annotatedWith(Names.named("PlainText")).toInstance(PlainText);
			bind(byte [].class).annotatedWith(Names.named("ExpectedCipherText")).toInstance(ExpectedCipherText);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
}
