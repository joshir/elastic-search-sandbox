package com.joshir.els;

import com.joshir.els.client.OrderClient;
import com.joshir.els.domain.OrderIndex;
import com.joshir.els.mapper.JsonMapperHelper;
import com.joshir.els.mapper.exceptions.MappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@SpringBootApplication
@Slf4j
public class ElsApplication implements CommandLineRunner {
	private final Resource _data;
	private final OrderClient orderIndexingClient;

	public ElsApplication(@Value("classpath:data/data.json") Resource data,
												OrderClient orderIndexingClient) {
		_data = data;
		this.orderIndexingClient = orderIndexingClient;
	}

	public static void main(String[] args) {
		SpringApplication.run(ElsApplication.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("loading data from resource file ...");
		List<OrderIndex> orders = null;
		try {
			orders = JsonMapperHelper
				.parseJsonArray(_data.getContentAsString(Charset.defaultCharset()), OrderIndex.class);
		} catch (ClassNotFoundException e) {
			throw new MappingException("Class OrderIndex not found", e);
		} catch (IOException e) {
			throw new MappingException("Error loading data from resource file", e);
		}
		log.info("successfully loaded data from resource file ...");
		orderIndexingClient.save(orders);
	}
}
