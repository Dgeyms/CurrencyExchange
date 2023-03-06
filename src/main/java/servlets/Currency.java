package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import params.ParamsCurrency;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


@WebServlet("/currency")
public class Currency extends HttpServlet {
    public Connection connection;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String getNameCurrency = request.getParameter("nameCurrency");

        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.print("ДАННЫЕ ПО ВАЛЮТЕ" + "<br/>");
        ParamsCurrency selectCurrencyParams = selectCurrencyParams(getNameCurrency);
        out.print(selectCurrencyParams.toString());
    }

    public ParamsCurrency selectCurrencyParams(String getNameCurrency) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/dgeyms/Yandex.Disk.localized/CurrencyExchange/src/CurrencyExchangeDatabase.db");
            System.out.println("Connect YES");

            String sql = "SELECT * FROM Currencies WHERE Code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, getNameCurrency);

            ResultSet resSet = preparedStatement.executeQuery();
            int id = resSet.getInt(1);
            String code = resSet.getString(2);
            String fullName = resSet.getString(3);
            String sing = resSet.getString(4);

            return new ParamsCurrency(id, code, fullName, sing);
        } catch (Exception e) {
            System.out.println("Connect No");
        }
        return null;
    }
}
