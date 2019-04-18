/* platformStoreData01 */
insert into store_role (id, name, code, disabled, defaultValue, note, opBy, opAt, delFlag) values('2017060000000001','店铺默认角色','default','0','1',NULL,'','1489043920','0')
/* platformStoreData02 */
insert into store_role_menu(roleId,menuId) SELECT '2017060000000001',id FROM store_menu
/* platformStoreData03 */
insert into store_type (id, name, note, disabled, defaultValue, opBy, opAt, delFlag) values('2017060000000001','普通店铺','普通店铺','0','1','','1489370933','0')
/* platformStoreData04 */
insert into store_level (id, name, goodsLimit, level, price, rate, note, opBy, opAt, delFlag) values('2017060000000001','黄金店铺','100','2','2000','0.0300000000','','','1489472702','0')
/* platformStoreData05 */
insert into store_level (id, name, goodsLimit, level, price, rate, note, opBy, opAt, delFlag) values('2017060000000002','白金店铺','50','1','1000','0.0500000000','','','1489472632','0')
/* platformStoreData06 */
insert into store_config (id, regNote, nendCompanyInfo, nendBankInfo, nendAlipayInfo, nendWechatInfo, opBy, opAt, delFlag) values('system','','1','1','1','1','','1488965634','0')
/* platformStoreData07 */
insert into store_class (id, parentId, path, name, deposit, location, hasChildren, opBy, opAt, delFlag) values('2017060000000001','','0001','默认分类','0','1','0','','1493022589','0')
/* platformStoreData08 */
insert into account_info (id, nickname, name, sex, b_year, b_month, b_day, astro, provinceId, cityId, areaId, jx_province, jx_city, jx_county, jx_town, opBy, opAt, delFlag) values('2017060000000001','自营管理员',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','1493022722','0')
/* platformStoreData09 */
insert into account_user (id, loginname, email, mobile, password, salt, disabled, accountId, opBy, opAt, delFlag) values('2017060000000001','store','wizzer@@qq.com','13359011952','c2EJeka2KTDfJ0JPW4gG9tk0eHdqhC8hcaU0bTquQxc=','2pMKEsNGF3vOgwjXWlF4iQ==','0','2017060000000001','','1493022722','0')
/* platformStoreData10 */
insert into store_main (id, mallId, userId, storeName, classId, levelId, typeId, joininYear, endAt, storeAddress, storeTel, storePostcode, storeProvince, storeCity, storeCounty, storeTown, disabled, recommend, opBy, opAt, delFlag,self) values('2017060000000001','','2017060000000001','平台自营','2017060000000001','2017060000000001','2017060000000001','1',NULL,'蜀山区','13359011952','342301','安徽省','合肥市','蜀山区',NULL,'0','0','','1493022722','0','1')
/* platformStoreData11 */
insert into store_user (id, accountId, storeId, customMenu, loginTheme, loginSidebar, loginBoxed, loginScroll, loginPjax, opBy, opAt, delFlag) values('2017060000000001','2017060000000001','2017060000000001',NULL,NULL,'0','0','0','0','','1493022722','0')
/* platformStoreData12 */
insert into store_user_role (userId, roleId) values('2017060000000001','2017060000000001')
/* platformStoreData13 */
insert into store_company (id, mallId, storeId, companyName, companyAddress, companyProvince, companyCity, companyCounty, companyTown, companyPhone, contactsName, contactsPhone, contactsEmail, companyEmployeeCount, companyRegisteredCapital, businessLicenceNumber, businessLicenceStart, businessLicenceEnd, businessSphere, businessLicenceupImage, organizationCode, organizationCodeImage, generalTaxpayerImage, taxRegistrationCertificate, taxpayerId, taxRegistrationCertificateImage, bankAccountName, bankAccountNumber, bankName, bankCode, bankAddress, bankLicenceImage, alipayName, alipayAccountNumber, weichatName, weichatAccountNumber, opBy, opAt, delFlag) values('2017060000000001',NULL,'2017060000000001','平台自营',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0')