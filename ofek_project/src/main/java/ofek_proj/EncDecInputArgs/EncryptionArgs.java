package ofek_proj.EncDecInputArgs;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
//encryption arguments
@SuppressWarnings("restriction")
@XmlRootElement( name = "Encrypt" )
public class EncryptionArgs {
	protected String AlgorithmName;
	protected String inputPath;
	public String getAlgorithmName(){
		return AlgorithmName;
	}
	@XmlElement(name="Algorithm")
	public void setAlgorithmName(String AlgorithmName){
		this.AlgorithmName=AlgorithmName;
	}
	@XmlElement(name="SourceFilePath")
	public void setInputPath(String inputPath){
		this.inputPath=inputPath;
	}
	@XmlElement(name="SourceDirectoryPath")
	public void setDirectoryInputPath(String inputPath){
		this.inputPath=inputPath;
	}
	
	public String getInputPath(){
		return inputPath;
	}
}
