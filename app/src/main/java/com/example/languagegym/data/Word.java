package com.example.languagegym.data;

import java.util.List;

public class Word {
    private String word;
    private String translation;
    private String partOfSpeech;
    private String gender;
    private List<String> declension;
    private List<String> synonyms;

    public Word(String word, String translation, String partOfSpeech, String gender, List<String> declension, List<String> synonyms) {
        this.word = word;
        this.translation = translation;
        this.partOfSpeech = partOfSpeech;
        this.gender = gender;
        this.declension = declension;
        this.synonyms = synonyms;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getDeclension() {
        return declension;
    }

    public void setDeclension(List<String> declension) {
        this.declension = declension;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }
}