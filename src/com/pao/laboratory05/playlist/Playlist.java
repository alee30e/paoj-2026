package com.pao.laboratory05.playlist;


import java.util.Arrays;

public class Playlist {
    private String name;
    private Song[] songs = new Song[0];
    public String getName(){
        return name;
    }
    public Playlist(String name){
        this.name = name;
    }
    public void addSong(Song song){
        Song[] temp = new Song[songs.length + 1];
        System.arraycopy(songs, 0, temp, 0, songs.length);
        temp[temp.length - 1] = song;
        songs = temp;
    }
    public void printSortedByTitle(){
        Song[] temp = songs.clone();
        Arrays.sort(temp);
        for (Song s : temp){
            System.out.println(s);
        }
    }

    public void printSortedByDuration(){
        Song[] temp = songs.clone();
        Arrays.sort(temp, new SongDurationComparator());
        for (Song s : temp) {
            System.out.println(s);
        }
    }
    int getTotalDuration(){
        int total = 0;
        for (Song s : songs){
            total += s.durationSeconds();
        }
        return total;
    }
}
