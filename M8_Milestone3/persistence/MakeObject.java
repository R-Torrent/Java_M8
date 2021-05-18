package M8_Milestone3.persistence;

import java.util.NoSuchElementException;
import java.util.Scanner;

import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import M8_Milestone3.domain.UsuariExtended;
import M8_Milestone3.domain.Video;

public class MakeObject {
	
// Si las entradas en la base de datos XML fueron creadas usando el API, ya habrán sido verificadas y no saltarán excepciones
	protected static UsuariExtended fromXML(Element elementU) throws NoSuchElementException { 
		Scanner scannerU = new Scanner(elementU.getAttribute("registre"));
		scannerU.useDelimiter("-");
		int regY = scannerU.nextInt();
		int regM = scannerU.nextInt();
		int regD = scannerU.nextInt();
		scannerU.close();
		
		int password = Integer.parseInt(elementU.getElementsByTagName("password").item(0).getFirstChild().getNodeValue());
		String nom = elementU.getElementsByTagName("nom").item(0).getFirstChild().getNodeValue();
		String cognom = elementU.getElementsByTagName("cognom").item(0).getFirstChild().getNodeValue();	 
		
		UsuariExtended usuari;
		try { usuari = new UsuariExtended(nom, cognom, password, regY, regM, regD);
		} catch (Exception e) { throw new NoSuchElementException(); }
		
		NodeList videos = elementU.getElementsByTagName("video");
		Element elementV;
		for(int v = 0; v < videos.getLength(); v++) {
			elementV = (Element)videos.item(v);
			
			Scanner scannerV = new Scanner(elementV.getAttribute("timestamp"));
			scannerV.useDelimiter(Pattern.compile("-|T|:"));
			int stmpYr = scannerV.nextInt();
			int stmpMo = scannerV.nextInt();
			int stmpDy = scannerV.nextInt();
			int stmpHr = scannerV.nextInt();
			int stmpMi = scannerV.nextInt();
			int stmpSc = scannerV.nextInt();
			scannerV.close();
			
			String url = elementV.getElementsByTagName("URL").item(0).getFirstChild().getNodeValue();
			String titol = elementV.getElementsByTagName("titol").item(0).getFirstChild().getNodeValue();
			
			Scanner scannerD = new Scanner(elementV.getElementsByTagName("durada").item(0).getFirstChild().getNodeValue());
			scannerD.useDelimiter(Pattern.compile(":"));
			int durHr = scannerD.nextInt();
			int durMi = scannerD.nextInt();
			int durSc = scannerD.nextInt();
			scannerD.close();
			
			Video video; 
			try { video = new Video(url, titol, durHr, durMi, durSc, stmpYr, stmpMo, stmpDy, stmpHr, stmpMi, stmpSc);
			} catch (Exception e) { throw new NoSuchElementException(); }
			
			NodeList tags = elementV.getElementsByTagName("tag");
			Element elementT;
			for(int t = 0; t < tags.getLength(); t++) {
				elementT = (Element)tags.item(t);
				video.addTag(elementT.getFirstChild().getNodeValue());
			}
			
			usuari.addVideo(video);
		}
		
		return usuari;
	}
	
}
