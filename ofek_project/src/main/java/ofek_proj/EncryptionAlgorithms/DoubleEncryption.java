package ofek_proj.EncryptionAlgorithms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import ofek_proj.Utils.DoubleKey;
import ofek_proj.Utils.utilArgs;
import ofek_proj.exceptions.InvalidEncryptionKeyException;

//double encryption
public class DoubleEncryption<T> implements EncryptionAlgorithm<DoubleKey<T>> {
	
	private EncryptionAlgorithm<T> algorithm;
	
	@Inject
	public DoubleEncryption (@Named("Init") EncryptionAlgorithm<T> algorithm){
		this.algorithm=algorithm;
	}
	public void setAlgorithm(EncryptionAlgorithm<T> algorithm){
		this.algorithm=algorithm;
	}
	public EncryptionAlgorithm<T> getAlgotithm(){
		return algorithm;
	}
	public DoubleKey<T> GenerateKey(){
		return new DoubleKey<T>(algorithm.GenerateKey(),algorithm.GenerateKey());
	}
	public void testkey(DoubleKey<T> key) throws InvalidEncryptionKeyException{
		//test both keys
		algorithm.testkey(key.GetFirstKey());
		algorithm.testkey(key.GetSecondKey());
	}
	public void Encrypt(DoubleKey<T> key,InputStream input,OutputStream output) throws IOException{
		ByteArrayOutputStream output1=new ByteArrayOutputStream();
		algorithm.Encrypt(key.GetFirstKey(),input,output1);
		InputStream input2 = new ByteArrayInputStream(output1.toByteArray());
		algorithm.Encrypt(key.GetSecondKey(),input2,output);
	}
	
	public void Decrypt(DoubleKey<T> key,InputStream input,OutputStream output) throws IOException{
		ByteArrayOutputStream output1=new ByteArrayOutputStream();
		algorithm.Decrypt(key.GetFirstKey(),input,output1);
		InputStream input2 = new ByteArrayInputStream(output1.toByteArray());
		algorithm.Decrypt(key.GetSecondKey(),input2,output);
	}
	
	public String getAlgorythmName() {
		return utilArgs.DoublePrefix+algorithm.getAlgorythmName();
	}

	public String GetStringKey(DoubleKey<T> key) {
		return "key1:"+algorithm.GetStringKey(key.GetFirstKey())+" key2:"+ algorithm.GetStringKey(key.GetSecondKey());
	}

	public DoubleKey<T> GetKeyFromString(String StringKey) {
		int pos2=StringKey.indexOf(" key2:");
		String key1=StringKey.substring(5, pos2);
		String key2=StringKey.substring(pos2+6);
		return new DoubleKey<T>(algorithm.GetKeyFromString(key1),algorithm.GetKeyFromString(key2));
	}
	
	public int getKeyStrength(){
		return 2*algorithm.getKeyStrength();
	}

	

}
