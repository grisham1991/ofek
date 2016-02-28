package ofek_proj.EncryptionObserverImplLogger;

import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import ofek_proj.FileAndDirectoryEncryptors.EncryptionObserver;

//encryption logger observer
public class EncryptionLogger implements EncryptionObserver {
	static Logger logger;
	private List<EncryptionLogEventArgs> EventList;
	private final ReentrantReadWriteLock rwl;
	
	public EncryptionLogger(){//insert op start to log
		EventList=new LinkedList<EncryptionLogEventArgs>();
		rwl = new ReentrantReadWriteLock();
		logger = Logger.getLogger(EncryptionLogger.class);
	}
	
	
	private void log(EncryptionLogEventArgs args){
		logger.debug("entered op end(log) with input "+args.getInputPath());
		int pos;
		long durration;
		String inputName=Paths.get(args.getInputPath()).getFileName().toString();
		String outputName=Paths.get(args.getOutputPath()).getFileName().toString();
		String algorithmName=args.getAlgorithmName();
		String op;
		String op2;
		String element;
		if (args.getIsFile()){
			element="file";
		}else{
			element="directory";
		}
		if (args.getOp()==1){ 
			op="encryption";
			op2="encrypted";
		}
		else{
			op="decryption";
			op2="decrypted";
		}
		EncryptionLogEventArgs EncryptionStartArgs;
		rwl.readLock().lock();
		logger.debug("read lock locked with input:"+inputName);
		pos=EventList.lastIndexOf(args);//find the start of the operation
		
		if (pos==-1){
			rwl.readLock().unlock();
			logger.debug("read lock unlocked with input:"+inputName);
			logger.error("cant find "+op+" strat");
		}else{
			EncryptionStartArgs=EventList.get(pos);
			rwl.readLock().unlock();
			logger.debug("read lock unlocked with input:"+inputName);
			rwl.writeLock().lock();
			logger.debug("write lock locked with input:"+inputName);
			EventList.remove(EncryptionStartArgs);
			rwl.writeLock().unlock();
			logger.debug("write lock unlocked with input file:"+inputName);
			durration=args.getMiliSeconds()-EncryptionStartArgs.getMiliSeconds();
			logger.info("the "+op+" for "+element+" "+inputName+" with method "+algorithmName+
					" took "+durration+" miliseconds.the "+op2+" "+element+" is located in "+element+" "+outputName);
		}
	}
	private void recordOpStart(EncryptionLogEventArgs args){
		logger.debug("entered op record with input path:"+args.getInputPath());
		rwl.writeLock().lock();
		logger.debug("write lock locked  with input path:"+args.getInputPath());
		EventList.add(args);
		rwl.writeLock().unlock();
		logger.debug("write lock unlocked with input path:"+args.getInputPath());
	}
	public void updateEncryptionStarted(EncryptionLogEventArgs args) {
		if (args.getOp()!=1) logger.error("operation will not be recorded because"
				+ " op code in encryption should be 1 but its "+args.getOp());
		else
			recordOpStart(args);
	}
	public void updateEncryptionEnded(EncryptionLogEventArgs args) {
		if (args.getOp()!=1) logger.error("operation will not be recorded because"
				+ " op code encryption should be 1 but its "+args.getOp());
		else
			log(args);
	}
	public void updateDecryptionStarted(EncryptionLogEventArgs args) {
		if (args.getOp()==1) logger.error("operation will not be recorded because"
				+ " op code decryption should not be 1");
		else
			recordOpStart(args);
	}

	public void updateDecryptionEnded(EncryptionLogEventArgs args) {
		if (args.getOp()==1) logger.error("operation will not be recorded because"
				+ " op code decryption should not be 1");
		else
			log(args);
	}

}
