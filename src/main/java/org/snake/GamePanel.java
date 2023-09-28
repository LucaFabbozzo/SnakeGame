package org.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/*Questa classe è un componente Swing personalizzato che estende JPanel e rappresenta il pannello di gioco effettivo. Alcuni punti chiave in questa classe:

        SCREEN_WIDTH e SCREEN_HEIGHT: Definiscono le dimensioni dello schermo di gioco.
        UNIT_SIZE: Definisce la dimensione di un singolo blocco nel gioco.
        GAME_UNITS: Il numero di unità nel gioco, calcolato come (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE.
        DELAY: Il ritardo tra gli aggiornamenti del gioco, influenzando la velocità del serpente.
        Il gioco è basato su un array di coordinate x e y per il corpo del serpente. Il serpente viene disegnato sul pannello di gioco, e il gioco inizia quando si chiama startGame().

        Ci sono metodi come move(), checkApple(), e newApple() che gestiscono la logica del gioco. Il metodo paintComponent(Graphics g) è chiamato per disegnare il gioco sulla finestra.*/


public class GamePanel extends JPanel implements ActionListener {

    //Definiscono le dimensioni dello schermo di gioco.
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;

    //Definisce la dimensione di un singolo blocco nel gioco.
    static final int UNIT_SIZE = 25;
    //Il numero di unità nel gioco
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    //Il ritardo tra gli aggiornamenti del gioco, influenzando la velocità del serpente.
    static final int DELAY = 80;
    //Array di coordinate per il corpo del serpente.
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    //Il numero di parti del corpo del serpente.
    int bodyParts = 6;
    //Il numero di mele mangiate dal serpente.
    int applesEaten;
    //Coordinate per la mela nel gioco.
    int appleX;
    int appleY;
    //La direzione in cui si muove il serpente ('U' per su, 'D' per giù, 'L' per sinistra, 'R' per destra).
    char direction = 'R';
    //Un flag che indica se il gioco è in corso.
    boolean running = false;
    //Un timer che controlla gli aggiornamenti del gioco.
    Timer timer;
    //Un oggetto Random utilizzato per posizionare casualmente la mela.
    Random random;

   /* Inizializza le variabili di istanza, imposta le dimensioni del pannello, il colore di sfondo e il focus in modo che il pannello possa gestire gli eventi della tastiera.
    Chiama il metodo startGame() per avviare il gioco.*/
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
   /* Inizializza il gioco. Chiama newApple() per posizionare la prima mela e avvia il timer per gli aggiornamenti del gioco.*/
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
   /* paintComponent(Graphics g): Questo metodo viene chiamato quando il pannello deve essere ridisegnato. È responsabile di disegnare gli elementi del gioco, inclusi il serpente, la mela e il punteggio.*/
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    /*draw(Graphics g): Questo metodo è chiamato da paintComponent per disegnare gli elementi del gioco quando il gioco è in esecuzione. Disegna il serpente, la mela e il punteggio.*/
    public void draw(Graphics g){

        if (running) {
            /*
            for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
             */
            g.setColor(Color.red);
            // La mela viene disegnata come un cerchio rosso utilizzando g.fillOval
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++){
                if (i == 0){
                   /* Il corpo del serpente viene disegnato come una serie di rettangoli verdi (la testa) e rettangoli colorati casualmente (il corpo).*/
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    //g.setColor(new Color(45, 180,0));
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }
    //Genera casualmente le coordinate x e y per la mela nel gioco.
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
    }
   /* Gestisce il movimento del serpente spostando le sue parti in base alla direzione corrente ('U', 'D', 'L' o 'R').*/
    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }
   /* Controlla se il serpente ha mangiato una mela. Se lo ha fatto, incrementa il punteggio, aggiunge una parte al corpo del serpente e genera una nuova mela.*/
    public void checkApple(){
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
        //checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        //check if head touches left boarder
        if (x[0] < 0) {
            running = false;
        }
        //check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        //check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if(!running){
            timer.stop();
        }

    }
    public void gameOver(Graphics g){
        //display the score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        //Game Over Text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

   /* È un'implementazione di KeyListener personalizzata che gestisce gli eventi di tastiera. Questo ti permette di controllare la direzione del serpente utilizzando i tasti freccia o altri tasti*/
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;

            }
        }
    }
}
