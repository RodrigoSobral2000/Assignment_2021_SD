<%@ taglib prefix="s" uri="/struts-tags" %>
	<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
		<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
		<html>

			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
				<title>Menu Principal</title>
				<link rel="shortcut icon" href="./resources/images/uc_logo.png">
				<link rel="stylesheet" href="./styles/index.css">
			</head>
			
			<body>
				<form action="login" method="post">
					
					<div class="container">
						<input type="text" placeholder="Username">
						<input type="password" placeholder="Password">
						<button>Login</button>

						<input id="fblogo" type="image" src="resources/images/facebook_logo.png" alt="Submit" width="48" height="48">
					</div>
				</form>

			</body>
		</html>