package com.algo;

import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Fork Join task that calculate primes
 * 
 * @author Keith
 *
 */
public class PrimeCalcForkJoinTask extends RecursiveTask<List<Integer>> {
	private static final long serialVersionUID = 1L;

	private int first;
	private int last;
	private PrimeCalcService primeCalcService;

	public PrimeCalcForkJoinTask(int first, int last,
			PrimeCalcService primeCalcService) {
		this.first = first;
		this.last = last;
		this.primeCalcService = primeCalcService;
	}

	@Override
	protected List<Integer> compute() {
		if (last - first <= 100) {
			// if less than a certain number, no point using forkjoin pool
			return primeCalcService.getAllPrimesWithinRange(first, last);
		} else {
			int mid = (first + last) / 2;

			PrimeCalcForkJoinTask task = new PrimeCalcForkJoinTask(first, mid,
					primeCalcService);
			task.fork();

			PrimeCalcForkJoinTask task2 = new PrimeCalcForkJoinTask(mid, last,
					primeCalcService);
			task2.fork();

			List<Integer> result = task.join();
			result.addAll(task2.join());

			return result;
		}

	}

}
