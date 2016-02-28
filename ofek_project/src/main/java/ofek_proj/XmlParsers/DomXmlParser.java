package ofek_proj.XmlParsers;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ofek_proj.EncDecInputArgs.DecryptionArgs;
import ofek_proj.EncDecInputArgs.EncryptionArgs;
import ofek_proj.EncDecInputArgs.XmlInputArgs;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
public class DomXmlParser implements XmlParser {
	public XmlInputArgs getXmlInput(String XmlFilePath,Schema schema) throws Exception {
		XmlInputArgs args=new XmlInputArgs();
		 File inputFile = new File(XmlFilePath);
         DocumentBuilderFactory dbFactory 
            = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         if  (schema!=null){//validate schema
        	 Validator validator = schema.newValidator();
        	 validator.setErrorHandler(new ErrorHandler(){
        		 public void fatalError(SAXParseException exception) throws SAXException {
        			 throw exception;
        		 }

        		 public void warning(SAXParseException exception) throws SAXException {
        			 throw exception;
        		 }

        		 public void error(SAXParseException exception) throws SAXException {
					throw exception;
        		 }
		});
        	 validator.validate(new DOMSource(doc));
         }
         doc.getDocumentElement().normalize();
         NodeList nList = doc.getElementsByTagName("Encrypt");//get all encryption operations
         for (int temp = 0; temp < nList.getLength(); temp++) {
             Node nNode = nList.item(temp);
             if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            	 Element eElement = (Element) nNode;
            	 EncryptionArgs encryptionArgs = new EncryptionArgs();
            	 encryptionArgs.setAlgorithmName(eElement.getElementsByTagName("Algorithm").item(0)
            	 .getTextContent());
            	 NodeList SourcePathNode=eElement.getElementsByTagName("SourceFilePath");
            	 if (SourcePathNode.getLength()>0){
            		 encryptionArgs.setInputPath(SourcePathNode.item(0).getTextContent());
            	 }
            	 SourcePathNode=eElement.getElementsByTagName("SourceDirectoryPath");
            	 if (SourcePathNode.getLength()>0){
            		 encryptionArgs.setInputPath(SourcePathNode.item(0).getTextContent());
            	 }
            	 args.addToEncrptionArgs(encryptionArgs);
             }
         }
         nList = doc.getElementsByTagName("Decrypt");//get all decryption operations
         for (int temp = 0; temp < nList.getLength(); temp++) {
             Node nNode = nList.item(temp);
             if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            	 Element eElement = (Element) nNode;
            	 DecryptionArgs decryptionArgs = new DecryptionArgs();
            	 decryptionArgs.setAlgorithmName(eElement.getElementsByTagName("Algorithm").item(0)
            	 .getTextContent());
            	 decryptionArgs.setkeyFilePath(eElement.getElementsByTagName("KeyFilePath").item(0)
                    	 .getTextContent());
            	 NodeList SourcePathNode=eElement.getElementsByTagName("SourceFilePath");
            	 if (SourcePathNode.getLength()>0){
            		 decryptionArgs.setInputPath(SourcePathNode.item(0).getTextContent());
            	 }
            	 SourcePathNode=eElement.getElementsByTagName("SourceDirectoryPath");
            	 if (SourcePathNode.getLength()>0){
            		 decryptionArgs.setInputPath(SourcePathNode.item(0).getTextContent());
            	 }
            	 args.addToDecrptionArgs(decryptionArgs);
             }
         }
         return args;
	}
	public XmlInputArgs getXmlInput(String XmlFilePath) throws Exception{
		return getXmlInput(XmlFilePath,null);
	}

}
