package Model;

import jakarta.persistence.*;

@Entity
@Table(name = "subjects")

@NamedQuery(
        name = "Subject.findAll",
        query = "SELECT S FROM Subject S"
)
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_subject", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Subject(String nombre, Integer id) {
        this.nombre = nombre;
        this.id = id;
    }
    public Subject() {
    }

    @Override
    public String toString() {
        return nombre;
    }
}