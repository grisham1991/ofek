package ofek_proj.EncryptionObserverImplLogger;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

//arguments for encryption logger
public class EncryptionLogEventArgs {
	
		private long miliSeconds;
		private String outputPath;
		private String inputPath;
		private String algorithmName;
		private int op;
		private boolean isFile;
		public EncryptionLogEventArgs(String outputPath,String inputPath,String algorithmName,int op,boolean isFile){
			this.outputPath=outputPath;
			this.inputPath=inputPath;
			this.algorithmName=algorithmName;
			this.op=op;//op=1 for encryption op!=1 for decryption
			miliSeconds=System.currentTimeMillis();
			this.isFile=isFile;//is it a file or a directory
		}
		
		public String getOutputPath(){
			return outputPath;
		}
		public String getInputPath(){
			return inputPath;
		}
		public String getAlgorithmName(){
			return algorithmName;
		}
		public long getMiliSeconds(){
			return miliSeconds;
		}
		public boolean getIsFile(){
			return isFile;
		}
		public int getOp(){
			return op;
		}
		@Override
		public boolean equals(Object obj){
			if (!(obj instanceof EncryptionLogEventArgs))
	            return false;
	        if (obj == this)
	            return true;
	        EncryptionLogEventArgs otherArgs = (EncryptionLogEventArgs) obj;
	        return new EqualsBuilder().
	            append(outputPath, otherArgs.outputPath).
	            append(inputPath, otherArgs.inputPath).
	            append(algorithmName, otherArgs.algorithmName).
	            append(op, otherArgs.op).
	            isEquals();
		}
		
		@Override
	    public int hashCode() {
	        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
	            append(outputPath).
	            append(inputPath).
	            append(algorithmName).
	            append(op).
	            toHashCode();
	    }
}
