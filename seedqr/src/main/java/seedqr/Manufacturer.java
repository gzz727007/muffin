package seedqr;

import java.util.Objects;

public class Manufacturer {
    private Integer id;
    private String name;

    public Manufacturer() {
    }

    public Manufacturer(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (id != null && obj instanceof Manufacturer) {
            return id.equals(((Manufacturer) obj).id);
        } else {
            return obj == this;
        }
    }
}
