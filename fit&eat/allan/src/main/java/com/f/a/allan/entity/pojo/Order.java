package com.f.a.allan.entity.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单处理对象
 * </p>
 *
 * @author micezhao
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "`order`")
public class Order extends Model<Order>  {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 用户编号
     */
    private String userAccount;

    /**
     * 商品编号
     */
    private String goodsId;

    /**
     * 商品类型
     */
    private String category;

    /**
     * 商品数量
     */
    private Integer num;

    /**
     * 应付总价
     */
    private Integer price;

    /**
     * 优惠价格
     */
    private Integer discountPrice;

    /**
     * 结算价格
     */
    private Integer settlementPrice;

    /**
     * 商户号
     */
    private String merchantId;

    /**
     * 订单状态
     */
    @TableField(value = "`status`" )
    private String status;

    /**
     * 订单创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime orderTime;

    /**
     * 订单完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime cdt;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime mdt;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
