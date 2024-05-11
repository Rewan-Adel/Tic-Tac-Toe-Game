package com.mycompany.mavenproject1;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.*;

public class TicTacToe {
    
    private Socket socket ;
    private BufferedReader in;
    private PrintWriter out;

    private String serverAddress;
    private int serverPort;

    int boardWidth = 600;
    int boardHeight = 650; //50px for the text panel on top

    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    JButton[][] board = new JButton[3][3];
    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX;

    boolean gameOver = false;
    int turns = 0;

    public TicTacToe(String serverAddress,int serverPort ) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;

        play();
        connectToServer();
    }
    
    void play(){
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setBackground(Color.pink);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.pink);
        frame.add(boardPanel);

       for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                tile.setBackground(Color.pink);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);
                

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (gameOver) return;
                        JButton tile = (JButton) e.getSource();
                       
                        if (tile.getText() == "") {
                            tile.setText(currentPlayer);
                            turns++;
                           checkWinner();
                            if (!gameOver) {
                                currentPlayer = currentPlayer == playerX ? playerO : playerX;
                                textLabel.setText(currentPlayer + "'s turn.");
                            }
                        }

                    }
                });
            }
         
        }
   
    }




    private void connectToServer() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverAddress, serverPort), 10000); // timeout set to 100 seconds
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (java.net.SocketTimeoutException e) {
            System.out.println("Connection timed out: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




void checkWinner() { 
        //horizontal 
        for (int r = 0; r < 3; r++) { 
            if (board[r][0].getText() == "") continue; 
 
            if (board[r][0].getText() == board[r][1].getText() && 
                board[r][1].getText() == board[r][2].getText()) { 
                for (int i = 0; i < 3; i++) { 
                    setWinner(board[r][i]); 
                } 
                gameOver = true; 
                return; 
            } 
        } 
 
        //vertical 
        for (int c = 0; c < 3; c++) { 
            if (board[0][c].getText() == "") continue; 
             
            if (board[0][c].getText() == board[1][c].getText() && 
                board[1][c].getText() == board[2][c].getText()) { 
                for (int i = 0; i < 3; i++) { 
                    setWinner(board[i][c]); 
                } 
                gameOver = true; 
                return; 
            } 
        } 
 
        //diagonally 
        if (board[0][0].getText() == board[1][1].getText() && 
            board[1][1].getText() == board[2][2].getText() && 
            board[0][0].getText() != "") { 
            for (int i = 0; i < 3; i++) { 
                setWinner(board[i][i]); 
            } 
            gameOver = true; 
            return; 
        } 
 

        //anti-diagonally 
        if (board[0][2].getText() == board[1][1].getText() && 
            board[1][1].getText() == board[2][0].getText() && 
            board[0][2].getText() != "") { 
            setWinner(board[0][2]); 
            setWinner(board[1][1]); 
            setWinner(board[2][0]); 
            gameOver = true; 
            return; 
        } 
 
        if (turns == 9) { 
            for (int r = 0; r < 3; r++) { 
                for (int c = 0; c < 3; c++) { 
                    setTie(board[r][c]); 
                } 
            } 
            gameOver = true; 
        } 
    } 
 
    void setWinner(JButton tile) { 
        textLabel.setText(currentPlayer + " is the winner!"); 
         
        ImageIcon imageIcon = new ImageIcon("image//winner.jpg"); 
        tile.setIcon(imageIcon);     
     
        Image image = imageIcon.getImage(); 
     
        JPanel panel = new JPanel() { 
            @Override 
            protected void paintComponent(Graphics g) { 
                super.paintComponent(g); 
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this); 
            } 
        }; 

 
        frame.setContentPane(panel); 
        frame.revalidate(); 
    } 
 
    void setTie(JButton tile) { 
        tile.setForeground(Color.pink); 
        tile.setBackground(Color.gray); 
        textLabel.setText("Tie!"); 
    } 
}
