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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PrimeCalcServiceImpl implements PrimeCalcService {

	private final Logger logger = Logger.getLogger(this.getClass());
	
	private ThreadPoolExecutor executor;

	private ForkJoinPool forkJoinPool;

	public PrimeCalcServiceImpl() {}
	
	@PostConstruct
	public void initialise(){
		// create pool with as many threads are cores
		int poolSize = Runtime.getRuntime().availableProcessors();

		executor = new ThreadPoolExecutor(poolSize, poolSize, Long.MAX_VALUE,
				TimeUnit.SECONDS, new LinkedBlockingQueue<>());

		forkJoinPool = new ForkJoinPool();
	}

	@PreDestroy
	public void destroy(){
		executor.shutdown();
		forkJoinPool.shutdown();
	}
	
	@Override
	public boolean isPrime(int number) {

		// check if n is a multiple of 2
		if (number <= 1 || number > 2 && number % 2 == 0)
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
		return getAllPrimesWithinRange(new Range(0, number));
	}

	@Override
	public List<Integer> getAllPrimesWithinRange(Range range) {
		
		List<Integer> result = new ArrayList<>();
		for (int i = range.getFrom(); i < range.getTo(); i++) {
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

		logger.info("Calculate primes using executor service for the first "
				+ number + " numbers");

		List<Future<List<Integer>>> futureList = new ArrayList<>();

		int noOfCores = Runtime.getRuntime().availableProcessors();
		
		if (number < noOfCores) {
			// if only a small number, no point using thread pool
			// need to tune this number
			return getAllPrimes(number);
		}
		
		int taskSize = number / noOfCores;

		for (int i = 0; i < noOfCores-1; i++) {
			Range range = new Range(i * taskSize, (i + 1)* taskSize);
			Future<List<Integer>> future = 
					executor.submit(new PrimeCalcExecutorTask(range, this));
			futureList.add(future);
		}
		
		Range range = new Range((noOfCores-1) * taskSize, number);
		Future<List<Integer>> future = 
				executor.submit(new PrimeCalcExecutorTask(range, this));
		futureList.add(future);


		List<Integer> result = new ArrayList<>();
		//get the results of each task
		for (Future<List<Integer>> f : futureList) {
			try {
				result.addAll(f.get());
			} catch (InterruptedException | ExecutionException e) {
				logger.error("error has occurred", e);
				return Collections.<Integer> emptyList();
			}
		}

		return result;

	}

	@Override
	public List<Integer> getAllPrimesUsingForkJoinPool(int number) {

		logger.info("Calculate primes using fork join pool for the first "
				+ number + " numbers");

		List<Integer> result 
			= forkJoinPool.invoke(new PrimeCalcForkJoinTask(new Range(0, number), this));

		return result;
	}

	@Override
	public List<Integer> getAllPrimesUsingCompletableFuture(int number) {

		logger.info("Calculate primes using Completable Future for the first "
				+ number + " numbers");

		List<Range> ranges = new ArrayList<>();
		int noOfCores = Runtime.getRuntime().availableProcessors();;
		
		if (number < noOfCores) {
			// if only a small number, no point using thread pool
			//need to tune this
			return getAllPrimes(number);
		}
		
		int taskSize = number / noOfCores;

		for (int i = 0; i < noOfCores - 1; i++) {
			ranges.add(new Range(i * taskSize, (i + 1) * taskSize));
		}
		ranges.add(new Range((noOfCores-1) * taskSize,  number));
		
		List<CompletableFuture<List<Integer>>> futures = ranges.stream()
													.map(range -> CompletableFuture.supplyAsync(() -> getAllPrimesWithinRange(range), executor))
													.collect(Collectors.toList());

		//Combine results of each task
		List<Integer> result = new ArrayList<>();
		futures
			.stream()
			.map((cf) -> (result.addAll(cf.join())))
			.collect(Collectors.toList());
		
		return result;

	}

}
