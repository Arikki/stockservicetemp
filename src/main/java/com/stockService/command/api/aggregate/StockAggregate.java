package com.stockService.command.api.aggregate;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stockService.command.api.commands.CreateStockCommand;
import com.stockService.command.api.commands.DeleteStockCommand;
import com.stockService.command.api.events.StockCreatedEvent;
import com.stockService.command.api.events.StockDeletedEvent;
import com.stockService.query.api.projection.StockProjection;

@Aggregate
public class StockAggregate {
	
	@AggregateIdentifier
	private String uuid;
	private String companyCode;
	private BigDecimal stockPrice;
	private Long numberOfHours;

	public StockAggregate() {

	}

	@CommandHandler
	public StockAggregate(CreateStockCommand createStockCommand) {
		// perform validations here
		StockCreatedEvent stockCreatedEvent = StockCreatedEvent.builder().uuid(createStockCommand.getUuid())
				.companyCode(createStockCommand.getCompanyCode()).stockPrice(createStockCommand.getStockPrice())
				.numberOfHours(createStockCommand.getNumberOfHours()).build();

		AggregateLifecycle.apply(stockCreatedEvent);
	}

	@CommandHandler
	public StockAggregate(DeleteStockCommand deleteStockCommand) {
		// perform validations here

		StockDeletedEvent stockDeletedEvent = StockDeletedEvent.builder().uuid(deleteStockCommand.getUuid())
				.companyCode(deleteStockCommand.getCompanyCode()).build();
		AggregateLifecycle.apply(stockDeletedEvent);
	}

	@EventSourcingHandler
	public void on(StockCreatedEvent stockCreatedEvent) {

		this.companyCode = stockCreatedEvent.getCompanyCode();
		this.stockPrice = stockCreatedEvent.getStockPrice();
		this.uuid = stockCreatedEvent.getUuid();
		this.numberOfHours = stockCreatedEvent.getNumberOfHours();

	}

	@EventSourcingHandler
	public void on(StockDeletedEvent stockDeletedEvent) {

		this.companyCode = stockDeletedEvent.getCompanyCode();

		this.uuid = stockDeletedEvent.getUuid();

	}
}
