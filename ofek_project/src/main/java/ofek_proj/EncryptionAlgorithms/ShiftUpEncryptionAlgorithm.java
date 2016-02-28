package ofek_proj.EncryptionAlgorithms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.reflections.Reflections;

import ofek_proj.Utils.utilArgs;
import ofek_proj.exceptions.InvalidEncryptionKeyException;

public class ShiftUpEncryptionAlgorithm implements EncryptionAlgorithm<Integer> {
	private int seed; //random seed to generate key drawn only once
	private int hashCode; //hash code of algorithm implementation names
	@SuppressWarnings("rawtypes")
	public ShiftUpEncryptionAlgorithm(){
		seed=new Random().nextInt(256);
		List<Integer> hashList=new ArrayList<Integer>();
		Reflections reflections=new Reflections("ex1");
		Set<Class<? extends EncryptionAlgorithm>> classes = reflections.getSubTypesOf(EncryptionAlgorithm.class);
		for (Class<? extends EncryptionAlgorithm> c:classes){
			hashList.add(c.getName().hashCode());
		}
		hashCode=utilArgs.getHashCode(hashList);
	}
	
	//perform byte operation for encryption
	protected byte PerformEncOp(byte bytekey,byte inputbyte){
		return (byte) (inputbyte+bytekey);
	}
	//perform byte operation for Decryption 
	protected byte PerformDecOp(byte bytekey,byte inputbyte){
		return (byte) (inputbyte-bytekey);
	}
	public int getKeyStrength(){
		return 3;
	}
	
	//read the streams and perform encryption/decryption if op=1 than its encryption else its decryption
	protected void DoOPerations(Integer key, InputStream input, OutputStream output,byte op) throws IOException {
		
		int numread,i;
		byte bytekey=(byte)key.intValue();
		byte [] inData=new byte[input.available()];
		numread=input.read(inData);
		for (i=0;i<numread;i++){
			if (op==1)
			inData[i]=PerformEncOp(bytekey,inData[i]);//encrypt the byte
			else inData[i]=PerformDecOp(bytekey,inData[i]); //decrypt the byte

		}
		output.write(inData, 0, numread);
	}
	
	public void Encrypt(Integer key, InputStream input, OutputStream output) throws IOException{
		DoOPerations(key,input,output,(byte)1);
	}
	
	public void Decrypt(Integer key, InputStream input, OutputStream output) throws IOException {
		DoOPerations(key,input,output,(byte)0);
	}
	
	public String getAlgorythmName(){
		return "Shift Up Algorithm";
	}
	
	public void testkey(Integer Key) throws InvalidEncryptionKeyException{
		if (Key>256 || Key<0){//check key is in range
			throw new InvalidEncryptionKeyException("key number is not in range 0-256");
		}
		
	}
	public Integer GenerateKey(){
		List<Integer> hashList=new ArrayList<Integer>();
		seed+=utilArgs.smear(seed);
		hashList.add(seed);
		hashList.add(hashCode);
		return Math.abs(utilArgs.smear(utilArgs.getHashCode(hashList)))%256;
	}	
	public String GetStringKey(Integer key) {
		return String.valueOf(key);
	}
	public Integer GetKeyFromString(String StringKey) {
		
		return Integer.valueOf(StringKey);
	}

}
