package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UploadDisplayServlet
 */
@WebServlet("/preUpload")
public class UploadDisplayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // リクエストパラメータからタスク番号を取得して、リクエスト属性へ格納
	    String idString = request.getParameter("id");

	    // 数値に変換する。もし変換できなかった場合はNumberFormatExceptionがスローされる
	    Integer id = Integer.parseInt(idString);

	    // リクエスト属性へ格納される。
	    request.setAttribute("id", id);

		request.getRequestDispatcher("/upload.jsp").forward(request, response);
	}
}
