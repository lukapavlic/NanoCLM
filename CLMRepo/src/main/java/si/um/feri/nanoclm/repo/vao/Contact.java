package si.um.feri.nanoclm.repo.vao;

import org.springframework.data.annotation.Id;
import si.um.feri.nanoclm.repo.dto.PostContact;
import java.util.*;

/**
 * A contact holds a set of attributes (tags), key-values and comments
 */
public class Contact {

    public Contact() {
    }

    public Contact(PostContact pc) {
        uniqueId=null;
        mongoId=null;
        title=pc.title();
        getAttrs().addAll(pc.attrs());
        getProps().putAll(pc.props());
        getComments().putAll(pc.comments());
    }

    public Contact(String title) {
        this.title = title;
    }

    private static Random rand=new Random();

    public void generateUniqueId(String belongingToTenant) {
        //TODO improve this
        setUniqueId(belongingToTenant+"-"+getTitle().hashCode()+"-"+rand.nextInt(1000));
    }

    @Id
    private String mongoId;
    private String uniqueId;
    private String title;
    private Map<String, String> props;
    private Set<String> attrs;
    private Map<String,String> comments;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, String> getProps() {
        if (props==null) props=new HashMap<>();
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    public Set<String> getAttrs() {
        if (attrs==null) attrs=new HashSet<>();
        return attrs;
    }

    public void setAttrs(Set<String> attrs) {
        this.attrs = attrs;
    }

    public Map<String, String> getComments() {
        if (comments==null) comments=new HashMap<>();
        return comments;
    }

    public void setComments(Map<String, String> comments) {
        this.comments = comments;
    }

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "mongoId='" + mongoId + '\'' +
                ", uniqueId='" + uniqueId + '\'' +
                ", title='" + title + '\'' +
                ", props=" + props +
                ", attrs=" + attrs +
                ", comments=" + comments +
                '}';
    }
}
