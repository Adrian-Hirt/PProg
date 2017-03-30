package ethz.ch.pp.assignment2;

import java.util.Random;

/*
Feedback:

Good job! All unittests pass and the code is nice and clean.
Keep it up.

MR
*/

public class Main {

	public static void main(String[] args) {
 		
		// taskA();
		
		int[] input1 = generateRandomInput(1000);
		int[] input2 = generateRandomInput(10000);
		int[] input3 = generateRandomInput(100000);
		int[] input4 = generateRandomInput(1000000);
		
		// Sequential version
		taskB(input1);
		taskB(input2);
		taskB(input3);
		taskB(input4);
		
		// Parallel version
		taskE(input1, 4);
		taskE(input2, 4);
		taskE(input3, 4);
		taskE(input4, 4);
		
		long threadOverhead = taskC();
		System.out.format("Thread overhead on current system is: %d nano-seconds\n", threadOverhead);		
	}
	
	private final static Random rnd = new Random(42);

	public static int[] generateRandomInput() {
		return generateRandomInput(rnd.nextInt(10000) + 1);
	}
	
	public static int[] generateRandomInput(int length) {	
		int[] values = new int[length];		
		for (int i = 0; i < values.length; i++) {
			values[i] = rnd.nextInt(99999) + 1;				
		}		
		return values;
	}
	
	public static int[] computePrimeFactors(int[] values) {		
		int[] factors = new int[values.length];	
		for (int i = 0; i < values.length; i++) {
			factors[i] = numPrimeFactors(values[i]);
		}		
		return factors;
	}
	
	public static int numPrimeFactors(int number) {
		int primeFactors = 0;
		int n = number;		
		for (int i = 2; i <= n; i++) {
			while (n % i == 0) {
				primeFactors++;
				n /= i;
			}
		}
		return primeFactors;
	}
	
	public static class ArraySplit {
		public final int startIndex;
		public final int length;
		
		ArraySplit(int startIndex, int length) {
			this.startIndex = startIndex;
			this.length = length;
		}
	}
	
	// TaskD
	public static ArraySplit[] PartitionData(int length, int numPartitions) {
		
		int lengthOfSegment = length / numPartitions;
		ArraySplit[] result = new ArraySplit[numPartitions];
		int rest = length % numPartitions;
		int i;
		
		for(i = 0; i < numPartitions - 1; i++) {
			ArraySplit a = new ArraySplit(i * lengthOfSegment, lengthOfSegment);
			result[i] = a;
		}
		
		result[numPartitions - 1] = new ArraySplit((i * lengthOfSegment), lengthOfSegment + rest);
		
		return result;
	}
	
	public static void taskA() {
		Thread t = new Thread() {
			public void run() {
				System.out.println("Hello Thread!");
				// System.out.println(Thread.currentThread().getId());
			}
		};
		
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static int[] taskB(final int[] values) {
		int[] result = new int[values.length];
		
		// long startTime = System.nanoTime();
		Thread t = new Thread() {
			public void run() {
				for(int i = 0; i < values.length; i++) {
					result[i] = numPrimeFactors(values[i]);
				}
			}
		};
		
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// long endTime = System.nanoTime() - startTime;
		// System.out.println("Sequential: " + (int)(endTime / 1.0e6) + " ms elapsed!");
		return result;
		
	}
	
	// Returns overhead of creating thread in nano-seconds
	public static long taskC() {		
		long startTime = System.nanoTime();
		Thread t = new Thread() {
			public void run() {}
		};
		t.start();
		t.interrupt();
		long duration = System.nanoTime() - startTime;
		return duration;
	}
	
	public static int[] taskE(final int[] values, final int numThreads) {
		ArraySplit[] data = PartitionData(values.length, numThreads);
		int[] result = new int[values.length];
		
		Thread[] threads = new Thread[numThreads];
		
		// long startTime = System.nanoTime();
		for(int i = 0; i < numThreads; i++) {
			final int k = i;
			Thread t = new Thread() {
				public void run() {
					for(int j = 0; j < data[k].length; j++) {
						result[data[k].startIndex + j] = numPrimeFactors(values[data[k].startIndex + j]);
					}
				}
			};
			threads[i] = t;
		}
		
		for(int i = 0; i < numThreads; i++) {
			threads[i].start();
		}
		
		for(int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// long endTime = System.nanoTime() - startTime;
		// System.out.println("Parallel: " + (int)(endTime / 1.0e6) + " ms elapsed!");
		return result;
	}


}
