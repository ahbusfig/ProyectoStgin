<%--
  Created by IntelliJ IDEA.
  User: alain
  Date: 30/12/2023
  Time: 10:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Unirse a partida - Conecta4</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            color: #1a1919;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }
        .title {
            margin-bottom: 20px;
            color: #61dafb;
        }
        .button {
            background-color: #61dafb;
            color: #282c34;
            padding: 15px 30px;
            margin: 10px;
            border: none;
            border-radius: 25px;
            cursor: pointer;
            font-size: 18px;
            transition: all 0.3s;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
            text-transform: uppercase;
            font-weight: bold;
        }
        .button:hover {
            background-color: #4fa1c0;
            box-shadow: 0px 6px 12px rgba(0, 0, 0, 0.3);
        }
        .button:focus {
            outline: none;
        }
        .container {
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container">
    <!-- Formulario para ingresar el código de partida -->
    <form action="UnirsePartidaServlet" method="post">
        <h1 class="title">Unirse a una partida</h1>
        <br>
        <br>

        <label for="codigoPartida">Introduce el código de la partida:</label>
        <input type="text"  id="codigoPartida" name="codigoPartida"  maxlength="6" required>
        <button type="submit" class="button">Unirse a la Partida</button>
    </form>
</div>
</body>
</html>