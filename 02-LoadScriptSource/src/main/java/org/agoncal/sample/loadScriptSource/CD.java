package org.agoncal.sample.loadScriptSource;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         https://github.com/agoncal
 *         --
 */
@Entity
public class CD {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Version
    @Column(name = "version")
    private int version;

    @Column(length = 50)
    private String title;

    private Float price;

    @Column(length = 3000)
    private String description;

    @Column(name = "total_duration")
    private Float totalDuration;

    @ManyToOne
    private MajorLabel label;

    @ManyToOne
    private Genre genre;

    @ManyToMany
    private Set<Musician> musicians = new HashSet<Musician>();

    // ======================================
    // =          Getters & Setters         =
    // ======================================

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Float totalDuration) {
        this.totalDuration = totalDuration;
    }

    public MajorLabel getLabel() {
        return this.label;
    }

    public void setLabel(final MajorLabel label) {
        this.label = label;
    }

    public Genre getGenre() {
        return this.genre;
    }

    public void setGenre(final Genre genre) {
        this.genre = genre;
    }

    public Set<Musician> getMusicians() {
        return this.musicians;
    }

    public void setMusicians(final Set<Musician> musicians) {
        this.musicians = musicians;
    }

    // ======================================
    // =         hash, equals, toString     =
    // ======================================

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CD)) {
            return false;
        }
        CD other = (CD) obj;
        if (id != null) {
            if (!id.equals(other.id)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (title != null && !title.trim().isEmpty())
            result += "title: " + title;
        if (price != null)
            result += ", price: " + price;
        if (description != null && !description.trim().isEmpty())
            result += ", description: " + description;
        if (totalDuration != null)
            result += ", totalDuration: " + totalDuration;
        return result;
    }
}