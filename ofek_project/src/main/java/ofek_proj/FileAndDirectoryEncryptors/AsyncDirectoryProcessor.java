package ofek_proj.FileAndDirectoryEncryptors;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import ofek_proj.exceptions.InvalidPathException;


public class AsyncDirectoryProcessor<T> extends SyncDirectoryProcessor<T> {
	private ExecutorService executorService;
	private int numOfThreads=10;//number of threads in pool 
	private int threadTimeOutMinutes=60;//amount of time to wait for threads to finish
	@Inject
	public AsyncDirectoryProcessor (@Named("Init") FileEncryptor<T> fileEncryptor){
		super(fileEncryptor);
		executorService=Executors.newFixedThreadPool(numOfThreads);
	}
	
	public void SetNumberOfThreads(int n){
		numOfThreads=n;
	}
	public int getNumberOfThreads(){
		return numOfThreads;
	}
	public void setThreadTimeOutMinutes(int minutes){
		if (minutes>0){
			threadTimeOutMinutes=minutes;
		}
	}
	public int getThreadTimeOutMinutes(){
		return threadTimeOutMinutes;
	}
	public String getMethodName(){
		return "Multithreaded encryption with "+fileEncryptor.GetAlgorythmName();
	}
	
	protected void PerformOp (String inputDirectoryPath, String outputDirectoryPath, T key,int op) throws InvalidPathException, IOException,InterruptedException,ExecutionException{
		executorService=Executors.newFixedThreadPool(numOfThreads);
		super.PerformOp(inputDirectoryPath, outputDirectoryPath, key, op);
		executorService.shutdown();
		executorService.awaitTermination(threadTimeOutMinutes,TimeUnit.MINUTES);
	}
	//submit encryption tasks tasks to thread pool
	protected void PerformEncryption(String inputFilePath,String outputFilePath,T key) throws InterruptedException,ExecutionException{
		Future<Integer> future=executorService.submit(new FileEncryptionTask<T>(fileEncryptor,inputFilePath,outputFilePath,key));
		future.get();
	}
	//submit decryption tasks to thread pool
	protected void PerformDecryption(String inputFilePath,String outputFilePath,T key) throws InterruptedException,ExecutionException{
		Future<Integer> future=executorService.submit(new FileDecryptionTask<T>(fileEncryptor,inputFilePath,outputFilePath,key));
		future.get();
	}


}
