package view;

import exceptions.Error;
import compilador.Analizador;
import compilador.Token;
import pathController.FileMain;
import pathController.FileService;
import util.NumeredBorder;
import util.ParsinTableModel;
import util.TokenTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aline Aparecida Gabriel Redivo
 * @author Tauan Oliveira Da Silva
 * @author Victor de Rose Trunfo
 */

public class Home extends JFrame {

    private JMenuBar menuBar;
    private JTextArea txtType;
    private JTextArea errorType;
    private JTable tokenTable;
    private JTable sintaticoTable;
    private JScrollPane sPane;
    private JScrollPane sPaneType;
    private JScrollPane sPaneError;
    private JScrollPane sPaneSintatio;
    private JButton newFile;
    private JButton loadFile;
    private JButton saveFile;
    private JButton deleteFile;
    private JButton playFile;
    private JButton playSintatico;
    private JFileChooser fc;
    private JFileChooser dc;
    private Stack<Token> finalStack;
    private Stack<Token> nTerminaisStack;
    HashMap<String, Integer> map;
    private FileMain fileMain;
    private FileService fileService;
    private List<String> lines;
    private Stack<Token> auxFinalStack;
    private Analizador analizador;
    private Boolean gambiarra = false;

    public void setFinalStack(Stack<Token> finalStack) {
        this.finalStack = finalStack;
    }

    public void setnTerminaisStack(Stack<Token> nTerminaisStack) {
        this.nTerminaisStack = nTerminaisStack;
    }

    public Home() {
        this.setSize(1215, 930);
        setResizable(false);
        setTitle("Analisador Léxico");
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        startHome();
    }

    public void startHome() {
        fileMain = new FileMain();
        fileService = new FileService();
        finalStack = new Stack<Token>();
        nTerminaisStack = new Stack<Token>();
        auxFinalStack = new Stack<Token>();
        map = new HashMap<String, Integer>();
        lines = new ArrayList<String>();
        fc = new JFileChooser();
        dc = new JFileChooser();

        fc.setMultiSelectionEnabled(false);
        fc.setCurrentDirectory(new File(""));
        dc.setMultiSelectionEnabled(false);
        dc.setCurrentDirectory(new File(""));
        dc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//texto
        txtType = new JTextArea();
        txtType.setBounds(5, 62, 600, 550);
        txtType.setBorder(new NumeredBorder());

        sPaneType = new JScrollPane(txtType);
        sPaneType.setBounds(5, 62, 600, 550);

        getContentPane().add(sPaneType);
//Error
        errorType = new JTextArea();
        errorType.setBounds(5, 615, 600, 200);

        sPaneError = new JScrollPane(errorType);
        sPaneError.setBounds(5, 615, 600, 200);

        getContentPane().add(sPaneError);
//Sintatico
        ParsinTableModel parsinTableModel = new ParsinTableModel();
        sintaticoTable = new JTable(parsinTableModel);
        sintaticoTable.setBounds(605, 62, 600, 450);

        sPaneSintatio = new JScrollPane(sintaticoTable);
        sPaneSintatio.setBounds(605, 62, 600, 450);

        getContentPane().add(sPaneSintatio);

//Tokens
        TokenTableModel ttm = new TokenTableModel();
        tokenTable = new JTable(ttm);
        tokenTable.setBounds(605, 515, 600, 300);

        sPane = new JScrollPane(tokenTable);
        sPane.setBounds(605, 515, 600, 300);

        getContentPane().add(sPane);

        menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 1215, 60);

