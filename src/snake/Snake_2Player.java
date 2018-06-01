/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snake; 

import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Snake_2Player extends JFrame implements Runnable {
    static final int XBORDER = 20;
    static final int YBORDER = 20;
    static final int YTITLE = 30;
    static final int WINDOW_BORDER = 8;
    static final int WINDOW_WIDTH = 2*(WINDOW_BORDER + XBORDER) + 795;
    static final int WINDOW_HEIGHT = YTITLE + WINDOW_BORDER + 2 * YBORDER + 925;
    boolean animateFirstTime = true;
    static int xsize = -1;
    static int ysize = -1;
    Image image;
    Graphics2D g;

    final int numRows = 41;
    final int numColumns = 41;
    final int EMPTY = 0;
    final int SNAKE = 1;
    final int BAD_BOX = 2;
    final int SNAKE_2 = 3;
    ArrayList<Snake> stars = new ArrayList<Snake>();
    final int FOOD = 4;
   
    int board[][];
    int currentRow;
    int currentColumn;
    int RowDir;
    int ColumnDir;
    int timecount;
    int score;
    int score_2;
    int highScore;
    int currentRow_2;
    int currentColumn_2;
    int RowDir_2;
    int ColumnDir_2;
    double seconds;
    boolean gameover;
    boolean gameover_2;
    boolean pause;
    
    static Snake_2Player frame1;
    public static void main(String[] args) {
        frame1 = new Snake_2Player();
        frame1.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(true);
    }

    public Snake_2Player() {

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button
                }
                if (e.BUTTON3 == e.getButton()) {
                    //right button
                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_RIGHT == e.getKeyCode())
                {
                    
                    ColumnDir = + 1;
                    RowDir = 0;
                }
                if (e.VK_LEFT == e.getKeyCode())
                {
                    
                    ColumnDir = - 1;
                    RowDir = 0;
                }
                if (e.VK_UP == e.getKeyCode())
                {
                    
                    RowDir = - 1;
                    ColumnDir = 0;
                }
                if (e.VK_DOWN == e.getKeyCode())
                {
                    
                    RowDir = + 1;
                    ColumnDir = 0;
                }
                if (e.VK_D == e.getKeyCode())
                {
                    
                    ColumnDir_2 = + 1;
                    RowDir_2 = 0;
                }
                if (e.VK_A == e.getKeyCode())
                {
                    
                    ColumnDir_2 = - 1;
                    RowDir_2 = 0;
                }
                if (e.VK_W == e.getKeyCode())
                {
                    RowDir_2 = - 1;
                    ColumnDir_2 = 0;
                }
                if (e.VK_S == e.getKeyCode())
                {
                    
                    RowDir_2 = + 1;
                    ColumnDir_2 = 0;
                }
                if (e.VK_P == e.getKeyCode())
                {
                    
                    pause = true;
                }

                repaint();
            }
        });
        init();
        start();
    }




    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }
////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

//fill background
        g.setColor(Color.black);

        g.fillRect(0, 0, xsize, ysize);

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
//fill border
        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.black);
        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }

        g.setColor(Color.gray);
//horizontal lines
        for (int zi=1;zi<numRows;zi++)
        {
            g.drawLine(getX(0) ,getY(0)+zi*getHeight2()/numRows ,
            getX(getWidth2()) ,getY(0)+zi*getHeight2()/numRows );
        }
//vertical lines
        for (int zi=1;zi<numColumns;zi++)
        {
            g.drawLine(getX(0)+zi*getWidth2()/numColumns ,getY(0) ,
            getX(0)+zi*getWidth2()/numColumns,getY(getHeight2())  );
        }

