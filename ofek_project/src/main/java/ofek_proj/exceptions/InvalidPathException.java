package ofek_proj.exceptions;

@SuppressWarnings("serial")
public class InvalidPathException extends Exception {
	String Path;
	public InvalidPathException(String Path){
		this.Path=Path;
	}
	public String getMessage(){
		String err;
		if (Path.length()!=0){
			err= "the path:"+Path+" is invalid";
		}
		else {err= "blank path not allowed";}
		return err;
	}
}
