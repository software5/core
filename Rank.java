package core;

public class Rank {
	int rank;
	String name;
	int score;
	int roundNum;

	public Rank(String n, int s, int r) {
		this.name = n;
		this.score = s;
		this.roundNum = r;
	}

	public Rank(int rank, String name, int score, int roundNum) {
		this.rank = rank;
		;
		this.name = name;
		this.score = score;
		this.roundNum = roundNum;
	}

	public Rank() {

	}

	/*
	 * 排名高 - 返回1 排名相同 - 返回0 排名低 - 返回-1
	 */
	public int compareRank(Rank rank) {
		if (score > rank.getScore())
			return 1;
		else if (score == rank.getScore()) {
			if (roundNum > rank.getRoundNum())
				return 1;
			else if (roundNum == rank.getRoundNum())
				return 0;
			else
				return -1;
		} else
			return -1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRoundNum() {
		return roundNum;
	}

	public void setRoundNum(int roundNum) {
		this.roundNum = roundNum;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

}
