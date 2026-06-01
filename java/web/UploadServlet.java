package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dao.TodoDAO;
import dto.Todo;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet( urlPatterns={"/upload"})
@MultipartConfig( location="C:/upload/")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        // <INPUT type="file" name="uploadfile">からMultipart形式のアップロードコンテンツの内容を取得
        Part part = request.getPart("uploadfile");

        // アップロードされたコンテンツ(Part)からファイル名部分を示す部分を解析し、取得する。
        String filename = null;
        for (String cd : part.getHeader("Content-Disposition").split(";")) {
            cd = cd.trim();
            log(cd);

            if (cd.startsWith("filename")) {
                // ファイル名は=の右側以降の文字列。
                // ただし利用環境によってはダブルコーテーションが含まれているので、取り除く。
                filename = cd.substring(cd.indexOf("=") + 1).trim().replace("\"", "");
                log("upload file:" + filename);
                break;
            }
        }

        // リクエストパラメータのidを取得する。
        String idStr = request.getParameter("id");
        log("idStr:" + idStr);
        int id = Integer.parseInt(idStr);

        // アップロードしたファイルを書きだす
        String message = null;
        if ( filename != null ) {
            log(">> file write start.");

            // アップロードされたファイル名は、OS依存のファイルパスなどを含んでいるので置換する。
            // \は/に置換し、その後ファイル名のみ抽出する。
            filename = filename.replace("\\", "/");

            int pos = filename.lastIndexOf("/");
            if ( pos >= 0 ) {
                filename = filename.substring(pos+1);
            }
            log("filename : " + filename);
            part.write(filename);

            log("complete!");

            // アップロードが完了した後はデータベースに登録する
            // 保存するのはファイル名のみ。完全パスは含まない。
            Todo dto = new Todo();
            dto.setId(id);
            dto.setFilename(filename);

            try(TodoDAO dao = new TodoDAO()){
                int result = dao.updateUploadInfo(dto);
                log("アップロード登録結果:" + result);
            } catch (Exception e) {
                throw new ServletException(e);
            }

            message = "[ " + filename + " ]のアップロードが完了しました";
        } else {
            log("upload filename is blank.");
            message = "アップロードが失敗しました";
        }

        request.setAttribute("message", message);

        request.getRequestDispatcher("/detail?id=" + id).forward(request, response);
        //request.getRequestDispatcher("/complete.jsp").forward(request, response);
	}
}
