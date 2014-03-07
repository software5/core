package core;

import java.util.ArrayList;

public class Game {
	// 难度
	final static int HIGH_DIFF = 3;
	final static int MED_DIFF = 2;
	final static int LOW_DIFF = 1;

	final static String HIGH_DICFILE = "dictionary.txt";
	final static String MED_DICFILE = "dictionary.txt";
	final static String LOW_DICFILE = "dictionary.txt";

	// 分数
	final static int INIT_SCORE = 10;
	final static int INIT_GOLD = 3;
	final static int RIGHT_INCRE = 1;
	final static int GOLD_INCRE = 1;
	final static int WRONG_DECRE = 1;
	final static int NEXTROUND_INCRE = 10;

	// guess
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

	final static int ALLROUND = 3;

	private int roundNum; // 当前关数
	private int score;
	private int gold;
	private int difficulty;
	private String saveFile;// 存档文件名
	private String dicFile;// 词汇本文件名
	private Round round;
	private int status = 1;

	private RankFunction rankfunc;
	private SaveFunction savefunc;

	public Game(String rf, String sf) {
		rankfunc = new RankFunction(rf);
		savefunc = new SaveFunction(sf);
	}

	/*
	 * 开始游戏 ifCover： true，不判断是否有存档【用户选择“覆盖以前存档”时调用】 返回0，开始游戏；
	 * false，判断是否有存档【用户点击“开始游戏”时调用】 无存档 - 返回0，并开始游戏 有存档 -
	 * 返回1，【界面：提醒用户“是否覆盖以前存档”】
	 */
	public int startGame(boolean ifCover) {
		if (ifCover == true) {
			savefunc.saveGame("no", 0, 0, 0, 0);
			startPlay(1, INIT_SCORE, INIT_GOLD, LOW_DIFF);
			return 0;
		} else {
			// 判断是否有存档
			if (!savefunc.hasSave()) {
				startPlay(1, INIT_SCORE, INIT_GOLD, LOW_DIFF);
				return 0;
			} else
				return 1;
		}

	}

	/*
	 * 继续游戏 无存档 - 返回0【界面：提醒用户“无存档记录”】 有存档 - 返回1，开始游戏
	 */
	public int continueGame() {
		if (!savefunc.hasSave())
			return 0;
		else {
			startPlay(savefunc.getRound(), savefunc.getScore(),
					savefunc.getGold(), savefunc.getDiff());
			return 1;
		}
	}

	/*
	 * 查看排行榜
	 */
	public ArrayList<Rank> getRank() {
		return rankfunc.getRanks();
	}

	/*
	 * 设置
	 */
	public void setDifficulty(int d) {
		this.difficulty = d;
	}

	/*
	 * 进入下一关 【用户过关后，弹出框显示关数、分数、金币数后，点击“进入下一关”后，调用】
	 */
	public void nextRound() {
		score += NEXTROUND_INCRE;
		gold += GOLD_INCRE;
		roundNum++;
		round = new Round(roundNum, dicFile);
	}

	/*
	 * "Enter"键
	 * 
	 * 字母猜对 - 返回位置+99（RIGHT） 字母猜对+进入下一关 - 返回位置+101（NEXTROUND） 字母猜对+通关+进入排行榜 -
	 * 返回位置+102（WIN_RANK） 字母猜对+通关+未进入排行榜 - 返回位置+103（WIN_NORANK） 字母猜错 -
	 * 返回-99（WRONG） 字母猜错+失败+进入排行榜 - 返回-101（FAIL_RANK） 字母猜错+失败+未进入排行榜 -
	 * 返回-102（FAIL_NORANK） 输入重复字母 - 返回-1（RPT） 输入格式不正确 - 返回-2(ERROR)
	 */
	public ArrayList<Integer> guess(String c) {
		c = c.toLowerCase();
		ArrayList<Integer> pos = new ArrayList<Integer>();
		// 先判断输入格式
		char[] cchar = c.toCharArray();
		if ((cchar.length != 1) || (cchar[0] < 97) || (cchar[0] > 122)) {
			status = ERROR;
			return pos;
		} else {
			pos = round.guess(c);
			status = round.getStatus();
			switch (status) {
			// 已经猜过这个字母
			case RPT:
				break;
			// 猜对
			case RIGHT:
				score += RIGHT_INCRE;
				break;
			case RIGHT_NEXTROUND:
				// 猜对进入下一关
				if (roundNum != ALLROUND) {
					score += RIGHT_INCRE;
					// nextRound();
					status = NEXTROUND;
				} else {
					// 猜对并通关并进入排行榜
					if (rankfunc.canRank(score, roundNum)) {
						score += RIGHT_INCRE;
						status = WIN_RANK;
					}
					// 猜对并通关没有进入排行榜
					else {
						score += RIGHT_INCRE;
						status = WIN_NORANK;
					}
				}
				break;
			// 猜错
			case WRONG:
				score -= WRONG_DECRE;
				break;
			case FAIL:
				// 失败进入排行榜
				if (rankfunc.canRank(score, roundNum)) {
					score -= WRONG_DECRE;
					status = FAIL_RANK;
				}
				// 失败没有进入排行榜
				else {
					score -= WRONG_DECRE;
					status = FAIL_NORANK;
				}
				break;
			default:
				break;
			}
		}

		return pos;
	}

	/*
	 * 提示 若金币数大于1，返回字母以及其位置 若金币数不足，返回null和-1 【玩家点击“提示”】
	 */
	public CharPosition help(int index) {
		CharPosition cp = new CharPosition();
		// 金币数大于1
		if (gold >= 1) {
			gold--;
			score -= WRONG_DECRE;
			cp = round.help(index);
			guess(new Character(cp.c).toString());
			return cp;
		}
		// 金币数不足
		else {
			cp.c = (Character) null;
			ArrayList<Integer> pos = new ArrayList<Integer>();
			pos.add(-1);
			cp.pos = pos;
		}

		return cp;
	}

	/*
	 * 保存并退出
	 */
	public void saveAndExit() {
		savefunc.saveGame("yes", roundNum, score, gold, difficulty);
	}

	/*
	 * 【能够进入排行榜的用户输入自己的姓名后，调用】
	 */
	public void getIntoRank(String user) {
		rankfunc.getIntoRank(user, roundNum, score);
	}

	public Round getRound() {
		return this.round;
	}

	public int getRoundNum() {
		return roundNum;
	}

	public int getScore() {
		return score;
	}

	public int getGold() {
		return gold;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int getStatus() {
		return status;
	}

	private void startPlay(int roundNum, int score, int gold, int diff) {
		this.roundNum = roundNum;
		this.score = score;
		this.gold = gold;
		this.difficulty = diff;

		score = INIT_SCORE;
		gold = INIT_GOLD;

		switch (difficulty) {
		case LOW_DIFF:
			dicFile = LOW_DICFILE;
			break;
		case MED_DIFF:
			dicFile = MED_DICFILE;
			break;
		case HIGH_DIFF:
			dicFile = HIGH_DICFILE;
			break;
		default:
			dicFile = LOW_DICFILE;
			break;
		}

		round = new Round(roundNum, dicFile);
	}

}
