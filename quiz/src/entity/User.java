package entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
	//一意なユーザーID
	private String id;
	//ハッシュ化されたパスワード
	private String password;
	//ユーザーネーム
	private String userName;
	//週間ランキング用のスコア(過去7日以内のMAXスコア)
	private Integer weeklyMaxScore;
	//日間ランキング用のスコア(現在日時のMAXスコア）
	private Integer dailyMaxScore;
	//過去一週間以内の月曜日のMAXスコア(日曜日用まで同じ理論)
	private Integer monScore;
	//最後にスコアを記録した月曜日の日時(日曜日用まで同じ理論)
	//※週間ランキング用のMAXスコア抽出、判定のため使用する
	private Timestamp monDt;
	private Integer tueScore;
	private Timestamp tueDt;
	private Integer wedScore;
	private Timestamp wedDt;
	private Integer thuScore;
	private Timestamp thuDt;
	private Integer friScore;
	private Timestamp friDt;
	private Integer surScore;
	private Timestamp surDt;
	private Integer sunScore;
	private Timestamp sunDt;
	//日間のスコア順位
	private Integer dailyRank;
	//週間のスコア順位
	private Integer weeklyRank;
	// 日間スコア記録日時
	private Timestamp updDt;
	// 週間スコア記録日時
	private Timestamp weeklyUpdDt;

	public User(String id,String password,String UserName
			   ,Integer wScore,Integer dScore
			   ,Integer monScore,Timestamp monDt
			   ,Integer tueScore,Timestamp tueDt
			   ,Integer wedScore,Timestamp wedDt
			   ,Integer thuScore,Timestamp thuDt
			   ,Integer friScore,Timestamp friDt
			   ,Integer surScore,Timestamp surDt
			   ,Integer sunScore,Timestamp sunDt
			   ,Timestamp updDt,Timestamp weeklyUpdDt) {
		this.setId(id);
		this.setPassword(password);
		this.setUserName(UserName);
		this.setWeeklyMaxScore(wScore);
		this.setDailyMaxScore(dScore);
		this.setMonScore(monScore);
		this.setMonDt(monDt);
		this.setTueScore(tueScore);
		this.setTueDt(tueDt);
		this.setWedScore(wedScore);
		this.setWedDt(wedDt);
		this.setThuScore(thuScore);
		this.setThuDt(thuDt);
		this.setFriScore(friScore);
		this.setFriDt(friDt);
		this.setSurScore(surScore);
		this.setSurDt(surDt);
		this.setSunScore(sunScore);
		this.setSunDt(sunDt);
		this.setUpdDt(updDt);
		this.setWeeklyUpdDt(weeklyUpdDt);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getWeeklyMaxScore() {
		return weeklyMaxScore;
	}

	public void setWeeklyMaxScore(Integer weeklyMaxScore) {
		this.weeklyMaxScore = weeklyMaxScore;
	}

	public Integer getDailyMaxScore() {
		return dailyMaxScore;
	}

	public void setDailyMaxScore(Integer dailyMaxScore) {
		this.dailyMaxScore = dailyMaxScore;
	}

	public Integer getDailyRank() {
		return dailyRank;
	}

	public void setDailyRank(Integer dailyRank) {
		this.dailyRank = dailyRank;
	}

	public Integer getMonScore() {
		return monScore;
	}

	public void setMonScore(Integer monScore) {
		this.monScore = monScore;
	}

	public Timestamp getMonDt() {
		return monDt;
	}

	public void setMonDt(Timestamp monDt) {
		if(monDt == null) {
			this.monDt = Timestamp.valueOf(LocalDateTime.now().minusDays(99));
		}else {
			this.monDt = monDt;
		}
	}

	public Integer getTueScore() {
		return tueScore;
	}

	public void setTueScore(Integer tueScore) {
		this.tueScore = tueScore;
	}

	public Timestamp getTueDt() {
		return tueDt;
	}

	public void setTueDt(Timestamp tueDt) {
		if(tueDt == null) {
			this.tueDt = Timestamp.valueOf(LocalDateTime.now().minusDays(99));
		}else {
			this.tueDt = tueDt;
		}
	}

	public Integer getWedScore() {
		return wedScore;
	}

	public void setWedScore(Integer wedScore) {
		this.wedScore = wedScore;
	}

	public Timestamp getWedDt() {
		return wedDt;
	}

	public void setWedDt(Timestamp wedDt) {
		if(wedDt == null) {
			this.wedDt = Timestamp.valueOf(LocalDateTime.now().minusDays(99));
		}else {
			this.wedDt = wedDt;
		}
	}

	public Integer getThuScore() {
		return thuScore;
	}

	public void setThuScore(Integer thuScore) {
		this.thuScore = thuScore;
	}

	public Timestamp getThuDt() {
		return thuDt;
	}

	public void setThuDt(Timestamp thuDt) {
		if(thuDt == null) {
			this.thuDt = Timestamp.valueOf(LocalDateTime.now().minusDays(99));
		}else {
			this.thuDt = thuDt;
		}
	}

	public Integer getFriScore() {
		return friScore;
	}

	public void setFriScore(Integer friScore) {
		this.friScore = friScore;
	}

	public Timestamp getFriDt() {
		return friDt;
	}

	public void setFriDt(Timestamp friDt) {
		if(friDt == null) {
			this.friDt = Timestamp.valueOf(LocalDateTime.now().minusDays(99));
		}else {
			this.friDt = friDt;
		}
	}

	public Integer getSurScore() {
		return surScore;
	}

	public void setSurScore(Integer surScore) {
		this.surScore = surScore;
	}

	public Timestamp getSurDt() {
		return surDt;
	}

	public void setSurDt(Timestamp surDt) {
		if(surDt == null) {
			this.surDt = Timestamp.valueOf(LocalDateTime.now().minusDays(99));
		}else {
			this.surDt = surDt;
		}
	}

	public Integer getSunScore() {
		return sunScore;
	}

	public void setSunScore(Integer sunScore) {
		this.sunScore = sunScore;
	}

	public Timestamp getSunDt() {
		return sunDt;
	}

	public void setSunDt(Timestamp sunDt) {
		if(sunDt == null) {
			this.sunDt = Timestamp.valueOf(LocalDateTime.now().minusDays(99));
		}else {
			this.sunDt = sunDt;
		}
	}

	public Integer getWeeklyRank() {
		return weeklyRank;
	}

	public void setWeeklyRank(Integer weeklyRank) {
		this.weeklyRank = weeklyRank;
	}

	public Timestamp getUpdDt() {
		return updDt;
	}

	public void setUpdDt(Timestamp updDt) {
		this.updDt = updDt;
	}

	public Timestamp getWeeklyUpdDt() {
		return weeklyUpdDt;
	}

	public void setWeeklyUpdDt(Timestamp weeklyUpdDt) {
		this.weeklyUpdDt = weeklyUpdDt;
	}
}
