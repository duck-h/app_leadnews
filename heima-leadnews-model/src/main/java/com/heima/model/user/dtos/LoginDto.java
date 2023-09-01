package com.heima.model.user.dtos;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginDto {

    //手機號碼
    @ApiModelProperty(value = "手機號碼" ,required = true)
    private String phone;

    //密碼
    @ApiModelProperty(value = "密碼" , required = true)
    private String password;
}
