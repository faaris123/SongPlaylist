import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileNotFoundException;

public interface IXMLReader {

  /**
   * Read the given XML file, extract the attributes of the Song like Title, Release Year, and
   * Artist of the Song, create a Song object with those attributes, and add them to a single
   * ArrayList.
   * 
   * @param filePathToCSV The path to the CSV file.
   * 
   * @return Return an ArrayList of all the Song objects.
   * 
   * @throws FileNotFoundException.
   */
  ArrayList<Song> readXMLAndCreateSongObject(String filePathToCSV) throws FileNotFoundException;
}