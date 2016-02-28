package ofek_proj.FileAndDirectoryEncryptors;

import java.util.concurrent.Callable;
//file encryption task submitted to thread pool
public class FileEncryptionTask<T> implements Callable<Integer> {
	FileEncryptor<T> Encryptor;
	String inputFilePath;
	String outputFilePath;
	T key;
	FileEncryptionTask(FileEncryptor<T> Encryptor,String inputFilePath,String outputFilePath,T key){
		this.Encryptor=Encryptor;
		this.inputFilePath=inputFilePath;
		this.outputFilePath=outputFilePath;
		this.key=key;
	}
	 public Integer call() throws Exception {
		Encryptor.EncryptFile(inputFilePath, outputFilePath, key);
		return 0;
	}

}
