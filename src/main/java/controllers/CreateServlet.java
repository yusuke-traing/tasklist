package controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import utils.DBUtil;
import validators.TaskValidator;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getParameter("_token");

        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();
            em.getTransaction().begin();

            // インスタンスを生成
            Task task = new Task();

            // パラメータを取得し値をセット
            String content = request.getParameter("content");
            task.setContent(content);

            // 現在時刻を取得し値をセット
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            task.setCreated_at(currentTime);
            task.setUpdated_at(currentTime);

            // バリデーションを実行
            List<String> errors = TaskValidator.validate(task);

            if (errors.size() > 0) {
                // エラーがある場合
                // リクエストスコープに格納
                request.setAttribute("_token", _token);
                request.setAttribute("errors", errors);
                request.setAttribute("task", task);

                // viewを表示
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/new.jsp");
                rd.forward(request, response);

            } else {
                // エラーがない場合
                // DBをコミット
                em.persist(task);
                em.getTransaction().commit();
                em.close();

                // セッションスコープにメッセージを格納
                request.getSession().setAttribute("flush", "登録が完了しました。");

                // リダイレクト
                response.sendRedirect(request.getContextPath() + "/index");
            }
        }
    }
}
