package params;

public class ParamsExchangeRates {
    private int id;
    private int baseCurrencyId ;
    private int targetCurrencyId ;
    private int rate ;

    public ParamsExchangeRates(int id, int baseCurrencyId, int targetCurrencyId, int rate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public int getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public int getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public int getRate() {
        return rate;
    }
}
