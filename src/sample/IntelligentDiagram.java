package sample;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.Sequence.ActionFlow;
import sample.Sequence.Sequence;
import sample.Sequence.SequenceDiagram;
import sample.UseCase.Action;
import sample.UseCase.Actor;
import sample.UseCase.Case;
import sample.UseCase.UseCaseDiagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by darko on 26.8.14.
 */
public class IntelligentDiagram {
    private String data;
    private DatabaseOperations db;
    public ArrayList<UseCaseDiagram> useCaseDiagrams;
    private boolean lastWord;

    public IntelligentDiagram(String text) {
        data = text;
        db = new DatabaseOperations("words.db");
        useCaseDiagrams = new ArrayList<>();
        lastWord = false;
    }

    public boolean isLastWordOfSentence(String word) {
        String endsOfSentence = ".!?;";
        if (endsOfSentence.contains(word.substring(word.length() - 1))) {
            return true;
        }
        return false;
    }
    //todo implement this method for creating sequence diagrams

    public String extractDataSequence() {

        StringBuffer output = new StringBuffer();
        String[] words = splitTextIntoWords(data);
        boolean hasLeft, hasAction;
        hasLeft = hasAction = false;
        List<String> lefts, rights, actions;

        lefts = new ArrayList<>();
        rights = new ArrayList<>();
        actions = new ArrayList<>();


        SequenceDiagram diagram;
        List<ActionFlow> sequences = new ArrayList<>();
        ActionFlow seq = new ActionFlow();
//        List<>
        for (String word : words) {
            if(isLastWordOfSentence(word)){
                lastWord = true;
                word = word.substring(0, word.length() - 1);
            }else{
                lastWord = false;
            }
            if (db.searchNoun(word)) {
                if (hasLeft) {
                    if (hasAction) {
                        seq.setaLeft(new TextField(word));
                    }
                } else {
                    seq = new ActionFlow();
                    seq.setaRight(new TextField(word));
                    hasLeft = true;
                }
            }
            if (db.searchVerb(word)) {
                seq.setActionText(new TextField(word));
                hasAction = true;
            }

            if (lastWord) {
                sequences.add(seq);
                hasLeft = hasAction = false;
            }
        }
        return output.toString();
    }

    public String extractDataUseCase() {

        StringBuffer output = new StringBuffer();
        String[] words = splitTextIntoWords(data);
        boolean hasLeft, hasAction;
        hasLeft = hasAction = false;
        List<String> lefts, rights, actions;

        lefts = new ArrayList<>();
        rights = new ArrayList<>();
        actions = new ArrayList<>();


        UseCaseDiagram diagram;
//        List<>
        for (String word : words) {
           if(isLastWordOfSentence(word)){
               lastWord = true;
               word = word.substring(0, word.length() - 1);
           }else{
               lastWord = false;
           }
            if (db.searchNoun(word)) {
                if (hasLeft) {
                    if (hasAction) {
                        rights.add(word);
                    } else {
                        lefts.add(word);
                    }
                } else {
                    lefts.add(word);
                    hasLeft = true;
                }
            }
            if (db.searchVerb(word)) {
                actions.add(word);
                hasAction = true;
            }

            if (lastWord) {
                diagram = new UseCaseDiagram();
                ArrayList<Case> useCases = new ArrayList<>();
                for (String leftActor: lefts){
                    for(String rightActor: rights){
                        useCases.add(new Case(leftActor, rightActor, actions));
                    }
                }
                diagram.cases = useCases;
                useCaseDiagrams.add(diagram);
                output.append(diagram.toString());
                lefts = new ArrayList<>();
                rights = new ArrayList<>();
                actions = new ArrayList<>();
                hasLeft = hasAction = false;
            }
        }
        return output.toString();
    }

    public static String[] splitTextIntoWords(String text) {
        String forbidden = ",:";
        String[] results = text.split(" ");
        for (int i = 0; i < results.length; i++) {
            if(results[i].length()>0) {
                String lastOne = String.valueOf(results[i].charAt(results[i].length() - 1));
                if (forbidden.contains(lastOne.substring(0))) {
                    results[i] = results[i].substring(0, results[i].length() - 1);
                }
            }
        }
        return results;
    }

}
