package com.f.a.kobe.mapper.previous;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.f.a.kobe.pojo.previous.CustomerBodyInfo;
import com.f.a.kobe.pojo.previous.CustomerBodyInfoExample;
@Mapper
public interface CustomerBodyInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_body_info
     *
     * @mbggenerated
     */
    int countByExample(CustomerBodyInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_body_info
     *
     * @mbggenerated
     */
    int deleteByExample(CustomerBodyInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_body_info
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_body_info
     *
     * @mbggenerated
     */
    int insert(CustomerBodyInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_body_info
     *
     * @mbggenerated
     */
    int insertSelective(CustomerBodyInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_body_info
     *
     * @mbggenerated
     */
    List<CustomerBodyInfo> selectByExample(CustomerBodyInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_body_info
     *
     * @mbggenerated
     */
    CustomerBodyInfo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_body_info
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CustomerBodyInfo record, @Param("example") CustomerBodyInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_body_info
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CustomerBodyInfo record, @Param("example") CustomerBodyInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_body_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CustomerBodyInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_body_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CustomerBodyInfo record);
}