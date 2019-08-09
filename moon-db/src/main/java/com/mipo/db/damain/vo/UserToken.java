package com.mipo.db.damain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("用户信息")
public class UserToken implements Serializable {


    private static final long serialVersionUID = 691409101944208868L;
    @ApiModelProperty("用户Id")
    private Long userId;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("真实姓名")
    private String name;

    @ApiModelProperty("生日 yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    @ApiModelProperty("手机号")
    private String phoneNo;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("性别")
    private Integer sex;

    @ApiModelProperty("描述")
    private String introduce;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("存在密码 只有phoneNo不为空时有效 true--有密码 false--没有密码")
    private Boolean existsPass = true;

    @ApiModelProperty("是否新注册用户 false 不是 true 是 ")
    private Boolean isRegister =false;

    //后续其它信息

}
