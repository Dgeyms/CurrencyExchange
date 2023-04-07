package servlets;
/*
* Получение данных по конкретной валюте
 */

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import params.ParamsCurrency;
import utility.CurrencyCheckDatabase;
import utility.UrlDatabase;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static java.lang.System.out;


@WebServlet("/currency")
public class Currency extends HttpServlet {
    private String nameCurrency;

    public void setNameCurrency(String nameCurrency) {
        this.nameCurrency = nameCurrency;
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setNameCurrency(request.getParameter("nameCurrency"));

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.println("ДАННЫЕ ПО ВАЛЮТЕ");
        out.println("---------------------------");

        if (nameCurrency.matches("[A-Z]{3}")) {
            ParamsCurrency selectCurrencyParams = selectCurrencyParams(nameCurrency);
            if(selectCurrencyParams != null){
                JSONObject currencyJSON = new JSONObject();
                currencyJSON.put("id", selectCurrencyParams.getId());
                currencyJSON.put("code", selectCurrencyParams.getCode());
                currencyJSON.put("fullName", selectCurrencyParams.getFullName());
                currencyJSON.put("sign", selectCurrencyParams.getSign());

                String jsonString = currencyJSON.toString(4);
                out.print(jsonString);
            }else{
                out.print("Такой валюты нет в базе данных!");
            }
        }else {
            out.print("Код валюты неправильный! (Пример правильного кода: USD)");
        }
    }

    public ParamsCurrency selectCurrencyParams(String getNameCurrency) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resSet = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(UrlDatabase.url);
            out.println("Connect YES");

            String sql = "SELECT * FROM Currencies WHERE Code = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, getNameCurrency);

            resSet = preparedStatement.executeQuery();
            int id = resSet.getInt(1);
            String code = resSet.getString(2);
            String fullName = resSet.getString(3);
            String sing = resSet.getString(4);

            return new ParamsCurrency(id, code, fullName, sing);
        } catch (Exception e) {
            out.println("Connect No");
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
            } catch (Exception e) {
                out.println("Error while closing resources: " + e.getMessage());
            }
        }
        return null;
    }
}
