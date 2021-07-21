<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null }">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <c:choose>
            <c:when test="${tasks.size()>0}">
                <ul>
                    <c:forEach var="task" items="tasks" >
                        <li>
                            <a href="${pageContext.request.contextPath}/show?id=${message.id}">
                                <c:out value="${task.id}"></c:out>
                            </a>
                            ：<c:out value="${message.content}" />
                        </li>
                    </c:forEach>
                </ul>
                <div id="pagenation">
                    (全 ${tasks_count} 件)<br>
                    <c:forEach var="i" begin="1" end="${((tasks_count - 1) / 10) + 1}" step="1">
                        <c:choose>
                            <c:when test="${i == page }">
                                <c:out value="${i}"></c:out>&nbsp;
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/index?page=${i}"><c:out value="${i}" /></a>&nbsp;
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <p>タスクはありません。</p>
            </c:otherwise>
        </c:choose>
        <p><a href="${pageContext.request.contextPath}/new">新規タスクの登録</a></p>
    </c:param>
</c:import>