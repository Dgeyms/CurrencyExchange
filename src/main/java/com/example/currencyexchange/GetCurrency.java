// Получение списка валют
package com.example.currencyexchange;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


//@WebServlet("/currencies")

public class GetCurrency extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        response.setContentType("text/html"); // название строки, на которую будет осуществлён переход
        // получаем параметр currencies
        //String currencies = response.getParameter("currencies");

        PrintWriter out = response.getWriter();
        out.println("<h2>" + "Список валют" + "</h2>");
        //out.println("</body></html>");*/
    }

    public void destroy() {
    }
}