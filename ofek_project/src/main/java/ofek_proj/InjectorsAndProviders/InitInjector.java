package ofek_proj.InjectorsAndProviders;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import ofek_proj.EncryptionAlgorithms.DoubleEncryption;
import ofek_proj.EncryptionAlgorithms.EncryptionAlgorithm;
import ofek_proj.EncryptionAlgorithms.ShiftUpEncryptionAlgorithm;
import ofek_proj.FileAndDirectoryEncryptors.FileEncryptor;
import ofek_proj.Utils.DoubleKey;


public class InitInjector extends AbstractModule {
	
	
	@Override
	protected void configure() {//choose default algorithm and file encyptor
		 bind(new TypeLiteral<EncryptionAlgorithm<Integer>>() {}).annotatedWith(Names.named("Init")).to((Class<? extends EncryptionAlgorithm<Integer>>) ShiftUpEncryptionAlgorithm.class);
		 bind(new TypeLiteral<EncryptionAlgorithm<DoubleKey<Integer>>>() {}).annotatedWith(Names.named("Init")).to(Key.get(new TypeLiteral<DoubleEncryption<Integer>>() {}));
	
		 bind(new TypeLiteral<FileEncryptor<Integer>>() {}).annotatedWith(Names.named("Init")).to(Key.get(new TypeLiteral<FileEncryptor<Integer>>() {}));
		 bind(new TypeLiteral<FileEncryptor<DoubleKey<Integer>>>() {}).annotatedWith(Names.named("Init")).to(Key.get(new TypeLiteral<FileEncryptor<DoubleKey<Integer>>>() {}));
	}

}
