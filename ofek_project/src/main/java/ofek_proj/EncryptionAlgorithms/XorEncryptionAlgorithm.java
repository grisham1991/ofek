package ofek_proj.EncryptionAlgorithms;

public class XorEncryptionAlgorithm extends ShiftUpEncryptionAlgorithm {
	public String getAlgorythmName(){
		return "One time pad";
	}
	
	protected byte PerformEncOp(byte bytekey,byte inputbyte){
		return (byte) (inputbyte^bytekey);
	}
	
	protected byte PerformDecOp(byte bytekey,byte inputbyte){
		return (byte) (inputbyte^bytekey);
	}
}
