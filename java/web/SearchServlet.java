package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.TodoDAO;
import dto.Todo;

/**
 * 検索機能。タスク一覧を取得し、一覧結果へフォワードする。
 */
@WebServlet( urlPatterns={"/search"})
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// 認証情報の取得処理を追加↓
		String userid = request.getRemoteUser();
		request.setAttribute("LoginUserId", userid);

		// adminロールを所有するユーザーであるかを判定する
		boolean isAdmin = request.isUserInRole("admin");
		request.setAttribute("isAdmin", isAdmin);

		// DAOの取得
		try(TodoDAO dao = new TodoDAO()) {
			// タスクのリストを一覧で取得し、リクエスト属性へ格納する
			List<Todo> list = dao.todoList();

			request.setAttribute("todoList", list);
		} catch (Exception e) {
			throw new ServletException(e);
		}

		// 検索一覧を表示する
		RequestDispatcher rd = request.getRequestDispatcher("/search.jsp");
		rd.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		doGet(request, response);
	}
}
