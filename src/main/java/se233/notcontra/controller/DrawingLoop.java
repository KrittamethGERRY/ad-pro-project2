package se233.notcontra.controller;

import java.util.List;

import se233.notcontra.model.Boss.WallBoss;
import se233.notcontra.model.Enums.BulletOwner;
import se233.notcontra.model.Enums.EnemyType;
import se233.notcontra.model.Enums.ShootingDirection;
import se233.notcontra.view.CheatManager;
import se233.notcontra.view.GameStages.FirstStage;
import se233.notcontra.view.GameStages.GameStage;
import se233.notcontra.view.GameStages.SecondStage;
import se233.notcontra.view.GameStages.ThirdStage;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import se233.notcontra.Launcher;
import se233.notcontra.model.*;

public class DrawingLoop implements Runnable {	
	public static List<Effect> effects = new ArrayList<>();
	private GameStage gameStage;
	private int frameRate;
	private float interval;
	private boolean running;
	private int itemSpawnTimer;
	boolean isWin;
	
	public DrawingLoop(GameStage gameStage) {
		this.isWin = false;
		this.gameStage = gameStage;
		frameRate = 60;
		interval = 1000f / frameRate;
		running = true;
		itemSpawnTimer = 500;
	}
	
	public void checkAllCollisions(Player player) {
		player.checkHighestJump();
		player.checkStageBoundaryCollision();
		player.checkPlatformCollision(gameStage.getPlatforms());
		player.checkItemCollision(gameStage);
		player.updateTimer();
		player.resetHitBoxHeight();
		checkPlayerEnemyCollision();
		for (Enemy enemy: GameLoop.enemies) {
			if (enemy.getType() == EnemyType.PATROL) {
				((PatrolEnemy) enemy).updateAI(player);
				((PatrolEnemy) enemy).repaint();
				((PatrolEnemy) enemy).checkReachHighest();
				((PatrolEnemy) enemy).checkPlatformCollision(gameStage.getPlatforms());
				((PatrolEnemy) enemy).checkReachGameWall();
			}
		}
		if (gameStage.getItem() != null) {
			gameStage.getItem().update(gameStage.getPlatforms());
		}
	}

	public void paint(Player player) {
		player.repaint();
	}

	private void paintBullet(List<Bullet> bullets, ShootingDirection direction) {
		Iterator<Bullet> iterator = bullets.iterator();
		while (iterator.hasNext()) {
			Bullet bullet = iterator.next();
			bullet.move();
			
			boolean shouldRemove = false;

			if (bullet.isOutOfBounds(GameStage.WIDTH, GameStage.HEIGHT )) {
				shouldRemove = true;
			}
			
			// Enemies collision with bullet
			for (Enemy enemy : gameStage.getEnemies()) {
				if (enemy.isAlive() && enemy.getBoundsInParent().intersects(bullet.getBoundsInParent()) && bullet.getOwner() == BulletOwner.PLAYER) {
					enemy.takeDamage(500, gameStage.getBoss());
					shouldRemove = true;
				}
				
				
				// Boss' children enemy
				if (enemy.isAlive() &&
						gameStage.getBoss().localToParent(enemy.getBoundsInParent()).intersects(bullet.getBoundsInParent())
						&& bullet.getOwner() == BulletOwner.PLAYER) {
					// Add effect after the bullet hit the enemies
					Effect explosion = new Effect(ImageAssets.EXPLOSION_IMG, 8, 8, 1, bullet.getxPos() - 16, bullet.getyPos() - 16, 64, 64);
					effects.add(explosion);
					javafx.application.Platform.runLater(() -> {
						gameStage.getChildren().add(explosion);
					});
					enemy.takeDamage(500, gameStage.getBoss());


					if (enemy.getType() == EnemyType.TURRET) {
						if (WallBoss.totalTurret <= 0 && !gameStage.getBoss().getWeakPoints().isEmpty()) {
							Enemy core = gameStage.getBoss().getWeakPoints().getFirst();
                            Platform.runLater(() -> {
                            	GameLoop.enemies.add(core);
                            });
						}
					}
					
					if (enemy.getType() == EnemyType.WALL) {
						
					}
					shouldRemove = true;
					
					break;
				}
			}
			
			// Player collisions with bullet
			
			Bounds playerBounds = gameStage.getPlayer().localToParent(gameStage.getPlayer().getHitBox().getBoundsInParent());
			if (playerBounds.intersects(bullet.getBoundsInParent())
					&& bullet.getOwner() != BulletOwner.PLAYER
					&& !gameStage.getPlayer().getTankBuster()
					&& !gameStage.getPlayer().isDying()) {
                if (!CheatManager.getInstance().areCheatsActive()) {
                    if (Player.spawnProtectionTimer <= 0) {
                        gameStage.getPlayer().die();
                        shouldRemove = true;
                    }
                }
			}
			Platform.runLater(this::updateLives);
			
			// Remove bullet
			if (shouldRemove) {
				iterator.remove();
				Platform.runLater(() -> gameStage.getChildren().remove(bullet));
			}

			Platform.runLater(this::updateScore);
		}
	}

