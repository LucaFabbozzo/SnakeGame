package org.snake;

import javax.swing.*;

/*Questa classe estende JFrame, che è un componente Swing utilizzato per creare finestre grafiche. Nel costruttore GameFrame, stai configurando la finestra del gioco:

        this.add(new GamePanel()): Aggiunge un pannello di gioco (GamePanel) alla finestra.
        this.setTitle("Snake"): Imposta il titolo della finestra a "Snake".
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE): Specifica l'operazione di chiusura predefinita quando si chiude la finestra.
        this.setResizable(false): Impedisce il ridimensionamento della finestra.
        this.pack(): Adatta automaticamente la finestra alle dimensioni del suo contenuto.
        this.setVisible(true): Rende la finestra visibile.
        this.setLocationRelativeTo(null): Posiziona la finestra al centro dello schermo.*/

public class GameFrame extends JFrame {
    GameFrame(){
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
