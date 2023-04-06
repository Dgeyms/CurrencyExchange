
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
    <table>
      <tr>
        <td>Введите код валюты</td>
        <td><input type="text" name="addCodeCurrencies"></td>
      </tr>
      <tr>
        <td>Введите имя валюты</td>
        <td><input type="text" name="addFullNameCurrencies"></td>
      </tr>
      <tr>
        <td>Введите символ валюты</td>
        <td><input type="text" name="addSingCurrencies"></td>
      </tr>
      <tr>
        <td align="right" colspan="2"><input type="submit" value="Отправить"></td>
      </tr>
    </table>
  </form>

  <h3>Получение списка всех обменных курсов</h3>
  <form method="get" action="exchangeRates">
      <input type = "submit" value="Получить список курсов">
  </form>

  <h3>Получение конкретного обменного курса</h3>
  <form method="get" action="exchangeRate/*">
    <table>
      <tr>
        <td>Введите код первой валюты, в формате (USD)</td>
        <td><input type="text" name="baseCurrency"></td>
        <td>Введите код второй валюты, в формате (USD)</td>
        <td><input type="text" name="targetCurrency"></td>
        <td align="right" colspan="2"><input type="submit" value="Отправить"></td>
      </tr>
    </table>
  </form>

    <h3>Добавление нового обменного курса в базу</h3>
    <form method="post" action="addNewRate/*" >
      <table>
        <tr>
          <td>Введите код базовой валюты</td>
          <td><input type="text" name="baseCurrency"></td>
        </tr>
        <tr>
          <td>Введите код второй валюты</td>
          <td><input type="text" name="targetCurrency"></td>
        </tr>
        <tr>
          <td>Введите обменный курс</td>
          <td><input type="number" name="exchangeRate" step="0.000001"></td>
        </tr>
        <tr>
          <td align="right" colspan="2"><input type="submit" value="Отправить"></td>
        </tr>
      </table>
    </form>

  <h3>Обновление существующего в базе обменного курса</h3>
  <form method="post" action="multipart/*" >
    <table>
      <tr>
        <td>Введите код базовой валюты</td>
        <td><input type="text" name="baseCurrency"></td>
      </tr>
      <tr>
        <td>Введите код второй валюты</td>
        <td><input type="text" name="targetCurrency"></td>
      </tr>
      <tr>
        <td>Введите новый обменный курс</td>
        <td><input type="number" name="exchangeRate" step="0.000001"></td>
      </tr>
      <tr>
        <td align="right" colspan="2"><input type="submit" value="Отправить"></td>
      </tr>
    </table>
  </form>

  <h3> Перевод из одной валюты в другую </h3>
  <form method="get" action="exchange/*" >
    <table>
      <tr>
        <td>Введите код базовой валюты</td>
        <td><input type="text" name="baseCurrency"></td>
      </tr>
      <tr>
        <td>Введите код второй валюты</td>
        <td><input type="text" name="targetCurrency"></td>
      </tr>
      <tr>
        <td>Сколько хотите перевести?</td>
        <td><input type="number" name="amountMoney" step="0.001"></td>
      </tr>
      <tr>
        <td align="right" colspan="2"><input type="submit" value="Отправить"></td>
      </tr>
    </table>
  </form>


</body>

</html>
