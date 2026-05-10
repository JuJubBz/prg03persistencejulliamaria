package br.com.ifba.infrastructure.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import br.com.ifba.infrastructure.entity.PersistenceEntity;

/**
 * Implementação genérica do DAO usando JPA.
 * 
 * <Entity> -> qualquer classe que herde de PersistenceEntity 
 * @param <Entity>
 */
@SuppressWarnings("unchecked")
public class GenericIDao<Entity extends PersistenceEntity>
        implements GenericDao<Entity> {

    /**
     * Responsável por gerenciar as operações com o banco
     */
    protected static EntityManager entityManager;

    /**
     * Bloco estático:
     * Executa UMA VEZ quando a classe é carregada
     * 
     * Cria a conexão com o banco usando o persistence.xml
     */
    static {
        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("gerenciamento_curso");

        entityManager = factory.createEntityManager();
    }

    /**
     * SALVAR entidade no banco
     * @param entity
     * @return 
     */
    @Override
    public Entity save(Entity entity) {

        entityManager.getTransaction().begin(); // inicia transação
        entityManager.persist(entity);          // salva no banco
        entityManager.getTransaction().commit(); // confirma

        return entity;
    }

    /**
     * ATUALIZAR entidade existente
     * @param entity
     * @return 
     */
    @Override
    public Entity update(Entity entity) {

        entityManager.getTransaction().begin();
        entityManager.merge(entity); // atualiza os dados
        entityManager.getTransaction().commit();

        return entity;
    }

    /**
     * DELETAR entidade
     * @param entity
     */
    @Override
    public void delete(Entity entity) {

        // Garante que a entidade está sendo gerenciada pelo JPA
        entity = findById(entity.getId());

        entityManager.getTransaction().begin();
        entityManager.remove(entity); // remove do banco
        entityManager.getTransaction().commit();
    }

    /**
     * LISTAR todas as entidades (Curso, Turma, etc)
     * @return 
     */
    @Override
    public List<Entity> findAll() {

        /**
         * Criamos uma query dinâmica baseada no tipo da entidade
         * Ex: "from Curso" ou "from Turma"
         */
        TypedQuery<Entity> query = entityManager.createQuery(
                "from " + getTypeClass().getName(),
                getTypeClass()
        );

        return query.getResultList();
    }

    /**
     * BUSCAR entidade pelo ID
     * @param id
     * @return 
     */
    @Override
    public Entity findById(Long id) {

        // Agora funciona corretamente porque o tipo está certo
        return entityManager.find(getTypeClass(), id);
    }

    /**
     * Descobre automaticamente o tipo da entidade (Curso, Turma, etc)
     * 
     * Parte mais importante do Generic DAO
     * Usa reflexão para pegar o tipo em tempo de execução
     */
    private Class<Entity> getTypeClass() {

        return (Class<Entity>) ((ParameterizedType)
                this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }
}