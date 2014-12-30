package fycsb.gky.tb_autosign.encrypt;

/**
 * Created by codefu on 2014/8/27.
 */

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.entity.TBList;
import fycsb.gky.tb_autosign.utils.PostUrlUtil;

public class MD5Util {

    public static String getMD5(String encryptStr) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] hash = null;
        hash = MessageDigest.getInstance("MD5").digest(encryptStr.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    //    public static void main(String args[]) throws Exception{
////        String str = "BDUSS=FMbzEzemZiTkUyU1JOfnlleklQM35KSTRHOWNzN01nUTgzYXVtaG1uSlhBTWxVQVFBQUFBJCQAAAAAAAAAAAEAAACsMnYgYWE2NTY0NjQxMjkAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFdzoVRXc6FUaV|0c668497e412e86266392de4f3db290c&_client_id=wappc_1419610591515_691&_client_type=2&_client_version=5.1.1&_phone_imei=000000000000000&cuid=709831DDD4CBADB28DAA316524933278%7C000000000000000&from=tieba&model=Motorola+Moto+X+-+4.2.2+-+API+17+-+720x1280&net_type=3&stErrorNums=0&stMethod=1&stMode=1&stSize=93&stTime=48&stTimesNum=0&timestamp=1419923155930&user_id=544617132&" + TieBaApi.FLAG;
////        str = URLDecoder.decode(str).replaceAll("&","");
////        System.out.println(str);
////        System.out.println(">>" + getMD5(str));
////        String sign = PostUrlUtil.getTieBaIDSign("FMbzEzemZiTkUyU1JOfnlleklQM35KSTRHOWNzN01nUTgzYXVtaG1uSlhBTWxVQVFBQUFBJCQAAAAAAAAAAAEAAACsMnYgYWE2NTY0NjQxMjkAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFdzoVRXc6FUaV|0c668497e412e86266392de4f3db290c","544617132",1419923155930L);
////        System.out.println(">>" + sign);
//        String fuckStr = "{'error':{'errno':'0','errmsg':'success','usermsg':'\\u6210\\u529f'},'forum_info':[{'forum_id':'5846492','forum_name':'lumia620','user_level':'10','user_exp':'2885','need_exp':'3000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/241f95cad1c8a7865e0e5db06609c93d70cf5063.jpg'},{'forum_id':'200521','forum_name':'gpp','user_level':'9','user_exp':'1348','need_exp':'2000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/abpic/item/c1d719f7905298227221ab8bd6ca7bcb0b46d458.jpg'},{'forum_id':'1040217','forum_name':'iphone5','user_level':'9','user_exp':'1632','need_exp':'2000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/1e30e924b899a9013c9fc2ce1c950a7b0308f5a4.jpg'},{'forum_id':'1017230','forum_name':'ios7','user_level':'9','user_exp':'1414','need_exp':'2000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/cdbf6c81800a19d83ab094c432fa828ba71e46bb.jpg'},{'forum_id':'9628214','forum_name':'\\u534e\\u4e3a\\u8363\\u80003c','user_level':'9','user_exp':'1402','need_exp':'2000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/91ef76c6a7efce1bef61e5d5ac51f3deb48f6562.jpg'},{'forum_id':'4864636','forum_name':'\\u5e7f\\u5dde\\u5e02\\u751f\\u7269\\u533b\\u836f\\u9ad8\\u7ea7\\u804c\\u4e1a\\u5b66\\u6821','user_level':'9','user_exp':'1429','need_exp':'2000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/6a63f6246b600c338b4dd7371b4c510fd9f9a11c.jpg'},{'forum_id':'896836','forum_name':'jquery','user_level':'9','user_exp':'1493','need_exp':'2000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/5243fbf2b2119313c2a23ba367380cd790238dc4.jpg'},{'forum_id':'3169481','forum_name':'ipadmini','user_level':'10','user_exp':'2133','need_exp':'3000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/c75c10385343fbf261721c74b17eca8065388f0f.jpg'},{'forum_id':'2990865','forum_name':'ipodtouch5','user_level':'10','user_exp':'2108','need_exp':'3000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/2e2eb9389b504fc2a43a96a3e4dde71190ef6d2a.jpg'},{'forum_id':'794409','forum_name':'\\u5e7f\\u5dde\\u5e02\\u4fe1\\u606f\\u5de5\\u7a0b\\u804c\\u4e1a\\u5b66\\u6821','user_level':'10','user_exp':'2106','need_exp':'3000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/4a36acaf2edda3cca6a2163b02e93901213f9263.jpg'},{'forum_id':'5043456','forum_name':'lumia520','user_level':'10','user_exp':'2058','need_exp':'3000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/7af40ad162d9f2d3df628c59a8ec8a136327cc11.jpg'},{'forum_id':'389364','forum_name':'vae','user_level':'10','user_exp':'2056','need_exp':'3000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/a08b87d6277f9e2fa78e5ebe1e30e924b999f3c9.jpg'},{'forum_id':'5972104','forum_name':'htc8s','user_level':'10','user_exp':'2053','need_exp':'3000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/80cb39dbb6fd5266b04b727baa18972bd4073678.jpg'},{'forum_id':'3204264','forum_name':'iphone\\u5ec9\\u4ef7\\u7248','user_level':'10','user_exp':'2046','need_exp':'3000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/caef76094b36acafd380db047dd98d1000e99ce2.jpg'},{'forum_id':'3236787','forum_name':'\\u66b4\\u8d70\\u6f2b\\u753b','user_level':'10','user_exp':'2043','need_exp':'3000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/4f60a345d688d43f1768fdab7e1ed21b0ff43b0d.jpg'},{'forum_id':'1978217','forum_name':'\\u5f88\\u7eaf\\u5f88\\u66a7\\u6627','user_level':'10','user_exp':'2037','need_exp':'3000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/a044ad345982b2b7b8820a6630adcbef76099b76.jpg'},{'forum_id':'1211534','forum_name':'\\u6821\\u82b1\\u7684\\u8d34\\u8eab\\u9ad8\\u624b','user_level':'10','user_exp':'2036','need_exp':'3000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/b7fd5266d0160924588b7d74d50735fae6cd3437.jpg'},{'forum_id':'7459387','forum_name':'iphone5c','user_level':'10','user_exp':'2029','need_exp':'3000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/7acb0a46f21fbe0901962e7a68600c338644ad90.jpg'},{'forum_id':'701500','forum_name':'javascript','user_level':'10','user_exp':'2008','need_exp':'3000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/b58f8c5494eef01f70276779e1fe9925bc317d5e.jpg'},{'forum_id':'3425514','forum_name':'lumia920','user_level':'10','user_exp':'2003','need_exp':'3000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/ac6eddc451da81cb35547a005366d01608243148.jpg'},{'forum_id':'834104','forum_name':'ubuntu','user_level':'9','user_exp':'1984','need_exp':'2000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/e7cd7b899e510fb3d06c4784db33c895d1430caf.jpg'},{'forum_id':'143071','forum_name':'macbookair','user_level':'9','user_exp':'1007','need_exp':'2000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/c83d70cf3bc79f3d81ef96d9bba1cd11738b29c8.jpg'},{'forum_id':'2126246','forum_name':'\\u5343\\u91cc\\u8fbe','user_level':'8','user_exp':'868','need_exp':'1000','is_sign_in':'0','cont_sign_num':'3','avatar':''},{'forum_id':'600979','forum_name':'macbook','user_level':'8','user_exp':'780','need_exp':'1000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/f9198618367adab485df10f88ad4b31c8701e47a.jpg'},{'forum_id':'3042111','forum_name':'\\u82f9\\u679c\\u8d8a\\u72f1','user_level':'8','user_exp':'761','need_exp':'1000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/aa64034f78f0f736598e86210855b319ebc4139a.jpg'},{'forum_id':'11481548','forum_name':'\\u5c0f\\u7c73\\u624b\\u73af','user_level':'8','user_exp':'632','need_exp':'1000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/58df5f1f3a292df520c7c34cbf315c6034a87311.jpg'},{'forum_id':'2495019','forum_name':'iphone6','user_level':'8','user_exp':'616','need_exp':'1000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/960a304e251f95ca4e91b0fccb177f3e67095217.jpg'},{'forum_id':'8854021','forum_name':'venue8pro','user_level':'8','user_exp':'603','need_exp':'1000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/e1fe9925bc315c60ae02a1988fb1cb1349547736.jpg'},{'forum_id':'14049345','forum_name':'bong2','user_level':'8','user_exp':'600','need_exp':'1000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/00e93901213fb80ea152f5d935d12f2eb9389439.jpg'},{'forum_id':'3065497','forum_name':'ios8','user_level':'8','user_exp':'546','need_exp':'1000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/e824b899a9014c08216fd04c087b02087af4f487.jpg'},{'forum_id':'2995049','forum_name':'\\u667a\\u80fd\\u624b\\u73af','user_level':'8','user_exp':'524','need_exp':'1000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/e7cd7b899e510fb3c6255e54d833c895d1430c30.jpg'},{'forum_id':'13135528','forum_name':'iwork8','user_level':'8','user_exp':'519','need_exp':'1000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/50da81cb39dbb6fdf4a8e21f0a24ab18972b3713.jpg'},{'forum_id':'3004652','forum_name':'win8\\u5e73\\u677f','user_level':'8','user_exp':'517','need_exp':'1000','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/e61190ef76c6a7efd761ec2efffaaf51f3de6676.jpg'},{'forum_id':'358325','forum_name':'\\u9b45\\u65cf','user_level':'7','user_exp':'334','need_exp':'500','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/6b63f6246b600c331d47469b194c510fd9f9a1a3.jpg'},{'forum_id':'366368','forum_name':'\\u5c0f\\u7c73','user_level':'7','user_exp':'246','need_exp':'500','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/72f1d5160924ab18c8158d8836fae6cd7a890ba0.jpg'},{'forum_id':'1749218','forum_name':'windows10','user_level':'7','user_exp':'239','need_exp':'500','is_sign_in':'0','cont_sign_num':'3','avatar':'http://imgsrc.baidu.com/forum/pic/item/908fa0ec08fa513d5b4fe8d03e6d55fbb3fbd9d3.jpg'}],'show_dialog':'0','sign_notice':'','title':'7\\u7ea7\\u4ee5\\u4e0a\\u7684\\u5427','text_pre':'\\u5427\\u5185\\u7b49\\u7ea7','text_color':'7\\u7ea7','text_mid':'\\u4ee5\\u4e0a\\u624d\\u53ef\\u4f7f\\u7528\\u54e6~\\u5feb\\u53bb\\u63d0\\u9ad8\\u7b49\\u7ea7\\u5427\\uff01','text_suf':'\\u6bcf\\u65e5\\u6700\\u4f73\\u7b7e\\u5230\\u65f6\\u95f49:00--16:00\\u300119:00--22:00','num_notice':'\\u4f607\\u7ea7\\u4ee5\\u4e0a\\u7684\\u5427\\u8d85\\u8fc7\\u4e86\\u6700\\u5927\\u9650\\u5ea6\\uff0c\\u73b0\\u5728\\u4ec5\\u652f\\u6301\\u5bf950\\u4e2a\\u5427\\u8fdb\\u884c\\u4e00\\u952e\\u7b7e\\u5230','level':'7','sign_max_num':'50','valid':'1','msign_step_num':'50','server_time':'78060','time':1419925096,'ctime':0,'logid':2296252545,'error_code':'0'}";
//        Gson gson = new Gson();
//
//        TBList tbList = gson.fromJson(fuckStr, TBList.class);
//        if (tbList!=null) {
//            System.out.println("shit");
//        }
//    }
    public static void main(String[] args) {
        System.out.println(new Date().getTime());
    }
}