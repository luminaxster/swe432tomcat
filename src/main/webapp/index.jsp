<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
  <script>
    var servletURL = window.location.origin;
  </script>
<title>First JSP</title>
</head>
<%@ page import="java.util.Date" %>
<body>
  <h2>Hello Heroku! I am JSP</h2>
  <strong>Current Time is</strong>: <%=new Date() %>
  <p>Try a: </p>
  <div style="display: flex; flex-direction: column;">
    <button onclick="window.location.assign(servletURL+'/hello');">Simple servlet </button>
    <button onclick="window.location.assign(servletURL+'/twoButtons');"> Two Buttons servlet </button>
    <button onclick="window.location.assign(servletURL+'/echo');">Echo POST requests servlet </button>
    <button onclick="window.location.assign(servletURL+'/file');">File persistence servlet </button>
    <button onclick="window.location.assign(servletURL+'/json');">JSON File persistence servlet </button>
    <button onclick="window.location.assign(servletURL+'/xml');">XML File persistence servlet </button>
    <button onclick="window.location.assign(servletURL+'/database');">Database persistence servlet </button>
  </div>
</body>
</html>
