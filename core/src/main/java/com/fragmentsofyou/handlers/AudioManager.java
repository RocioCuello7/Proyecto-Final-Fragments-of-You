package com.fragmentsofyou.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

    private static final float PASO_VOLUMEN = 0.1f;

    private static final float BASE_AMBIENTE = 0.06f;
    private static final float BASE_ANGEL = 0.06f;
    private static final float BASE_TV = 0.04f;
    private static final float BASE_DESTELLO = 0.3f;
    private static final float BASE_DISPARO = 0.3f;
    private static final float BASE_PASOS = 0.25f;

    private float volumenMaestro = 1.0f;

    private Music musicaAmbiente;
    private Music musicaAngel;
    private Music tvVieja;

    private Sound sonidoDestello;
    private Sound sonidoDisparo;
    private Sound agarrarObjeto;
    private Sound bum;
    private Sound sonidoRaro;
    private Sound pasos;

    private Sound sonidoGolpe;

    public AudioManager() {
        musicaAmbiente = Gdx.audio.newMusic(Gdx.files.internal("musica/Twin Peaks Theme (Instrumental).mp3"));
        musicaAmbiente.setLooping(true);

        musicaAngel = Gdx.audio.newMusic(Gdx.files.internal("musica/Massive Attack - Angel Instrumental (1998) - CR055FIRE.mp3"));
        musicaAngel.setLooping(true);

        tvVieja = Gdx.audio.newMusic(Gdx.files.internal("sonidos/Old TV static - CSX2000.mp3"));
        tvVieja.setLooping(true);

        agarrarObjeto = Gdx.audio.newSound(Gdx.files.internal("sonidos/agarrarObjeto.wav"));
        bum = Gdx.audio.newSound(Gdx.files.internal("sonidos/bumm.mp3"));
        sonidoRaro = Gdx.audio.newSound(Gdx.files.internal("sonidos/Drone_1.wav"));
        pasos = Gdx.audio.newSound(Gdx.files.internal("sonidos/Thick Carpet Step_6.wav"));
        sonidoDestello = Gdx.audio.newSound(Gdx.files.internal("sonidos/destello.mp3"));
        sonidoDisparo = Gdx.audio.newSound(Gdx.files.internal("sonidos/disparo.mp3"));

        sonidoGolpe = Gdx.audio.newSound(Gdx.files.internal("sonidos/golpe.mp3"));

        aplicarVolumen();
    }

    public float getVolumenMaestro() {
        return volumenMaestro;
    }

    public void setVolumenMaestro(float v) {
        volumenMaestro = Math.round(Math.max(0f, Math.min(1f, v)) * 10f) / 10f;
        aplicarVolumen();
    }

    public void subirVolumen() {
        setVolumenMaestro(volumenMaestro + PASO_VOLUMEN);
    }

    public void bajarVolumen() {
        setVolumenMaestro(volumenMaestro - PASO_VOLUMEN);
    }

    private float vol(float base) {
        return base * volumenMaestro;
    }

    private void aplicarVolumen() {
        musicaAmbiente.setVolume(vol(BASE_AMBIENTE));
        musicaAngel.setVolume(vol(BASE_ANGEL));
        tvVieja.setVolume(vol(BASE_TV));
    }

    public void iniciarMusicaAmbiente() {
        if (!musicaAmbiente.isPlaying()) musicaAmbiente.play();
    }

    public void pausarMusicaAmbiente() {
        if (musicaAmbiente.isPlaying()) musicaAmbiente.pause();
    }

    public void iniciarMusicaAngel() {
        if (!musicaAngel.isPlaying()) musicaAngel.play();
    }

    public void pausarMusicaAngel() {
        if (musicaAngel.isPlaying()) musicaAngel.pause();
    }

    public void iniciarTvVieja() {
        if (!tvVieja.isPlaying()) tvVieja.play();
    }

    public void pausarTvVieja() {
        if (tvVieja.isPlaying()) tvVieja.pause();
    }

    public void detenerTvVieja() {
        if (tvVieja.isPlaying()) tvVieja.stop();
    }

    public void detenerMusicas() {
        musicaAmbiente.stop();
        musicaAngel.stop();
        tvVieja.stop();
    }

    public void playGolpe() {
        sonidoGolpe.play(vol(1f));
    }

    public void playDestello() { sonidoDestello.play(vol(BASE_DESTELLO)); }
    public void playDisparo() { sonidoDisparo.play(vol(BASE_DISPARO)); }
    public void playBum() { bum.play(vol(1f)); }
    public void playAgarrarObjeto() { agarrarObjeto.play(vol(1f)); }
    public void playSonidoRaro() { sonidoRaro.play(vol(1f)); }

    public long playPasos() { return pasos.play(vol(BASE_PASOS)); }
    public void detenerPasos(long id) { pasos.stop(id); }

    public void dispose() {
        musicaAmbiente.dispose();
        musicaAngel.dispose();
        tvVieja.dispose();
        sonidoDestello.dispose();
        sonidoDisparo.dispose();
        agarrarObjeto.dispose();
        bum.dispose();
        sonidoRaro.dispose();
        pasos.dispose();
        sonidoGolpe.dispose();
    }
}
