package ofek_proj.AutomaticTests.Configurations;

import ofek_proj.EncryptionAlgorithms.DoubleEncryption;
import ofek_proj.EncryptionAlgorithms.ShiftUpEncryptionAlgorithm;

public class Configuration7  extends prepareTest {

	@Override
	protected void configure() {
		prepare(null,new DoubleEncryption<Integer>(new ShiftUpEncryptionAlgorithm()),"PlainText.txt","double_shiftup_encrypted1.txt","double_key_shiftup1.txt");
	}

}
