package br.ufu.backes.model;

import java.util.*;

public class KFold {
    // Split the dataset into k groups
    private int K;

    //numero de amostras
    private int N;

    private List<Element> allData;
    private Map<Integer, List<Element>> foldMap;
    private double rightGuesses;
    private double wrongGuesses;

    public KFold(int K){
        this.K = K;
        allData = new ArrayList<>();
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

    public void addElementToData(Element e){
        allData.add(e);
    }

    public List<Element> getAllData() {
        return allData;
    }

    public void setAllData(List<Element> allData) {
        this.allData = allData;
    }

    public Map<Integer, List<Element>> getFoldMap() {
        return foldMap;
    }

    public void avaliarQualidadeDeClassificador(KNearestNeighbor kNN) throws Exception {

        createAndPopulateFolds(kNN);

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
//        printMap();
    }

    private void createAndPopulateFolds(KNearestNeighbor kNN){
        int elementsPerFold = (kNN.getNumberOfElements()/K);
        //k = 4
        //12
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
                ", allData=" + allData +
                ", trainingData=" + foldMap +
                "}";
    }
}
