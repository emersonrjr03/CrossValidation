package br.ufu.backes.model;

import java.util.*;
import java.util.stream.Collectors;

public class KNearestNeighbor {
    // K is the number of nearest neighbors
    int K;
    boolean normalize;
    float attributesAverage[];
    float attributesStdDeviation[];
    private int numberOfElements;
    private int numberOfAttributes;

    private List<Element> dataSet;

    private List<Element> foldDataSet;
    private boolean useFoldDataSet = false;

    public KNearestNeighbor(int K, int numberOfElements, int numberOfAttribute, boolean normalize) {
        this.K = K;
        this.numberOfElements = numberOfElements;
        this.numberOfAttributes = numberOfAttribute;
        this.normalize = normalize;
    }

    private void calculateElementEuclideanDistanceFromUnknow(Element element, Element unknownObject){
        double sum = 0;
        for(int i = 0; i < numberOfAttributes; i++){
            double aux = Math.abs(element.getAttribute(i) - unknownObject.getAttribute(i));
            sum += aux * aux;
        }
        element.setDistanceFromUnknowObject(Math.sqrt(sum));
    }


    public void classifyUnknownObject(Element unkownObject, boolean useFoldDataSet){
        this.useFoldDataSet = useFoldDataSet;
        classifyUnknownObject(unkownObject);
        this.useFoldDataSet = false;
    }
    public void classifyUnknownObject(Element unkownObject){
        calculateAllElementsEuclideanDistanceFromUnknow(unkownObject);
        debugDataSet();
        unkownObject.setElementClass(getMostPredominantClassOnNeighbors());
    }

    /**
     * Here we sort the dataSet, and get the first K.
     * And after that we just create a hashMap to count which one is the predominant class
     * between the number of selected neighbors.
     * @return the prerdominant class
     */
    private String getMostPredominantClassOnNeighbors(){
        Collections.sort(getDataSet(), Comparator.comparing(Element::getDistanceFromUnknowObject));
        HashMap<String, Integer> hashMap = new HashMap<>();
        for(int i = 0; i < K; i++){//<2,2>   <3,1> //k = 4
            Element e = getDataSet().get(i);
            if(hashMap.containsKey(e.getElementClass())){
                hashMap.put(e.getElementClass(), hashMap.get(e.getElementClass()) + 1);
            } else {
                hashMap.put(e.getElementClass(), 1);
            }
        }

        debugDataSet();

        Integer max = 0;
        String mostAppearingClass = "";

        for(Map.Entry<String, Integer> entry : hashMap.entrySet()){
            if (entry.getValue() > max) {
                max = entry.getValue();
                mostAppearingClass = entry.getKey();
            }
        }

        return mostAppearingClass;
    }

    private void debugDataSet() {
        System.out.println("ID  DISTANCIA   CLASSE");
        for(Element e : getDataSet()){
            System.out.println(e.getId() + "    " + e.getDistanceFromUnknowObject() + "  " + e.getElementClass());
        }
    }

    private void calculateAllElementsEuclideanDistanceFromUnknow(Element unknownObject){
        for(Element element : getDataSet()){
            calculateElementEuclideanDistanceFromUnknow(element , unknownObject);
        }
    }

    private void normalizeDataSet(){
        attributesAverage = new float[numberOfAttributes];
        attributesStdDeviation = new float[numberOfAttributes];

        //calculating the average
        for(Element element : getDataSet()){
            for(int i = 0; i < numberOfAttributes; i++){
                attributesAverage[i] += element.getAttribute(i);
            }
        }
        for(int i = 0; i < numberOfAttributes; i++){
            attributesAverage[i] = attributesAverage[i]/(float)getDataSet().size();
        }

        //calculating the StandardDeviation
        for(Element element : getDataSet()){
            for(int i = 0; i < numberOfAttributes; i++){
                double aux = element.getAttribute(i) - attributesAverage[i];
                attributesStdDeviation[i] += aux * aux;
            }
        }

        for(int i = 0; i < numberOfAttributes; i++){
            attributesStdDeviation[i] = (float)Math.sqrt(attributesStdDeviation[i]/(float)(getDataSet().size()-1));
        }

        //changing element values to the normalized ones
        for(Element element : getDataSet()){
            for(int i = 0; i < numberOfAttributes; i++){
                element.setAttribute(i, (element.getAttribute(i) - attributesAverage[i])/attributesStdDeviation[i]);
            }
        }
    }

    public void setDataSetAndNormalize(List<Element> dataSet, boolean normalize) {
        this.dataSet = dataSet;

        if(normalize){
            normalizeDataSet();
        }
    }

    public List<Element> getDataSet() {
        if(useFoldDataSet){
            return foldDataSet;
        } else {
            return dataSet;
        }
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public int getNumberOfAttributes() {
        return numberOfAttributes;
    }

    public List<Element> getFoldDataSet() {
        return foldDataSet;
    }

    public void setFoldDataSet(List<Element> foldDataSet) {
        this.foldDataSet = foldDataSet;
    }
}
