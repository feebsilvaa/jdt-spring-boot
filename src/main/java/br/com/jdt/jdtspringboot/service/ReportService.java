package br.com.jdt.jdtspringboot.service;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class ReportService implements Serializable {

	private static final long serialVersionUID = 2905103560315002138L;
	
	@Autowired
	private PessoaService pessoaService;
	
	public byte[] exportarPdfPessoas(ServletContext context) throws JRException {
		return this.pdfReport(this.pessoaService.listar(),"pessoa", context);
	}
	
	public byte[] exportarPdfPessoas(String query, ServletContext context) throws JRException {
		return this.pdfReport(this.pessoaService.buscaPorQualquerParametro(query), "pessoa", context);
	}
	
	/**
	 * Gera relatorio e exporta em pdf
	 * @return
	 * @throws JRException 
	 */
	private byte[] pdfReport(List<?> dados, String relatorio, ServletContext context) throws JRException {
		// cria a lista de dados para o relatório
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);
		// Carrega o caminho do arquivo jasper compilado
		String caminhoJasper = context.getRealPath("relatorios") + File.separator + relatorio + ".jasper";
		// Carrega o arquivo jasper passando os dados
		JasperPrint jasperPrint = JasperFillManager.fillReport(caminhoJasper, new HashMap<String, Object>(), dataSource);
		// Exporta para bytes para fazer download do pdf
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	
}
