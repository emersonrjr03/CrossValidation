package br.ufu.backes.model;

import java.util.*;

public class KFold {
    // Split the dataset into k groups
    private int K;

    //numero de amostras
    private int N;

    private Map<Integer, List<Element>> foldMap;
    private double rightGuesses;
    private double wrongGuesses;
    private boolean debugMode;

    public KFold(int K, boolean debugMode){
        this.K = K;
        this.debugMode = debugMode;

        foldMap = new HashMap<Integer, List<Element>>();
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }

    public Map<Integer, List<Element>> getFoldMap() {
        return foldMap;
    }

    public void avaliarQualidadeDeClassificador(KNearestNeighbor kNN) throws Exception {
        if(K==1){
            evaluateClassificatorForSingleFold(kNN);
        } else {
            createAndPopulateFoldsDistributively(kNN);
            printMap();
            evaluateClassificatorForMultiFolds(kNN);
        }
    }

    /**
     * This function is used when we have a single fold, because on that cause
     * our dataset will be every element but the one that is being classified.
     * @param kNN
     */
    private void evaluateClassificatorForSingleFold(KNearestNeighbor kNN){
        for(Element testElement : kNN.getDataSet()) {
            List<Element> trainingDataset = new ArrayList<>(kNN.getDataSet());
            trainingDataset.remove(testElement);

            kNN.setFoldDataSet(trainingDataset);

            String realClass = testElement.getElementClass();
            kNN.classifyUnknownObject(testElement, true);
            String calculatedClass = testElement.getElementClass();

            if(realClass.equals(calculatedClass)){
                rightGuesses++;
            } else {
                logInfo("ERROR: " + testElement.toString() + "\n");
                wrongGuesses++;
            }
        }
    }

    /**
     * This function is used to evaluate the given classificator when you have k > 1.
     * @param kNN
     */
    private void evaluateClassificatorForMultiFolds(KNearestNeighbor kNN){
        //go through all the folds, and from each fold different than the one that we are now,
        //we create training folds.
        for(Map.Entry<Integer, List<Element>> testingFold : foldMap.entrySet()){
            //filling the foldDataSet based on all the folds that doesn't belong to the testfold
            List<Element> trainingDataSet = new ArrayList<>();
            for(Map.Entry<Integer, List<Element>> trainingFold : foldMap.entrySet()){
                if(trainingFold.getKey() == testingFold.getKey()){
                    continue;
                } else {
                    trainingDataSet.addAll(trainingFold.getValue());
                }
            }
            kNN.setFoldDataSet(trainingDataSet);

            //now we go through all the elements, comparing each one with the trainingDataSet.
            for(Element testElement : testingFold.getValue()) {
                String realClass = testElement.getElementClass();

                kNN.classifyUnknownObject(testElement, true);
                String calculatedClass = testElement.getElementClass();

                if(realClass.equals(calculatedClass)){
                    rightGuesses++;
                } else {
                    logInfo("ERROR: " + testElement.toString() + "\n");
                    wrongGuesses++;
                }
            }
        }
    }

    private void createAndPopulateFoldsDistributively(KNearestNeighbor kNN){
        int foldIndex = 1;
        for(int i=0; i < kNN.getNumberOfElements(); i++){
            addElementToFold(foldIndex, new Element(kNN.getDataSet().get(i)));
            if(foldIndex == K){
                foldIndex = 1;
            } else {
                foldIndex++;
            }
        }
    }

    private void addElementToFold(int kIndex, Element element){
        if(foldMap.get(kIndex) == null){
            foldMap.put(kIndex, new ArrayList<>());
        }
        foldMap.get(kIndex).add(element);
    }

    private void printMap(){
        for(int i = 1; i<= K; i++){
            List<Element> foldElements = foldMap.get(i);

            for(Element e : foldElements){
                logInfo(e.toString() + " Fold: " + i);
            }
        }

    }

    public double getRightGuesses() {
        return rightGuesses;
    }

    public double getWrongGuesses() {
        return wrongGuesses;
    }

    @Override
    public String toString() {
        return "KFold{" +
                "K=" + K +
                ", N=" + N +
                ", trainingData=" + foldMap +
                "}";
    }

    private void logInfo(String msg){
        if(debugMode){
            System.out.println(msg);
        }
    }
}
