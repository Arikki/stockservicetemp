package com.stockService.query.api.queries;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GetStocksQuery {

	private String companyCode;
	private Date startDate;
	private Date endDate;
}
