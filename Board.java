/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordsearch2;
import javafx.scene.control.Button;

public class Board {
	private int length;
	private char[][] layout;
	private String[] orientation;
	private String[] words;
	private int[] x;
	private int[] y;
	
	public Board(int boardSize, String[] words) {
		length = boardSize;
		layout = new char[boardSize][boardSize];
		for(int r = 0; r < boardSize; r++) {
			for(int c = 0; c < boardSize; c++) {
				layout[r][c] = ' ';
			}
		}
		orientation = new String[words.length];
		this.words = words;
		x = new int[words.length];
		y = new int[words.length];
	}
	
	public void fillRandom() {
		for(int r = 0; r < length; r++) {
			for(int c = 0; c < length; c++) {
				if(layout[r][c] == ' ') {
					char random = (char)(97 + (int)(26*Math.random()));
					layout[r][c] = random;
				}
			}
		}
	}
	
	public void displayBoard() {
		for(int r = 0; r < length; r++) {
			for(int c = 0; c < length; c++) {
				System.out.print(layout[r][c] + " ");
			}
			System.out.println();
		}
	}
	
	public void addWords() {
		addFirstWord();
		for(int i=1; i<words.length; i++) {
			addNextWord(words, i);
		}
	}
	
	public void addFirstWord() {
		String s = words[0];
		int randCol = (int)(length * Math.random());
		while(randCol + s.length() > length && randCol - s.length() + 1 < 0) {
			randCol = (int)(length * Math.random());
		}
		boolean isForwards = true;
		if(randCol + s.length() > length) {
			isForwards = false;
			orientation[0] = "left";
		}else{
			orientation[0] = "right";
		}
		int randRow = (int)(length * Math.random());
		char[] firstWord = new char[s.length()];
		for(int i=0; i<s.length(); i++) {
			firstWord[i] = s.charAt(i);
		}
		if(isForwards) {
			for(int i=0; i<firstWord.length; i++) {
				layout[randRow][randCol+i] = firstWord[i];
			}
		}else{
			for(int i=0; i<firstWord.length; i++) {
				layout[randRow][randCol-i] = firstWord[i];
			}
		}
		x[0] = randCol;
		y[0] = randRow;
	}
	
	public int[] overlap(int n, int q) {
		int indexOfFirst = -1;
		int indexOfSecond = -1;
		boolean foundOverlap = false;
		for(int i=0; i < words[n-q].length(); i++) {
			if(!foundOverlap) {
				for(int j=0; j<words[n].length(); j++) {
					if(words[n-q].charAt(i) == words[n].charAt(j) && !foundOverlap) {
						indexOfFirst = i;
						indexOfSecond = j;
						foundOverlap = true;
					}
				}
			}
		}
		int[] arr = {indexOfFirst, indexOfSecond};
		return arr;
	}
	
	public void addNextWord(String[] words, int n) {
		int[] arr = {-1, -1};
		for(int i=1; i<=n; i++) {
			if(overlap(n, i)[0] != -1) {
				arr = overlap(n, i);
				placeWord(n, n-i, arr);
				break;
			}
		}
		if(arr[0] == -1) {
			randomPlacement(words[n], n);
		}
	}
	
	public void randomPlacement(String s, int n) {
		if((int)(2 * Math.random()) == 0) {	
			int randCol = (int)(length * Math.random());
			int randRow = (int)(length * Math.random());
			while((randCol + s.length() > length && randCol - s.length()  < -1) || noCollision("horizontal", s.length(), randRow, randCol).equals("nope")) {
				randCol = (int)(length * Math.random());
				randRow = (int)(length * Math.random());
			}
			boolean isForwards;
			if(noCollision("horizontal", s.length(), randRow, randCol).equals("right")) {
				isForwards = true;
				orientation[n] = "right";
			}else{
				isForwards = false;
				orientation[n] = "left";
			}
			char[] firstWord = new char[s.length()];
			for(int i=0; i<s.length(); i++) {
				firstWord[i] = s.charAt(i);
			}
			if(isForwards) {
				for(int i=0; i<firstWord.length; i++) {
					layout[randRow][randCol+i] = firstWord[i];
				}
			}else{
				for(int i=0; i<firstWord.length; i++) {
					layout[randRow][randCol-i] = firstWord[i];
				}
			}
			x[n] = randCol;
			y[n] = randRow;
		}else{
			int randCol = (int)(length * Math.random());
			int randRow = (int)(length * Math.random());
			while((randRow + s.length() > length && randRow - s.length()  < -1) || noCollision("vertical", s.length(), randRow, randCol).equals("nope")) {
				randCol = (int)(length * Math.random());
				randRow = (int)(length * Math.random());
			}
			boolean isUp;
			if(noCollision("vertical", s.length(), randRow, randCol).equals("up")) {
				isUp = true;
				orientation[n] = "up";
			}else{
				isUp = false;
				orientation[n] = "down";
			}
			System.out.println(orientation[n]);
			char[] firstWord = new char[s.length()];
			for(int i=0; i<s.length(); i++) {
				firstWord[i] = s.charAt(i);
			}
			if(isUp) {
				for(int i=0; i<firstWord.length; i++) {
					layout[randRow - i][randCol] = firstWord[i];
				}
			}else{
				for(int i=0; i<firstWord.length; i++) {
					System.out.println("row: " + (randRow + i));
					System.out.println("col: " + randCol);
					layout[randRow + i][randCol] = firstWord[i];
				}
			}
			x[n] = randCol;
			y[n] = randRow;
		}
	}
	
