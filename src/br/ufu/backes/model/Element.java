package br.ufu.backes.model;

import java.util.Arrays;

public class Element {
    private String elementClass;
    private double[] attributes;
    private double distanceFromUnknowObject;
    private int Id;

    public Element(String elementClass, double... attributes){
        this.elementClass = elementClass;
        this.attributes = new double[attributes.length];

        for(int i=0; i < attributes.length; i++){
            this.attributes[i] = attributes[i];
        }
    }

    public Element (Element element){
        this.elementClass = element.getElementClass();
        this.attributes = Arrays.copyOf(element.getAttributes(), element.getAttributes().length);
        this.distanceFromUnknowObject = element.getDistanceFromUnknowObject();
    }

    public Element(double... attributes){
        this(null, attributes);
    }

    public String getElementClass() {
        return elementClass;
    }

    public void setElementClass(String elementClass) {
        this.elementClass = elementClass;
    }

    public double[] getAttributes() {
        return attributes;
    }

    public void setAttributes(double[] attributes) {
        this.attributes = attributes;
    }

    public double getAttribute(int index) {
        return attributes[index];
    }

    public void setAttribute(int index, double value) {
        this.attributes[index] = value;
    }

    public double getDistanceFromUnknowObject() {
        return distanceFromUnknowObject;
    }

    public void setDistanceFromUnknowObject(double distanceFromUnknowObject) {
        this.distanceFromUnknowObject = distanceFromUnknowObject;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public String toString() {
        return Arrays.toString(attributes);
    }
}
