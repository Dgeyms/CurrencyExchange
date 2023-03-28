package servlets;
/*
* Обменный курс двух валют
 */

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateTwoCurrencies extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String currencyPair = request.getPathInfo().substring(1); // убираем первый символ "/"
        if (currencyPair.length() != 6){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Указан не корректный код. Правильно (USDEUR");
            return;
        }

        String BaseCurrency = currencyPair.substring(0, 3);
        String TargetCurrency = currencyPair.substring(3, 6);

        try {
            receivingSpecificCurrencyExchange(BaseCurrency, TargetCurrency);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

   private void receivingSpecificCurrencyExchange(String BaseCurrency, String TargetCurrency ) throws SQLException, ClassNotFoundException {
       Connection connection = null;

       try {
           Class.forName("org.sqLite.JDBC");
           connection = DriverManager.getConnection("jdbc:sqlite::resource:CurrencyExchangeDatabase.db");

           String sql = "SELECT * FROM  ";

       } finally {
           connection.close();
       }


   }
}
