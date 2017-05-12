package ethz.ch.pp.fib;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FibonacciMulti extends RecursiveTask<BigInteger> implements IFibonacci {

	int n;

	public FibonacciMulti(int n) {
		this.n = n;
	}

	public FibonacciMulti() {
		new FibonacciMulti(0);
	}

	@Override
	public BigInteger fib(int n) {

		ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		BigInteger result = pool.invoke(new FibonacciMulti(n));
		return result;
	}

	@Override
	protected BigInteger compute() {
		BigInteger number;
		if(n == 0) {
			return BigInteger.ZERO;
		}
		else if(n == 1) {
			return  BigInteger.ONE;
		}
		else {
			int cutoff = 14;
			if(n <= cutoff) {
				BigInteger left = fib(n - 1);
				BigInteger right = fib(n - 2);
				number = left.add(right);
			}
			else {
				FibonacciMulti left = new FibonacciMulti(n - 1);
				FibonacciMulti right = new FibonacciMulti(n - 2);

				left.fork();
				BigInteger rightAns = right.compute();
				BigInteger leftAns = left.join();

				number = leftAns.add(rightAns);
			}
			return number;
		}
	}

}
