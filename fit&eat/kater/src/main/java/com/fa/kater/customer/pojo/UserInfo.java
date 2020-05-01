package com.fa.kater.customer.pojo;

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
 * @since 2020-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花算法生成id号
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 用户账户
     */
    //@TableId(value = "user_account", type = IdType.ID_WORKER_STR)
    private String userAccount;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private String gender;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String headimgUrl;

    /**
     * 当前积分
     */
    private Integer score;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 逻辑删除
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
