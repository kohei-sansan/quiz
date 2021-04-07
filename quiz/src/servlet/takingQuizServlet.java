package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDao;
import entity.Question;
import entity.User;

/**
 * Servlet implementation class takingQuizServlet
 */
@WebServlet("/takingQuizServlet")
public class takingQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public takingQuizServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 文字化け対策
		response.setCharacterEncoding("UTF-8");
		// クイズ実施中のサーブレット
		// セッション取得
		HttpSession session = request.getSession();
		// 問題カウンタ取得
		int qCount = (int)session.getAttribute("quizCount");
		// ユーザーの回答リスト取得
		List<String> userChoiceList = (List<String>)session.getAttribute("userChoiceList");
		// 問題リスト取得
		List<Question> qList = (List<Question>)session.getAttribute("currentQList");
		// 押下されたボタンのパラメータ取得
		String btn = request.getParameter("btn");
		// 前ページのユーザーの回答保管用変数　
		String userChoice = null;
		// userDao生成
		UserDao uDao = new UserDao();
		if(btn.equals("back")||btn.equals("next")) {
			// 問題カウンタを元に、それぞれの条件でユーザーが選択したオプションにチェックをつけるための処理
			if(btn.equals("back")){
				// カウンタを指定のインデックスにし、ユーザーの回答を"userChoice"にセット
				qCount--;
				// カウンタ再セット
				session.setAttribute("quizCount", qCount);
				session.setAttribute("userChoice", userChoiceList.get(qCount));
				// 現在の問題をセッションに保存
				session.setAttribute("currentQuestion", qList.get(qCount));
				// takeQuiz.jspに遷移
				RequestDispatcher dispatchBack = request.getRequestDispatcher("takeQuiz.jsp");
				dispatchBack.forward(request, response);
			}
			if(btn.equals("next")){
				// 前ページのユーザーの回答を取得して、userChoiceListに格納
				userChoice = request.getParameter("option");
				userChoiceList.set(qCount, userChoice);
				// ユーザーの回答リスト更新
				session.setAttribute("userChoiceList", userChoiceList);
				qCount++;
				// カウンタ再セット
				session.setAttribute("quizCount", qCount);
				session.setAttribute("userChoice", userChoiceList.get(qCount));
				// 次ページ用の問題をセッションに保存
				session.setAttribute("currentQuestion", qList.get(qCount));
				// takeQuiz.jspに遷移
				RequestDispatcher dispatchNext = request.getRequestDispatcher("takeQuiz.jsp");
				dispatchNext.forward(request, response);
			}
		}
		if(btn.equals("finish")||btn.equals("quit")) {
			if(btn.equals("finish")) {
				// 前ページのユーザーの回答を取得し、ユーザー回答リストに格納
				userChoice = request.getParameter("option");
				userChoiceList.set(qCount, userChoice);
				// 問題リスト、ユーザー回答リストを引数にとる採点メソッドを呼ぶ
				int score = uDao.getTotalScore(qList,userChoiceList);
				// 導き出されたスコアを元に、DB更新メソッドを呼ぶ
				List<Integer> resultList =
						uDao.updateUserScore(score,((User)(session.getAttribute("currentUser"))).getId());
				// セッションに保存されたユーザーのフィールド更新処理
				User currentUser = (User)session.getAttribute("currentUser");
				// 本日スコアのみ更新時
				if(resultList.get(0).equals(1)) {
					request.setAttribute("dailyUpdated", true);
					List<User> dailyUsers = uDao.getDailyUsers();
					for(User u:dailyUsers) {
						/*
						 *  ログインユーザーと同じレコードが見つかったらオブジェクトに値を格納し、
						 *  セッションに再度保存(ホーム画面での表示更新のため)
						 */
						if(u.getId().equals(currentUser.getId())) {
							currentUser.setDailyRank(u.getDailyRank());
							currentUser.setDailyMaxScore(u.getDailyMaxScore());
							session.setAttribute("currentUser", currentUser);
							break;
						}
					}
				}
				// 週間スコアも更新時
				if(resultList.get(0).equals(2)) {
					request.setAttribute("dailyUpdated", true);
					request.setAttribute("weeklyUpdated", true);
					List<User> weeklyUsers = uDao.getWeeklyUser();
					for(User u:weeklyUsers) {
						/*
						 *  ログインユーザーと同じレコードが見つかったらオブジェクトに値を格納し、
						 *  セッションに再度保存(ホーム画面での表示更新のため)
						 */
						if(u.getId().equals(currentUser.getId())) {
							currentUser.setWeeklyRank(u.getWeeklyRank());
							currentUser.setWeeklyMaxScore(u.getWeeklyMaxScore());
							break;
						}
					}
					List<User> dailyUsers = uDao.getDailyUsers();
					for(User u:dailyUsers) {
						/*
						 *  ログインユーザーと同じレコードが見つかったらオブジェクトに値を格納し、
						 *  セッションに再度保存(ホーム画面での表示更新のため)
						 */
						if(u.getId().equals(currentUser.getId())) {
							currentUser.setDailyRank(u.getDailyRank());
							currentUser.setDailyMaxScore(u.getDailyMaxScore());
							session.setAttribute("currentUser", currentUser);
							break;
						}
					}
				}
				// クイズ後のスコアをリクエストスコープに保存
				request.setAttribute("resultScore", score);
				// quizResult.jspに遷移　
				RequestDispatcher dispatchResult = request.getRequestDispatcher("quizResult.jsp");
				dispatchResult.forward(request, response);
			}
			if(btn.equals("quit")) {
				// 問題リスト、ユーザー回答リストを引数にとる採点メソッドを呼ぶ
				int quitScore = uDao.getTotalScore(qList,userChoiceList);
				request.setAttribute("resultScore", quitScore);
				// クイズのセッション情報をnullにする
				session.setAttribute("quizCount", null);
				session.setAttribute("userChoiceList", null);
				session.setAttribute("currentQList", null);
				session.setAttribute("quizQuit", true);
				// quizResult.jspに遷移　
				RequestDispatcher dispatchResult2 = request.getRequestDispatcher("quizResult.jsp");
				dispatchResult2.forward(request, response);
			}
		}
	}

}
