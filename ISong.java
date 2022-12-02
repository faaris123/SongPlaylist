public interface ISong extends Comparable<ISong> {

  int compareTo(ISong song);

  /**
   * Get the artist of this Song object.
   * 
   *
   * @return return the title of this song.
   */
  String getTitle();

  /**
   * Get the release year of this Song object.
   *
   * @return return the release year of this Song object.
   */
  String getReleaseYear();

  /**
   * Get the artist of this Song object.
   *
   * @return return the artist of this Song object.
   */
  String getArtist();

  /**
   * Set the title of the book.
   */
  void setTitle(String title);

  /**
   * Set the release year of the book.
   */
  void setReleaseYear(String releaseYear);

  /**
   * Set the artist of the book.
   */
  void setArtist(String artist);
}