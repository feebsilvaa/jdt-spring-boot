package br.com.jdt.jdtspringboot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import br.com.jdt.jdtspringboot.model.entity.Pessoa;
import br.com.jdt.jdtspringboot.model.entity.Telefone;
import br.com.jdt.jdtspringboot.service.PessoaService;
import br.com.jdt.jdtspringboot.service.TelefoneService;

@Controller
@RequestMapping("/telefones")
public class TelefoneController {

	private final static Logger log = LoggerFactory.getLogger(PessoaController.class);

	@Autowired
	private TelefoneService telefoneService;
	
	@Autowired
	private PessoaService pessoaService;

	@PostMapping("/adicionar/{idPessoa}")
	public Object adicionarTelefone(@PathVariable("idPessoa") Long id, @Valid Telefone telefone,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.error("Erro de validação do formulário.");
			ModelAndView mav = new ModelAndView("pessoas/detalhe-pessoa");
			Pessoa pessoa = this.pessoaService.buscarPorId(id).get();
			mav.addObject("pessoa", pessoa);
			List<String> messages = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				messages.add(error.getDefaultMessage());
			}
			mav.addObject("messages", messages);
			mav.addObject("telefoneForm", telefone);
			return mav;
		}
		log.info("Adicionando telefone: {}", telefone);
		this.telefoneService.adicionarTelefone(telefone, id);
		return new RedirectView("/pessoas/detalhes/" + id, true);
	}

	@GetMapping("/remover/{idTelefone}/{idPessoa}")
	public RedirectView removerTelefone(@PathVariable("idTelefone") Long idTelefone,
			@PathVariable("idPessoa") Long idPessoa) {
		log.info("Removendo telefone id: {}", idTelefone);
		this.telefoneService.removerTelefone(idTelefone);
		return new RedirectView("/pessoas/detalhes/" + idPessoa, true);
	}

}