//Display the objects of the board
        for (int zrow=0;zrow<numRows;zrow++)
        {
            for (int zcolumn=0;zcolumn<numColumns;zcolumn++)
            {
                if (board[zrow][zcolumn] == FOOD)
                {
                    g.setColor(Color.orange);
                    g.fillRect(getX(0)+zcolumn*getWidth2()/numColumns,
                    getY(0)+zrow*getHeight2()/numRows,
                    getWidth2()/numColumns,
                    getHeight2()/numRows);
                }
                if (board[zrow][zcolumn] == SNAKE_2)
                {
                    g.setColor(Color.blue);
                    g.fillRect(getX(0)+zcolumn*getWidth2()/numColumns,
                    getY(0)+zrow*getHeight2()/numRows,
                    getWidth2()/numColumns,
                    getHeight2()/numRows);
                }  
                if (board[zrow][zcolumn] == SNAKE)
                {
                    g.setColor(Color.green);
                    g.fillRect(getX(0)+zcolumn*getWidth2()/numColumns,
                    getY(0)+zrow*getHeight2()/numRows,
                    getWidth2()/numColumns,
                    getHeight2()/numRows);
                }
                if (board[zrow][zcolumn] == BAD_BOX)
                {
                    g.setColor(Color.red);
                    g.fillRect(getX(0)+zcolumn*getWidth2()/numColumns,
                    getY(0)+zrow*getHeight2()/numRows,
                    getWidth2()/numColumns,
                    getHeight2()/numRows);
                }
            }
        }
        g.setColor(Color.white);
        g.setFont(new Font("Old English Text", Font.PLAIN, 12));
        g.drawString("Player 1 Score: " + score, 50, 45);
        g.drawString("Player 2 score: " + score_2, 250, 45);
        if (pause)
       {
           g.setColor(Color.black);
           g.setFont(new Font("Old English Text", Font.PLAIN, 62));
           g.drawString("Paused", WINDOW_WIDTH/2 - 100, WINDOW_HEIGHT/2);
       }
        if (gameover ||gameover_2)
        {
            g.setColor(Color.black);
            g.setFont(new Font("Old English Text", Font.PLAIN, 62));
            g.drawString("Game Over", WINDOW_WIDTH/2 - 150, WINDOW_HEIGHT/2);
            RowDir = 0;
            ColumnDir = 0;
            RowDir_2 = 0;
            ColumnDir_2 = 0;
            timecount = 0;
            
        }
//        if (gameover && score >= score_2)
//        {
//            g.setColor(Color.black);
//            g.setFont(new Font("Old English Text", Font.PLAIN, 62));
//            g.drawString("Blue Won!!!", WINDOW_WIDTH/2 - 150, WINDOW_HEIGHT/2);
//            RowDir = 0;
//            ColumnDir = 0;
//            RowDir_2 = 0;
//            ColumnDir_2 = 0;
//            timecount = 0;
//            
//        }
//        else if (gameover_2 && score_2 >= score)
//        {
//            g.setColor(Color.black); 
//            g.setFont(new Font("Old English Text", Font.PLAIN, 62));
//            g.drawString("Green Won!!!", WINDOW_WIDTH/2 - 150, WINDOW_HEIGHT/2);
//            RowDir = 0;
//            ColumnDir = 0;
//            RowDir_2 = 0;
//            ColumnDir_2 = 0;
//            timecount = 0;
//            
//        }
//        else if (gameover_2 && score_2 < score || gameover && score_2 > score)
//        {
//            g.setColor(Color.black);
//            g.setFont(new Font("Old English Text", Font.PLAIN, 62));
//            g.drawString("Nobody Won!!!", WINDOW_WIDTH/2 - 150, WINDOW_HEIGHT/2);
//            RowDir = 0;
//            ColumnDir = 0;
//            RowDir_2 = 0;
//            ColumnDir_2 = 0;
//            timecount = 0;
//            
//        }
        gOld.drawImage(image, 0, 0, null);
    }


////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            seconds = 0.3;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {
//Allocate memory for the 2D array that represents the board.
        board = new int[numRows][numColumns];
