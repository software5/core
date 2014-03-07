package core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RankFunction {
	String rankFile;
	ArrayList<Rank> ranks;

	public RankFunction(String rankFile) {
		this.rankFile = rankFile;

		loadRank();

	}

	/*
	 * 获得当前排行榜内容
	 */
	public ArrayList<Rank> getRanks() {
		return ranks;
	}

	/*
	 * 更新排行榜
	 */
	public void getIntoRank(String user, int roundNum, int score) {
		// 更新排名
		Rank thisRank = new Rank(user, score, roundNum);
		boolean isSet = false;
		int i = 0;
		do {
			if (ranks.size() == 0) {
				thisRank.setRank(1);
				ranks.add(thisRank);
				isSet = true;
			} else {
				Rank cmpRank = ranks.get(i);
				int cmp = thisRank.compareRank(cmpRank);
				if (cmp > 0) {
					thisRank.setRank(cmpRank.getRank());
					changeRank(cmpRank.getRank());
					ranks.add(i, thisRank);
					isSet = true;
				} else if (cmp == 0) {
					thisRank.setRank(cmpRank.getRank());
					ranks.add(i, thisRank);
					isSet = true;
				} else {
					i++;
				}
			}

		} while (!isSet && i < ranks.size());

		if (!isSet) {
			thisRank.setRank(ranks.get(ranks.size() - 1).getRank() + 1);
			ranks.add(thisRank);
		}

		// 将更新后的排名写入rank.txt
		i = 0;
		FileWriter writer;
		try {
			writer = new FileWriter("rank.txt");
			while ((i < ranks.size()) && (ranks.get(i).getRank() <= 5)) {
				Rank rank = ranks.get(i);
				String rankStr = rank.getRank() + "," + rank.getName() + ","
						+ rank.getScore() + "," + rank.getRoundNum() + "\n";
				writer.write(rankStr);
				writer.flush();
				i++;
			}
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * 根据分数判断是否进入排行榜
	 */
	public boolean canRank(int score, int roundNum) {
		// 排行榜上人数不足5
		if (ranks.size() < 5)
			return true;
		// 排行榜上已有5人
		else {
			int lastScore = ranks.get(ranks.size() - 1).getScore();
			int lastRoundNum = ranks.get(ranks.size() - 1).getRoundNum();
			// 当前分数与最后一名分数相同时，比较关数
			if (score == lastScore) {
				if (roundNum >= lastRoundNum)
					return true;
				else
					return false;
			}
			// 当前分数比最后一名分数高
			else if (score > lastScore)
				return true;
			else
				return false;
		}
	}

	/*
	 * 从start这一“排名”开始，改变rank中的名次
	 */
	private void changeRank(int start) {
		for (int i = 0; i < ranks.size(); i++) {
			Rank rank = ranks.get(i);
			if (rank.getRank() >= start) {
				int r = rank.getRank();
				rank.setRank(r + 1);
			}
		}
	}

	private void loadRank() {
		ranks = new ArrayList<Rank>();
		try {
			FileReader reader = new FileReader(rankFile);
			BufferedReader bufreader = new BufferedReader(reader);
			String str = "";
			try {
				while ((str = bufreader.readLine()) != null) {
					int r = Integer.parseInt(str.split(",")[0]);
					// System.out.println(r);
					String name = str.split(",")[1];
					// System.out.println(name);
					int score = Integer.parseInt(str.split(",")[2]);
					// System.out.println(score);
					// System.out.println(str.split(",")[3]);;
					int roundNum = Integer.parseInt(str.split(",")[3]);
					Rank rank = new Rank(r, name, score, roundNum);
					ranks.add(rank);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
