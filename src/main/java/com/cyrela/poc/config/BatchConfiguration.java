package com.cyrela.poc.config;

import com.cyrela.poc.domain.FinancialPosition;
import com.cyrela.poc.domain.Sell;
import com.cyrela.poc.listener.JobCompletionNotificationListener;
import com.cyrela.poc.processor.SellItemProcessor;
import com.cyrela.poc.processor.FinancialPositionItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public FlatFileItemReader<FinancialPosition> financialPositionReader() {
		return new FlatFileItemReaderBuilder<FinancialPosition>()
				.name("clientItemReader")
				.resource(new ClassPathResource("dados_Tabela_PosicaoFinanceira.csv"))
				.delimited()
				.delimiter(";")
				.names(new String[]{"obra","bloco","unidade","empresa","situacaoUnidade","dataVenda","valorVenda","dataLiberacaoChaves","formaPagamento","faseIncorporacao","dataCessao","dataDesembolso","dataEntregaInicial","dataHabiteSe","statusDistrato","dataChaves","indicePreChaves","indicePosChaves","debitoAutomatico","saldoDevedor","diasAtraso","valorAtraso","totalAtraso","cRM_ProcessamentoPendente","cRM_Operacao","cRM_PosicaoFinanceiraId","createdOn","modifiedOn","dataPrevisaoEntrega","valorPago","valorPagoAtualizado","tipoPagamento","dataQuitacao","valorQuitacao","lR_TipoContrato","lR_Saldo","lR_DataVencimento","lR_Codigo","lR_DataRenegociacao","pCVF_SaldoDevedor","pCVF_TotalAtraso","pCVU_SaldoDevedor","pCVU_TotalAtraso","pCVP_SaldoDevedor","pCVP_TotalAtraso","dEC_SaldoDevedor","dEC_TotalAtraso","mOD_SaldoDevedor","mOD_TotalAtraso","lIG_SaldoDevedor","lIG_TotalAtraso","tCS_SaldoDevedor","tCS_TotalAtraso","lOT_SaldoDevedor","lOT_TotalAtraso","cRM_ProcessamentoPendenteRepasse","valorTotalReceberObras","valorParcelaChaves","valorTotalPosObra","dataUltimaPrestacaoPaga","dataUltimaAlteracao"})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<FinancialPosition>() {{
					setTargetType(FinancialPosition.class);
				}})
				.build();
	}

	@Bean
	public FlatFileItemReader<Sell> sellReader() {
		return new FlatFileItemReaderBuilder<Sell>()
				.name("sellReader")
				.resource(new ClassPathResource("vendas.csv"))
				.delimited()
				.delimiter(";")
				.names(new String[]{"obra_cod","obra_descricao","marca","regional","carteira","vGV","data"})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Sell>() {{
					setTargetType(Sell.class);
				}})
				.build();
	}

	@Bean
	public FlatFileItemWriter itemWriter() {
		return  new FlatFileItemWriterBuilder<FinancialPosition>()
				.name("itemWriter")
				.resource(new FileSystemResource(".\\output.txt"))
				.lineAggregator(new PassThroughLineAggregator<>())
				.build();
	}

	@Bean
	public FinancialPositionItemProcessor financialPositionProcessor() {
		return new FinancialPositionItemProcessor();
	}

	@Bean
	public SellItemProcessor sellProcessor() {
		return new SellItemProcessor();
	}

	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1, Step step2) {
		return jobBuilderFactory.get("importUserJob")
			.incrementer(new RunIdIncrementer())
			.listener(listener)
			.flow(step1)
			.next(step2)
			.end()
			.build();
	}

	@Bean
	public Step step1(FlatFileItemWriter<FinancialPosition> writer) {
		return stepBuilderFactory.get("step1")
				.<FinancialPosition, FinancialPosition> chunk(1000)
				.reader(financialPositionReader())
				.processor(financialPositionProcessor())
				.writer(writer)
				.build();
	}

	@Bean
	public Step step2(FlatFileItemWriter<Sell> writer) {
		return stepBuilderFactory.get("step2")
				.<Sell, Sell> chunk(1000)
				.reader(sellReader())
				.processor(sellProcessor())
				.writer(writer)
				.build();
	}
}
