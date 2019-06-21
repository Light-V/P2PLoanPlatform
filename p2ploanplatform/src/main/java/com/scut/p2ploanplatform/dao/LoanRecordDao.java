package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.LoanRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zonghang
 * Date 2019/6/20 21:49
 */
@Repository
@Mapper
public interface LoanRecordDao {

    @Insert("insert into loan_record(record_id, purchase_id, borrower_id, investor_id, time, amount) " +
            "values (#{recordId}, #{purchaseId}, #{borrowerId}, #{investorId}, #{time}, #{amount})")
    int insert(LoanRecord loanRecord);

    @Select("select * from loan_record where borrower_id = #{borrowerId}")
    List<LoanRecord> findByBorrowerId(String borrowerId);

    @Select("select * from loan_record where investor_id = #{investorId}")
    List<LoanRecord> findByInvestorId(String investorId);
}
