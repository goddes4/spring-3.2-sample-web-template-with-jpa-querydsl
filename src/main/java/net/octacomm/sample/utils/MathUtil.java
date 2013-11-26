package net.octacomm.sample.utils;

import java.util.Random;

public class MathUtil {

	private static Random random = new Random();

	public static int random(int num) {
		return random.nextInt(num);
	}
}
