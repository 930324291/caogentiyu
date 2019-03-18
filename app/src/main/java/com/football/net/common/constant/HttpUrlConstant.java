package com.football.net.common.constant;

/**
 * Created by Administrator on 2015/8/20.
 */
public class HttpUrlConstant {

    public static String SERVER_URL = "http://120.24.89.9:80";
    // public static String SERVER_URL = "http://192.168.2.104:8080";
    public static String APP_SERVER_URL = SERVER_URL + "/app/";
//    public static String APP_SERVER_URL = "http://localhost:26586/";

    public static String Interface() {
        return APP_SERVER_URL + "GeneralInterfaceHandler.ashx";
    }

    public static String register() {
        return APP_SERVER_URL + "AppInterfaceHandler.ashx";
    }

    public static String uploadHeader() {
        return "http://img.lucksoft.cn/Handlers/UploadHandler.ashx";
    }

    public static String GeneralKey() {
        return APP_SERVER_URL + "GeneralHandler.ashx";
    }

    /**
     * 登录
     */
    public static String Login() {
        return "OperatorLogin";
    }

    /**
     * 获取会员注册
     */
    public static String MemRegister() {
        return "MemRegister";
    }
    /**
     * 获取会员注册
     */
    public static String MemberReg() {
        return "MemberReg";
    }

    /**
     * 获取会员信息
     */
    public static String GetMemInfo() {
        return "GetMemInfo";
    }

    /**
     * 充值
     */
    public static String RechargeMoney() {
        return "RechargeMoney";
    }

    /**
     * 分页获取会员等级
     */
    public static String GetLevelList() {
        return "GetLevelList";
    }

    /**
     * 分页获取会员等级
     */
    public static String LevelList() {
        return "LevelList";
    }

    /**
     * 2.	获取会员自定义属性
     */
    public static String GetMemCustomField() {
        return "GetMemCustomField";
    }

    /**
     * 获取企业计次商品详情
     */
    public static String GetUniqueCountGood = "GetUniqueCountGood";
    /**
     * 4.34	获取企业所有服务商品
     */
    public static String GetServiceGood = "GetServiceGood";
    /**
     * 会员充次
     */
    public static String RechargeCount = "RechargeCount";
    /**
     * 积分变动
     */
    public static String AdjustPoint = "AdjustPoint";
    /**
     * 获取礼品列表
     */
    public static String GiftList = "GiftList";
    /**
     * 兑换礼拜
     */
    public static String ExchangeGifts = "ExchangeGifts";
    /**
     * 4.10	挂失锁定解锁
     */
    public static String UpdateState = "UpdateState";
    /**
     * 换卡
     */
    public static String ChangeMemCard = "ChangeMemCard";

    /**
     * 修改会员等级
     */
    public static String UpdateMemLevel = "UpdateMemLevel";
    /**
     * 修改会员密码
     */
    public static String UpdatePassword = "UpdatePassword";
    /**
     * 分页获取消费记录
     */
    public static String OrderHistory = "OrderHistory";
    /**
     * 商品消费详情
     */
    public static String OrderDetail = "OrderDetail";
    /**
     * 充值记录
     */
    public static String RechargeHistory = "RechargeHistory";
    /**
     * 4.51	分页获取商品进货记录
     */
    public static String GetStockInLog = "GetStockInLog";
    /**
     * 改价
     */
    public static String UpdateGoodsPrice = "UpdateGoodsPrice";
    /**
     * 分页获取供应商
     */
    public static String GetAllSuppliers = "GetAllSuppliers";
    /**
     * 快速消费
     */
    public static String QuickConsume = "QuickConsume";
    /**
     * 获取扫描商品详情
     */
    public static String GetUniqueGood = "GetUniqueGood";
    /**
     * 商品消费
     */
    public static String GoodsConsumption = "GoodsConsumption";
    /**
     * 获取会员所有计次商品
     */
    public static String GetCardByCount = "GetCardByCount";
    /**
     * 获取礼品详情
     */
    public static String GetUniqueGift = "GetUniqueGift";
    /**
     * 3.	获取当日、当周、当月总收入、会员开卡数量
     */
    public static String GetSalesMemNum = "GetSalesMemNum";
    /**
     * 获取操作员最近交易
     */
    public static String GetOperatorDeal = "GetOperatorDeal";
    /**
     * 3.	获取充值优惠活动
     */
    public static String GetRechargeActivity = "GetRechargeActivity";
    /**
     * 分页获取会员列表
     */
    public static String GetMemberList = "GetMemberList";
    /**
     * 注册
     */
    public static String CompCodeReg = "CompCodeReg";
}
