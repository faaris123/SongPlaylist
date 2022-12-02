import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class SongPlayer {
  public static void main(String[] args) throws FileNotFoundException {
    ArrayList<Song> songs =
        (new XMLReader()).readXMLAndCreateSongObject("classic-rock-song-list.xml");
    for (int i = 0; i < songs.size(); i++) {
    }
    ISongPlayerBackend backend = new SongPlayerBackend(false);
    for (ISong song : songs) {
      backend.addSong(song);
    }

    Scanner userInputScanner = new Scanner(System.in);
    SongPlayerFrontend frontend = new SongPlayerFrontend(userInputScanner, backend, false);
    frontend.runLoop();
  }
}
