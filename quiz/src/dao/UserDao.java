package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import connection.ConnectionGetter;
import entity.Question;
import entity.User;

public class UserDao {
	//ユーザー登録
	private static final String USERINSERT = "insert into users(id,password,name) values(?,?,?)";
	//ログイン時ユーザー取得
	private static final String USERSELECTBYID = "select * from users where id = ?";
	//ログインユーザー週間ランキング(順位)取得
	private static final String WEEKLYRANKSELECT =
			"select count(distinct weeklymaxscore) from users where weeklymaxscore >= ? ";
	//ログインユーザー日間ランキング(順位)取得
	private static final String DAILYRANKSELECT =
			"select count(distinct dailymaxscore) from users where dailymaxscore >= ? ";
	//週間ランキングユーザー取得
	private static final String WEEKLYSELECT = "select * from users where weeklyupddt is not null";
	//日間ランキングユーザー取得
	private static final String DAILYSELECT = "select * from users where to_char(upddt,'YYYY-MM-DD') = ?";
	// 本日日付のユーザー最大スコア取得(本日の記録なし→0カラム)
	private static final String DAILYMAXSELECT = "select dailymaxscore "
	                                            +"from users where (to_char(upddt,'YYYY-MM-DD') = ?) and id = ?";
	// 本日日付＋特定曜日のスコア更新処理
	private static final String MONDAILYANDDATEUPDATE = "update users set dailymaxscore = ?,"
	                                                   +"monscore = ?,mondt = now(),upddt = now() where id = ?";
	private static final String TUEDAILYANDDATEUPDATE = "update users set dailymaxscore = ?,"
	                                                   +"tuescore = ?,tuedt = now(),upddt = now() where id = ?";
	private static final String WEDDAILYANDDATEUPDATE = "update users set dailymaxscore = ?,"
	                                                   +"wedscore = ?,weddt = now(),upddt = now() where id = ?";
	private static final String THUDAILYANDDATEUPDATE =	"update users set dailymaxscore = ?,"
	                                                   +"thuscore = ?,thudt = now(),upddt = now() where id = ?";
	private static final String FRIDAILYANDDATEUPDATE = "update users set dailymaxscore = ?,"
	                                                   +"friscore = ?,fridt = now(),upddt = now() where id = ?";
	private static final String SURDAILYANDDATEUPDATE = "update users set dailymaxscore = ?,"
	                                                   +"surscore = ?,surdt = now(),upddt = now() where id = ?";
	private static final String SUNDAILYANDDATEUPDATE = "update users set dailymaxscore = ?,"
                                                       +"sunscore = ?,sundt = now(),upddt = now() where id = ?";
	// 週間最大スコア、週間スコア更新日付更新処理
	private static final String WEEKLYMAXUPDATE = "update users set weeklymaxscore = ?,weeklyupddt = now() where id = ?";
	// 週間最大スコア、週間スコア更新日付削除処理
	private static final String DELETEWEEKLYDATA = "update users set weeklyupddt = null,weeklymaxscore = null where id = ?";
	// 週間スコア、更新日付更新
	private static final String UPDATEWEEKLYDATA = "update users set weeklyupddt = ?,weeklymaxscore = ? where id = ?";
	//ユーザー登録処理
	public int userInsert(String id,String password,String name) {
		try(Connection con = ConnectionGetter.getConnection();
			PreparedStatement ps = con.prepareStatement(USERINSERT)){
			//結果用変数
			int result = 0;
			ps.setString(1, id);
			ps.setString(2, password);
			ps.setString(3, name);
			//インサートした結果(期待される結果==1)をセット
			result = ps.executeUpdate();
			return result;
		}catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	//ログイン時ユーザー取得メソッド
	public User getUser(String id,String password) {
		try(Connection con = ConnectionGetter.getConnection();
			PreparedStatement ps = con.prepareStatement(USERSELECTBYID);
			PreparedStatement ps2 = con.prepareStatement(DELETEWEEKLYDATA);
			PreparedStatement ps3 = con.prepareStatement(UPDATEWEEKLYDATA)){
			//返すUserオブジェクト
			User user = null;
			ps.setString(1, id);
			//idが一致していれば抽出
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				//パスワードまで一致していればエンティティとしてNewする
				if(rs.getString("password").equals(password)) {
					user = new User(rs.getString("id")
								   ,rs.getString("password")
								   ,rs.getString("name")
								   ,rs.getInt("weeklymaxscore")
								   ,rs.getInt("dailymaxscore")
								   ,rs.getInt("monscore")
								   ,rs.getTimestamp("mondt")
								   ,rs.getInt("tuescore")
								   ,rs.getTimestamp("tuedt")
								   ,rs.getInt("wedscore")
								   ,rs.getTimestamp("weddt")
								   ,rs.getInt("thuscore")
								   ,rs.getTimestamp("thudt")
								   ,rs.getInt("friscore")
								   ,rs.getTimestamp("fridt")
								   ,rs.getInt("surscore")
								   ,rs.getTimestamp("surdt")
								   ,rs.getInt("sunscore")
								   ,rs.getTimestamp("sundt")
								   ,rs.getTimestamp("upddt")
							       ,rs.getTimestamp("weeklyupddt"));
				}else {
					// パスワード不一致
					return null;
				}
				// 週間最大スコア集計し直し
				LocalDate today = LocalDate.now();
				// 1週間以内にクイズ実施、またはクイズ実施後1週間以上のログインなし
				if(rs.getTimestamp("weeklyupddt") != null) {
					LocalDate weeklyUpddt =
							LocalDate.from(rs.getTimestamp("weeklyupddt").toLocalDateTime());
					// 週間最大スコアの記録日付が1週間以内ではない場合、セットし直し
					if(weeklyUpddt.isBefore(LocalDate.now().minusDays(6))) {
						//それぞれの週のスコア更新日時が1週間以内でない場合、スコアを-1に設定する処理
						LocalDate minus6Date = LocalDate.now().minusDays(6);
						LocalDate tmpDate = null;
						// それぞれの曜日の更新日がnull、又は1週間以上前 → スコアを-1にする
						if(user.getMonDt() != null) {
							tmpDate = LocalDate.from(user.getMonDt().toLocalDateTime());
							if(tmpDate.isBefore(minus6Date)){
								user.setMonScore(-1);
							}
						}else {
							user.setMonScore(-1);
							user.setMonDt(new Timestamp(System.currentTimeMillis()));
						}
						if(user.getTueDt() != null) {
							tmpDate = LocalDate.from(user.getTueDt().toLocalDateTime());
							if(tmpDate.isBefore(minus6Date)){
								user.setTueScore(-1);
							}
						}else {
							user.setTueScore(-1);
							user.setTueDt(new Timestamp(System.currentTimeMillis()));
						}
						if(user.getWedDt() != null) {
							tmpDate = LocalDate.from(user.getWedDt().toLocalDateTime());
							if(tmpDate.isBefore(minus6Date)){
								user.setWedScore(-1);
							}
						}else {
							user.setWedScore(-1);
							user.setWedDt(new Timestamp(System.currentTimeMillis()));
						}
						if(user.getThuDt() != null) {
							tmpDate = LocalDate.from(user.getThuDt().toLocalDateTime());
							if(tmpDate.isBefore(minus6Date)){
							user.setThuScore(-1);
							}
						}else {
							user.setThuScore(-1);
							user.setThuDt(new Timestamp(System.currentTimeMillis()));
						}
						if(user.getFriDt() != null) {
							tmpDate = LocalDate.from(user.getFriDt().toLocalDateTime());
							if(tmpDate.isBefore(minus6Date)){
								user.setFriScore(-1);
							}
						}else {
							user.setFriScore(-1);
							user.setFriDt(new Timestamp(System.currentTimeMillis()));
						}
						if(user.getSurDt() != null) {
							tmpDate = LocalDate.from(user.getSurDt().toLocalDateTime());
							if(tmpDate.isBefore(minus6Date)){
								user.setSurScore(-1);
							}
						}else {
							user.setSurScore(-1);
							user.setSurDt(new Timestamp(System.currentTimeMillis()));
						}
						if(user.getSunDt() != null) {
							tmpDate = LocalDate.from(user.getSunDt().toLocalDateTime());
							if(tmpDate.isBefore(minus6Date)){
								user.setSunScore(-1);
							}
						}else {
							user.setSunScore(-1);
							user.setSunDt(new Timestamp(System.currentTimeMillis()));
						}
						// ユーザーの週間最大スコア取得
						int weeklyMaxScore = IntStream.of(user.getMonScore(),user.getTueScore(),
													  user.getWedScore(),user.getThuScore(),
													  user.getFriScore(),user.getSurScore(),
													  user.getSunScore())
										 .max()
										 .getAsInt();
						/*
						 * weeklyMaxScore == -1 → 1週間以上テスト未実施かつログインしていない、
						 * もしくは該当する曜日の採点が初めて、のいずれか
						 * その場合は週間ランキング情報はN/Aにする
						 */
						if(weeklyMaxScore == -1) {
							// weeklyUpddt、weeklymaxscoreをnullにする処理(このメソッド内でこのカラムをチェックするため)
							ps2.setString(1, id);
							ps2.executeUpdate();
							// オブジェクトにも格納(N/Aと表示するため)
							user.setWeeklyMaxScore(null);
						}else {
							/*
							 *  weeklyMaxScore != -1 → 1週間以内に記録したスコアあり
							 *  → 1週間以内の最大スコアかつ、最新日付のマッピング１組を取得
							 */
							Map<Integer,Timestamp> map = new HashMap<>();
							map.put(user.getMonScore(), user.getMonDt());
							map.put(user.getTueScore(), user.getTueDt());
							map.put(user.getWedScore(), user.getWedDt());
							map.put(user.getThuScore(), user.getThuDt());
							map.put(user.getFriScore(), user.getFriDt());
							map.put(user.getSurScore(), user.getSurDt());
							map.put(user.getSunScore(), user.getSunDt());
							Map.Entry<Integer, Timestamp> entry;
							entry = map.entrySet().stream()
												   .sorted(Map.Entry.<Integer,Timestamp>comparingByKey()
														   .thenComparing(Map.Entry.comparingByValue()))
												   .collect(Collectors.toList())
												   .get(6);
							/*
							 *  このリストのインデックス6(最新日付)をweeklyupddt、
							 *  スコアをweeklymaxscoreとして格納する
							 */
							ps3.setTimestamp(1, entry.getValue());
							ps3.setInt(2, weeklyMaxScore);
							ps3.setString(3, id);
							ps3.executeUpdate();
							// オブジェクトに値を格納(週間のみ)
							user.setWeeklyMaxScore(rs.getInt("weeklymaxscore"));
						}
					}else {
						// そのままオブジェクトに日付、週間の値をそのまま格納する
						user.setDailyMaxScore(rs.getInt("dailymaxscore"));
						user.setWeeklyMaxScore(rs.getInt("weeklymaxscore"));
					}
					// 過去にログインしクイズを行っているが、本日ではない
					if(LocalDate.from(rs.getTimestamp("upddt").toLocalDateTime()).isBefore(today)) {
						user.setDailyMaxScore(null);
					}
				}else {
					// 全てN/Aにする
					user.setDailyMaxScore(null);
					user.setWeeklyMaxScore(null);
				}
				return user;
			}else {
				// id不一致
				return null;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	//ログイン時ユーザー週間ランキング取得メソッド
	public int getUserWeeklyRank(int weeklyScore) {
		try(Connection con = ConnectionGetter.getConnection();
			PreparedStatement ps = con.prepareStatement(WEEKLYRANKSELECT)){
			//返すint型変数
			int rank = -1;
			ps.setInt(1, weeklyScore);
			ResultSet rs = ps.executeQuery();

			if(rs.next()) {
				rank = rs.getInt(1);
			}
			return rank;
		}catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	//ログイン時ユーザー日間ランキング取得メソッド
	public int getUserDailyRank(int dailyScore) {
		try(Connection con = ConnectionGetter.getConnection();
			PreparedStatement ps = con.prepareStatement(DAILYRANKSELECT)){
			//返すint型変数
			int rank = -1;
			ps.setInt(1, dailyScore);
			ResultSet rs = ps.executeQuery();

			if(rs.next()) {
				rank = rs.getInt(1);
			}
			return rank;
		}catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	//週間ランキングユーザー取得
	public List<User> getWeeklyUser() {
		try(Connection con = ConnectionGetter.getConnection();
			PreparedStatement ps = con.prepareStatement(WEEKLYSELECT);
			PreparedStatement ps2 = con.prepareStatement(DELETEWEEKLYDATA);
			PreparedStatement ps3 = con.prepareStatement(UPDATEWEEKLYDATA)){
			// 返却用UserList
			List<User> uList = new ArrayList<>();
			// 格納用Userオブジェクト
			User user = null;
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				// 週間最大スコア格納用変数
				int weeklyMaxScore = -1;
					user = new User(rs.getString("id")
								   ,rs.getString("password")
								   ,rs.getString("name")
								   ,rs.getInt("weeklymaxscore")
								   ,rs.getInt("dailymaxscore")
								   ,rs.getInt("monscore")
								   ,rs.getTimestamp("mondt")
								   ,rs.getInt("tuescore")
								   ,rs.getTimestamp("tuedt")
								   ,rs.getInt("wedscore")
								   ,rs.getTimestamp("weddt")
								   ,rs.getInt("thuscore")
								   ,rs.getTimestamp("thudt")
								   ,rs.getInt("friscore")
								   ,rs.getTimestamp("fridt")
								   ,rs.getInt("surscore")
								   ,rs.getTimestamp("surdt")
								   ,rs.getInt("sunscore")
								   ,rs.getTimestamp("sundt")
								   ,rs.getTimestamp("upddt")
							       ,rs.getTimestamp("weeklyupddt"));

					LocalDate weeklyUpddt =
							LocalDate.from(rs.getTimestamp("weeklyupddt").toLocalDateTime());
					// 週間最大スコア記録日付更新し直し
					if(weeklyUpddt.isBefore(LocalDate.now().minusDays(6))){
						//それぞれの週のスコア更新日時が1週間以内でない場合、スコアを-1に設定する処理
						LocalDate minus6Date = LocalDate.now().minusDays(6);
						LocalDate tmpDate = null;
						if(user.getMonDt() != null) {
							tmpDate = LocalDate.from(user.getMonDt().toLocalDateTime());
							if(tmpDate.isBefore(minus6Date)){
								user.setMonScore(-1);
							}
						}else {
							user.setMonScore(-1);
							user.setMonDt(new Timestamp(System.currentTimeMillis()));
						}
						if(user.getTueDt() != null) {
							tmpDate = LocalDate.from(user.getTueDt().toLocalDateTime());
							if(tmpDate.isBefore(minus6Date)){
								user.setTueScore(-1);
							}
						}else {
							user.setTueScore(-1);
							user.setTueDt(new Timestamp(System.currentTimeMillis()));
						}
						if(user.getWedDt() != null) {
							tmpDate = LocalDate.from(user.getWedDt().toLocalDateTime());
							if(tmpDate.isBefore(minus6Date)){
								user.setWedScore(-1);
							}
						}else {
							user.setWedScore(-1);
							user.setWedDt(new Timestamp(System.currentTimeMillis()));
						}
						if(user.getThuDt() != null) {
							tmpDate = LocalDate.from(user.getThuDt().toLocalDateTime());
							if(tmpDate.isBefore(minus6Date)){
								user.setThuScore(-1);
							}
						}else {
							user.setThuScore(-1);
							user.setThuDt(new Timestamp(System.currentTimeMillis()));
						}
						if(user.getFriDt() != null) {
							tmpDate = LocalDate.from(user.getFriDt().toLocalDateTime());
							if(tmpDate.isBefore(minus6Date)){
								user.setFriScore(-1);
							}
						}else {
							user.setFriScore(-1);
							user.setFriDt(new Timestamp(System.currentTimeMillis()));
						}
						if(user.getSurDt() != null) {
							tmpDate = LocalDate.from(user.getSurDt().toLocalDateTime());
							if(tmpDate.isBefore(minus6Date)){
								user.setSurScore(-1);
							}
						}else {
							user.setSurScore(-1);
							user.setSurDt(new Timestamp(System.currentTimeMillis()));
						}
						if(user.getSunDt() != null) {
							tmpDate = LocalDate.from(user.getSunDt().toLocalDateTime());
							if(tmpDate.isBefore(minus6Date)){
								user.setSunScore(-1);
							}
						}else {
							user.setSunScore(-1);
							user.setSunDt(new Timestamp(System.currentTimeMillis()));
						}
						// ユーザーの週間最大スコア取得
						weeklyMaxScore = IntStream.of(user.getMonScore(),user.getTueScore(),
													  user.getWedScore(),user.getThuScore(),
													  user.getFriScore(),user.getSurScore(),
													  user.getSunScore())
										 .max()
										 .getAsInt();
						/*
						 * weeklyMaxScore == -1 → 1週間以上ログインしていない、
						 * もしくは該当する曜日の採点が初めて、のいずれか
						 * その場合はweeklyUpddt、weeklymaxscoreをnullにする
						 */
						if(weeklyMaxScore == -1) {
							// weeklyUpddt、weeklymaxscoreをnullにする処理
							ps2.setString(1, user.getId());
							ps2.executeUpdate();
							// ユーザーオブジェクトのデータを無効化
							user.setWeeklyMaxScore(null);
						}else {
							Map<Integer,Timestamp> map = new HashMap<>();
							map.put(user.getMonScore(), user.getMonDt());
							map.put(user.getTueScore(), user.getTueDt());
							map.put(user.getWedScore(), user.getWedDt());
							map.put(user.getThuScore(), user.getThuDt());
							map.put(user.getFriScore(), user.getFriDt());
							map.put(user.getSurScore(), user.getSurDt());
							map.put(user.getSunScore(), user.getSunDt());
							Map.Entry<Integer, Timestamp> entry;
							entry = map.entrySet().stream()
												   .sorted(Map.Entry.<Integer,Timestamp>comparingByKey()
														   .thenComparing(Map.Entry.comparingByValue()))
												   .collect(Collectors.toList())
												   .get(6);
							/*
							 *  このリストのインデックス6(最新日付)をweeklyupddt、
							 *  スコアをweeklymaxscoreとして格納する
							 */
							ps3.setTimestamp(1, entry.getValue());
							ps3.setInt(2, weeklyMaxScore);
							ps3.setString(3, user.getId());
							ps3.executeUpdate();
							// ユーザーオブジェクトのデータを格納
							user.setWeeklyMaxScore(weeklyMaxScore);
							user.setWeeklyUpdDt(entry.getValue());
							}
					}
					// 1週間以内のデータがない場合は除外
					if(user.getWeeklyMaxScore()==null) {
						continue;
					}
					uList.add(user);
			}
			// 順位用一時変数
			int tmpRank = 0;
			// 一時スコア変数
			int tmpScore = 11;
			// ユーザーリストをスコアの降順の後ユーザー名の昇順にする
			uList = uList.stream()
						  .sorted(Comparator.comparing(User::getWeeklyMaxScore)
								  .reversed()
								  .thenComparing(User::getUserName))
						  .collect(Collectors.toList());
			// 順位格納処理
			for(User u:uList) {
				if(tmpScore > u.getWeeklyMaxScore()) {
					tmpScore = u.getWeeklyMaxScore();
					u.setWeeklyRank(++tmpRank);
				}else {
					u.setWeeklyRank(tmpRank);
				}
			}
			return uList;
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	//日間ランキングユーザー取得
	public List<User> getDailyUsers() {
		try(Connection con = ConnectionGetter.getConnection();
			PreparedStatement ps = con.prepareStatement(DAILYSELECT)){
			//ランキング一覧用のリスト
			List<User> dailyUserList = new ArrayList<>();
			//リスト格納用のユーザーオブジェクト
			User user = null;
			//日間ランキング用現在日付取得
			String now = LocalDate.now().toString();
			ps.setString(1, now);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				user = new User(rs.getString("id")
						       ,rs.getString("password")
						       ,rs.getString("name")
						       ,rs.getInt("weeklymaxscore")
						       ,rs.getInt("dailymaxscore")
						       ,rs.getInt("monscore")
						       ,rs.getTimestamp("mondt")
						       ,rs.getInt("tuescore")
						       ,rs.getTimestamp("tuedt")
						       ,rs.getInt("wedscore")
						       ,rs.getTimestamp("weddt")
						       ,rs.getInt("thuscore")
						       ,rs.getTimestamp("thudt")
						       ,rs.getInt("friscore")
						       ,rs.getTimestamp("fridt")
						       ,rs.getInt("surscore")
						       ,rs.getTimestamp("surdt")
						       ,rs.getInt("sunscore")
						       ,rs.getTimestamp("sundt")
						       ,rs.getTimestamp("upddt")
						       ,rs.getTimestamp("weeklyupddt"));

				dailyUserList.add(user);
			}
			//日間のスコアの降順(後にユーザー名の昇順)にソートする処理
			Collections.sort(dailyUserList
					        ,Comparator.comparing(User::getDailyMaxScore)
					        .reversed()
					        .thenComparing(User::getUserName));
			//ランク抽出処理 tmpRankは0、1、2とカウントアップ
			//tmpScore(スコア)が下がるにつれ、tmpRank(順位)は増える
			int tmpRank = 0;
			int tmpScore = 11;

			for(User u:dailyUserList) {
				if(u.getDailyMaxScore() < tmpScore) {
					tmpScore = u.getDailyMaxScore();
					u.setDailyRank(++tmpRank);
				}else {
					u.setDailyRank(tmpRank);
				}
			}
			return dailyUserList;
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	//idでユーザー存在チェック
	public boolean UserExistsById(String id) {
		try(Connection con = ConnectionGetter.getConnection();
				PreparedStatement ps = con.prepareStatement(USERSELECTBYID)){
				//存在フラグ
				boolean isUserExist = false;
				ps.setString(1, id);
				//idが一致していれば1レコード抽出される
				ResultSet rs = ps.executeQuery();
				//抽出された場合、trueをセット
				if(rs.next()) {
					isUserExist = true;
				}
				return isUserExist;
			}catch(SQLException e) {
				e.printStackTrace();
				return false;
			}
	}
	// クイズ実施結果(スコア)取得メソッド
	public int getTotalScore(List<Question> qList,List<String> userChoiceList) {
		int score = 0;
		for(int i = 0;i < 10;i++) {
			// 問題の正解 == ユーザーの選択したオプションかを判定
			if(qList.get(i).getAnswer().equals(userChoiceList.get(i))) {
				score++;
			}
		}
		return score;
	}
	// クイズ実施後のスコアを引数に、ユーザーの記録更新処理
	public List<Integer> updateUserScore(int score,String userId) {
		try(Connection con = ConnectionGetter.getConnection();
			PreparedStatement ps1 = con.prepareStatement(DAILYMAXSELECT);
			PreparedStatement ps3 = con.prepareStatement(USERSELECTBYID);
			PreparedStatement ps4 = con.prepareStatement(WEEKLYMAXUPDATE);
			PreparedStatement ps5 = con.prepareStatement(DELETEWEEKLYDATA);
			PreparedStatement ps6 = con.prepareStatement(UPDATEWEEKLYDATA)){
				// リターン用List ※index0==更新した要素数 1==
			    List<Integer> rList = Arrays.asList(0,0,0);
			    // 本日日付'YYYY-MM-DD'のStringを用意(１つ目のプレースホルダ)
			    String today = LocalDate.now().toString();
				ps1.setString(1, today);
				// ２つ目のプレースホルダセット(userId)し、SQLを呼ぶ
				ps1.setString(2, userId);
				ResultSet rs = ps1.executeQuery();
				// 従来の本日最大スコア変数用
				int beforeDailyMax = -1;
				// レコードが返ってきたら、引数のスコアとレコードの本日最大スコアと比較
				if(rs.next()) {
					// クイズ後のスコア > レコードの本日最大スコア
					beforeDailyMax = rs.getInt(1);
				}
					if(score > beforeDailyMax) {
						rList.set(0, 1);
						rList.set(1,beforeDailyMax);
						// dailymaxscore,特定曜日のmaxscore更新処理
						// switchで本日の曜日を取得して、該当の曜日のスコアもupdate
						// 現在曜日格納用変数
						String date_today = null;
						Calendar cal = Calendar.getInstance();
						// 分岐処理のため、ここでPreparedStatement生成
						PreparedStatement ps2 = null;
						switch(cal.get(Calendar.DAY_OF_WEEK)) {
						case Calendar.SUNDAY:
							ps2 = con.prepareStatement(SUNDAILYANDDATEUPDATE);
							break;
						case Calendar.MONDAY:
							ps2 = con.prepareStatement(MONDAILYANDDATEUPDATE);
							break;
						case Calendar.TUESDAY:
							ps2 = con.prepareStatement(TUEDAILYANDDATEUPDATE);
							break;
						case Calendar.WEDNESDAY:
							ps2 = con.prepareStatement(WEDDAILYANDDATEUPDATE);
							break;
						case Calendar.THURSDAY:
							ps2 = con.prepareStatement(THUDAILYANDDATEUPDATE);
							break;
						case Calendar.FRIDAY:
							ps2 = con.prepareStatement(FRIDAILYANDDATEUPDATE);
							break;
						case Calendar.SATURDAY:
							ps2 = con.prepareStatement(SURDAILYANDDATEUPDATE);
							break;
						}
						// プレースホルダに値をセット
						ps2.setInt(1, score);
						ps2.setInt(2, score);
						ps2.setString(3, userId);
						ps2.executeUpdate();
						//if() score > 週間最大スコア　
						ps3.setString(1, userId);
						ResultSet rs2 = ps3.executeQuery();
						// 週間最大スコア取得用にユーザーオブジェクト生成
						User user = null;
						if(rs2.next()) {
							// 週間最大スコア格納用変数
							int weeklyMaxScore = -1;
							// 初回採点するユーザーではなく、1週間以内にログインしている場合
							if(rs2.getTimestamp("weeklyupddt")!=null) {
								LocalDate weeklyUpddt =
										LocalDate.from(rs2.getTimestamp("weeklyupddt").toLocalDateTime());
								// 週間最大スコア記録日付更新し直し
								if(weeklyUpddt.isBefore(LocalDate.now().minusDays(6))){
									user = new User(rs2.getString("id")
										       ,rs2.getString("password")
										       ,rs2.getString("name")
										       ,rs2.getInt("weeklymaxscore")
										       ,rs2.getInt("dailymaxscore")
										       ,rs2.getInt("monscore")
										       ,rs2.getTimestamp("mondt")
										       ,rs2.getInt("tuescore")
										       ,rs2.getTimestamp("tuedt")
										       ,rs2.getInt("wedscore")
										       ,rs2.getTimestamp("weddt")
										       ,rs2.getInt("thuscore")
										       ,rs2.getTimestamp("thudt")
										       ,rs2.getInt("friscore")
										       ,rs2.getTimestamp("fridt")
										       ,rs2.getInt("surscore")
										       ,rs2.getTimestamp("surdt")
										       ,rs2.getInt("sunscore")
										       ,rs2.getTimestamp("sundt")
										       ,rs.getTimestamp("upddt")
										       ,rs.getTimestamp("weeklyupddt"));
									//それぞれの週のスコア更新日時が1週間以内でない場合、スコアを-1に設定する処理
									LocalDate minus6Date = LocalDate.now().minusDays(6);
									LocalDate tmpDate = null;
									if(user.getMonDt() != null) {
										tmpDate = LocalDate.from(user.getMonDt().toLocalDateTime());
										if(tmpDate.isBefore(minus6Date)){
											user.setMonScore(-1);
										}
									}else {
										user.setMonScore(-1);
										user.setMonDt(new Timestamp(System.currentTimeMillis()));
									}
									if(user.getTueDt() != null) {
										tmpDate = LocalDate.from(user.getTueDt().toLocalDateTime());
										if(tmpDate.isBefore(minus6Date)){
											user.setTueScore(-1);
										}
									}else {
										user.setTueScore(-1);
										user.setTueDt(new Timestamp(System.currentTimeMillis()));
									}
									if(user.getWedDt() != null) {
										tmpDate = LocalDate.from(user.getWedDt().toLocalDateTime());
										if(tmpDate.isBefore(minus6Date)){
											user.setWedScore(-1);
										}
									}else {
										user.setWedScore(-1);
										user.setWedDt(new Timestamp(System.currentTimeMillis()));
									}
									if(user.getThuDt() != null) {
										tmpDate = LocalDate.from(user.getThuDt().toLocalDateTime());
										if(tmpDate.isBefore(minus6Date)){
											user.setThuScore(-1);
										}
									}else {
										user.setThuScore(-1);
										user.setThuDt(new Timestamp(System.currentTimeMillis()));
									}
									if(user.getFriDt() != null) {
										tmpDate = LocalDate.from(user.getFriDt().toLocalDateTime());
										if(tmpDate.isBefore(minus6Date)){
											user.setFriScore(-1);
										}
									}else {
										user.setFriScore(-1);
										user.setFriDt(new Timestamp(System.currentTimeMillis()));
									}
									if(user.getSurDt() != null) {
										tmpDate = LocalDate.from(user.getSurDt().toLocalDateTime());
										if(tmpDate.isBefore(minus6Date)){
											user.setSurScore(-1);
										}
									}else {
										user.setSurScore(-1);
										user.setSurDt(new Timestamp(System.currentTimeMillis()));
									}
									if(user.getSunDt() != null) {
										tmpDate = LocalDate.from(user.getSunDt().toLocalDateTime());
										if(tmpDate.isBefore(minus6Date)){
											user.setSunScore(-1);
										}
									}else {
										user.setSunScore(-1);
										user.setSunDt(new Timestamp(System.currentTimeMillis()));
									}
									// ユーザーの週間最大スコア取得
									weeklyMaxScore = IntStream.of(user.getMonScore(),user.getTueScore(),
																  user.getWedScore(),user.getThuScore(),
																  user.getFriScore(),user.getSurScore(),
																  user.getSunScore())
													 .max()
													 .getAsInt();
									/*
									 * weeklyMaxScore == -1 → 1週間以上ログインしていない、
									 * もしくは該当する曜日の採点が初めて、のいずれか
									 * その場合はweeklyUpddt、weeklymaxscoreをnullにする
									 */
									if(weeklyMaxScore == -1) {
										// weeklyUpddt、weeklymaxscoreをnullにする処理
										ps5.setString(1, userId);
										ps5.executeUpdate();
									}else {
										Map<Integer,Timestamp> map = new HashMap<>();
										map.put(user.getMonScore(), user.getMonDt());
										map.put(user.getTueScore(), user.getTueDt());
										map.put(user.getWedScore(), user.getWedDt());
										map.put(user.getThuScore(), user.getThuDt());
										map.put(user.getFriScore(), user.getFriDt());
										map.put(user.getSurScore(), user.getSurDt());
										map.put(user.getSunScore(), user.getSunDt());
										Map.Entry<Integer, Timestamp> entry;
										entry = map.entrySet().stream()
															   .sorted(Map.Entry.<Integer,Timestamp>comparingByKey()
																	   .thenComparing(Map.Entry.comparingByValue()))
															   .collect(Collectors.toList())
															   .get(6);
										/*
										 *  このリストのインデックス6(最新日付)をweeklyupddt、
										 *  スコアをweeklymaxscoreとして格納する
										 */
										ps6.setTimestamp(1, entry.getValue());
										ps6.setInt(2, weeklyMaxScore);
										ps6.setString(3, userId);
										ps6.executeUpdate();
									}
								}else {
									weeklyMaxScore = rs2.getInt("weeklymaxscore");
								}
							}
							// score > 週間最大スコア(scoreを考慮しない)の場合の処理
							if(score > weeklyMaxScore) {
								rList.set(0, 2);
								rList.set(2, weeklyMaxScore);
								// weeklymaxscore,weeklyupddtのDB更新処理
								ps4.setInt(1, score);
								ps4.setString(2,userId);
								int result = ps4.executeUpdate();
								if(result != 1) {
									throw new SQLException();
								}
							}
						}
					}
				return rList;
			}catch(SQLException e) {
				e.printStackTrace();
				return null;
			}
	}
	// パスワードハッシュ化メソッド
	public String passToHash(String plainPass) {
		return "tentative";
	}
}
