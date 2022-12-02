import java.util.Scanner;
import java.util.List;

public interface ISongPlayerFrontend {
  /**
   * The constructor that the implementation this interface will provide. Takes in a scanner and an
   * object from the Backend
   */
  // ISongPlayerFrontend(Scanner userInput, ISongPlayerBackend backend)

  public void runLoop();

  public void displayMenu();

  public void displaySongs(List<ISong> songs);

  public void yearSearch();

  public void nameSearch();
}