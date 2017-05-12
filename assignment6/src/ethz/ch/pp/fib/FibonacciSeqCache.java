package ethz.ch.pp.fib;

import java.math.BigInteger;

public class FibonacciSeqCache implements IFibonacci {

	static BigInteger[] values;

	@Override
	public BigInteger fib(int n) {
		values = new BigInteger[n + 1];
		return calculate(n);
	}

	public BigInteger calculate(int n) {
		if(n == 0) {
			return BigInteger.ZERO;
		}
		else if(n == 1) {
			return BigInteger.ONE;
		}
		else {
			if(values[n] != null) {
				return values[n];
			}
			else {
				BigInteger result = (calculate(n - 1)).add(calculate(n - 2));
				values[n] = result;
				return result;
			}
		}
	}
	
}
