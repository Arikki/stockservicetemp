package com.stockService.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data

@Builder
@AllArgsConstructor
@RequiredArgsConstructor

public class StockWriteModel {

	private int id;
	private String companyCode;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private BigDecimal stockPrice;
}
