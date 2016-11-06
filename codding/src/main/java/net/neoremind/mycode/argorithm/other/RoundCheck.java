package net.neoremind.mycode.argorithm.other;

import java.util.*;
import java.util.stream.Collectors;

public class RoundCheck {

	public static void main(String[] args) {
		double[] pay = new double[] { 1.2, 2.7, 3.5 };
		List<Integer> res = calc(pay);
		System.out.println(res);
		System.out.println(res.stream().reduce(0, (a, b) -> a + b));
		Queue<Integer> queue = new LinkedList<>();
		System.out.println("=======");

		pay = new double[] { 1.2, 2.7, 3.5, 4.2, 9.0, 2.8, 3.6, 5.8 };
		res = calc(pay);
		System.out.println(res);
		System.out.println(res.stream().reduce(0, (a, b) -> a + b));
		System.out.println("=======");

		pay = new double[] { 1.5, 4.3, 6.8, 1.5, 6.5, 3.3 };
		res = calc(pay);
		System.out.println(res);
		System.out.println(res.stream().reduce(0, (a, b) -> a + b));
	}

	private static List<Integer> calc(double[] pay) {
		List<Integer> res = new ArrayList<>();
		int floor = 0;
		double result = 0;
		for (int i = 0; i < pay.length; i++) {
			floor += (int) Math.floor(pay[i]);
			result += pay[i];
		}
		int finalResult = (int) Math.round(result);
		System.out.println(finalResult);
		int diff = finalResult - floor;
		System.out.println(diff);
		List<Double> sorted = Arrays.stream(pay).boxed()
				.sorted((a, b) -> Double.compare(Math.ceil(a) - a, Math.ceil(b) - b)).collect(Collectors.toList());
		System.out.println(sorted);
		for (Double d : sorted) {
			if (diff > 0 && Math.ceil(d) != d) { // corner case
				res.add((int) Math.ceil(d));
				diff--;
			} else {
				res.add((int) Math.floor(d));
			}
		}
		return res;
	}
}
