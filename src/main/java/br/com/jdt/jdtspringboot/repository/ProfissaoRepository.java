package br.com.jdt.jdtspringboot.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.jdt.jdtspringboot.model.entity.Profissao;

@Transactional
@Repository
public interface ProfissaoRepository extends JpaRepository<Profissao, Long>{

}
