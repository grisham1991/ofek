package ofek_proj;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

import ofek_proj.EncDecInputArgs.DecryptionArgs;
import ofek_proj.EncDecInputArgs.EncryptionArgs;
import ofek_proj.EncDecInputArgs.XmlInputArgs;
import ofek_proj.EncDecOutputArgs.OutputArgs;
import ofek_proj.EncDecOutputArgs.XmlDecryptionOutputArgs;
import ofek_proj.EncDecOutputArgs.XmlEncryptionOutputArgs;
import ofek_proj.EncDecOutputArgs.XmlOutputArgs;
import ofek_proj.EncryptionAlgorithms.EncryptionAlgorithm;
import ofek_proj.EncryptionAlgorithms.ShiftMultiplyEncryptionAlgorithm;
import ofek_proj.EncryptionAlgorithms.ShiftUpEncryptionAlgorithm;
import ofek_proj.EncryptionAlgorithms.XorEncryptionAlgorithm;
import ofek_proj.EncryptionAlgorithms.DoubleEncryption;
import ofek_proj.EncryptionObserverImplLogger.EncryptionLogger;
import ofek_proj.FileAndDirectoryEncryptors.DirectoryEncryptor;
import ofek_proj.FileAndDirectoryEncryptors.FileEncryptor;
import ofek_proj.InjectorsAndProviders.ImplInjector;
import ofek_proj.MethdTester.MethodTester;
import ofek_proj.Utils.DoubleKey;
import ofek_proj.Utils.utilArgs;
import ofek_proj.XmlParsers.XmlParser;
import ofek_proj.exceptions.InvalidPathException;


@SuppressWarnings("restriction")
/*the main class it takes user input from gui interface and passes it  
 *  to the directory and file encryptors*/
public class ui {
	//the gui components
	private JFrame mainFrame;
	private JFrame chooseAlgorithmFrame;
	private JLabel chooseAlgorithmLabel;
	private JPanel useDoublePanel;
	private JLabel doUseDoubleLabel;
	private JButton doUseDoubleLabeltrue;
	private JButton doUseDoubleLabelfalse;
	private List<JPanel> AlgorithmsPanels=new ArrayList<JPanel>();
	private List<JButton> AlgorithmsButtons=new ArrayList<JButton>();
	private JLabel headline;
	private JLabel xmlLabel;
	private JLabel headlineXml;
	private JLabel headlineMethod;
	private JLabel headlineText;
	private JLabel statusLabel;
	private JPanel mainXmlPanel;
	private JPanel xmlPanel;
	private JPanel xmlPanelButton;
	private JPanel controlPanelEnc;
	private JPanel Methods;
	private JPanel sub_controlPanelEnc1;
	private JPanel sub_controlPanelEnc2;
	private JPanel controlPanelDec;
	private JPanel sub_controlPanelDec1;
	private JPanel sub_controlPanelDec2;
	private JPanel sub_controlPanelDec3;
	private JTextField xmlFile;
	private JTextField sourceFileEnc;
	private JTextField sourceFileDec;
	private JTextField sourcekeyDec;
	private JButton startXmlOp;
	private JButton EncButton;
	private JButton DecButton;
	private JButton TestButton;
	private JButton ChooseAlgorithmButton;
	
	//the logger
	Logger uiLogger;
	
	
	@Inject
	XmlParser xmlParser;//the parser sue to read from xml
	Schema schema = null;//the validation schema
	
	private FileEncryptor<Integer> Encryptor;//the encryptor for not double encryption
	@Inject
	private DirectoryEncryptor<Integer> DirectoryEncryptor; //the directory encryptor not double encryption
	
	
	private FileEncryptor<DoubleKey<Integer>> EncryptorDouble;//file encryption using double encryption
	@Inject
	private DirectoryEncryptor<DoubleKey<Integer>> DirectoryEncryptorDouble;//directory encryption using double encryption
	//the list of algorithm using single encryption
	private List<EncryptionAlgorithm<Integer>> algorithmList;
	//tester of encryption methods
	private MethodTester<Integer> methodTester;
	@Inject
	@Named("UnaryAlgorithmName")
	String unaryAlgorithmName;//the single encryption algorithm  name used
	
