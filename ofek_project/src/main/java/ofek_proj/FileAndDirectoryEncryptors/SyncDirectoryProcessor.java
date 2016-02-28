package ofek_proj.FileAndDirectoryEncryptors;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import ofek_proj.EncryptionObserverImplLogger.EncryptionLogEventArgs;
import ofek_proj.exceptions.InvalidEncryptionKeyException;
import ofek_proj.exceptions.InvalidPathException;

public class SyncDirectoryProcessor<T> implements DirectoryEncryptor<T> {
	
	protected FileEncryptor<T> fileEncryptor;//the file encryptor used
	static Logger DirectoryEncryptorlogger;//logger for debug messages
	private List<EncryptionObserver> ObserverList=new LinkedList<EncryptionObserver>();//list of observers
	private boolean doNotify=true;//if do notify is false observers will not be notified
	@Inject
	public SyncDirectoryProcessor(@Named("Init") FileEncryptor<T> fileEncryptor){
		this.fileEncryptor=fileEncryptor;
		DirectoryEncryptorlogger=Logger.getLogger(DirectoryEncryptor.class);
	}
	public FileEncryptor<T> getFileEncryptor(){
		return fileEncryptor;
	}
	public void setFileEncryptor(FileEncryptor<T> FileEncryptor){
		this.fileEncryptor=FileEncryptor;
	}
	public String getMethodName(){
		return "single threaded encryption with "+fileEncryptor.GetAlgorythmName();
	}
	public void addObserver(EncryptionObserver observer){
		ObserverList.add(observer);
		DirectoryEncryptorlogger.debug("observer added.observer num:"+ObserverList.size());
	}
	public boolean removeObserver(EncryptionObserver observer){
		boolean isRemoved=ObserverList.remove(observer);
		if (isRemoved){
			DirectoryEncryptorlogger.debug("observer removed.observer num:"+ObserverList.size());
		}else DirectoryEncryptorlogger.debug("observer not removed because no such observer.observer num:"+ObserverList.size());	
		return isRemoved;
	}
	public void setNotification(boolean doNotify){
		this.doNotify=doNotify;
	}
	
	public boolean getNotificationStatus(){
		return doNotify;
	}
	private void notifyEncryptionStarted(String inputDirectoryPath,String outputDirectoryPath){
		for (EncryptionObserver observer:ObserverList){
			observer.updateEncryptionStarted(new EncryptionLogEventArgs(outputDirectoryPath,
					inputDirectoryPath,getMethodName(),1,false));
		}
	}
	private void notifyEncryptionEnded(String inputDirectoryPath,String outputDirectoryPath){
		for (EncryptionObserver observer:ObserverList){
			observer.updateEncryptionEnded(new EncryptionLogEventArgs(outputDirectoryPath,
					inputDirectoryPath,getMethodName(),1,false));
		}
	}
	private void notifyDecryptionStarted(String inputDirectoryPath,String outputDirectoryPath){
		for (EncryptionObserver observer:ObserverList){
			observer.updateDecryptionStarted(new EncryptionLogEventArgs(outputDirectoryPath,
					inputDirectoryPath,getMethodName(),0,false));
		}
	}
	public void notifyDecryptionEnded(String inputDirectoryPath,String outputDirectoryPath){
		for (EncryptionObserver observer:ObserverList){
			observer.updateDecryptionEnded(new EncryptionLogEventArgs(outputDirectoryPath,
					inputDirectoryPath,getMethodName(),0,false));
		}
	}
	//if op=1 encrypt directory if op!=1 decrypt directory
	protected void PerformOp (String inputDirectoryPath, String outputDirectoryPath, T key,int op) throws InvalidPathException, IOException,InterruptedException,ExecutionException{
		DirectoryEncryptorlogger.debug("Starting directory op with input directory at:"+inputDirectoryPath);
		File inputDir = new File(inputDirectoryPath);
		File outputDir = new File(outputDirectoryPath);
		outputDir.mkdir();
		if (!inputDir.exists()){
			DirectoryEncryptorlogger.debug("thrown InvalidDirectoryException for directory at:"+inputDirectoryPath+" because it doesnt exist");
			throw new InvalidPathException(inputDirectoryPath);
		}
		if (!inputDir.isDirectory()){
			DirectoryEncryptorlogger.debug("thrown InvalidDirectoryException for directory at:"+inputDirectoryPath+" because it is not a directory");
			throw new InvalidPathException(inputDirectoryPath);
		}
		DirectoryEncryptorlogger.debug("listing files in directory:"+inputDirectoryPath);
		File[] filesList = inputDir.listFiles();
	    for(File f : filesList){
	    	if(f.isFile() &&  f.getName().endsWith(".txt")){
	    		if (op==1){
	    			PerformEncryption(f.getPath(), outputDirectoryPath+File.separator+f.getName(), key);
	    		}else{
	    			PerformDecryption(f.getPath(), outputDirectoryPath+File.separator+f.getName(), key);
	    		}
	         }
	     }
		 
	}
	
	protected void PerformEncryption(String inputFilePath,String outputFilePath,T key) throws IOException, InvalidPathException,InterruptedException,ExecutionException{
		fileEncryptor.EncryptFile(inputFilePath, outputFilePath, key);
	}
	
	protected void PerformDecryption(String inputFilePath,String outputFilePath,T key) throws IOException, InvalidPathException,InterruptedException,ExecutionException{
		fileEncryptor.DecryptFile(inputFilePath, outputFilePath, key);
	}
	
	public void EncryptDirectory(String inputDirectoryPath, String outputDirectoryPath, T key)
			throws IOException, InvalidPathException,InterruptedException,ExecutionException {
		if (doNotify) notifyEncryptionStarted(inputDirectoryPath,outputDirectoryPath);
		PerformOp(inputDirectoryPath,outputDirectoryPath,key,1);
		if (doNotify) notifyEncryptionEnded(inputDirectoryPath,outputDirectoryPath);
	}

	public void DecryptDirectory(String inputDirectoryPath, String outputDirectoryPath, T key)
			throws IOException, InvalidPathException,InterruptedException,ExecutionException {
		if (doNotify) notifyDecryptionStarted(inputDirectoryPath,outputDirectoryPath);
		PerformOp(inputDirectoryPath,outputDirectoryPath,key,0);
		if (doNotify) notifyDecryptionEnded(inputDirectoryPath,outputDirectoryPath);
	
	}

	public T GenerateKeyAndWriteToFile(String KeyFilePath) throws IOException, InvalidPathException {
		return fileEncryptor.GenerateKeyAndWriteToFile(KeyFilePath);
	}

	public T ReadKeyFromFile(String KeyFilePath) throws InvalidPathException, InvalidEncryptionKeyException, IOException {
		return fileEncryptor.ReadKeyFromFile(KeyFilePath);
	}

}
