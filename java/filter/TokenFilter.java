package filter;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


/**
 * CSRF対策フィルタ。
 */
@WebFilter("/*")
public class TokenFilter implements Filter {

	private static final Logger log = Logger.getLogger(TokenFilter.class);

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest httpreq = (HttpServletRequest)request;

		if (!tokenCheck(httpreq)) {
			throw new ServletException("トークンチェック例外です。");
		}

		// トークンの生成
		createToken(httpreq);

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * トークンチェック
	 * @param request
	 * @return
	 */
	protected boolean tokenCheck(HttpServletRequest request) {

		String contentType = request.getContentType();
		log.info("content-type:" + contentType);
		if ( contentType != null && contentType.contains("multipart/form-data")) {
			return true;

		}

		// HTTPメソッドでPOSTのときのみチェックを行う。
		String method = request.getMethod();
		log.debug("HTTP METHOD:" + method);
		if (! "POST".equals(method)) return true;

		// リクエストパラメータからトークンを取得
		String reqToken = request.getParameter("token");
		if ( reqToken == null || reqToken.isEmpty()) {
			log.warn("token[request] is empty.");
			return false;
		}

		// セッションからトークンを取得
		HttpSession session = request.getSession(false);
		if ( session == null) {
			log.debug("session isn't available.");
			return false;
		}

		String sessionToken = (String)session.getAttribute("token");
		log.debug("token check:");
		log.debug("request:" + reqToken);
		log.debug("session:" + sessionToken);
		// 両方のトークンを比較

		return ( reqToken.equals(sessionToken));
	}

	private void createToken(HttpServletRequest request) {
		// フィルタのリクエストはHttpServletRequestクラスではないのでキャストする
		HttpSession session = request.getSession();
		String token = generateToken();
		log.debug("next token:" + token);
		session.setAttribute("token", token);
	}

	private String generateToken() {
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for ( int i=0;i<64;i++) {
			char c = (char)(r.nextInt(64) + 32);
			sb.append(c);
		}
		MessageDigest md= null;
        try{
            md= MessageDigest.getInstance("SHA-256");
        }
        catch(NoSuchAlgorithmException e){
            return sb.toString();
        }
        md.reset();
        md.update(sb.toString().getBytes());
        byte[] hash= md.digest();

        StringBuilder hex = new StringBuilder();
        for(int i= 0; i< hash.length; i++){
            hex.append(Integer.toHexString( (hash[i]>> 4) & 0xFF ) );
            hex.append(Integer.toHexString( hash[i] & 0xFF ) );
        }
        return hex.toString();
    }

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