	//the prefix of double encryption its the empty string if single encryption used 
	//and the double prefix for double encryption
	@Inject
	@Named("DoublePrefix")
	String DoublePrefix;
	
	
	//main
	public static void main(String[] args){		
		ui user_interface = new ui();  
		user_interface.showInterface();  
	}
	
	//constructor
	public ui(){
		uiLogger= Logger.getLogger(ui.class);
		//inject data members
		Injector injectorImpl = Guice.createInjector(new ImplInjector());
		injectorImpl.injectMembers(this);
		//take the file encryptor from the directory encrypto
		Encryptor=DirectoryEncryptor.getFileEncryptor();
		EncryptorDouble=DirectoryEncryptorDouble.getFileEncryptor();
		readSchema();//Prepare the validation schema
		addAlgorithms();//add the single encryption algorithm to list
		//set the method tester
		methodTester=new MethodTester<Integer>();
		//test algorithm from the algorithm list
		methodTester.setAlgorithmList(algorithmList);
		SetEncryptionLogger();//make the encryptors take log
		prepareGUI();//set the gui
	}
	//init schema
	private void readSchema(){
		try {
			String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
			SchemaFactory factory = SchemaFactory.newInstance(language);
			schema = factory.newSchema(new File("src"+File.separator+"main"+File.separator+"resources"+File.separator+"xml_schema.xsd"));
		} catch (Exception e) {
			uiLogger.error("cant create scheam object",e);
		}
	}
	
	//create the algorithm list and add algorithm to it
	private void addAlgorithms(){
		algorithmList=new ArrayList<EncryptionAlgorithm<Integer>>();
		algorithmList.add(new ShiftUpEncryptionAlgorithm());
		algorithmList.add(new ShiftMultiplyEncryptionAlgorithm());
		algorithmList.add(new XorEncryptionAlgorithm());
	}
	
	//add observer to the encryptors
	private void SetEncryptionLogger(){
		EncryptionLogger Logger=new EncryptionLogger();
		Encryptor.addObserver(Logger);
		DirectoryEncryptor.addObserver(Logger);
		EncryptorDouble.addObserver(Logger);
		DirectoryEncryptorDouble.addObserver(Logger);
	}
	
