package core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Round {
	final static int RIGHT = 99;
	final static int RPT = -1;
	final static int FAIL = -100;
	final static int RIGHT_NEXTROUND = 100;
	final static int WRONG = -99;

	private int roundNum;// 关数，用于单词长度
	private String dicFile;// 单词本文件名

	private String correctWord;
	private String correctExplain;
	private int life;
	private ArrayList<Character> guessedChar;// 所有猜过的字母
	private ArrayList<Character> wrongChar;// 所有猜错的字母
	private String guessedWord = "";// 已经猜出的单词
	private boolean ifWin;

	private ArrayList<String> wordBand = new ArrayList<String>();
	private ArrayList<String> explainBand = new ArrayList<String>();

	public Round(int roundNum, String dicFile) {
		this.roundNum = roundNum;
		this.dicFile = dicFile;

		// read the dictionary file, get all words and their explanations
		loadDictionary(dicFile);

		// select a word to guess form word band
		initializeCorrectWord(roundNum);
		
		System.out.println(correctWord);
		
		//initialize other variables
		life = 6;
		guessedChar = new ArrayList<Character>();
		wrongChar = new ArrayList<Character>();
		for(int i = 0; i<correctWord.length();i++)
			guessedWord = guessedWord.concat("_");
		ifWin = false;
		
	}

	/*
	 * 这个字母之前已经猜过 - 返回-1（RPT）
	 * 猜对 - 返回该字母位置 + 99 
	 * 猜错 - -99 
	 * 猜对并过关 - 返回该字母位置和一个100（WIN） 
	 * 猜错并失败 - 返回-100（FAIL）
	 * (默认c是一个字母，即长度为1)
	 */
	public ArrayList<Integer> guess(String c) {
		ArrayList<Integer> pos = new ArrayList<Integer>();
		char c_char = c.toCharArray()[0];
		
		//如果这个字母之前已经猜过
		if(guessedChar.contains(c_char)){
			pos.add(RPT);
			return pos;
		}
		else{
			//将这个字母加入guessedChar中
			guessedChar.add(c_char);
			
			//判断是否猜对
			boolean hit = false;
			
			char[] correctWordChar = correctWord.toCharArray();
			for(int i = 0; i < correctWord.length(); i++){
				//猜对
				if(correctWordChar[i]==c_char){
					pos.add(i);
					StringBuffer bufguessedWord = new StringBuffer(guessedWord);
					bufguessedWord.setCharAt(i, c_char);
					guessedWord = bufguessedWord.toString();
					hit = true;
				}
			}
			
//			System.out.println(guessedWord);
			
			//猜错
			if(!hit){
				life--;
				wrongChar.add(c_char);
				//猜错并失败
				if(life == 0){
					pos.add(FAIL);
					return pos;
				}
				//猜错还没失败
				else {
					pos.add(WRONG);
					return pos;
				}
			}
			//猜对
			else{
				//猜对并通关
				if(guessedWord.equals(correctWord)){
					pos.add(RIGHT_NEXTROUND);
					ifWin = true;
					return pos;
				}
				//猜对没通关
				else {
					pos.add(RIGHT);
					return pos;
				}
			}
		}
		
		
	}
	
	/*
	 * 随机给出一个字母的提示
	 */
	public CharPosition help() {
		CharPosition cp = new CharPosition();
		
		//随机产生出一个提示的字母，确保该字母不在已经猜过的字母当中
		char hintChar;
		char[] correctWordChar = correctWord.toCharArray();
		do{
			Random rand = new Random();
			int r = rand.nextInt(correctWord.length());
			hintChar = correctWordChar[r];
		} while(guessedChar.contains(hintChar));
		
		
		cp.c = hintChar;
		for(int i =0; i<correctWord.length();i++){
			if(correctWordChar[i]==hintChar){
				cp.pos.add(i);
			}
		}
		
		return cp;
	}

	public String getCorrectWord() {
		return correctWord;
	}

	public String getCorrectExplain() {
		return correctExplain;
	}

	public int getLife() {
		return life;
	}

	public String getGuessedWord() {
		return guessedWord;
	}

	public boolean isIfWin() {
		return ifWin;
	}


	/*
	 * 读取词库
	 */
	private void loadDictionary(String dicFile) {
		try {
			FileReader reader = new FileReader(dicFile);
			BufferedReader bufreader = new BufferedReader(reader);
			String str = "";
			try {
				while ((str = bufreader.readLine()) != null) {
					wordBand.add(str.split(":")[0]);
					explainBand.add(str.split(":")[1]);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/*
	 * 从词库中选择一个相应长度的单词
	 * */
	private void initializeCorrectWord(int roundNum) {
		int wordLength = roundNum + 2;

		Random rand = new Random();
		int r = rand.nextInt(wordBand.size());

		do {
			r = (r + 1) % (wordBand.size());
			correctWord = wordBand.get(r);
			correctExplain = explainBand.get(r);
		} while (correctWord.length() != wordLength);

	}

}
