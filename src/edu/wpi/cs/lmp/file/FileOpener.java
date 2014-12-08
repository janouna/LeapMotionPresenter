package edu.wpi.cs.lmp.file;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public abstract class FileOpener {	
	public static void openPresentation() {
		// TODO File Chooser
		
		try {
			File file = new File("SampleXMLSave.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			
			Element root = doc.getDocumentElement();
			
			parseNode(root.getFirstChild());
			
		} catch (ParserConfigurationException e) {
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

	private static void parseNode(Node node) {
		for(Node n = node; n != null; n = n.getNextSibling()){ // Iterates through sibling nodes
			// TODO Do a thing
			
			if(n.hasChildNodes()) // Recursive call to child nodes
				parseNode(n.getFirstChild());
		}
	}
}
