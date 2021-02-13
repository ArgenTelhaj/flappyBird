import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random ;


public class FlappyBird implements ActionListener, MouseListener {


    public static FlappyBird flappyBird;
    public final int WIDTH = 800 ;
    public final int HEIGHT = 800 ;

    public ImageIcon imageIcon;

    public Renderer renderer;

    public Random rand;

    public boolean gameOver, started;

    public Rectangle bird;

    public int ticks, yMotion, score;

    public ArrayList<Rectangle> columns;

    public FlappyBird( ){

        JFrame frame = new JFrame();
        renderer= new Renderer();
        imageIcon = new ImageIcon("bird.png");
        rand = new Random();
        Timer timer = new Timer(20 ,this);


        frame.add(renderer);
        frame.addMouseListener(this);
        frame.setTitle("FlappyBird");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(WIDTH,HEIGHT);
        frame.setIconImage(imageIcon.getImage());
        frame.setVisible(true);


        bird = new Rectangle(WIDTH/2 - 10 ,HEIGHT/2 - 10,20,20);//centering it and giving the size
        columns = new ArrayList<Rectangle>();
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();

    }

    public void addColumn(boolean start){
        int space= 300;
        int width= 100;
        int height= 50 + rand.nextInt(300);

        if(start){
            columns.add( new Rectangle(WIDTH+width+columns.size()*300,HEIGHT - height- 120, width,height));
            columns.add(new Rectangle(WIDTH+ width+(columns.size()-1)*300,0,width,HEIGHT-height-space));
        }
        else{
            columns.add( new Rectangle(columns.get(columns.size() -1 ).x + 600,HEIGHT - height- 120, width,height));
            columns.add(new Rectangle(columns.get(columns.size() -1 ).x ,0,width,HEIGHT-height-space));
        }
    }

    public void paintColumn(Graphics g, Rectangle column){
        g.setColor(Color.GREEN.darker());
        g.fillRect(column.x, column.y,column.width,column.height);
    }

    public void jump(){
        //starting the game when mouse clicked
        if(gameOver){
            bird = new Rectangle(WIDTH/2 - 10 ,HEIGHT/2 - 10,20,20);//centering it and giving the size
            columns.clear();
            yMotion = 0;
            score= 0;
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver= false;
        }
        if(!started){
            started= true;
        }else if(!gameOver){
            if(yMotion>0){
                yMotion=0;
            }
            yMotion-=10;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        renderer.repaint();
        int speed= 10;

        ticks++;
        //makes it so that everything moves when the game starts
        if(started) {
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                column.x -= speed;
            }

            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }

            //loops through the column elements in the array list
            for (int i = 0; i < columns.size(); i++) {

                Rectangle column = columns.get(i);
                //checks if the columns coordinates are outside of the frame and if they are it removes them
                if (column.x + column.width < 0) {

                    columns.remove(column);
                    //if it is the top column then add another column
                    if (column.y == 0) {
                        addColumn(false);
                    }
                }
            }

            //moves the bird
            bird.y += yMotion;

            //checks for collision
            for (Rectangle column : columns) {
                //checks if its in the center of the column
                if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {
                    score++;
                }

                if (column.intersects(bird)) {
                    gameOver = true;

                    //if the bird falls and or crashes with a pipe it stops
                    bird.x = column.x - bird.width;
                }
            }

            if (bird.y > HEIGHT - 120 || bird.y < 0) {
                gameOver = true;
            }
            if (bird.y + yMotion >= HEIGHT - 120) {
                bird.y = HEIGHT - 120 - bird.height;//stops the bird after fall

            }

        }
        renderer.repaint();
    }



    public void repaint(Graphics g) {
        g.setColor(Color.cyan);
        g.fillRect(0,0,WIDTH,HEIGHT);

        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT- 120, WIDTH , 120);

        g.setColor(Color.green);
        g.fillRect(0, HEIGHT- 120, WIDTH , 20);

        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for(Rectangle column:columns){

            paintColumn(g, column);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial",1, 100));
        if(!started){
            g.drawString("START GAME!",70, HEIGHT/2-20);
        }

        if(gameOver){
            g.drawString("GAME OVER",100, HEIGHT/2-20);
        }
        //counting the points
        if(!gameOver && started){
            g.drawString(String.valueOf(score),WIDTH/2 -25,100);
        }
    }

    public static void main(String[] args) {


        flappyBird = new FlappyBird();


    }


    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
