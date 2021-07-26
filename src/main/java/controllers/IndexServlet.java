package controllers;

import java.io.IOException;
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

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // EntityManagerの生成
        EntityManager em = DBUtil.createEntityManager();

        //  開くページ数を取得（デフォルトは1ページ目）
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) {}

        // 最大件数と開始位置を指定してタスクを取得
        List<Task> tasks = em.createNamedQuery("getAllTasks", Task.class)
                             // 開始位置を指定（0から始まる数字）
                             .setFirstResult(10 * (page - 1))
                             // 取得件数を指定
                             .setMaxResults(10)
                             .getResultList();
        // 全件数を取得
        long tasks_count = (long)em.createNamedQuery("getTasksCount", Long.class)
                                         .getSingleResult();

        // EntityManagerの破棄
        em.close();

        // リクエストパラメータに取得したデータを格納
        request.setAttribute("tasks", tasks);
        request.setAttribute("page", page);
        request.setAttribute("tasks_count", tasks_count);

        // フラッシュメッセージがセッションスコープにセットされていたら
        // リクエストスコープに保存する（セッションスコープからは削除）
        if (request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        // viewへ遷移
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/index.jsp");
        rd.forward(request, response);

    }

}
