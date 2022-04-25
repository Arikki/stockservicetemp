package com.stockService.data;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

	@Query("FROM Stock WHERE DATE(end_date_time) <= :endDate AND DATE(start_date_time) >= :startDate AND company_code = :companyCode")
	List<Stock> findAllStockWithDateRange(@Param("endDate") Date endDate, @Param("startDate") Date startDate,
			@Param("companyCode") String companyCode);

	Long deleteByCompanyCode(String companyCode);

	List<Stock> findAllByCompanyCodeOrderByStartDateTimeDesc(String companyCode);

	@Query(value = "select * from Stock t inner join (select company_code, max(start_date_time) as MaxDate from stock group by company_code) tm on t.company_code = tm.company_code and t.start_date_time = tm.MaxDate", nativeQuery = true)
	List<Stock> findLatestStockPriceForAll();
}
