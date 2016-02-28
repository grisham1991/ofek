package ofek_proj.InjectorsAndProviders;

import com.google.inject.Provider;

import ofek_proj.XmlParsers.JaxbXmlParser;
import ofek_proj.XmlParsers.XmlParser;

public class XmlImplProvider implements Provider<XmlParser> {
	//use jxa parser
	public XmlParser get() {
		return new JaxbXmlParser();
	}

}
