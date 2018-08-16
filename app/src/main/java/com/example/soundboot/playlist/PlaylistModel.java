package com.example.soundboot.playlist;

public class PlaylistModel {

    private String name, imageUri, tracksHref, playlistUri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getTracksHref() {
        return tracksHref;
    }

    public void setTracksHref(String tracksHref) {
        this.tracksHref = tracksHref;
    }

    public String getPlaylistUri() {
        return playlistUri;
    }

    public void setPlaylistUri(String playlistUri) {
        this.playlistUri = playlistUri;
    }
}
