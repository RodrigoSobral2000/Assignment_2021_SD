<%@ taglib prefix="s" uri="/struts-tags" %>
	<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
		<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
		<html>

			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
				<title>Menu Admin</title>
				<link rel="shortcut icon" href="resources/images/uc_logo.png">
				<link rel="stylesheet" href="styles/regist_template.css">
			</head>
			
			<body>
				<h1>Consultar Eleicoes Passadas</h1>
				<br><br>
				<div class="container">
					<form>
						<textarea value="%{ask_elections}" cols="95" rows="50" disabled="true"/>
                    </form>
                    <form action="admin">
                        <button>Cancelar</button>
                    </form>
				</div>
			</body>
		</html>