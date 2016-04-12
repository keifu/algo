package com.algo;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.algo.PrimeCalcService;
import com.algo.PrimeCalcServiceImpl;

@RunWith(JUnitParamsRunner.class)
public class PrimeCaclServiceImpTest {
	
	private PrimeCalcService primeCaclService;
	
	@Before
	public void setup(){
		primeCaclService = new PrimeCalcServiceImpl();
	}
	
	@After
	public void tearDown(){
		primeCaclService = null;
	}
	
	public static Collection<Object[]> getTestDataFirstFiftyNumbers() {
		
		return Arrays.asList(new Object[][] { 
				{0 ,false},{1 ,false},{2 ,true},
				{3 ,true},{4 ,false},{5 ,true},
				{6 ,false},{7 ,true},{8 ,false},
				{9 ,false},{10 ,false},{11 ,true},
				{12 ,false},{13 ,true},{14 ,false},
				{15 ,false},{16 ,false},{17 ,true},
				{18 ,false},{19 ,true},{20 ,false},
				{21 ,false},{22 ,false},{23 ,true},
				{24 ,false},{25 ,false},{26 ,false},
				{27 ,false},{28 ,false},{29 ,true},
				{30 ,false},{31 ,true},{32 ,false},
				{33 ,false},{34 ,false},{35 ,false},
				{36 ,false},{37 ,true},{38 ,false},
				{39 ,false},{40 ,false},{41 ,true},
				{42 ,false},{43 ,true},{44 ,false},
				{45 ,false},{46 ,false},{47 ,true},
				{48 ,false},{49 ,false},{49 ,false},
				{50 ,false}
		});
	}
	
	private static List<Integer> PRIMES_FOR_FIST_50_NUMBERS =  
				Arrays.asList(new Integer[]{ 
						2,3,5,7,11,13,17,19,23,29,31,37,41,43,47
	});

		
	@Test
	@Parameters(method = "getTestDataFirstFiftyNumbers")
	public void testIsPrime(int number, boolean expected) {	
		assertEquals(expected, primeCaclService.isPrime(number));
	}
	
	@Test
	public void testGetAllPrimes() {	
		List<Integer> primes = primeCaclService.getAllPrimes(50);
		assertEquals(PRIMES_FOR_FIST_50_NUMBERS, primes);
	}
	
	@Test
	public void testGetAllPrimesUsingStream() {	
		List<Integer> primes = primeCaclService.getAllPrimesUsingStream(50);
		assertEquals(PRIMES_FOR_FIST_50_NUMBERS, primes);
	}
	
	@Test
	public void testGetAllPrimesUsingParellelStream() {	
		List<Integer> primes = primeCaclService.getAllPrimesUsingParallelStream(50);
		assertEquals(PRIMES_FOR_FIST_50_NUMBERS, primes);
	}
	
	@Test
	public void testGetAllPrimesUsingExecutorService() {	
		List<Integer> primes = primeCaclService.getAllPrimesUsingExecutorService(50);
		assertEquals(PRIMES_FOR_FIST_50_NUMBERS, primes);
	}
	
	@Test
	public void testGetAllPrimesUsingForkJoinPool() {	
		List<Integer> primes = primeCaclService.getAllPrimesUsingForkJoinPool(50);
		assertEquals(PRIMES_FOR_FIST_50_NUMBERS, primes);
	}
	
	@Test
	public void testGetAllPrimesUsingCompletableFuture() {	
		List<Integer> primes = primeCaclService.getAllPrimesUsingCompletableFuture(50);
		assertEquals(PRIMES_FOR_FIST_50_NUMBERS, primes);
	}
		
	

}
