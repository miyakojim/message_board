<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="../layout/app.jsp">
    <c:param name="content">

        <h2>id : ${message.id} のメッセージ詳細ページ</h2>

        <p>タイトル：<c:out value="${message.title}" /></p>
        <p>メッセージ：<c:out value="${message.content}" /></p>
        <%-- 日時情報は基本的にオブジェクトの形で、単純な文字列ではない --%>
        <%-- <fmt:formatDate> タグで作成日時や更新日時を pattern 属性で指定した 年-月-日 時:分:秒 の形式で表示するようにしている --%>
        <p>作成日時：<fmt:formatDate value="${message.created_at}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
        <p>更新日時：<fmt:formatDate value="${message.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" /></p>

        <p><a href="${pageContext.request.contextPath}/index">一覧に戻る</a></p>

    </c:param>
</c:import>

<%-- http://localhost:8080/message_board/show?id=1 --%>