//Initialize the board to be empty.
        for (int zrow = 0;zrow < numRows;zrow++)
        {
            for (int zcolumn = 0;zcolumn < numColumns;zcolumn++)
            {
                board[zrow][zcolumn] = EMPTY;
            }
        }
       gameover = false;
       gameover_2 = false;
       currentRow = numRows/2;
       currentColumn = numColumns/2 + 1;
       board[currentRow][currentColumn] = SNAKE;
       currentRow_2 = numRows/2;
       currentColumn_2 = numColumns/2 - 1;
       board[currentRow_2][currentColumn_2] = SNAKE_2;
       ColumnDir = 1;
       RowDir = 0;
       ColumnDir_2 = -1;
       RowDir_2 = 0;
       score = 0;
       score_2 = 0;
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {

        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }

            reset();
        }
        if (pause == true)
        {
            seconds = 0;
        }
       if (timecount % 2 == 1)
       {
       int row = 0;
       int column = 0;
       boolean KeepLoop = true;
       while (KeepLoop)
       {
        row = (int)(Math.random()*numRows);
        column = (int)(Math.random()*numColumns);
        if (board[row][column] == EMPTY)
        {
        board[row][column] = BAD_BOX;
        KeepLoop = false;
        }
       }
       }
        if (timecount % 2 == 1)
       {
       int row2 = 0;
       int column2 = 0;
       boolean KeepLoop2 = true;
       while (KeepLoop2)
       {
        row2 = (int)(Math.random()*numRows);
        column2 = (int)(Math.random()*numColumns);
        if (board[row2][column2] == EMPTY)
        {
        board[row2][column2] = FOOD;
        KeepLoop2 = false;
        }
       }
       }
        if (pause)
       {
           try {
           Thread.sleep(1000);                 //1000 milliseconds is one second.
           } catch(InterruptedException ex) {
           Thread.currentThread().interrupt();
           }
           pause = false;
       }
       currentColumn += ColumnDir;
       currentRow += RowDir;
       if (currentRow >= numRows)
       {
           currentRow = 0;
       }
       if (currentRow < 0 )
       {
           currentRow = numRows - 1;
       }
       if ( currentColumn >= numColumns)
       {
           currentColumn = 0;
       }
       if (currentColumn < 0)
       {
           currentColumn = numColumns;
       }
       else if (board[currentRow][currentColumn] == SNAKE || board[currentRow][currentColumn] == BAD_BOX || board[currentRow][currentColumn] == SNAKE_2)
       {
           gameover = true;
       }
       else if (board[currentRow][currentColumn] == FOOD)
       {
           score += 1;
           board[currentRow][currentColumn] = SNAKE;
       }
       else{
           board[currentRow][currentColumn] = SNAKE;
       }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////       
       currentColumn_2 += ColumnDir_2;
       currentRow_2 += RowDir_2;
       if (currentRow_2 >= numRows - 1)
       {
           currentRow_2 = 0;
       }
       if (currentRow_2 < 0 )
       {
           currentRow_2 = numRows - 1;
       }
       if ( currentColumn_2 >= numColumns)
       {
           currentColumn_2 = 0;
       }
       if (currentColumn_2 < 0)
       {
           currentColumn_2 = numColumns;
       }
       else if (board[currentRow_2][currentColumn_2] == BAD_BOX || board[currentRow_2][currentColumn_2] == SNAKE_2 || board[currentRow_2][currentColumn_2] == SNAKE)
       {
           gameover_2 = true;
       }
       else if (board[currentRow_2][currentColumn_2] == FOOD)
       {
           score_2 += 1;
           board[currentRow_2][currentColumn_2] = SNAKE_2;
       }
//////////////////////////////////////////////////////////////////////////////////////////////////
         else
       {
           board[currentRow_2][currentColumn_2] = SNAKE_2;
       }
////////////       
       

       if (gameover == false && gameover_2 == false){
           timecount++;
           score += 1;
           score_2 += 1;
       }
    }

////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
/////////////////////////////////////////////////////////////////////////
    public static int getX(int x) {
        return (x + XBORDER + WINDOW_BORDER);
    }

    public static int getY(int y) {
        return (y + YBORDER + YTITLE );
    }

    public static int getYNormal(int y) {
        return (-y + YBORDER + YTITLE + getHeight2());
    }
    
    public static int getWidth2() {
        return (xsize - 2 * (XBORDER + WINDOW_BORDER));
    }

    public static int getHeight2() {
        return (ysize - 2 * YBORDER - WINDOW_BORDER - YTITLE);
    }
}
