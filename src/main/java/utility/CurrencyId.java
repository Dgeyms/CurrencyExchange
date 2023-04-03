package utility;

import params.ParamsCurrency;
import servlets.Currency;

/*
* Получение ID валют
 */
public class CurrencyId {
    public int getCurrencyId(String baseCurrency){
        Currency currency = new Currency();
        ParamsCurrency paramsCurrency = currency.selectCurrencyParams(baseCurrency);
        int idCurrency = paramsCurrency.getId();
        return idCurrency;
    }

}
