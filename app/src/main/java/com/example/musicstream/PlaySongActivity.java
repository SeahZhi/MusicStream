package com.example.musicstream;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlaySongActivity extends AppCompatActivity {

    private String title = "";
    private String artiste = "";
    private String filelink = "";
    private int drawable;
    private int currentIndex = -1;

    private MediaPlayer player = new MediaPlayer();
    private Button btnPlayPause = null;
    private SongCollection songCollection = new SongCollection();
    private SongCollection originalsongCollection = new SongCollection();

    List<Song> shuffleList = Arrays.asList(songCollection.songs);
    //Sorts songs array into a list for the purpose of shuffling.

    //The 3 variables necessary for the SeekBar Example
    SeekBar seekbar;
    Handler handler = new Handler();
    Runnable runnable;

    Button repeatBtn;
    Boolean repeatFlag = false;
    Button shuffleBtn;
    Boolean shuffleFlag = false;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        if (player.isPlaying()){
            player = null;
        }
        seekbar = (SeekBar) findViewById(R.id.songSeekBar);
        //Reset the player to prevent any problem regarding the player e.g. multiple songs playing at once

        btnPlayPause = findViewById(R.id.btnPlayPause);
        Bundle songData = this.getIntent().getExtras();
        currentIndex = songData.getInt("index"); // getting the pressed song array position.
        Log.d("temasek", "Retrieved Position is: " + currentIndex);
        displaySongBasedOnIndex(currentIndex);
        playSong(filelink);

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            //listens to the sequence for any updates which always runs in the background
            @Override
            public void onPrepared(MediaPlayer mp){
                seekbar.setMax(player.getDuration());
                updateSeekbar();
            }
        });
        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                double ratio = percent/100;
            }
        });

        repeatBtn = findViewById(R.id.repeatBtn);
        shuffleBtn = findViewById(R.id.shuffleBtn);

    }


    public void displaySongBasedOnIndex(int selectedIndex) {
        Song song = songCollection.getCurrentSong(selectedIndex);
        title = song.getTitle();
        artiste = song.getArtiste();
        filelink = song.getFileLink();
        drawable = song.getDrawable();

        TextView txtTitle = findViewById(R.id.txtSongTitle);
        txtTitle.setText(title);

        TextView txtArtiste = findViewById(R.id.txtArtist);
        txtArtiste.setText(artiste);

        ImageView iCoverArt = findViewById(R.id.imgCoverArt);
        iCoverArt.setImageResource(drawable);
    }
    //This retrieves data from selected song and changes the image and text to correspond to the selected song.

    public void playSong(String songUrl) {
        try {
            player.reset();
            player.setDataSource(songUrl);
            player.prepare();
            player.start();
            gracefullyStopsWhenMusicEnds();

            btnPlayPause.setText("PAUSE");
            setTitle(title);

            player.seekTo(0);
            System.out.println("This is the song Duration:" + player.getDuration());
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        player.seekTo(progress);
                        seekBar.setProgress(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        gracefullyStopsWhenMusicEnds();

    }

    public void playOrPauseMusic(View view) {
        //this method changes the play/pause button according to whether song is playing or not.
        if (player.isPlaying()) {
            player.pause();
            btnPlayPause.setText("PLAY");
        } else {
            player.start();
            btnPlayPause.setText("PAUSE");
        }
    }

    private void gracefullyStopsWhenMusicEnds() {
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if(repeatFlag){
                    playOrPauseMusic(null);
                    //When repeatFLag is true which is when loop is activated, repeats current song.
                }
                else {
                    btnPlayPause.setText("PLAY");
                    //else nothing plays and play/pause button becomes paused.
                }

                seekbar.setProgress(0);
                //reset he seekbar back to the start when music has ended
            }
        });
    }

    public void playNext(View view) {
        currentIndex = songCollection.getNextSong(currentIndex);
        Toast.makeText(this, "After clicking playNext , \n the current index of this song \n" + "in the SongCollection array is now :" + currentIndex,
                Toast.LENGTH_LONG).show();
        Log.d("Temasek", "After playNext, the index is now :" + currentIndex);
        displaySongBasedOnIndex(currentIndex);
        playSong(filelink);
    }

    public void playPrevious(View view) {
        currentIndex = songCollection.getPrevSong(currentIndex);
        Toast.makeText(this, "After clicking playPrevious, \n the current index of this song \n" + "in the SongCollection array is now :" + currentIndex,
                Toast.LENGTH_LONG).show();
        Log.d("Temasek", "After playPrevious, the index is now :" + currentIndex);
        displaySongBasedOnIndex(currentIndex);
        playSong(filelink);
    }

    @Override
    public void onBackPressed(){
        if (player != null){
            player.release(); //Clear the player

            if(handler != null)
                handler.removeCallbacksAndMessages(null); //Clear the handler
        }

        super.onBackPressed();
        player.release();
    }

    public void updateSeekbar(){
        int currPos = player.getCurrentPosition();
        //Storing the current position value into an integer variable(currPos)
        seekbar.setProgress(currPos);
        //Passing in the value of currPos so that the seekbar gets updated concurrently when the music is playing.
        runnable = new Runnable(){
            @Override
            public void run(){
                updateSeekbar();
            }
        };
        handler.postDelayed(runnable, 1000);
    }


    public void repeatSong(View view) {
        //this method changes the repeat icon when pressed.
        if(repeatFlag){
            repeatBtn.setBackgroundResource(R.drawable.repeat);
            //Changes the button to be unhighlighted (loop function not activated yet) when pressed.
        }
        else{
            repeatBtn.setBackgroundResource(R.drawable.repeated);
            //changes the button to be highlighted when pressed.
        }
        repeatFlag = !repeatFlag;
        //Changes the true or false value of repeatFlag so as to create a cycle for the button to be able to change its background.
    }

    public void shuffleSong(View view) {
        //this method changes the shuffle icon when pressed.
        if(shuffleFlag){
            shuffleBtn.setBackgroundResource(R.drawable.shuffles);
            //Changes the button to be unhighlighted (shuffle function not activated yet) when pressed.
            songCollection = new SongCollection();
            //resets the list of sing to original line up.
        }
        else{
            shuffleBtn.setBackgroundResource(R.drawable.shuffled);
            //changes the button to be highlighted when pressed.
            Collections.shuffle(shuffleList);
            //shuffles the list of sing.
            shuffleList.toArray(songCollection.songs);
            //passing in the shuffleList's order of songs into the actual array so as to have a result that the songs are shuffled.

        }
        shuffleFlag = !shuffleFlag;
        //Changes the true or false value of shuffleFlag so as to create a cycle for the button to be able to change its background.
    }
}