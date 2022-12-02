import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class SongPlayerFrontend implements ISongPlayerFrontend {
  protected Scanner user = new Scanner(System.in);
  private ISongPlayerBackend backend;

  SongPlayerFrontend(Scanner user, ISongPlayerBackend backend) throws FileNotFoundException {
    this.backend = backend;
    this.user = user;
  }

  /**
   * Runs the loop that will serve as a entry point for user inputs and their corresponding
   * functions
   */
  @Override
  public void runLoop() {
    displayMenu();
    String input = user.next();
    while (!input.equals("6")) {
      if (input.equals("1")) {
        nameSearch();
        input = user.next();
      }
      else if (input.equals("2")) {
        System.out.println("Filtering Songs by Artist Name: ");
        System.out.println("Enter Artist Name: ");
        user.nextLine();
        String artistName = user.nextLine();
        System.out.println();
        if (artistName.equals("none")) {
          backend.resetAuthorFilter();
          displayMenu();
        } else {
          backend.setAuthorFilter(artistName);
          displayMenu();
        }
        input = user.next();
      }
      else if (input.equals("3")) {
        yearSearch();
        input = user.next();
      }
      else if (input.equals("4")) {
        System.out.println("Removing a Song");
        System.out.println("Name of Song that will be removed: ");
        user.nextLine();
        String songName = user.nextLine();
        try {
          ISong removedSong = (ISong) backend.searchByName(songName);
          backend.removeSong(removedSong);
        } catch (Exception e) {
          System.out.println("Song is not in Playlist");
        }
        System.out.println();
        displayMenu();
        input = user.next();
      }
      else if (input.equals("5")) {
        System.out.println("Adding a Song");
        System.out.println("Name of Song that will be added: ");
        user.nextLine();
        String songName = user.nextLine();
        System.out.println("Artist of Song: ");
        String artistName = user.nextLine();
        System.out.println("Release Year: ");
        String releaseYear = user.nextLine();
        Song addedSong = new Song(songName, artistName, releaseYear);
        try {
          backend.addSong(addedSong);
        } catch (Exception e) {
          System.out.println("Song is already in Playlist");
        }
        System.out.println();
        displayMenu();
        input = user.next();
      }
      else {
        System.out.println("Cannot process " + input + ". Please enter a valid input.");
        System.out.println();
        displayMenu();
        input = user.next();
      }
    }
    if (input.equals("6")) {
      System.out.println("Goodbye");
    }
    user.close();
  }

  /**
   * Prints out the main menu of the application
   */
  @Override
  public void displayMenu() {
    System.out.println("Welcome to the Song Playlist Application: ");
    System.out.println("1. Search Song by Name");
    System.out.println("2. Filter Artist Name");
    System.out.println("3. Search Song by Release Year");
    System.out.println("4. Remove a Song");
    System.out.println("5. Add a Song");
    System.out.println("6. Exit Application");
  }

  /**
   * Prints out a list of songs 
   * @param songs
   */
  @Override
  public void displaySongs(List<ISong> songs) {
    String songList = "";
    int num = 1;
    for (int i = 0; i < songs.size(); i++) {
      if (i == songs.size() - 1) {
        songList += String.valueOf(num);
        num += 1;
        songList += ". ";
        songList += '"' + songs.get(i).getTitle() + " by " + songs.get(i).getArtist() + '"';
        songList += "\n";
      } else {
        songList += String.valueOf(num);
        songList += ". ";
        songList += '"' + songs.get(i).getTitle() + " by " + songs.get(i).getArtist() + '"';
        num += 1;
        songList += "\n";
        songList += "\n";
      }
    }
    System.out.println(songList);
  }

  /**
   * Prints out a single song
   * @param song
   */
  public void displaySong(ISong song) {
    String songList = "";
    int num = 1;
    songList += String.valueOf(num);
    songList += ". ";
    songList += '"' + song.getTitle() + " by " + song.getArtist() + '"';
    songList += "\n";
    System.out.println(songList);
  }

  /**
   * Conducts the year search operation on the list of songs 
   */
  @Override
  public void yearSearch() {
    System.out.println("Searching by Release Year: ");
    System.out.println("Enter Year: ");
    user.useDelimiter("\n");
    String year = user.next();
    user.nextLine();
    System.out.println();
    if (year.equals("none")) {
      year = "";
      List<ISong> a = backend.searchByYear(year);
      if (a.size() == 0) {
        System.out.println("No results found");
        displayMenu();
      } else {
        displaySongs(a);
        System.out.println();
        displayMenu();
      }
    } else {
      if (backend.getAuthorFilter() == null) {
        List<ISong> a = backend.searchByYear(year);
        displaySongs(a);
        System.out.println();
        displayMenu();
      } else {
        List<ISong> a = backend.searchByYear(year);
        displaySongs(a);
        System.out.println();
        displayMenu();
      }
    }
  }

  /**
   * Conducts the name search operation on the list of songs
   */
  @Override
  public void nameSearch() {
    System.out.println("Searching by Name: ");
    System.out.println("Enter Song Name to lookup: ");
    user.useDelimiter("\n");
    String name = user.next();
    user.nextLine();
    try {
      if (name.equals("none")) {
        name = "";
        ISong a = backend.searchByName(name);
        if (a == null) {
          System.out.println("No results found");
          displayMenu();
        } else {
          displaySong(a);
          System.out.println();
          displayMenu();
        }
      } else {
        if (backend.getAuthorFilter() == null) {
          ISong a = backend.searchByName(name);
          displaySong(a);
          System.out.println();
          displayMenu();
        } else {
          ISong a = backend.searchByName(name);
          displaySong(a);
          System.out.println();
          displayMenu();
        }
      }
    } catch (Exception e) {
      System.out.println("Song is not in Playlist");
      System.out.println();
      displayMenu();
    }
  }
}