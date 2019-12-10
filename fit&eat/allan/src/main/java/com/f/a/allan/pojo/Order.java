package com.f.a.allan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author micezhao
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`Order`") // 如果表名或者字段名有关键字，就通过注解解决
public class Order extends Model<Order> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 用户编号
     */
    private String customerId;

    /**
     * 订单总价
     */
    private String total;

    /**
     * 优惠金额
     */
    private String discountPrice;

    /**
     * 结算金额
     */
    private String settlementPrice;

    /**
     * 下单时间
     */
    private LocalDateTime orderTime;

    /**
     * 订单过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 订单关闭时间
     */
    private LocalDateTime closeTime;

    /**
     * 订单完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 订单状态
     */
    @TableField("`status`")
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime cdt;

    /**
     * 修改时间
     */
    private LocalDateTime mdt;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
