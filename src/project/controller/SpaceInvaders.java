package project.controller;

import project.model.*;
import project.model.Character;
import project.model.projectiles.Projectile;
import project.utils.MyLinkedList;
import project.utils.Point;
import project.view.Board;
import project.model.projectiles.Note;
import project.view.MyAnimation;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;

import static java.awt.image.ImageObserver.ABORT;

public class SpaceInvaders extends MyAnimation {
    // Variables
    private final int boardWidth = 800;
    private final int boardHeight = 600;
    private final int ENEMY_PER_LINE = 5;
    private final int NUMBER_OF_LINE = 4;
    private int gameLevel = 1;
    private int difficultyLevel = 0;  //  using enum DifficultyLevel
    private boolean changeDifficultyLevel = false;
    private int playerScore = 0;
    private final int POINT_PER_ENEMY_KILL = 50;
    private final Group<Enemy> enemies = new Group<>();
    private MyLinkedList<Projectile> enemiesShots = new MyLinkedList<>();
    private MyLinkedList<Projectile> playerShots = new MyLinkedList<>();
    private Player player;
    private Board board;

    ////////////////////////////////////////////////////////
    //                   CONSTRUCTOR                      //
    ////////////////////////////////////////////////////////
    public static void main(String[] args) {
        new SpaceInvaders().launch(true);
    }

    ////////////////////////////////////////////////////////
    //               Drawing The Sprites                  //
    ////////////////////////////////////////////////////////
    @Override
    protected void step() {
        // In Game
        this.player.move(this.player.getVelocity());
        this.moveEnemies();
        this.movePlayerShots();
        this.moveEnemiesShots();
        this.resolveShotsCollisions();

        this.board.setStatisticPanelScore(playerScore);
        this.board.setStatisticPanelLife(this.player.getCurrentHealth());

        this.checkWinLoseStatus();  // check enemy and player. set booleans in Board's class accordingly.
    }


    private void movePlayerShots(){
        for (Projectile playerShot : this.playerShots) {
            playerShot.move(playerShot.getMOVE_UP());
        }
    }

    private void moveEnemiesShots(){
        // Enemies are shooting
        for (Enemy enemy : enemies) {
            this.enemiesShots = enemy.shot(this.enemiesShots, null);  // flat is created in Enemy.shot()
        }
        // Shots are moving
        for (Projectile enemiesShot : this.enemiesShots) {
            enemiesShot.move(enemiesShot.getMOVE_DOWN());
        }
    }

    private void moveEnemies(){
        if( enemies.isEmpty() ){
            System.out.println("[SpaceInvaders][moveEnemies] enemies is empty -> nothing to move!");
            return;
        }
        if( enemies.getVelocity().equals(enemies.get(0).getMOVE_RESET()) ){
            enemies.setVelocity(enemies.get(0).getMoveRight());  // move enemy RIGHT if they are stopped.(i.e. beginning)
        }
        enemies.move(enemies.getVelocity());

        // If the border is met, everybody goes down and direction changed
        if( enemies.getMax() >= (this.boardWidth - enemies.getWidth()) ){
            enemies.move(enemies.get(0).getMoveDown());
            enemies.setVelocity(enemies.get(0).getMoveLeft());
        } else if ( enemies.getMin() <= 0 ){
            enemies.move(enemies.get(0).getMoveDown());
            enemies.setVelocity(enemies.get(0).getMoveRight());
        }
    }


