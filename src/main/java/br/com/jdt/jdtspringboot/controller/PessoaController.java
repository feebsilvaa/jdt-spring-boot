package br.com.jdt.jdtspringboot.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import br.com.jdt.jdtspringboot.model.entity.Pessoa;
import br.com.jdt.jdtspringboot.model.entity.Profissao;
import br.com.jdt.jdtspringboot.model.entity.Telefone;
import br.com.jdt.jdtspringboot.model.enums.SexoEnum;
import br.com.jdt.jdtspringboot.service.PessoaService;
import br.com.jdt.jdtspringboot.service.ProfissaoService;
import br.com.jdt.jdtspringboot.service.ReportService;
import br.com.jdt.jdtspringboot.service.TelefoneService;
import net.sf.jasperreports.engine.JRException;

@Controller
@RequestMapping("/pessoas")
public class PessoaController {
	
	private final static Logger log = LoggerFactory.getLogger(PessoaController.class);

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private TelefoneService telefoneService;

	@Autowired
	private ReportService reportService;
	
	@Autowired
	private ProfissaoService profissaoService;
	
	@GetMapping
	public ModelAndView homePessoas() {
		ModelAndView viewPessoas = new ModelAndView("pessoas/home");
		List<Pessoa> pessoas = this.pessoaService.listar();
		viewPessoas.addObject("pessoas", pessoas);
		return viewPessoas;
	}

	@GetMapping("/cadastro")
	public ModelAndView cadastro() {
		return this.setupViewCadastro(null);
	}

	@PostMapping("/salvar")
	public Object salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.error("Erro de validação do formulário.");
			List<String> messages = new ArrayList<String>();
			for (ObjectError error: bindingResult.getAllErrors()) {
				messages.add(error.getDefaultMessage());
			}
			
			// 
			Map<String, Object> params = Stream.of(new Object[][] {
				{ "messages", messages },
				{ "pessoaForm", pessoa }
			}).collect(Collectors.toMap(data -> (String) data[0], data -> data[1]));
			
			return this.setupViewCadastro(params);
		}
		this.pessoaService.salvar(pessoa);
		return new RedirectView("/pessoas", true);
	}

	@GetMapping("/editar/{id}")
	public ModelAndView edicao(@PathVariable Long id) {
		return this.setupViewEdicao(id, null);
	}

	@PostMapping("/editar/{id}")
	public Object editar(@PathVariable Long id, @Valid Pessoa pessoa, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.error("Erro de validação do formulário.");
			List<String> messages = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				messages.add(error.getDefaultMessage());
			}
			
			// 
			Map<String, Object> params = Stream.of(new Object[][] {
				{ "messages", messages },
				{ "pessoaForm", pessoa }
			}).collect(Collectors.toMap(data -> (String) data[0], data -> data[1]));
			
			return this.setupViewEdicao(null, params);
		}
		this.pessoaService.editar(id, pessoa);
		return new RedirectView("/pessoas", true);
	}

	@GetMapping("/remover/{id}")
	public RedirectView remover(@PathVariable Long id) {
		this.pessoaService.remover(id);
		return new RedirectView("/pessoas", true);
	}
	
	@GetMapping("/buscaPorNome")
	public ModelAndView buscaPorNome(@RequestParam("buscaNome") String nome) {
		ModelAndView mav = new ModelAndView("/pessoas/home");
		List<Pessoa> pessoasPorNome = this.pessoaService.buscaPorNome(nome);
		mav.addObject("pessoas", pessoasPorNome);
		return mav;
	}
	
	@PostMapping("/busca")
	public ModelAndView busca(@RequestParam("buscaGeral") String query) {
		ModelAndView mav = new ModelAndView("/pessoas/home");
		List<Pessoa> pessoasPorNome = this.pessoaService.buscaPorQualquerParametro(query);
		mav.addObject("pessoas", pessoasPorNome);
		return mav;
	}
	
	@GetMapping("/busca")
	public ResponseEntity<Resource> exportarPessoasPdf(@RequestParam("buscaGeral") String query, HttpServletRequest request) throws IOException {
		try {
			byte[] pdfReport = this.reportService.exportarPdfPessoas(query, request.getServletContext());
			String fileName = "export-pessoas";
			Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "force-download"));
			headers.set("Content-Disposition", String.format("attachment; filename=%s-%s.pdf", fileName, timestamp));
			headers.setContentLength(pdfReport.length);
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(new ByteArrayResource(pdfReport));
		} catch (JRException e) {
			log.error("Ocorreu um erro ao tentar extrair o relatório de pessoas - Erro: {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/detalhes/{id}")
	public ModelAndView detalhes(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView("pessoas/detalhe-pessoa");
		Pessoa pessoa = this.pessoaService.buscarPorId(id).get();
		mav.addObject("pessoa", pessoa);
		List<Telefone> telefones = this.telefoneService.listarPorPessoa(id);
		mav.addObject("telefones", telefones);
		return mav;
	}

	/**
	 * Método que seta os objetos padrão da view de cadastro, e recebe parametros não obrigatorios
	 * @param params
	 * @return
	 */
	private ModelAndView setupViewCadastro(Map<String, Object> params) {
		ModelAndView mav = new ModelAndView("pessoas/cadastro");
		List<SexoEnum> sexoList = Arrays.asList(SexoEnum.values());
		List<Profissao> profissoes = this.profissaoService.listarProfissoes();
		mav.addObject("sexoList", sexoList);
		mav.addObject("profissoes", profissoes);
		if (params != null)
			mav.addAllObjects(params);
		return mav;
	}
	
	/**
	 * Método que seta os objetos padrão da view de cadastro, e recebe paramatros nao obrigatorios
	 * @param id
	 * @return
	 */
	private ModelAndView setupViewEdicao(Long id, Map<String, Object> params) {
		ModelAndView mav = new ModelAndView("pessoas/editar");
		List<SexoEnum> sexoList = Arrays.asList(SexoEnum.values());
		List<Profissao> profissoes = this.profissaoService.listarProfissoes();
		mav.addObject("sexoList", sexoList);
		mav.addObject("profissoes", profissoes);
		if (id != null)
			mav.addObject("pessoa", this.pessoaService.buscarPorId(id).get());
		if (params != null)
			mav.addAllObjects(params);
		return mav;
	}
}
