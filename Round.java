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

	private int roundNum;// ���������ڵ��ʳ���
	private String dicFile;// ���ʱ��ļ���

	private String correctWord;
	private String correctExplain;
	private int life;
	private ArrayList<Character> guessedChar;// ���в¹�����ĸ
	private ArrayList<Character> wrongChar;// ���в´����ĸ
	private String guessedWord = "";// �Ѿ��³��ĵ���
	private int status;

	private ArrayList<String> wordBand = new ArrayList<String>();
	private ArrayList<String> explainBand = new ArrayList<String>();

	public Round(int roundNum, String dicFile) {
		this.roundNum = roundNum;
		this.dicFile = dicFile;

		// read the dictionary file, get all words and their explanations
		loadDictionary(dicFile);

		// select a word to guess form word band
		initializeCorrectWord(roundNum);

		// System.out.println(correctWord);

		// initialize other variables
		life = 6;
		guessedChar = new ArrayList<Character>();
		wrongChar = new ArrayList<Character>();
		for (int i = 0; i < correctWord.length(); i++)
			guessedWord = guessedWord.concat("_");

	}

	/*
	 * �����ĸ֮ǰ�Ѿ��¹� - ����-1��RPT�� �¶� - ���ظ���ĸλ�� + 99 �´� - -99 �¶Բ����� -
	 * ���ظ���ĸλ�ú�һ��100��WIN�� �´�ʧ�� - ����-100��FAIL�� (Ĭ��c��һ����ĸ��������Ϊ1)
	 */
	public ArrayList<Integer> guess(String c) {
		ArrayList<Integer> pos = new ArrayList<Integer>();
		char c_char = c.toCharArray()[0];

		// ��������ĸ֮ǰ�Ѿ��¹�
		if (guessedChar.contains(c_char)) {
			pos.add(RPT);
			return pos;
		} else {
			// �������ĸ����guessedChar��
			guessedChar.add(c_char);

			// �ж��Ƿ�¶�
			boolean hit = false;

			char[] correctWordChar = correctWord.toCharArray();
			for (int i = 0; i < correctWord.length(); i++) {
				// �¶�
				if (correctWordChar[i] == c_char) {
					pos.add(i);
					StringBuffer bufguessedWord = new StringBuffer(guessedWord);
					bufguessedWord.setCharAt(i, c_char);
					guessedWord = bufguessedWord.toString();
					hit = true;
				}
			}

			// �´�
			if (!hit) {
				life--;
				wrongChar.add(c_char);
				// �´�ʧ��
				if (life == 0) {
					status = FAIL;
					return pos;
				}
				// �´�ûʧ��
				else {
					status = WRONG;
					return pos;
				}
			}
			// �¶�
			else {
				// �¶Բ�ͨ��
				if (guessedWord.equals(correctWord)) {
					status = RIGHT_NEXTROUND;
					return pos;
				}
				// �¶�ûͨ��
				else {
					status = RIGHT;
					return pos;
				}
			}
		}

	}

	/*
	 * ����һ����ĸ����ʾ
	 */
	public CharPosition help(int index) {
		CharPosition cp = new CharPosition();

		// ���������һ����ʾ����ĸ��ȷ������ĸ�����Ѿ��¹�����ĸ����
		char hintChar = correctWord.charAt(index);
		// char[] correctWordChar = correctWord.toCharArray();
		// do {
		// Random rand = new Random();
		// int r = rand.nextInt(correctWord.length());
		// hintChar = correctWordChar[r];
		// } while (guessedChar.contains(hintChar));

		// �ҵ�����ĸ����λ��
		cp.c = hintChar;
		for (int i = 0; i < correctWord.length(); i++) {
			if (correctWord.charAt(index) == hintChar) {
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

	public int getStatus() {
		return status;
	}

	/*
	 * ��ȡ�ʿ�
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
	 * �Ӵʿ���ѡ��һ����Ӧ���ȵĵ���
	 */
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
