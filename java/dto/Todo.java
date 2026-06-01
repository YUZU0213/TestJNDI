package dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO検索結果１行単位のValueObject
 *
 */
public class Todo implements Serializable {
    /** TODOのID */
    private int id;

    /** TODOのタイトル */
    private String title;

    /** 詳細なタスクの名前 */
    private String task;

    /** タスクの期限 */
    private Timestamp limitdate;

    /** タスクの最終更新日時 */
    private Timestamp lastupdate;

    /** タスクのユーザID */
    private String userid;

    /** タスクのステータスコード */
    private int status;

    /** タスクのステータス表示 */
    private String label;

    /** 登録画面で入力された期限 */
    private String inputLimitdate;

    /** アップロードされて紐付けされたファイル名 */
    private String filename;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Timestamp getLimitdate() {
        return limitdate;
    }

    public void setLimitdate(Timestamp limitdate) {
        this.limitdate = limitdate;
    }

    public Timestamp getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(Timestamp lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInputLimitdate() {
        return inputLimitdate;
    }

    public void setInputLimitdate(String inputLimitdate) {
        this.inputLimitdate = inputLimitdate;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * CSV出力用文字列を取得する。
     *
     * @return CSV用VOの値の文字列化。
     */
    public String toCSVString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder builder = new StringBuilder();
        builder.append("'").append(id).append("',");
        builder.append("'").append(title).append("',");
        builder.append("'").append(task).append("',");
        builder.append("'").append(format.format(limitdate)).append("',");
        builder.append("'").append(format.format(lastupdate)).append("',");
        builder.append("'").append(userid).append("',");
        builder.append("'").append(status).append("'\r\n");

        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TodoValueObject [id=");
        builder.append(id);
        builder.append(", ");
        if (title != null) {
            builder.append("title=");
            builder.append(title);
            builder.append(", ");
        }
        if (task != null) {
            builder.append("task=");
            builder.append(task);
            builder.append(", ");
        }
        if (limitdate != null) {
            builder.append("limit=");
            builder.append(limitdate);
            builder.append(", ");
        }
        if (lastupdate != null) {
            builder.append("lastupdate=");
            builder.append(lastupdate);
            builder.append(", ");
        }
        if (userid != null) {
            builder.append("userid=");
            builder.append(userid);
            builder.append(", ");
        }
        builder.append("status=");
        builder.append(status);
        builder.append(", ");
        if (label != null) {
            builder.append("label=");
            builder.append(label);
            builder.append(", ");
        }
        if (inputLimitdate != null) {
            builder.append("inputLimitdate=");
            builder.append(inputLimitdate);
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * 入力チェックを行う。 もし入力チェックエラーがあった場合には自動的にエラーメッセージが追加される。
     */
    public boolean valueCheck() {
        // エラーメッセージの初期化
        errorMessages = new ArrayList<String>();

        // id
        if (id < 0) {
            errorMessages.add("不正な入力を検出しました");
        }

        // title
        if (title == null || title.isEmpty()) {
            errorMessages.add("入力したタイトルが空です");
        } else if (title.length() > 256) {
            errorMessages.add("入力したタイトルが長すぎます");
        }

        // task
        if (task == null || task.isEmpty()) {
            errorMessages.add("入力したタスクが空です");
        } else if (title.length() > 512) {
            errorMessages.add("入力したタスクが長すぎます");
        }

        // limitdate
        if (inputLimitdate == null || inputLimitdate.isEmpty()) {
            errorMessages.add("入力したタスク期限が空です");
        } else if (!inputLimitdate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            errorMessages.add("入力したタスク期限のフォーマットが違います");
        }

        // userid
        if (userid == null || userid.isEmpty()) {
            errorMessages.add("入力したユーザーIDが空です");
        } else if (userid.length() > 64) {
            errorMessages.add("入力したユーザーIDが長すぎます");
        }

        // status
        if (status < 0 || status > 3) {
            errorMessages.add("入力したステータスの値が不正です");
        }

        return (errorMessages.size() == 0);
    }

    private List<String> errorMessages;

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
