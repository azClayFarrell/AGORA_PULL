/**
 *  Scott Barlowe
 *  Starter file for thread exercise
 *
 */

import java.util.*;

public class MatrixAdder{

    public static void main(String[] args){

        int [][] matrix0 = new int[25][];

        Random r = new Random();

        for(int i = 0; i < matrix0.length; i++){
              matrix0[i] = new int[r.nextInt(50000)];
        }
       
        for(int i = 0; i < matrix0.length; i++){
            for(int j = 0; j < matrix0[i].length; j++){
                matrix0[i][j] = 1;
            }
        }

        ArrayList<Thread> threads = new ArrayList<>(matrix0.length);
        SumAccumulator summer = new SumAccumulator();
        for (int i = 0; i < matrix0.length; i++){
            threads.add(new Thread(new AdderWorker(summer, matrix0[i], i)));
        }
        for (Thread adder : threads){
            adder.run();
        }
        try{
            for (Thread adder : threads){
                adder.join();
            }
        }
        catch (InterruptedException ie){
            System.err.println("ERROR: Could not join the threads together");
        }
        System.out.println("SUM --> " + summer.getSum());
        
    }            
}
