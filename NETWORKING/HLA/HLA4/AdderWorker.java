/**
 *  Scott Barlowe
 * 
 *  Mostly empty file
 */

public class AdderWorker implements Runnable{
	/**a variable to add the sums as the threads finish*/
	private SumAccumulator summer;
	/**an array to hold a row of the matrix created in MatrixAdder.java*/
	private int[] row;
	/**an integer to hold an assigned ID*/
	private int id;
	

    /**Constructor for the AdderWorker that will be summing things up
     * @param summer - A SumAccumulator that will hold all the finished summations
     * @param row - a row of the 2d matrix to have it's elements summed up
     * @param id - the id of the thread*/
	public AdderWorker(SumAccumulator summer, int[] row, int id){
        //init all the variables to the parameters
		this.summer = summer;
		this.row = row;
		this.id = id;
	}

	public void run(){
		int local_sum = 0;
        //sum all the elements in the row
        for (int num : row){
            local_sum += num;
        }
        //print the result of this summation
        System.out.println("Thread " + id + " added " + row.length + " nums --> "
                           + local_sum);
        summer.setSum(local_sum);
	}

}

        
