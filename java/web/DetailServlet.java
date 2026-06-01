package web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.TodoDAO;
import dto.Todo;

/**
 * タスク詳細画面を表示するサーブレット。
 */
@WebServlet("/detail")
public class DetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// リクエストパラメータから選択したタスクidを取得する
		String paramId = request.getParameter("id");

		// Stringからintへ変換し、daoで処理を行う。更新対象のタスクを１件取得する。
		Todo dto;
		try (TodoDAO dao = new TodoDAO()){
			// intへ変換※NumberFormatExceptionが発生する可能性あり。チェック対象。
			int id = Integer.parseInt(paramId);

			// タスク詳細結果を取得
			dto = dao.detail(id);
		} catch (Exception e) {
			throw new ServletException(e);
		}

		// タスク１件のvoをリクエスト属性へバインド
		request.setAttribute("dto", dto);

		// 画面を返す
		// 検索一覧を表示する
		RequestDispatcher rd = request.getRequestDispatcher("/detail.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
