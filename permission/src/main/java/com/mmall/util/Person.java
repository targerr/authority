package com.mmall.util;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/03
 */
@Data
public class Person {
    private int id;
    @Min(18)
    @Max(value=65)
    private int age;
    private String name;
    @Length(max=10)
    private String address;

    @Past
    private Date birthday;
    private BigDecimal weight;
    @Email
    private String email;
}
