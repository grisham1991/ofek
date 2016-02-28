package ofek_proj.XmlParsers;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

import ofek_proj.EncDecInputArgs.XmlInputArgs;

@SuppressWarnings("restriction")
public class JaxbXmlParser implements XmlParser {

	
	public XmlInputArgs getXmlInput(String XmlFilePath,Schema schema) throws Exception {
		File file = new File(XmlFilePath);
		JAXBContext jaxbContext = JAXBContext.newInstance(XmlInputArgs.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		if (schema!=null){
			jaxbUnmarshaller.setSchema(schema);
		}
		return (XmlInputArgs) jaxbUnmarshaller.unmarshal(file);
	}
	public XmlInputArgs getXmlInput(String XmlFilePath) throws Exception{
		return getXmlInput(XmlFilePath,null);
	}

}
