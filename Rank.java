package core;

public class Rank {
	String name;
	int score;
	int roundNum;
	
	public Rank(String n, int s, int r){
		this.name = n;
		this.score = s;
		this.roundNum = r;
	}
	
	public Rank(){
		
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
	
	
	

}
