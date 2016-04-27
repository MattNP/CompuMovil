package com.example.mateonorena.reproductor;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnPreparedListener {
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean pause,video=false;
    private String path;
    private int savePos = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceView.setVisibility(View.INVISIBLE);
        path="android.resource://"+getPackageName()+"/" + R.raw.cancion;
    }

    public void play(View v) {//Reproducir el video o audio
        if (mediaPlayer != null) {//Si existe el objeto del reproductor
            if (pause) {
                mediaPlayer.start();//Iniciamos la reproducción si estaba pausada
            }
        }
        else{//El reproductor no habia sido creado entonces lo creamos
            playArchivo();
        }
    }

    public void pause(View v) {
        if (mediaPlayer != null) {//Control de excepciones
            pause = true;//Señalamos la pausa
            mediaPlayer.pause();//Pausamos el reproductor
        }
    }

    public void stop(View v) {
        if (mediaPlayer != null) {//Control de excepciones
            mediaPlayer.release();//Vaciamos el buffer
            mediaPlayer=null;//Restauramos el reproductor
        }
    }

    public void music(View v){//Reproducir música
        stop(v);//Detenemos el reproductor actual
        surfaceView.setVisibility(View.INVISIBLE);//Escondemos la ventana de video
        video=false;
        path="android.resource://"+getPackageName()+"/" + R.raw.cancion;//Asignamos la ruta del recurso de audio que vamos a utilizar
    }

    public void video(View v){//Reproducir video
        stop(v);//Detenemos el reproductor actual
        surfaceView.setVisibility(View.VISIBLE);//Mistramos la ventana de video
        video=true;
        path="android.resource://"+getPackageName()+"/" + R.raw.video;//Asignamos la ruta del recurso de video que vamos a utilizar
    }

    private void playArchivo() {//Reproducción como tal
        if(video) {//Video
            try {
                mediaPlayer = new MediaPlayer();//Inicializamos el reproductor
                mediaPlayer.setDataSource(this, Uri.parse(path));//Asignamos el recurso de video
                mediaPlayer.setDisplay(surfaceHolder);//Llevamos el display o visualización del reproductor a la surface que tenemos en pantalla
                mediaPlayer.prepare();//Cargamos el buffer
                // mediaPlayer.prepareAsync(); Para streaming
                //Método que debemos hacer para el surfaceview pero son para eventos y acciones con la ventana de video, para nuestro caso no hacemos nada con estos
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//Le seteamos al reproductor el tipo de salida de sonido
                mediaPlayer.seekTo(savePos);//Le seteamos al reproductor el tiempo en el cual queremos que se posicione la reproduccion
                mediaPlayer.start();//Iniciamos la reproduccion
                Toast.makeText(this, "Reproduciendo video", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        }
        else{
            try {//Música
                mediaPlayer = new MediaPlayer();//Inicializamos el reproductor
                mediaPlayer.setDataSource(this, Uri.parse(path));//Asignamos el recurso de audio
                mediaPlayer.prepare();
                // mediaPlayer.prepareAsync(); Para streaming
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//Le seteamos al reproductor el tipo de salida de sonido
                mediaPlayer.seekTo(savePos);//Le seteamos al reproductor el tiempo en el cual queremos que se posicione la reproduccion
                mediaPlayer.start();//Iniciamos la reproduccion
                Toast.makeText(this, "Reproduciendo música", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        }
    }

    public void onPrepared(MediaPlayer mediaplayer) {//Convertirmos nuestro video para la resolución del surfaceview que tenemos en la interfaz
        int mVideoWidth = mediaPlayer.getVideoWidth();
        int mVideoHeight = mediaPlayer.getVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
        }
    }

    @Override
    public void onPause() {//Para liberar el reproductor al salir o cambiar de aplicación
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onPause();
    }
}
