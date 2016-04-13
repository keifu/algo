package com.algo;

import java.util.List;
import java.util.concurrent.Callable;

class PrimeCalcExecutorTask implements Callable<List<Integer>> {

	private final Range range;

	private final PrimeCalcService primeCalcService;

	public PrimeCalcExecutorTask(Range range,
			PrimeCalcService primeCalcService) {
		this.range = range;
		this.primeCalcService = primeCalcService;
	}

	@Override
	public List<Integer> call() throws Exception {
		
		List<Integer> primes = primeCalcService.getAllPrimesWithinRange(range);
				
		return primes;
	}

}
