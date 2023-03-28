package params;

/*
* Валюты
 */
public class ListCurrencyParams {
    private int id;
    private String code;
    private String fullName;
    private String sing;

   public ListCurrencyParams(int id, String code, String fullName, String sing) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sing = sing;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSing() {
        return sing;
    }
}