	public String noCollision(String orientation, int length, int r, int c) {
		String s = "nope";
		if(orientation.equals("right") || orientation.equals("left") || orientation.equals("horizontal")) {
			if(c + length <= this.length) {
				System.out.println("c: " + c);
				System.out.println("length: " + length);
				s = "right";
				for(int i=0; i<length; i++) {
					if(layout[r][c+i] != ' ') {
						s = "nope";
					}
				}
				if(s.equals("right")) {
					return s;
				}
			}
			if(c - length >= -1){
				System.out.println("c: " + c);
				System.out.println("length: " + length);
				s = "left";
				for(int i=0; i<length; i++) {
					if(layout[r][c-i] != ' ') {
						s = "nope";
					}
				}
				if(s.equals("left")) {
					return s;
				}
			}
			return s;
		}else{
			if(r + length <= this.length) {
				s = "down";
				System.out.println("r: " + r);
				System.out.println("length: " + length);
				for(int i=0; i<length; i++) {
					if(layout[r+i][c] != ' ') {
						s = "nope";
					}
				}
				if(s.equals("up")) {
					return s;
				}
			}
			if(r - length >= -1){
				System.out.println("r: " + r);
				System.out.println("length: " + length);
				s = "up";
				for(int i=0; i<length; i++) {
					if(layout[r-i][c] != ' ') {
						s = "nope";
					}
				}
				if(s.equals("down")) {
					return s;
				}
			}
			return s;
		}
	}
	
	public String noCollision(String orientation, int length, int r, int c, int xCollision, int yCollision) {
		String s = "nope";
		if(c>=this.length || c<0 || r>=this.length || r<0) {
			return "nope";
		}
		if(orientation.equals("right") || orientation.equals("left") || orientation.equals("horizontal")) {
			if(c + length <= this.length) {
				System.out.println("c: " + c);
				System.out.println("length: " + length);
				s = "right";
				for(int i=0; i<length; i++) {
					if(layout[r][c+i] != ' ' && (r!=yCollision || c+i!=xCollision)) {
						s = "nope";
					}
				}
				if(s.equals("right")) {
					return s;
				}
			}
			if(c - length >= -1){
				System.out.println("c: " + c);
				System.out.println("length: " + length);
				s = "left";
				for(int i=0; i<length; i++) {
					if(layout[r][c-i] != ' ' && (r!=yCollision || c-i!=xCollision)) {
						s = "nope";
					}
				}
				if(s.equals("left")) {
					return s;
				}
			}
			return s;
		}else{
			if(r + length <= this.length) {
				s = "down";
				System.out.println("r: " + r);
				System.out.println("length: " + length);
				for(int i=0; i<length; i++) {
					if(layout[r+i][c] != ' ' && (r+i!=yCollision || c!=xCollision)) {
						s = "nope";
					}
				}
				if(s.equals("up")) {
					return s;
				}
			}
			if(r - length >= -1){
				System.out.println("r: " + r);
				System.out.println("c: " + c);
				System.out.println("length: " + length);
				s = "up";
				for(int i=0; i<length; i++) {
					if(layout[r-i][c] != ' ' && (r-i!=yCollision || c!=xCollision)) {
						s = "nope";
					}
				}
				if(s.equals("down")) {
					return s;
				}
			}
			return s;
		}
	}
	