	private void checkPlayerEnemyCollision() {
		if (gameStage.getPlayer().getTankBuster() || gameStage.getPlayer().isDying()) {
			return;
		}

		Bounds playerBounds = gameStage.getPlayer().localToParent(
				gameStage.getPlayer().getHitBox().getBoundsInParent()
		);

		List<Enemy> enemiesCopy = new ArrayList<>(GameLoop.enemies);

		for (Enemy enemy : enemiesCopy) {
			if (enemy.isAlive() && enemy.getType() == EnemyType.FLYING) {
				Bounds enemyBounds = gameStage.getBoss().localToParent(enemy.getBoundsInParent());

				if (enemyBounds.intersects(playerBounds)) {
					if (!CheatManager.getInstance().areCheatsActive()) {
						gameStage.getPlayer().die();
					}

					clearAllEnemies();

					Platform.runLater(this::updateLives);
				}
			}
			
			// Player Collided with PATROL enemy
			if (enemy.isAlive() && (enemy.getType() == EnemyType.PATROL)) {
				Bounds enemyBounds = enemy.localToParent(((PatrolEnemy) enemy).getBoundsInLocal());
				if (enemyBounds.intersects(playerBounds) && Player.spawnProtectionTimer <= 0) {
					gameStage.getPlayer().die();
				}
			}
		}
	}
	
