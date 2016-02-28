package ofek_proj.AutomaticTests.Configurations;

import ofek_proj.EncryptionAlgorithms.ShiftUpEncryptionAlgorithm;

public class Configuration3  extends prepareTest {

	@Override
	protected void configure() {
		prepare(new ShiftUpEncryptionAlgorithm(),null,"PlainText.txt","shiftup_encrypted1.txt","shiftup_key1.txt");
	}

}
