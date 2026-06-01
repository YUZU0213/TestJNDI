package web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.TodoDAO;

/**
 * 削除処理を行う。
 */
@WebServlet("/delete")
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// リクエストパラメータから選択したタスクidを取得する
		String paramId = request.getParameter("id");

		try(TodoDAO dao = new TodoDAO()) {
			// intへ変換※NumberFormatExceptionが発生する可能性あり。チェック対象。
			int id = Integer.parseInt(paramId);

			// Stringからintへ変換し、daoで処理を行う。対象のタスクを１件削除し、成功すると１が返される。
			int result = dao.delete(id);
			log("タスクを削除しました:"+result);
		} catch (Exception e) {
			throw new ServletException(e);
		}

		setMessage(request, "タスク[ " + paramId + " ]の削除処理が完了しました。");

		// 画面を返す
		// 完了画面を表示する
		RequestDispatcher rd = request.getRequestDispatcher("/todo/search");
		rd.forward(request, response);
	}

	/**
	 * JSPで表示するメッセージを設定する。
	 *
	 * @param request
	 *            サーブレットリクエスト
	 * @param message
	 *            メッセージ文字列
	 */
	protected void setMessage(HttpServletRequest request, String message) {
		request.setAttribute("message", message);
	}
}
