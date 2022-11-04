package si.um.feri.nanoclm.backend.search.vao.filter;

import si.um.feri.nanoclm.backend.search.vao.Filter;

public class PropertyFilter extends Filter {

    public enum FilterType { EQUALS, CONTAINS }

    public PropertyFilter(FilterType type, String propertyName, String content) {
        this.type = type;
        this.propertyName = propertyName;
        this.content = content;
    }

    private FilterType type;

    private String propertyName;

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String toString() {
        return "PropertyFilter{" +
                "type=" + type +
                ", propertyName='" + propertyName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
