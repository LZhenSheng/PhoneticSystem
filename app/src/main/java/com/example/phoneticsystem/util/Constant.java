package com.example.phoneticsystem.util;


/***
* 常量类
* @author 胜利镇
* @time 2021/4/13
* @dec 
*/
public class Constant {

    /**
     * 左侧（其他人）图片消息
     */
    public static final int TYPE_IMAGE_LEFT = 120;

    /**
     * 右侧（我的）图片消息
     */
    public static final int TYPE_IMAGE_RIGHT = 130;

    /*跳转ID*/
    public static final String ID ="ID" ;

    /**
     * 手机号正则表达式
     * 移动：134 135 136 137 138 139 147 150 151 152 157 158 159 178 182 183 184 187 188 198
     * 联通：130 131 132 145 155 156 166 171 175 176 185 186
     * 电信：133 149 153 173 177 180 181 189 199
     * 虚拟运营商: 170
     */
    public static final String REGEX_PHONE = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    public static final String SERVICE_UUID="ffffffff-fcac-0451-0000-00004736d062";

    public static final String IP="10.74.227.102";

}
