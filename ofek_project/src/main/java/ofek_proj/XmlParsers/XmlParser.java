package ofek_proj.XmlParsers;

import javax.xml.validation.Schema;

import ofek_proj.EncDecInputArgs.XmlInputArgs;

public interface XmlParser {
	public XmlInputArgs getXmlInput(String XmlFilePath,Schema schema) throws Exception;
	public XmlInputArgs getXmlInput(String XmlFilePath) throws Exception;
}
