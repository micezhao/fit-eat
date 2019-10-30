package com.f.a.kobe.pojo;

import java.io.Serializable;
import java.util.Date;

public class CustomerBaseInfo implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.customer_id
     *
     * @mbg.generated
     */
    private Long customerId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.realname
     *
     * @mbg.generated
     */
    private String realname;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.birthday
     *
     * @mbg.generated
     */
    private String birthday;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.age
     *
     * @mbg.generated
     */
    private Integer age;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.nickname
     *
     * @mbg.generated
     */
    private String nickname;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.headimg
     *
     * @mbg.generated
     */
    private String headimg;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.sorce
     *
     * @mbg.generated
     */
    private Integer sorce;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.mobile
     *
     * @mbg.generated
     */
    private String mobile;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.ext1
     *
     * @mbg.generated
     */
    private String ext1;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.ext2
     *
     * @mbg.generated
     */
    private String ext2;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.ext3
     *
     * @mbg.generated
     */
    private String ext3;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.ext4
     *
     * @mbg.generated
     */
    private String ext4;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.ext5
     *
     * @mbg.generated
     */
    private String ext5;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.dr
     *
     * @mbg.generated
     */
    private String dr;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.cdt
     *
     * @mbg.generated
     */
    private Date cdt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_baseinfo.mdt
     *
     * @mbg.generated
     */
    private Date mdt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table customer_baseinfo
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.id
     *
     * @return the value of customer_baseinfo.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.id
     *
     * @param id the value for customer_baseinfo.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.customer_id
     *
     * @return the value of customer_baseinfo.customer_id
     *
     * @mbg.generated
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.customer_id
     *
     * @param customerId the value for customer_baseinfo.customer_id
     *
     * @mbg.generated
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.realname
     *
     * @return the value of customer_baseinfo.realname
     *
     * @mbg.generated
     */
    public String getRealname() {
        return realname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.realname
     *
     * @param realname the value for customer_baseinfo.realname
     *
     * @mbg.generated
     */
    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.birthday
     *
     * @return the value of customer_baseinfo.birthday
     *
     * @mbg.generated
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.birthday
     *
     * @param birthday the value for customer_baseinfo.birthday
     *
     * @mbg.generated
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.age
     *
     * @return the value of customer_baseinfo.age
     *
     * @mbg.generated
     */
    public Integer getAge() {
        return age;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.age
     *
     * @param age the value for customer_baseinfo.age
     *
     * @mbg.generated
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.nickname
     *
     * @return the value of customer_baseinfo.nickname
     *
     * @mbg.generated
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.nickname
     *
     * @param nickname the value for customer_baseinfo.nickname
     *
     * @mbg.generated
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.headimg
     *
     * @return the value of customer_baseinfo.headimg
     *
     * @mbg.generated
     */
    public String getHeadimg() {
        return headimg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.headimg
     *
     * @param headimg the value for customer_baseinfo.headimg
     *
     * @mbg.generated
     */
    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? null : headimg.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.sorce
     *
     * @return the value of customer_baseinfo.sorce
     *
     * @mbg.generated
     */
    public Integer getSorce() {
        return sorce;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.sorce
     *
     * @param sorce the value for customer_baseinfo.sorce
     *
     * @mbg.generated
     */
    public void setSorce(Integer sorce) {
        this.sorce = sorce;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.mobile
     *
     * @return the value of customer_baseinfo.mobile
     *
     * @mbg.generated
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.mobile
     *
     * @param mobile the value for customer_baseinfo.mobile
     *
     * @mbg.generated
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.ext1
     *
     * @return the value of customer_baseinfo.ext1
     *
     * @mbg.generated
     */
    public String getExt1() {
        return ext1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.ext1
     *
     * @param ext1 the value for customer_baseinfo.ext1
     *
     * @mbg.generated
     */
    public void setExt1(String ext1) {
        this.ext1 = ext1 == null ? null : ext1.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.ext2
     *
     * @return the value of customer_baseinfo.ext2
     *
     * @mbg.generated
     */
    public String getExt2() {
        return ext2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.ext2
     *
     * @param ext2 the value for customer_baseinfo.ext2
     *
     * @mbg.generated
     */
    public void setExt2(String ext2) {
        this.ext2 = ext2 == null ? null : ext2.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.ext3
     *
     * @return the value of customer_baseinfo.ext3
     *
     * @mbg.generated
     */
    public String getExt3() {
        return ext3;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.ext3
     *
     * @param ext3 the value for customer_baseinfo.ext3
     *
     * @mbg.generated
     */
    public void setExt3(String ext3) {
        this.ext3 = ext3 == null ? null : ext3.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.ext4
     *
     * @return the value of customer_baseinfo.ext4
     *
     * @mbg.generated
     */
    public String getExt4() {
        return ext4;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.ext4
     *
     * @param ext4 the value for customer_baseinfo.ext4
     *
     * @mbg.generated
     */
    public void setExt4(String ext4) {
        this.ext4 = ext4 == null ? null : ext4.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.ext5
     *
     * @return the value of customer_baseinfo.ext5
     *
     * @mbg.generated
     */
    public String getExt5() {
        return ext5;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.ext5
     *
     * @param ext5 the value for customer_baseinfo.ext5
     *
     * @mbg.generated
     */
    public void setExt5(String ext5) {
        this.ext5 = ext5 == null ? null : ext5.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.dr
     *
     * @return the value of customer_baseinfo.dr
     *
     * @mbg.generated
     */
    public String getDr() {
        return dr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.dr
     *
     * @param dr the value for customer_baseinfo.dr
     *
     * @mbg.generated
     */
    public void setDr(String dr) {
        this.dr = dr == null ? null : dr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.cdt
     *
     * @return the value of customer_baseinfo.cdt
     *
     * @mbg.generated
     */
    public Date getCdt() {
        return cdt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.cdt
     *
     * @param cdt the value for customer_baseinfo.cdt
     *
     * @mbg.generated
     */
    public void setCdt(Date cdt) {
        this.cdt = cdt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_baseinfo.mdt
     *
     * @return the value of customer_baseinfo.mdt
     *
     * @mbg.generated
     */
    public Date getMdt() {
        return mdt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_baseinfo.mdt
     *
     * @param mdt the value for customer_baseinfo.mdt
     *
     * @mbg.generated
     */
    public void setMdt(Date mdt) {
        this.mdt = mdt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_baseinfo
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CustomerBaseInfo other = (CustomerBaseInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCustomerId() == null ? other.getCustomerId() == null : this.getCustomerId().equals(other.getCustomerId()))
            && (this.getRealname() == null ? other.getRealname() == null : this.getRealname().equals(other.getRealname()))
            && (this.getBirthday() == null ? other.getBirthday() == null : this.getBirthday().equals(other.getBirthday()))
            && (this.getAge() == null ? other.getAge() == null : this.getAge().equals(other.getAge()))
            && (this.getNickname() == null ? other.getNickname() == null : this.getNickname().equals(other.getNickname()))
            && (this.getHeadimg() == null ? other.getHeadimg() == null : this.getHeadimg().equals(other.getHeadimg()))
            && (this.getSorce() == null ? other.getSorce() == null : this.getSorce().equals(other.getSorce()))
            && (this.getMobile() == null ? other.getMobile() == null : this.getMobile().equals(other.getMobile()))
            && (this.getExt1() == null ? other.getExt1() == null : this.getExt1().equals(other.getExt1()))
            && (this.getExt2() == null ? other.getExt2() == null : this.getExt2().equals(other.getExt2()))
            && (this.getExt3() == null ? other.getExt3() == null : this.getExt3().equals(other.getExt3()))
            && (this.getExt4() == null ? other.getExt4() == null : this.getExt4().equals(other.getExt4()))
            && (this.getExt5() == null ? other.getExt5() == null : this.getExt5().equals(other.getExt5()))
            && (this.getDr() == null ? other.getDr() == null : this.getDr().equals(other.getDr()))
            && (this.getCdt() == null ? other.getCdt() == null : this.getCdt().equals(other.getCdt()))
            && (this.getMdt() == null ? other.getMdt() == null : this.getMdt().equals(other.getMdt()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_baseinfo
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCustomerId() == null) ? 0 : getCustomerId().hashCode());
        result = prime * result + ((getRealname() == null) ? 0 : getRealname().hashCode());
        result = prime * result + ((getBirthday() == null) ? 0 : getBirthday().hashCode());
        result = prime * result + ((getAge() == null) ? 0 : getAge().hashCode());
        result = prime * result + ((getNickname() == null) ? 0 : getNickname().hashCode());
        result = prime * result + ((getHeadimg() == null) ? 0 : getHeadimg().hashCode());
        result = prime * result + ((getSorce() == null) ? 0 : getSorce().hashCode());
        result = prime * result + ((getMobile() == null) ? 0 : getMobile().hashCode());
        result = prime * result + ((getExt1() == null) ? 0 : getExt1().hashCode());
        result = prime * result + ((getExt2() == null) ? 0 : getExt2().hashCode());
        result = prime * result + ((getExt3() == null) ? 0 : getExt3().hashCode());
        result = prime * result + ((getExt4() == null) ? 0 : getExt4().hashCode());
        result = prime * result + ((getExt5() == null) ? 0 : getExt5().hashCode());
        result = prime * result + ((getDr() == null) ? 0 : getDr().hashCode());
        result = prime * result + ((getCdt() == null) ? 0 : getCdt().hashCode());
        result = prime * result + ((getMdt() == null) ? 0 : getMdt().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_baseinfo
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", realname=").append(realname);
        sb.append(", birthday=").append(birthday);
        sb.append(", age=").append(age);
        sb.append(", nickname=").append(nickname);
        sb.append(", headimg=").append(headimg);
        sb.append(", sorce=").append(sorce);
        sb.append(", mobile=").append(mobile);
        sb.append(", ext1=").append(ext1);
        sb.append(", ext2=").append(ext2);
        sb.append(", ext3=").append(ext3);
        sb.append(", ext4=").append(ext4);
        sb.append(", ext5=").append(ext5);
        sb.append(", dr=").append(dr);
        sb.append(", cdt=").append(cdt);
        sb.append(", mdt=").append(mdt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}