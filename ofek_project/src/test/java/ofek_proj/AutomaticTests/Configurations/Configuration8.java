package ofek_proj.AutomaticTests.Configurations;

import ofek_proj.EncryptionAlgorithms.DoubleEncryption;
import ofek_proj.EncryptionAlgorithms.ShiftUpEncryptionAlgorithm;

public class Configuration8  extends prepareTest {

	@Override
	protected void configure() {
		prepare(null,new DoubleEncryption<Integer>(new ShiftUpEncryptionAlgorithm()),"PlainText.txt","double_shiftup_encrypted2.txt","double_key_shiftup2.txt");
	}

}
