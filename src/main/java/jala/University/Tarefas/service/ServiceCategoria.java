package jala.University.Tarefas.service;

import jala.University.Tarefas.exception.BusinessException;
import jala.University.Tarefas.exception.ResourceNotFoundException;
import jala.University.Tarefas.model.Categoria;
import jala.University.Tarefas.repository.RepositoryCategoria;
import jala.University.Tarefas.repository.RepositoryTarefas;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceCategoria {

    private final RepositoryCategoria repositoryCategoria;
    private final RepositoryTarefas repositoryTarefas;

    public Categoria saveCategoria(Categoria categoria) {
        if (categoria.getTipoCategoria() == null || categoria.getTipoCategoria().isEmpty()) {
            throw new BusinessException("Campo Categoria deve estar preenchido");
        }
        return repositoryCategoria.save(categoria);
    }

    // Método para listar todas as categorias
    public List<Categoria> findAll() {
        return repositoryCategoria.findAll();
    }

    public Categoria findById(long idCategoria) {
        return repositoryCategoria.findById(idCategoria)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria nao encontrada"));
    }

    public Categoria resolveCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new BusinessException("Categoria deve ser informada.");
        }
        if (categoria.getId() > 0) {
            return findById(categoria.getId());
        }
        if (categoria.getTipoCategoria() != null && !categoria.getTipoCategoria().isBlank()) {
            return repositoryCategoria.findByTipoCategoriaIgnoreCase(categoria.getTipoCategoria().trim())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria nao encontrada"));
        }
        throw new BusinessException("Categoria deve ser informada.");
    }

    public Categoria updateCategoria(long idCategoria, Categoria novaCategoria) {
        Categoria categoriaAtualizada = findById(idCategoria);
        categoriaAtualizada.setTipoCategoria(novaCategoria.getTipoCategoria());
        return repositoryCategoria.save(categoriaAtualizada);
    }

    public void deletecategoria(Long idCategoria) {
        Categoria categoriaDeletada = findById(idCategoria);
        if (repositoryTarefas.countByCategoriaId(categoriaDeletada.getId()) > 0) {
            throw new BusinessException("Nao e possivel excluir uma categoria vinculada a tarefas.");
        }
        repositoryCategoria.deleteById(categoriaDeletada.getId());
    }
}
