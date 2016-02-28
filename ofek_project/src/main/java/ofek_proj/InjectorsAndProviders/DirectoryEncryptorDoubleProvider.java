package ofek_proj.InjectorsAndProviders;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

import ofek_proj.FileAndDirectoryEncryptors.AsyncDirectoryProcessor;
import ofek_proj.FileAndDirectoryEncryptors.DirectoryEncryptor;
import ofek_proj.Utils.DoubleKey;

public class DirectoryEncryptorDoubleProvider implements Provider<DirectoryEncryptor<DoubleKey<Integer>>> {
	//use async directory encryptor
	public DirectoryEncryptor<DoubleKey<Integer>> get() {
		Injector initInjector=Guice.createInjector(new InitInjector()); 
		return initInjector.getInstance(Key.get(new TypeLiteral<AsyncDirectoryProcessor<DoubleKey<Integer>>>() {}));
	}

}
