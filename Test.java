package core;

import java.util.ArrayList;
import java.util.Scanner;

public class Test {
	static Game g = new Game("rank.txt", "save.txt");

	public static void main(String[] args) {

		init();

	}

	private static int init() {
		System.out.println("1.�½���Ϸ 2.������Ϸ 3.�鿴���а� 4.�����Ѷ� 5.�˳�");
		System.out.println("�����룺");
		Scanner scan = new Scanner(System.in);
		int input = Integer.parseInt(scan.next());
		go_to(input);
		return input;
	}

	private static void go_to(int result) {
		Scanner scan;
		switch (result) {
		case 1:
			// �û�������½���Ϸ��
			result = g.startGame(false);
			// �޴浵��ֱ�ӿ�ʼ
			if (result == 0)
				beginGame();
			// �д浵��ѯ������Ƿ񸲸�
			else {
				System.out.println("�д浵�ļ����Ƿ񸲸�֮ǰ?(y/n)");
				scan = new Scanner(System.in);
				String s = scan.next();
				if (s.equals("y")) {
					g.startGame(true);
					beginGame();
				} else {
					System.out.println("�����ǣ�����������");
					init();
				}
			}

			break;
		case 2:
			result = g.continueGame();
			if (result == 0) {
				System.out.println("�޴浵�ļ�������������");
				init();
			} else {
				beginGame();
			}
			break;
		case 3:
			g.getRank();
			break;
		case 4:
			g.setDifficulty(1);
			break;
		}
	}

	private static void beginGame() {
		System.out.print("��ǰ������" + g.getRoundNum());
		System.out.print("��ǰ������" + g.getScore());
		System.out.print("��ǰ��ң�" + g.getGold());
		System.out.println("��ǰ�Ѷȣ�" + g.getDifficulty());

		System.out.println("(��ȷ���ʣ�" + g.getRound().getCorrectWord() + ")");
		System.out.println(g.getRound().getGuessedWord());
		Scanner scan = new Scanner(System.in);
		String c = scan.next();
		ArrayList<Integer> pos = new ArrayList<Integer>();
		if (c.equals("help")) {
			CharPosition cp = g.help(1);
			guess_go_to(g.getStatus());
		} else if (c.equals("save")) {
			g.saveAndExit();
			System.out.println("���沢����");
		} else {
			pos = g.guess(c);
			guess_go_to(g.getStatus());
		}

	}

	// guess

	/*
	 * "Enter"��
	 * 
	 * ��ĸ�¶� - ����λ��+99��RIGHT�� ��ĸ�¶�+������һ�� - ����λ��+101��NEXTROUND�� ��ĸ�¶�+ͨ��+�������а� -
	 * ����λ��+102��WIN_RANK�� ��ĸ�¶�+ͨ��+δ�������а� - ����λ��+103��WIN_NORANK�� ��ĸ�´� -
	 * ����-99��WRONG�� ��ĸ�´�+ʧ��+�������а� - ����-101��FAIL_RANK�� ��ĸ�´�+ʧ��+δ�������а� -
	 * ����-102��FAIL_NORANK�� �����ظ���ĸ - ����-1��RPT�� �����ʽ����ȷ - ����-2(ERROR)
	 */
	final static int ERROR = -2;
	final static int RIGHT = 99;
	final static int RPT = -1;
	final static int WRONG = -99;
	final static int FAIL = -100;
	final static int RIGHT_NEXTROUND = 100;

	final static int NEXTROUND = 101;
	final static int WIN_RANK = 102;
	final static int WIN_NORANK = 103;
	final static int FAIL_RANK = -101;
	final static int FAIL_NORANK = -102;

	private static void guess_go_to(int m) {

		switch (m) {
		case RIGHT:
			beginGame();
			break;
		case WRONG:
			beginGame();
			break;
		case NEXTROUND:
			System.out.println("������һ��!");
			g.nextRound();
			beginGame();
			break;
		case WIN_RANK:
			System.out.println("ͨ�أ��������а�");
			Scanner scan = new Scanner(System.in);
			String name = scan.next();
			g.getIntoRank(name);
			break;
		case WIN_NORANK:
			System.out.println("ͨ�أ�δ�������а�");
			break;
		case FAIL_RANK:
			System.out.println("ʧ�ܣ��������а�");
			g.getIntoRank("test");
			break;
		case FAIL_NORANK:
			System.out.println("ʧ�ܣ�δ�������а�");
			break;
		case RPT:
			System.out.println("�ظ�������ĸ");
			beginGame();
			break;
		case ERROR:
			System.out.println("��ʽ����");
			beginGame();
			break;

		}
	}

}
