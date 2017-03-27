package ethz.ch.pp.searchAndCount;
import ethz.ch.pp.util.Workload;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SearchAndCountMultiple extends RecursiveTask<Integer>{

	int[] input;
	Workload.Type workloadType;
	int cutOff;
	int threads;
	int low;
	int high;

	private SearchAndCountMultiple(int[] input, Workload.Type wt, int co, int noThreads, int low, int high) {
		this.input = input;
		this.workloadType = wt;
		this.cutOff = co;
		this.threads = noThreads;
		this.low = low;
		this.high = high;
	}


	public static Integer countNoAppearances(int[] input, int co, Workload.Type wt, int noThreads) {
		long t0 = System.currentTimeMillis();
		SearchAndCountMultiple counter = new SearchAndCountMultiple(input, wt, co, noThreads, 0, input.length);

		ForkJoinPool pool = new ForkJoinPool(noThreads);

		int result = pool.commonPool().invoke(counter);
		long t1 = System.currentTimeMillis();
		System.out.println("For (inputsize=" + input.length + ",workload=" + wt + ") SearchAndCount Multiple takes "
				+ (t1 - t0) + " msec");
		return result;
	}

	@Override
	public Integer compute() {
		int count = 0;
		if(high - low <= cutOff) {
			for (int i = low; i < high; i++) {
				if (Workload.doWork(input[i], workloadType)){
					count++;
				}
			}
		}
		else {
			int mid = low + (high - low) / 2;
			SearchAndCountMultiple left = new SearchAndCountMultiple(input, workloadType, cutOff, threads, low, mid);
			SearchAndCountMultiple right = new SearchAndCountMultiple(input, workloadType, cutOff, threads, mid, high);
			left.fork();
			int rightAns = right.compute();
			int leftAns  = left.join();
			count = leftAns + rightAns;
		}
		return count;
	}
}