package ofek_proj.EncDecInputArgs;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

//xml arguments unbounded number of encryption and decryption arguments
@SuppressWarnings("restriction")
@XmlRootElement( name = "ProcessSettings" )
public class XmlInputArgs {
	private List <EncryptionArgs> encryptionArgs=new ArrayList<EncryptionArgs>();
	private List <DecryptionArgs> decryptionArgs=new ArrayList<DecryptionArgs>();
	public void addToEncrptionArgs(EncryptionArgs args){
		encryptionArgs.add(args);
	}
	public void addToDecrptionArgs(DecryptionArgs args){
		decryptionArgs.add(args);
	}
	public List<EncryptionArgs> getEncryptionArgs(){
		return encryptionArgs;
	}
	public List<DecryptionArgs> getDecryptionArgs(){
		return decryptionArgs;
	}
	@XmlElement( name = "Encrypt" )
	public void setEncryptionArgs(List <EncryptionArgs> encryptionArgs){
		this.encryptionArgs=encryptionArgs;
	}
	@XmlElement( name = "Decrypt" )
	public void setDecryptionArgs(List <DecryptionArgs> decryptionArgs){
		this.decryptionArgs=decryptionArgs;
	}
}
