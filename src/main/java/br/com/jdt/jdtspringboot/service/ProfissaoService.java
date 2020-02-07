package br.com.jdt.jdtspringboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdt.jdtspringboot.model.entity.Profissao;
import br.com.jdt.jdtspringboot.repository.ProfissaoRepository;

@Service
public final class ProfissaoService {

	@Autowired
	private ProfissaoRepository profissaoRepository;

	public List<Profissao> listarProfissoes() {
		return this.profissaoRepository.findAll();
	}
	
}
