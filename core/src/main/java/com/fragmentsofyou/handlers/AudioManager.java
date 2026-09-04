package com.fragmentsofyou.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager{

    private Music musicaAmbiente;
    private Sound sonidoDestello;
    private Sound sonidoDisparo;

    public AudioManager() {
        musicaAmbiente = Gdx.audio.newMusic(Gdx.files.internal("musica/Twin Peaks Theme (Instrumental).mp3"));
        musicaAmbiente.setLooping(true);
        musicaAmbiente.setVolume(0.06f);

        sonidoDestello = Gdx.audio.newSound(Gdx.files.internal("sonidos/destello.mp3"));
        sonidoDisparo = Gdx.audio.newSound(Gdx.files.internal("sonidos/disparo.mp3"));
    }

    public void iniciarMusica() {
        if (musicaAmbiente != null && !musicaAmbiente.isPlaying()) {
            musicaAmbiente.play();
        }
    }

    public void pausarMusica() {
        if (musicaAmbiente != null && musicaAmbiente.isPlaying()) {
            musicaAmbiente.pause();
        }
    }

    public void playDestello() {
        sonidoDestello.play(1.0f);
    }

    public void playDisparo() {
        sonidoDisparo.play(1.0f);
    }

    public void dispose() {
        if (musicaAmbiente != null) {
            musicaAmbiente.stop();
            musicaAmbiente.dispose();
        }
        if (sonidoDestello != null) sonidoDestello.dispose();
        if (sonidoDisparo != null) sonidoDisparo.dispose();
    }
}
