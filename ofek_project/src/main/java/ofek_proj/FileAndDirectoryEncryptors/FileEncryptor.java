package ofek_proj.FileAndDirectoryEncryptors;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import ofek_proj.EncryptionAlgorithms.EncryptionAlgorithm;
import ofek_proj.EncryptionObserverImplLogger.EncryptionLogEventArgs;
import ofek_proj.exceptions.InvalidEncryptionKeyException;
import ofek_proj.exceptions.InvalidPathException;

//file encryptor
public class FileEncryptor<T> {
	private EncryptionAlgorithm<T> algorithm;//the algorithm used for encryption
	private int BlockSize=512;//the maximum block size read from a file in one time
	static Logger FileEncryptorlogger;//logger for debug messages
	private List<EncryptionObserver> ObserverList=new LinkedList<EncryptionObserver>();//list of observers
	private boolean doNotify=true;//if doNotify=false the observers will not be notified
	@Inject
	public FileEncryptor(@Named("Init")EncryptionAlgorithm<T> algorithm){
		this.algorithm=algorithm;
		FileEncryptorlogger=Logger.getLogger(FileEncryptor.class);
	}
	public void addObserver(EncryptionObserver observer){
		ObserverList.add(observer);
		FileEncryptorlogger.debug("observer added.observer num:"+ObserverList.size());
	}
	public boolean removeObserver(EncryptionObserver observer){
		boolean isRemoved=ObserverList.remove(observer);
		if (isRemoved){
			FileEncryptorlogger.debug("observer removed.observer num:"+ObserverList.size());
		}else FileEncryptorlogger.debug("observer not removed because no such observer.observer num:"+ObserverList.size());	
		return isRemoved;
	}
	
	public void setNotification(boolean doNotify){
		this.doNotify=doNotify;
	}
	
	public boolean getNotificationStatus(){
		return doNotify;
	}
	
	protected void notifyEncryptionStarted(String inputFilePath,String outputFilePath){
		for (EncryptionObserver observer:ObserverList){
			observer.updateEncryptionStarted(new EncryptionLogEventArgs(outputFilePath,
					inputFilePath,algorithm.getAlgorythmName(),1,true));
		}
	}
	protected void notifyEncryptionEnded(String inputFilePath,String outputFilePath){
		for (EncryptionObserver observer:ObserverList){
			observer.updateEncryptionEnded(new EncryptionLogEventArgs(outputFilePath,
					inputFilePath,algorithm.getAlgorythmName(),1,true));
		}
	}
	protected void notifyDecryptionStarted(String inputFilePath,String outputFilePath){
		for (EncryptionObserver observer:ObserverList){
			observer.updateDecryptionStarted(new EncryptionLogEventArgs(outputFilePath,
					inputFilePath,algorithm.getAlgorythmName(),0,true));
		}
	}
	public void notifyDecryptionEnded(String inputFilePath,String outputFilePath){
		for (EncryptionObserver observer:ObserverList){
			observer.updateDecryptionEnded(new EncryptionLogEventArgs(outputFilePath,
					inputFilePath,algorithm.getAlgorythmName(),0,true));
		}
	}
	public void SetBlockSize(int BlockSize){
		if (BlockSize>0)
			this.BlockSize=BlockSize;
	}
	public int GetBlockSize(){
		return BlockSize;
	}
	public void setAlgorythm(EncryptionAlgorithm<T> algorithm){
		this.algorithm=algorithm;
	}
	public EncryptionAlgorithm<T> getAlgorithm(){
		return algorithm;
	}
	
	public String GetAlgorythmName(){
		return algorithm.getAlgorythmName();
	}
	
