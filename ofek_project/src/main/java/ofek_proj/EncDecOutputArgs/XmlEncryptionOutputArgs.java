package ofek_proj.EncDecOutputArgs;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//encryption algorithm output
@SuppressWarnings("restriction")
@XmlRootElement
public class XmlEncryptionOutputArgs {
	private List <OutputArgs> encryptionOutputArgs=new ArrayList<OutputArgs>();
	
	public void addToEncrptionOutputArgs(OutputArgs args){
		encryptionOutputArgs.add(args);
	}
	@XmlElement( name = "EncryptionResult" )
	public void setEncryptionOutputArgs(List <OutputArgs> encryptionOutputArgs){
		this.encryptionOutputArgs=encryptionOutputArgs;
	}
	public List<OutputArgs> getEncryptionOutputArgs(){
		return encryptionOutputArgs;
	}
}
