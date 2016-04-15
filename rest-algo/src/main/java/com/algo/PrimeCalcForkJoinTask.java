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

	private final Range range;
	private final PrimeCalcService primeCalcService;

	public PrimeCalcForkJoinTask(Range range,
			PrimeCalcService primeCalcService) {
		this.range = range;
		this.primeCalcService = primeCalcService;
	}

	@Override
	protected List<Integer> compute() {
		if (range.getTo() - range.getFrom() <= 30) {
			return primeCalcService.getAllPrimesWithinRange(range);
		} else {
			int mid = (range.getFrom() + range.getTo()) / 2;
			
			PrimeCalcForkJoinTask task 
			     = new PrimeCalcForkJoinTask(new Range(range.getFrom(), mid), primeCalcService);
			task.fork();

			PrimeCalcForkJoinTask task2 
				 = new PrimeCalcForkJoinTask(new Range(mid, range.getTo()),primeCalcService);
			task2.fork();

			List<Integer> result = task.join();
			result.addAll(task2.join());

			return result;
		}

	}

}
