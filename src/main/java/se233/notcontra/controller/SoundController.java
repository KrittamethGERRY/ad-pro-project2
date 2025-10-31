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
	private AudioClip thirdStageMusic;
	private AudioClip playerDieSound;
	private AudioClip proneSound;
	private AudioClip winSound;
	private AudioClip javaDieSound;
	private AudioClip canDieSound;
	private AudioClip javaAttackSound;
	private AudioClip startTheme;
	private AudioClip loseSound;
	private AudioClip respawnSound;
	private AudioClip dropDownSound;
	private AudioClip rdArmDestroyedSound;
	private AudioClip rdEyeDestroyedSound;
	
    public void stopAllSounds() {
        if (shootSound != null) shootSound.stop();
        if (jumpSound != null) jumpSound.stop();
        if (enemyDieSound != null) enemyDieSound.stop();
        if (explosionSound != null) explosionSound.stop();
        if (metalSound != null) metalSound.stop();
        if (metalSound2 != null) metalSound2.stop();
        if (pickUpItemSound != null) pickUpItemSound.stop();
        if (cannonSound != null) cannonSound.stop();
        if (screamingSound != null) screamingSound.stop();
        if (firstStageMusic != null) firstStageMusic.stop();
        if (secondStageMusic != null) secondStageMusic.stop();
        if (thirdStageMusic != null) thirdStageMusic.stop();
        if (playerDieSound != null) playerDieSound.stop();
        if (proneSound != null) proneSound.stop();
        if (winSound != null) winSound.stop();
        if (javaDieSound != null) javaDieSound.stop();
        if (canDieSound != null) canDieSound.stop();
        if (javaAttackSound != null) javaAttackSound.stop();
        if (startTheme != null) startTheme.stop();
        if (loseSound != null) loseSound.stop();
        if (respawnSound != null) respawnSound.stop();
        if (dropDownSound != null) dropDownSound.stop();
        if (rdArmDestroyedSound != null) rdArmDestroyedSound.stop();
        if (rdEyeDestroyedSound != null) rdEyeDestroyedSound.stop();
    }
	private SoundController() {
		shootSound = new AudioClip(Launcher.class.getResource("assets/Sounds/gunshot.mp3").toString());
		jumpSound = new AudioClip(Launcher.class.getResource("assets/Sounds/jumpSound.mp3").toString());
		enemyDieSound = new AudioClip(Launcher.class.getResource("assets/Sounds/dieSound.mp3").toString());
		explosionSound = new AudioClip(Launcher.class.getResource("assets/Sounds/explosionSound.mp3").toString());
		metalSound = new AudioClip(Launcher.class.getResource("assets/Sounds/metalHitSound1.mp3").toString());
		metalSound2 = new AudioClip(Launcher.class.getResource("assets/Sounds/metalHitSound2.mp3").toString());
		pickUpItemSound = new AudioClip(Launcher.class.getResource("assets/Sounds/pickUpItemSound.mp3").toString());
		screamingSound = new AudioClip(Launcher.class.getResource("assets/Sounds/manScreamingSound.mp3").toString());
		cannonSound = new AudioClip(Launcher.class.getResource("assets/Sounds/cannonSound.mp3").toString());
		firstStageMusic = new AudioClip(Launcher.class.getResource("assets/Sounds/marioTheme.mp3").toString());
		secondStageMusic = new AudioClip(Launcher.class.getResource("assets/Sounds/undergroundTheme.mp3").toString());
		thirdStageMusic = new AudioClip(Launcher.class.getResource("assets/Sounds/lastBattleTheme.mp3").toString());
		playerDieSound = new AudioClip(Launcher.class.getResource("assets/Sounds/playerDieSound.mp3").toString());
		proneSound = new AudioClip(Launcher.class.getResource("assets/Sounds/proneSound.mp3").toString());
		winSound = new AudioClip(Launcher.class.getResource("assets/Sounds/winSound.mp3").toString());
		javaDieSound = new AudioClip(Launcher.class.getResource("assets/Sounds/javaDieSound.mp3").toString());
		canDieSound = new AudioClip(Launcher.class.getResource("assets/Sounds/flyingDieSound.mp3").toString());
		javaAttackSound = new AudioClip(Launcher.class.getResource("assets/Sounds/javaAttackSound.mp3").toString());
		startTheme = new AudioClip(Launcher.class.getResource("assets/Sounds/startingTheme.mp3").toString());
		loseSound = new AudioClip(Launcher.class.getResource("assets/Sounds/loseSound.mp3").toString());
		respawnSound = new AudioClip(Launcher.class.getResource("assets/Sounds/respawnSound.mp3").toString());
		dropDownSound = new AudioClip(Launcher.class.getResource("assets/Sounds/dropDownSound.mp3").toString());
		rdEyeDestroyedSound = new AudioClip(Launcher.class.getResource("assets/Sounds/rdEyeDestroyedSound.mp3").toString());
		rdArmDestroyedSound = new AudioClip(Launcher.class.getResource("assets/Sounds/rdArmDestroyedSound.mp3").toString());

		metalSound.setVolume(0.1);
		metalSound2.setVolume(0.1);
		rdEyeDestroyedSound.setVolume(0.5);
		rdArmDestroyedSound.setVolume(0.5);
		shootSound.setVolume(0.25);
		firstStageMusic.setVolume(0.35);
		secondStageMusic.setVolume(0.35);
		thirdStageMusic.setVolume(0.20);
		startTheme.setVolume(0.15);

		respawnSound.setVolume(0.15);
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
	public void playThirdStageMusic() {
		thirdStageMusic.play();
	}
	public void playPlayerDieSound() {
		playerDieSound.play();
	}
	public void playProneSound() {
		proneSound.play();
	}
	
	public void playWinSound() {
		winSound.play();
	}
	
	public void playJavaDieSound() {
		javaDieSound.play();
	}
	
	public void playCanDieSound() {
		canDieSound.play();
	}
	
	public void playJavaAttackSound() {
		javaAttackSound.play();
	}
	
	public void playStartTheme() {
		startTheme.play();
	}
	public void playLoseSound() {
		loseSound.play();
	}
	
	public void playRespawnSound() {
		respawnSound.play();
	}
	
	public void playDropDownSound() {
		dropDownSound.play();
	}
	
	public void playRDEyeDestroyedSound() {
		rdEyeDestroyedSound.play();
	}
	
	public void playRDArmDestroyedSound() {
		rdArmDestroyedSound.play();
	}
	public static SoundController getInstance() {
		if (instance == null) return new SoundController(); 
		return instance;
	}
}
