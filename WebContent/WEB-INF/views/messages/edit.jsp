<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <h2>id : ${message.id} のメッセージ編集ページ</h2>

        <form method="POST" action="${pageContext.request.contextPath}/update">
            <%-- _form.jsp で <input type="text" name="title" value="${message.title}" /> のように記述したおかげでデータベースに保存されていたメッセージやタイトルが初期値としてテキストボックスに格納される --%>
            <c:import url="_form.jsp" />
        </form>

        <p><a href="${pageContext.request.contextPath}/index">一覧に戻る</a></p>

    </c:param>
</c:import>


<%-- http://localhost:8080/message_board/edit?id=1 --%>