	//gets algorithm name and makes the encryptors use the correct algorithm
	//return true if the encryption is double encryption and false otherwise
	private boolean setAlgorithm(String algorithmName){
		int i;
		boolean isDouble=algorithmName.startsWith(utilArgs.DoublePrefix);//check if its double encryption 
		String name=algorithmName;
		if (isDouble){//get the name of the single encryption algorithm
			name=algorithmName.substring(utilArgs.DoublePrefix.length());
		}
		//find the algorithm that matches the name
		EncryptionAlgorithm<Integer> algorithm=null;
		for (i=0;i<algorithmList.size();i++){
			if (algorithmList.get(i).getAlgorythmName().equals(name)){
				algorithm=algorithmList.get(i);
				break;
			}
		}
		//if no matching algorithm found just return 
		if (algorithm==null){
			return isDouble;
		}
		//set the matching algorithm
		if (isDouble){
			((DoubleEncryption<Integer>)(EncryptorDouble.getAlgorithm())).setAlgorithm(algorithm);
		}else{
			Encryptor.setAlgorythm(algorithm);
		}
		
		return isDouble;
	}
	//gets user input and performs decryption
	private OutputArgs Decrypt(DecryptionArgs args){
		String input=args.getInputPath();
		String KeyFilePath=args.getkeyFilePath();
		boolean isDouble=setAlgorithm(args.getAlgorithmName());//set the correct algorithm
		//check if input is a directory or a file
		File Source=new File(input);
		boolean isFile;
		boolean isDirectory;
		if (Source.isFile()){
			isFile=true;
		}else {
			isFile=false;
		}
		if (Source.isDirectory()){
			isDirectory=true;
		}else{
			isDirectory=false;
		}
		//prepare the output file paths
		int pos;
		String sourceFilePath=null;
		String outputFilePath=null;
		String sourceDirectoryPath=null;
		String outputDirectoryPath=null;
		OutputArgs output=new OutputArgs();
		//make output file be the same format as input file
		//only with _decrypted in its name
		if (isFile){
			uiLogger.debug("input is a file");
			sourceFilePath=input;			
			//prepare output file path
			pos=sourceFilePath.lastIndexOf('.');
			if (pos==-1){
				outputFilePath=sourceFilePath+"_decrypted";
			}else{
				outputFilePath=sourceFilePath.substring(0, pos)+"_decrypted"+sourceFilePath.substring(pos);
			}
		}
		//make the output directory inside the input directory and call it decrypted
		if (isDirectory){
			uiLogger.debug("input is a directory");
			sourceDirectoryPath=input;
			outputDirectoryPath=sourceDirectoryPath+File.separator+"decrypted";				
		}
		
		boolean error=false;
		//decrypt and check errors
		try{
			if (!isFile && !isDirectory){//if its not a file or directory sat the paths are nor legal
				uiLogger.debug("input is not a directory and not a file");
				throw new InvalidPathException(input);
			}else{
				if (!isDouble){//using single decryption
					uiLogger.debug("started single decryption");
					if (isFile){//decrypt a file 
						uiLogger.debug("the arguments passed to file decryptor are:"+sourceFilePath+","+ outputFilePath+","+ KeyFilePath);
						Encryptor.DecryptFile(sourceFilePath, outputFilePath,Encryptor.ReadKeyFromFile(KeyFilePath));
					}
					if (isDirectory){//decrypt a directory
						uiLogger.debug("the arguments passed to directory decryptor are:"+sourceDirectoryPath+","+ outputDirectoryPath+","+ KeyFilePath);
						DirectoryEncryptor.DecryptDirectory(sourceDirectoryPath, outputDirectoryPath, DirectoryEncryptor.ReadKeyFromFile(KeyFilePath));
					}
				}else{//using double decryption
					uiLogger.debug("started double decryption");
					if (isFile){//double decryption for file
						uiLogger.debug("the arguments passed to file double decryptor are:"+sourceFilePath+","+ outputFilePath+","+ KeyFilePath);
						EncryptorDouble.DecryptFile(sourceFilePath, outputFilePath,EncryptorDouble.ReadKeyFromFile(KeyFilePath));
					}
					if (isDirectory){//double decryption for directory
						uiLogger.debug("the arguments passed to double directory decryptor are:"+sourceDirectoryPath+","+ outputDirectoryPath+","+ KeyFilePath);
						DirectoryEncryptorDouble.DecryptDirectory(sourceDirectoryPath, outputDirectoryPath, DirectoryEncryptorDouble.ReadKeyFromFile(KeyFilePath));
					}
				}
			}
		}catch(Exception someException){//if error return error message
			uiLogger.error("Decryption faild:cought Exception",someException);
			error=true;
			output.setIsError(true);
			output.setOutputMessage("Decryption faild:"+someException.getMessage());
		}
		if (!error){//if not error return success message
			output.setIsError(false);
			if (isFile){
				output.setOutputMessage("Decryption completed successfully"+System.lineSeparator()+"the decrypted file is at "+outputFilePath); 
			}
			if (isDirectory){
				output.setOutputMessage("Decryption completed successfully"+System.lineSeparator()+"the decrypted directory is at "+outputDirectoryPath); 
			}
		}
		return output;
	}
	//encrypt file or directory given by user
	private OutputArgs Encrypt(EncryptionArgs args){
		boolean isDouble=setAlgorithm(args.getAlgorithmName());
		String input=args.getInputPath();
		File Source=new File(input);
		boolean isFile;
		boolean isDirectory;
		//check if input is a file or directory
		if (Source.isFile()){
			isFile=true;
		}else {
			isFile=false;
		}
		if (Source.isDirectory()){
			isDirectory=true;
		}else{
			isDirectory=false;
		}
		
		int pos;
		String sourceFilePath=null;
		String KeyFilePath = null;
		String outputFilePath = null;
		String SourceDirectoryPath=null;
		String OutputDirectoryPath=null;
		//make the output file be the same format only add _encrypted to its name
		//put the key file the same place ass the input file
		if (isFile){
			sourceFilePath=input;	
			pos=sourceFilePath.lastIndexOf('.');
			if (pos==-1){
				outputFilePath=sourceFilePath+"_encrypted";
			}else{
				outputFilePath=sourceFilePath.substring(0, pos)+"_encrypted"+sourceFilePath.substring(pos);
			}
			//prepare the key file path
			pos=sourceFilePath.lastIndexOf(File.separator);
			if (pos==-1)
				KeyFilePath="";
			else KeyFilePath=sourceFilePath.substring(0, pos+1);
				KeyFilePath+="key.txt";  
		}
		//make encrypted directory be under input directory
		//make key file also be in the encrypted directory
		if (isDirectory){
			SourceDirectoryPath=input;
			OutputDirectoryPath=SourceDirectoryPath+File.separator+"encrypted";
			new File(OutputDirectoryPath).mkdir();
			KeyFilePath=OutputDirectoryPath+File.separator+"key.txt";
		}
		OutputArgs output=new OutputArgs();
		boolean error=false;
		//encrypt and check for errors
		try{
			if (!isFile && !isDirectory){//if its not a file and not a directory throw exception
				uiLogger.debug("input is not a directory and not a file");
				throw new InvalidPathException(input);
			}else{
				if (!isDouble){//using single encryption
					uiLogger.debug("single encryption used");
					if (isFile){//encrypt file
						uiLogger.debug("the arguments passed to encryptor are:"+sourceFilePath+","+ outputFilePath+","+ KeyFilePath);
						Encryptor.EncryptFile(sourceFilePath, outputFilePath,Encryptor.GenerateKeyAndWriteToFile(KeyFilePath));
					}
					if (isDirectory){//encrypt directory
						uiLogger.debug("the arguments passed to directory encryptor are:"+SourceDirectoryPath+","+ OutputDirectoryPath+","+ KeyFilePath);
						DirectoryEncryptor.EncryptDirectory(SourceDirectoryPath, OutputDirectoryPath, DirectoryEncryptor.GenerateKeyAndWriteToFile(KeyFilePath));
					}
				}else{//double encryption
					uiLogger.debug("double encryption used");
					if (isFile){//encrypt file
						uiLogger.debug("the arguments passed to double encryptor are:"+sourceFilePath+","+ outputFilePath+","+ KeyFilePath);
						EncryptorDouble.EncryptFile(sourceFilePath, outputFilePath,EncryptorDouble.GenerateKeyAndWriteToFile(KeyFilePath));
					}
					if (isDirectory){//encrypt directory
						uiLogger.debug("the arguments passed to double directory encryptor are:"+SourceDirectoryPath+","+ OutputDirectoryPath+","+ KeyFilePath);
						DirectoryEncryptorDouble.EncryptDirectory(SourceDirectoryPath, OutputDirectoryPath, DirectoryEncryptorDouble.GenerateKeyAndWriteToFile(KeyFilePath));
					}
				}
			}
		}catch(Exception someException){//if error return error message
			uiLogger.error("Encryption faild:cought Exception",someException);
			output.setOutputMessage("Encryption faild:"+someException.getMessage());
			error=true;
			output.setIsError(true);
		}
		if (!error){//if not error return success message
			output.setIsError(false);
			if (isFile){
				output.setOutputMessage("Encryption completed successfully"+System.lineSeparator()+"the encrypted file is at "+outputFilePath+System.lineSeparator()
						  +"the key file is at "+KeyFilePath); 
			}
			if (isDirectory){
				output.setOutputMessage("Encryption completed successfully"+System.lineSeparator()+"the encrypted directory is at "+OutputDirectoryPath+System.lineSeparator()
	    				  +"the key file is at "+KeyFilePath); 
			}
			
		}
		return output;
	}
	
	
	//prepare GUI layout and set elements
	private void prepareGUI(){
		uiLogger.debug("starting preapering gui");
		//set main frame
		mainFrame = new JFrame("File Encryption/Decryption");
		mainFrame.setSize(720,1000);
		mainFrame.setLayout(new GridLayout(8, 1));
		//set the frame where the user chooses algorithm
		chooseAlgorithmFrame = new JFrame("Choose Algorithm");
		chooseAlgorithmFrame.setSize(360,400);
		chooseAlgorithmFrame.setLayout(new GridLayout(algorithmList.size()+2, 1));
		
		//set frames layout
		
		//set the panel for choosing double encryption or not
		useDoublePanel=new JPanel();
		useDoublePanel.setLayout(new FlowLayout());
		doUseDoubleLabel=new JLabel("",JLabel.CENTER );
		//add a panel for each algorithm
		for (int i=0;i<algorithmList.size();i++){
			JPanel panel=new JPanel();
			panel.setLayout(new FlowLayout());
			AlgorithmsPanels.add(panel);
		}
	
		chooseAlgorithmLabel=new JLabel("",JLabel.CENTER );
		chooseAlgorithmFrame.setVisible(false);
		headline= new JLabel("",JLabel.CENTER );
		Methods=new JPanel();
		Methods.setLayout(new FlowLayout());
		headlineXml=new JLabel("",JLabel.CENTER );
		xmlLabel=new JLabel("",JLabel.CENTER );
		headlineMethod=new JLabel("",JLabel.CENTER );
		statusLabel = new JLabel("",JLabel.CENTER);        
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
		        System.exit(0);
	         }        
	      });   
		chooseAlgorithmFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
		       chooseAlgorithmFrame.setVisible(false);
	         }        
	      });
		headlineText= new JLabel("",JLabel.CENTER );
		mainXmlPanel=new JPanel();
		mainXmlPanel.setLayout(new GridLayout(2, 1));
		xmlPanel= new JPanel();
		xmlPanel.setLayout(new FlowLayout());
		xmlPanelButton= new JPanel();
		xmlPanelButton.setLayout(new FlowLayout());
		controlPanelEnc = new JPanel();
		controlPanelEnc.setLayout(new GridLayout(2, 1));
		sub_controlPanelEnc1=new JPanel();
		sub_controlPanelEnc1.setLayout(new FlowLayout());
		controlPanelEnc.add(sub_controlPanelEnc1);
		sub_controlPanelEnc2=new JPanel();
		sub_controlPanelEnc2.setLayout(new FlowLayout());
		controlPanelEnc.add(sub_controlPanelEnc2);
		controlPanelDec = new JPanel();
		controlPanelDec.setLayout(new GridLayout(3, 1));
		sub_controlPanelDec1=new JPanel();
		sub_controlPanelDec1.setLayout(new FlowLayout());
		controlPanelDec.add(sub_controlPanelDec1);
		sub_controlPanelDec2=new JPanel();
		sub_controlPanelDec2.setLayout(new FlowLayout());
		controlPanelDec.add(sub_controlPanelDec2);
		sub_controlPanelDec3=new JPanel();
		sub_controlPanelDec3.setLayout(new FlowLayout());
		controlPanelDec.add(sub_controlPanelDec3);
		chooseAlgorithmFrame.add(useDoublePanel);
		chooseAlgorithmFrame.add(chooseAlgorithmLabel);
		for (int i=0;i<algorithmList.size();i++){
			chooseAlgorithmFrame.add(AlgorithmsPanels.get(i));
		}
		mainFrame.add(headline);
		mainFrame.add(headlineXml);
		mainFrame.add(mainXmlPanel);
		mainFrame.add(headlineText);
		mainFrame.add(Methods);
		mainFrame.add(controlPanelEnc);
		mainFrame.add(controlPanelDec);
		mainFrame.add(statusLabel);
		uiLogger.debug("finished preapering layout");
		uiLogger.debug("starting setting gui elements");
		
		//set the gui elements
		
		//set the choose algorithm frame gui elements
		
		//set the double or single encryption panel
		doUseDoubleLabel.setText("Use Double Encryption");
		doUseDoubleLabeltrue=new JButton("true");
		//set the use double encryption button
		doUseDoubleLabeltrue.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				DoublePrefix=utilArgs.DoublePrefix;
				headlineMethod.setText("Encryption used:"+DoublePrefix+unaryAlgorithmName);
				statusLabel.setText("Using Double Encryption");
				statusLabel.setForeground(Color.GREEN);
			}});
		
		//set the use single encryption method
		doUseDoubleLabelfalse=new JButton("false");
		doUseDoubleLabelfalse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				DoublePrefix="";
				headlineMethod.setText("Encryption used:"+unaryAlgorithmName);
				statusLabel.setText("Using Unary Encryption");
				statusLabel.setForeground(Color.GREEN);
			}});
		
		useDoublePanel.add(doUseDoubleLabel);
		useDoublePanel.add(doUseDoubleLabeltrue);
		useDoublePanel.add(doUseDoubleLabelfalse);
		//set the choose algorithm panel
		chooseAlgorithmLabel.setText("Choose Algorithm");
		chooseAlgorithmLabel.setFont(new Font("Serif", Font.BOLD, 15));
		chooseAlgorithmLabel.setForeground (Color.blue);
		//add algorithm buttons
		for (int i=0;i<algorithmList.size();i++){
			JButton button=new JButton(algorithmList.get(i).getAlgorythmName());
			button.setPreferredSize(new Dimension(170,40));
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					unaryAlgorithmName=e.getActionCommand();
					headlineMethod.setText("Encryption used:"+DoublePrefix+unaryAlgorithmName);
					statusLabel.setText("Encryption method updated");
					statusLabel.setForeground(Color.GREEN);
				}});
			AlgorithmsButtons.add(button);
		}
		//add the buttons to the pannel
		for (int i=0;i<algorithmList.size();i++){
			AlgorithmsPanels.get(i).add(AlgorithmsButtons.get(i));
		}
		
		//set the main frame gui elemnts
		
		//set headline
		headline.setFont(new Font("Serif", Font.BOLD, 25));
		headline.setForeground (Color.blue);
		headline.setText("File or Directory Encryption\\Decryption");
		
		//set xml panel
		headlineXml.setFont(new Font("Serif", Font.ITALIC, 19));
		headlineXml.setText("File or Directory Encryption\\Decryption xml file input");
		xmlFile=new JTextField(45);
		xmlLabel.setText("Enter xml file path:");
		xmlPanel.add(xmlLabel);
		xmlPanel.add(xmlFile);
		startXmlOp=new JButton("Read xml file");
		startXmlOp.addActionListener(new ActionListener(){//xml op button action
			public void actionPerformed(ActionEvent e) {
				String XmlInputFilePath=xmlFile.getText();
				XmlInputArgs args=null;
				try {
					args=xmlParser.getXmlInput(XmlInputFilePath,schema);//get argument and validate schema
				} catch (Exception e1) {//if schema validation failed print error and return
					statusLabel.setForeground(Color.red);
					statusLabel.setText("Error parsing xml file");
					uiLogger.error("Error parsing xml file",e1);
					return;
				}
				XmlOutputArgs xmlOutput=new XmlOutputArgs();
				XmlEncryptionOutputArgs xmlEncryptionOutput=new XmlEncryptionOutputArgs();
				XmlDecryptionOutputArgs xmlDecryptionOutput=new XmlDecryptionOutputArgs();
				//encrypt and decrypt each reauest
				for (EncryptionArgs encryptionArgs:args.getEncryptionArgs()){
					xmlEncryptionOutput.addToEncrptionOutputArgs(Encrypt(encryptionArgs));
				}
				for (DecryptionArgs decryptionArgs:args.getDecryptionArgs()){
					xmlDecryptionOutput.addToDecrptionOutputArgs(Decrypt(decryptionArgs));
				}
				
				xmlOutput.setDecryptionResults(xmlDecryptionOutput);
				xmlOutput.setEncryptionResults(xmlEncryptionOutput);
				String xmlOutputPath=XmlInputFilePath;
				//prepare the output file path it will be in the same location
				//and end with _output.xml
				int pos=xmlOutputPath.lastIndexOf('.');
				if (pos==-1){
					xmlOutputPath=xmlOutputPath+"_output.xml";
				}else{
					xmlOutputPath=xmlOutputPath.substring(0, pos)+"_output.xml";
				}
				File file = new File(xmlOutputPath);
				try{//marshal the output
					JAXBContext jaxbContext = JAXBContext.newInstance(XmlOutputArgs.class);
					Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					// output pretty printed
					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					jaxbMarshaller.marshal(xmlOutput, file);
					statusLabel.setForeground(Color.GREEN);
					statusLabel.setText("<html>xml operations completed<br> the output file is at:"+xmlOutputPath+"</html>");
				}catch(Exception exception){
					statusLabel.setForeground(Color.ORANGE);
					statusLabel.setText("<html>xml operations completed<br>faild creating xml output</html>");
					uiLogger.error("failed crating xml output",exception);
				}
				
			}});
		
		xmlPanelButton.add(startXmlOp);
		mainXmlPanel.add(xmlPanel);
		mainXmlPanel.add(xmlPanelButton);
		
		
		//set method headline
		headlineMethod.setFont(new Font("Serif", Font.ITALIC, 15));
		headlineMethod.setText("Encryption used: "+ DoublePrefix+unaryAlgorithmName);
		Methods.add(headlineMethod);//set the choose methods button
		ChooseAlgorithmButton=new JButton("Choose Method");
		ChooseAlgorithmButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				chooseAlgorithmFrame.setVisible(true);//make the choose algorithms frame visable
			}});
		TestButton=new JButton("Test Methods");//test methods button
		TestButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				boolean error=false;
				try{
					methodTester.Test();//test the algorithms
				}catch(Exception exception){
					error=true;
					statusLabel.setText("error testing:"+exception.getMessage());
				}
				if (!error){
					statusLabel.setForeground(Color.green);
					statusLabel.setText("Testing finished refer to log for results");
				}else{
					statusLabel.setForeground(Color.red);
				}
			}
		});
		
		Methods.add(ChooseAlgorithmButton);
		Methods.add(TestButton);
		
		//set user input headline
		headlineText.setFont(new Font("Serif", Font.ITALIC, 19));
		headlineText.setText("File or Directory Encryption\\Decryption with text input");
		
		//set the user input encryption panel
		
		//set encryption source file text filed and encrypt button
		sourceFileEnc = new JTextField(45);
		EncButton = new JButton("Encrypt");
		EncButton.addActionListener(new ActionListener(){//encryption button action
			//the encrypt button listener
			public void actionPerformed(ActionEvent e) {
				uiLogger.debug("detected encrypt button push");
				String input=sourceFileEnc.getText();
				EncryptionArgs args=new EncryptionArgs();
				args.setInputPath(input);
				args.setAlgorithmName(DoublePrefix+unaryAlgorithmName);
				OutputArgs output=Encrypt(args);
				String outputMessage="<html>"+output.getOutputMessage().replaceAll(System.lineSeparator(), "<br>")+"</html>";
				statusLabel.setText(outputMessage);
				if (output.getIsError()){
					statusLabel.setForeground(Color.RED);
				}else{
					statusLabel.setForeground(Color.GREEN);
				}
	      }}); 
		sub_controlPanelEnc1.add(new JLabel("Enter source path:   ")); 
		sub_controlPanelEnc1.add(sourceFileEnc); 
		sub_controlPanelEnc2.add(EncButton);  
		
		//set the user input decryption panel
		
		//set source key and source file text fields and decrypt button
		sourcekeyDec = new JTextField(45); 
		sub_controlPanelDec1.add(new JLabel("Enter key file path:      ")); 
		sub_controlPanelDec1.add(sourcekeyDec); 
		sourceFileDec = new JTextField(45);
		DecButton = new JButton("Decrypt");
		//set decryption button listener
		DecButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				uiLogger.debug("detected decrypt button push");
				//read user input
				DecryptionArgs args=new DecryptionArgs();
				args.setInputPath(sourceFileDec.getText());
				args.setkeyFilePath(sourcekeyDec.getText());
				args.setAlgorithmName(DoublePrefix+unaryAlgorithmName);
				OutputArgs output=Decrypt(args);
				String outputMessage="<html>"+output.getOutputMessage().replaceAll(System.lineSeparator(), "<br>")+"</html>";
				statusLabel.setText(outputMessage);
				if (output.getIsError()){
					statusLabel.setForeground(Color.RED);
				}else{
					statusLabel.setForeground(Color.GREEN);
				}
	      }}); 
		sub_controlPanelDec2.add(new JLabel("Enter source path:      ")); 
		sub_controlPanelDec2.add(sourceFileDec); 
		sub_controlPanelDec3.add(DecButton); 
		
		//set the status panel
		
		//set status line font
		statusLabel.setFont(new Font("Serif", Font.PLAIN, 17));
		uiLogger.debug("finished setting gui elements");
	}
	
	
	//make everything visable
	public void showInterface(){
		uiLogger.debug("making main frame visable");
		mainFrame.setVisible(true);  
	}
	//hide the interface
	public void hideInterface(){
		uiLogger.debug("hiding all frames");
		mainFrame.setVisible(false);
		chooseAlgorithmFrame.setVisible(false);
	}
	
}
