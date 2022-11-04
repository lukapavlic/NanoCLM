package si.um.feri.nanoclm.backend.search.vao.filter;


import si.um.feri.nanoclm.backend.search.vao.Filter;

public class AttributeFilter extends Filter {

    public enum FilterType {ATTRIBUTE_PRESENT, ATTRIBUTE_ABSENT}

    public AttributeFilter(FilterType type, String content) {
        this.type = type;
        this.content=content;
    }

    private FilterType type;

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AttributeFilter{" +
                "type=" + type +
                ", content='" + content + '\'' +
                '}';
    }

}
