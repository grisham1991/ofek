package ofek_proj.AutomaticTests.Configurations;

import ofek_proj.EncryptionAlgorithms.RepeatEncryption;
import ofek_proj.EncryptionAlgorithms.ShiftUpEncryptionAlgorithm;

public class Configuration10  extends prepareTest {

	@Override
	protected void configure() {
		prepare(new RepeatEncryption<Integer>(new ShiftUpEncryptionAlgorithm(),4),null,"PlainText.txt","repeat4_shiftup_encrypted2.txt","repeat4_key_shiftup2.txt");
	}

}
