<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*,memo.model.dto.MemoDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>메모 목록</title>
</head>
<body>
  <h2>메모 목록</h2>
  <p>
    <a href="<%= request.getContextPath() %>/memos/new">새 메모 작성</a>
  </p>
  <ul>
    <%
      Object obj = request.getAttribute("memos");
      if (obj instanceof List) {
          List<?> raw = (List<?>) obj;
          for (Object o : raw) {
              MemoDTO m = (MemoDTO) o;
    %>
      <li>
        <strong><%= m.title() %></strong>
        <div style="white-space: pre-wrap;"><%= m.content() %></div>
        <small>작성일: <%= m.createdAt() %></small>
      </li>
    <%
          }
          if (raw.isEmpty()) {
    %>
      <li>등록된 메모가 없습니다.</li>
    <%
          }
      } else {
    %>
      <li>목록 데이터를 불러오지 못했습니다.</li>
    <%
      }
    %>
  </ul>

  <p><a href="<%= request.getContextPath() %>/">홈으로</a></p>
</body>
</html>