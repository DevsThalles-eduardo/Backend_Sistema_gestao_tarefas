package jala.University.Tarefas.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum StatusTarefa {
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em_andamento"),
    CONCLUIDO("Concluido");

    private String descricao;
}
