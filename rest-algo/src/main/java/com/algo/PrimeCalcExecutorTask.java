package com.algo;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class PrimeCalcExecutorTask implements Callable<List<Integer>> {

	private int first;
	private int last;
	private PrimeCalcService primeCalcService;

	public PrimeCalcExecutorTask(int first, int last,
			PrimeCalcService primeCalcService) {
		this.first = first;
		this.last = last;
		this.primeCalcService = primeCalcService;
	}

	@Override
	public List<Integer> call() throws Exception {

		List<Integer> primes = IntStream.range(this.first, this.last)
				.filter(x -> primeCalcService.isPrime(x)).boxed()
				.collect(Collectors.toList());

		return primes;
	}

}
