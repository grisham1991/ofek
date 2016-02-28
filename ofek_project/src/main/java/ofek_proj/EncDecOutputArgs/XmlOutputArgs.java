package ofek_proj.EncDecOutputArgs;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
//xml output list of encryption and decryption results
@SuppressWarnings("restriction")
@XmlRootElement( name = "Results" )
public class XmlOutputArgs {
	private XmlEncryptionOutputArgs EncryptionResults;
	private XmlDecryptionOutputArgs DecryptionResults;
	
	@XmlElement( name = "EncryptionResults" )
	public void setEncryptionResults(XmlEncryptionOutputArgs EncryptionResults){
		this.EncryptionResults=EncryptionResults;
	}
	public XmlEncryptionOutputArgs getEncryptionResults(){
		return EncryptionResults;
	}
	@XmlElement( name = "DecryptionResults" )
	public void setDecryptionResults(XmlDecryptionOutputArgs DecryptionResults){
		this.DecryptionResults=DecryptionResults;
	}
	public XmlDecryptionOutputArgs getDecryptionResults(){
		return DecryptionResults;
	}
}