	public void placeWord(int newWord, int old, int[] arr) {
		int xCollision;
		int yCollision;
		if(orientation[old].equals("up")) {
			xCollision = x[old];
			yCollision = y[old] - arr[0];
			if(!noCollision("right", words[newWord].length(), yCollision, xCollision - arr[1], xCollision, yCollision).equals("right") && !noCollision("left", words[newWord].length(), yCollision, xCollision + arr[1], xCollision, yCollision).equals("left")) {
				randomPlacement(words[newWord], newWord);
			}else if(noCollision("left", words[newWord].length(), yCollision, xCollision + arr[1], xCollision, yCollision).equals("left")) {
				for(int i=0; i<words[newWord].length(); i++) {
					layout[yCollision][xCollision+arr[1]-i] = words[newWord].charAt(i);
				}
				orientation[newWord] = "left";
			}else{
				for(int i=0; i<words[newWord].length(); i++) {
					layout[yCollision][xCollision-arr[1]+i] = words[newWord].charAt(i);
				}
				orientation[newWord] = "right";
			}
		}else if(orientation[old].equals("down")) {
			xCollision = x[old];
			yCollision = y[old] + arr[0];
			if(!noCollision("right", words[newWord].length(), yCollision, xCollision - arr[1], xCollision, yCollision).equals("right") && !noCollision("left", words[newWord].length(), yCollision, xCollision + arr[1], xCollision, yCollision).equals("left")) {
				randomPlacement(words[newWord], newWord);
			}else if(noCollision("left", words[newWord].length(), yCollision, xCollision + arr[1], xCollision, yCollision).equals("left")) {
				for(int i=0; i<words[newWord].length(); i++) {
					layout[yCollision][xCollision+arr[1]-i] = words[newWord].charAt(i);
				}
				orientation[newWord] = "left";
			}else{
				for(int i=0; i<words[newWord].length(); i++) {
					layout[yCollision][xCollision-arr[1]+i] = words[newWord].charAt(i);
				}
				orientation[newWord] = "right";
			}
		}else if(orientation[old].equals("right")) {
			xCollision = x[old] + arr[0];
			System.out.println("xCollision: " + xCollision);
			yCollision = y[old];
			System.out.println("yCollision: " + yCollision);
			if(!noCollision("up", words[newWord].length(), yCollision + arr[1], xCollision, xCollision, yCollision).equals("up") && !noCollision("down", words[newWord].length(), yCollision - arr[1], xCollision, xCollision, yCollision).equals("down")) {
				randomPlacement(words[newWord], newWord);
			}else if(noCollision("down", words[newWord].length(), yCollision - arr[1], xCollision, xCollision, yCollision).equals("down")) {
				for(int i=0; i<words[newWord].length(); i++) {
					System.out.println("row: " + (yCollision-arr[1]+i));
					System.out.println("col: " + xCollision);
					layout[yCollision-arr[1]+i][xCollision] = words[newWord].charAt(i);
				}
				orientation[newWord] = "down";
			}else{
				for(int i=0; i<words[newWord].length(); i++) {
					System.out.println("row: " + (yCollision+arr[1]-i));
					System.out.println("col: " + xCollision);
					layout[yCollision+arr[1]-i][xCollision] = words[newWord].charAt(i);
				}
				orientation[newWord] = "up";
			}
		}else{
			xCollision = x[old] - arr[0];
			System.out.println("xCollision: " + xCollision);
			yCollision = y[old];
			System.out.println("yCollision: " + yCollision);
			if(!noCollision("up", words[newWord].length(), yCollision + arr[1], xCollision, xCollision, yCollision).equals("up") && !noCollision("down", words[newWord].length(), yCollision - arr[1], xCollision, xCollision, yCollision).equals("down")) {
				randomPlacement(words[newWord], newWord);
			}else if(noCollision("down", words[newWord].length(), yCollision - arr[1], xCollision, xCollision, yCollision).equals("down")) {
				for(int i=0; i<words[newWord].length(); i++) {
					System.out.println("row: " + (yCollision-arr[1]+i));
					System.out.println("col: " + xCollision);
					layout[yCollision-arr[1]+i][xCollision] = words[newWord].charAt(i);
				}
				orientation[newWord] = "down";
			}else{
				for(int i=0; i<words[newWord].length(); i++) {
					System.out.println("row: " + (yCollision+arr[1]-i+1));
					System.out.println("col: " + xCollision);
					layout[yCollision+arr[1]-i][xCollision] = words[newWord].charAt(i);
				}
				orientation[newWord] = "up";
			}
		}
	}


    public Button[][] boardToButtons(){
        Button[][] grid = new Button[length][length];
         for(int i = 0; i < length; i++){
            for(int j = 0; j < length; j++){
                grid[i][j] = new Button(""+layout[i][j]);
            }
        }
         return grid;
    }
    public String[] getWords(){
        return words;
    }
}
