package com.ninecats.flashcard;


import java.io.Serializable;

public class Cards implements Serializable {


    String word;
    String def;
    int id_;
    Boolean side;
    //front = 1, back = 0


    public Cards(String inWord, String inDef) {
        this.word = inWord;
        this.def = inDef;
        this.id_ = id_;
        this.side = true;

    }

    public Cards()
    {
        this.word = "herp";
        this.def = "derp";
        this.id_ = -1;
        this.side = true;
    }



    public String getWord() {
        return word;
    }

    public void setWord(String inWord) {
        word = inWord;
    }

    public void setSide(Boolean inSide) {
        side = inSide;
    }

    public void setDef(String inDef) {
        def = inDef;
    }


    public String getDef() {
        return def;
    }

    public boolean getSide() {
        return side;
    }



    public int getId() {
        return id_;
    }
}