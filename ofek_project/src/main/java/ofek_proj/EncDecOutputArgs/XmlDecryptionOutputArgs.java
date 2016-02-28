package ofek_proj.EncDecOutputArgs;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//decryption algorithm output
@SuppressWarnings("restriction")
@XmlRootElement
public class XmlDecryptionOutputArgs {
	
	private List <OutputArgs> decryptionOutputArgs=new ArrayList<OutputArgs>();
	
	public void addToDecrptionOutputArgs(OutputArgs args){
		decryptionOutputArgs.add(args);
	}
	@XmlElement( name = "DecryptionResult" )
	public void setDecryptionOutputArgs(List <OutputArgs> decryptionOutputArgs){
		this.decryptionOutputArgs=decryptionOutputArgs;
	}
	public List<OutputArgs> getDecryptionOutputArgs(){
		return decryptionOutputArgs;
	}
}
