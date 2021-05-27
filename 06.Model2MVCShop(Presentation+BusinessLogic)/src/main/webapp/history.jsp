<%@ page contentType="text/html; charset=EUC-KR" %>
<%@ page import = "java.net.URLDecoder" %>


<html>
<head>

<title>열어본 상품 보기</title>

</head>
<body>
	당신이 열어본 상품을 알고 있다
<br>
<br>
<%
	request.setCharacterEncoding("euc-kr");
	response.setCharacterEncoding("euc-kr");
	String history = null;
	Cookie[] cookies = request.getCookies();
	if (cookies!=null && cookies.length > 0) {
		for (int i = 1; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie.getName() != null && cookie.getName().startsWith("history")) {
				history = cookie.getValue();
				%>
				<a href="/getProduct.do?prodNo=<%=history%>&menu=search" target="rightFrame"><%=URLDecoder.decode(cookie.getName().substring(7), "UTF-8")%></a>
				<br>
				<%
			}
		}
	}
%>

</body>
</html>