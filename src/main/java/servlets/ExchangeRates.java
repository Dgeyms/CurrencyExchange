package servlets;
/*
* Получение списка всех обменных курсов.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import params.ParamsCurrency;
import params.ParamsExchangeRates;
import utility.SelectParamsCurrency;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/exchangeRates")
public class ExchangeRates extends HttpServlet{
    Connection connection;
    private int id;
    private ParamsCurrency paramsBase;
    private ParamsCurrency paramsTarget;
    private int rate;

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
        ArrayList<ParamsExchangeRates> paramsExchangeRates = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite::resource:CurrencyExchangeDatabase.db");

            Statement stmt = connection.createStatement();
            ResultSet resSet = stmt.executeQuery("SELECT * FROM ExchangeRates");
            while(resSet.next()){
                int id = resSet.getInt(1);
                int baseCurrencyId = resSet.getInt(2);
                int targetCurrencyId = resSet.getInt(3);
                int rate = resSet.getInt(4);
                paramsExchangeRates.add(new ParamsExchangeRates(id, baseCurrencyId, targetCurrencyId, rate));
            }
            stmt.close();
            resSet.close();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Ошибка при получении списка обменных курсов", e);
        }
        return paramsExchangeRates;
    }
    @Override
    public String toString() {
        return String.format("{\n" +
                "\"id\": %l,\n" +
                "\"baseCurrency\": %s,\n" +
                "\"targetCurrency\": %s,\n" +
                "\"rate\": %f\n" +
                "}", id, paramsBase, paramsTarget, rate);
    }
}
