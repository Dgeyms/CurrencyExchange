package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.print("ВАЛЮТА УСПЕШНО ДОБАВЛЕНА" + "<br/>");

        Currency currency = new Currency();
        ParamsCurrency selectCurrencyParams = currency.selectCurrencyParams(getCodeCurrency);
        out.print(selectCurrencyParams.toString());
        System.out.println(selectCurrencyParams.toString());
    }

    public void addCurrenciesDataBase(ParamsCurrency paramsCurrency){
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/dgeyms/Yandex.Disk.localized/CurrencyExchange/src/CurrencyExchangeDatabase.db");
            System.out.println("Connect YES");

            String sql = "INSERT INTO Currencies (Code, FullName, Sing) VALUES(?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, paramsCurrency.getCode());
            statement.setString(2, paramsCurrency.getFullName());
            statement.setString(3, paramsCurrency.getSign());

            int x = statement.executeUpdate();
            if (x > 0)
                System.out.println("Successfully Inserted");
            else
                System.out.println("Insert Failed");

            statement.close();
            connection.close();
            System.out.println("Отключение от СУБД выполнено.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("Ошибка SQL!");
        }
    }

}
