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
	 * ��õ�ǰ���а�����
	 */
	public ArrayList<Rank> getRanks() {
		return ranks;
	}

	/*
	 * �������а�
	 */
	public void getIntoRank(String user, int roundNum, int score) {
		// ��������
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

		// �����º������д��rank.txt
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
	 * ���ݷ����ж��Ƿ�������а�
	 */
	public boolean canRank(int score, int roundNum) {
		// ���а�����������5
		if (ranks.size() < 5)
			return true;
		// ���а�������5��
		else {
			int lastScore = ranks.get(ranks.size() - 1).getScore();
			int lastRoundNum = ranks.get(ranks.size() - 1).getRoundNum();
			// ��ǰ���������һ��������ͬʱ���ȽϹ���
			if (score == lastScore) {
				if (roundNum >= lastRoundNum)
					return true;
				else
					return false;
			}
			// ��ǰ���������һ��������
			else if (score > lastScore)
				return true;
			else
				return false;
		}
	}

	/*
	 * ��start��һ����������ʼ���ı�rank�е�����
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
