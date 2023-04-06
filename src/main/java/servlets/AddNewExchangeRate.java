package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import params.ParamsCurrency;
import utility.CurrencyId;
import utility.UrlDatabase;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * Добавление нового обменного курса в базу
 */
@WebServlet("/addNewRate/*")
public class AddNewExchangeRate extends HttpServlet {
    private String baseCurrency;
    private String targetCurrency;
    private double exchangeRate;

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }
    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }
    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setBaseCurrency(request.getParameter("baseCurrency"));
        setTargetCurrency(request.getParameter("targetCurrency"));
        setExchangeRate(Double.parseDouble(request.getParameter("exchangeRate"))); // BigDecimal не поддерживает sqlite.JDBC

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        if (exchangeRate < 0) {
            out.println("Курс не может быть меньше нуля");
            return;
        }
        if (baseCurrency.matches("[A-Z]{3}") && targetCurrency.matches("[A-Z]{3}")) {
            CurrencyId currencyId = new CurrencyId();
            int idBaseCurrency = currencyId.getCurrencyId(baseCurrency);
            int idTargetCurrency = currencyId.getCurrencyId(targetCurrency);

            out.println("НОВЫЙ ОБМЕННЫЙ КУРС");
            out.println("---------------------------");

            addNewExchangeRateDatabase(idBaseCurrency, idTargetCurrency, exchangeRate);

            ExchangeRateTwoCurrencies exchangeRateTwoCurrencies = new ExchangeRateTwoCurrencies();
            double newRate =  exchangeRateTwoCurrencies.receivingSpecificCurrencyExchange(idBaseCurrency, idTargetCurrency);

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
            jsonId.put("rate", newRate);

            String json = jsonId.toString(4);
            out.println(json);
            out.println("---------------------------");
        } else {
            out.println("Код валюты неправильный или курс меньше нуля");
        }
    }

    public void addNewExchangeRateDatabase(int idBaseCurrency, int idTargetCurrency, double exchangeRate){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(UrlDatabase.url);

            String sql = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES(?, ?, ?)";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idBaseCurrency);
            preparedStatement.setInt(2, idTargetCurrency);
            preparedStatement.setDouble(3, exchangeRate);
            int x = preparedStatement.executeUpdate();

            if (x > 0) {
                System.out.println("Успешно вставлено");
            } else {
                System.out.println("Вставить не удалось");
            }

        }catch(ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("Ошибка SQL!");
        } finally {
            try {
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
    }
}
