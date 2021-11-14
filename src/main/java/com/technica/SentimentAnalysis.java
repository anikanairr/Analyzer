package com.technica;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;


import java.util.*;

public class SentimentAnalysis {
    private static StanfordCoreNLP stanfordCoreNLP;
    private static String propertiesName = "tokenize, ssplit, pos, lemma, ner, parse, sentiment";
    private static Properties properties;

    public static void main(String[] args) {
        // setup core nlp

        setupCoreNLP();
        // in a loop read input and give sentiment

        Scanner in = new Scanner(System.in);
        System.out.print("Enter the words you want to block separated by commas: ");
        String words = in.nextLine();
        List<String> wordList = Arrays.asList(words.split(","));



        System.out.println("Enter caption: ");
        String text = in.nextLine();

        String sentiment = getSentiment(text);

        if (sentiment.equalsIgnoreCase("Negative")) {
            System.out.println("Sentiment is Negative.");
            if (containBlockedWord(wordList, text, sentiment)) {
                System.out.println("Negative Sentiment does include blocked word.");
            } else if (!containBlockedWord(wordList, text, sentiment)) {
                System.out.println("Negative Sentiment does not include blocked word.");
            }
        } else if (sentiment.equalsIgnoreCase("Positive")) {
            System.out.println("Sentiment is Positive.");
        } else if (sentiment.equalsIgnoreCase("Neutral")) {
            System.out.println("Sentiment is Neutral.");
        }



    }

    private static boolean containBlockedWord(List<String> wordList, String text, String sentiment) {
        if (sentiment.equalsIgnoreCase("Negative")) {
            for (String s : wordList) {
                if (text.toLowerCase().contains(s.toLowerCase())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private static void setupCoreNLP() {
        System.out.println("Loading NLP...");
        properties = new Properties();
        properties.setProperty("annotators", propertiesName);
        stanfordCoreNLP = new StanfordCoreNLP(properties);
        System.out.println("Successfully Loaded NLP...");
    }

    private static String getSentiment(String text) {
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreSentence> sentences = coreDocument.sentences();
        boolean hasPositive = false;
        boolean hasNeutral = false;

        for (CoreSentence sentence : sentences) {
            String sentiment = sentence.sentiment();
            if (sentiment.equalsIgnoreCase("negative")) {
                return "Negative";
            } else if (sentiment.equalsIgnoreCase("positive")) {
                hasPositive = true;
            } else if (sentiment.equalsIgnoreCase("neutral")) {
                hasNeutral = true;
            }
        }

        if (hasPositive && hasNeutral) {
            return "Positive";
        } if (hasPositive) {
            return "Positive";
        } if (hasNeutral) {
            return "Neutral";
        }

        return "Unknown";
    }
}
