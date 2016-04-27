package com.example.mateonorena.apiyoutube;

import java.util.ArrayList;

/**
 * Created by mateo.norena on 27/04/16.
 */
public class Config {

    // Google Console APIs developer key
// Reemplace la clave por la clave personal obtenida
    private String DEVELOPER_KEY = "AIzaSyCL7F1-tge5K1utfJLj682BTsrQC2llQpk";
    // ID del video de YouTube, está conformado por 11 caracteres
    private String YOUTUBE_VIDEO_CODE = "FJzSH6_zpW0";
    // Lista de videos
    private ArrayList<String> videos = new ArrayList<String>() {{
        add("a4NT5iBFuZs");
        add("7YcW25PHnAA");
        add("pVAMOielOJQ");
        add("WtsS8sEzEeY");
    }};


    public String getDEVELOPER_KEY() {
        return DEVELOPER_KEY;
    }

    public void setDEVELOPER_KEY(String DEVELOPER_KEY) {
        this.DEVELOPER_KEY = DEVELOPER_KEY;
    }

    public String getYOUTUBE_VIDEO_CODE() {
        return YOUTUBE_VIDEO_CODE;
    }

    public void setYOUTUBE_VIDEO_CODE(String YOUTUBE_VIDEO_CODE) {
        this.YOUTUBE_VIDEO_CODE = YOUTUBE_VIDEO_CODE;
    }

    public ArrayList<String> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<String> videos) {
        this.videos = videos;
    }
}
