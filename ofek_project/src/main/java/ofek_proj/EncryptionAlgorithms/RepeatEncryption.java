package ofek_proj.EncryptionAlgorithms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ofek_proj.exceptions.InvalidEncryptionKeyException;

//Repeat encryption
public class RepeatEncryption<T> implements EncryptionAlgorithm<T> {
	private int numberOfTimes;
	private EncryptionAlgorithm<T> algorithm;
	public RepeatEncryption (EncryptionAlgorithm<T> algorithm,int numberOfTimes){
		this.algorithm=algorithm;
		this.numberOfTimes=numberOfTimes;
	}
	public void setAlgorithm(EncryptionAlgorithm<T> algorithm){
		this.algorithm=algorithm;
	}
	public EncryptionAlgorithm<T> getAlgotithm(){
		return algorithm;
	}
	public int GetNumbetOfTimes(){
		return numberOfTimes;
	}
	public void SetNumbetOfTimes(int numberOfTimes){
		this.numberOfTimes=numberOfTimes;
	}
	//if op=1 performs Encryption NumberOfTimes if op!=1 performs Decryption NumberOfTimes
	protected void DoChainOp(T key,InputStream input,OutputStream output,byte op) throws IOException{
		int i;
		InputStream inputN=input;
		ByteArrayOutputStream outputN=new ByteArrayOutputStream();
		for (i=0;i<numberOfTimes-1;i++){
			if (op==1) algorithm.Encrypt(key,inputN,outputN);
			else  algorithm.Decrypt(key,inputN,outputN);
			inputN = new ByteArrayInputStream(outputN.toByteArray());
			outputN=new ByteArrayOutputStream();
		}
		if (op==1) algorithm.Encrypt(key,inputN,output);
		else  algorithm.Decrypt(key,inputN,output);
		
	}
	public void Encrypt(T key,InputStream input,OutputStream output) throws IOException{
		DoChainOp(key,input,output,(byte) 1);
	}
	public void Decrypt(T key,InputStream input,OutputStream output) throws IOException{
		DoChainOp(key,input,output,(byte) 0);
	}
	public String getAlgorythmName() {
		return "Reapeat Encryption:"+numberOfTimes+" times using "+algorithm.getAlgorythmName();

	}
	public T GenerateKey() {
		return algorithm.GenerateKey();
	}
	public void testkey(T Key) throws InvalidEncryptionKeyException {
		algorithm.testkey(Key);
		
	}
	public String GetStringKey(T key) {
		return algorithm.GetStringKey(key);
	}
	public T GetKeyFromString(String StringKey) {
		return algorithm.GetKeyFromString(StringKey);
	}
	public int getKeyStrength(){
		return algorithm.getKeyStrength();
	}


}
