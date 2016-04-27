package com.example.mateonorena.apiyoutube;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{


    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private Config config;
    // YouTube player view
    private YouTubePlayerView youTubeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = new Config();
        setContentView(R.layout.activity_main);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        // Inicializa el reproductor de video con la llave de desarrolador
        youTubeView.initialize(config.getYOUTUBE_VIDEO_CODE(), (YouTubePlayer.OnInitializedListener) this);
    }

    // Metodo para mostrar los mensajes de error
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        // loadVideo() para reproducir automaticamente el video
        player.loadVideo(config.getYOUTUBE_VIDEO_CODE());
        //player.loadVideos(config.getVideos());
        // Oculta la mayoria de las herramientas excepto la reproduccion y pausa
        //player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        // Oculta todas las herramientas de la interfaz
        // player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            youTubeView.initialize(config.getYOUTUBE_VIDEO_CODE(), this);
        }
    }
}
