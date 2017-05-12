package ethz.ch.pp.mergeSort;

import ethz.ch.pp.util.DatasetGenerator;

/*
Feedback:

Good job with task1. Try to avoid the fork,fork,join,join pattern in task 2 (in general there's nothing wrong with it, 
		however as written in the slides I've sent you all it is buggy in java).

Best,
MR
*/

public class Main {

	public static void main(String[] args) {

	    DatasetGenerator dg = new DatasetGenerator(1000000);
	    int[] input = dg.generate();
	    
	    taskA(input);
	    taskB(input, Runtime.getRuntime().availableProcessors());
	}
	
	public static void taskA(int[] input){		
		MergeSortSingle.sort(input);
	}
	
	public static void taskB(int[] input, int numThreads){
		MergeSortMulti.sort(input, numThreads);
	}

}
