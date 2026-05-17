package Model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_booking", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_mentorship", nullable = false)
    private Mentorship codMentorship;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_alumno", nullable = false)
    private User codAlumno;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;

    @ColumnDefault("'pendiente'")
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Mentorship getCodMentorship() {
        return codMentorship;
    }

    public void setCodMentorship(Mentorship codMentorship) {
        this.codMentorship = codMentorship;
    }

    public User getCodAlumno() {
        return codAlumno;
    }

    public void setCodAlumno(User codAlumno) {
        this.codAlumno = codAlumno;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}