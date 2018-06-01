/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 *
 * @author 384000346
 */
public class Snake {
        Image Star;
    private int starXPos;
    private int starYPos;
    
    Snake()
    {
        starXPos = (int)(Math.random() * Snake_2Player.getWidth2());
        starYPos = (int)(Math.random() * Snake_2Player.getHeight2());
    }
    public void draw(Graphics2D g)
    {
//        drawStar(obj,g,image, Window.getX(starXPos), Window.getYNormal(starYPos), 0.0, 1.0, 1.0);
        drawStar(g, Snake_2Player.getX(starXPos),Snake_2Player.getYNormal(starYPos),0.0,1.0,1.0 );
    }
    public static void moveRight(int moveVal)
    {
    }
    public static void moveLeft(int moveVal)
    {
    }
    public static void moveUp(int moveVal)
    {
    }
    public static void moveDown(int moveVal)
    {
    }
////////////////////////////////////////////////////////////////////////////
    public void drawStar(Graphics2D g,int xpos,int ypos,double rot,double xscale,double yscale)
    {
//        int width = image.getWidth(obj);
//        int height = image.getHeight(obj);
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );
        
//        g.drawImage(Star,-width/2,-height/2,
//        width,height,this);
        g.setColor(Color.yellow);
        g.fillOval(-10,-10,20,20);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }    

}
