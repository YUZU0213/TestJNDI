package web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.TodoDAO;
import dto.Todo;

/**
 * Servlet implementation class DownloadFileServlet
 */
@WebServlet("/download")
public class DownloadFileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        // ダウンロードするファイルの情報を取得する。
        int id = Integer.parseInt(request.getParameter("id"));

        Todo dto = null;
        try(TodoDAO dao = new TodoDAO()) {
        	dto = dao.detail(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }

        // ファイル名を取り出す
        String filename = dto.getFilename();

        // もしファイルが登録されてない(取得出来なかった)場合は、再度詳細画面へ戻る。
        if (filename == null || "".equals(filename)) {
            request.setAttribute("message", "ファイルは添付されていません");
            request.getRequestDispatcher("/todo/search?id=" + id).forward(
                    request, response);

            // ここで処理終了
            return;
        }

        // 添付ファイルの保存ディレクトリ→アップロードディレクトリ
        File downloadFile = new File("C:/upload/" + filename);
        FileInputStream fis = new FileInputStream(downloadFile);
        BufferedInputStream buf = new BufferedInputStream(fis);

        // 全角ファイル
        filename = URLEncoder.encode(filename, "utf-8");

        response.setContentType("application/octet-stream; charset=\"utf-8\"");
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + filename + "\"");

        // サーブレットの出力（レスポンス）を取得する。
        ServletOutputStream out = response.getOutputStream();

        int length = 0;
        byte[] buffer = new byte[1024];
        while ((length = buf.read(buffer)) >= 0) {
            out.write(buffer, 0, length);
        }
        buf.close();
        out.close();
        out.flush();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    		if (!"GET".equals(request.getMethod())) {
				response.sendRedirect("login.jsp");
			}
    }

}
