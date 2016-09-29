package seedqr;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DataService {
    private Map<Integer, Manufacturer> manufacturers;

    @PostConstruct
    private void init() {
        manufacturers = new LinkedHashMap<>();
        manufacturers.put(1, new Manufacturer(1, "雪榕生物"));
        manufacturers.put(2, new Manufacturer(2, "新赛股份"));
        manufacturers.put(3, new Manufacturer(3, "隆平高科"));
        manufacturers.put(4, new Manufacturer(4, "香梨股份"));
        manufacturers.put(5, new Manufacturer(5, "登海种业"));
    }

    public Collection<Manufacturer> getManufacturers() {
        return manufacturers.values();
    }

    public Manufacturer getManufacturer(int id) {
        return manufacturers.get(id);
    }

    public Manufacturer addManufacturer(String name) {
        int id = manufacturers.size() + 1;
        Manufacturer manufacturer = new Manufacturer(id, name);
        manufacturers.put(id, manufacturer);
        return manufacturer;
    }
}
