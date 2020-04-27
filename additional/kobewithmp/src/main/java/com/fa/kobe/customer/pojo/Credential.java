package com.fa.kobe.customer.pojo;

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
 * @author jxh
 * @since 2020-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Credential implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花算法生成id号
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 关联手机号
     */
    private String mobile;

    /**
     * 微信凭证
     */
    private String wxopenid;

    /**
     * 支付宝凭证
     */
    private String aliopenid;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 登录密码
     */
    private String loginPwd;

    /**
     * 是否逻辑删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
