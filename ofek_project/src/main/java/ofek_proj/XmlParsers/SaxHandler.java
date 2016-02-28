package ofek_proj.XmlParsers;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ofek_proj.EncDecInputArgs.DecryptionArgs;
import ofek_proj.EncDecInputArgs.EncryptionArgs;
import ofek_proj.EncDecInputArgs.XmlInputArgs;

public class SaxHandler extends DefaultHandler {
	private XmlInputArgs inputArgs=new XmlInputArgs();
	private EncryptionArgs encryptionArgs;
	private DecryptionArgs decryptionArgs;
	private Stack<String> elementStack = new Stack<String>();
	public XmlInputArgs getParsedArgs(){
		return inputArgs;
	}
	public void startElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException {
		this.elementStack.push(qName);
		if ("Decrypt".equals(qName)){
			decryptionArgs=new DecryptionArgs();
		}
		if ("Encrypt".equals(qName)){
			encryptionArgs=new EncryptionArgs();
		}
	}
	public void endElement(String uri, String localName,
	        String qName) throws SAXException {
		this.elementStack.pop();
		if ("Decrypt".equals(qName)){
			inputArgs.addToDecrptionArgs(decryptionArgs);
		}
		if ("Encrypt".equals(qName)){
			inputArgs.addToEncrptionArgs(encryptionArgs);
		}
	}
	public void characters(char ch[], int start, int length)
	        throws SAXException {
		String value = new String(ch, start, length).trim();
		if(value.length() == 0) return; // ignore white space
		EncryptionArgs newArgs = null;
		if ("Encrypt".equals(currentElementParent())){
			newArgs=encryptionArgs;
		}
		if ("Decrypt".equals(currentElementParent())){
			newArgs=decryptionArgs;
		}
		if ("Encrypt".equals(currentElementParent()) || 
				"Decrypt".equals(currentElementParent())){
			if("Algorithm"  .equals(currentElement())){
				newArgs.setAlgorithmName(value);
			}else if("SourceFilePath".equals(currentElement())){
				newArgs.setInputPath(value);
			}else if("SourceDirectoryPath".equals(currentElement())){
				newArgs.setInputPath(value);
			}
		}
		if("KeyFilePath".equals(currentElement()) && 
				"Decrypt".equals(currentElementParent())){
			decryptionArgs.setkeyFilePath(value);
		}
	}
	
	private String currentElement() {
        return this.elementStack.peek();
    }
    private String currentElementParent() {
        if(this.elementStack.size() < 2) return null;
        return this.elementStack.get(this.elementStack.size()-2);
    }
	
}
