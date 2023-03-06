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
    // Выводим информацию по продукту

    @Override
    public String toString() {
        return "servlets.Currency{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", fullName='" + fullName + '\'' +
                ", sing=" + sing +
                '}';
    }
}
