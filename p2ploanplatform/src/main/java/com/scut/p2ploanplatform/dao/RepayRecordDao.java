package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.RepayRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zonghang
 * Date 2019/6/20 23:38
 */
@Repository
@Mapper
public interface RepayRecordDao {

    @Insert("insert into repay_record(record_id, plan_id, purchase_id, payer_id, payee_id, time, amount) " +
            "values (#{recordId}, #{planId}, #{purchaseId}, #{payerId}, #{payeeId}, #{time}, #{amount})")
    int insert(RepayRecord repayRecord);

    @Select("select * from repay_record where payee_id = #{payeeId}")
    List<RepayRecord> findByPayeeId(String payeeId);

    @Select("select * from repay_record where payer_id = #{payerId}")
    List<RepayRecord> findByPayerId(String payerId);
}
