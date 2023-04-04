package servlets;
/*
 *Получение списка валют
 */
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import params.ListCurrencyParams;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/listCurrencies")
public class ListCurrencies extends HttpServlet {
    public Connection connection;
    // Получение данных
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.println("СПИСОК ВАЛЮТ");
        out.println("---------------------------");

        JSONObject json = new JSONObject();
        JSONObject listCurrenciesJSON = new JSONObject();

        ArrayList<ListCurrencyParams> currencies = selectListCurrencyParams();
        for (ListCurrencyParams c : currencies){
            listCurrenciesJSON.put("id", c.getId());
            listCurrenciesJSON.put("name", c.getFullName());
            listCurrenciesJSON.put("code", c.getCode());
            listCurrenciesJSON.put("sing", c.getSing());

            json.put("Currency", listCurrenciesJSON);
            String jsonString = json.toString(4);
            out.println(jsonString.toString());
        }
    }

    // Получаем данные из таблицы Currencies
    public ArrayList<ListCurrencyParams> selectListCurrencyParams() {
        Connection connection = null;
        Statement stmt = null;
        ResultSet resSet = null;

        ArrayList<ListCurrencyParams> currencies = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite::resource:CurrencyExchangeDatabase.db");
            System.out.println("Connect YES");

            stmt = connection.createStatement();

           resSet = stmt.executeQuery("SELECT * FROM Currencies");
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
        } finally {
            try {
                if (resSet != null) {
                    resSet.close();
                }
                if (stmt!= null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error while closing resources: " + e.getMessage());
        }
    }
        return currencies;
    }
}