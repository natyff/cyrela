package com.cyrela.poc.listener;

import com.cyrela.poc.processor.SellItemProcessor;
import com.cyrela.poc.processor.FinancialPositionItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger logger = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("!!! GENERATING REPORTS DATA");
			generateFinancialPositionDataReport();
			logger.info("!!! FINANTIAL DATA POSITION GENERATED");
			generateSellDataReport();
			logger.info("!!! SELLS DATA GENERATED");
		}
	}

	private void generateSellDataReport() {
		try {
			StringBuilder sb = new StringBuilder();
			SellItemProcessor.exit.forEach(each -> {
				sb.append(each);
			});
			OutputStreamWriter var = new OutputStreamWriter(new FileOutputStream(".\\vendas.csv"), StandardCharsets.UTF_8);
			var.write(sb.toString());


		} catch (IOException e) {
			logger.error("something went wrong on sell data generate ", e.getMessage());
		}
	}

	private void generateFinancialPositionDataReport() {
		try {
			FileWriter writer = new FileWriter(".\\finantial_position.csv");
			StringBuilder sb = new StringBuilder();
			FinancialPositionItemProcessor.exit.forEach(each -> {
				sb.append(each);
			});
			writer.write(sb.toString());
			writer.close();
		} catch (IOException e) {
			logger.error("something went wrong on finantial position data generate ", e.getMessage());
		}
	}
}
