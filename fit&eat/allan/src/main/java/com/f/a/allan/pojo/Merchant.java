package com.f.a.allan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Merchant extends Model<Merchant> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商户编号
     */
    private String merchantNo;

    /**
     * 商户名称
     */
    private String name;

    /**
     * 注册时间
     */
    private String registerDate;

    /**
     * 商户类型
     */
    private String type;

    /**
     * 联系人
     */
    private String contacter;

    /**
     * 联系电话
     */
    private String contactPhone;

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
