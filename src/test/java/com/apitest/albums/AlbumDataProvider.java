package com.apitest.albums;

public class AlbumDataProvider {
    public static AlbumPayload createAlbum() {
        return new AlbumPayload(1, "Automation Test Album");
    }
    public static int getValidAlbumId() { return 1; }
    public static int getInvalidAlbumId() { return 99999; }
}
