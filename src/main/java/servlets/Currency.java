package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import params.ParamsCurrency;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/currency")
public class Currency extends HttpServlet {
    public Connection connection;
    public Statement stmt;
    public ResultSet resSet;

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
        ArrayList<ParamsCurrency> currency = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Java project/My project/CurrencyExchange/CurrencyExchangeDatabase.db");
            System.out.println("Connect YES");

            //Для взаимодействия с базой данных приложение отправляет серверу MySQL команды на языке SQL.
            //Чтобы выполнить команду, вначале необходимо создаеть объект Statement.
            Statement stmt = connection.createStatement(); // создаем заявление

            // В resultSet будет храниться результат нашего запроса,
            // который выполняется командой stmt.executeQuery()
           /*PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Currencies WHERE code = 'AUG'");
           int id = preparedStatement.executeUpdate("ID");
           String name = preparedStatement.enquoteLiteral("Code");
           String code = preparedStatement.setString(3);
           char sign = preparedStatement.setString(4).charAt(0);*/

            ResultSet resSet = stmt.executeQuery("SELECT * FROM Currencies WHERE code = 'AUD'");
            while (resSet.next()) {
                int id = resSet.getInt(1);
                String name = resSet.getString(2);
                String code = resSet.getString(3);
                char sign = resSet.getString(4).charAt(0);

                return new ParamsCurrency(id, name, code, sign);
            }
        } catch (Exception e) {
            System.out.println("No");
        }
        return null;
    }
}
