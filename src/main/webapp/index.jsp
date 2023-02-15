
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
      <td>Введите имя валюты:</td>
      <td><input type="text" name="nameCurrency"></td>
    </tr>
    <tr>
      <td align="right" colspan="2"><input type="submit" value="Отправить"></td>
    </tr>
  </table>
</form>
</body>

</html>
