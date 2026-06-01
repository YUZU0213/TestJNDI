package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import dto.Todo;

public class TodoDAO extends DAO {
	public List<Todo> todoList() throws Exception {
		List<Todo> returnList = new ArrayList<Todo>();

		String sql = "SELECT id, title, task, limitdate, lastupdate, userid, label, status, filename FROM todo_list";

//		String sql = "SELECT todo_list.ID, todo_list.title, todo_list.task, todo_list.limitdate, todo_list.lastupdate, todo_list.userid, status_list.label, todo_list.status todo_list.filename FROM status_list LEFT JOIN todo_list ON status_list.status = todo_list.status";

		// プリペアステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);

		// SQLを実行してその結果を取得する。
		ResultSet rs = statement.executeQuery();

		// 検索結果の行数分フェッチを行い、取得結果をDTOへ格納する
		while (rs.next()) {
			Todo dto = new Todo();

			// クエリー結果をDTOへ格納(あらかじめクエリー結果とDTOの変数名は一致させている)
			dto.setId(rs.getInt("id"));
			dto.setTitle(rs.getString("title"));
			dto.setTask(rs.getString("task"));
			dto.setLimitdate(rs.getTimestamp("limitdate"));
			dto.setLastupdate(rs.getTimestamp("lastupdate"));
			dto.setUserid(rs.getString("userid"));
			dto.setLabel(rs.getString("label"));
			dto.setFilename(rs.getString("filename"));

			returnList.add(dto);
		}

		return returnList;
	}

	/**
	 * 新規登録の画面を表示する。新規登録は空のDTOをJSPへ渡す。
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Todo insert() {

		Todo dto = new Todo();
		// 新規登録であることを判別するためid=0としている。
		dto.setId(0);

		return dto;
	}

	/**
	 * 表示するタスクの番号を指定して、タスク詳細を返す。
	 * @param id 表示対象のタスクID
	 * @return
	 * @throws Exception
	 */
	public Todo detail(int id) throws Exception {
		Todo dto = new Todo();

		String sql = "SELECT todo_list.id, todo_list.title, todo_list.task, todo_list.limitdate, todo_list.lastupdate, todo_list.userid, todo_list.label, todo_list.status, todo_list.filename "
				+ "FROM todo_list LEFT JOIN status_list status ON todo_list.status  where todo_list.id = ?";

		// プリペアステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);
		statement.setInt(1, id);

		// SQLを実行してその結果を取得する。
		ResultSet rs = statement.executeQuery();

		// 検索結果の行数分フェッチを行い、取得結果をDTOへ格納する
		while (rs.next()) {
			// クエリー結果をDTOへ格納(あらかじめクエリー結果とDTOの変数名は一致させている)
			dto.setId(rs.getInt("id"));
			dto.setTitle(rs.getString("title"));
			dto.setTask(rs.getString("task"));
			dto.setLimitdate(rs.getTimestamp("limitdate"));
			dto.setLastupdate(rs.getTimestamp("lastupdate"));
			dto.setUserid(rs.getString("userid"));
			dto.setLabel(rs.getString("label"));
			dto.setStatus(rs.getInt("status"));
			dto.setFilename(rs.getString("filename"));
		}
		return dto;
	}

	/**
	 * 削除処理を行う。指定されたidのタスクを削除する。
	 * @param id
	 * @return 削除件数
	 * @throws Exception
	 */
	public int delete(int id) throws Exception {
		String sql = "DELETE FROM todo_list where id = ?";

		// SQLを実行してその結果を取得する。
		int result = 0;
		try {
			// プリペアステートメントを取得し、実行SQLを渡す
			PreparedStatement statement = getPreparedStatement(sql);
			statement.setInt(1, id);

			result = statement.executeUpdate();

			// コミットを行う
			super.commit();
		} catch (Exception e) {
			super.rollback();
			throw e;
		}

		return result;
	}

