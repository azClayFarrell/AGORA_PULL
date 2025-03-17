/**
 * Support file inspired by https://www.cs-book.com/OS9/java-dir/4.pdf
 *
 */


public class SumAccumulator{
    private int sum;

    public SumAccumulator(){
        this.sum = 0;
    }
    public int getSum(){
        return sum;
    }

    public void setSum(int sum){
        this.sum += sum;
    }
}
