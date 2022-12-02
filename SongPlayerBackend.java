import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SongPlayerBackend implements ISongPlayerBackend {
  SortedCollectionInterface<ISong> RBT;
  String filterBy;

  SongPlayerBackend() {
    RBT = new RedBlackTree<ISong>();
  }

  /**
   * Adds a new song to the song playlist
   * 
   * @param song the song to add
   */
  @Override
  public void addSong(ISong song) {
    RBT.insert(song);

  }

  /**
   * Removes a song from the playlist
   * 
   * @param song the song to add
   */
  @Override
  public void removeSong(ISong song) {
    RBT.remove(song);

  }

  /**
   * Used to set a filter for the Artist names contained in the search results.
   * 
   * @param filterBy the string that the song's Artist names must contain
   */
  @Override
  public void setAuthorFilter(String filterBy) {
    this.filterBy = filterBy;

  }

  /**
   * Returns the string used as the author filter, null if no author filter is currently set.
   * 
   * @return the string used as the author filter, or null if none is set
   */
  @Override
  public String getAuthorFilter() {

    return this.filterBy;
  }

  /**
   * Resets the author filter to null (no filter).
   */
  @Override
  public void resetAuthorFilter() {
    this.filterBy = null;

  }

  /**
   * Search through all the songs in the year base and return songs whose year is equal to the
   * string year (and that satisfies the author filter, if an author filter is set).
   * 
   * @param year year that song is being published
   * @return list of songs found
   */
  @Override
  public List<ISong> searchByYear(String year) {
    List<ISong> resultList = new ArrayList<>();
    Iterator<ISong> itr = RBT.iterator();

    while (itr.hasNext()) {
      ISong currSong = (ISong) itr.next();
      String SongYear = currSong.getReleaseYear();
      if (SongYear.equals(year.trim())) {
        if (filterBy != null) {
          if (currSong.getArtist().contains(filterBy.trim())) {
            resultList.add(currSong);
          }
        } else {
          resultList.add(currSong);
        }
      }
    }
    return resultList;
  }

  /**
   * Search through all the songs in the title base and return songs whose title contains the string
   * word (and that satisfies the author filter, if an author filter is set).
   * 
   * @param word word that must be contained in a song's title in result set
   * @return list of songs found
   */
  @Override
  public ISong searchByName(String word) {
    ISong song = new BD_Song(word, "The Weeknd", "2020");
    if (!RBT.contains(song)) {
      throw new NoSuchElementException("the song does not exist!");
    }
    if (filterBy != null) {
      if (!RBT.search(song).getArtist().equals(filterBy)) {
        throw new NoSuchElementException("the song does not exist!");
      }
    }
    return RBT.search(song);
  }
}