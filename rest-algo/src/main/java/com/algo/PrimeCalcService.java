package com.algo;

import java.util.List;

/**
 * 
 * Prime calculation serivce. Calculates a list of prime number up to the
 * specified limit.  Uses different algorithms to achieve this, using both
 * single and multi threading techniques
 * 
 * For simplicity, assume <code>Integer</code> is enough for all numbers
 * (Otherwise need to beware of overflow)
 * 
 * @author Keith
 *
 */
public interface PrimeCalcService {

	/**
	 * Whether the specified number is a prime number. A prime number is a
	 * positive whole number which can only be divided by itself and 1
	 * 
	 * @param number
	 * @return
	 */
	public boolean isPrime(int number);

	/**
	 * 
	 * Get all prime numbers up to but not including the specified number.
	 * Uses a traditional loop
	 * 
	 * @param number
	 * @return
	 */
	public List<Integer> getAllPrimes(int number);

	/**
	 * Get all prime numbers with range. 
	 * {@code from} inclusive {@code to} exclusive
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Integer> getAllPrimesWithinRange(Range range);

	/**
	 * 
	 * Get all prime number up to but not including the specified number. 
	 * Uses Java 8 streams
	 * 
	 * @param number
	 * @return
	 */
	public List<Integer> getAllPrimesUsingStream(int number);

	/**
	 * 
	 * Get all prime number up but not including the specified number. 
	 * Uses Java 8 parallel streams
	 * 
	 * @param number
	 * @return
	 */
	public List<Integer> getAllPrimesUsingParallelStream(int number);

	/**
	 * 
	 * Get all prime number up but not including the specified number. 
	 * Uses standard Executor Service
	 * 
	 * @param number
	 * @return
	 */
	public List<Integer> getAllPrimesUsingExecutorService(int number);

	/**
	 * Get all prime number up but not including the specified number. 
	 * Uses the forkjoin pool
	 * 
	 * @param number
	 * @return
	 */
	public List<Integer> getAllPrimesUsingForkJoinPool(int number);

	/**
	 * Get all prime number up but not including the specified number. 
	 * Uses <code>CompletableFuture</code>
	 * 
	 * @param number
	 * @return
	 */
	public List<Integer> getAllPrimesUsingCompletableFuture(int number);

}
