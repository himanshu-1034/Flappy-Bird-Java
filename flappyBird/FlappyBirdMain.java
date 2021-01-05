package flappyBird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBirdMain extends JFrame implements ActionListener, KeyListener {
    public static FlappyBirdMain flappyBird;
    public final int HEIGHT=800,WIDTH=800;
    public int score;
    public boolean gameOver=false,started=false;
    public ViewRenderer viewRenderer;
    public Rectangle bird;
    public int ticks=0,yMotion=0;
    int delay=20;
    public ArrayList<Rectangle> columns;
    public Random random;

    public FlappyBirdMain(){

        Timer timer = new Timer(delay,this);

        viewRenderer = new ViewRenderer();
        this.add(viewRenderer);



        bird = new Rectangle(WIDTH/2-10,HEIGHT/2-10,20,20);
        columns = new ArrayList<Rectangle>();
        random = new Random();


        this.addKeyListener(this);
        this.setResizable(false);
        this.setDefaultCloseOperation(3);
        this.setLayout(null);
        this.setTitle("FLAPPY BIRD");
        this.setSize(WIDTH,HEIGHT);
        this.setVisible(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        timer.start();
    }

    public static void main(String[] args) {
       flappyBird = new FlappyBirdMain();
    }

    public void addColumn(boolean start){
        int space=250;
        int width = 100;
        int height = 50+ random.nextInt(300);


        if(start){
            columns.add(new Rectangle(WIDTH + width + columns.size()*300,HEIGHT-height,width,height));
            columns.add(new Rectangle(WIDTH+width+(columns.size()-1)*300,0,width,HEIGHT-height-space));

        }else {
            columns.add(new Rectangle(columns.get(columns.size()-1).x+600,HEIGHT-height,width,height));
            columns.add(new Rectangle(columns.get(columns.size()-1).x,0,width,HEIGHT-height-space));

        }


    }

    public void paintColumn(Graphics g,Rectangle column){
        g.setColor(Color.green.darker());
        g.fillRect(column.x,column.y,column.width,column.height);

    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.cyan);
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(Color.orange);
        g.fillRect(0,HEIGHT-120,WIDTH,120);
        g.setColor(Color.green);
        g.fillRect(0,HEIGHT-120,WIDTH,20);
        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for (Rectangle column: columns){
            paintColumn(g,column);
        }

        if (!started){
            g.setColor(Color.white);
            g.setFont(new Font("Arial",Font.BOLD,50));
            g.drawString("PRESS SPACE TO START",75,HEIGHT/2-50);
        }

        if (gameOver){
            g.setColor(Color.white);
            g.setFont(new Font("Arial",Font.BOLD,50));
            g.drawString("GAME OVER.",230,HEIGHT/2-50);
            g.drawString("TO RESTART, PRESS SPACE",50,HEIGHT/2+30);
        }

        if (!gameOver && started){
            g.setColor(Color.white);
            g.setFont(new Font("Arial",Font.BOLD,70));
            g.drawString(String.valueOf(score),WIDTH/2-25,100);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        int speed=10;
        ticks++;
        if (started){


            for (int i=0;i<columns.size();i++){
                Rectangle col = columns.get(i);
                col.x -= speed;
            }

            if (ticks%2==0 && yMotion<15){
                yMotion+=2;
            }

            for (int i=0;i<columns.size();i++){
                Rectangle col = columns.get(i);
                if (col.x + col.width <0){
                    columns.remove(col);

                    if (col.y==0){
                        addColumn(false);
                    }

                }
            }

            bird.y+=yMotion;

            for (Rectangle column:columns){

                if (column.y==0 && bird.x + bird.width/2>column.x+column.width/2 - 5 && bird.x+bird.width/2 < column.x+column.width/2+5){
                    score++;
                }

                if (column.intersects(bird)){
                    gameOver=true;


                    if (bird.x<=column.x){
                        bird.x = column.x-bird.width;
                    }
                    else {
                        if (column.y != 0){
                            bird.y =column.y-bird.height;
                        }
                        else if (bird.y < column.height){
                            bird.y = column.height;
                        }
                    }
                }

            }
            if (bird.y>HEIGHT-120 || bird.y<0){

                gameOver=true;
            }
            if (bird.y + yMotion >= HEIGHT-120){
                bird.y = HEIGHT-120-bird.height;
            }

        }

        repaint();
    }

    public void jump(){
        if (gameOver){

            bird = new Rectangle(WIDTH/2-10,HEIGHT/2-10,20,20);
            columns.clear();
            yMotion=0;
            score=0;
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);


            gameOver=false;
        }
        if (!started){
            started=true;
        }else if (!gameOver){
            if (yMotion>0){
                yMotion=0;
            }
            yMotion-=10;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_SPACE){
            started=true;

        }
        if (e.getKeyCode()==KeyEvent.VK_SPACE){
            jump();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
