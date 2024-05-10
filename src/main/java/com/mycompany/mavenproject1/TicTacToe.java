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

        connectToServer();
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


}