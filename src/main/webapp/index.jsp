
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Menu</title>
</head>
<body>
  <h3>МЕНЮ ВАЛЮТНЫХ ОПЕРАЦИЙ</h3>

  <h3>Получить список валют</h3>
  <form method="get" action="listCurrencies">
    <input type = "submit" value="Получить список валют">
  </form>

    <h3>Получение данных по конкретной валюте:</h3>
<form method="get" action="currency">
  <table>
    <tr>
      <td>Введите имя валюты</td>
      <td><input type="text" name="nameCurrency"></td>
    </tr>
    <tr>
      <td align="right" colspan="2"><input type="submit" value="Отправить"></td>
    </tr>
  </table>
</form>
  <h3>Добавление новой валюты в базу данных</h3>
<form method="post" action="addCurrencies">
  <tr>
    <td><td>Введите код валюты</td>
    <td><input type="text" name="addCodeCurrencies"></td></td>
    <td><td>Введите имя валюты</td>
    <td><input type="text" name="addFullNameCurrencies"></td></td>
    <td><td>Введите символ валюты</td>
    <td><input type="text" name="addSingCurrencies"></td></td>
    <td align="right" colspan="2"><input type="submit" value="Отправить"></td>
  </tr>
</form>
</body>

</html>
