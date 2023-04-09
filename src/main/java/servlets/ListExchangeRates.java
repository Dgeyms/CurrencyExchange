package servlets;
/*
* Получение списка всех обменных курсов.
 */
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import params.ParamsCurrency;
import params.ParamsExchangeRates;
import utility.SelectParamsCurrency;
import utility.UrlDatabase;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/exchangeRates")
public class ListExchangeRates extends HttpServlet{
    private int id;
    private ParamsCurrency paramsBase;
    private ParamsCurrency paramsTarget;
    private double rate;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.println("СПИСОК ВСЕХ ОБМЕННЫХ КУРСОВ");
        out.println("---------------------------");

        SelectParamsCurrency selectParamsCurrency = new SelectParamsCurrency();

        ArrayList<ParamsExchangeRates> paramsExchangeRates = getExchangeRates();

        for(ParamsExchangeRates c : paramsExchangeRates ){
           int baseCurrencyId = c.getBaseCurrencyId();
            ParamsCurrency paramsBase = selectParamsCurrency.selectCurrencyParams(baseCurrencyId);

            JSONObject paramBaseJson = new JSONObject();
            paramBaseJson.put("id", paramsBase.getId());
            paramBaseJson.put("code", paramsBase.getCode());
            paramBaseJson.put("fullName", paramsBase.getFullName());
            paramBaseJson.put("sign", paramsBase.getSign());

            int targetCurrencyId = c.getTargetCurrencyId();
            ParamsCurrency paramsTarget = selectParamsCurrency.selectCurrencyParams(targetCurrencyId);
            JSONObject paramTargetJson = new JSONObject();
            paramTargetJson.put("id", paramsTarget.getId());
            paramTargetJson.put("code", paramsTarget.getCode());
            paramTargetJson.put("fullName", paramsTarget.getFullName());
            paramTargetJson.put("sign", paramsTarget.getSign());

            JSONObject jsonId = new JSONObject();
            jsonId.put("id", c.getId());
            jsonId.put("baseCurrency", paramBaseJson);
            jsonId.put("targetCurrency", paramTargetJson);
            jsonId.put("rate", c.getRate());

            String json = jsonId.toString(4);
            out.println(json);
            out.println("---------------------------");
        }
    }

    public ArrayList<ParamsExchangeRates> getExchangeRates(){
        Connection connection = null;
        Statement stmt = null;
        ResultSet resSet = null;

        ArrayList<ParamsExchangeRates> paramsExchangeRates = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(UrlDatabase.urlRelativePath);

            stmt = connection.createStatement();
            resSet = stmt.executeQuery("SELECT * FROM ExchangeRates");
            while(resSet.next()){
                int id = resSet.getInt(1);
                int baseCurrencyId = resSet.getInt(2);
                int targetCurrencyId = resSet.getInt(3);
                double rate = resSet.getDouble(4);
                paramsExchangeRates.add(new ParamsExchangeRates(id, baseCurrencyId, targetCurrencyId, rate));
            }
        }catch(ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("Ошибка SQL!");
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
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
        return paramsExchangeRates;
    }
}
