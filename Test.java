package core;

import java.util.ArrayList;
import java.util.Scanner;

public class Test {
	static Game g = new Game("rank.txt", "save.txt");

	public static void main(String[] args) {

		init();

	}

	private static int init() {
		System.out.println("1.新建游戏 2.继续游戏 3.查看排行榜 4.设置难度 5.退出");
		System.out.println("请输入：");
		Scanner scan = new Scanner(System.in);
		int input = Integer.parseInt(scan.next());
		go_to(input);
		return input;
	}

	private static void go_to(int result) {
		Scanner scan;
		switch (result) {
		case 1:
			// 用户点击“新建游戏”
			result = g.startGame(false);
			// 无存档，直接开始
			if (result == 0)
				beginGame();
			// 有存档，询问玩家是否覆盖
			else {
				System.out.println("有存档文件，是否覆盖之前?(y/n)");
				scan = new Scanner(System.in);
				String s = scan.next();
				if (s.equals("y")) {
					g.startGame(true);
					beginGame();
				} else {
					System.out.println("不覆盖！返回主界面");
					init();
				}
			}

			break;
		case 2:
			result = g.continueGame();
			if (result == 0) {
				System.out.println("无存档文件！返回主界面");
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
		System.out.print("当前关数：" + g.getRoundNum());
		System.out.print("当前分数：" + g.getScore());
		System.out.print("当前金币：" + g.getGold());
		System.out.println("当前难度：" + g.getDifficulty());

		System.out.println("(正确单词：" + g.getRound().getCorrectWord() + ")");
		System.out.println(g.getRound().getGuessedWord());
		Scanner scan = new Scanner(System.in);
		String c = scan.next();
		ArrayList<Integer> pos = new ArrayList<Integer>();
		if (c.equals("help")) {
			CharPosition cp = g.help(1);
			guess_go_to(g.getStatus());
		} else if (c.equals("save")) {
			g.saveAndExit();
			System.out.println("保存并结束");
		} else {
			pos = g.guess(c);
			guess_go_to(g.getStatus());
		}

	}

	// guess

	/*
	 * "Enter"键
	 * 
	 * 字母猜对 - 返回位置+99（RIGHT） 字母猜对+进入下一关 - 返回位置+101（NEXTROUND） 字母猜对+通关+进入排行榜 -
	 * 返回位置+102（WIN_RANK） 字母猜对+通关+未进入排行榜 - 返回位置+103（WIN_NORANK） 字母猜错 -
	 * 返回-99（WRONG） 字母猜错+失败+进入排行榜 - 返回-101（FAIL_RANK） 字母猜错+失败+未进入排行榜 -
	 * 返回-102（FAIL_NORANK） 输入重复字母 - 返回-1（RPT） 输入格式不正确 - 返回-2(ERROR)
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
			System.out.println("进入下一关!");
			g.nextRound();
			beginGame();
			break;
		case WIN_RANK:
			System.out.println("通关！进入排行榜！");
			Scanner scan = new Scanner(System.in);
			String name = scan.next();
			g.getIntoRank(name);
			break;
		case WIN_NORANK:
			System.out.println("通关！未进入排行榜！");
			break;
		case FAIL_RANK:
			System.out.println("失败！进入排行榜！");
			g.getIntoRank("test");
			break;
		case FAIL_NORANK:
			System.out.println("失败！未进入排行榜！");
			break;
		case RPT:
			System.out.println("重复输入字母");
			beginGame();
			break;
		case ERROR:
			System.out.println("格式错误");
			beginGame();
			break;

		}
	}

}
