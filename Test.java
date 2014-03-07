package core;

import java.util.ArrayList;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Round round = new Round(1, "dictionary.txt");

		ArrayList<Integer> pos = new ArrayList<Integer>();

		pos = round.guess("a");
//		for (int i = 0; i < pos.size(); i++){
//			System.out.print(pos.get(i));
//			System.out.println();
//		}
		
		CharPosition cp = round.help();
//		System.out.println(cp.c);
//		System.out.println(cp.pos.size());
//		

//		pos = round.guess("a");
//		for (int i = 0; i < pos.size(); i++){
//			System.out.print(pos.get(i));
//			System.out.println();
//		}

//		pos = round.guess("n");
//		for (int i = 0; i < pos.size(); i++){
//			System.out.print(pos.get(i));
//			System.out.println();
//		}
//		
//		System.out.println(pos.contains(100));

	}

}
