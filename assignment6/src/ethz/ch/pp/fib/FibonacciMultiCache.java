package ethz.ch.pp.fib;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


public class FibonacciMultiCache extends RecursiveTask<BigInteger> implements IFibonacci {

	int n;
	private static BigInteger[] values;

	public FibonacciMultiCache(int n) {
		this.n = n;
	}

	public FibonacciMultiCache() {
		new FibonacciMulti(0);
	}

	@Override
	public BigInteger fib(int n) {
		values = new BigInteger[n];

		ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		BigInteger result = pool.invoke(new FibonacciMulti(n));
		return result;
	}

	synchronized public void setValue(int key, BigInteger value) {
		values[key] = value;
	}

	synchronized public BigInteger getValue(int key) {
		return values[key];
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
			if(getValue(n) != null) {
				return getValue(n);
			}
			int cutoff = 10;
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
			setValue(n, number);
			return number;
		}
	}
}
