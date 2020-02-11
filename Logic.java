/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordsearch2;

/**
 *
 * @author nandy
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
/**
 *
 * @author nandy
 */
public class Logic {
    private File textFile;
    private Scanner readFromFile;
    public Logic(String filename) throws FileNotFoundException{
        try{
        textFile = new File(filename);
        readFromFile = new Scanner(textFile);
    }
        catch(FileNotFoundException f){
            f.printStackTrace();
        }
    
}
    private String[] fileToStringArr(){
        StringBuilder content = new StringBuilder();
        while(readFromFile.hasNextLine()){
            content.append(readFromFile.nextLine() + ",");
        }
        String buffer = content.toString();
        String[] rv = buffer.split(",");
        return rv;
    }
    private ArrayList<String> filterSize(String[] content, int maxSize){
        ArrayList<String> filtered = new ArrayList<String>();
        for(int i = 0; i < content.length;i++){
            if(content[i].length() <= maxSize){
                filtered.add(content[i]);
            }
        }
        return filtered;
    }
    public Board wordsToSearch(int size){
        String[] words = new String[size - 2];
        ArrayList<String> content= filterSize(fileToStringArr(), size-2);
        int totalNumWords = content.size();
        Random rnd = new Random();
        for(int i = 0; i < words.length; i++){
            int rndIdx = rnd.nextInt(totalNumWords);
            words[i] = content.get(rndIdx);
        }
        return new Board(size, words);
        
    }

}