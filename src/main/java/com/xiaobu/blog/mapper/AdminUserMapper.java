package com.xiaobu.blog.mapper;

import com.xiaobu.blog.model.AdminUser;
import com.xiaobu.blog.model.AdminUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminUserMapper {
    AdminUser _selectByUsername(String username);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_admin
     *
     * @mbg.generated Mon Mar 23 10:10:15 CST 2020
     */
    long countByExample(AdminUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_admin
     *
     * @mbg.generated Mon Mar 23 10:10:15 CST 2020
     */
    int deleteByExample(AdminUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_admin
     *
     * @mbg.generated Mon Mar 23 10:10:15 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_admin
     *
     * @mbg.generated Mon Mar 23 10:10:15 CST 2020
     */
    int insert(AdminUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_admin
     *
     * @mbg.generated Mon Mar 23 10:10:15 CST 2020
     */
    int insertSelective(AdminUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_admin
     *
     * @mbg.generated Mon Mar 23 10:10:15 CST 2020
     */
    List<AdminUser> selectByExample(AdminUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_admin
     *
     * @mbg.generated Mon Mar 23 10:10:15 CST 2020
     */
    AdminUser selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_admin
     *
     * @mbg.generated Mon Mar 23 10:10:15 CST 2020
     */
    int updateByExampleSelective(@Param("record") AdminUser record, @Param("example") AdminUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_admin
     *
     * @mbg.generated Mon Mar 23 10:10:15 CST 2020
     */
    int updateByExample(@Param("record") AdminUser record, @Param("example") AdminUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_admin
     *
     * @mbg.generated Mon Mar 23 10:10:15 CST 2020
     */
    int updateByPrimaryKeySelective(AdminUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_admin
     *
     * @mbg.generated Mon Mar 23 10:10:15 CST 2020
     */
    int updateByPrimaryKey(AdminUser record);

}