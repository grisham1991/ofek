package ofek_proj.AutomaticTests.Configurations;

import ofek_proj.EncryptionAlgorithms.ShiftMultiplyEncryptionAlgorithm;

public class Configuration1 extends prepareTest {

	@Override
	protected void configure() {
		prepare(new ShiftMultiplyEncryptionAlgorithm(),null,"PlainText.txt","multiply_encrypted1.txt","multiply_key1.txt");
	}

}
