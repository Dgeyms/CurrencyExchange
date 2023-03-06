package servlets;
/*
 *Получение списка валют
 */

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import params.ListCurrencyParams;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/listCurrencies")
public class ListCurrencies extends HttpServlet {
    public Connection connection;
    public Statement stmt;
    public ResultSet resSet;

    // Получение данных
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.print("СПИСОК ВАЛЮТ" + "<br/>");

        ArrayList<ListCurrencyParams> currencies = selectListCurrencyParams();
        for (ListCurrencyParams c : currencies){
            out.println(c + "<br/>");
        }
        out.close();
    }

    // Получаем данные из таблицы Currencies
    public ArrayList<ListCurrencyParams> selectListCurrencyParams() {

        ArrayList<ListCurrencyParams> currencies = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC"); //здесь мы загружаем файл класса драйвера в память во время выполнения
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/dgeyms/Yandex.Disk.localized/CurrencyExchange/src/CurrencyExchangeDatabase.db");
            System.out.println("Connect YES");

            Statement stmt = connection.createStatement(); // создаем заявление

            ResultSet resSet = stmt.executeQuery("SELECT * FROM Currencies");
            while (resSet.next()) {
                int id = resSet.getInt(1);
                String code = resSet.getString(2);
                String fullName = resSet.getString(3);
                String sing = resSet.getString(4);

                currencies.add(new ListCurrencyParams(id, code, fullName, sing));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("Ошибка SQL !");
        }
        return currencies;
    }
}