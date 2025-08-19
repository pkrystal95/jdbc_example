<!-- ===============================================
     1) index.jsp
================================================== -->
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>메모 앱</title></head>
<body>
  <h1>메모 앱</h1>
  <p><a href="<%= request.getContextPath() %>/memos">메모 목록 보기</a></p>
  <p><a href="<%= request.getContextPath() %>/memos/new">메모 작성</a></p>
</body>
</html>