	//open file check for errors and convert to output stream
	protected FileOutputStream openOutputFile(String path) throws InvalidPathException{
		FileOutputStream output;
		try{
		File outputFile = new File(path);
		output = new FileOutputStream(outputFile);
		} catch (Exception e){
			FileEncryptorlogger.debug("thrown InvalidPathException on path:"+path);
			throw new InvalidPathException(path);
		}
		return output;
		
	}
	//open file check for errors and convert to input stream
	protected FileInputStream openInputFile(String path) throws InvalidPathException{
		FileInputStream Input;
		try{
		File InputFile = new File(path);
		Input = new FileInputStream(InputFile);
		} catch (Exception e){
			FileEncryptorlogger.debug("thrown InvalidPathException on path:"+path);
			throw new InvalidPathException(path);
		}
		return Input;
	}
	public T ReadKeyFromFile(String KeyFilePath) throws InvalidPathException, InvalidEncryptionKeyException, IOException{
		FileInputStream keyStream=openInputFile(KeyFilePath);
		//check key file not too long
		if (keyStream.available()>4096){
			FileEncryptorlogger.debug("InvalidEncryptionKeyException thrown on key length check");
			throw new InvalidEncryptionKeyException("key too long");
		}
		//read the key
		String StringKey=new String(Files.readAllBytes(Paths.get(KeyFilePath)));
		T key=null;
		try{
			key=algorithm.GetKeyFromString(StringKey);
		}catch (Exception e){
			FileEncryptorlogger.debug("InvalidEncryptionKeyException thrownon key:"+key);
			throw new InvalidEncryptionKeyException("cant convert key string to key type");
		}
		FileEncryptorlogger.debug("testing key");
		//test key
		algorithm.testkey(key);
		keyStream.close();
		return key;
	}
	//generate a key and write it to file at KeyFilePath
	public T GenerateKeyAndWriteToFile(String KeyFilePath) throws IOException, InvalidPathException{
		FileOutputStream KeyFileStream = openOutputFile(KeyFilePath);
		FileEncryptorlogger.debug("genarating key");
		T key=algorithm.GenerateKey();
		//write key
		KeyFileStream.write(algorithm.GetStringKey(key).getBytes());
		FileEncryptorlogger.debug("key written to:"+KeyFilePath);
		//close stream
		KeyFileStream.close();
		return key;
	}
	//op=1 for encryption op!=1 for decryption
	protected void PerformOp(String inputFilePath,String outputFilePath,T key,int op) throws InvalidPathException, IOException{
		FileInputStream input =openInputFile(inputFilePath);
		FileOutputStream output=openOutputFile(outputFilePath);
		FileEncryptorlogger.debug("reading blocks from file");
		//read block by block and perform op
		byte [] indata=new byte[BlockSize];
		int numread;
		while ((numread=input.read(indata))>0){
			if (op==1) {
				algorithm.Encrypt(key,new ByteArrayInputStream(indata,0,numread),output);
			}
			else{
				algorithm.Decrypt(key,new ByteArrayInputStream(indata,0,numread),output);
			}
		}
		FileEncryptorlogger.debug("closing streams");
		//close all streams
		input.close();
		output.close();
	}
	//open the files and encrypt block by block
	public void EncryptFile(String inputFilePath,String outputFilePath,T key) throws IOException, InvalidPathException{
		FileEncryptorlogger.debug("starting encryption of:"+inputFilePath);
		if (doNotify) notifyEncryptionStarted(inputFilePath,outputFilePath);
		PerformOp(inputFilePath,outputFilePath,key,1);
		if (doNotify) notifyEncryptionEnded(inputFilePath,outputFilePath);
	}
	
	//decrypt the file block by block
	public void DecryptFile(String inputFilePath,String outputFilePath,T key) throws IOException, InvalidPathException{
		FileEncryptorlogger.debug("starting decryption of:"+inputFilePath);
		if (doNotify) notifyDecryptionStarted(inputFilePath,outputFilePath);
		PerformOp(inputFilePath,outputFilePath,key,0);
		if (doNotify) notifyDecryptionEnded(inputFilePath,outputFilePath);
	}
	
	
	
}
