<%@ taglib prefix="s" uri="/struts-tags" %>
	<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
		<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
				<title>eVoting</title>
				<link rel="shortcut icon" href="resources/images/uc_logo.png">
				<link rel="stylesheet" href="styles/index.css">
			</head>
			
			<body>				
				<div class="wrapper fadeInDown">
					<div id="formContent">
						<h2 class="active">eVoting</h2>
						<form action="user_login" method="post">
							<input type="text" id="Login" class="fadeIn second" name="username" placeholder="Username">
							<input type="password" id="password" class="fadeIn third" name="password" placeholder="Password">
							<input type="submit" class="fadeIn fourth" id ="botao_submit" value="Login">    
                        </form>
                        <form action = "fb_login" method="post">
                            <input type="submit" id="facebook_button" class="fadeIn third" value="Login com Facebook">
                        </form>
					</div>
				</div>
			</body>
		</html>

