package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import connection.ConnectionGetter;
import entity.User;
import entity.Question;

public class QuestionsDao {
	private static String SELECT10QUESTIONS = "select * from questions order by random() limit 10";
	private static String SELECTUSERQUESTIONS = "select * from questions where userid = ?";
	private static String INSERTQUESTION = "insert into questions values(?,?,?,?,?,?,?)";
	private static String QUESTIONEXISTS = "select * from questions where word = ?";
	private static String SELECTUSERQUESTION = "select * from questions where userid = ?";

	//出題用問題10個抽出メソッド
	public List<Question> get10Questions() {
		try(Connection con = ConnectionGetter.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT10QUESTIONS)){
			//リターン用Question格納リスト
			List<Question> qList = new ArrayList<>();
			//格納するQuestionオブジェクト
			Question question  = null;
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				question = new Question(rs.getString("userId"),
										rs.getString("word"),
										rs.getString("answer"),
										rs.getString("option1"),
										rs.getString("option2"),
										rs.getString("option3"));
				qList.add(question);
			}
			return qList;
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	//ユーザー投稿問題抽出メソッド
	public List<Question> getUserQuestions(String userId) {
		try(Connection con = ConnectionGetter.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECTUSERQUESTIONS)){
			//リターン用Question格納リスト
			List<Question> qList = new ArrayList<>();
			//格納するQuestionオブジェクト
			Question question  = null;
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				question = new Question(rs.getString("userId"),
										rs.getString("word"),
										rs.getString("answer"),
										rs.getString("option1"),
										rs.getString("option2"),
										rs.getString("option3"));
				qList.add(question);
			}
			return qList;
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	//問題登録処理
			public int registerQestion(String userId,String word,
									   String answer,String option1,
									   String option2,String option3,
									   Timestamp createDate) {
				try(Connection con = ConnectionGetter.getConnection();
					PreparedStatement ps = con.prepareStatement(INSERTQUESTION)){
					//リターン用変数
					int result = -1;
					ps.setString(1, userId);
					ps.setString(2, word);
					ps.setString(3, answer);
					ps.setString(4, option1);
					ps.setString(5, option2);
					ps.setString(6, option3);
					ps.setTimestamp(7, createDate);
					result = ps.executeUpdate();

					return result;
				}catch(SQLException e) {
					e.printStackTrace();
					return -1;
				}
			}
	//問題重複チェック
	public boolean questionExists(String word) {
				try(Connection con = ConnectionGetter.getConnection();
					PreparedStatement ps = con.prepareStatement(QUESTIONEXISTS)){
					ps.setString(1, word);
					ResultSet result = ps.executeQuery();

					if(result.next()) {
						return true;
					}else {
						return false;
					}
				}catch(SQLException e) {
					e.printStackTrace();
					// 設計として懸念が残るところ
					return true;
				}
			}
	//ユーザーの投稿した問題抽出処理
	public List<Question> userQuestionSelect(String userId) {
			try(Connection con = ConnectionGetter.getConnection();
				PreparedStatement ps = con.prepareStatement(SELECTUSERQUESTION)){
				// リターン用リスト生成
				List<Question> qList = new ArrayList<>();

				ps.setString(1, userId);
				ResultSet rs = ps.executeQuery();

				while(rs.next()) {
					qList.add(new Question(rs.getString("userid"),
										   rs.getString("word"),
										   rs.getString("answer"),
										   rs.getString("option1"),
										   rs.getString("option2"),
										   rs.getString("option3"),
										   rs.getTimestamp("createdate")));
				}

				return qList;
			}catch(SQLException e) {
				e.printStackTrace();
				return null;
			}
	}
}
