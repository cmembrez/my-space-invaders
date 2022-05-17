package project.view;

import oop.lib.Display;
import project.model.Enemy;
import project.model.projectiles.Projectile;
import project.utils.MyLinkedList;
import project.model.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Board extends Display {
    //////////////////////////////////////////////
    /////            FIELDS                  /////
    //////////////////////////////////////////////
    private int height;
    private int width;

    private LocalStatisticPanel statisticPanel;
    private MyLinkedList<Projectile> enemiesShots;
    private MyLinkedList<Enemy> enemies;
    private MyLinkedList<Projectile> playerShots;
    private Player player;
    private boolean playerWins = false;

    private boolean playerLoses = false;

    //////////////////////////////////////////////
    /////        CONSTRUCTOR                 /////
    //////////////////////////////////////////////
    public Board(int width, int height, MyLinkedList<Projectile> enemiesShots, MyLinkedList<Enemy> enemies,
                 MyLinkedList<Projectile> playerShots, Player player) {
        super(width, height);
        this.width = width;
        this.height = height;
        this.enemiesShots = enemiesShots;
        this.enemies = enemies;
        this.playerShots = playerShots;
        this.player = player;

        this.setLayout(new BorderLayout());
        this.add(statisticPanel = new LocalStatisticPanel(), BorderLayout.NORTH);
    }

    //////////////////////////////////////////////
    /////          INNER PANELS              /////
    //////////////////////////////////////////////
    private static class LocalStatisticPanel extends JPanel{
        private final JLabel lifeRemaining = new JLabel("Health: ");
        private final JLabel score = new JLabel("Score: ");
        private final JLabel enemiesRemaining = new JLabel("Enemies: ");

        private LocalStatisticPanel(){
            setBorder(new EmptyBorder(1, 60, 5, 60));
            setLayout(new GridLayout(1, 2, 0, 0));
            setPreferredSize(new Dimension(160, 30));

            lifeRemaining.setHorizontalAlignment(SwingConstants.CENTER);
            score.setHorizontalAlignment(SwingConstants.CENTER);
            enemiesRemaining.setHorizontalAlignment(SwingConstants.CENTER);

            add(lifeRemaining);
            add(score);
            add(enemiesRemaining);
        }

        // Methods
        public void setLifeRemaining(int lifes_number){
            this.lifeRemaining.setText("Lifes: " + lifes_number);
        }

        public void setScore(int score){
            this.score.setText("Score: " + score);
        }

        public void setEnemiesRemaining(int enemies_number){
            this.enemiesRemaining.setText("Enemies: " + enemies_number);
        }
    }

    //////////////////////////////////////////////
    /////          METHODS                   /////
    //////////////////////////////////////////////

    /**
     * Reset the board for a new game
     * @param width
     * @param height
     * @param enemiesShots
     * @param enemies
     * @param playerShots
     * @param player
     */
    public void resetBoard(int width, int height, MyLinkedList<Projectile> enemiesShots, MyLinkedList<Enemy> enemies,
                MyLinkedList<Projectile> playerShots, Player player) {
        this.width = width;
        this.height = height;
        this.enemiesShots = enemiesShots;
        this.enemies = enemies;
        this.playerShots = playerShots;
        this.player = player;
    }

    /**
     * Pop-Up an end-game message and ask player to play again or not
     * @return 0 to restart game, 1 to exit, -1 otherwise.
     */
    public int setEndMessage(){
        final String END_MESSAGE;
        final String END_TITLE;
        int end_choice = -1;
        // ask player to play again or not
        if ( this.playerWins ) {
            END_MESSAGE = "Victory is yours, congrats! Want to handle the next invasion?";
            END_TITLE = "VICTORY";
            // messagePanel.setEndGameMessage("You are a WINNER!!!");
            end_choice = JOptionPane.showConfirmDialog(this,
                    END_MESSAGE, END_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        } else if( this.playerLoses ) {
            END_MESSAGE = "Better luck next time. You want to try again?";
            END_TITLE = "GAME OVER";
            // messagePanel.setEndGameMessage("Game Over! Better Luck Next Time...");
            end_choice = JOptionPane.showConfirmDialog(this,
                    END_MESSAGE, END_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        } else{
            System.out.println("[Board][setEndMessage] ERROR ...stalemate? playerWins, playerLoses are both false");
        }
        // restart or exit game
        if( end_choice == 1 || end_choice == -1){  // NO  or exit-cross
            System.exit(ABORT);
            return 1;
        } else if( end_choice == 0 ){  // YES
            return 0;
        } else{
            System.out.println("ERROR !! choice number undefined!");
            return -1;
        }
    }

    //////////////////////////////////////////////
    /////          PROPERTIES                /////
    //////////////////////////////////////////////
    public void setPlayerWins(boolean win){
        this.playerWins = win;
    }
    public void setPlayerLoses(boolean playerLoses) {
        this.playerLoses = playerLoses;
    }

    public void setStatisticPanelScore(int score){
        this.statisticPanel.setScore(score);
    }

    public void setStatisticPanelLife(int life){
        this.statisticPanel.setLifeRemaining(life);
    }

    //////////////////////////////////////////////
    /////          OVERRIDES                 /////
    //////////////////////////////////////////////
    @Override
    public void paint(Display display) {
        statisticPanel.setEnemiesRemaining(this.enemies.size());
        for (Enemy enemy : this.enemies) {
            enemy.paint(this);
        }
        for (Projectile enemiesShot : this.enemiesShots) {
            if (enemiesShot == null) continue;
            enemiesShot.paint(this);
        }
        for (Projectile playerShot : this.playerShots) {
            if (playerShot == null) continue;
            playerShot.paint(this);
        }
        if ( !player.isDead() ){
            player.paint(this);
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
