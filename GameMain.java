package de.thdeg.game;

import de.thdeg.game.runtime.InternalLedGameThread;

//TODO cleanup project
public class GameMain {

    static public void resetScreen(short[] screen)
    {
        for(int i=0; i<screen.length; i+=3){
            screen[i+0]=(short)137;
            screen[i+1]=(short)207;
            screen[i+2]=(short)240;
        }
    }

    static public void setPixel(short[] screen,int x, int y, short r, short g, short b)
    {
        screen[(y * 48 + x) * 3] = r;
        screen[(y * 48 + x) * 3 + 1] = g;
        screen[(y * 48 + x) * 3 + 2] = b;
    }

    static public void main(String[] passedArgs) throws InterruptedException {
        
        short[] myImage=new short[24*48*3];
        int thisKey=0;
        int frame = 0;

        // This is initialization, do not change this
        InternalLedGameThread.run();

        // Now we show some introductory message and wait 1s before we switch to purple
        System.out.println("Please wait for three seconds for the game to start");
        Thread.sleep(3000);
        resetScreen(myImage);
        InternalLedGameThread.showImage(myImage);

        Bird playerController = new Bird();
        PipeManager pipeManager = new PipeManager();
        ScoreManager scoreHandler = new ScoreManager();

        // After 300ms we show the player
        playerController.draw(myImage);
        Thread.sleep(300);
        InternalLedGameThread.showImage(myImage);

        // Now we loop with keyboard input
        while(true){
            thisKey= InternalLedGameThread.getKeyboard();
            if(thisKey!=-1) {
                switch (thisKey) {
                    case 0:
                        playerController.updateHeight((short) -1);
                        break;
                    case 1:
                        playerController.updateHeight((short) 1);
                        break;
                }                
            }
            pipeManager.update(myImage, frame);
            playerController.draw(myImage);           
            scoreHandler.displayScore(myImage);
            scoreHandler.displayHighScore(myImage);
            if(pipeManager.checkCollision(playerController, scoreHandler))
            {
                System.out.println("GAME OVER");              
                resetScreen(myImage);
                pipeManager = new PipeManager();
                playerController = new Bird();
                frame = 0;
                scoreHandler.onGameOver();
                Thread.sleep(500);
            }
            else{
                pipeManager.setSpeed(scoreHandler);
                InternalLedGameThread.showImage(myImage);
                resetScreen(myImage);
                frame++;
            }           
            Thread.sleep(100);
        }
    }
}
