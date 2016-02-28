package ofek_proj.FileAndDirectoryEncryptors;

import java.util.concurrent.Callable;

//file decryption task submitted to thread pool
public class FileDecryptionTask<T> implements Callable<Integer> {
	FileEncryptor<T> Encryptor;
	String inputFilePath;
	String outputFilePath;
	T key;
	FileDecryptionTask(FileEncryptor<T> Encryptor,String inputFilePath,String outputFilePath,T key){
		this.Encryptor=Encryptor;
		this.inputFilePath=inputFilePath;
		this.outputFilePath=outputFilePath;
		this.key=key;
	}
	 public Integer call() throws Exception {
		Encryptor.DecryptFile(inputFilePath, outputFilePath, key);
		return 0;
	}
}
