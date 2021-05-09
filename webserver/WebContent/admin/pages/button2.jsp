<%@ taglib prefix="s" uri="/struts-tags" %>
	<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
		<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
		<html>

			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
				<title>Admin Menu</title>
				<link rel="shortcut icon" href="../resources/images/uc_logo.png">
				<link rel="stylesheet" href="../styles/adminmenu.css">
			</head>

			<body>
				<h1>Registar Eleicao</h1>
				<br><br>
				<div class="container">
					<form action="regist_eleitor">
                        <label class="checking">Estudante
							<input type="radio" checked="checked" name="radio">
							<span class="checkmark"></span>
						</label>
						<label class="checking">Professor
							<input type="radio" name="radio">
							<span class="checkmark"></span>
						</label>
						<label class="checking">Funcionario
							<input type="radio" name="radio">
							<span class="checkmark"></span>
						</label>
						<input type="text" placeholder="Nome">
						<input type="password" placeholder="password">
						<input type="text" placeholder="Morada">
						<input type="number" placeholder="Contacto Telefonico">
						<input type="text" placeholder="Faculdade">
						<input type="text" placeholder="Departamento">
						<input type="number" placeholder="Numero Cartao Cidadao">
						<input type="text" placeholder="Validade Cartao Cidadao">
                    </form>
                    <form action="cancel_admin">
                        <button>Cancelar</button>
                    </form>
				</div>
			</body>
		</html>