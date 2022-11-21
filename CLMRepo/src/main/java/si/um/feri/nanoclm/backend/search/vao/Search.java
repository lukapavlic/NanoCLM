package si.um.feri.nanoclm.backend.search.vao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import si.um.feri.nanoclm.backend.search.dto.PostSearch;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document("search")
public class Search {

    public enum ResultPresentation { LIST, CARDS }

    public enum SortOrientation { ASC, DESC }

    public Search() {
    }

    public Search(PostSearch ps, String owner, String tenant) {
        this.owner = owner;
        this.onTenant = tenant;
        this.searchName = ps.searchName();
        this.search = ps.search();
        this.publicSearchOnTenant = ps.publicSearchOnTenant();
        this.presentation = ps.presentation();
        this.sortOrientation = ps.sortOrientation();
        this.sortBy =ps.sortBy();
        this.getFilters().addAll(ps.filters());
        this.mongoId = ps.mongoId();
    }

    public PostSearch asPostSearchDto() {
        return new PostSearch(
            mongoId,
            searchName,
            publicSearchOnTenant,
            search,filters,
            sortBy,
            sortOrientation,
            presentation
        );
    }

    public si.um.feri.nanoclm.backend.search.dto.Search asSearchDto() {
        return new si.um.feri.nanoclm.backend.search.dto.Search(
                search,filters,
                sortBy,sortOrientation
        );
    }

    public Search(String owner, String onTenant, String searchName, String search, boolean publicSearchOnTenant, List<Filter> filters) {
        this.owner = owner;
        this.onTenant = onTenant;
        this.searchName = searchName;
        this.search = search;
        this.publicSearchOnTenant = publicSearchOnTenant;
        this.filters = filters;
    }

    @Id
    private String mongoId;

    //user id
    private String owner;

    //user id can be allowed to many tenants - UPPERCASE
    private String onTenant;

    private String searchName;

    private String search="";

    private String sortBy="";

    private SortOrientation sortOrientation=SortOrientation.ASC;

    private ResultPresentation presentation=ResultPresentation.LIST;

    private boolean publicSearchOnTenant=false;


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOnTenant() {
        return onTenant;
    }

    public void setOnTenant(String onTenant) {
        this.onTenant = onTenant.toUpperCase();
    }

    private List<Filter> filters;

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public SortOrientation getSortOrientation() {
        return sortOrientation;
    }

    public void setSortOrientation(SortOrientation sortOrientation) {
        this.sortOrientation = sortOrientation;
    }

    public ResultPresentation getPresentation() {
        return presentation;
    }

    public void setPresentation(ResultPresentation presentation) {
        this.presentation = presentation;
    }

    public boolean isPublicSearchOnTenant() {
        return publicSearchOnTenant;
    }

    public void setPublicSearchOnTenant(boolean publicSearchOnTenant) {
        this.publicSearchOnTenant = publicSearchOnTenant;
    }

    public List<Filter> getFilters() {
        if (filters==null) filters=new ArrayList<>();
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Search srch = (Search) o;
        return Objects.equals(mongoId, srch.mongoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mongoId);
    }

    @Override
    public String toString() {
        return "Search{" +
                "mongoId='" + mongoId + '\'' +
                ", owner='" + owner + '\'' +
                ", onTenant='" + onTenant + '\'' +
                ", searchName='" + searchName + '\'' +
                ", search='" + search + '\'' +
                ", sortBy='" + sortBy + '\'' +
                ", sortOrientation=" + sortOrientation +
                ", presentation=" + presentation +
                ", publicSearchOnTenant=" + publicSearchOnTenant +
                ", filters=" + filters +
                '}';
    }

}
