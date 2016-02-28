package ofek_proj.AutomaticTests;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import ofek_proj.EncryptionAlgorithms.DoubleEncryption;
import ofek_proj.EncryptionAlgorithms.EncryptionAlgorithm;
import ofek_proj.EncryptionAlgorithms.ShiftUpEncryptionAlgorithm;
import ofek_proj.Utils.DoubleKey;

public class AlgorithmInjector extends AbstractModule {
	@Override
	protected void configure() {
		 bind(new TypeLiteral<EncryptionAlgorithm<Integer>>() {}).annotatedWith(Names.named("Init")).to((Class<? extends EncryptionAlgorithm<Integer>>) ShiftUpEncryptionAlgorithm.class);
		 bind(new TypeLiteral<EncryptionAlgorithm<DoubleKey<Integer>>>() {}).annotatedWith(Names.named("Init")).to(Key.get(new TypeLiteral<DoubleEncryption<Integer>>() {}));
	}

}