        newFile = new JButton();
        newFile.setPreferredSize(new Dimension(60, 60));
        newFile.setIcon(new ImageIcon(getClass().getResource("/img/newFile.png")));
        newFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String aux = JOptionPane.showInputDialog("Informe o nome do arquivo");
                if (aux!=null&&!aux.equalsIgnoreCase("")) {
                    fileMain.setName(aux);

                    dc.showDialog(getContentPane(), "Selecionar Diretório");
                    if (!dc.getSelectedFile().getParent().isEmpty()) {
                        fileMain.setAdress(dc.getSelectedFile().getPath());
                        try {
                            fileService.createFile(fileMain);
                        } catch (IOException ex) {
                            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        txtType.setText("");
                    }
                }
            }
        });

        loadFile = new JButton();
        loadFile.setPreferredSize(new Dimension(60, 60));
        loadFile.setIcon(new ImageIcon(getClass().getResource("/img/loadFile.png")));
        loadFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                fc.showDialog(getContentPane(), "Selecionar Arquivo");
                try{
                    fileMain.setAdress(fc.getSelectedFile().getParent());
                    fileMain.setName(fc.getSelectedFile().getName());
                    try {
                        List<String> lines = fileService.catchLineList(fileMain);
                        String text = "";
                        txtType.setRows(lines.size());
                        for (String s : lines) {
                            text += s + "\n";
                        }

                        txtType.setText(text);
                    } catch (IOException ex) {
                        Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }catch (java.lang.NullPointerException e){

                }
            }
        });

        saveFile = new JButton();
        saveFile.setPreferredSize(new Dimension(60, 60));
        saveFile.setIcon(new ImageIcon(getClass().getResource("/img/saveFile.png")));
        saveFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try{


                    if (fileMain.getName()!=null) {
                        try {
                            fileService.rewriteOnFile(fileMain, txtType.getText());
                        } catch (IOException ex) {
                            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        String aux = JOptionPane.showInputDialog("Informe o nome do arquivo");
                        if (!aux.equalsIgnoreCase("")) {
                            fileMain.setName(aux);

                            dc.showDialog(getContentPane(), "Selecionar Diretório");
                            if (!dc.getSelectedFile().getParent().isEmpty()) {
                                fileMain.setAdress(dc.getSelectedFile().getPath());
                                try {
                                    fileService.createFile(fileMain);
                                } catch (IOException ex) {
                                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                txtType.setText("");
                            }
                        }
                    }
                }catch (java.lang.NullPointerException e) {

                }

            }
        });

        deleteFile = new JButton();
        deleteFile.setPreferredSize(new Dimension(60, 60));
        deleteFile.setIcon(new ImageIcon(getClass().getResource("/img/deleteFile.png")));

        playFile = new JButton();
        playFile.setPreferredSize(new Dimension(60, 60));
        playFile.setIcon(new ImageIcon(getClass().getResource("/img/playFile.png")));
        playFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //zera tabela parsing
                nTerminaisStack.clear();
                parsinTableModel.wipe();
                for(Token fnt: nTerminaisStack) {
                    parsinTableModel.addToken(fnt);
                }
                sintaticoTable.setModel(parsinTableModel);

                String array[] = txtType.getText().split("\n");
                lines.clear();
                for (int i = 0; i < array.length; i++) {
                    lines.add(array[i]);
                }
                finalStack.clear();

                Error erro = Analizador.Analize(finalStack, lines);

                if (!erro.isStatus()) {
                    ttm.wipe();

                    for (Token t : finalStack) {
                        ttm.addToken(t);
                    }
                    tokenTable.setModel(ttm);
                }else{
                    ttm.wipe();
                    JOptionPane.showConfirmDialog(rootPane, erro.getMessage()+" na linha: "+ erro.getLine());
                }
            }
        });

        playSintatico = new JButton();
        playSintatico.setPreferredSize(new Dimension(60, 60));
        playSintatico.setIcon(new ImageIcon(getClass().getResource("/img/playSintatico.png")));
        playSintatico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(nTerminaisStack.isEmpty()) {
                    nTerminaisStack.add(new Token(52, "PROGRAMA"));
                }

                //INVERTE PILHAS
                finalStack = invertePilha(finalStack);
                nTerminaisStack = invertePilha(nTerminaisStack);

                //VERIFICA ERRO
                Error error = Analizador.analizadorSintaticoError(finalStack, nTerminaisStack);
                
                if(!gambiarra) {
                    nTerminaisStack = invertePilha(nTerminaisStack);
                    gambiarra = true;
                }

                //DESENVERTE PILHA
                finalStack = invertePilha(finalStack);
                nTerminaisStack = invertePilha(nTerminaisStack);

                ttm.wipe();
                for(Token fs: finalStack) {
                    ttm.addToken(fs);
                }
                tokenTable.setModel(ttm);

                parsinTableModel.wipe();
                for(Token fnt: nTerminaisStack) {
                    parsinTableModel.addToken(fnt);
                }
                sintaticoTable.setModel(parsinTableModel);

            }

        });

        menuBar.add(newFile);
        menuBar.add(loadFile);
        menuBar.add(saveFile);
        menuBar.add(deleteFile);
        menuBar.add(playFile);
        menuBar.add(playSintatico);
        getContentPane().add(menuBar);
    }

    public Stack<Token> invertePilha(Stack<Token> pilha) {
        int x = pilha.toArray().length;
        for(int i = 0;i < x; i++) {
            auxFinalStack.push(pilha.pop());
        }
        pilha.clear();
        pilha.addAll(auxFinalStack);
        auxFinalStack.clear();
        return pilha;
    }

}