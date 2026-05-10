package br.com.ifba.infrastructure.dao;

import java.util.List;
import br.com.ifba.infrastructure.entity.PersistenceEntity;

/**
 * Interface genérica para operações básicas de banco de dados.
 * 
 * <Entity> -> qualquer classe que herde de PersistenceEntity
 */
public interface GenericDao<Entity extends PersistenceEntity> {

    /**
     * Salva uma entidade no banco
     */
    Entity save(Entity entity);

    /**
     * Atualiza uma entidade existente
     */
    Entity update(Entity entity);

    /**
     * Remove uma entidade
     */
    void delete(Entity entity);

    /**
     * Busca todas as entidades
     */
    List<Entity> findAll();

    /**
     * Busca por ID
     */
    Entity findById(Long id);
}