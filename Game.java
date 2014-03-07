package core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Game {
	final static int HIGH_DIFF = 3;
	final static int MED_DIFF = 2;
	final static int LOW_DIFF = 1;
	final static int INIT_SCORE = 10;
	final static int INIT_GOLD = 3;
	final static int RIGHT_INCRE = 1;
	final static int GOLD_INCRE = 1;
	final static int WRONG_DECRE = 1;
	final static int NEXTROUND_INCRE = 10;
	
	final static String HIGH_DICFILE = "dictionary.txt";
	final static String MED_DICFILE = "dictionary.txt";
	final static String LOW_DICFILE = "dictionary.txt";
	
	private int roundNum; //当前关数
	private int score;
	private int gold;
	private int difficulty;
	private String saveFile;//存档文件名
	private String rankFile;//排行榜文件名
	private String dicFile;//词汇本文件名
	private Round round;
	
	private ArrayList<Rank> ranks;//排行榜
	
	
	public Game(){
		saveFile = "save.txt";
		rankFile = "rank.txt";
	}
	
	/*
	 * 开始游戏
	 * 无存档 - 返回0
	 * 有存档 - 返回1
	 * */
	public int startGame(){
		//判断是否有存档
		try {
			FileReader reader = new FileReader(saveFile);
			BufferedReader bufreader = new BufferedReader(reader);
			try {
				String str = bufreader.readLine();//save.txt第一行存档格式为save:no或save:yes
				//如果没有存档
				if(str.split(":")[1].equals("no")){
					startPlay(1,INIT_SCORE,INIT_GOLD,LOW_DIFF);
					return 0;
				}
				//如果有存档
				else{
					return 1;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	
	/*
	 * 进入下一关
	 * */
	public void nextRound(){
		score+=NEXTROUND_INCRE;
		gold+=GOLD_INCRE;
		roundNum++;
		round = new Round(roundNum,dicFile);
	}
	

	
	/*
	 * 这个字母之前已经猜过 - 返回-1（RPT）
	 * 猜对 - 返回该字母位置 
	 * 猜错 - 返回空 
	 * 猜对并过关 - 返回该字母位置和一个100（WIN） 
	 * 猜错并失败 - 返回-100（FAIL）
	 * (默认c是一个字母，即长度为1)
	 * */
	
	
	final static int RIGHT = 99;
	final static int RPT = -1;
	final static int FAIL = -100;
	final static int RIGHT_NEXTROUND = 100;
	final static int WRONG = -99;
	
	final static int NEXTROUND = 101;
	final static int WIN_RANK = 102;
	final static int WIN_NORANK = 103;
	final static int FAIL_RANK = -101;
	final static int FAIL_NORANK = 102;
	
	public ArrayList<Integer> guess(String c){
		ArrayList<Integer> pos = round.guess(c);
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		//The last number in the array is a marker
		int marker = pos.get(pos.size()-1);
		switch(marker){
		//已经猜过这个字母
		case RPT:
			break;
		//猜对
		case RIGHT:
			score+=RIGHT_INCRE;
			break;
		case RIGHT_NEXTROUND:
			//猜对进入下一关
			if(roundNum!= 8){
				score+=RIGHT_INCRE;
				nextRound();
				pos.add(NEXTROUND);
			}
			else{
				//猜对并通关并进入排行榜
				if(canRank()){
					score+=RIGHT_INCRE;
					score+=NEXTROUND_INCRE;
					pos.add(WIN_RANK);
				}
				//猜对并通关没有进入排行榜
				else{
					score+=RIGHT_INCRE;
					score+=NEXTROUND_INCRE;
					pos.add(WIN_NORANK);
				}
			}
			break;
		//猜错
		case WRONG:
			score-=WRONG_DECRE;
			break;
		case FAIL:
			//失败进入排行榜
			if(canRank()){
				score-=WRONG_DECRE;
				pos.add(FAIL_RANK);
			}
			//失败没有进入排行榜
			else{
				score-=WRONG_DECRE;
				pos.add(FAIL_NORANK);
			}
			break;
		default:
			break;
		}
		
		
		return result;
	}
	
	/*
	 * 提示
	 * 返回提示的字母和位置
	 * */
	public CharPosition help(){
		CharPosition cp = round.help();
		
		return cp;
	}
	
	/*
	 * 保存并退出
	 * */
	public void saveAndExit(){
		
	}
	
	/*
	 * 继续游戏
	 * */
	public void continueGame(){
		
	}
	
	/*
	 * 查看排行榜
	 * */
	public ArrayList<Rank> getRank(){
		ArrayList<Rank> ranks = new ArrayList<Rank>();
		try {
			FileReader reader = new FileReader(saveFile);
			BufferedReader bufreader = new BufferedReader(reader);
			String str = "";
			try {
				while((str = bufreader.readLine())!=null){
					String name = str.split(",")[0];
					int score = Integer.parseInt(str.split(",")[1]);
					int roundNum = Integer.parseInt(str.split(",")[2]);
					Rank rank = new Rank(name,score,roundNum);
					ranks.add(rank);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return ranks;
	}
	
	
	/*
	 * 设置
	 * */
	public void setDifficulty(){
		
	}
	
	/*
	 * 能够进入排行榜的用户输入自己的姓名后，调用
	 * */
	public void getIntoRank(String user){
		
	}
	
	
	
	
	
	/*
	 * 根据分数判断是否进入排行榜
	 * */
	private boolean canRank(){
		ranks = getRank();
		//排行榜上人数不足5
		if(ranks.size()<5)
			return true;
		//排行榜上已有5人
		else{
			int lastScore = ranks.get(ranks.size()-1).getScore();
			int lastRoundNum = ranks.get(ranks.size()-1).getRoundNum();
			//当前分数与最后一名分数相同时，比较关数
			if(score==lastScore){
				if(roundNum>=lastRoundNum)
					return true;
				else
					return false;
			}
			//当前分数比最后一名分数高
			else if(score>lastScore)
				return true;
			else
				return false;
		}
	}
	
	private void startPlay(int roundNum, int score, int gold, int diff){
		this.roundNum = roundNum;
		this.score = score;
		this.gold = gold;
		this.difficulty = diff;
		
		score = INIT_SCORE;
		gold = INIT_GOLD;
		
		switch(difficulty){
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
		
		round = new Round(roundNum,dicFile);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
