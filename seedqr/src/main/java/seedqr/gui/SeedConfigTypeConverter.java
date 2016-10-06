package seedqr.gui;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("seedConfigTypeConverter")
public class SeedConfigTypeConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value.equals("基本信息") ? 1 : 2;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Integer) value) == 1 ? "基本信息" : "种植信息";
    }
}
