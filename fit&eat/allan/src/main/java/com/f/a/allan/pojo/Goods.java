package com.f.a.allan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
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

public class Goods extends Model<Goods> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    
    private Long id;

    /**
     * 商品编号
     */
    private String goodsId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 所属商户编号
     */
    private String merchantNo;

    /**
     * 商品分类
     */
    private String type;

    /**
     * 商品价格
     */
    private String price;

    /**
     * 商户状态
     */
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
