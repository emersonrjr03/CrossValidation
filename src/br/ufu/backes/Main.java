package br.ufu.backes;

import br.ufu.backes.util.Classification;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
//        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//        System.out.println("File path:");
//        String filepath = myObj.nextLine();
//        System.out.println("Number of Neiughbors:");
//        int numberOfNeighbors = myObj.nextInt();
//        System.out.println("Number of Folds:");
//        int folds = myObj.nextInt();
//
//        System.out.println("Normalizar dados?(y/n)");
//        String option = myObj.next();
//        boolean normalize;
//        if("y".equals(option)){
//            normalize = true;
//        } else {
//            normalize = false;
//        }

//        Classification c = new Classification(filepath, numberOfNeighbors, folds, normalize);

//        Classification c = new Classification("src/br/ufu/backes/resources/data.txt", 5, 15, true);

        Classification c = new Classification("src/br/ufu/backes/resources/sexo.txt", 3, 1, false);

        c.avaliarQualidadeDeClassificador();


    }
}
