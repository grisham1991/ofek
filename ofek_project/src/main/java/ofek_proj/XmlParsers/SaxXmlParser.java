package ofek_proj.XmlParsers;

import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ofek_proj.EncDecInputArgs.XmlInputArgs;

public class SaxXmlParser implements XmlParser{
	public XmlInputArgs getXmlInput(String XmlFilePath,Schema schema) throws Exception{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		if (schema!=null){
			factory.setSchema(schema);
		}
		InputStream    xmlInput  =new FileInputStream(XmlFilePath);
		SAXParser      saxParser = factory.newSAXParser();
		SaxHandler handler   = new SaxHandler() {
			  @Override
			  public void error(SAXParseException e) throws SAXException {
			    throw e;
			  }
		};
		saxParser.parse(xmlInput, handler);
		return handler.getParsedArgs();
	}
	public XmlInputArgs getXmlInput(String XmlFilePath) throws Exception{
		return getXmlInput(XmlFilePath,null);
	}

}