    ////////////////////////////////////////////////////////
    //                         Init                       //
    ////////////////////////////////////////////////////////
    @Override
    public void init(boolean restart) {
        super.init(restart);
        String popupMessage = "";
        String popupTitle = "";
        String popupDifficultyLevel = "difficulty " + DifficultyLevel.values()[this.difficultyLevel].toString().toLowerCase() +
                                      ", level " + this.gameLevel;
        if( restart ){
            this.gameLevel++;  // increase level number
            popupMessage = "You! Yes, you the crazy Player! Kill all the Aliens Ships!\n\n" +
                    "Shoot: SPACE key, and they'll quaver to death\n" +
                    "Move: A/D or Left/Right-Arrow keys\n\n" +
                    "Tip: avoid their black 'b' attack or you'll be flat dead!";
            popupTitle = "THE BATTLE CONTINUES: " + popupDifficultyLevel;
        } else{
            popupMessage = "Welcome back Crazy Player! In case you forgot, Kill those Aliens invaders!\n\n" +
                    "Shoot: SPACE key, and they'll quaver to death\n" +
                    "Move: A/D or Left/Right-Arrow keys\n\n" +
                    "Tip: avoid their black 'b' attack or you'll be flat dead!";
            popupTitle = "THE BATTLE STARTS: " + popupDifficultyLevel;
        }
        int start_choice = JOptionPane.showConfirmDialog(this.board,
                popupMessage, popupTitle, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if( start_choice != 0 ){  // exit
            System.exit(ABORT);
        }

        // PLAYER INIT
        int playerHorizontalInitialPos = 50;
        try {
            this.player = Player.playerBeethoven(playerHorizontalInitialPos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ENEMY INIT
        // Displaying a bunch of enemies
        int spaceBetweenEnemies = this.boardWidth /ENEMY_PER_LINE;

        try {
            MyLinkedList<Enemy> tempEnemies = new MyLinkedList<>();
            for (int r=0; r < ENEMY_PER_LINE; r++){
                for (int i=0; i < NUMBER_OF_LINE; i++){
                    Enemy enemyOne = Enemy.enemyAlienMultiEyes(new Point((1+i)*spaceBetweenEnemies, 50*(r+1) ),
                                                              this.difficultyLevel, this.gameLevel);  //
                    tempEnemies.add(enemyOne);
                }
            }
            this.enemies.addAll(tempEnemies);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // BOARD INIT
        if( restart ){
            this.board.resetBoard(this.boardWidth, this.boardHeight, this.enemiesShots, this.enemies, this.playerShots, this.player);
        } else {
            this.board = new Board(this.boardWidth, this.boardHeight, this.enemiesShots, this.enemies, this.playerShots, this.player);
        }
        this.board.setStatisticPanelLife(this.player.getCurrentHealth());
        this.board.setStatisticPanelScore(0);
        this.board.addKeyListener(new LocalKeyboardListener());

        new MenuListener();
        setDisplay(this.board);
    }

    ////////////////////////////////////////////////////////
    //                     COLLISIONS                     //
    ////////////////////////////////////////////////////////
    /**
     * - Checks if a shot (note) from Player collides with an Enemy.
     *  - Inflicts damage to the enemy
     *  - Removes shot (note) that hit enemy or are out of bound, and dead enemy as well.
     *  - Increments score for each kill
     */
    private void resolveShotsCollisions() {
        MyLinkedList<Projectile> notesToRemove = new MyLinkedList<>();
        // Enemy crash into Player (at the bottom of the screen) => end game
        try {
            if( this.enemies.receiveDamage(this.player, this.player.getCurrentHealth()) ){
                this.board.setPlayerLoses(true);
                this.player.setCurrentHealth(0);
                checkWinLoseStatus();
            }
        } catch (Character.CharacterAlreadyDeadException e) {
            throw new RuntimeException(e);
        }

        // Projectiles From Enemy
        for(Projectile currentProjectile : this.enemiesShots){
            if( currentProjectile == null ) {
                continue;
            }
            if( this.player.receiveDamage(currentProjectile, currentProjectile.getDamage()) ){
                notesToRemove.add(currentProjectile);
            }

            if( currentProjectile.getCoordinate().y() < 0 ||
                currentProjectile.getCoordinate().y() > this.boardHeight ){
                notesToRemove.add(currentProjectile);
            }
        }
        this.enemiesShots.removeAll(notesToRemove);

        // Projectiles From Player
        for(Projectile currentProjectile : this.playerShots){
            // Check if enemies are hit
            if (currentProjectile == null) {
                continue;
            }

            // removes shots that hit the enemy
            try {
                if( enemies.receiveDamage(currentProjectile, currentProjectile.getDamage()) ){
                    notesToRemove.add(currentProjectile);
                    this.playerScore += this.POINT_PER_ENEMY_KILL;
                }
            } catch (Character.CharacterAlreadyDeadException e) {
                throw new RuntimeException(e);
            }

            // remove out-of-screen shots
            if (currentProjectile.getCoordinate().y() < 0 ||
                    currentProjectile.getCoordinate().y() > this.boardHeight) {
                notesToRemove.add(currentProjectile);
            }
        }
        this.playerShots.removeAll(notesToRemove);
        this.enemies.detectAndRemoveDeadEnemies();
    }

    /**
     * Set board's boolean "playerWins" to true if all enemies are dead,
     * or set "playerLoses" to true if player's has zero health point.
     */
    private void checkWinLoseStatus(){
        int player_choice = -1;  // Either: 0 to restart game, 1 to exit, -1 otherwise.
        if( this.enemies.isEmpty() ){
            this.board.setPlayerWins(true);
            player_choice = this.board.setEndMessage();
        }
        if( this.player.isDead() ){
            this.board.setPlayerLoses(true);
            player_choice = this.board.setEndMessage();
        }

        if ( player_choice == 0 || changeDifficultyLevel){
            if(this.player.isDead() || changeDifficultyLevel){          // player lost or changed difficulty level
                this.playerScore = 0;                                   // reset score before new game
                this.gameLevel = 1;
                this.board.setPlayerLoses(false);
            }
            if(this.enemies.isEmpty()){                                  // player won
                // increase difficulty level for next game!
                this.board.setPlayerWins(false);
            }
            // empty all Sprites
            this.player = null;
            this.playerShots.clear();
            this.enemies.clear();
            this.enemiesShots.clear();
            // restart the game in current window/thread
            changeDifficultyLevel = false;                               // reset to false to avoid infinite loop
            this.reset();
        }
    }


    ////////////////////////////////////////////////////////
    //                     LISTENERS                      //
    ////////////////////////////////////////////////////////
    /**
     * Set the menus of the main window
     * source: <a href="https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html">https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html</a>
     */
    private class MenuListener implements ActionListener {

        private MenuListener() {
            JMenuBar menuBar = new JMenuBar();

            // Difficulty Menu: from enum
            JMenu menuDifficulty = new JMenu("Change Difficulty");
            Arrays.asList(DifficultyLevel.values()).forEach(  // source: https://www.baeldung.com/java-enum-iteration
                    level -> {
                        String textItem = String.valueOf(level);  // String.valueOf(level).charAt(0) + String.valueOf(level).substring(1).toLowerCase();
                        JMenuItem tempItem = new JMenuItem(textItem);
                        tempItem.addActionListener(this);
                        menuDifficulty.add(tempItem);
                    }
            );
            menuBar.add(menuDifficulty);
            SpaceInvaders.this.setMenuBar(menuBar);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirmedPlayerChoice = -1;
            String confirmationMessage = "You are about to restart the game with difficulty level ";
            String sourceString = e.getActionCommand();
            if (sourceString.equals(DifficultyLevel.BEGINNER.toString())){
                difficultyLevel = DifficultyLevel.BEGINNER.ordinal();  // 0
                confirmedPlayerChoice = JOptionPane.showConfirmDialog(SpaceInvaders.this.board,
                        confirmationMessage + "beginner.", "Difficulty level: Beginner",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            } else if (sourceString.equals(DifficultyLevel.NORMAL.toString())) {
                difficultyLevel = DifficultyLevel.NORMAL.ordinal();  // 1
                confirmedPlayerChoice = JOptionPane.showConfirmDialog(SpaceInvaders.this.board,
                        confirmationMessage + "normal.", "Difficulty level: Normal",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            } else if (sourceString.equals(DifficultyLevel.EXPERT.toString())) {
                difficultyLevel = DifficultyLevel.EXPERT.ordinal();  // 2
                confirmedPlayerChoice = JOptionPane.showConfirmDialog(SpaceInvaders.this.board,
                        confirmationMessage + "Expert.", "Difficulty level: Expert",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            } else{
                System.out.println("Difficulty level not recognized by enum DifficultyLevel.");
            }

            if( confirmedPlayerChoice == 0){
                System.out.println("Player chose " + sourceString);
                changeDifficultyLevel = true;
            } else{
                System.out.println("Player not 0?? chose " + confirmationMessage);
            }
        }
    }

    /**
     * Listens to user movements (left, right) and action (shot with space)
     */
    private class LocalKeyboardListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            // when unicode character of e is sent by keyboard to system input
            // pression + released
        }

        @Override
        public void keyPressed(KeyEvent e) {
            Player thisPlayer = SpaceInvaders.this.player;

            // when key pressed
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A){
                thisPlayer.setVelocity(thisPlayer.getMOVE_LEFT());
            }
            if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D){
                thisPlayer.setVelocity(thisPlayer.getMOVE_RIGHT());
            }
            if (keyCode == KeyEvent.VK_SPACE) {
                try {
                    int newX = (int) thisPlayer.getCoordinate().x() + thisPlayer.get_width()/2;
                    Note newPlayerNote = Note.shotQuaver(new Point(newX, thisPlayer.getCoordinate().y()-5));
                    SpaceInvaders.this.playerShots = thisPlayer.shot(SpaceInvaders.this.playerShots, newPlayerNote);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            Player thisPlayer = SpaceInvaders.this.player;

            // when key comes up
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A){
                // System.out.println("Player wants to move left");
                thisPlayer.setVelocity(thisPlayer.getMOVE_RESET());
            }
            if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D){
                // System.out.println("Player wants to move right");
                thisPlayer.setVelocity(thisPlayer.getMOVE_RESET());
            }
        }
    }
}
