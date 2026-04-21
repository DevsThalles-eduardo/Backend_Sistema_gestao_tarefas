package jala.University.Tarefas.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prioridade")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Prioridade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prioridade")
    private Long id;

    @Column(nullable = false, length = 25)
    private String nivel;

    @OneToMany(mappedBy = "prioridade", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Tarefa> Tarefas;
}
