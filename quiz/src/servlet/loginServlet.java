package servlet;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDao;
import entity.User;
import util.MyUtil;

/**
 * Servlet implementation class loginServlet
 */
@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginServlet() {
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
		// ユーザーID、パスワードを格納する変数
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		// ユーザー格納用変数
		User user = null;
		//入力チェック
		if(MyUtil.isNullOrEmpty(userId)||MyUtil.isNullOrEmpty(password)) {
			// id入力チェック
			if(MyUtil.isNullOrEmpty(userId)) {
				request.setAttribute("isIdBlank",true);
			}
			// パスワード入力チェック
			if(MyUtil.isNullOrEmpty(password)) {
				request.setAttribute("isPassBlank",true);
			}
			// login.jsp にページ遷移
			RequestDispatcher dispatch = request.getRequestDispatcher("login.jsp");
			dispatch.forward(request, response);
		}
		// ユーザー存在チェック
		UserDao uDao = new UserDao();
		user = uDao.getUser(userId, password);
		//存在しない(id、パスワードのどちらかが間違っている)場合はログイン画面に戻る
		if(user==null) {
			//入力値不正解フラグを立てる
			request.setAttribute("notUserExist", true);
			// login.jsp にページ遷移
			RequestDispatcher dispatch = request.getRequestDispatcher("login.jsp");
			dispatch.forward(request, response);
		}
		// ランキング用にフィールドにデータセット　
		if(user.getWeeklyMaxScore() == null) {
			// フラグ立てる
			//request.setAttribute("blankWeeklyScore", true);
		}else {
			List<User> weeklyUsers = uDao.getWeeklyUser();
			for(User u:weeklyUsers) {
				if(u.getId().equals(userId)) {
					user.setWeeklyRank(u.getWeeklyRank());
					break;
				}
			}
		}
		if(user.getDailyMaxScore() == null) {
			// フラグ立てる
			//request.setAttribute("blankDailyScore", true);
		}else {
			List<User> dailyUsers = uDao.getDailyUsers();
			for(User u:dailyUsers) {
				if(u.getId().equals(userId)) {
					user.setDailyRank(u.getDailyRank());
					break;
				}
			}
		}
		// 存在する場合、セッション開始して、ホーム画面に遷移
		HttpSession session = request.getSession();
		session.setAttribute("currentUser", user);
		// home.jsp にページ遷移
		RequestDispatcher dispatch = request.getRequestDispatcher("home.jsp");
		dispatch.forward(request, response);
	}

}
