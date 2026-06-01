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
import util.SimpleMailSender;

/**
 * 登録処理を行う。
 */
@WebServlet("/todo/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//		response.setCharacterEncoding("UTF-8");
//		request.setCharacterEncoding("UTF-8");

		// リクエストパラメータを受け取り、DTOに格納する準備をする。
		int id = Integer.parseInt(request.getParameter("id"));
		String title = request.getParameter("title");
		String task = request.getParameter("task");
		String inputLimitdate = request.getParameter("limitdate");
		String userid = request.getParameter("userid");
		int status = Integer.parseInt(request.getParameter("status"));

		// DTOへ格納する。登録される期限(limit)はTodoクラスではinputLimitになる。
		Todo dto = new Todo();
		dto.setId(id);
		dto.setTitle(title);
		dto.setTask(task);
		dto.setInputLimitdate(inputLimitdate);
		dto.setUserid(userid);
		dto.setStatus(status);

		// 入力チェックを行う。
		boolean checkResult = dto.valueCheck();

		// もし入力チェックエラーがあった場合は、エラーメッセージを表示し、再入力させるため元の詳細画面へ戻る
		if (! checkResult ) {
			request.setAttribute("errorMessages", dto.getErrorMessages());
			// タスク１件のvoをリクエスト属性へバインド
			request.setAttribute("dto", dto);

			// 詳細画面を表示する
			RequestDispatcher rd = request.getRequestDispatcher("/detail.jsp");
			rd.forward(request, response);
			return;
		}

		String message = "";
		try(TodoDAO dao = new TodoDAO()) {
			// 更新または登録処理を行う
			// idが0のときは新規登録、id>=1のときは更新
			if (id == 0) {
				dao.registerInsert(dto);
				message = "タスクの新規登録処理が完了しました。";
			} else {
				dao.registerUpdate(dto);
				message = "タスク[ " + id + " ]の更新処理が完了しました。";
			}
			setMessage(request, message);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		String toAddr = "webmaster@marbleanser.com";
		String fromAddr = "webmaster@marbleanser.com";
		String personName = "Mail Test User";
		String subject = "管理アプリケーションからの報告です";
		// 完了時にメールを送信する
		SimpleMailSender mail = new SimpleMailSender();
		try {
			mail.sendMessage(toAddr, fromAddr, personName, subject, message);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 登録完了→一覧画面を表示する
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
