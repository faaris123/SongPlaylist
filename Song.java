public class Song implements ISong {
  String title;
  String releaseYear;
  String artist;

  public Song(String songName, String artistName, String year) {
    this.title = songName;
    this.releaseYear = year;
    this.artist = artistName;
  }

  /**
   * Getter method for the name of a song
   * 
   * @return string representation of title
   */
  @Override
  public String getTitle() {
    return this.title;
  }

  /**
   * Getter method for the year a song was released
   * 
   * @return string representation of release year
   */
  @Override
  public String getReleaseYear() {
    return this.releaseYear;
  }

  /**
   * Compares the titles of two different songs
   * 
   * @param song the song being compared or compared to
   * @return 1 if the song's title is greater than the others 0 if they are equal and -1 if it is
   *         smaller.
   */
  @Override
  public int compareTo(ISong song) {
    return this.title.compareTo(song.getTitle());
  }

  /**
   * Getter method for the artist
   * 
   * @return string representation of the artist name
   */
  @Override
  public String getArtist() {
    return this.artist;
  }

  /**
   * Setter method for the name of the song
   * 
   * @param title name of the song
   */
  @Override
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Setter method for the release year of a song
   * 
   * @param releaseYear the year a song was released
   */
  @Override
  public void setReleaseYear(String releaseYear) {
    this.releaseYear = releaseYear;
  }

  /**
   * Setter method for the artist name
   * 
   * @param artist name of the artist
   */
  @Override
  public void setArtist(String artist) {
    this.artist = artist;
  }

  @Override
  public String toString() {
    return this.title;
  }
}