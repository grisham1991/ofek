package ofek_proj.InjectorsAndProviders;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

import ofek_proj.FileAndDirectoryEncryptors.AsyncDirectoryProcessor;
import ofek_proj.FileAndDirectoryEncryptors.DirectoryEncryptor;

public class DirectoryEncryptorProvider implements Provider<DirectoryEncryptor<Integer>>{     
	//use async directory encryptor
	public DirectoryEncryptor<Integer> get() {
		Injector initInjector=Guice.createInjector(new InitInjector()); 
		return initInjector.getInstance(Key.get(new TypeLiteral<AsyncDirectoryProcessor<Integer>>() {}));
	}

}
