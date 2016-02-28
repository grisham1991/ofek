package ofek_proj.EncryptionAlgorithms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ofek_proj.exceptions.InvalidEncryptionKeyException;

public interface EncryptionAlgorithm<T>{
	//the name of the encryption algorithm
	public String getAlgorythmName();
	//return the strength of the key
	public int getKeyStrength();
	//generate key in correct format
	public T GenerateKey();
	//convert key to string
	public String GetStringKey(T key);
	//convert Key from String to T
	public T GetKeyFromString(String StringKey);
	//validate key is of correct format
	public void testkey(T Key) throws InvalidEncryptionKeyException;
	//encrypt data in input stream to  to output stream
	public void Encrypt(T key,InputStream input,OutputStream output) throws IOException;
	//encrypt data in input stream to  to output stream 
	public void Decrypt(T key,InputStream input,OutputStream output) throws IOException;
}
