package ofek_proj.FileAndDirectoryEncryptors;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import ofek_proj.exceptions.InvalidEncryptionKeyException;
import ofek_proj.exceptions.InvalidPathException;

public interface DirectoryEncryptor<T> {
	//returns the name of the encryption method used
	public String getMethodName();
	//add observer
	public void addObserver(EncryptionObserver observer);
	//remove observer
	public boolean removeObserver(EncryptionObserver observer);
	//encrypt directory using key
	public void EncryptDirectory(String inputDirectoryPath,String outputDirectoryPath,T key) throws IOException, InvalidPathException,InterruptedException,ExecutionException;
	//decrypt directory using key
	public void DecryptDirectory(String inputDirectoryPath,String outputDirectoryPath,T key) throws IOException, InvalidPathException,InterruptedException,ExecutionException;
	//generate a key and write it to file at KeyFilePath
	public T GenerateKeyAndWriteToFile(String KeyFilePath) throws IOException, InvalidPathException;
	//read key from file at KeyFilePath
	public T ReadKeyFromFile(String KeyFilePath) throws InvalidPathException, InvalidEncryptionKeyException, IOException;
	//getter for the file encryptor
	public FileEncryptor<T> getFileEncryptor();
	//setter for the file encryptor
	public void setFileEncryptor(FileEncryptor<T> FileEncryptor);
}
