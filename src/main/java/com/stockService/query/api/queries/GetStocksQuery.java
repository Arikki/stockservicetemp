package com.stockService.query.api.queries;

import java.util.Date;

import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GetStocksQuery {

	private String companyCode;
	private String startDate;
	private String endDate;
	private int pageNo;
	private int size;
}
