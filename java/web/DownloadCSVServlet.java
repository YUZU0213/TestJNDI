package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.TodoDAO;

/**
 * Servlet implementation class DownloadCSVServlet
 */
@WebServlet("/download/csv")
public class DownloadCSVServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String CSVData = null;
		// CSVデータの取得(DAOでString変換済)
		try(TodoDAO dao = new TodoDAO()) {
			CSVData = dao.csvList();
		} catch (Exception e) {
			throw new ServletException(e);
		}

		// 例外が発生してなければダウンロード処理を開始する。
		// ファイルのダウンロード
		response.setContentType("application/octet-stream; charset=\"utf-8\"");
		response.setHeader("Content-Disposition", "attachment; filename=\"todo.csv\"");

		// サーブレットの出力（レスポンス）を取得する。
		ServletOutputStream out = response.getOutputStream();
		out.write(CSVData.getBytes("utf-8"));
		out.close();
	}

}
