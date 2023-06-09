package com.joshir.els;

import com.joshir.els.client.OrderClient;
import com.joshir.els.domain.OrderIndex;
import com.joshir.els.exceptions.MappingException;
import com.joshir.els.mapper.JsonMapperHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootApplication
@Slf4j
public class ElsApplication implements CommandLineRunner {
	private final Resource _data;
	private final OrderClient orderIndexingClient;

	public ElsApplication(@Value("classpath:data/data.json") Resource data, OrderClient orderIndexingClient) {
		_data = data;
		this.orderIndexingClient = orderIndexingClient;
	}

	public static void main(String[] args) {
		SpringApplication.run(ElsApplication.class, args);
	}

	@Override
	public void run(String... args) throws MappingException {
		log.info("loading data from resource file ...");
		List<OrderIndex> orders = null;
		try {
			orders = JsonMapperHelper.parseJsonArray (_data.getContentAsString(Charset.defaultCharset()), OrderIndex.class);
		} catch (IOException e) {
			throw new MappingException("error converting from ByteArray representation of json resource to String" , e);
		}
		orderIndexingClient
			.save(orders)
			.forEach(order -> log.info("order id: {} saved",order));
	}
}
