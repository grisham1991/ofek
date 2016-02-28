package ofek_proj.MethdTester;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FileUtils;

import ofek_proj.EncryptionAlgorithms.EncryptionAlgorithm;
import ofek_proj.EncryptionObserverImplLogger.EncryptionLogger;
import ofek_proj.FileAndDirectoryEncryptors.AsyncDirectoryProcessor;
import ofek_proj.FileAndDirectoryEncryptors.FileEncryptor;
import ofek_proj.FileAndDirectoryEncryptors.SyncDirectoryProcessor;
import ofek_proj.exceptions.InvalidPathException;

//test algorithm running times on big files
public class MethodTester<T> {
	private final String prefix="src\\main\\resources";
	private String testDirectoryPath=prefix+"\\testDir";//test directory path
	private int numberOfTestFiles=10;//number of files to create
	private int testFileSize=1000000;//each file size
	private  boolean doDelete=true;//delete directory after tests or not
	private  boolean FileNotification=false;//notify with file encryptor or not
	private  boolean DirectoryNotification=true;//notify with directory encryptor or not
	private List<EncryptionAlgorithm<T>> AlgorithmList=new ArrayList<EncryptionAlgorithm<T>>();//list of algorithms to test
	
	public void setTestDirectoryPath(String testDirectoryPath){
		this.testDirectoryPath=testDirectoryPath;
	}
	public String setTestDirectoryPath(){
		 return testDirectoryPath;
	}
	public void setAlgorithmList(List<EncryptionAlgorithm<T>> AlgorithmList){
		this.AlgorithmList=AlgorithmList;
	}
	public void addAlgorithm(EncryptionAlgorithm<T> Method){
		AlgorithmList.add(Method);
	}
	public void removeAlgorithm(EncryptionAlgorithm<T> Method){
		AlgorithmList.remove(Method);
	}
	public void setDoDelete(boolean doDelete){
		this.doDelete=doDelete;
	}
	public boolean getDoDelete(){
		return doDelete;
	}
	public void setNumberOfTestFiles(int numberOfTestFiles){
		this.numberOfTestFiles=numberOfTestFiles;
	}
	public int getNumberOfTestFiles(){
		return numberOfTestFiles;
	}
	public void setTestFileSize(int TestFileSize){
		this.testFileSize=TestFileSize;
	}
	public int getTestFileSize(){
		return testFileSize;
	}
	public void setFileNotification(boolean FileNotification){
		this.FileNotification=FileNotification;
	}
	public boolean getFileNotification(){
		return FileNotification;
	}
	public void setDirectoryNotification(boolean DirectoryNotification){
		this.DirectoryNotification=DirectoryNotification;
	}
	public boolean getDirectoryNotification(){
		return DirectoryNotification;
	}
	//test
	public void Test() throws IOException, InvalidPathException, InterruptedException, ExecutionException{
		if (AlgorithmList.size()>0){
			File testDir=new File(testDirectoryPath);
			testDir.mkdir();//create test directory
			FileEncryptor<T> Encryptor=new FileEncryptor<T>(AlgorithmList.get(0));
			EncryptionLogger logger=new EncryptionLogger();
			Encryptor.addObserver(logger);
			Encryptor.setNotification(FileNotification);
			SyncDirectoryProcessor<T> syncDirectoryEncryptor =new SyncDirectoryProcessor<T>(Encryptor);
			AsyncDirectoryProcessor<T> asyncDirectoryEncryptor =new AsyncDirectoryProcessor<T>(Encryptor);
			syncDirectoryEncryptor.addObserver(logger);
			asyncDirectoryEncryptor.addObserver(logger);
			syncDirectoryEncryptor.setNotification(DirectoryNotification);
			asyncDirectoryEncryptor.setNotification(DirectoryNotification);
			byte[] testchars=new byte[testFileSize];
			for (int i=0;i<testFileSize;i++){
				testchars[i]='a';
			}
			for (int i=0;i<numberOfTestFiles;i++){
				FileOutputStream testOutput= new FileOutputStream(testDirectoryPath+"\\test-"+i+".txt");
				testOutput.write(testchars);
				testOutput.close();
			}
			for (int k=0;k<AlgorithmList.size();k++){
				Encryptor.setAlgorythm(AlgorithmList.get(k));
				T key=AlgorithmList.get(k).GenerateKey();
				syncDirectoryEncryptor.EncryptDirectory(testDirectoryPath, testDirectoryPath+"\\encryptedSync+_"+AlgorithmList.get(k).getAlgorythmName(), key);
				asyncDirectoryEncryptor.EncryptDirectory(testDirectoryPath, testDirectoryPath+"\\encryptedAsync_"+AlgorithmList.get(k).getAlgorythmName(), key);
			}
			if (doDelete)
				FileUtils.deleteDirectory(testDir);
		}
	}
	
}
