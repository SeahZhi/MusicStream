package com.example.musicstream;

public class SongCollection {

    Song songs[] = new Song[3];

    public SongCollection(){
        Song theWayYouLookTonight = new Song("S1001",
                "1. The Way You Look Tonight",
                "https://p.scdn.co/mp3-preview/a5b8972e764025020625bbf9c1c2bbb06e394a60?cid=2afe87a64b0042dabf51f37318616965",
                "Michael Buble",
                4.66,
                R.drawable.michael_buble_collection);
        Song billiejean = new Song("s1002",
                "2. Billie Jean",
                "https://p.scdn.co/mp3-preview/f504e6b8e037771318656394f532dede4f9bcaea?cid=2afe87a64b0042dabf51f37318616965",
                "Michael Jackson",
                4.9,
                R.drawable.billie_jean);
        Song ed = new Song("s1003",
                "3. One",
                "https://p.scdn.co/mp3-preview/daa8679253ba20620db6e1db9c88edfcf1f4069f?cid=2afe87a64b0042dabf51f37318616965",
                "Ed Sheeran",
                4.21,
                R.drawable.photograph);

        songs[0] = theWayYouLookTonight;
        songs[1] = billiejean;
        songs[2] = ed;
    }

    public int searchSongById(String id){
        for(int index = 0; index < songs.length; index++){
            Song tempSong = songs[index];
            if(tempSong.getId().equals(id))
                return index;
        }
        return -1;
    }

    public Song getCurrentSong(int currentSongId){
        return songs[currentSongId];
    }

    public int getNextSong(int currentSongIndex){
        if(currentSongIndex >=  songs.length-1){
            return  currentSongIndex - 2;
        }
        else {
            return currentSongIndex + 1;
        }
    }
    public int getPrevSong(int currentSongIndex){
        if(currentSongIndex <= 0){
            return currentSongIndex + 2;
        }
        else{
            return currentSongIndex-1;
        }
    }

}
