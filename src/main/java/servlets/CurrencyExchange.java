package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import params.ParamsCurrency;
import utility.CurrencyCheckDatabase;
import utility.CurrencyId;
import utility.UrlDatabase;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/*
* Рассчёт перевода определённого количества средств из одной валюты в другую
 */
@WebServlet("/exchange/*")
public class CurrencyExchange extends HttpServlet {
    private String baseCurrency;
    private String targetCurrency;
    private double amountMoney; // Сумма для конвертации
    private double rate; // Курс валют
    private double convertedAmount; // Преобразованная сумма

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }
    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }
    public void setAmountMoney(double amountMoney) {
        this.amountMoney = amountMoney;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }
    public void setConvertedAmount(double convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setBaseCurrency(request.getParameter("baseCurrency"));
        setTargetCurrency(request.getParameter("targetCurrency"));
        setAmountMoney(Double.parseDouble(request.getParameter("amountMoney")));

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        if (amountMoney < 0) {
            out.println("Сумма не может быть меньше нуля");
            return;
        }
        if (baseCurrency.matches("[A-Z]{3}") && targetCurrency.matches("[A-Z]{3}")) {
            if(CurrencyCheckDatabase.checkPresenceCurrency(baseCurrency) && CurrencyCheckDatabase.checkPresenceCurrency(targetCurrency)){
                CurrencyId currencyId = new CurrencyId();
                int idBaseCurrency = currencyId.getCurrencyId(baseCurrency);
                int idTargetCurrency = currencyId.getCurrencyId(targetCurrency);

                setRate(getRate(idBaseCurrency, idTargetCurrency));
                setConvertedAmount(makeConvertedAmount(rate, amountMoney));


                out.println("СРЕДСТВА УСПЕШНО ПЕРЕВЕДЕНЫ");
                out.println("---------------------------");

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
                jsonId.put("rate", rate);
                jsonId.put("baseCurrency", jsonParamsCurrencyBase);
                jsonId.put("targetCurrency", jsonParamsCurrencyTarget);
                jsonId.put("amount", amountMoney);
                jsonId.put("convertedAmount", convertedAmount);

                String json = jsonId.toString(4);
                out.println(json);
                out.println("---------------------------");
            }else{
                out.println("Одной из валют нет в базе данных");
            }
        } else {
            out.println("Код валюты неправильный! (Пример правильного кода: USD)");
        }
    }

    public double getRate(int idBaseCurrency, int idTargetCurrency){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resSet = null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(UrlDatabase.urlRelativePath);

            preparedStatement = connection.prepareStatement("SELECT Rate FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?");
            preparedStatement.setInt(1, idBaseCurrency);
            preparedStatement.setInt(2, idTargetCurrency);
            resSet = preparedStatement.executeQuery();

            double rate = 0.0;
            if (resSet.next()) {
                rate = resSet.getDouble("Rate");
            }
            return rate;

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
                if (resSet != null) {
                    resSet.close();
                }
            } catch (SQLException e) {
                System.out.println("Error while closing resources: " + e.getMessage());
            }
        }
        return 0.0;
    }

    private double makeConvertedAmount(double rate, double amountMoney){
        double convertedAmount = rate * amountMoney;
        return convertedAmount;
    }
}
