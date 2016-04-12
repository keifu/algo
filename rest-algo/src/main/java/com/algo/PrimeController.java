package com.algo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/prime")
public class PrimeController {

	@Autowired
	private PrimeCalcService primeCalcService;

	@RequestMapping
	public boolean isPrime(@RequestParam(value = "number") int number) {
		return primeCalcService.isPrime(number);
	}

	@RequestMapping("/serial")
	public List<Integer> getPrimeUsingSeriial(
			@RequestParam(value = "limit", defaultValue = "50") int limit) {
		List<Integer> primeList = primeCalcService.getAllPrimes(limit);
		return primeList;
	}

	@RequestMapping("/stream")
	public List<Integer> getPrimeUsingStream(
			@RequestParam(value = "limit", defaultValue = "50") int limit) {
		List<Integer> primeList = primeCalcService
				.getAllPrimesUsingStream(limit);
		return primeList;
	}

	@RequestMapping("/pararrelstream")
	public List<Integer> getPrimeUsingParallelStream(
			@RequestParam(value = "limit", defaultValue = "50") int limit) {
		List<Integer> primeList = primeCalcService
				.getAllPrimesUsingParallelStream(limit);
		return primeList;
	}

	@RequestMapping("/executorservice")
	public List<Integer> getPrimeUsingExectuorService(
			@RequestParam(value = "limit", defaultValue = "50") int limit) {
		List<Integer> primeList = primeCalcService
				.getAllPrimesUsingExecutorService(limit);
		return primeList;
	}

	@RequestMapping("/forkjoin")
	public List<Integer> getPrimeUsingForkJoin(
			@RequestParam(value = "limit", defaultValue = "50") int limit) {
		List<Integer> primeList = primeCalcService
				.getAllPrimesUsingForkJoinPool(limit);
		return primeList;
	}

	@RequestMapping("/completableFuture")
	public List<Integer> getPrimeUsingCompletableFuture(
			@RequestParam(value = "limit", defaultValue = "50") int limit) {
		List<Integer> primeList = primeCalcService
				.getAllPrimesUsingCompletableFuture(limit);
		return primeList;

	}
}
