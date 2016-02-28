package ofek_proj.AutomaticTests.Configurations;

import ofek_proj.EncryptionAlgorithms.ShiftUpEncryptionAlgorithm;

public class Configuration4  extends prepareTest {

	@Override
	protected void configure() {
		prepare(new ShiftUpEncryptionAlgorithm(),null,"PlainText.txt","shiftup_encrypted2.txt","shiftup_key2.txt");
	}

}
