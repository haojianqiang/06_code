package com.gpdi.web.entity.specialist;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Auther: zhangzheng
 * @Date: 2019/11/22 08:54
 * @Description: 专家实体类
 */
@Entity
@Table(name = "sys_specialist")
public class Specialist implements Serializable {
    private Integer id;
    private String  specialistName;//专家名字
    private String  personalIntro;//个人简介
    private String  goodAtDomain;//擅长领域
    private String  graduateSchool;//毕业院校
    private String  phone;//电话号码
    private String  email;//邮箱

    @Id
    @GeneratedValue
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "name")
    public String getSpecialistName() {
        return specialistName;
    }

    public void setSpecialistName(String specialistName) {
        this.specialistName = specialistName;
    }
    @Column(name = "synopsis")
    public String getPersonalIntro() {
        return personalIntro;
    }

    public void setPersonalIntro(String personalIntro) {
        this.personalIntro = personalIntro;
    }
    @Column(name = "field")
    public String getGoodAtDomain() {
        return goodAtDomain;
    }

    public void setGoodAtDomain(String goodAtDomain) {
        this.goodAtDomain = goodAtDomain;
    }
    @Column(name = "school")
    public String getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
