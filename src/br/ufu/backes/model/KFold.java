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

    public KFold(int K){
        this.K = K;
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
            createAndPopulateFolds(kNN);
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
                wrongGuesses++;
            }
        }
    }

    private void evaluateClassificatorForMultiFolds(KNearestNeighbor kNN){
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
                    wrongGuesses++;
                }
            }
        }
    }

    private void createAndPopulateFolds(KNearestNeighbor kNN){
        int elementsPerFold = (kNN.getNumberOfElements()/K);

        int mod =  kNN.getNumberOfElements() % K;
        int aux = 0;
        for(int i = 1; i <= K; i++){
            addFold(i,new ArrayList<>(kNN.getDataSet().subList(aux, aux + elementsPerFold)));
            aux += elementsPerFold;
        }

        //distribute the remaining elements through the folds.
        int j = 1;
        for(int i = 0; i< mod; i++){

            foldMap.get(j).add(new Element(kNN.getDataSet().get(aux + i)));

            if(j == K){
                j = 1;
            } else {
                j++;
            }
        }
    }

    private void addFold(int kIndex, List<Element> elements){
        foldMap.put(kIndex, elements);
    }

    private void printMap(){
        for(int i = 1; i<= K; i++){
            List<Element> foldElements = foldMap.get(i);

            System.out.println("Fold: " + i);
            for(Element e : foldElements){
                System.out.println(e.toString());
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
}
