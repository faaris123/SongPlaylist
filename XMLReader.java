import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader implements IXMLReader {

  /**
   * Reads in the given XML files and converts it into useable data
   * 
   * @param filePathToXML
   * @return An ArrayList of all the Songs in the XML File
   * @throws FileNotFoundException
   */
  @Override
  public ArrayList<Song> readXMLAndCreateSongObject(String filePathToXML)
      throws FileNotFoundException {
    ArrayList<Song> songList = new ArrayList<>();
    try {
      File f = new File(filePathToXML);
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(f);
      doc.getDocumentElement().normalize();
      NodeList nodeList = doc.getElementsByTagName("dataitem");
      boolean flag;
      for (int i = 0; i < nodeList.getLength(); ++i) {
        Node node = nodeList.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          Element eElement = (Element) node;
          String title = eElement.getElementsByTagName("Title").item(0).getTextContent().trim();
          String artist = eElement.getElementsByTagName("Artist").item(0).getTextContent().trim();
          String releaseYear =
              eElement.getElementsByTagName("Release-Year").item(0).getTextContent().trim();
          if (releaseYear.equals("0")) {
            releaseYear = "Invalid";
          }
          Song song = new Song(title, artist, releaseYear);
          flag = true;
          for (int j = 1; j < songList.size(); ++j) {
            if (songList.get(j).getTitle().equals(title)) {
              flag = false;
            }
          }
          if (flag) {
            songList.add(song);
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return songList;
  }
}