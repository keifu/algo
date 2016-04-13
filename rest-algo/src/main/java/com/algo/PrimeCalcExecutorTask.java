package com.algo;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

		List<Integer> primes = 
				IntStream.range(range.getFrom(), range.getTo())
					.filter(x -> primeCalcService.isPrime(x))
					.boxed()
					.collect(Collectors.toList());

		return primes;
	}

}
