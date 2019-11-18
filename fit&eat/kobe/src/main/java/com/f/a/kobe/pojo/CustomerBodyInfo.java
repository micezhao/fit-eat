package com.f.a.kobe.pojo;

import java.io.Serializable;
import java.util.Date;

public class CustomerBodyInfo implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.customer_id
     *
     * @mbggenerated
     */
    private Long customerId;
    
  
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.record_id
     *
     * @mbggenerated
     */
    private String recordId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.register_date
     *
     * @mbggenerated
     */
    private String registerDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.height
     *
     * @mbggenerated
     */
    private String height;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.weight
     *
     * @mbggenerated
     */
    private String weight;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.chest
     *
     * @mbggenerated
     */
    private String chest;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.waistline
     *
     * @mbggenerated
     */
    private String waistline;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.hipline
     *
     * @mbggenerated
     */
    private String hipline;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.waist_hip_ratio
     *
     * @mbggenerated
     */
    private String waistHipRatio;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.left_arm_circumference
     *
     * @mbggenerated
     */
    private String leftArmCircumference;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.right_arm_circumference
     *
     * @mbggenerated
     */
    private String rightArmCircumference;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.left_thigh_circumference
     *
     * @mbggenerated
     */
    private String leftThighCircumference;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.right_thigh_circumference
     *
     * @mbggenerated
     */
    private String rightThighCircumference;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.fat_percentage
     *
     * @mbggenerated
     */
    private String fatPercentage;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.heart_rate
     *
     * @mbggenerated
     */
    private String heartRate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.sdp
     *
     * @mbggenerated
     */
    private String sdp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.dbp
     *
     * @mbggenerated
     */
    private String dbp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.fat_content
     *
     * @mbggenerated
     */
    private String fatContent;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.muscle_content
     *
     * @mbggenerated
     */
    private String muscleContent;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.ext1
     *
     * @mbggenerated
     */
    private String ext1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.ext2
     *
     * @mbggenerated
     */
    private String ext2;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.ext3
     *
     * @mbggenerated
     */
    private String ext3;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.ext4
     *
     * @mbggenerated
     */
    private String ext4;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.ext5
     *
     * @mbggenerated
     */
    private String ext5;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.bmi
     *
     * @mbggenerated
     */
    private String bmi;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.cdt
     *
     * @mbggenerated
     */
    private Date cdt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer_body_info.mdt
     *
     * @mbggenerated
     */
    private Date mdt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table customer_body_info
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.id
     *
     * @return the value of customer_body_info.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.id
     *
     * @param id the value for customer_body_info.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.customer_id
     *
     * @return the value of customer_body_info.customer_id
     *
     * @mbggenerated
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.customer_id
     *
     * @param customerId the value for customer_body_info.customer_id
     *
     * @mbggenerated
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.record_id
     *
     * @return the value of customer_body_info.record_id
     *
     * @mbggenerated
     */
    public String getRecordId() {
        return recordId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.record_id
     *
     * @param recordId the value for customer_body_info.record_id
     *
     * @mbggenerated
     */
    public void setRecordId(String recordId) {
        this.recordId = recordId == null ? null : recordId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.register_date
     *
     * @return the value of customer_body_info.register_date
     *
     * @mbggenerated
     */
    public String getRegisterDate() {
        return registerDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.register_date
     *
     * @param registerDate the value for customer_body_info.register_date
     *
     * @mbggenerated
     */
    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.height
     *
     * @return the value of customer_body_info.height
     *
     * @mbggenerated
     */
    public String getHeight() {
        return height;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.height
     *
     * @param height the value for customer_body_info.height
     *
     * @mbggenerated
     */
    public void setHeight(String height) {
        this.height = height == null ? null : height.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.weight
     *
     * @return the value of customer_body_info.weight
     *
     * @mbggenerated
     */
    public String getWeight() {
        return weight;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.weight
     *
     * @param weight the value for customer_body_info.weight
     *
     * @mbggenerated
     */
    public void setWeight(String weight) {
        this.weight = weight == null ? null : weight.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.chest
     *
     * @return the value of customer_body_info.chest
     *
     * @mbggenerated
     */
    public String getChest() {
        return chest;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.chest
     *
     * @param chest the value for customer_body_info.chest
     *
     * @mbggenerated
     */
    public void setChest(String chest) {
        this.chest = chest == null ? null : chest.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.waistline
     *
     * @return the value of customer_body_info.waistline
     *
     * @mbggenerated
     */
    public String getWaistline() {
        return waistline;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.waistline
     *
     * @param waistline the value for customer_body_info.waistline
     *
     * @mbggenerated
     */
    public void setWaistline(String waistline) {
        this.waistline = waistline == null ? null : waistline.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.hipline
     *
     * @return the value of customer_body_info.hipline
     *
     * @mbggenerated
     */
    public String getHipline() {
        return hipline;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.hipline
     *
     * @param hipline the value for customer_body_info.hipline
     *
     * @mbggenerated
     */
    public void setHipline(String hipline) {
        this.hipline = hipline == null ? null : hipline.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.waist_hip_ratio
     *
     * @return the value of customer_body_info.waist_hip_ratio
     *
     * @mbggenerated
     */
    public String getWaistHipRatio() {
        return waistHipRatio;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.waist_hip_ratio
     *
     * @param waistHipRatio the value for customer_body_info.waist_hip_ratio
     *
     * @mbggenerated
     */
    public void setWaistHipRatio(String waistHipRatio) {
        this.waistHipRatio = waistHipRatio == null ? null : waistHipRatio.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.left_arm_circumference
     *
     * @return the value of customer_body_info.left_arm_circumference
     *
     * @mbggenerated
     */
    public String getLeftArmCircumference() {
        return leftArmCircumference;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.left_arm_circumference
     *
     * @param leftArmCircumference the value for customer_body_info.left_arm_circumference
     *
     * @mbggenerated
     */
    public void setLeftArmCircumference(String leftArmCircumference) {
        this.leftArmCircumference = leftArmCircumference == null ? null : leftArmCircumference.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.right_arm_circumference
     *
     * @return the value of customer_body_info.right_arm_circumference
     *
     * @mbggenerated
     */
    public String getRightArmCircumference() {
        return rightArmCircumference;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.right_arm_circumference
     *
     * @param rightArmCircumference the value for customer_body_info.right_arm_circumference
     *
     * @mbggenerated
     */
    public void setRightArmCircumference(String rightArmCircumference) {
        this.rightArmCircumference = rightArmCircumference == null ? null : rightArmCircumference.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.left_thigh_circumference
     *
     * @return the value of customer_body_info.left_thigh_circumference
     *
     * @mbggenerated
     */
    public String getLeftThighCircumference() {
        return leftThighCircumference;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.left_thigh_circumference
     *
     * @param leftThighCircumference the value for customer_body_info.left_thigh_circumference
     *
     * @mbggenerated
     */
    public void setLeftThighCircumference(String leftThighCircumference) {
        this.leftThighCircumference = leftThighCircumference == null ? null : leftThighCircumference.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.right_thigh_circumference
     *
     * @return the value of customer_body_info.right_thigh_circumference
     *
     * @mbggenerated
     */
    public String getRightThighCircumference() {
        return rightThighCircumference;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.right_thigh_circumference
     *
     * @param rightThighCircumference the value for customer_body_info.right_thigh_circumference
     *
     * @mbggenerated
     */
    public void setRightThighCircumference(String rightThighCircumference) {
        this.rightThighCircumference = rightThighCircumference == null ? null : rightThighCircumference.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.fat percentage
     *
     * @return the value of customer_body_info.fat percentage
     *
     * @mbggenerated
     */
    public String getFatPercentage() {
        return fatPercentage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.fat percentage
     *
     * @param fatPercentage the value for customer_body_info.fat percentage
     *
     * @mbggenerated
     */
    public void setFatPercentage(String fatPercentage) {
        this.fatPercentage = fatPercentage == null ? null : fatPercentage.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.heart_rate
     *
     * @return the value of customer_body_info.heart_rate
     *
     * @mbggenerated
     */
    public String getHeartRate() {
        return heartRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.heart_rate
     *
     * @param heartRate the value for customer_body_info.heart_rate
     *
     * @mbggenerated
     */
    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate == null ? null : heartRate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.sdp
     *
     * @return the value of customer_body_info.sdp
     *
     * @mbggenerated
     */
    public String getSdp() {
        return sdp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.sdp
     *
     * @param sdp the value for customer_body_info.sdp
     *
     * @mbggenerated
     */
    public void setSdp(String sdp) {
        this.sdp = sdp == null ? null : sdp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.dbp
     *
     * @return the value of customer_body_info.dbp
     *
     * @mbggenerated
     */
    public String getDbp() {
        return dbp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.dbp
     *
     * @param dbp the value for customer_body_info.dbp
     *
     * @mbggenerated
     */
    public void setDbp(String dbp) {
        this.dbp = dbp == null ? null : dbp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.fat_content
     *
     * @return the value of customer_body_info.fat_content
     *
     * @mbggenerated
     */
    public String getFatContent() {
        return fatContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.fat_content
     *
     * @param fatContent the value for customer_body_info.fat_content
     *
     * @mbggenerated
     */
    public void setFatContent(String fatContent) {
        this.fatContent = fatContent == null ? null : fatContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.muscle_content
     *
     * @return the value of customer_body_info.muscle_content
     *
     * @mbggenerated
     */
    public String getMuscleContent() {
        return muscleContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.muscle_content
     *
     * @param muscleContent the value for customer_body_info.muscle_content
     *
     * @mbggenerated
     */
    public void setMuscleContent(String muscleContent) {
        this.muscleContent = muscleContent == null ? null : muscleContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.ext1
     *
     * @return the value of customer_body_info.ext1
     *
     * @mbggenerated
     */
    public String getExt1() {
        return ext1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.ext1
     *
     * @param ext1 the value for customer_body_info.ext1
     *
     * @mbggenerated
     */
    public void setExt1(String ext1) {
        this.ext1 = ext1 == null ? null : ext1.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.ext2
     *
     * @return the value of customer_body_info.ext2
     *
     * @mbggenerated
     */
    public String getExt2() {
        return ext2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.ext2
     *
     * @param ext2 the value for customer_body_info.ext2
     *
     * @mbggenerated
     */
    public void setExt2(String ext2) {
        this.ext2 = ext2 == null ? null : ext2.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.ext3
     *
     * @return the value of customer_body_info.ext3
     *
     * @mbggenerated
     */
    public String getExt3() {
        return ext3;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.ext3
     *
     * @param ext3 the value for customer_body_info.ext3
     *
     * @mbggenerated
     */
    public void setExt3(String ext3) {
        this.ext3 = ext3 == null ? null : ext3.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.ext4
     *
     * @return the value of customer_body_info.ext4
     *
     * @mbggenerated
     */
    public String getExt4() {
        return ext4;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.ext4
     *
     * @param ext4 the value for customer_body_info.ext4
     *
     * @mbggenerated
     */
    public void setExt4(String ext4) {
        this.ext4 = ext4 == null ? null : ext4.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.ext5
     *
     * @return the value of customer_body_info.ext5
     *
     * @mbggenerated
     */
    public String getExt5() {
        return ext5;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.ext5
     *
     * @param ext5 the value for customer_body_info.ext5
     *
     * @mbggenerated
     */
    public void setExt5(String ext5) {
        this.ext5 = ext5 == null ? null : ext5.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.bmi
     *
     * @return the value of customer_body_info.bmi
     *
     * @mbggenerated
     */
    public String getBmi() {
        return bmi;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.bmi
     *
     * @param bmi the value for customer_body_info.bmi
     *
     * @mbggenerated
     */
    public void setBmi(String bmi) {
        this.bmi = bmi == null ? null : bmi.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.cdt
     *
     * @return the value of customer_body_info.cdt
     *
     * @mbggenerated
     */
    public Date getCdt() {
        return cdt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.cdt
     *
     * @param cdt the value for customer_body_info.cdt
     *
     * @mbggenerated
     */
    public void setCdt(Date cdt) {
        this.cdt = cdt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer_body_info.mdt
     *
     * @return the value of customer_body_info.mdt
     *
     * @mbggenerated
     */
    public Date getMdt() {
        return mdt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer_body_info.mdt
     *
     * @param mdt the value for customer_body_info.mdt
     *
     * @mbggenerated
     */
    public void setMdt(Date mdt) {
        this.mdt = mdt;
    }
    

	/**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_body_info
     *
     * @mbggenerated
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
        CustomerBodyInfo other = (CustomerBodyInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCustomerId() == null ? other.getCustomerId() == null : this.getCustomerId().equals(other.getCustomerId()))
            && (this.getRecordId() == null ? other.getRecordId() == null : this.getRecordId().equals(other.getRecordId()))
            && (this.getRegisterDate() == null ? other.getRegisterDate() == null : this.getRegisterDate().equals(other.getRegisterDate()))
            && (this.getHeight() == null ? other.getHeight() == null : this.getHeight().equals(other.getHeight()))
            && (this.getWeight() == null ? other.getWeight() == null : this.getWeight().equals(other.getWeight()))
            && (this.getChest() == null ? other.getChest() == null : this.getChest().equals(other.getChest()))
            && (this.getWaistline() == null ? other.getWaistline() == null : this.getWaistline().equals(other.getWaistline()))
            && (this.getHipline() == null ? other.getHipline() == null : this.getHipline().equals(other.getHipline()))
            && (this.getWaistHipRatio() == null ? other.getWaistHipRatio() == null : this.getWaistHipRatio().equals(other.getWaistHipRatio()))
            && (this.getLeftArmCircumference() == null ? other.getLeftArmCircumference() == null : this.getLeftArmCircumference().equals(other.getLeftArmCircumference()))
            && (this.getRightArmCircumference() == null ? other.getRightArmCircumference() == null : this.getRightArmCircumference().equals(other.getRightArmCircumference()))
            && (this.getLeftThighCircumference() == null ? other.getLeftThighCircumference() == null : this.getLeftThighCircumference().equals(other.getLeftThighCircumference()))
            && (this.getRightThighCircumference() == null ? other.getRightThighCircumference() == null : this.getRightThighCircumference().equals(other.getRightThighCircumference()))
            && (this.getFatPercentage() == null ? other.getFatPercentage() == null : this.getFatPercentage().equals(other.getFatPercentage()))
            && (this.getHeartRate() == null ? other.getHeartRate() == null : this.getHeartRate().equals(other.getHeartRate()))
            && (this.getSdp() == null ? other.getSdp() == null : this.getSdp().equals(other.getSdp()))
            && (this.getDbp() == null ? other.getDbp() == null : this.getDbp().equals(other.getDbp()))
            && (this.getFatContent() == null ? other.getFatContent() == null : this.getFatContent().equals(other.getFatContent()))
            && (this.getMuscleContent() == null ? other.getMuscleContent() == null : this.getMuscleContent().equals(other.getMuscleContent()))
            && (this.getExt1() == null ? other.getExt1() == null : this.getExt1().equals(other.getExt1()))
            && (this.getExt2() == null ? other.getExt2() == null : this.getExt2().equals(other.getExt2()))
            && (this.getExt3() == null ? other.getExt3() == null : this.getExt3().equals(other.getExt3()))
            && (this.getExt4() == null ? other.getExt4() == null : this.getExt4().equals(other.getExt4()))
            && (this.getExt5() == null ? other.getExt5() == null : this.getExt5().equals(other.getExt5()))
            && (this.getBmi() == null ? other.getBmi() == null : this.getBmi().equals(other.getBmi()))
            && (this.getCdt() == null ? other.getCdt() == null : this.getCdt().equals(other.getCdt()))
            && (this.getMdt() == null ? other.getMdt() == null : this.getMdt().equals(other.getMdt()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_body_info
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCustomerId() == null) ? 0 : getCustomerId().hashCode());
        result = prime * result + ((getRecordId() == null) ? 0 : getRecordId().hashCode());
        result = prime * result + ((getRegisterDate() == null) ? 0 : getRegisterDate().hashCode());
        result = prime * result + ((getHeight() == null) ? 0 : getHeight().hashCode());
        result = prime * result + ((getWeight() == null) ? 0 : getWeight().hashCode());
        result = prime * result + ((getChest() == null) ? 0 : getChest().hashCode());
        result = prime * result + ((getWaistline() == null) ? 0 : getWaistline().hashCode());
        result = prime * result + ((getHipline() == null) ? 0 : getHipline().hashCode());
        result = prime * result + ((getWaistHipRatio() == null) ? 0 : getWaistHipRatio().hashCode());
        result = prime * result + ((getLeftArmCircumference() == null) ? 0 : getLeftArmCircumference().hashCode());
        result = prime * result + ((getRightArmCircumference() == null) ? 0 : getRightArmCircumference().hashCode());
        result = prime * result + ((getLeftThighCircumference() == null) ? 0 : getLeftThighCircumference().hashCode());
        result = prime * result + ((getRightThighCircumference() == null) ? 0 : getRightThighCircumference().hashCode());
        result = prime * result + ((getFatPercentage() == null) ? 0 : getFatPercentage().hashCode());
        result = prime * result + ((getHeartRate() == null) ? 0 : getHeartRate().hashCode());
        result = prime * result + ((getSdp() == null) ? 0 : getSdp().hashCode());
        result = prime * result + ((getDbp() == null) ? 0 : getDbp().hashCode());
        result = prime * result + ((getFatContent() == null) ? 0 : getFatContent().hashCode());
        result = prime * result + ((getMuscleContent() == null) ? 0 : getMuscleContent().hashCode());
        result = prime * result + ((getExt1() == null) ? 0 : getExt1().hashCode());
        result = prime * result + ((getExt2() == null) ? 0 : getExt2().hashCode());
        result = prime * result + ((getExt3() == null) ? 0 : getExt3().hashCode());
        result = prime * result + ((getExt4() == null) ? 0 : getExt4().hashCode());
        result = prime * result + ((getExt5() == null) ? 0 : getExt5().hashCode());
        result = prime * result + ((getBmi() == null) ? 0 : getBmi().hashCode());
        result = prime * result + ((getCdt() == null) ? 0 : getCdt().hashCode());
        result = prime * result + ((getMdt() == null) ? 0 : getMdt().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_body_info
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", recordId=").append(recordId);
        sb.append(", registerDate=").append(registerDate);
        sb.append(", height=").append(height);
        sb.append(", weight=").append(weight);
        sb.append(", chest=").append(chest);
        sb.append(", waistline=").append(waistline);
        sb.append(", hipline=").append(hipline);
        sb.append(", waistHipRatio=").append(waistHipRatio);
        sb.append(", leftArmCircumference=").append(leftArmCircumference);
        sb.append(", rightArmCircumference=").append(rightArmCircumference);
        sb.append(", leftThighCircumference=").append(leftThighCircumference);
        sb.append(", rightThighCircumference=").append(rightThighCircumference);
        sb.append(", fatPercentage=").append(fatPercentage);
        sb.append(", heartRate=").append(heartRate);
        sb.append(", sdp=").append(sdp);
        sb.append(", dbp=").append(dbp);
        sb.append(", fatContent=").append(fatContent);
        sb.append(", muscleContent=").append(muscleContent);
        sb.append(", ext1=").append(ext1);
        sb.append(", ext2=").append(ext2);
        sb.append(", ext3=").append(ext3);
        sb.append(", ext4=").append(ext4);
        sb.append(", ext5=").append(ext5);
        sb.append(", bmi=").append(bmi);
        sb.append(", cdt=").append(cdt);
        sb.append(", mdt=").append(mdt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}