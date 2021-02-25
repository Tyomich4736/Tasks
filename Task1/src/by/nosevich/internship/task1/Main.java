package by.nosevich.internship.task1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		System.out.println("Write numbers. If you want to see information write \"info\"");
		List<Integer> list = new ArrayList<Integer>();
		while (true) {
			String input = new Scanner(System.in).next();
			if (input.equals("info")) {
				showInfo(list);
			} else {
				writeNumber(input, list);
			}
		}
	}

	public static void writeNumber(String input, List<Integer> list) {
		try {
			int num = Integer.parseInt(input);
			list.add(num);
		} catch (NumberFormatException e) {
			System.out.println("Invalid input");
		}
	}

	public static void showInfo(List<Integer> list) {
		if (list.isEmpty()) {
			System.out.println("List is empty");
		} else {
			System.out.println(list);
			System.out.println("Smallest number is " + getMin(list));
			System.out.println("Largest number is " + getMax(list));
			System.out.println("Average number is " + getAverage(list));
		}
	}
	public static int getMin(List<Integer> list) {
		int min = Integer.MAX_VALUE;
		for(int elem : list) {
			if (elem<min)
				min=elem;
		}
		return min;
	}
	public static int getMax(List<Integer> list) {
		int max = Integer.MIN_VALUE;
		for(int elem : list) {
			if (elem>max)
				max=elem;
		}
		return max;
	}
	public static double getAverage(List<Integer> list) {
		int sum = 0;
		int count = 0;
		for (int elem : list) {
			sum+=elem;
			count++;
		}
		return (double)sum/(double)count;
	}
}
