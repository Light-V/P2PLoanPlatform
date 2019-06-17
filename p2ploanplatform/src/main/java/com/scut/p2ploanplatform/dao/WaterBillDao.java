package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.WaterBill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zonghang
 * Date 2019/6/14 15:15
 */
@Mapper
@Repository
public interface WaterBillDao {

    @Insert("insert into water_bill(water_bill_id, payee_id, payer_id, amount, mode, time) " +
            "values (#{waterBillId}, #{payeeId}, #{payerId}, #{amount}, #{mode}, #{time})")
    int insert(WaterBill waterBill);

    @Select("select * from water_bill where payee_id = #{payeeId}")
    List<WaterBill> findByPayeeId(String payeeId);

    @Select("select * from water_bill where payer_id = #{payerId}")
    List<WaterBill> findByPayerId(String payerId);

    @Select("select * from water_bill where mode = #{mode}")
    List<WaterBill> findByMode(Integer mode);
}
