package org.adapt.domain;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A LineItem.
 */
@Entity
@Table(name = "line_item")
public class LineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "link")
    private String link;

    @Column(name = "categories")
    private String categories;

    @Column(name = "roles")
    private String roles;

    @Column(name = "jhi_desc")
    private String desc;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "type")
    private String type;
    
    @Transient()
    private TypeEnum typeEnum;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public LineItem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public LineItem link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategories() {
        return categories;
    }

    public LineItem categories(String categories) {
        this.categories = categories;
        return this;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getRoles() {
        return roles;
    }

    public LineItem roles(String roles) {
        this.roles = roles;
        return this;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getDesc() {
        return desc;
    }

    public LineItem desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public LineItem viewCount(Integer viewCount) {
        this.viewCount = viewCount;
        return this;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getType() {
    	type=TypeEnum.getEnumbyString(type).toString();
        return type;
    }

    public LineItem type(String type) {
    	type=TypeEnum.getEnumbyString(type).toString();
        this.type = type;
        return this;
    }

    public void setType(String type) {
    	type=TypeEnum.getEnumbyString(type).toString();
        this.type = type;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LineItem)) {
            return false;
        }
        return id != null && id.equals(((LineItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "LineItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", link='" + getLink() + "'" +
            ", categories='" + getCategories() + "'" +
            ", roles='" + getRoles() + "'" +
            ", desc='" + getDesc() + "'" +
            ", viewCount=" + getViewCount() +
            ", type='" + getType() + "'" +
            "}";
    }
}
