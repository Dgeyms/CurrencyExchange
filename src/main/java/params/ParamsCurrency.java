package params;
/*
 * Данные по конкретной валюте
 */

public class ParamsCurrency {
    private int id;
    private String code;
    private String fullName;
    private String sign;

    public ParamsCurrency(int id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public ParamsCurrency(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
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

    public String getSign() {
        return sign;
    }


}
