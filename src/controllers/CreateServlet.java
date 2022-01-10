package controllers;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import utils.DBUtil;

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
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getParameter("_token");
        
        //CSRF対策のチェック→_token に値がセットされていない場合やセッションIDと値が異なったりしたらデータの登録ができないようにしてる
        //ここのチェックがtrueにならない時は、意図しない不正なページ遷移によって /create へアクセスされた場合
        //悪意のあるネット利用者が勝手に投稿できないようにするための対策
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();
            em.getTransaction().begin();

            Message m = new Message();

            String title = request.getParameter("title");
            m.setTitle(title);

            String content = request.getParameter("content");
            m.setContent(content);

            //データの保存部分について
            //id はMySQLの auto_increment の採番に任せ、title と content はフォームから入力された内容をセット
            //created_at と updated_at は、下記のように記述することで現在日時の情報を持つ日付型のオブジェクトを取得できる
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            //現在日時の情報を持つ日付型オブジェクトを2つのカラムにセット
            m.setCreated_at(currentTime);
            m.setUpdated_at(currentTime);

            //必要な情報をセットした Message クラスのオブジェクトを persist メソッドを使ってデータベースにセーブ
            em.persist(m);
            //commit
            em.getTransaction().commit();
            //Chapter 15.3フラッシュメッセージをセッションスコープにセット
            request.getSession().setAttribute("flush", "登録が完了しました。");
            em.close();

            //データベースへの保存が完了したら、indexページへリダイレクト（遷移）させる
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }

}
