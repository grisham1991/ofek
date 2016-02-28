package ofek_proj.InjectorsAndProviders;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import ofek_proj.EncryptionAlgorithms.ShiftUpEncryptionAlgorithm;
import ofek_proj.FileAndDirectoryEncryptors.DirectoryEncryptor;
import ofek_proj.Utils.DoubleKey;
import ofek_proj.Utils.utilArgs;
import ofek_proj.XmlParsers.XmlParser;
//inject members
public class ImplInjector  extends AbstractModule{

	@Override
	protected void configure() {
		bind(XmlParser.class).toProvider(XmlImplProvider.class);
		bind(new TypeLiteral<DirectoryEncryptor<Integer>>() {}).toProvider(DirectoryEncryptorProvider.class);
		bind(new TypeLiteral<DirectoryEncryptor<DoubleKey<Integer>>>() {}).toProvider(DirectoryEncryptorDoubleProvider.class);
		bind(String.class).annotatedWith(Names.named("UnaryAlgorithmName")).toProvider(new Provider<String>(){
			public String get(){
				return new ShiftUpEncryptionAlgorithm().getAlgorythmName();
			}
		});
		bind(String.class).annotatedWith(Names.named("DoublePrefix")).toProvider(new Provider<String>(){
			public String get(){
				return utilArgs.DoublePrefix;
			}
		});

	}

}
