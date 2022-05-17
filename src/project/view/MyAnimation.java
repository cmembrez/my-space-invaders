package project.view;

import oop.lib.Display;
import oop.lib.Paintable;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class MyAnimation implements Runnable, ActionListener {
    ////////////////////////////////////////////////////////
    //                      FIELDS                         //
    ////////////////////////////////////////////////////////
    private JFrame frame;
    private Timer timer;
    private boolean autoplay;
    private boolean running;
    private Display display;

    private JMenuBar menuBar;

    ////////////////////////////////////////////////////////
    //                    CONSTRUCTOR                     //
    ////////////////////////////////////////////////////////
    public MyAnimation(){
    }

    ////////////////////////////////////////////////////////
    //                         PUBLIC                     //
    ////////////////////////////////////////////////////////
    @Override
    public void run() {
        this.frame = new JFrame("Space Invaders - Crazy Ludi episode");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.timer = new Timer(20, this);
        init(false);  // initial start: new Board
        this.frame.getContentPane().add(display);  // in case the display has changed
        this.frame.setJMenuBar(this.menuBar);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.display.requestFocus();
        this.frame.setVisible(true);
        if (autoplay){
            this.timer.start();
            this.timer.setInitialDelay(20);
        }
    }

    public void reset(){
        init(true);  // restart game in current Board
        this.frame.getContentPane().add(display);  // in case the display has changed
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.display.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.step();
        this.display.repaint();
    }

    ////////////////////////////////////////////////////////
    //                        PROTECTED                   //
    ////////////////////////////////////////////////////////

    /**
     * Initialize the gam if restart false. otherwise, reset and restart game in current Board.
     * @param restart if true, restart game in current Board, else create a new Board and new game.
     */
    protected void init(boolean restart) {
    }

    protected abstract void step();

    protected synchronized void launch(boolean automatic){
        this.autoplay = automatic;
        this.running = automatic;
        SwingUtilities.invokeLater(this);
    }

    ////////////////////////////////////////////////////////
    //                      PROPERTIES                    //
    ////////////////////////////////////////////////////////

    public void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }
    protected final void setDisplay(Display display){
        this.display = display;
    }

    protected final Display getDisplay() {
        return this.display;
    }
}
