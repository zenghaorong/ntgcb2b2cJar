package com.aebiz.app.store.modules.services.impl;

import com.aebiz.app.goods.modules.models.Goods_class;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.services.GoodsClassService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.goods.modules.services.GoodsService;
import com.aebiz.app.shop.modules.models.Shop_area_management;
import com.aebiz.app.shop.modules.services.ShopAreaManagementService;
import com.aebiz.app.shop.modules.services.ShopExpressService;
import com.aebiz.app.store.modules.commons.vo.StoreFreightProduct;
import com.aebiz.app.store.modules.models.Store_freight;
import com.aebiz.app.store.modules.models.Store_freight_rules;
import com.aebiz.app.store.modules.models.Store_freight_rulesdetail;
import com.aebiz.app.store.modules.services.StoreFreightRulesService;
import com.aebiz.app.store.modules.services.StoreFreightRulesdetailService;
import com.aebiz.app.store.modules.services.StoreFreightService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.util.cri.SqlExpression;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreFreightServiceImpl extends BaseServiceImpl<Store_freight> implements StoreFreightService {
    @Autowired
    private ShopAreaManagementService shopAreaManagementService;
    @Autowired
    private ShopExpressService shopExpressService;
    @Autowired
    private GoodsClassService goodsClassService;
    @Autowired
    private StoreFreightRulesService storeFreightRulesService;
    @Autowired
    private StoreFreightRulesdetailService storeFreightRulesdetailService;
    @Autowired
    private GoodsProductService goodsProductService;
    @Autowired
    private GoodsService goodsService;

    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    /**
     * 添加模板规则
     *
     * @param storeFreight
     * @param temps
     */
    @Override
    @Transactional
    public void addStoreFreight(Store_freight storeFreight, String temps) {
        storeFreight.setOpBy(StringUtil.getUid());
        storeFreight.setOpAt((int) (System.currentTimeMillis() / 1000));
        storeFreight.setStoreId("2017060000000001");
        Store_freight freightTemplate = this.insert(storeFreight);
        if (freightTemplate.isEnabled()) {//当前模板为启用状态则修改其它模板状态为禁用
            this.update(Chain.make("enabled", false), Cnd.where("id", "<>", freightTemplate.getId()));
        }
        List<Store_freight_rules> tampList = Json.fromJsonAsList(Store_freight_rules.class, temps);//解析前台传递的json串
        List<Store_freight_rulesdetail> rulesdetailList = Json.fromJsonAsList(Store_freight_rulesdetail.class, temps);
        //循环插入规则表
        circularInsertRules(tampList,freightTemplate);
        //循环插入规则明细表
        circularInsertDetail(rulesdetailList, freightTemplate);
    }
    /**
     * 添加模板规则
     *商户
     * @param storeFreight
     * @param temps
     */
    @Override
    @Transactional
    public void addStoreTemplateFreight(Store_freight storeFreight, String temps) {
        String storeId=StringUtil.getStoreId();
        storeFreight.setOpBy(StringUtil.getUid());
        storeFreight.setOpAt((int) (System.currentTimeMillis() / 1000));
        storeFreight.setStoreId(storeId);
        Store_freight freightTemplate = this.insert(storeFreight);
        if (freightTemplate.isEnabled()) {//当前模板为启用状态则修改其它模板状态为禁用
            this.update(Chain.make("enabled", false), Cnd.where("id", "<>", freightTemplate.getId()));
        }
        List<Store_freight_rules> tampList = Json.fromJsonAsList(Store_freight_rules.class, temps);//解析前台传递的json串
        List<Store_freight_rulesdetail> rulesdetailList = Json.fromJsonAsList(Store_freight_rulesdetail.class, temps);
        //循环插入规则表
        circularInsertRules(tampList,freightTemplate);
        //循环插入规则明细表
        circularInsertDetail(rulesdetailList, freightTemplate);
    }
    /**
     * 删除原规则后添加模板规则
     */
    @Override
    @Transactional
    public void editStoreFreight(Store_freight freightTemplate, String temps) {
        this.updateIgnoreNull(freightTemplate);
        storeFreightRulesService.clear(Cnd.where("templateId", "=", freightTemplate.getId()));//删除关联模板规则和模板规则明细表
        //根据模板规则ID查询出规则明细
        storeFreightRulesdetailService.clear(Cnd.where("templateId", "=", freightTemplate.getId()));
        List<Store_freight_rules> tampList = Json.fromJsonAsList(Store_freight_rules.class, temps);//解析传递的json字符串为集合
        List<Store_freight_rulesdetail> rulesdetailList = Json.fromJsonAsList(Store_freight_rulesdetail.class, temps);
        //循环插入规则表
        circularInsertRules(tampList,freightTemplate);
        //循环插入规则明细表
        circularInsertDetail(rulesdetailList, freightTemplate);
    }

    private void circularInsertRules(List<Store_freight_rules> tampList,Store_freight freightTemplate) {
        for (Store_freight_rules storeFreightRules : tampList) { //循环插入模板规则表
            storeFreightRules.setTemplateId(freightTemplate.getId());
            StringBuffer sbArea = new StringBuffer();
            StringBuffer sbGoods = new StringBuffer();
            StringBuffer sbLogistics = new StringBuffer();
            if (!"".equals(storeFreightRules.getAreaCode())) { //判断模板规则片区Code是否为空\不限
                String[] areas = storeFreightRules.getAreaCode().split(",");
                for (String area : areas) {
                    sbArea.append(shopAreaManagementService.fetch(Cnd.where("code", "=", area)).getName()).append(",");
                }
            }
            if (!"".equals(storeFreightRules.getGoodsId())) { //判断模板规则商品分类是否为空
                String[] goodss = storeFreightRules.getGoodsId().split(",");
                for (String goods : goodss) {
                    sbGoods.append(goodsClassService.fetch(Cnd.where("id", "=", goods)).getName()).append(",");
                }
            }
            if (!"".equals(storeFreightRules.getLogisticsCode())) { //判断模板规则物流公司Code是否为空
                String[] logisticss = storeFreightRules.getLogisticsCode().split(",");
                for (String logistics : logisticss) {
                    sbLogistics.append(shopExpressService.fetch(Cnd.where("code", "=", logistics)).getName()).append(",");
                }
            }
            storeFreightRules.setAreaName(sbArea.toString());//设置模板规则参数
            storeFreightRules.setLogisticsName(sbLogistics.toString());
            storeFreightRules.setGoodsName(sbGoods.toString());
            storeFreightRulesService.insert(storeFreightRules);
        }
    }


    private void circularInsertDetail(List<Store_freight_rulesdetail> rulesdetailList, Store_freight freightTemplate) {
        for (Store_freight_rulesdetail rulesdetail : rulesdetailList) {
            List<Store_freight_rulesdetail> detailList = new ArrayList<>();
            List<String> notNullList = new ArrayList<>();
            rulesdetail.setTemplateId(freightTemplate.getId());
            if (Strings.isNotBlank(rulesdetail.getAreaCode())) {
                notNullList.add("areaCode");
            }
            if (Strings.isNotBlank(rulesdetail.getGoodsId())) {
                notNullList.add("goodsId");
            }
            if (Strings.isNotBlank(rulesdetail.getLogisticsCode())) {
                notNullList.add("logisticsCode");
            }
            if (notNullList.size() > 0) {
                for (String info : notNullList) {
                    updateDetail(detailList, rulesdetail, info); //对前台传递的模板规则表做交互组合 添加至模板规则明细表
                }
            }
            for (Store_freight_rulesdetail storeFreightRulesDetail : detailList) {
                storeFreightRulesdetailService.insert(storeFreightRulesDetail);
            }
        }
    }

    /**
     * @param detailList
     * @param rulesdetail
     * @param fieldName
     */
    private void updateDetail(List<Store_freight_rulesdetail> detailList, Store_freight_rulesdetail rulesdetail, String fieldName) {
        try {
            Field infoFiled = rulesdetail.getClass().getDeclaredField(fieldName);
            String filedName = infoFiled.getName();
            infoFiled.setAccessible(true);
            String val = (String) infoFiled.get(rulesdetail);
            if (Strings.isNotBlank(val)) {
                String[] infos = val.split(",");
                if (detailList.size() > 0) {
                    List<Store_freight_rulesdetail> tempDetailList = new ArrayList<>();
                    tempDetailList.addAll(detailList);
                    detailList.clear();
                    for (int n = 0; n < tempDetailList.size(); n++) {
                        for (int i = 0; i < infos.length; i++) {
                            Store_freight_rulesdetail storeFreightRulesdetail = new Store_freight_rulesdetail();
                            switch (filedName) {
                                case "areaCode":
                                    tempDetailList.get(n).setAreaCode(infos[i]);
                                    tempDetailList.get(n).setAreaName(shopAreaManagementService.fetch(Cnd.where("code", "=", infos[i])).getName());
                                    break;
                                case "goodsId":
                                    tempDetailList.get(n).setGoodsId(infos[i]);
                                    tempDetailList.get(n).setGoodsName(goodsClassService.fetch(Cnd.where("id", "=", infos[i])).getName());
                                    break;
                                case "logisticsCode":
                                    tempDetailList.get(n).setLogisticsCode(infos[i]);
                                    tempDetailList.get(n).setLogisticsName(shopExpressService.fetch(Cnd.where("code", "=", infos[i])).getName());
                                    break;
                            }
                            BeanUtils.copyProperties(tempDetailList.get(n), storeFreightRulesdetail);
                            detailList.add(storeFreightRulesdetail);
                        }
                    }
                } else {
                    for (String newInfo : infos) {
                        Store_freight_rulesdetail storeFreightRulesdetail = new Store_freight_rulesdetail();
                        switch (filedName) {
                            case "areaCode":
                                rulesdetail.setAreaCode(newInfo);
                                rulesdetail.setAreaName(shopAreaManagementService.fetch(Cnd.where("code", "=", newInfo)).getName());
                                break;
                            case "goodsId":
                                rulesdetail.setGoodsId(newInfo);
                                rulesdetail.setGoodsName(goodsClassService.fetch(Cnd.where("id", "=", newInfo)).getName());
                                break;
                            case "logisticsCode":
                                rulesdetail.setLogisticsCode(newInfo);
                                rulesdetail.setLogisticsName(shopExpressService.fetch(Cnd.where("code", "=", newInfo)).getName());
                                break;
                        }
                        BeanUtils.copyProperties(rulesdetail, storeFreightRulesdetail);
                        detailList.add(storeFreightRulesdetail);
                    }
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void del(String id) {
        this.delete(id);
        storeFreightRulesService.clear(Cnd.where("templateId", "=", id));
        storeFreightRulesdetailService.clear(Cnd.where("templateId", "=", id)); //删除模板规则明细关联数据
    }

    @Override
    @Transactional
    public void delRules(String id) {
        storeFreightRulesService.delete(id); //删除模板规则关联数据
        storeFreightRulesdetailService.clear(Cnd.where("templateId", "=", storeFreightRulesService.fetch(id).getTemplateId())); //删除模板规则明细关联数据
    }

    /**
     * 启用禁用
     *
     * @param id 模板ID
     */
    @Override
    @Transactional
    public void enabled(String id) {
        Store_freight storeFreight = this.fetch(id);
        if (true == storeFreight.isEnabled()) {
            storeFreight.setEnabled(false);
            this.updateIgnoreNull(storeFreight);
        } else {
            storeFreight.setEnabled(true);
            this.updateIgnoreNull(storeFreight);
            this.update(Chain.make("enabled", false), Cnd.where("id", "<>", id));
        }
    }

    /**
     * 运费计算
     *
     * @param productsList  货品sku集合
     * @param provinceCode  省的Code
     * @param logisticsCode 物流公司Code
     * @param memberId      会员Id(促销的时候用)
     * @param storeId       storeId
     * @return
     */
    public int countFreight(List<StoreFreightProduct> productsList, String provinceCode, String logisticsCode, String memberId, String storeId) {
        String areaCode = "";
        Shop_area_management shopAreaManagement = shopAreaManagementService.fetch(Cnd.where("areaCode", "LIKE", "%" + provinceCode + "%"));
        if (null != shopAreaManagement) {
            areaCode = shopAreaManagement.getCode();
        }
        int freightCost = 0; //定义当前运费为0
        String goodsId = "";
        for (StoreFreightProduct product : productsList) {
            Goods_product goodsProduct = goodsProductService.fetch(Cnd.where("sku", "=", product.getSku()));  //根据商品sku参数获取商品表
            if (null != goodsProduct) {
                Goods_main goodsMain = goodsService.fetch(goodsProduct.getGoodsId()); //根据商品规格id获取商品主表
                if (null != goodsMain) {
                    Goods_class goodsClass = goodsClassService.fetch(goodsMain.getClassId()); //根据商品主表关联商品分类表
                    if (null != goodsClass) {
                        goodsId = goodsClass.getId();
                    }
                }
            }
            Store_freight storeFreight = this.fetch(Cnd.where("enabled", "=", true).and("storeId", "=", storeId)); //获取当前启用模板
            if (null == storeFreight) {
                Store_freight storeFreightTemple = new Store_freight();
                storeFreightTemple.setTemplateName("默认模板");
                if ("".equals(StringUtil.getStoreUid())) {
                    storeFreightTemple.setStoreId("2017060000000001");
                } else {
                    storeFreightTemple.setStoreId(StringUtil.getStoreUid());
                }
                storeFreightTemple.setAddUnit(1);
                storeFreightTemple.setAddCost(500);
                storeFreightTemple.setDefaultUnit(1);
                storeFreightTemple.setDefautAffix(1000);
                storeFreightTemple.setEnabled(true);
                storeFreightTemple.setBillingType("1");
                storeFreightTemple.setOpAt((int) (System.currentTimeMillis() / 1000));
                storeFreightTemple.setOpBy(StringUtil.getUid());
                storeFreight = this.insert(storeFreightTemple);
            }
            Cnd cnd = Cnd.NEW();//根据启用运费模板ID、商品分类、片区Code、物流公司获取所对应模板
            SqlExpressionGroup groupGoodsId = new SqlExpressionGroup();//商品分类、片区、物流公司都要满足相等或者--不限--  且 商品分类ID和片区为必传字段
            SqlExpression sqlGoodsId1 = Cnd.exp("goodsId", "=", goodsId);
            SqlExpression sqlGoodsId2 = Cnd.exp("goodsId", "=", "");
            groupGoodsId.or(sqlGoodsId1);
            groupGoodsId.or(sqlGoodsId2);
            cnd.and(groupGoodsId);
            SqlExpressionGroup groupAreaCode = new SqlExpressionGroup();
            SqlExpression sqlAreaCode1 = Cnd.exp("areaCode", "=", areaCode);
            SqlExpression sqlAreaCode2 = Cnd.exp("areaCode", "=", "");
            groupAreaCode.or(sqlAreaCode1);
            groupAreaCode.or(sqlAreaCode2);
            cnd.and(groupAreaCode);

            SqlExpressionGroup grouplogisticsCode = new SqlExpressionGroup();
            SqlExpression sqlLogisticsCode1 = Cnd.exp("logisticsCode", "=", logisticsCode);
            SqlExpression sqlLogisticsCode2 = Cnd.exp("logisticsCode", "=", "");
            grouplogisticsCode.or(sqlLogisticsCode1);
            grouplogisticsCode.or(sqlLogisticsCode2);
            cnd.and(grouplogisticsCode);
            Store_freight_rulesdetail rulesdetail = storeFreightRulesdetailService.fetch(cnd.and("templateId", "=", storeFreight.getId()));
            if (null == rulesdetail) { //判断当前运费规则明细表查询是否为空
                String billingType = storeFreight.getBillingType(); //为空则采用默认模板   获取当前模板计费方式
                if ("1".equals(billingType)) {
                    if (product.getNum() <= storeFreight.getDefaultUnit()) {//当前计费方式为按件数
                        freightCost += storeFreight.getDefautAffix();
                    } else {
                        int addCost = (int) Math.ceil((product.getNum() - storeFreight.getDefaultUnit()) / storeFreight.getAddUnit());
                        freightCost += storeFreight.getDefautAffix() + addCost * storeFreight.getAddCost();
                    }
                } else if ("2".equals(billingType)) {
                    int weightSum = goodsProduct.getWeight() * product.getNum();//当前计费方式为按重量、购买商品总重量为
                    if (weightSum <= storeFreight.getDefaultUnit()) {
                        freightCost += storeFreight.getDefautAffix();
                    } else {
                        int addCost = (int) Math.ceil((weightSum - storeFreight.getDefaultUnit()) / storeFreight.getAddUnit());
                        freightCost += storeFreight.getDefautAffix() + addCost * storeFreight.getAddCost();
                    }
                } else if ("3".equals(billingType)) {
                    //当前计费方式为按体积 、商品总体积为
                    int length = goodsProduct.getLength();
                    int width = goodsProduct.getWidth();
                    int height = goodsProduct.getHeight();
                    int volumeSum = length * width * height * product.getNum();
                    if (volumeSum <= storeFreight.getDefaultUnit()) {
                        freightCost += storeFreight.getDefautAffix();
                    } else {
                        int addCost = (int) Math.ceil((volumeSum - storeFreight.getDefaultUnit()) / storeFreight.getAddUnit());
                        freightCost += storeFreight.getDefautAffix() + addCost * storeFreight.getAddCost();
                    }
                }
            } else {
                String valuationType = rulesdetail.getValuationType(); //不为空则获取当前模板规则明细的计费方式
                if ("1".equals(valuationType)) {
                    if (product.getNum() <= rulesdetail.getFirstUnit()) {//当前计费方式为按件数
                        freightCost += rulesdetail.getFirstCost();
                    } else {
                        int addCost = (int) Math.ceil((product.getNum() - rulesdetail.getFirstUnit()) / rulesdetail.getContinueUnit());
                        freightCost += rulesdetail.getFirstCost() + addCost * rulesdetail.getContinueCost();
                    }
                } else if ("2".equals(valuationType)) {
                    int weightSum = goodsProduct.getWeight() * product.getNum(); //当前计费方式为按重量 、当前商品总重量为
                    if (weightSum <= rulesdetail.getFirstUnit()) {
                        freightCost += rulesdetail.getFirstCost();
                    } else {
                        int addCost = (int) Math.ceil((weightSum - rulesdetail.getFirstUnit()) / rulesdetail.getContinueUnit());
                        freightCost += rulesdetail.getFirstCost() + addCost * rulesdetail.getContinueCost();
                    }
                } else if ("3".equals(valuationType)) {
                    int length = goodsProduct.getLength();//当前计费方式为按体积、商品总体积为
                    int width = goodsProduct.getWidth();
                    int height = goodsProduct.getHeight();
                    int volumeSum = length * width * height * product.getNum();
                    if (volumeSum <= rulesdetail.getFirstUnit()) {
                        freightCost += rulesdetail.getFirstCost();
                    } else {
                        int addCost = (int) Math.ceil((volumeSum - rulesdetail.getFirstUnit()) / rulesdetail.getContinueUnit());
                        freightCost += rulesdetail.getFirstCost() + addCost * rulesdetail.getContinueCost();
                    }
                }
            }
        }
        return freightCost;//当前商户运费总和
    }



    /**
     * 运费计算
     *商户
     * @param productsList  货品sku集合
     * @param provinceCode  省的Code
     * @param logisticsCode 物流公司Code
     * @param memberId      会员Id(促销的时候用)
     * @param storeId       storeId
     * @return
     */
    public int storeCountFreight(List<StoreFreightProduct> productsList, String provinceCode, String logisticsCode, String memberId, String storeId) {
        String areaCode = "";
        Shop_area_management shopAreaManagement = shopAreaManagementService.fetch(Cnd.where("areaCode", "LIKE", "%" + provinceCode + "%"));
        if (null != shopAreaManagement) {
            areaCode = shopAreaManagement.getCode();
        }
        int freightCost = 0; //定义当前运费为0
        String goodsId = "";
        for (StoreFreightProduct product : productsList) {
            Goods_product goodsProduct = goodsProductService.fetch(Cnd.where("sku", "=", product.getSku()));  //根据商品sku参数获取商品表
            if (null != goodsProduct) {
                Goods_main goodsMain = goodsService.fetch(goodsProduct.getGoodsId()); //根据商品规格id获取商品主表
                if (null != goodsMain) {
                    Goods_class goodsClass = goodsClassService.fetch(goodsMain.getClassId()); //根据商品主表关联商品分类表
                    if (null != goodsClass) {
                        goodsId = goodsClass.getId();
                    }
                }
            }
            Store_freight storeFreight = this.fetch(Cnd.where("enabled", "=", true).and("storeId", "=", storeId)); //获取当前启用模板
            if (null == storeFreight) {
                Store_freight storeFreightTemple = new Store_freight();
                storeFreightTemple.setTemplateName("默认模板");
                if ("".equals(StringUtil.getStoreUid())) {
                    storeFreightTemple.setStoreId(storeId);
                } else {
                    storeFreightTemple.setStoreId(StringUtil.getStoreUid());
                }
                storeFreightTemple.setAddUnit(1);
                storeFreightTemple.setAddCost(500);
                storeFreightTemple.setDefaultUnit(1);
                storeFreightTemple.setDefautAffix(1000);
                storeFreightTemple.setEnabled(true);
                storeFreightTemple.setBillingType("1");
                storeFreightTemple.setOpAt((int) (System.currentTimeMillis() / 1000));
                storeFreightTemple.setOpBy(StringUtil.getUid());
                storeFreight = this.insert(storeFreightTemple);
            }
            Cnd cnd = Cnd.NEW();//根据启用运费模板ID、商品分类、片区Code、物流公司获取所对应模板
            SqlExpressionGroup groupGoodsId = new SqlExpressionGroup();//商品分类、片区、物流公司都要满足相等或者--不限--  且 商品分类ID和片区为必传字段
            SqlExpression sqlGoodsId1 = Cnd.exp("goodsId", "=", goodsId);
            SqlExpression sqlGoodsId2 = Cnd.exp("goodsId", "=", "");
            groupGoodsId.or(sqlGoodsId1);
            groupGoodsId.or(sqlGoodsId2);
            cnd.and(groupGoodsId);
            SqlExpressionGroup groupAreaCode = new SqlExpressionGroup();
            SqlExpression sqlAreaCode1 = Cnd.exp("areaCode", "=", areaCode);
            SqlExpression sqlAreaCode2 = Cnd.exp("areaCode", "=", "");
            groupAreaCode.or(sqlAreaCode1);
            groupAreaCode.or(sqlAreaCode2);
            cnd.and(groupAreaCode);

            SqlExpressionGroup grouplogisticsCode = new SqlExpressionGroup();
            SqlExpression sqlLogisticsCode1 = Cnd.exp("logisticsCode", "=", logisticsCode);
            SqlExpression sqlLogisticsCode2 = Cnd.exp("logisticsCode", "=", "");
            grouplogisticsCode.or(sqlLogisticsCode1);
            grouplogisticsCode.or(sqlLogisticsCode2);
            cnd.and(grouplogisticsCode);
            Store_freight_rulesdetail rulesdetail = storeFreightRulesdetailService.fetch(cnd.and("templateId", "=", storeFreight.getId()));
            if (null == rulesdetail) { //判断当前运费规则明细表查询是否为空
                String billingType = storeFreight.getBillingType(); //为空则采用默认模板   获取当前模板计费方式
                if ("1".equals(billingType)) {
                    if (product.getNum() <= storeFreight.getDefaultUnit()) {//当前计费方式为按件数
                        freightCost += storeFreight.getDefautAffix();
                    } else {
                        int addCost = (int) Math.ceil((product.getNum() - storeFreight.getDefaultUnit()) / storeFreight.getAddUnit());
                        freightCost += storeFreight.getDefautAffix() + addCost * storeFreight.getAddCost();
                    }
                } else if ("2".equals(billingType)) {
                    int weightSum = goodsProduct.getWeight() * product.getNum();//当前计费方式为按重量、购买商品总重量为
                    if (weightSum <= storeFreight.getDefaultUnit()) {
                        freightCost += storeFreight.getDefautAffix();
                    } else {
                        int addCost = (int) Math.ceil((weightSum - storeFreight.getDefaultUnit()) / storeFreight.getAddUnit());
                        freightCost += storeFreight.getDefautAffix() + addCost * storeFreight.getAddCost();
                    }
                } else if ("3".equals(billingType)) {
                    //当前计费方式为按体积 、商品总体积为
                    int length = goodsProduct.getLength();
                    int width = goodsProduct.getWidth();
                    int height = goodsProduct.getHeight();
                    int volumeSum = length * width * height * product.getNum();
                    if (volumeSum <= storeFreight.getDefaultUnit()) {
                        freightCost += storeFreight.getDefautAffix();
                    } else {
                        int addCost = (int) Math.ceil((volumeSum - storeFreight.getDefaultUnit()) / storeFreight.getAddUnit());
                        freightCost += storeFreight.getDefautAffix() + addCost * storeFreight.getAddCost();
                    }
                }
            } else {
                String valuationType = rulesdetail.getValuationType(); //不为空则获取当前模板规则明细的计费方式
                if ("1".equals(valuationType)) {
                    if (product.getNum() <= rulesdetail.getFirstUnit()) {//当前计费方式为按件数
                        freightCost += rulesdetail.getFirstCost();
                    } else {
                        int addCost = (int) Math.ceil((product.getNum() - rulesdetail.getFirstUnit()) / rulesdetail.getContinueUnit());
                        freightCost += rulesdetail.getFirstCost() + addCost * rulesdetail.getContinueCost();
                    }
                } else if ("2".equals(valuationType)) {
                    int weightSum = goodsProduct.getWeight() * product.getNum(); //当前计费方式为按重量 、当前商品总重量为
                    if (weightSum <= rulesdetail.getFirstUnit()) {
                        freightCost += rulesdetail.getFirstCost();
                    } else {
                        int addCost = (int) Math.ceil((weightSum - rulesdetail.getFirstUnit()) / rulesdetail.getContinueUnit());
                        freightCost += rulesdetail.getFirstCost() + addCost * rulesdetail.getContinueCost();
                    }
                } else if ("3".equals(valuationType)) {
                    int length = goodsProduct.getLength();//当前计费方式为按体积、商品总体积为
                    int width = goodsProduct.getWidth();
                    int height = goodsProduct.getHeight();
                    int volumeSum = length * width * height * product.getNum();
                    if (volumeSum <= rulesdetail.getFirstUnit()) {
                        freightCost += rulesdetail.getFirstCost();
                    } else {
                        int addCost = (int) Math.ceil((volumeSum - rulesdetail.getFirstUnit()) / rulesdetail.getContinueUnit());
                        freightCost += rulesdetail.getFirstCost() + addCost * rulesdetail.getContinueCost();
                    }
                }
            }
        }
        return freightCost;//当前商户运费总和
    }
}
