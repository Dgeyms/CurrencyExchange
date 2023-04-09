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
import utility.UrlDatabase;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/addCurrencies")
public class AddNewCurrencies extends HttpServlet {
    String getCodeCurrency;
    String getFullNameCurrency;
    String getSingCurrency;
    public void setGetCodeCurrency(String getCodeCurrency) {
        this.getCodeCurrency = getCodeCurrency;
    }
    public void setGetFullNameCurrency(String getFullNameCurrency) {
        this.getFullNameCurrency = getFullNameCurrency;
    }
    public void setGetSingCurrency(String getSingCurrency) {
        this.getSingCurrency = getSingCurrency;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setGetCodeCurrency(request.getParameter("addCodeCurrencies")) ;
        setGetFullNameCurrency(request.getParameter("addFullNameCurrencies")) ;
        setGetSingCurrency(request.getParameter("addSingCurrencies")) ;

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.println("ВАЛЮТА УСПЕШНО ДОБАВЛЕНА");

        if (getCodeCurrency.matches("[A-Z]{3}")) {
            addCurrenciesDataBase(new ParamsCurrency(getCodeCurrency, getFullNameCurrency, getSingCurrency));

            Currency currency = new Currency();
            ParamsCurrency selectCurrencyParams = currency.selectCurrencyParams(getCodeCurrency);

            JSONObject currencyJSON = new JSONObject();
            currencyJSON.put("id", selectCurrencyParams.getId());
            currencyJSON.put("code", selectCurrencyParams.getCode());
            currencyJSON.put("fullName", selectCurrencyParams.getFullName());
            currencyJSON.put("sign", selectCurrencyParams.getSign());

            out.println(currencyJSON.toString(4));
        }else {
            out.print("Код валюты неправильный! (Пример правильного кода: USD)");
        }
    }
    public void addCurrenciesDataBase(ParamsCurrency paramsCurrency){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(UrlDatabase.urlRelativePath);

            String sql = "INSERT INTO Currencies (Code, FullName, Sing) VALUES(?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, paramsCurrency.getCode());
            statement.setString(2, paramsCurrency.getFullName());
            statement.setString(3, paramsCurrency.getSign());

            int x = statement.executeUpdate();
            if (x > 0) {
                System.out.println("Успешно вставлено");
            } else {
                System.out.println("Вставить не удалось");
            }
        }catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Ошибка SQL при выполнении операции вставки: " + e.getMessage());
                System.out.println("SQL state: " + e.getSQLState());
                System.out.println("Error code: " + e.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC драйвер для СУБД не найден!");

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
