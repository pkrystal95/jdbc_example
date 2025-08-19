<!-- ===============================================
     3) /memo/form.jsp
     - 메모 작성 폼. POST /memos 로 제출
     - 간단한 에러 메시지 표시(request scope의 "error")
================================================== -->
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>메모 작성</title></head>
<body>
  <h2>새 메모 작성</h2>

  <%
    String error = (String) request.getAttribute("error");
    if (error != null && !error.isEmpty()) {
  %>
    <p style="color:red;"><%= error %></p>
  <%
    }
  %>

  <form method="post" action="<%= request.getContextPath() %>/memos" accept-charset="UTF-8">
    <div>
      <label>제목</label><br/>
      <input type="text" name="title" style="width: 360px;" />
    </div>
    <div style="margin-top:8px;">
      <label>내용</label><br/>
      <textarea name="content" rows="6" style="width: 360px;"></textarea>
    </div>
    <div style="margin-top:12px;">
      <button type="submit">저장</button>
      <a href="<%= request.getContextPath() %>/memos">취소</a>
    </div>
  </form>

  <p style="margin-top:16px;"><a href="<%= request.getContextPath() %>/">홈으로</a></p>
</body>
</html>