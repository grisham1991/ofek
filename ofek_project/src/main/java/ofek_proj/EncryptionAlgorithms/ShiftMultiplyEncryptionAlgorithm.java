package ofek_proj.EncryptionAlgorithms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import ofek_proj.exceptions.InvalidEncryptionKeyException;

public class ShiftMultiplyEncryptionAlgorithm extends ShiftUpEncryptionAlgorithm {
	
	public String getAlgorythmName(){
		return "Shift Multiply Algorithm";
	}
	
	protected byte PerformEncOp(byte bytekey,byte inputbyte){
		return (byte) (inputbyte*bytekey);
	}
	
	protected byte PerformDecOp(byte bytekey,byte inputbyte){
		return (byte) (inputbyte*bytekey);
	}
	public Integer GenerateKey(){
		Integer key=super.GenerateKey();
		if (key%2==0) key+=1; //we only allow odd numbers only they have modular inverse
		return key;
	}
	public void Decrypt(Integer key, InputStream input, OutputStream output) throws IOException {
		BigInteger bi2=new BigInteger("256");
		BigInteger bi=new BigInteger(key.toString());
		super.Decrypt(bi.modInverse(bi2).intValue(), input, output);
	}
	
	public void testkey(Integer Key) throws InvalidEncryptionKeyException{
		super.testkey(Key);
		BigInteger bi2=new BigInteger("256");
		BigInteger bi=new BigInteger(Key.toString());
		try{//test if key has modular inverse
			bi.modInverse(bi2);
		}catch (Exception e){
			throw new InvalidEncryptionKeyException("key has no modular inverse");
		}
	}

}
