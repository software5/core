package core;

import java.util.ArrayList;

public class Game {
	// �Ѷ�
	final static int HIGH_DIFF = 3;
	final static int MED_DIFF = 2;
	final static int LOW_DIFF = 1;

	final static String HIGH_DICFILE = "dictionary.txt";
	final static String MED_DICFILE = "dictionary.txt";
	final static String LOW_DICFILE = "dictionary.txt";

	// ����
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

	private int roundNum; // ��ǰ����
	private int score;
	private int gold;
	private int difficulty;
	private String saveFile;// �浵�ļ���
	private String dicFile;// �ʻ㱾�ļ���
	private Round round;
	private int status = 1;

	private RankFunction rankfunc;
	private SaveFunction savefunc;

	public Game(String rf, String sf) {
		rankfunc = new RankFunction(rf);
		savefunc = new SaveFunction(sf);
	}

	/*
	 * ��ʼ��Ϸ ifCover�� true�����ж��Ƿ��д浵���û�ѡ�񡰸�����ǰ�浵��ʱ���á� ����0����ʼ��Ϸ��
	 * false���ж��Ƿ��д浵���û��������ʼ��Ϸ��ʱ���á� �޴浵 - ����0������ʼ��Ϸ �д浵 -
	 * ����1�������棺�����û����Ƿ񸲸���ǰ�浵����
	 */
	public int startGame(boolean ifCover) {
		if (ifCover == true) {
			savefunc.saveGame("no", 0, 0, 0, 0);
			startPlay(1, INIT_SCORE, INIT_GOLD, LOW_DIFF);
			return 0;
		} else {
			// �ж��Ƿ��д浵
			if (!savefunc.hasSave()) {
				startPlay(1, INIT_SCORE, INIT_GOLD, LOW_DIFF);
				return 0;
			} else
				return 1;
		}

	}

	/*
	 * ������Ϸ �޴浵 - ����0�����棺�����û����޴浵��¼���� �д浵 - ����1����ʼ��Ϸ
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
	 * �鿴���а�
	 */
	public ArrayList<Rank> getRank() {
		return rankfunc.getRanks();
	}

	/*
	 * ����
	 */
	public void setDifficulty(int d) {
		this.difficulty = d;
	}

	/*
	 * ������һ�� ���û����غ󣬵�������ʾ������������������󣬵����������һ�ء��󣬵��á�
	 */
	public void nextRound() {
		score += NEXTROUND_INCRE;
		gold += GOLD_INCRE;
		roundNum++;
		round = new Round(roundNum, dicFile);
	}

	/*
	 * "Enter"��
	 * 
	 * ��ĸ�¶� - ����λ��+99��RIGHT�� ��ĸ�¶�+������һ�� - ����λ��+101��NEXTROUND�� ��ĸ�¶�+ͨ��+�������а� -
	 * ����λ��+102��WIN_RANK�� ��ĸ�¶�+ͨ��+δ�������а� - ����λ��+103��WIN_NORANK�� ��ĸ�´� -
	 * ����-99��WRONG�� ��ĸ�´�+ʧ��+�������а� - ����-101��FAIL_RANK�� ��ĸ�´�+ʧ��+δ�������а� -
	 * ����-102��FAIL_NORANK�� �����ظ���ĸ - ����-1��RPT�� �����ʽ����ȷ - ����-2(ERROR)
	 */
	public ArrayList<Integer> guess(String c) {
		c = c.toLowerCase();
		ArrayList<Integer> pos = new ArrayList<Integer>();
		// ���ж������ʽ
		char[] cchar = c.toCharArray();
		if ((cchar.length != 1) || (cchar[0] < 97) || (cchar[0] > 122)) {
			status = ERROR;
			return pos;
		} else {
			pos = round.guess(c);
			status = round.getStatus();
			switch (status) {
			// �Ѿ��¹������ĸ
			case RPT:
				break;
			// �¶�
			case RIGHT:
				score += RIGHT_INCRE;
				break;
			case RIGHT_NEXTROUND:
				// �¶Խ�����һ��
				if (roundNum != ALLROUND) {
					score += RIGHT_INCRE;
					// nextRound();
					status = NEXTROUND;
				} else {
					// �¶Բ�ͨ�ز��������а�
					if (rankfunc.canRank(score, roundNum)) {
						score += RIGHT_INCRE;
						status = WIN_RANK;
					}
					// �¶Բ�ͨ��û�н������а�
					else {
						score += RIGHT_INCRE;
						status = WIN_NORANK;
					}
				}
				break;
			// �´�
			case WRONG:
				score -= WRONG_DECRE;
				break;
			case FAIL:
				// ʧ�ܽ������а�
				if (rankfunc.canRank(score, roundNum)) {
					score -= WRONG_DECRE;
					status = FAIL_RANK;
				}
				// ʧ��û�н������а�
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
	 * ��ʾ �����������1��������ĸ�Լ���λ�� ����������㣬����null��-1 ����ҵ������ʾ����
	 */
	public CharPosition help(int index) {
		CharPosition cp = new CharPosition();
		// ���������1
		if (gold >= 1) {
			gold--;
			score -= WRONG_DECRE;
			cp = round.help(index);
			guess(new Character(cp.c).toString());
			return cp;
		}
		// ���������
		else {
			cp.c = (Character) null;
			ArrayList<Integer> pos = new ArrayList<Integer>();
			pos.add(-1);
			cp.pos = pos;
		}

		return cp;
	}

	/*
	 * ���沢�˳�
	 */
	public void saveAndExit() {
		savefunc.saveGame("yes", roundNum, score, gold, difficulty);
	}

	/*
	 * ���ܹ��������а���û������Լ��������󣬵��á�
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
