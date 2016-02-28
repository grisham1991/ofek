package ofek_proj.AutomaticTests.Configurations;

import ofek_proj.EncryptionAlgorithms.XorEncryptionAlgorithm;

public class Configuration5  extends prepareTest {

	@Override
	protected void configure() {
		prepare(new XorEncryptionAlgorithm(),null,"PlainText.txt","xor_encrypted1.txt","xor_key1.txt");
	}

}
