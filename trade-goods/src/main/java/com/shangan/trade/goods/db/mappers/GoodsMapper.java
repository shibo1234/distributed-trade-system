package com.shangan.trade.goods.db.mappers;

import com.shangan.trade.goods.db.model.Goods;

public interface GoodsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods
     *
     * @mbg.generated Fri Oct 13 19:54:56 PDT 2023
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods
     *
     * @mbg.generated Fri Oct 13 19:54:56 PDT 2023
     */
    int insert(Goods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods
     *
     * @mbg.generated Fri Oct 13 19:54:56 PDT 2023
     */
    int insertSelective(Goods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods
     *
     * @mbg.generated Fri Oct 13 19:54:56 PDT 2023
     */
    Goods selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods
     *
     * @mbg.generated Fri Oct 13 19:54:56 PDT 2023
     */
    int updateByPrimaryKeySelective(Goods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods
     *
     * @mbg.generated Fri Oct 13 19:54:56 PDT 2023
     */
    int updateByPrimaryKey(Goods record);
}