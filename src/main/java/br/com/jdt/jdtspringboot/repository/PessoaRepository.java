package br.com.jdt.jdtspringboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.jdt.jdtspringboot.model.entity.Pessoa;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	List<Pessoa> findByNomeContainingOrSobrenomeContaining(String nome, String sobrenome);

	@Query(""
			+ "select p "
			+ "from Pessoa p "
			+ "where (:query is null or p.nome like %:query%) "
			+ "or (p.sobrenome like %:query%) "
			+ "or (p.sexo like %:query%) ")
	List<Pessoa> buscaPorQualquerParametro(String query);
	
	@Query(""
			+ "from Pessoa p "
			+ "order by p.id desc")
	List<Pessoa> listarOrdenadoPorId();

}
