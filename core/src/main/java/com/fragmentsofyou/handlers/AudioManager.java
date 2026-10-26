package com.fragmentsofyou.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager{

    private Music musicaAmbiente;
    private Sound sonidoDestello;
    private Sound sonidoDisparo;
    private Music musicaAngel;
    public Sound agarrarObjeto;
    public Sound bum;
    public Sound disparo;
    public Sound sonidoRaro;
    private Music tvVieja;
    public Sound Pasos;

    public AudioManager() {
        musicaAmbiente = Gdx.audio.newMusic(Gdx.files.internal("musica/Twin Peaks Theme (Instrumental).mp3"));
        musicaAmbiente.setLooping(true);
        musicaAmbiente.setVolume(0.06f);

        musicaAngel = Gdx.audio.newMusic(Gdx.files.internal("musica/Massive Attack - Angel Instrumental (1998) - CR055FIRE.mp3"));
        musicaAngel.setLooping(true);
        musicaAngel.setVolume(0.06f);

        agarrarObjeto = Gdx.audio.newSound(
            Gdx.files.internal("sonidos/agarrarObjeto.wav")
        );

        bum = Gdx.audio.newSound(
            Gdx.files.internal("sonidos/bumm.mp3")
        );

        disparo = Gdx.audio.newSound(
            Gdx.files.internal("sonidos/disparo.mp3")
        );

        sonidoRaro = Gdx.audio.newSound(
            Gdx.files.internal("sonidos/Drone_1.wav")
        );

        tvVieja = Gdx.audio.newMusic(
            Gdx.files.internal("sonidos/Old TV static - CSX2000.mp3")
        );
        tvVieja.setLooping(true);
        tvVieja.setVolume(0.04f);

        Pasos = Gdx.audio.newSound(
            Gdx.files.internal("sonidos/Thick Carpet Step_6.wav")
        );

        sonidoDestello = Gdx.audio.newSound(Gdx.files.internal("sonidos/destello.mp3"));
        sonidoDisparo = Gdx.audio.newSound(Gdx.files.internal("sonidos/disparo.mp3"));
    }

    public void iniciarMusicaAmbiente() {
        if (musicaAmbiente != null && !musicaAmbiente.isPlaying()) {
            musicaAmbiente.play();
        }
    }
    public void iniciarMusicaAngel() {
        if (musicaAngel!= null && !musicaAngel.isPlaying()) {
            musicaAngel.play();
        }
    }


    public void pausarMusicaAmbiente() {
        if (musicaAmbiente != null && musicaAmbiente.isPlaying()) {
            musicaAmbiente.pause();
        }
    }

    public void iniciarTvVieja() {
        if (tvVieja != null && !tvVieja.isPlaying()) {
            tvVieja.setLooping(true);
            tvVieja.play();
        }
    }

    public void pausarTvVieja() {
        if (tvVieja != null && tvVieja.isPlaying()) {
            tvVieja.pause();
        }
    }

    public void detenerTvVieja() {
        if (tvVieja != null && tvVieja.isPlaying()) {
            tvVieja.stop();
        }
    }

    public void pausarMusicaAngel() {
        if (musicaAngel != null && musicaAngel.isPlaying()) {
            musicaAngel.pause();
        }
    }


    public void playDestello() {
        sonidoDestello.play(0.3f);
    }

    public void playDisparo() {
        sonidoDisparo.play(0.3f);
    }

    public void dispose() {
        if (musicaAmbiente != null) {
            musicaAmbiente.stop();
            musicaAmbiente.dispose();
        }
        if(musicaAngel!=null) {
            musicaAngel.stop();
            musicaAngel.dispose();
        }
        if (sonidoDestello != null) sonidoDestello.dispose();
        if (sonidoDisparo != null) sonidoDisparo.dispose();
    }
}
