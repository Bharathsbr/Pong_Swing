import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
public class GamePanel extends JPanel implements Runnable{
    static final int GAME_WIDTH=1000;
    static final int GAME_HEIGHT=(int)(GAME_WIDTH*(0.5555));
    static final Dimension dim=new Dimension(GAME_WIDTH,GAME_HEIGHT);
    static final int BALL_Diameter=20;
    static final int PADDLE_WIDTH=25;
    static final int PADDLE_HEIGHT=100;
    Thread gameThread;
    Graphics graphics;
    Image image;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;


    GamePanel(){
        newPaddles();
        newBall();
        score=new Score(GAME_WIDTH,GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(dim);
        gameThread=new Thread(this);
        gameThread.start();
    }

    public void newBall(){
        random=new Random();
        ball=new Ball((GAME_WIDTH/2)-(BALL_Diameter/2),random.nextInt(GAME_HEIGHT-BALL_Diameter),BALL_Diameter,BALL_Diameter);
    }
    public void newPaddles(){
        paddle1=new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
        paddle2=new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);
    }
    public void paint(Graphics g){
        image=createImage(getWidth(),getHeight());
        graphics=image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
    }
    public void draw(Graphics g){
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);

    }
    public void move(){
        paddle1.move();
        paddle2.move();
        ball.move();
    }
    public void run(){
        long lastTime=System.nanoTime();
        double amount=60.0;
        double ns=1000000000/amount;
        double delta=0;
        while(true){
            long now=System.nanoTime();
            delta+=(now-lastTime)/ns;
            lastTime=now;
            if(delta>=1){
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }
    public void checkCollision(){

        if(ball.y<=0){
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.y>=GAME_HEIGHT-BALL_Diameter){
            ball.setYDirection(-ball.yVelocity);
        }

        if(ball.intersects(paddle1)){
            ball.xVelocity=Math.abs(ball.xVelocity);
            ball.xVelocity++;
            if(ball.yVelocity>0){
                ball.yVelocity++;
            }else{
                ball.yVelocity--;
            }
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        if(ball.intersects(paddle2)){
            ball.xVelocity=Math.abs(ball.xVelocity);
            ball.xVelocity++;
            if(ball.yVelocity>0){
                ball.yVelocity++;
            }else{
                ball.yVelocity--;
            }
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if(paddle1.y<=0){
            paddle1.y=0;
        }
        if(paddle1.y>=(GAME_HEIGHT-PADDLE_HEIGHT)){
            paddle1.y=GAME_HEIGHT-PADDLE_HEIGHT;
        }
        if(paddle2.y<=0){
            paddle2.y=0;
        }
        if(paddle2.y>=(GAME_HEIGHT-PADDLE_HEIGHT)){
            paddle2.y=GAME_HEIGHT-PADDLE_HEIGHT;
        }
        if(ball.x<=0){
            score.player2++;
            newPaddles();
            newBall();
        }
        if(ball.x>=GAME_WIDTH-BALL_Diameter){
            score.player1++;
            newPaddles();
            newBall();
        }
    }

    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            paddle1.KeyPressed(e);
            paddle2.KeyPressed(e);
        }
        public void keyReleased(KeyEvent e){
            paddle1.KeyReleased(e);
            paddle2.KeyReleased(e);
        }
    }
}