	private void paintEffects(List<Effect> effects) {
        Iterator<Effect> iterator = effects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            effect.tick();
            if (effect.isFinished()) {
                iterator.remove();
                Platform.runLater(() -> gameStage.getChildren().remove(effect));
            }
        }
    }

	private void clearAllEnemies() {
		List<Enemy> enemiesToRemove = new ArrayList<>();

		for (Enemy enemy : GameLoop.enemies) {
			if (enemy.getType() == EnemyType.FLYING) {
				// Add explosion effect for each removed enemy
				Bounds enemyBounds = gameStage.getBoss().localToParent(enemy.getBoundsInParent());
				Effect explosion = new Effect(
						ImageAssets.EXPLOSION_IMG, 8, 8, 1,
						(int) enemyBounds.getCenterX() - 32,
						(int) enemyBounds.getCenterY() - 32,
						64, 64
				);
				effects.add(explosion);
				Platform.runLater(() -> {gameStage.getChildren().add(explosion);});

				enemiesToRemove.add(enemy);
			}
		}

		// Remove all marked enemies
		for (Enemy enemy : enemiesToRemove) {
			GameLoop.enemies.remove(enemy);
			Platform.runLater(() -> {
				gameStage.getBoss().getChildren().remove(enemy);
			});
		}
	}

	private void updateScore() {
		gameStage.getScoreLabel().setText("Score: " + String.format("%06d", GameLoop.getScore()));
	}
	
	private void updateLives() {
		gameStage.getLivesLabel().setText("Lives: " + gameStage.getPlayer().getLives());
	}

	// Update boss in each stage
	private void updateBoss() {
		if (gameStage instanceof FirstStage) {
			gameStage.getPlayer().isCollided(gameStage);
			if (GameStage.totalMinions <= 0 && !GameStage.bossPhase) {
				GameLoop.enemies.addAll(WallBoss.getTurrets());
				GameStage.bossPhase = true;
			}
			if (GameStage.bossPhase) {
                if (gameStage.getBoss() != null && gameStage.getBoss().isAlive()) {
                    gameStage.getBoss().update();
                }
			}
			if (gameStage.getBoss().getWeakPoints().isEmpty() && !isWin) {
				isWin = true;
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("You Win!");
				alert.setContentText("Continue to the next stage?");
				alert.showAndWait();
				if (alert.getResult() == ButtonType.OK) {
					Launcher.changeStage(1);
				} else {
					Launcher.exitToMenu();
				}
			}
		} else if (gameStage instanceof SecondStage) {
			gameStage.getPlayer().isCollided(gameStage);
			if (GameStage.totalMinions <= 0 && !GameStage.bossPhase) {
				GameStage.bossPhase = true;
			}
			if (GameStage.bossPhase) {
				if (gameStage.getBoss() != null && gameStage.getBoss().isAlive()) {
					gameStage.getBoss().update();
				}
			}
			if (gameStage.getBoss().getWeakPoints().isEmpty() && !isWin) {
				isWin = true;
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("You Win!");
				alert.setContentText("Continue to the next stage?");
				alert.showAndWait();
				if (alert.getResult() == ButtonType.OK) {
					Launcher.changeStage(2);
				} else {
					Launcher.exitToMenu();
				}
			}
		} else if (gameStage instanceof ThirdStage) {
			if (GameStage.totalMinions <= 0 && !GameStage.bossPhase) {
				//
				//
				GameStage.bossPhase = true;
			}
			if (GameStage.bossPhase) {
				if (gameStage.getBoss() != null && gameStage.getBoss().isAlive()) {
					gameStage.getBoss().update();
				}
			}
			if (gameStage.getBoss().getWeakPoints().isEmpty() && !isWin) {
				isWin = true;
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("You Win!");
				alert.setContentText("Continue to the next stage?");
				alert.showAndWait();
				if (alert.getResult() == ButtonType.OK) {
					Launcher.changeStage(1);
				} else {
					Launcher.exitToMenu();
				}
			}
		}
	}
	
	private void updateEnemies() {
		if (!GameLoop.isPaused) {
			Iterator<Enemy> iterator = GameLoop.enemies.iterator();
			while (iterator.hasNext()) {
				Enemy enemy = iterator.next();
				if (enemy.isAlive()) {
					enemy.updateWithPlayer(gameStage.getPlayer(), gameStage);
				} else if (!(enemy.getType() == EnemyType.TURRET)){
					// Kill remove enemy from the stage
					iterator.remove();
					javafx.application.Platform.runLater(() -> {
						gameStage.getBoss().getChildren().remove(enemy);
					});
					if (enemy.getType() == EnemyType.PATROL) {
						Platform.runLater(() -> gameStage.getChildren().remove(enemy));
					}
				} 
			}
		}
	}
	
	public void stop() {
		running = false;
	}
	
	public void updateItemSpawnTimer() {
		if (itemSpawnTimer > 0) {
			itemSpawnTimer--;
			if (itemSpawnTimer == 0) {
				gameStage.spawnItem();
			}
			return;
		}
		itemSpawnTimer = 500;
		// RANDOMLY SPAWN THE ITEM WITH THE SAME Y BUT DIFFER X
	}

    @Override
    public void run() {
        while (running) {
            float startTime = System.currentTimeMillis();

                javafx.application.Platform.runLater(() -> {
                    if (GameLoop.isPaused || !running) {
                        return;
                    }
                    checkAllCollisions(gameStage.getPlayer());
                    paint(gameStage.getPlayer());
                    paintEffects(effects);
                    paintBullet(GameLoop.bullets, GameLoop.shootingDir);
                    updateEnemies();
                    updateBoss();
                    updateItemSpawnTimer();
                });

                float elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime < interval) {
                    try {
                        Thread.sleep((long) (interval - elapsedTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep((long) (interval - (interval % elapsedTime)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                
            }
        }
    }
}