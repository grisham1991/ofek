package ofek_proj.EncDecInputArgs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

//decryption input
@SuppressWarnings("restriction")
@XmlRootElement( name = "Decrypt" )
public class DecryptionArgs extends EncryptionArgs {
	protected String keyFilePath;
	public String getkeyFilePath(){
		return keyFilePath;
	}
	@XmlElement(name="KeyFilePath")
	public void setkeyFilePath(String keyFilePath){
		this.keyFilePath=keyFilePath;
	}
	
}
