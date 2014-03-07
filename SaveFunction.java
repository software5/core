package core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveFunction {
	String saveFile;
	String save;
	int round;
	int score;
	int gold;
	int diff;
	
	public SaveFunction(String saveFile){
		this.saveFile = saveFile;
		
		loadFile();
	}
	
	/*
	 * 判断是否有存档
	 * */
	public boolean hasSave(){
		if(save.equals("yes"))
			return true;
		else
			return false;
	}
	
	public void saveGame(String save,int round,int score,int gold,int diff){
		try {
			FileWriter writer = new FileWriter(saveFile);
			writer.write("save:"+save+"\n");
			writer.write("round:"+round+"\n");
			writer.write("score:"+score+"\n");
			writer.write("gold:"+gold+"\n");
			writer.write("difficulty:"+diff);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getSaveFile() {
		return saveFile;
	}

	public String getSave() {
		return save;
	}

	public int getRound() {
		return round;
	}

	public int getScore() {
		return score;
	}

	public int getGold() {
		return gold;
	}

	public int getDiff() {
		return diff;
	}

	private void loadFile(){
		try {
			FileReader reader = new FileReader(saveFile);
			BufferedReader bufreader = new BufferedReader(reader);
			try {
				save = bufreader.readLine().split(":")[1];
				if(hasSave()){
					round = Integer.parseInt(bufreader.readLine().split(":")[1]);
					score = Integer.parseInt(bufreader.readLine().split(":")[1]);
					gold = Integer.parseInt(bufreader.readLine().split(":")[1]);
					diff = Integer.parseInt(bufreader.readLine().split(":")[1]);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
