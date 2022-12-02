import java.util.List;

public interface ISongPlayerBackend {
  /**
   * Adds a new song to the backend's database and is stored in playlist internally.
   * 
   * @param song the song to add
   */
  public void addSong(ISong song);

  /**
   * Removes a new song to the backend's database and is stored in a Playlist internally.
   * 
   * @param song the song to remove
   */
  public void removeSong(ISong song);


  /**
   * This method can be used to set a filter for the author names contained in the search results. A
   * song is only returned as a result for a search byName and ByYear, it is also contains the
   * string filterBy in the names of its authors.
   * 
   * @param filterBy the string that the song's author names must contain
   */
  public void setAuthorFilter(String filterBy);

  /**
   * Returns the string used as the author filter, null if no author filter is currently set.
   * 
   * @return the string used as the author filter, or null if none is set
   */
  public String getAuthorFilter();

  /**
   * Resets the author filter to null (no filter).
   */
  public void resetAuthorFilter();

  /**
   * Search through all the songs in the year base and return songs that are being released in the
   * given year(and that satisfies the author filter, if an author filter is set).
   * 
   * @param year year of the song that are being released Search through all the songs in the year
   *             base and return songs that are being released in the given year(and that satisfies
   *             the author filter, if an author filter is set).
   * @param year year of the song that are being released
   * @return list of songs found by the given year
   */
  public List<ISong> searchByYear(String year);

  /**
   * Search through all the songs in the Name base and return songs whose name contains the string
   * word (and that satisfies the author filter, if an author filter is set).
   * 
   * @param word word that must be contained in a song's name in result set
   * @return list of songs found by the given year
   */
  public ISong searchByName(String word);
}