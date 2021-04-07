package entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {
	/*
	 * 問題をオブジェクトとして扱う
	 * word==対象の英単語
	 * option1〜option3は不正解として用意
	 * シャッフルして出題するため、別途選択肢のリストをフィールドとして用意
	 */
	private String userId;
	private String word;
	private String answer;
	private String option1;
	private String option2;
	private String option3;
	private Timestamp createDate;

	/*
	 * 選択肢のリスト（出題用にのみ使用）
	 * 選択肢をコンストラクタで受け取って格納し、シャッフルした状態で
	 * オブジェクトを生成する
	 */
	List<String> Options;

	// ユーザー投稿問題抽出用コンストラクタ(問題シャッフルなし)
	public Question(String userId,String word
				   ,String answer,String option1
				   ,String option2,String option3
				   ,Timestamp createDate) {
		this.setUserId(userId);
		this.setWord(word);
		this.setAnswer(answer);
		this.setOption1(option1);
		this.setOption2(option2);
		this.setOption3(option3);
		this.setCreateDate(createDate);

	}
	public Question(String userId,String word
			   ,String answer,String option1
			   ,String option2,String option3) {
	this.setUserId(userId);
	this.setWord(word);
	this.setAnswer(answer);
	this.setOption1(option1);
	this.setOption2(option2);
	this.setOption3(option3);
	this.setCreateDate(createDate);

	//出題用選択肢リスト格納&シャッフル
	Options = new ArrayList<>();
	this.Options.add(answer);
	this.Options.add(option1);
	this.Options.add(option2);
	this.Options.add(option3);
	Collections.shuffle(Options);
}
	//private Timestamp createDate;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getOption1() {
		return option1;
	}
	public void setOption1(String option1) {
		this.option1 = option1;
	}
	public String getOption2() {
		return option2;
	}
	public void setOption2(String option2) {
		this.option2 = option2;
	}
	public String getOption3() {
		return option3;
	}
	public void setOption3(String option3) {
		this.option3 = option3;
	}
	public List<String> getOptions(){
		return this.Options;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
}
