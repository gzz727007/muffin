package seedqr;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Manufacturer.class, value = "manufacturer")
public class ManufacturerConverter implements Converter {
    private DataService dataService;

    public ManufacturerConverter() {
        dataService = CDI.current().select(DataService.class).get();
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return dataService.getManufacturer(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return Integer.toString(((Manufacturer) value).getId());
    }
}
