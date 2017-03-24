package ethz.ch.pp.searchAndCount;

import ethz.ch.pp.util.DatasetGenerator;
import ethz.ch.pp.util.Workload;

public class Main {

	public static void main(String[] args) {

		DatasetGenerator dg = new DatasetGenerator(100000);
		int[] input = dg.generate();

		// Standard function executions to test Single vs Multiple

		taskA(input, Workload.Type.HEAVY);
		taskB(input, 1000, Workload.Type.HEAVY, 4);

		// Test for finding the optimal cutoff and thread count

		for(int i = 100; i <= 1000000; i = i * 10) {
			for(int j = 2; j <= 128; j = j * 2) {
				System.out.println("Testing  program with " + i + " cutoff and " + j + " threads");
				taskB(input, i, Workload.Type.HEAVY, j);
			}
		}
	}

	public static void taskA(int[] input, Workload.Type wt){		
		SearchAndCountSingle.countNoAppearances(input, wt);
	}

	public static void taskB(int[] input, int cutoff, Workload.Type wt, int numThreads){		
		SearchAndCountMultiple.countNoAppearances(input, cutoff, wt, numThreads);
	}

}
