package AirlineDog;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Inputs {

	public static int input(int lower_boundary, int upper_boundary) {
		Scanner in = new Scanner(System.in);
		int inp = -1;
		try {
			inp = in.nextInt();
			if(inp < lower_boundary || inp > upper_boundary) {
				throw new InputMismatchException();
			}
		}catch (InputMismatchException e){
			System.err.println("Παρακαλώ εισάγεται αριθμό απο το "+lower_boundary+" μεχρι το "+upper_boundary);
			inp = input(lower_boundary, upper_boundary);
		}
			return inp;	
	}
	
}
