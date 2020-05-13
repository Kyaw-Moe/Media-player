package com.myapp.mediaplayer;

public class SongInfo {

    public String Name, Artist, Path, Album;

    public SongInfo() {
    }

    public SongInfo(String name, String artist, String path, String album) {
        Name = name;
        Artist = artist;
        Path = path;
        Album = album;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }
}
