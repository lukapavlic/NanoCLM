package si.um.feri.nanoclm.repo.vao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import si.um.feri.nanoclm.repo.dto.PostTenant;
import java.util.HashSet;
import java.util.Set;

/**
 * A tenant (client) has a separate set of contacts; client is operated by a set of users
 * Unique name is used to separate contacts into separate document collections
 */
@Document("tenants")
public class Tenant {

    public Tenant() {
    }

    public Tenant(String title) {
        this.title = title;
    }

    public Tenant(PostTenant pc) {
        mongoId =null;
        title=pc.title();
        tenantUniqueName=pc.uniqueName();
        if (pc.allowedUsers()!=null)
            getAllowedUsers().addAll(pc.allowedUsers());
    }

    @Id
    private String mongoId;
    private String title;
    private String tenantUniqueName;
    private Set<String> allowedUsers;

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }

    public String getTenantUniqueName() {
        return tenantUniqueName;
    }

    public void setTenantUniqueName(String tenantUniqueName) {
        this.tenantUniqueName = tenantUniqueName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getAllowedUsers() {
        if (allowedUsers==null) allowedUsers=new HashSet<>();
        return allowedUsers;
    }

    public void setAllowedUsers(Set<String> allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "mongoId='" + mongoId + '\'' +
                ", title='" + title + '\'' +
                ", tenantUniqueName='" + tenantUniqueName + '\'' +
                ", allowedUsers=" + allowedUsers +
                '}';
    }

}
