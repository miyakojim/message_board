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

import models.Message;
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
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        EntityManager em = DBUtil.createEntityManager();

        // 開くページ数を取得（デフォルトは1ページ目）
        int page = 1;
        
        //数値ではないものを Integer.parseInt に渡すと NumberFormatException という例外が表示される性質を使って、
        //try と catch で囲って処理が止まらないようにしている
        try {
            //request.getParameter() で取得できるのはString型だからInteger.parseInt() で文字列から数値に変更
            page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) {}

        // 最大件数と開始位置を指定してメッセージを取得
        List<Message> messages = em.createNamedQuery("getAllMessages", Message.class)
                                               //何件目からデータを取得するか（配列と同じ0番目から数える）を設定
                                                   .setFirstResult(15 * (page - 1))
                                               //「データの最大取得件数（今回は15件で固定）」を設定
                                                   .setMaxResults(15)
                                               //getAllMessages は複数のデータが結果として戻ってくる可能性があるため
                                               //getResultList() で問い合わせ結果を取得
                                                   .getResultList();

        // 全件数を取得
        //getMessagesCount は全件数という1つの結果のみが戻ってくる
        long messages_count = (long)em.createNamedQuery("getMessagesCount", Long.class)
                                                        // “1件だけ取得する” という命令を指定
                                                        .getSingleResult();

        em.close();

        //メッセージデータのリスト、全件数の数値、表示するページ数の数値も
        //リクエストスコープにセットして index.jsp に送る
        request.setAttribute("messages", messages);
        request.setAttribute("messages_count", messages_count);     // 全件数
        request.setAttribute("page", page);  
        
        /*
        //Chapter 15.3まで
        List<Message> messages = em.createNamedQuery("getAllMessages", Message.class).getResultList();

        em.close();

        request.setAttribute("messages", messages);
        */
        
        
        //Chapter 15.3フラッシュメッセージ
        //一度限りの表示とするため、IndexServlet でセッションスコープからリクエストスコープに移し替え、そのあとセッションスコープから除去
        // フラッシュメッセージがセッションスコープにセットされていたら
        // リクエストスコープに保存する（セッションスコープからは削除）
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/messages/index.jsp");
        rd.forward(request, response);
        
        /*
        //Chapter8まで
        EntityManager em = DBUtil.createEntityManager();
        
        List<Message> messages = em.createNamedQuery("getAllMessages", Message.class).getResultList();
        //データの登録件数
        response.getWriter().append(Integer.valueOf(messages.size()).toString());
        
        em.close();
        */
    }

}
