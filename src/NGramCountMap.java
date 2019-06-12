/**
* NGramCountMap.java
* by Yasmeen Awad
*
* Contains a NGramCountMap object, which implements a binary search tree, and a Node object which
* make up the tree. Nodes have data (ngrams and their frequency) as well as left and right children.
*/

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NGramCountMap{
    public Node root;

    public NGramCountMap(){
        this.root = null;
    }

    public class Node{
        public String ngram;
        public int count;
        public Node leftChild;
        public Node rightChild;

        public Node(String ngram){
            this.ngram = ngram;
            this.count = 1;
            this.leftChild = null;
            this.rightChild = null;
        }

        public String getNgram(){
            return this.ngram;
        }
        public int getCount(){
            return this.count;
        }
        public Node getLeftChild(){
            return this.leftChild;
        }
        public Node getRightChild(){
            return this.rightChild;
        }
        public void increment(){
            this.count += 1;
        }
        public void setLeftChild(Node leftChild){
            this.leftChild = leftChild;
        }
        public void setRightChild(Node rightChild){
            this.rightChild = rightChild;
        }
    }

    public Node getRoot(){
        return this.root;
    }

    public boolean contains(String ngram){
        Node currentNode = this.root;
        while (currentNode != null){
            int comp = currentNode.getNgram().compareTo(ngram);
            if (comp == 0){
                return true;
            } else if (comp > 0){
                currentNode = currentNode.getLeftChild();
            } else if (comp < 0){
                currentNode = currentNode.getRightChild();
            }
        }
        return false;
    }

    public boolean isEmpty(){
        if (this.root == null){
            return true;
        }
        return false;
    }

    /**
    * If the specified ngram is already in this NgramCountMap, then its count is increased by one.
    * Otherwise, the ngram is added to the map with a count of one.
    */
    public void put(String ngram){
        // If tree is empty, add node as the root
        if (this.isEmpty()){
            Node newNode = new Node(ngram);
            this.root = newNode;
            return;
        }
        // Search for ngram in tree; if it exists then it will increment and return
        Node currentNode = this.root;
        while (currentNode != null){
            int comp = currentNode.getNgram().compareTo(ngram);
            if (comp == 0){
                currentNode.increment();
                return;
            } else if (comp > 0){
                if (currentNode.getLeftChild() == null){
                    break;
                }
                currentNode = currentNode.getLeftChild();
            } else if (comp < 0){
                if (currentNode.getRightChild() == null){
                    break;
                }
                currentNode = currentNode.getRightChild();
            }
        }
        // If it isn't in tree, add it
        Node newNode = new Node(ngram);
        Node prevNode = this.root;
        while (prevNode != null){
            int comp = prevNode.getNgram().compareTo(ngram);
            if (comp > 0){
                if (prevNode.getLeftChild() == null){
                    prevNode.setLeftChild(newNode);
                    return;
                }
                prevNode = prevNode.getLeftChild();

            } else {
                if (prevNode.getRightChild() == null){
                    prevNode.setRightChild(newNode);
                    return;
                } else {
                    prevNode = prevNode.getRightChild();
                }
            }
        }
    }

    public static void mergeSort(NGramCount[] a) {
        if (a == null || a.length == 0) {
            return;
        }
        NGramCount[] tempArray = new NGramCount[a.length];
        mergeSortHelper(a, tempArray, 0, a.length - 1);
    }

    private static void mergeSortHelper(NGramCount[] a, NGramCount[] tempArray, int bottom, int top) {
        if (bottom < top) {
            int middle = (bottom + top) / 2;
            mergeSortHelper(a, tempArray, bottom, middle);
            mergeSortHelper(a, tempArray, middle + 1, top);
            merge(a, tempArray, bottom, middle, top);
        }
    }

    private static void merge(NGramCount[] a, NGramCount[] tempArray, int bottom, int middle, int top) {
        int i = bottom;
        int j = middle + 1;
        int k = 0;

        // While both lists still have items in them, merge those
        // items into the temporary array temp[].
        while (i <= middle && j <= top) {
            if (a[i].getCount() > a[j].getCount()) {
                tempArray[k] = a[i];
                i++;
            } else {
                tempArray[k] = a[j];
                j++;
            }
            k++;
        }

        // Once one of the lists has been exhausted, dump the other
        // list into the remainder of temp[].
        if (i > middle) {
            while (j <= top) {
                tempArray[k] = a[j];
                j++;
                k++;
            }
        } else {
            while (i <= middle) {
                tempArray[k] = a[i];
                i++;
                k++;
            }
        }

        // Copy tempArray[] into the proper portion of a[].
        for (i = bottom; i <= top; i++) {
            a[i] = tempArray[i - bottom];
        }
    }

    /**
    * Returns a list of NgramCounter objects, one per ngram stored in the NgramCountMap, sorted
    * in decreasing order by count.
    */
    public ArrayList<NGramCount> getNgramCountsByCount(){
        ArrayList<NGramCount> list = this.getNgramCountsByNgram();
        NGramCount[] array = new NGramCount[list.size()];
        for (int j = 0; j < array.length; j++){
            array[j] = list.get(j);
        }
        mergeSort(array);
        ArrayList<NGramCount> newList = new ArrayList<NGramCount>();
        for (int i = 0; i < array.length; i++){
            newList.add(array[i]);
        }
        return newList;
    }

    /**
    * Returns a list of NgramCount objects, one per ngram stored in this NgramCountMap, sorted
    * alphabetically by ngram.
    */
    public ArrayList<NGramCount> getNgramCountsByNgram(){
        ArrayList<NGramCount> list = new ArrayList<NGramCount>();
        inOrderTraversal(this.root, list);
        return list;

    }
    /**
    * A recursive helper function for getNgramCountsByNgram. Takes in the node of the root of the
    * tree and an ArrayList in which you want your NGramCounts to be stored.
    */
    public static void inOrderTraversal(Node root, ArrayList<NGramCount> list){
        if (root != null){
            inOrderTraversal(root.getLeftChild(), list);
            NGramCount a = new NGramCount(root.getNgram(), root.getCount());
            list.add(a);
            inOrderTraversal(root.getRightChild(), list);
            NGramCount b = new NGramCount(root.getNgram(), root.getCount());
        }
    }
}
