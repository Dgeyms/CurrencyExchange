package servlets;
/*
* Добавление новой валюты в базу данных
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

@WebServlet("/addCurrencies")
public class NewCurrencies extends HttpServlet {
    public Connection connection;
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String getCodeCurrency = request.getParameter("addCodeCurrencies");
        String getFullNameCurrency = request.getParameter("addFullNameCurrencies");
        String getSingCurrency = request.getParameter("addSingCurrencies");
        addCurrenciesDataBase(new ParamsCurrency(getCodeCurrency, getFullNameCurrency, getSingCurrency));

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.println("ВАЛЮТА УСПЕШНО ДОБАВЛЕНА");

        Currency currency = new Currency();
        ParamsCurrency selectCurrencyParams = currency.selectCurrencyParams(getCodeCurrency);

        JSONObject currencyJSON = new JSONObject();

        currencyJSON.put("id", selectCurrencyParams.getId());
        currencyJSON.put("code", selectCurrencyParams.getCode());
        currencyJSON.put("fullName", selectCurrencyParams.getFullName());
        currencyJSON.put("sign", selectCurrencyParams.getSign());

        out.println(currencyJSON.toString(4));
    }

    public void addCurrenciesDataBase(ParamsCurrency paramsCurrency){
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite::resource:CurrencyExchangeDatabase.db");
            System.out.println("Connect YES");

            String sql = "INSERT INTO Currencies (Code, FullName, Sing) VALUES(?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, paramsCurrency.getCode());
            statement.setString(2, paramsCurrency.getFullName());
            statement.setString(3, paramsCurrency.getSign());

            int x = statement.executeUpdate();
            if (x > 0)
                System.out.println("Successfully Inserted");
            else
                System.out.println("Insert Failed");

            statement.close();
            System.out.println("Отключение от СУБД выполнено.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("Ошибка SQL!");
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error while closing resources: " + e.getMessage());
            }
        }
    }

}
