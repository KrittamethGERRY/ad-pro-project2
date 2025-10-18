package se233.notcontra.controller;

import javafx.scene.media.AudioClip;
import se233.notcontra.Launcher;

public class SoundController {
	private static SoundController instance;
	private AudioClip shootSound;
	private AudioClip jumpSound;
	private AudioClip enemyDieSound;
	private AudioClip explosionSound;
	private AudioClip metalSound;
	private AudioClip metalSound2;
	private AudioClip pickUpItemSound;
	private AudioClip screamingSound;
	private AudioClip cannonSound;
	private AudioClip firstStageMusic;
	private AudioClip secondStageMusic;
	private AudioClip playerDieSound;
	private AudioClip proneSound;
    public void stopAllSounds() {
        if (shootSound != null) shootSound.stop();
        if (jumpSound != null) jumpSound.stop();
        if (enemyDieSound != null) enemyDieSound.stop();
        if (explosionSound != null) explosionSound.stop();
        if (metalSound != null) metalSound.stop();
        if (metalSound2 != null) metalSound2.stop();
        if (pickUpItemSound != null) pickUpItemSound.stop();
        if (cannonSound != null) cannonSound.stop();
        if (firstStageMusic != null) firstStageMusic.stop();
        if (secondStageMusic != null) secondStageMusic.stop();
        if (playerDieSound != null) playerDieSound.stop();
        if (proneSound != null) proneSound.stop();

    }
	private SoundController() {
		shootSound = new AudioClip(Launcher.class.getResource("assets/Sounds/gunshot.mp3").toString());
		jumpSound = new AudioClip(Launcher.class.getResource("assets/Sounds/jumpSound.mp3").toString());
		enemyDieSound = new AudioClip(Launcher.class.getResource("assets/Sounds/dieSound.mp3").toString());
		explosionSound = new AudioClip(Launcher.class.getResource("assets/Sounds/explosionSound.mp3").toString());
		metalSound = new AudioClip(Launcher.class.getResource("assets/Sounds/metalHitSound1.mp3").toString());
		metalSound2 = new AudioClip(Launcher.class.getResource("assets/sounds/metalHitSound2.mp3").toString());
		pickUpItemSound = new AudioClip(Launcher.class.getResource("assets/Sounds/pickUpItemSound.mp3").toString());
		screamingSound = new AudioClip(Launcher.class.getResource("assets/Sounds/manScreamingSound.mp3").toString());
		cannonSound = new AudioClip(Launcher.class.getResource("assets/Sounds/cannonSound.mp3").toString());
		firstStageMusic = new AudioClip(Launcher.class.getResource("assets/Sounds/marioTheme.mp3").toString());
		secondStageMusic = new AudioClip(Launcher.class.getResource("assets/Sounds/undergroundTheme.mp3").toString());
		playerDieSound = new AudioClip(Launcher.class.getResource("assets/Sounds/playerDieSound.mp3").toString());
		proneSound = new AudioClip(Launcher.class.getResource("assets/Sounds/proneSound.mp3").toString());


		metalSound.setVolume(0.2);
		metalSound2.setVolume(0.2);
		shootSound.setVolume(0.25);
		firstStageMusic.setVolume(0.35);
		secondStageMusic.setVolume(0.35);
		jumpSound.setVolume(0.1);
		playerDieSound.setVolume(0.35);
	}
	
	public void playShootSound() {
		shootSound.play();
	}
	public void playJumpSound() {
		jumpSound.play();
	}
	
	public void playDieSound() {
		enemyDieSound.play();
	}
	
	public void playExplosionSound() {
		explosionSound.play();
	}
	
	public void playMetalHitSound() {
		metalSound.play();
	}
	
	public void playMetalHitSound2() {
		metalSound2.play();
	}
	
	public void playPickItemSound() {
		pickUpItemSound.play();
	}
	public void playScreamingSound() {
		screamingSound.play();
	}
	
	public void playCannonSound() {
		cannonSound.play();
	}
	public void playFirstStageMusic() {
		firstStageMusic.play();
	}
	public void playSecondStageMusic() {
		secondStageMusic.play();
	}
	public void playPlayerDieSound() {
		playerDieSound.play();
	}
	
	public void playProneSound() {
		proneSound.play();
	}
	

    
	public static SoundController getInstance() {
		if (instance == null) return new SoundController(); 
		return instance;
	}
}
