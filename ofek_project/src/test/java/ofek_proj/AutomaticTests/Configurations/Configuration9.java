package ofek_proj.AutomaticTests.Configurations;

import ofek_proj.EncryptionAlgorithms.RepeatEncryption;
import ofek_proj.EncryptionAlgorithms.ShiftUpEncryptionAlgorithm;

public class Configuration9  extends prepareTest {

	@Override
	protected void configure() {
		prepare(new RepeatEncryption<Integer>(new ShiftUpEncryptionAlgorithm(),3),null,"PlainText.txt","repeat3_shiftup_encrypted1.txt","repeat3_key_shiftup1.txt");
	}

}
