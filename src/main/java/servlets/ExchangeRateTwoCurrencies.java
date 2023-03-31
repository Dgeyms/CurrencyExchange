package servlets;
/*
* Обменный курс двух валют
 */

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import params.ParamsCurrency;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/exchangeRate/*")
public class ExchangeRateTwoCurrencies extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseCurrency = request.getParameter("baseCurrency");
        String targetCurrency = request.getParameter("targetCurrency");

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        if (baseCurrency.matches("[A-Z]{3}") && targetCurrency.matches("[A-Z]{3}")) {
            int idBaseCurrency = getCurrencyId(baseCurrency);
            int idTargetCurrency = getCurrencyId(targetCurrency);


            out.println("ОБМЕННЫЙ КУРС ДВУХ ВАЛЮТ");
            out.println("---------------------------");

            double exchangeRateTwoCurrencies = receivingSpecificCurrencyExchange(idBaseCurrency, idTargetCurrency);
            if (exchangeRateTwoCurrencies == -1) {
                out.println("Exchange rate not found");
                return;
            }

            Currency currency = new Currency();
            ParamsCurrency paramsCurrencyBase = currency.selectCurrencyParams(baseCurrency);
            ParamsCurrency paramsCurrencyTarget = currency.selectCurrencyParams(targetCurrency);

            JSONObject jsonParamsCurrencyBase = new JSONObject();
            jsonParamsCurrencyBase.put("id", paramsCurrencyBase.getId());
            jsonParamsCurrencyBase.put("name", paramsCurrencyBase.getFullName());
            jsonParamsCurrencyBase.put("code", paramsCurrencyBase.getCode());
            jsonParamsCurrencyBase.put("sing", paramsCurrencyBase.getSign());

            JSONObject jsonParamsCurrencyTarget = new JSONObject();
            jsonParamsCurrencyTarget.put("id", paramsCurrencyTarget.getId());
            jsonParamsCurrencyTarget.put("name", paramsCurrencyTarget.getFullName());
            jsonParamsCurrencyTarget.put("code", paramsCurrencyTarget.getCode());
            jsonParamsCurrencyTarget.put("sing", paramsCurrencyTarget.getSign());

            JSONObject jsonId = new JSONObject();
            jsonId.put("baseCurrency", jsonParamsCurrencyBase);
            jsonId.put("targetCurrency", jsonParamsCurrencyTarget);
            jsonId.put("rate", exchangeRateTwoCurrencies);

            String json = jsonId.toString(4);
            out.println(json);
            out.println("---------------------------");
        } else {
            out.println("Код валюты неправильный! (Пример правильного кода: USD)");
        }
    }

   public double receivingSpecificCurrencyExchange(int idBaseCurrency, int idTargetCurrency ){
       Connection connection = null;
       ResultSet resSet = null;
       PreparedStatement preparedStatement = null;
       try {
           Class.forName("org.sqlite.JDBC");
           connection = DriverManager.getConnection("jdbc:sqlite::resource:CurrencyExchangeDatabase.db");

           String sql = "SELECT Rate FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";
           preparedStatement = connection.prepareStatement(sql);
           preparedStatement.setInt(1, idBaseCurrency);
           preparedStatement.setInt(2, idTargetCurrency);
           resSet = preparedStatement.executeQuery();

           if (resSet.next()) {
               double exchangeRate = resSet.getDouble(1);
               return exchangeRate;
           }

       } catch (ClassNotFoundException e) {
           e.printStackTrace(); // обработка ошибки  Class.forName
           System.out.println("JDBC драйвер для СУБД не найден!");
       } catch (SQLException e) {
           e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
           System.out.println("Ошибка SQL!");
       } finally {
           try {
               if (resSet != null) {
                   resSet.close();
               }
               if (preparedStatement != null) {
                   preparedStatement.close();
               }
               if (connection != null) {
                   connection.close();
               }
           } catch (SQLException e) {
               System.out.println("Error while closing resources: " + e.getMessage());
           }
       }
       return -1;
   }
   private int getCurrencyId(String baseCurrency){
        Currency currency = new Currency();
       ParamsCurrency paramsCurrency = currency.selectCurrencyParams(baseCurrency);
        int idCurrency = paramsCurrency.getId();
        return idCurrency;
   }
}
