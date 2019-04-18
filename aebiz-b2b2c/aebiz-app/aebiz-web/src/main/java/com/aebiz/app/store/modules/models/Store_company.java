package com.aebiz.app.store.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 店铺公司信息表
 * Created by wizzer on 2017/3/1.
 */
@Table("store_company")
public class Store_company extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("商场ID（预留）")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String mallId;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @Column
    @Comment("公司名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String companyName;

    @Column
    @Comment("公司地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String companyAddress;

    @Column
    @Comment("所在省")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String companyProvince;

    @Column
    @Comment("所在市")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String companyCity;

    @Column
    @Comment("所在县区")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String companyCounty;

    @Column
    @Comment("所在街道")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String companyTown;

    @Column
    @Comment("公司电话")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String companyPhone;

    @Column
    @Comment("联系人")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String contactsName;

    @Column
    @Comment("联系电话")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String contactsPhone;

    @Column
    @Comment("电子邮箱")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String contactsEmail;

    @Column
    @Comment("员工总数")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String companyEmployeeCount;

    @Column
    @Comment("注册资金")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String companyRegisteredCapital;//单位: 万元

    @Column
    @Comment("营业执照号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String businessLicenceNumber;

    @Column
    @Comment("营业执照有效期起")
    @ColDefine(type = ColType.VARCHAR, width = 10)
    private String businessLicenceStart;

    @Column
    @Comment("营业执照有效期至")
    @ColDefine(type = ColType.VARCHAR, width = 10)
    private String businessLicenceEnd;

    @Column
    @Comment("法定经营范围")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String businessSphere;

    @Column
    @Comment("营业执照电子版")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String businessLicenceupImage;

    @Column
    @Comment("组织机构代码")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String organizationCode;

    @Column
    @Comment("组织机构代码证电子版")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String organizationCodeImage;

    @Column
    @Comment("一般纳税人证明")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String generalTaxpayerImage;

    @Column
    @Comment("税务登记证号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String taxRegistrationCertificate;

    @Column
    @Comment("纳税人识别号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String taxpayerId;

    @Column
    @Comment("税务登记证号电子版")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String taxRegistrationCertificateImage;

    @Column
    @Comment("银行开户名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String bankAccountName;

    @Column
    @Comment("银行账号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String bankAccountNumber;

    @Column
    @Comment("开户银行支行名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String bankName;

    @Column
    @Comment("支行联行号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String bankCode;

    @Column
    @Comment("开户银行所在地")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String bankAddress;

    @Column
    @Comment("开户银行许可证电子版")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String bankLicenceImage;

    @Column
    @Comment("支付宝姓名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String alipayName;

    @Column
    @Comment("支付宝账号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String alipayAccountNumber;

    @Column
    @Comment("微信姓名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String weichatName;

    @Column
    @Comment("微信账号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String weichatAccountNumber;

    @One(field = "storeId")
    private Store_main storeMain;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMallId() {
        return mallId;
    }

    public void setMallId(String mallId) {
        this.mallId = mallId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyProvince() {
        return companyProvince;
    }

    public void setCompanyProvince(String companyProvince) {
        this.companyProvince = companyProvince;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyCounty() {
        return companyCounty;
    }

    public void setCompanyCounty(String companyCounty) {
        this.companyCounty = companyCounty;
    }

    public String getCompanyTown() {
        return companyTown;
    }

    public void setCompanyTown(String companyTown) {
        this.companyTown = companyTown;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getContactsEmail() {
        return contactsEmail;
    }

    public void setContactsEmail(String contactsEmail) {
        this.contactsEmail = contactsEmail;
    }

    public String getCompanyEmployeeCount() {
        return companyEmployeeCount;
    }

    public void setCompanyEmployeeCount(String companyEmployeeCount) {
        this.companyEmployeeCount = companyEmployeeCount;
    }

    public String getCompanyRegisteredCapital() {
        return companyRegisteredCapital;
    }

    public void setCompanyRegisteredCapital(String companyRegisteredCapital) {
        this.companyRegisteredCapital = companyRegisteredCapital;
    }

    public String getBusinessLicenceNumber() {
        return businessLicenceNumber;
    }

    public void setBusinessLicenceNumber(String businessLicenceNumber) {
        this.businessLicenceNumber = businessLicenceNumber;
    }

    public String getBusinessLicenceStart() {
        return businessLicenceStart;
    }

    public void setBusinessLicenceStart(String businessLicenceStart) {
        this.businessLicenceStart = businessLicenceStart;
    }

    public String getBusinessLicenceEnd() {
        return businessLicenceEnd;
    }

    public void setBusinessLicenceEnd(String businessLicenceEnd) {
        this.businessLicenceEnd = businessLicenceEnd;
    }

    public String getBusinessSphere() {
        return businessSphere;
    }

    public void setBusinessSphere(String businessSphere) {
        this.businessSphere = businessSphere;
    }

    public String getBusinessLicenceupImage() {
        return businessLicenceupImage;
    }

    public void setBusinessLicenceupImage(String businessLicenceupImage) {
        this.businessLicenceupImage = businessLicenceupImage;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationCodeImage() {
        return organizationCodeImage;
    }

    public void setOrganizationCodeImage(String organizationCodeImage) {
        this.organizationCodeImage = organizationCodeImage;
    }

    public String getGeneralTaxpayerImage() {
        return generalTaxpayerImage;
    }

    public void setGeneralTaxpayerImage(String generalTaxpayerImage) {
        this.generalTaxpayerImage = generalTaxpayerImage;
    }

    public String getTaxRegistrationCertificate() {
        return taxRegistrationCertificate;
    }

    public void setTaxRegistrationCertificate(String taxRegistrationCertificate) {
        this.taxRegistrationCertificate = taxRegistrationCertificate;
    }

    public String getTaxpayerId() {
        return taxpayerId;
    }

    public void setTaxpayerId(String taxpayerId) {
        this.taxpayerId = taxpayerId;
    }

    public String getTaxRegistrationCertificateImage() {
        return taxRegistrationCertificateImage;
    }

    public void setTaxRegistrationCertificateImage(String taxRegistrationCertificateImage) {
        this.taxRegistrationCertificateImage = taxRegistrationCertificateImage;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankLicenceImage() {
        return bankLicenceImage;
    }

    public void setBankLicenceImage(String bankLicenceImage) {
        this.bankLicenceImage = bankLicenceImage;
    }

    public String getAlipayName() {
        return alipayName;
    }

    public void setAlipayName(String alipayName) {
        this.alipayName = alipayName;
    }

    public String getAlipayAccountNumber() {
        return alipayAccountNumber;
    }

    public void setAlipayAccountNumber(String alipayAccountNumber) {
        this.alipayAccountNumber = alipayAccountNumber;
    }

    public String getWeichatName() {
        return weichatName;
    }

    public void setWeichatName(String weichatName) {
        this.weichatName = weichatName;
    }

    public String getWeichatAccountNumber() {
        return weichatAccountNumber;
    }

    public void setWeichatAccountNumber(String weichatAccountNumber) {
        this.weichatAccountNumber = weichatAccountNumber;
    }

    public Store_main getStoreMain() {
        return storeMain;
    }

    public void setStoreMain(Store_main storeMain) {
        this.storeMain = storeMain;
    }
}
