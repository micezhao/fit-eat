package com.fa.kater.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2020-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDict implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id")
    private Long id;

    private Long pid;

    /**
     * 数据类别
     */
    @TableField(value = "`group`" )
    private String group;

    /**
     * 数据类型
     */
    @TableField(value = "`key`" )
    private String key;

    /**
     * 数据类型名称
     */
    private String keyName;

    /**
     * 数据类型对应的值
     */
    private String value;

    /**
     * 数据类型值名称
     */
    private String valueName;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 0：可用 / 1：不可用
     */
    private String dr;

    /**
     * 0：未删除 / 1:已删除
     */
    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