	/**
	 * 新規登録処理を行う。
	 * タスクIDはAutoIncrementのキー項目なので、INSERT文のSQLに含めなくても自動的に最新のIDが登録される。
	 * @param dto 入力されたタスク内容。
	 * @return 追加された件数
	 * @throws Exception
	 */
	public int registerInsert(Todo dto) throws Exception {

		String sql = "INSERT INTO todo_list (title, task, limitdate, lastupdate, userid, todo_list.status) "
				   + "VALUES (?,?,?,now(),?,0)";

		int result = 0;
		// プリペアステートメントを取得し、実行SQLを渡す
		try {
			PreparedStatement statement = getPreparedStatement(sql);
			statement.setString(1, dto.getTitle());
			statement.setString(2, dto.getTask());
			statement.setString(3, dto.getInputLimitdate());
			statement.setString(4, dto.getUserid());

			result = statement.executeUpdate();
			System.out.println(result+"：件追加しました。");
			// コミットを行う
			super.commit();
		} catch (Exception e) {
			// ロールバックを行い、スローした例外はDAOから脱出する
			super.rollback();
			throw e;
		}

		return result;
	}

	/**
	 * 更新処理を行う。
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public int registerUpdate(Todo dto) throws Exception {
		String sql = "UPDATE todo_list SET title = ? , task = ? , limitdate = ? , lastupdate=now() , userid = ? , todo_list.status = ? WHERE id = ?";

		// プリペアステートメントを取得し、実行SQLを渡す
		int result =0;
		try {
			PreparedStatement statement = getPreparedStatement(sql);
			statement.setString(1, dto.getTitle());
			statement.setString(2, dto.getTask());
			statement.setString(3, dto.getInputLimitdate() );
			statement.setString(4, dto.getUserid());
			statement.setInt(5, dto.getStatus());
			statement.setInt(6, dto.getId());

			result = statement.executeUpdate();

			// コミットを行う
			super.commit();
		} catch (Exception e) {
			super.rollback();
			throw e;
		}

		return result;
	}

	/**
	 * CSV用の一覧Listを返す。
	 * @return CSV文字列
	 * @throws Exception
	 */
	public String csvList() throws Exception {
		List<Todo> returnList = new ArrayList<Todo>();

		String sql = "SELECT id , title , task , `limitdate` , lastupdate , userid , status FROM todo_list";

		// プリペアステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);

		// SQLを実行してその結果を取得する。
		ResultSet rs = statement.executeQuery();

		// 検索結果の行数分フェッチを行い、取得結果をDTOへ格納する
		while (rs.next()) {
			Todo dto = new Todo();

			// クエリー結果をDTOへ格納(あらかじめクエリー結果とDTOの変数名は一致させている)
			dto.setId(rs.getInt("id"));
			dto.setTitle(rs.getString("title"));
			dto.setTask(rs.getString("task"));
			dto.setLimitdate(rs.getTimestamp("limitdate"));
			dto.setLastupdate(rs.getTimestamp("lastupdate"));
			dto.setUserid(rs.getString("userid"));
			dto.setStatus(rs.getInt("status"));

			returnList.add(dto);
		}

		// CSV用の文字列に変換する。
		StringJoiner join = new StringJoiner("");
		returnList.stream().forEach(dto -> join.add(dto.toCSVString()));
		return join.toString();
	}

	/**
	 * タスクに添付されるアップロードされたファイル情報を更新する。
	 * @param vo
	 * @throws Exception
	 */
	public int updateUploadInfo(Todo dto) throws Exception {
	    String sql = "UPDATE todo_list SET filename = ? WHERE id = ?";
	    int result = 0;
        // プリペアステートメントを取得し、実行SQLを渡す
        try {
            PreparedStatement statement = getPreparedStatement(sql);
            statement.setString(1, dto.getFilename());
            statement.setInt(2, dto.getId());

            // 登録を行う
            result = statement.executeUpdate();

            // コミットを行う
            super.commit();
        } catch (Exception e) {
            super.rollback();
            throw e;
        }

        return result;
	}
}
