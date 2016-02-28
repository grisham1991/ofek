package ofek_proj.AutomaticTests.Configurations;

import ofek_proj.EncryptionAlgorithms.ShiftMultiplyEncryptionAlgorithm;

public class Configuration2  extends prepareTest {

	@Override
	protected void configure() {
		prepare(new ShiftMultiplyEncryptionAlgorithm(),null,"PlainText.txt","multiply_encrypted2.txt","multiply_key2.txt");
	}

}
