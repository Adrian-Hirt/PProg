package ethz.ch.pp.mergeSort;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import ethz.ch.pp.util.ArrayUtils;

public class MergeSortMulti extends RecursiveAction {

	int[] input;
	int[] result;
	int start;
	int length;

	private MergeSortMulti(int[] inputData, int newStart, int newLength) {
		input = inputData;
		start = newStart;
		length = newLength;
	}


	public static int[] sort(int[] input, int numThreads) {
		long t0 = System.currentTimeMillis();
		ForkJoinPool pool = new ForkJoinPool(numThreads);
		MergeSortMulti task = new MergeSortMulti(input, 0, input.length);
		pool.invoke(task);
		long t1 = System.currentTimeMillis();
		System.out.println("For " + input.length + " MergeSort Multi takes " +(t1 - t0) + " msec");

		return task.result;
	}

	@Override
	protected void compute() {
		int[] result = new int[length];

		if (length == 1) {
			result[0] = input[start];
		} else if (length == 2) {
			if (input[start] > input[start + 1]) {
				result[0] = input[start + 1];
				result[1] = input[start];
			} else {
				result[0] = input[start];
				result[1] = input[start + 1];
			}
		} else {
			int halfSize = (length) / 2;

			MergeSortMulti left = new MergeSortMulti(input, start, halfSize);
			MergeSortMulti right = new MergeSortMulti(input, start + halfSize, length - halfSize);

			left.fork();
			right.fork();
			left.join();
			right.join();

			ArrayUtils.merge(left.result, right.result, result);
		}

		this.result = result;
	}
}