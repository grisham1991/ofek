package ofek_proj.EncDecOutputArgs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement
public class OutputArgs {
	private boolean isError=false;
	private String outputMessage;
	
	public String getOutputMessage(){
		return outputMessage;
	}
	public boolean getIsError(){
		return isError;
	}
	@XmlElement( name = "OutputMessage" )
	public void setOutputMessage(String outputMessage){
		this.outputMessage=outputMessage;
	}
	@XmlElement( name = "isError" )
	public void setIsError(boolean isError){
		this.isError=isError;
	}
	
}
