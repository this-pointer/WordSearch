/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordsearch2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.*;
import java.util.Arrays;
import javafx.util.Duration;
import javax.swing.JOptionPane;

/**
 *
 * @author nandy
 */
public class GUI extends Application {
    private BorderPane root;
    private VBox wordList;
    private HBox bottomButtons;
    private GridPane grid;
    private Logic logic;
    private int boardSize;
    private Scene actualGame;
    private VBox userInputLayout;
    private Scene inputScene;
    private Scene gameOverScene;
    private BorderPane gameOverLayout;
    private Stage window;
    private ArrayList<Text> TextList = new ArrayList();
    private ArrayList<Button> selectedLetters = new ArrayList();
    String defaultStyle;
    private void makeGrid(int n) throws FileNotFoundException{
        try{
         logic = new Logic("wordSearchWords.txt");
        }
        catch(FileNotFoundException f){
            f.printStackTrace();
        }
        Board board = logic.wordsToSearch(n);
        board.addWords();
        board.fillRandom();
        Button[][] matrix = board.boardToButtons();
        for(int r = 0; r < n; r++){
            for(int c = 0; c < n; c++){
                Button b = matrix[r][c];
                b.setOnAction(e->
                { b.setStyle("-fx-background-color: green;");
                  selectedLetters.add(b);  
                });
            
        }
        }
        root = new BorderPane();
        grid = new GridPane();
        wordList = new VBox(20);
        bottomButtons = new HBox(20);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                grid.add(matrix[i][j], j, i+1);
            }
        }
        String[] words = board.getWords();
        for(String word : words){
            Text t = new Text(word);
            wordList.getChildren().add(t);
            TextList.add(t);
        }
        Button checkSelected = new Button("check");
        checkSelected.setOnAction(e->checkSelected());
        Button quitGame = new Button("quit");
        quitGame.setOnAction(e->window.close());
        bottomButtons.getChildren().addAll(checkSelected, quitGame);
       root.setCenter(grid);
       root.setRight(wordList);
       root.setBottom(bottomButtons);
       
    }
    public void unselect(){
        for(Button b: selectedLetters){
            b.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        }
        selectedLetters.clear();
        
    }
    public void checkSelected(){
        String selection = buttonListToStr(selectedLetters);
        boolean rightAnswer = false;
        for(Text t: TextList){
            if(sortedString(selection).equals(sortedString(t.getText()))){
                rightAnswer = true;
                t.setStrikethrough(true);
                TextList.remove(t);
                break;
            }
        }
        if(!rightAnswer){
            unselect();
        }
        if(TextList.size() == 0){
            gameOver();
        }
        selectedLetters.clear();
    }
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        window = primaryStage;
        userInputLayout = new VBox(20);
        Label prompt = new Label("Please enter a size for a n by n wordsearch game");
        TextField input = new TextField();
        Button submitButton = new Button("submit");
        Button chooseWords = new Button("choose words");
        chooseWords.setOnAction(e->chooseOwnWords());
        submitButton.setOnAction(e -> 
        {
            boardSize = Integer.parseInt(input.getText());
            try {
                makeGrid(boardSize);
                actualGame = new Scene(root);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            primaryStage.setScene(actualGame);
        });
        userInputLayout.getChildren().addAll(prompt, input, submitButton, chooseWords);
        inputScene = new Scene(userInputLayout);
        primaryStage.setTitle("WORDSEARCH");
        primaryStage.setScene(inputScene);
        primaryStage.show();
    }
    public void gameOver(){
        gameOverLayout = new BorderPane();
        Text gameOverText = new Text("YOU WON!");
        gameOverText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 30));
        Button quitGame = new Button("quit");
        quitGame.setOnAction(e->window.close());
        Button playAgain = new Button("Play Again");
        playAgain.setOnAction(e-> window.setScene(inputScene));
        HBox bottomButtons = new HBox(20);
        bottomButtons.getChildren().addAll(quitGame, playAgain);
        gameOverLayout.setTop(gameOverText);
        gameOverLayout.setBottom(bottomButtons);
        gameOverScene = new Scene(gameOverLayout);
        window.setScene(gameOverScene);
    }
    private  String sortedString(String s){
        char[] tmp = s.toCharArray();
        Arrays.sort(tmp);
        return Arrays.toString(tmp);
    }
    private String buttonListToStr(ArrayList<Button> buttons){
        StringBuilder tmp = new StringBuilder();
        for(Button b: buttons){
            tmp.append(b.getText());
        }
        return tmp.toString();
    } 
    public void chooseOwnWords(){
        userInputLayout = new VBox(20);
        Label prompt = new Label("Please enter a size for a n by n wordsearch game");
        TextField input = new TextField();
        Button submitButton = new Button("submit");
        boardSize = Integer.parseInt(JOptionPane.showInputDialog(null,  "Enter your size:"));
        String[] words = new String[boardSize-2];
        while(boardSize < 3) {
            boardSize = Integer.parseInt(JOptionPane.showInputDialog(null,  "Choose a larger size:"));
	}
		for(int i=0; i< words.length; i++) {
			int lastVal = (i+1)%10;
			String postFix;
			if(lastVal == 1 && (i<10 || i+1>20)) {
				postFix = "st";
			}else if(lastVal == 2 && (i<10 || i+1>20)) {
				postFix = "nd";
			}else if(lastVal == 3 && (i<10 || i+1>20)) {
				postFix = "rd";
			}else{
				postFix = "th";
			}
			String word = JOptionPane.showInputDialog(null,  "Enter your " + (i+1) + postFix + " word:");
			while(word.length() > boardSize - 2) {
				word = JOptionPane.showInputDialog(null,  "Last word was too long, try again:");
			}
			words[i] = word;
		}
		Board b = new Board(boardSize, words);
		b.addWords();
		b.fillRandom(); 
        Button[][] matrix = b.boardToButtons();
        for(int r = 0; r < boardSize; r++){
            for(int c = 0; c < boardSize; c++){
                Button button = matrix[r][c];
                button.setOnAction(e->
                { button.setStyle("-fx-background-color: green;");
                  selectedLetters.add(button);  
                });
            
        }
        }
        root = new BorderPane();
        grid = new GridPane();
        wordList = new VBox(20);
        bottomButtons = new HBox(20);
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                grid.add(matrix[i][j], j, i+1);
            }
        }
        for(String word : words){
            Text t = new Text(word);
            wordList.getChildren().add(t);
            TextList.add(t);
        }
                Button checkSelected = new Button("check");
        checkSelected.setOnAction(e->checkSelected());
        Button quitGame = new Button("quit");
        quitGame.setOnAction(e->window.close());
        bottomButtons.getChildren().addAll(checkSelected, quitGame);
        root.setCenter(grid);
        root.setRight(wordList);
        root.setBottom(bottomButtons);
        actualGame = new Scene(root);
        window.setScene(actualGame);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
