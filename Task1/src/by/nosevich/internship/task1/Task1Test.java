package by.nosevich.internship.task1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static by.nosevich.internship.task1.Main.*;

class Task1Test {
	
	List<Integer> list;

	@BeforeEach
	public void init() {
		list=Arrays.asList(1,2,3,4,5);
	}
	
	@Test
	void largestNumTest() {
		int num = getMax(list);
		assertEquals(5, num);
	}
	@Test
	void smallestNumTest() {
		int num = getMin(list);
		assertEquals(1, num);
	}
	@Test
	void averageNumTest() {
		double num = getAverage(list);
		assertEquals(3, num);
	}
	
	@Test
	void writeNumTest() {
		List<Integer> testList = new ArrayList<Integer>();
		writeNumber("ab",testList);
		writeNumber("6",testList);
		writeNumber("3.5",testList);
		assertEquals(Arrays.asList(6), testList);;
	}

}
