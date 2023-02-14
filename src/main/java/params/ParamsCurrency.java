package params;

public class ParamsCurrency {
    private int id;
    private String name;
    private String code;
    private char sign;

    public ParamsCurrency(int id, String name, String code, char sign) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "ParamsCurrency{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", sign=" + sign +
                '}';
    }
}
