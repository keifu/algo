package com.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PrimeCalcServiceImpl implements PrimeCalcService {

	private final Logger logger = Logger.getLogger(this.getClass());

	private final ThreadPoolExecutor executor;

	private final ForkJoinPool forkJoinPool;

	public PrimeCalcServiceImpl() {

		// create pool with as many threads are cores
		int poolSize = Runtime.getRuntime().availableProcessors();

		executor = new ThreadPoolExecutor(poolSize, poolSize, Long.MAX_VALUE,
				TimeUnit.SECONDS, new LinkedBlockingQueue<>());

		forkJoinPool = new ForkJoinPool();
	}

	@Override
	public boolean isPrime(Integer number) {

		// check if n is a multiple of 2
		if (number == 0 || number == 1 || number > 2 && number % 2 == 0)
			return false;

		// if not, check odd numbers up to square root
		for (int i = 3; i * i <= number; i += 2) {
			if (number % i == 0)
				return false;
		}
		return true;

	}

	@Override
	public List<Integer> getAllPrimes(int number) {
		logger.debug("Calculate all primes to " + number);
		return getAllPrimesWithinRange(0, number);
	}

	@Override
	public List<Integer> getAllPrimesWithinRange(int from, int to) {
		

		List<Integer> result = new ArrayList<>();
		for (int i = from; i < to; i++) {
			if (isPrime(i)) {
				result.add(i);
			}
		}
		return result;
	}

	@Override
	public List<Integer> getAllPrimesUsingStream(int number) {

		logger.info("Calculate primes using stream for the first " + number
				+ " numbers");

		List<Integer> primes = IntStream.range(0, number)
					.filter(x -> isPrime(x))
					.boxed()
					.collect(Collectors.toList());

		return primes;
	}

	@Override
	public List<Integer> getAllPrimesUsingParallelStream(int number) {

		logger.info("Get primes using parellel stream for the first " + number
				+ " numbers");

		List<Integer> primes = IntStream.range(0, number)
				.parallel()
				.filter(x -> isPrime(x))
				.boxed()
				.collect(Collectors.toList());

		return primes;
	}

	@Override
	public List<Integer> getAllPrimesUsingExecutorService(int number) {

		if (number <= 30) {
			// if only a small number, no point using thread pool
			// need to tune this number
			return getAllPrimes(number);
		}

		logger.info("Calculate primes using executor service for the first "
				+ number + " numbers");

		List<Future<List<Integer>>> futureList = new ArrayList<>();

		int noOfCores = Runtime.getRuntime().availableProcessors();
		int taskSize = number / noOfCores;

		for (int i = 0; i < 3; i++) {
			Future<List<Integer>> future = 
					executor.submit(new PrimeCalcExecutorTask(i * taskSize, (i + 1)* taskSize, this));
			futureList.add(future);
		}
		Future<List<Integer>> future = 
				executor.submit(new PrimeCalcExecutorTask(3 * taskSize, number + 1,this));

		futureList.add(future);

		List<Integer> result = new ArrayList<>();
		for (Future<List<Integer>> f : futureList) {
			try {
				result.addAll(f.get());
			} catch (InterruptedException | ExecutionException e) {
				// error has occurred, raise alert
				return Collections.<Integer> emptyList();
			}
		}

		return result;

	}

	@Override
	public List<Integer> getAllPrimesUsingForkJoinPool(int number) {

		logger.info("Calculate primes using fork join pool for the first "
				+ number + " numbers");

		List<Integer> result = forkJoinPool.invoke(new PrimeCalcForkJoinTask(0,
				number, this));

		return result;
	}

	@Override
	public List<Integer> getAllPrimesUsingCompletableFuture(int number) {

		logger.info("Calculate primes using Completable Future for the first "
				+ number + " numbers");

		if (number <= 30) {
			// if only a small number, no point using thread pool
			// need to tune this number
			return getAllPrimes(number);
		}

		List<Range> ranges = new ArrayList<>();

		int taskSize = number / 4;

		for (int i = 0; i < 3; i++) {
			ranges.add(new Range(i * taskSize, (i + 1) * taskSize));
		}

		ranges.add(new Range(3 * taskSize, number));

		List<CompletableFuture<List<Integer>>> futures = ranges
				.stream()
				.map(range -> CompletableFuture
						.supplyAsync(() -> getAllPrimesWithinRange(
								range.getTo(), range.getFrom())))
				.collect(Collectors.toList());

		List<Integer> result = new ArrayList<>();

		futures
			.stream()
			.map((cf) -> (result.addAll(cf.join())))
			.collect(Collectors.toList());
		
		return result;

	}

}
