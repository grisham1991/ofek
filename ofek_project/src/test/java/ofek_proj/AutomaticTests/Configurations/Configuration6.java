package ofek_proj.AutomaticTests.Configurations;

import ofek_proj.EncryptionAlgorithms.XorEncryptionAlgorithm;

public class Configuration6  extends prepareTest {

	@Override
	protected void configure() {
		prepare(new XorEncryptionAlgorithm(),null,"PlainText.txt","xor_encrypted2.txt","xor_key2.txt");
	}

}
