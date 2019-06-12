/**
* NGramCount.java
* by Yasmeen Awad
*
* A class containing NGramCounts, which contain an ngram and its frequency in a particular text.
*/

public class NGramCount{
    public String word;
    public int count;

    public NGramCount(String ngram, int count){
        this.word = ngram;
        this.count = count;
    }

    public String getWord(){
        return this.word;
    }

    public int getCount(){
        return this.count;
    }

    public void setWord(String newWord){
        this.word = newWord;
    }

    public void setCount(int newCount){
        this.count = newCount;
    }

    public void print(){
        System.out.println(this.word + ": " + this.count);
    }

    public String nGramToString(){
        return this.word + ": " + Integer.toString(this.count);
    }

}
