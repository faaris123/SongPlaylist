import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Class with main method to run the Song Player app.
 */
public class SongPlayer {
  public static void main(String[] args) throws FileNotFoundException {
    // load the songs from the data file
    ArrayList<Song> songs =
        (new XMLReader()).readXMLAndCreateSongObject("classic-rock-song-list.xml");
    for (int i = 0; i < songs.size(); i++) {
    }
    // instantiate the backend
    ISongPlayerBackend backend = new SongPlayerBackend(false);
    // add all the songs to the backend
    for (ISong song : songs) {
      backend.addSong(song);
    }

    // instantiate the scanner for user input (to be used by the front end)
    Scanner userInputScanner = new Scanner(System.in);
    // instantiate the front end and pass references to the scanner, backend, and isbn validator to
    // it
    SongPlayerFrontend frontend = new SongPlayerFrontend(userInputScanner, backend, false);
    // start the input loop of the front end
    frontend.runLoop();
  }
}