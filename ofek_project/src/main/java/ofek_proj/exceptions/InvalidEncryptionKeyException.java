package ofek_proj.exceptions;

@SuppressWarnings("serial")
public class InvalidEncryptionKeyException extends Exception {
	String reason;
	public InvalidEncryptionKeyException(String reason){
		this.reason=reason;
	}
	public String getMessage(){
		return "the key given is invalid becouse "+reason;
	}
	
}
