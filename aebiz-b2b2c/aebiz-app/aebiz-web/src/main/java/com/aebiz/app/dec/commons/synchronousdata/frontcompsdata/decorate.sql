-- ----------------------------
-- Table structure for dec_component
-- ----------------------------
DROP TABLE IF EXISTS `dec_component`;
CREATE TABLE `dec_component` (
  `id` varchar(32) NOT NULL DEFAULT '' COMMENT 'ID',
  `versionType` int(32) DEFAULT '1' COMMENT '版本类型',
  `compCategoryUuid` varchar(255) DEFAULT NULL COMMENT '所属组件分类',
  `compType` int(32) DEFAULT '1' COMMENT '组件类型',
  `useType` int(32) DEFAULT '1' COMMENT '组件使用类型',
  `compId` varchar(255) DEFAULT NULL COMMENT '组件编号',
  `competence` char(1) DEFAULT NULL,
  `compName` varchar(255) DEFAULT NULL COMMENT '组件名称',
  `compImage` varchar(255) DEFAULT NULL COMMENT '组件图标',
  `classFullName` varchar(255) DEFAULT NULL COMMENT '组件类的全路径',
  `description` varchar(255) DEFAULT NULL COMMENT '组件描述',
  `pHeight` int(32) DEFAULT '0' COMMENT '弹层高度',
  `pWidth` int(32) DEFAULT '0' COMMENT '弹层宽度',
  `disabled` tinyint(1) DEFAULT NULL COMMENT '是否启用',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dec_component
-- ----------------------------
INSERT INTO `dec_component` VALUES ('0227529d55134e35887c87ab5cb79f5c', '1', 'ca488a6ebb28424fb127f8e06d55359d', '1', '1', 'top', '1', '顶部组件', null, 'com.aebiz.app.dec.commons.comps.top.vo.TopCompModel', '顶部组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498699881', '0');
INSERT INTO `dec_component` VALUES ('07033a87e2804b20817ede7470f6233f', '1', 'ca488a6ebb28424fb127f8e06d55359d', '1', '1', 'qrcode', '1', '二维码', null, 'com.aebiz.app.dec.commons.comps.qrcode.vo.QRCodeCompModel', '二维码', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027355', '0');
INSERT INTO `dec_component` VALUES ('11371e159d7b4bc382b2a0641246ee85', '1', 'dc30a5f32352443fbef9aa6e22bceb1b', '1', '1', 'recommendproduct', '1', '商品推荐', null, 'com.aebiz.app.dec.commons.comps.recommendproduct.vo.RecommendProductCompModel', '商品推荐', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027364', '0');
INSERT INTO `dec_component` VALUES ('184e1c62f8184fb0add9e7048536bdeb', '1', '43f23f90523b4acba656be0357a6f71b', '1', '1', 'searchShowCategory', '1', '搜索显示分类', null, 'com.aebiz.app.dec.commons.comps.searchshowcategory.vo.SearchShowCategoryCompModel', '搜索显示分类', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499396771', '0');
INSERT INTO `dec_component` VALUES ('296cfb04ad404385b52b340caef89f63', '1', '43f23f90523b4acba656be0357a6f71b', '1', '1', 'fullSearchComp', '1', '全文检索组件', null, 'com.aebiz.app.dec.commons.comps.fullsearch.vo.FullSearchCompModel', '全文检索组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027375', '0');
INSERT INTO `dec_component` VALUES ('4cd0ded631c045f0a66476be55f139f3', '1', 'ca488a6ebb28424fb127f8e06d55359d', '1', '1', 'imagetexteditor', '1', '图文编辑器', null, 'com.aebiz.app.dec.commons.comps.imagetexteditor.vo.ImageTextEditorCompModel', '图文编辑器', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027384', '0');
INSERT INTO `dec_component` VALUES ('4dc9bf6c470043aaac7f0115f47a01c6', '1', 'dc30a5f32352443fbef9aa6e22bceb1b', '1', '1', 'ProductMain', '1', '产品主要信息', null, 'com.aebiz.app.dec.commons.comps.productmain.vo.ProductMainCompModel', '产品主要信息', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499327657', '0');
INSERT INTO `dec_component` VALUES ('5609c78815a6424b81e09da2645423b1', '1', 'dc30a5f32352443fbef9aa6e22bceb1b', '1', '1', 'choosewellproduct', '1', '精选商品', null, 'com.aebiz.app.dec.commons.comps.choosewellproduct.vo.ChooseWellProductCompModel', '精选商品', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027395', '0');
INSERT INTO `dec_component` VALUES ('5900766e85524417a7a173d6e1333630', '1', 'ca488a6ebb28424fb127f8e06d55359d', '1', '1', 'share', '1', '分享', null, 'com.aebiz.app.dec.commons.comps.share.vo.ShareCompModel', '分享组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027406', '0');
INSERT INTO `dec_component` VALUES ('6124e841b57f4dbd8a4bc119f44358be', '1', 'dc30a5f32352443fbef9aa6e22bceb1b', '1', '1', 'productdetail', '1', '商品详情选项卡', null, 'com.aebiz.app.dec.commons.comps.productdetail.vo.ProductDetailCompModel', '商品详情选项卡', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499950452', '0');
INSERT INTO `dec_component` VALUES ('61bcb66b2cb148acadcfbcf5a5d5101e', '1', 'ef2cca224b8c4fc09d60275c1ba9d966', '1', '1', 'columnNavigation', '1', '栏目导航', null, 'com.aebiz.app.dec.commons.comps.columnnavigation.vo.ColumnNavigationCompModel', '栏目导航', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027416', '0');
INSERT INTO `dec_component` VALUES ('707baaf25f56419eba17a73d416fdf53', '1', 'dc30a5f32352443fbef9aa6e22bceb1b', '1', '1', 'SlideProducts', '1', '商品轮播组件', null, 'com.aebiz.app.dec.commons.comps.slideproducts.vo.SlideProductsCompModel', '商品轮播组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027426', '0');
INSERT INTO `dec_component` VALUES ('738c12dc331b4225ab8a99dd32306f40', '1', 'ef2cca224b8c4fc09d60275c1ba9d966', '1', '1', 'noticeComp', '1', '公告栏组件', null, 'com.aebiz.app.dec.commons.comps.notice.vo.NoticeCompModel', '公告栏组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027439', '0');
INSERT INTO `dec_component` VALUES ('774394ec3cea4354a85ebdcaab9f19d6', '2', 'ca488a6ebb28424fb127f8e06d55359d', '2', '1', 'mBottomBar', '1', '底部导航', null, 'com.aebiz.app.dec.commons.comps.userdefined.vo.UserDefinedModel', '底部导航', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498803225', '0');
INSERT INTO `dec_component` VALUES ('794337ba183347289dfee67ff9f49628', '1', 'ca488a6ebb28424fb127f8e06d55359d', '1', '1', 'poplogin', '1', '弹出登录框', null, 'com.aebiz.app.dec.commons.comps.poplogin.vo.PopLoginCompModel', '弹出登录框', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498630652', '0');
INSERT INTO `dec_component` VALUES ('805c36c6078b480e8ecbb11bb9287ea2', '1', 'ef2cca224b8c4fc09d60275c1ba9d966', '1', '1', 'bottomHelpCenter', '1', '底部帮助中心', null, 'com.aebiz.app.dec.commons.comps.bottomHelpCenter.vo.BottomHelpCenterCompModel', '底部帮助中心', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027455', '0');
INSERT INTO `dec_component` VALUES ('8ceaecea46354ac1b50753c7cc242284', '1', 'ca488a6ebb28424fb127f8e06d55359d', '1', '1', 'sideEnterance', '1', '快速入口', null, 'com.aebiz.app.dec.commons.comps.sideenterance.vo.SideEnteranceCompModel', '快速入口', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027468', '0');
INSERT INTO `dec_component` VALUES ('96f057f85f794991aa8c012f7630eb6b', '1', 'dc30a5f32352443fbef9aa6e22bceb1b', '1', '1', 'hotproduct', '1', '热销组件', null, 'com.aebiz.app.dec.commons.comps.hotproduct.vo.HotProductCompModel', '热销组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027479', '0');
INSERT INTO `dec_component` VALUES ('a564ba204bdb407f80e5972e499bea87', '1', 'ca488a6ebb28424fb127f8e06d55359d', '1', '1', 'floorLable', '1', '楼层标签', null, 'com.aebiz.app.dec.commons.comps.floorlable.vo.FloorLableCompModel', '楼层组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027495', '0');
INSERT INTO `dec_component` VALUES ('a84c0171435845bead2069c10964084d', '1', 'e2abc9d060ef4277b7be3055aa46920a', '1', '1', 'picturead', '1', '图片广告', null, 'com.aebiz.app.dec.commons.comps.picturead.vo.PictureAdCompModel', '图片广告', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027510', '0');
INSERT INTO `dec_component` VALUES ('b90e4e16c95f44589395d93ed134361b', '1', 'dc30a5f32352443fbef9aa6e22bceb1b', '1', '1', 'collectionproduct', '1', '商品收藏', null, 'com.aebiz.app.dec.commons.comps.collectionproduct.vo.CollectionProductCompModel', '商品收藏', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499411035', '0');
INSERT INTO `dec_component` VALUES ('bbb16c095f7740f0a4b0eff6beb56841', '1', 'e2abc9d060ef4277b7be3055aa46920a', '1', '1', 'sliadcomp', '1', '轮播广告', null, 'com.aebiz.app.dec.commons.comps.slideads.vo.SlideAdsCompModel', '轮播广告', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027522', '0');
INSERT INTO `dec_component` VALUES ('bf2570299aca415dbba323b731ffd517', '1', '43f23f90523b4acba656be0357a6f71b', '1', '1', 'screen', '1', '筛选条件组件', null, 'com.aebiz.app.dec.commons.comps.screen.vo.ScreenCompModel', '筛选条件组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498458366', '0');
INSERT INTO `dec_component` VALUES ('bf79c9b9fb354c1b8526911e0ef0a6ec', '1', '43f23f90523b4acba656be0357a6f71b', '1', '1', 'productList', '1', '商品列表组件', null, 'com.aebiz.app.dec.commons.comps.productlist.vo.ProductListCompModel', '商品列表组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027538', '0');
INSERT INTO `dec_component` VALUES ('c47f80da90d849ca84387e7dbf90fdb4', '1', '43f23f90523b4acba656be0357a6f71b', '1', '1', 'category', '1', '前台分类组件', null, 'com.aebiz.app.dec.commons.comps.category.vo.CategoryCompModel', '前台分类组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027589', '0');
INSERT INTO `dec_component` VALUES ('d6461c869efd46519302c46ca6139dac', '1', 'ef2cca224b8c4fc09d60275c1ba9d966', '1', '1', 'navigation', '1', '导航栏组件', null, 'com.aebiz.app.dec.commons.comps.navigation.vo.NavigationCompModel', '导航栏组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027600', '0');
INSERT INTO `dec_component` VALUES ('d6569925f6ec4d5db32f66defe71b9cf', '1', 'ef2cca224b8c4fc09d60275c1ba9d966', '1', '1', 'usefulLinksComp', '1', '友情链接组件', null, 'com.aebiz.app.dec.commons.comps.usefullinks.vo.UsefulLinksCompModel', '友情链接组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027610', '0');
INSERT INTO `dec_component` VALUES ('e6a80f87f0b24181b514d493d20fa3fa', '1', 'ef2cca224b8c4fc09d60275c1ba9d966', '1', '1', 'floor', '1', '楼层组件', null, 'com.aebiz.app.dec.commons.comps.floor.vo.FloorCompModel', '楼层组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027623', '0');
INSERT INTO `dec_component` VALUES ('eadacfebf4c14613b2de85dfbc6954cb', '1', 'ef2cca224b8c4fc09d60275c1ba9d966', '1', '1', 'bottom', '1', '底部组件', null, 'com.aebiz.app.dec.commons.comps.bottom.vo.BottomCompModel', '底部组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027635', '0');
INSERT INTO `dec_component` VALUES ('ee893795ac6e4bdda148b2e82ff8d21d', '1', '0ccaf6e4e6b244e99187414990030cad', '1', '1', 'reputation', '1', '信誉', null, 'com.aebiz.app.dec.commons.comps.reputation.vo.ReputationCompModel', '信誉', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1500025703', '0');
INSERT INTO `dec_component` VALUES ('f0e252c51b1d4619ba7c8923ddaae2ae', '1', 'ca488a6ebb28424fb127f8e06d55359d', '1', '1', 'cartComp', '1', '购物车组件', null, 'com.aebiz.app.dec.commons.comps.cartcomp.vo.CartCompModel', '购物车组件', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027662', '0');
INSERT INTO `dec_component` VALUES ('f515d03b42e841a39ee63b41339fbf9e', '1', 'dc30a5f32352443fbef9aa6e22bceb1b', '1', '1', 'producttab', '1', '商品选项卡', null, 'com.aebiz.app.dec.commons.comps.producttab.vo.ProductTabCompModel', '商品选项卡', '0', '0', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027678', '0');

-- ----------------------------
-- Table structure for dec_component_class
-- ----------------------------
DROP TABLE IF EXISTS `dec_component_class`;
CREATE TABLE `dec_component_class` (
  `id` varchar(32) NOT NULL DEFAULT '' COMMENT 'ID',
  `categoryName` varchar(255) DEFAULT NULL COMMENT '分类名称',
  `description` varchar(255) DEFAULT NULL COMMENT '分类描述',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dec_component_class
-- ----------------------------
INSERT INTO `dec_component_class` VALUES ('0ccaf6e4e6b244e99187414990030cad', '商户', 'shop', '25c1fadce5094b6bbc7fa89514a8ac5b', '1500025588', '0');
INSERT INTO `dec_component_class` VALUES ('43f23f90523b4acba656be0357a6f71b', '搜索类', '搜索类', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027298', '0');
INSERT INTO `dec_component_class` VALUES ('ca488a6ebb28424fb127f8e06d55359d', '公共类', '公共类', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027326', '0');
INSERT INTO `dec_component_class` VALUES ('dc30a5f32352443fbef9aa6e22bceb1b', '商品类', '商品类', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027287', '0');
INSERT INTO `dec_component_class` VALUES ('e2abc9d060ef4277b7be3055aa46920a', '广告类', '广告类', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027276', '0');
INSERT INTO `dec_component_class` VALUES ('ef2cca224b8c4fc09d60275c1ba9d966', 'CMS', 'CMS', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498027260', '0');

-- ----------------------------
-- Table structure for dec_component_resource
-- ----------------------------
DROP TABLE IF EXISTS `dec_component_resource`;
CREATE TABLE `dec_component_resource` (
  `id` varchar(32) NOT NULL DEFAULT '' COMMENT 'ID',
  `compUuid` varchar(100) DEFAULT NULL COMMENT '组件uuid',
  `versionNo` varchar(100) DEFAULT NULL COMMENT '验证号',
  `resourceType` varchar(100) DEFAULT NULL COMMENT '资源类型',
  `resourceKey` varchar(100) DEFAULT NULL COMMENT '文件保存的key',
  `disabled` tinyint(4) DEFAULT NULL COMMENT '是否正在使用',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dec_component_resource
-- ----------------------------
INSERT INTO `dec_component_resource` VALUES ('00513e5bdef1474f8768d1148136e9a3', '96f057f85f794991aa8c012f7630eb6b', '', 'js', 'component_js_hotproduct', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497512745', '0');
INSERT INTO `dec_component_resource` VALUES ('02de0882677647ac8f7ee45f29da3e46', 'f515d03b42e841a39ee63b41339fbf9e', '', 'js', 'component_js_producttab', '1', 'ef293dd013104453ab0f10cf4b252eca', '1497234445', '0');
INSERT INTO `dec_component_resource` VALUES ('06ddb290ed254d4f8fb319c5f1ba63c6', 'a564ba204bdb407f80e5972e499bea87', '', 'html', 'component_html_floorLable', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496321163', '0');
INSERT INTO `dec_component_resource` VALUES ('0747c7f708624f3aa2b308c1cbfeac82', 'eadacfebf4c14613b2de85dfbc6954cb', '', 'js', 'component_js_bottom', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815316', '0');
INSERT INTO `dec_component_resource` VALUES ('07abd283aa004e2a9d556aac7b991fe0', 'ee893795ac6e4bdda148b2e82ff8d21d', '1.0', 'jsp', 'component_jsp_reputation', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1500025729', '0');
INSERT INTO `dec_component_resource` VALUES ('0b61b0c6eee24a49a27b1926ae26b566', '184e1c62f8184fb0add9e7048536bdeb', '1.0', 'jsp', 'component_jsp_searchShowCategory', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499396838', '0');
INSERT INTO `dec_component_resource` VALUES ('0bc47b36649f4725a52b6c43e73b4e2b', 'e6a80f87f0b24181b514d493d20fa3fa', '', 'html', 'component_html_floor', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815293', '0');
INSERT INTO `dec_component_resource` VALUES ('0c3135f2069847e1a1272cdda30cfd22', 'd6569925f6ec4d5db32f66defe71b9cf', '', 'html', 'component_html_usefulLinksComp', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815268', '0');
INSERT INTO `dec_component_resource` VALUES ('0cefd34d8c3c4b349cf65718de07ec67', 'ee893795ac6e4bdda148b2e82ff8d21d', '', 'js', 'component_js_reputation', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1500025729', '0');
INSERT INTO `dec_component_resource` VALUES ('10085479d74d45978ea81bf1a75ceca3', '0227529d55134e35887c87ab5cb79f5c', '', 'html', 'component_html_top', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497439543', '0');
INSERT INTO `dec_component_resource` VALUES ('135b00bee368482eb60d715b80ed8671', 'bbb16c095f7740f0a4b0eff6beb56841', '', 'js', 'component_js_sliadcomp', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815186', '0');
INSERT INTO `dec_component_resource` VALUES ('1570244b36be4dba862be7653fbc0242', '296cfb04ad404385b52b340caef89f63', '', 'js', 'component_js_fullSearchComp', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496814978', '0');
INSERT INTO `dec_component_resource` VALUES ('186fbe75f67142e59e905dbac042f4c9', '5900766e85524417a7a173d6e1333630', '', 'js', 'component_js_share', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815030', '0');
INSERT INTO `dec_component_resource` VALUES ('1f0d84ad3ce24ba9a4de4fbf125e5b8f', 'eadacfebf4c14613b2de85dfbc6954cb', '1.0', 'jsp', 'component_jsp_bottom', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815316', '0');
INSERT INTO `dec_component_resource` VALUES ('25efaea4d4564cc0a2be8864f392e0b8', '07033a87e2804b20817ede7470f6233f', '', 'html', 'component_html_qrcode', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496814942', '0');
INSERT INTO `dec_component_resource` VALUES ('26ba9606f8114432ac8f733fd7a1cca2', '07033a87e2804b20817ede7470f6233f', '', 'js', 'component_js_qrcode', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496814942', '0');
INSERT INTO `dec_component_resource` VALUES ('2af77e94a74e42db981e944198c236d9', '805c36c6078b480e8ecbb11bb9287ea2', '', 'js', 'component_js_bottomHelpCenter', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815119', '0');
INSERT INTO `dec_component_resource` VALUES ('2b8924962d794b188eb07e0a418dfa55', '61bcb66b2cb148acadcfbcf5a5d5101e', '1.0', 'jsp', 'component_jsp_columnNavigation', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815059', '0');
INSERT INTO `dec_component_resource` VALUES ('2cfb979722e84f3986cb5c2118015650', '4dc9bf6c470043aaac7f0115f47a01c6', '', 'html', 'component_html_ProductMain', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499327696', '0');
INSERT INTO `dec_component_resource` VALUES ('2e166873bd3647e5882bc798d2882c6e', 'b90e4e16c95f44589395d93ed134361b', '', 'js', 'component_js_collectionproduct', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499410838', '0');
INSERT INTO `dec_component_resource` VALUES ('33feeb2b96d04f1899f1c4ed95c11113', 'bf79c9b9fb354c1b8526911e0ef0a6ec', '', 'js', 'component_js_productList', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497345334', '0');
INSERT INTO `dec_component_resource` VALUES ('3424852dea2a43049fcdc1ca760b3074', '11371e159d7b4bc382b2a0641246ee85', '1.0', 'jsp', 'component_jsp_recommendproduct', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497841393', '0');
INSERT INTO `dec_component_resource` VALUES ('36222093d1a54f769dd5aa672a74e94e', '8ceaecea46354ac1b50753c7cc242284', '', 'html', 'component_html_sideEnterance', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497861074', '0');
INSERT INTO `dec_component_resource` VALUES ('375acaf1fb3f45a4a839087e9363d125', '0227529d55134e35887c87ab5cb79f5c', '', 'js', 'component_js_top', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497439543', '0');
INSERT INTO `dec_component_resource` VALUES ('3b84eaac209f4dd0adec5ebc47bf2c69', 'b90e4e16c95f44589395d93ed134361b', '1.0', 'jsp', 'component_jsp_collectionproduct', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499410838', '0');
INSERT INTO `dec_component_resource` VALUES ('43fef16569634b2fb28e91db68a38621', '4dc9bf6c470043aaac7f0115f47a01c6', '', 'js', 'component_js_ProductMain', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499327696', '0');
INSERT INTO `dec_component_resource` VALUES ('47992264d8a646fd901f00a24aa09ec8', '774394ec3cea4354a85ebdcaab9f19d6', '1.0', 'jsp', 'component_jsp_mBottomBar', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498803282', '0');
INSERT INTO `dec_component_resource` VALUES ('48c056d194954cdcbe05854819b87adc', '4cd0ded631c045f0a66476be55f139f3', '', 'js', 'component_js_imagetexteditor', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815002', '0');
INSERT INTO `dec_component_resource` VALUES ('49d6066154d246e9a7d097d945067967', '805c36c6078b480e8ecbb11bb9287ea2', '2.0', 'jsp', 'component_jsp_bottomHelpCenter', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815119', '0');
INSERT INTO `dec_component_resource` VALUES ('4b89893208ca46cdab01f11832bb558e', 'a84c0171435845bead2069c10964084d', '', 'js', 'component_js_picturead', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815161', '0');
INSERT INTO `dec_component_resource` VALUES ('529b5a165abb4e88b027441dbbb32f70', '6124e841b57f4dbd8a4bc119f44358be', '1.0', 'jsp', 'component_jsp_productdetail', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499950501', '0');
INSERT INTO `dec_component_resource` VALUES ('561477bc51c24b22864cf96080b7d2fb', 'bbb16c095f7740f0a4b0eff6beb56841', '', 'html', 'component_html_sliadcomp', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815186', '0');
INSERT INTO `dec_component_resource` VALUES ('593eabb9472044f0b6f168a141b3489a', '4dc9bf6c470043aaac7f0115f47a01c6', '1.0', 'jsp', 'component_jsp_ProductMain', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499327696', '0');
INSERT INTO `dec_component_resource` VALUES ('5c0ec2c9d1fc4b59bee612e505191547', '6124e841b57f4dbd8a4bc119f44358be', '', 'js', 'component_js_productdetail', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499950501', '0');
INSERT INTO `dec_component_resource` VALUES ('5c253d2cdc0a4260b259d77b7fcda972', '794337ba183347289dfee67ff9f49628', '1.0', 'jsp', 'component_jsp_poplogin', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498630697', '0');
INSERT INTO `dec_component_resource` VALUES ('5c4dff81f9a14209ba0f3a145ec8bb22', 'd6569925f6ec4d5db32f66defe71b9cf', '', 'js', 'component_js_usefulLinksComp', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815268', '0');
INSERT INTO `dec_component_resource` VALUES ('5e80a932a1f84e65925b9ff766676305', '707baaf25f56419eba17a73d416fdf53', '', 'html', 'component_html_SlideProducts', '1', 'ef293dd013104453ab0f10cf4b252eca', '1497012047', '0');
INSERT INTO `dec_component_resource` VALUES ('646a13e0bf5f487b8aee89998fd3196b', 'ee893795ac6e4bdda148b2e82ff8d21d', '', 'html', 'component_html_reputation', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1500025729', '0');
INSERT INTO `dec_component_resource` VALUES ('66339b82de224dfdb582f4e5dbcaea89', 'c47f80da90d849ca84387e7dbf90fdb4', '', 'js', 'component_js_category', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496738045', '0');
INSERT INTO `dec_component_resource` VALUES ('6931001b9e23480a91e0f36137c787ea', '11371e159d7b4bc382b2a0641246ee85', '', 'html', 'component_html_recommendproduct', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497841393', '0');
INSERT INTO `dec_component_resource` VALUES ('6a3adc7ce0384d668e83ea4a18aae598', '805c36c6078b480e8ecbb11bb9287ea2', '', 'html', 'component_html_bottomHelpCenter', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815119', '0');
INSERT INTO `dec_component_resource` VALUES ('6febbdb22fa44326b67cc4636b87699b', '0227529d55134e35887c87ab5cb79f5c', '1.0', 'jsp', 'component_jsp_top', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497439543', '0');
INSERT INTO `dec_component_resource` VALUES ('7000f0927ab545e6bd39c8b19594118f', 'f515d03b42e841a39ee63b41339fbf9e', '', 'html', 'component_html_producttab', '1', 'ef293dd013104453ab0f10cf4b252eca', '1497234445', '0');
INSERT INTO `dec_component_resource` VALUES ('702f28445ecc402095bd7254f97e7cd0', 'd6461c869efd46519302c46ca6139dac', '1.0', 'jsp', 'component_jsp_navigation', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815242', '0');
INSERT INTO `dec_component_resource` VALUES ('71a880f937b34c6591e1dcad639b7bb4', 'a84c0171435845bead2069c10964084d', '', 'html', 'component_html_picturead', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815161', '0');
INSERT INTO `dec_component_resource` VALUES ('72a261ec9f8a4d22877f7983910e17b9', 'bf2570299aca415dbba323b731ffd517', '', 'html', 'component_html_screen', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498458432', '0');
INSERT INTO `dec_component_resource` VALUES ('7498c60e10874b11963b678b9818004d', 'a564ba204bdb407f80e5972e499bea87', '1.0', 'jsp', 'component_jsp_floorLable', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496321163', '0');
INSERT INTO `dec_component_resource` VALUES ('766887422be44d03ba59759aa5c11906', '07033a87e2804b20817ede7470f6233f', '1.0', 'jsp', 'component_jsp_qrcode', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496814942', '0');
INSERT INTO `dec_component_resource` VALUES ('7a2edc76d9344c0797a93d19aa959707', 'a564ba204bdb407f80e5972e499bea87', '', 'js', 'component_js_floorLable', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496321163', '0');
INSERT INTO `dec_component_resource` VALUES ('7f11ada626cc40b99751805154c5cb0c', 'a84c0171435845bead2069c10964084d', '2.0', 'jsp', 'component_jsp_picturead', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815162', '0');
INSERT INTO `dec_component_resource` VALUES ('82664a6135d3454e8875b9f6bf6aed98', 'bf79c9b9fb354c1b8526911e0ef0a6ec', '1.0', 'jsp', 'component_jsp_productList', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497345334', '0');
INSERT INTO `dec_component_resource` VALUES ('85908f9c728048128ae595a2b0bdc6e6', 'd6461c869efd46519302c46ca6139dac', '', 'html', 'component_html_navigation', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815242', '0');
INSERT INTO `dec_component_resource` VALUES ('8ad9f522a4bb448ca66030c62cea7e47', '296cfb04ad404385b52b340caef89f63', '1.0', 'jsp', 'component_jsp_fullSearchComp', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496814978', '0');
INSERT INTO `dec_component_resource` VALUES ('910f325444f34b9cb834b996fa88d048', 'c47f80da90d849ca84387e7dbf90fdb4', '', 'html', 'component_html_category', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496738045', '0');
INSERT INTO `dec_component_resource` VALUES ('94744fa37c734e1687626ee1ea56da4d', '794337ba183347289dfee67ff9f49628', '', 'html', 'component_html_poplogin', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498630697', '0');
INSERT INTO `dec_component_resource` VALUES ('964df7fd628945579c1ba5a28e8c1521', '5900766e85524417a7a173d6e1333630', '', 'html', 'component_html_share', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815030', '0');
INSERT INTO `dec_component_resource` VALUES ('a1a0aeb13c464f98809116a0e89669f3', '738c12dc331b4225ab8a99dd32306f40', '', 'js', 'component_js_noticeComp', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815093', '0');
INSERT INTO `dec_component_resource` VALUES ('a5d957913b474bd78df4f2fc8fc802e2', '738c12dc331b4225ab8a99dd32306f40', '1.0', 'jsp', 'component_jsp_noticeComp', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815093', '0');
INSERT INTO `dec_component_resource` VALUES ('aa7d10ea72cc4ae99b9f69868c787e16', '184e1c62f8184fb0add9e7048536bdeb', '', 'js', 'component_js_searchShowCategory', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499396838', '0');
INSERT INTO `dec_component_resource` VALUES ('aaa3ca08b72a4ff4a0e14c6e57fbdb6d', '8ceaecea46354ac1b50753c7cc242284', '2.0', 'jsp', 'component_jsp_sideEnterance', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497861074', '0');
INSERT INTO `dec_component_resource` VALUES ('acb9a8fca05b4d5ba9d1d959487a89f1', '738c12dc331b4225ab8a99dd32306f40', '', 'html', 'component_html_noticeComp', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815093', '0');
INSERT INTO `dec_component_resource` VALUES ('aeea6e8dfa6b46819a0a0a688ddeb36f', '96f057f85f794991aa8c012f7630eb6b', '1.0', 'jsp', 'component_jsp_hotproduct', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497512746', '0');
INSERT INTO `dec_component_resource` VALUES ('b018ea1578aa4ae9abc40775c08ff70b', '184e1c62f8184fb0add9e7048536bdeb', '', 'html', 'component_html_searchShowCategory', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499396838', '0');
INSERT INTO `dec_component_resource` VALUES ('b38e227291a147f1ab8fd17d7bcf3aad', 'f0e252c51b1d4619ba7c8923ddaae2ae', '', 'js', 'component_js_cartComp', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497679570', '0');
INSERT INTO `dec_component_resource` VALUES ('b815fcd23464463db73e72a224b68f28', 'e6a80f87f0b24181b514d493d20fa3fa', '1.0', 'jsp', 'component_jsp_floor', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815293', '0');
INSERT INTO `dec_component_resource` VALUES ('bcc3d7235b534495bba04dd3f2de0d92', '61bcb66b2cb148acadcfbcf5a5d5101e', '', 'html', 'component_html_columnNavigation', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815059', '0');
INSERT INTO `dec_component_resource` VALUES ('c06ba81efc6042499c4661cee135210a', 'bf79c9b9fb354c1b8526911e0ef0a6ec', '', 'html', 'component_html_productList', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497345333', '0');
INSERT INTO `dec_component_resource` VALUES ('c173a9eca7554fd8a13d422261b93be1', '707baaf25f56419eba17a73d416fdf53', '', 'js', 'component_js_SlideProducts', '1', 'ef293dd013104453ab0f10cf4b252eca', '1497012047', '0');
INSERT INTO `dec_component_resource` VALUES ('c6614108b2dd435bb2092a691b624191', '707baaf25f56419eba17a73d416fdf53', '4.0', 'jsp', 'component_jsp_SlideProducts', '1', 'ef293dd013104453ab0f10cf4b252eca', '1497012047', '0');
INSERT INTO `dec_component_resource` VALUES ('c6614a3bdaaf448282931ae1f33c0072', 'f0e252c51b1d4619ba7c8923ddaae2ae', '1.0', 'jsp', 'component_jsp_cartComp', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497679570', '0');
INSERT INTO `dec_component_resource` VALUES ('c9c4b557a2094af4bba627234868a27b', '5900766e85524417a7a173d6e1333630', '1.0', 'jsp', 'component_jsp_share', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815030', '0');
INSERT INTO `dec_component_resource` VALUES ('cbbe8aff27ae42b1953aaa5e8573fdfe', 'b90e4e16c95f44589395d93ed134361b', '', 'html', 'component_html_collectionproduct', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499410838', '0');
INSERT INTO `dec_component_resource` VALUES ('d1bb89bef3f44b87a254db4de90ef0e9', '5609c78815a6424b81e09da2645423b1', '', 'html', 'component_html_choosewellproduct', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497427537', '0');
INSERT INTO `dec_component_resource` VALUES ('d388957367ce451a8d375ba815cc006a', 'f0e252c51b1d4619ba7c8923ddaae2ae', '', 'html', 'component_html_cartComp', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497679570', '0');
INSERT INTO `dec_component_resource` VALUES ('d7ce43bb9d4049879b02fb06f787bbf0', '96f057f85f794991aa8c012f7630eb6b', '', 'html', 'component_html_hotproduct', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497512745', '0');
INSERT INTO `dec_component_resource` VALUES ('e069221918cf4b31822d43c44d8ab604', 'bbb16c095f7740f0a4b0eff6beb56841', '1.0', 'jsp', 'component_jsp_sliadcomp', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815186', '0');
INSERT INTO `dec_component_resource` VALUES ('e7adfaff03804a65a5845b569f05615b', 'd6461c869efd46519302c46ca6139dac', '', 'js', 'component_js_navigation', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815242', '0');
INSERT INTO `dec_component_resource` VALUES ('e7b22607e0424c0aadfe42745ef9c13d', '794337ba183347289dfee67ff9f49628', '', 'js', 'component_js_poplogin', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498630697', '0');
INSERT INTO `dec_component_resource` VALUES ('e8094857d5ec4f30b23407fd5a4abbe7', 'd6569925f6ec4d5db32f66defe71b9cf', '1.0', 'jsp', 'component_jsp_usefulLinksComp', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815268', '0');
INSERT INTO `dec_component_resource` VALUES ('e91cbb5cf06a4e038d6a6a9456878363', '8ceaecea46354ac1b50753c7cc242284', '', 'js', 'component_js_sideEnterance', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497861074', '0');
INSERT INTO `dec_component_resource` VALUES ('ec9e3e9758ff44808cfb1ec80ebb0af3', 'e6a80f87f0b24181b514d493d20fa3fa', '', 'js', 'component_js_floor', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815293', '0');
INSERT INTO `dec_component_resource` VALUES ('ef12f72335b241acb1a0854b59801526', 'f515d03b42e841a39ee63b41339fbf9e', '7.0', 'jsp', 'component_jsp_producttab', '1', 'ef293dd013104453ab0f10cf4b252eca', '1497234445', '0');
INSERT INTO `dec_component_resource` VALUES ('ef655c1da3be40cc8d4960036d91490b', '296cfb04ad404385b52b340caef89f63', '', 'html', 'component_html_fullSearchComp', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496814978', '0');
INSERT INTO `dec_component_resource` VALUES ('f18239a28a0349da897162a002e7eb01', '774394ec3cea4354a85ebdcaab9f19d6', '', 'html', 'component_html_mBottomBar', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498803282', '0');
INSERT INTO `dec_component_resource` VALUES ('f293b8d4a6f24844816c8598b9c23523', 'bf2570299aca415dbba323b731ffd517', '', 'js', 'component_js_screen', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498458432', '0');
INSERT INTO `dec_component_resource` VALUES ('f33fbc24f4a9476aa35e84cf24ca037c', 'bf2570299aca415dbba323b731ffd517', '1.0', 'jsp', 'component_jsp_screen', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498458432', '0');
INSERT INTO `dec_component_resource` VALUES ('f38ef5706df247f7925124212dc52c19', '774394ec3cea4354a85ebdcaab9f19d6', '', 'js', 'component_js_mBottomBar', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498803282', '0');
INSERT INTO `dec_component_resource` VALUES ('f3a74d14ecb34a248437338cf933a061', 'c47f80da90d849ca84387e7dbf90fdb4', '1.0', 'jsp', 'component_jsp_category', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496738045', '0');
INSERT INTO `dec_component_resource` VALUES ('f65d71dcc8a04d2088920cb66719b4db', '5609c78815a6424b81e09da2645423b1', '3.0', 'jsp', 'component_jsp_choosewellproduct', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497427538', '0');
INSERT INTO `dec_component_resource` VALUES ('f79069bf3cdc42919c997046e88b350f', '6124e841b57f4dbd8a4bc119f44358be', '', 'html', 'component_html_productdetail', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499950500', '0');
INSERT INTO `dec_component_resource` VALUES ('f94f7f4a72154199aa77e61adcca90f7', '4cd0ded631c045f0a66476be55f139f3', '3.0', 'jsp', 'component_jsp_imagetexteditor', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815002', '0');
INSERT INTO `dec_component_resource` VALUES ('f955717c774c40a492ce1739a813a17e', 'eadacfebf4c14613b2de85dfbc6954cb', '', 'html', 'component_html_bottom', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815316', '0');
INSERT INTO `dec_component_resource` VALUES ('fa203fee72cd4b509dac26d9b5264972', '4cd0ded631c045f0a66476be55f139f3', '', 'html', 'component_html_imagetexteditor', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815002', '0');
INSERT INTO `dec_component_resource` VALUES ('fadb2b6b7d95441790b1df2cb379be6e', '61bcb66b2cb148acadcfbcf5a5d5101e', '', 'js', 'component_js_columnNavigation', '1', 'ef293dd013104453ab0f10cf4b252eca', '1496815059', '0');
INSERT INTO `dec_component_resource` VALUES ('fe189573c41e49efa7c51d94588be1db', '11371e159d7b4bc382b2a0641246ee85', '', 'js', 'component_js_recommendproduct', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497841393', '0');
INSERT INTO `dec_component_resource` VALUES ('fe540824d1224932b9211a158b365942', '5609c78815a6424b81e09da2645423b1', '', 'js', 'component_js_choosewellproduct', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1497427538', '0');

-- ----------------------------
-- Table structure for dec_page_layout
-- ----------------------------
DROP TABLE IF EXISTS `dec_page_layout`;
CREATE TABLE `dec_page_layout` (
  `id` varchar(32) NOT NULL,
  `layoutName` varchar(32) DEFAULT NULL,
  `layoutId` varchar(40) DEFAULT NULL,
  `resourceKey` varchar(200) DEFAULT NULL,
  `versionType` char(1) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `opBy` varchar(32) DEFAULT NULL,
  `opAt` int(32) DEFAULT NULL,
  `delFlag` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dec_page_layout
-- ----------------------------
INSERT INTO `dec_page_layout` VALUES ('1b9c3079ff5d47159215a3e6bd96aaf6', '手机布局测试1', 'phoneTest1', 'layout_html_phoneTest1', '2', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499740915', '0');
INSERT INTO `dec_page_layout` VALUES ('86430fe2a1f149be9fa6b02fbaf903e2', '手机布局测试', 'phoneTest', '', '2', '手机布局测试', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498801754', '0');
INSERT INTO `dec_page_layout` VALUES ('ef1fad0a270b46238a1bbe044b9d4a05', '两列右侧自适应1', 'ss', 'layout_html_ss', '1', '两列右侧自适应为g-box属性', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499741394', '0');
INSERT INTO `dec_page_layout` VALUES ('ef2b90b3d0d24cc39eef493a04c1179c', '顶顶顶顶', 'ddd', 'layout_html_ddd', '1', '顶顶顶顶', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499740068', '0');

-- ----------------------------
-- Table structure for dec_templates_files
-- ----------------------------
DROP TABLE IF EXISTS `dec_templates_files`;
CREATE TABLE `dec_templates_files` (
  `id` varchar(32) NOT NULL DEFAULT '' COMMENT 'ID',
  `templateUuid` varchar(100) DEFAULT NULL COMMENT '所属模板',
  `fileType` int(32) DEFAULT NULL COMMENT '文件类型',
  `showName` varchar(100) DEFAULT NULL COMMENT ' 文件夹/页面显示名称',
  `pageUuid` varchar(100) DEFAULT NULL COMMENT '页面uuid',
  `parentUuid` varchar(100) DEFAULT NULL COMMENT '父文件夹uuid',
  `note` varchar(100) DEFAULT NULL COMMENT '备注',
  `isDefault` int(32) DEFAULT NULL COMMENT '是否系统默认',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dec_templates_files
-- ----------------------------
INSERT INTO `dec_templates_files` VALUES ('00255bddc1894a269def73a54fa8c44c', '2109a98d343e4155a7dce69dfc686146', '1', 'js', null, '', '脚本文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131595', '0');
INSERT INTO `dec_templates_files` VALUES ('0065a3e2b88449d8854e1067358e2ce0', '4d6d9b30b24b4c46a1e3bcc0e87c97fa', '1', 'js', null, '', '脚本文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499482935', '0');
INSERT INTO `dec_templates_files` VALUES ('01d8763e8c9642499d4f8784d7c0a246', 'd6726823f77a4ddcaedda43a41baf530', '2', 'index.html', '618144e176114b4a9f761c9a3f2d4265', '', null, '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499483123', '0');
INSERT INTO `dec_templates_files` VALUES ('0388ad4e2f9b4e88a52ca61da51c52ea', 'f1f1b935194c41ae933051aa7af80bce', '1', 'images', null, '', '图片文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498722345', '0');
INSERT INTO `dec_templates_files` VALUES ('1935d7de392b4a9d9e501635b8ed5716', '2109a98d343e4155a7dce69dfc686146', '2', 'detail.html', '0c9f3b1e50ea468991521b964ed04ba5', '', null, '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131595', '0');
INSERT INTO `dec_templates_files` VALUES ('262277bae34945849d16b51b1dd4b881', 'f1f1b935194c41ae933051aa7af80bce', '2', 'index.html', '786bf78fa95c44c08bc63073d67ef84c', '', null, '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498722345', '0');
INSERT INTO `dec_templates_files` VALUES ('2a81bdea00a242e0bb0643530c901539', '4d6d9b30b24b4c46a1e3bcc0e87c97fa', '1', 'fonts', null, '', '字体文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499482935', '0');
INSERT INTO `dec_templates_files` VALUES ('3402f503cae94766853da4dd2cb2058e', '4d6d9b30b24b4c46a1e3bcc0e87c97fa', '2', 'list.html', '9c090245de2f41ee860f80d16e43c203', '', null, '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499482935', '0');
INSERT INTO `dec_templates_files` VALUES ('353b444ff463481f8f7d0c1ad00376e6', '2109a98d343e4155a7dce69dfc686146', '1', 'css', null, '', '样式文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131595', '0');
INSERT INTO `dec_templates_files` VALUES ('43d9930e52c44556b8f40bcd0fa52f81', 'd6726823f77a4ddcaedda43a41baf530', '2', 'detail.html', '7481311fdebc40daa46c5ea6ade3c2dd', '', null, '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499483123', '0');
INSERT INTO `dec_templates_files` VALUES ('4ffde2aa0f9941ab9710c6944119c3a8', '4d6d9b30b24b4c46a1e3bcc0e87c97fa', '2', 'index.html', 'a8889969006a43bcbbce280559c9dffd', '', null, '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499482935', '0');
INSERT INTO `dec_templates_files` VALUES ('520c044e7c2646c188449987d42d9d50', 'f1f1b935194c41ae933051aa7af80bce', '2', 'detail.html', 'f6c100eb782d4d9bb5796457f2148786', '', null, '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498722345', '0');
INSERT INTO `dec_templates_files` VALUES ('522cefa0b78c4384b6a7b483c15a1fe8', '4d6d9b30b24b4c46a1e3bcc0e87c97fa', '1', 'images', null, '', '图片文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499482935', '0');
INSERT INTO `dec_templates_files` VALUES ('590a5dc9ea6743129991efb3004e5a4e', '2109a98d343e4155a7dce69dfc686146', '1', 'images', null, '', '图片文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131595', '0');
INSERT INTO `dec_templates_files` VALUES ('6e15953562e343dcb8f89071b121c3a7', '2109a98d343e4155a7dce69dfc686146', '2', 'index.html', '5db3b9af99ae48c697e099f7878e85c3', '', null, '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131594', '0');
INSERT INTO `dec_templates_files` VALUES ('71ca05074e12480f9ec61556ca7d5409', 'f1f1b935194c41ae933051aa7af80bce', '1', 'fonts', null, '', '字体文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498722345', '0');
INSERT INTO `dec_templates_files` VALUES ('771140b0a70c42939aa921170fde9288', '4d6d9b30b24b4c46a1e3bcc0e87c97fa', '2', 'detail.html', '6e585ab3b1ee42b4a4d3534fcea16aed', '', null, '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499482935', '0');
INSERT INTO `dec_templates_files` VALUES ('7fc0e28440c94fcd85e7d8524b20c527', '2109a98d343e4155a7dce69dfc686146', '2', 'list.html', '23863626df9f469ead117c2fe0c27d42', '', null, '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131594', '0');
INSERT INTO `dec_templates_files` VALUES ('8aa3959f122f4b0fbe010d24c105d83b', 'd6726823f77a4ddcaedda43a41baf530', '1', 'images', null, '', '图片文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499483123', '0');
INSERT INTO `dec_templates_files` VALUES ('9b8f58c53ae44ec9ba914fca01770b44', '2109a98d343e4155a7dce69dfc686146', '2', 'test.html', '1ef64a2749ea485da922dd79282de0cd', '', null, '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499419533', '0');
INSERT INTO `dec_templates_files` VALUES ('a6d14c3bccaf46e6a50e17c580fdf224', '2109a98d343e4155a7dce69dfc686146', '1', 'fonts', null, '', '字体文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131595', '0');
INSERT INTO `dec_templates_files` VALUES ('ac6b059010b74d0aac1f6396707ce058', 'd6726823f77a4ddcaedda43a41baf530', '1', 'css', null, '', '样式文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499483123', '0');
INSERT INTO `dec_templates_files` VALUES ('af8f6eb45d744527a234e60ad1e441a3', 'd6726823f77a4ddcaedda43a41baf530', '1', 'js', null, '', '脚本文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499483123', '0');
INSERT INTO `dec_templates_files` VALUES ('b3c8f0c1283b4374943fc4ff0e22a68b', '4d6d9b30b24b4c46a1e3bcc0e87c97fa', '1', 'css', null, '', '样式文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499482935', '0');
INSERT INTO `dec_templates_files` VALUES ('b7118f891bb8473c9a7a39087de11e5c', 'd6726823f77a4ddcaedda43a41baf530', '2', 'list.html', '779624e2fa3a4d7390c77fc268330bb7', '', null, '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499483123', '0');
INSERT INTO `dec_templates_files` VALUES ('bb650d9b316a4a8cbcd00827bc175244', 'd6726823f77a4ddcaedda43a41baf530', '1', 'fonts', null, '', '字体文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499483123', '0');
INSERT INTO `dec_templates_files` VALUES ('c8276286d58a44c3994b5ffe0c60bfe2', '2109a98d343e4155a7dce69dfc686146', '2', 'artic.html', '58dc4025b1384f0ba6809d72995eca45', '', null, '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498292844', '0');
INSERT INTO `dec_templates_files` VALUES ('ed1b0e4e40f9473cb55c758f1d813e19', 'f1f1b935194c41ae933051aa7af80bce', '1', 'css', null, '', '样式文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498722345', '0');
INSERT INTO `dec_templates_files` VALUES ('fac8b726631447c4bd9390ea5af0ecac', 'f1f1b935194c41ae933051aa7af80bce', '2', 'list.html', 'c462947abf924282b55f6583314f1635', '', null, '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498722345', '0');
INSERT INTO `dec_templates_files` VALUES ('fe503c2df5154875b8da13da9bf468af', 'f1f1b935194c41ae933051aa7af80bce', '1', 'js', null, '', '脚本文件夹', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498722345', '0');

-- ----------------------------
-- Table structure for dec_templates_manager
-- ----------------------------
DROP TABLE IF EXISTS `dec_templates_manager`;
CREATE TABLE `dec_templates_manager` (
  `id` varchar(32) NOT NULL DEFAULT '' COMMENT 'ID',
  `templateNo` varchar(100) DEFAULT NULL COMMENT '模板编号',
  `templateZhName` varchar(100) DEFAULT NULL COMMENT '模板中文名称',
  `templateEnName` varchar(100) DEFAULT NULL COMMENT '模板英文名称',
  `templateImage` varchar(100) DEFAULT NULL COMMENT '模板缩略图',
  `storeId` varchar(100) DEFAULT NULL,
  `versionType` varchar(100) DEFAULT NULL,
  `useType` varchar(100) DEFAULT '1' COMMENT '模板使用类型',
  `description` varchar(100) DEFAULT NULL COMMENT '模板描述',
  `disabled` tinyint(1) DEFAULT NULL COMMENT '是否启用',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dec_templates_manager
-- ----------------------------
INSERT INTO `dec_templates_manager` VALUES ('2109a98d343e4155a7dce69dfc686146', null, '名流会', 'mlh', 'http://117.78.50.205:8888/group1/M00/00/0B/wKgBCFlLrPuAdZx7AAGXSxqU9_Q758.jpg', null, '1', '1', '1', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131594', '0');
INSERT INTO `dec_templates_manager` VALUES ('4d6d9b30b24b4c46a1e3bcc0e87c97fa', null, '烦烦烦', 'ddd', '', null, '1', '1', 'dddd', '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499482935', '0');
INSERT INTO `dec_templates_manager` VALUES ('d6726823f77a4ddcaedda43a41baf530', null, 'ddddddd', 'ddd', '', null, '1', '1', 'dd', '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499483122', '0');
INSERT INTO `dec_templates_manager` VALUES ('f1f1b935194c41ae933051aa7af80bce', null, '手机测试', 'phoneTest', 'http://117.78.50.205:8888/group1/M00/00/0E/wKgBCFlUsIKAHoUoAAJHhk1oUKc577.png', null, '2', '1', 'phoneTest', '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498722344', '0');

-- ----------------------------
-- Table structure for dec_templates_pages
-- ----------------------------
DROP TABLE IF EXISTS `dec_templates_pages`;
CREATE TABLE `dec_templates_pages` (
  `id` varchar(32) NOT NULL DEFAULT '' COMMENT 'ID',
  `templateUuid` varchar(255) DEFAULT NULL COMMENT ' 模板uuid',
  `pageNo` varchar(255) DEFAULT NULL COMMENT ' 页面编号',
  `pageName` varchar(255) DEFAULT NULL COMMENT ' 页面名称',
  `pageFileName` varchar(255) DEFAULT NULL COMMENT ' 文件名称',
  `pageType` int(32) DEFAULT NULL COMMENT ' 页面类型',
  `description` varchar(255) DEFAULT NULL COMMENT ' 页面描述',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dec_templates_pages
-- ----------------------------
INSERT INTO `dec_templates_pages` VALUES ('0c9f3b1e50ea468991521b964ed04ba5', '2109a98d343e4155a7dce69dfc686146', null, '商品详情页', 'detail.html', '3', '商品详情页', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131595', '0');
INSERT INTO `dec_templates_pages` VALUES ('1ef64a2749ea485da922dd79282de0cd', '2109a98d343e4155a7dce69dfc686146', null, '测试', 'test.html', '4', 'sss', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499419533', '0');
INSERT INTO `dec_templates_pages` VALUES ('23863626df9f469ead117c2fe0c27d42', '2109a98d343e4155a7dce69dfc686146', null, '商品列表页', 'list.html', '2', '商品列表页', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131594', '0');
INSERT INTO `dec_templates_pages` VALUES ('58dc4025b1384f0ba6809d72995eca45', '2109a98d343e4155a7dce69dfc686146', null, '栏目导航', 'artic.html', '4', '栏目导航', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498292844', '0');
INSERT INTO `dec_templates_pages` VALUES ('5db3b9af99ae48c697e099f7878e85c3', '2109a98d343e4155a7dce69dfc686146', null, '首页', 'index.html', '1', '首页', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131594', '0');
INSERT INTO `dec_templates_pages` VALUES ('618144e176114b4a9f761c9a3f2d4265', 'd6726823f77a4ddcaedda43a41baf530', null, '首页', 'index.html', '1', '首页', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499483122', '0');
INSERT INTO `dec_templates_pages` VALUES ('6e585ab3b1ee42b4a4d3534fcea16aed', '4d6d9b30b24b4c46a1e3bcc0e87c97fa', null, '商品详情页', 'detail.html', '3', '商品详情页', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499482935', '0');
INSERT INTO `dec_templates_pages` VALUES ('7481311fdebc40daa46c5ea6ade3c2dd', 'd6726823f77a4ddcaedda43a41baf530', null, '商品详情页', 'detail.html', '3', '商品详情页', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499483123', '0');
INSERT INTO `dec_templates_pages` VALUES ('779624e2fa3a4d7390c77fc268330bb7', 'd6726823f77a4ddcaedda43a41baf530', null, '商品列表页', 'list.html', '2', '商品列表页', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499483123', '0');
INSERT INTO `dec_templates_pages` VALUES ('786bf78fa95c44c08bc63073d67ef84c', 'f1f1b935194c41ae933051aa7af80bce', null, '首页', 'index.html', '1', '首页', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498722345', '0');
INSERT INTO `dec_templates_pages` VALUES ('9c090245de2f41ee860f80d16e43c203', '4d6d9b30b24b4c46a1e3bcc0e87c97fa', null, '商品列表页', 'list.html', '2', '商品列表页', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499482935', '0');
INSERT INTO `dec_templates_pages` VALUES ('a8889969006a43bcbbce280559c9dffd', '4d6d9b30b24b4c46a1e3bcc0e87c97fa', null, '首页', 'index.html', '1', '首页', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499482935', '0');
INSERT INTO `dec_templates_pages` VALUES ('c462947abf924282b55f6583314f1635', 'f1f1b935194c41ae933051aa7af80bce', null, '商品列表页', 'list.html', '2', '商品列表页', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498722345', '0');
INSERT INTO `dec_templates_pages` VALUES ('f6c100eb782d4d9bb5796457f2148786', 'f1f1b935194c41ae933051aa7af80bce', null, '商品详情页', 'detail.html', '3', '商品详情页', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498722345', '0');

-- ----------------------------
-- Table structure for dec_templates_resource
-- ----------------------------
DROP TABLE IF EXISTS `dec_templates_resource`;
CREATE TABLE `dec_templates_resource` (
  `id` varchar(32) NOT NULL DEFAULT '' COMMENT 'ID',
  `folderUuid` varchar(100) DEFAULT NULL COMMENT '所属模板文件夹',
  `fileName` varchar(100) DEFAULT NULL COMMENT '文件名',
  `suffix` varchar(100) DEFAULT NULL COMMENT '后缀',
  `fileType` int(32) DEFAULT NULL COMMENT '文件类型',
  `resourceKey` varchar(100) DEFAULT NULL COMMENT '资源保存的key',
  `note` varchar(500) DEFAULT NULL COMMENT '备注',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dec_templates_resource
-- ----------------------------
INSERT INTO `dec_templates_resource` VALUES ('02f9bf2f4d024f38b2ef6d5e0aea0c6f', '590a5dc9ea6743129991efb3004e5a4e', 'ratings.png', 'png', null, '2109a98d343e4155a7dce69dfc686146_images_ratings.png', null, '25c1fadce5094b6bbc7fa89514a8ac5b', '1499855003', '0');
INSERT INTO `dec_templates_resource` VALUES ('122ea3be05744439bea9795b1bbf7a44', '353b444ff463481f8f7d0c1ad00376e6', 'font-awesome.min.css', 'css', null, '2109a98d343e4155a7dce69dfc686146_css_font-awesome.min.css', null, '25c1fadce5094b6bbc7fa89514a8ac5b', '1499823654', '0');
INSERT INTO `dec_templates_resource` VALUES ('339cf3fa9d544e8894efd4763b378895', '353b444ff463481f8f7d0c1ad00376e6', 'qwui_components.css', 'css', null, '2109a98d343e4155a7dce69dfc686146_css_qwui_components.css', null, '25c1fadce5094b6bbc7fa89514a8ac5b', '1500085507', '0');
INSERT INTO `dec_templates_resource` VALUES ('9359aa1ecede4bfbbd5230a39a66a6f9', '353b444ff463481f8f7d0c1ad00376e6', 'qwui_components_blue.css', 'css', null, '2109a98d343e4155a7dce69dfc686146_css_qwui_components_blue.css', null, '25c1fadce5094b6bbc7fa89514a8ac5b', '1500085507', '0');
INSERT INTO `dec_templates_resource` VALUES ('a122a00d95a24c7f9cda0ecc42bc2641', '353b444ff463481f8f7d0c1ad00376e6', 'qwui_base.css', 'css', null, '2109a98d343e4155a7dce69dfc686146_css_qwui_base.css', null, '25c1fadce5094b6bbc7fa89514a8ac5b', '1499923532', '0');
INSERT INTO `dec_templates_resource` VALUES ('cb9cac9f78fe4dd8b81b8cc261e62a1c', '590a5dc9ea6743129991efb3004e5a4e', 'y_fixflobg.gif', 'gif', null, '2109a98d343e4155a7dce69dfc686146_images_y_fixflobg.gif', null, '25c1fadce5094b6bbc7fa89514a8ac5b', '1498289122', '0');
INSERT INTO `dec_templates_resource` VALUES ('f0c6ec01f8f044c29eff9b402f788a7a', '353b444ff463481f8f7d0c1ad00376e6', 'qwui_components_black.css', 'css', null, '2109a98d343e4155a7dce69dfc686146_css_qwui_components_black.css', null, '25c1fadce5094b6bbc7fa89514a8ac5b', '1500085507', '0');
INSERT INTO `dec_templates_resource` VALUES ('fead333bb51348b3b159d8069573ae89', '590a5dc9ea6743129991efb3004e5a4e', 'grade.png', 'png', null, '2109a98d343e4155a7dce69dfc686146_images_grade.png', null, '25c1fadce5094b6bbc7fa89514a8ac5b', '1500026933', '0');
INSERT INTO `dec_templates_resource` VALUES ('ffcb0adcd23645e3ac34239f42d03eb5', '353b444ff463481f8f7d0c1ad00376e6', 'qwui_ui.css', 'css', null, '2109a98d343e4155a7dce69dfc686146_css_qwui_ui.css', null, '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131779', '0');

-- ----------------------------
-- Table structure for dec_templates_style
-- ----------------------------
DROP TABLE IF EXISTS `dec_templates_style`;
CREATE TABLE `dec_templates_style` (
  `id` varchar(32) NOT NULL DEFAULT '' COMMENT 'ID',
  `templateUuid` varchar(255) DEFAULT NULL COMMENT ' 模板uuid',
  `styleNo` varchar(255) DEFAULT NULL COMMENT ' 皮肤编号',
  `styleName` varchar(255) DEFAULT NULL COMMENT ' 皮肤名称',
  `styleFileName` varchar(255) DEFAULT NULL COMMENT ' 皮肤文件名称',
  `styleImage` varchar(255) DEFAULT NULL COMMENT ' 图标',
  `disabled` tinyint(1) DEFAULT NULL COMMENT ' 模板uuid',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dec_templates_style
-- ----------------------------
INSERT INTO `dec_templates_style` VALUES ('15a0be5e0435439db171aecf185d266e', '2109a98d343e4155a7dce69dfc686146', null, '嘻嘻嘻嘻', 'ccccc.css', '', '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499481849', '0');
INSERT INTO `dec_templates_style` VALUES ('2e25d7b9d94249c290c11825ee7e735d', '2109a98d343e4155a7dce69dfc686146', null, 's', 's', '', '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499481883', '0');
INSERT INTO `dec_templates_style` VALUES ('5fd94560c0804b58ae96f75bddb47e28', '2109a98d343e4155a7dce69dfc686146', null, '默认皮肤', 'qwui_components.css', 'http://117.78.50.205:8888/group1/M00/00/0B/wKgBCFlMhfOAAwG1AAGBFIBBc30079.jpg', '1', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498188141', '0');
INSERT INTO `dec_templates_style` VALUES ('84e95dee6f2141cd964c0e56238482ec', '2109a98d343e4155a7dce69dfc686146', null, '蓝色皮肤', 'qwui_components_blue.css', '', '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498186871', '0');
INSERT INTO `dec_templates_style` VALUES ('aa2bfe083ccb4210970e45deddc9b1ca', '2109a98d343e4155a7dce69dfc686146', null, '黑色皮肤', 'qwui_components_black.css', '', '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498186499', '0');
INSERT INTO `dec_templates_style` VALUES ('acb25a71fb2e473cb15168a0bc2f34f4', '2109a98d343e4155a7dce69dfc686146', null, 'q', 'a', '', '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499481889', '0');

-- ----------------------------
-- Table structure for dec_templates_sub
-- ----------------------------
DROP TABLE IF EXISTS `dec_templates_sub`;
CREATE TABLE `dec_templates_sub` (
  `id` varchar(32) NOT NULL DEFAULT '' COMMENT 'ID',
  `pageUuid` varchar(255) DEFAULT NULL COMMENT ' 页面uuid',
  `resourceKey` varchar(255) DEFAULT NULL COMMENT ' 页面保存的key',
  `versionNo` varchar(255) DEFAULT NULL COMMENT '版本号',
  `description` varchar(255) DEFAULT NULL COMMENT '版本描述',
  `disabled` int(32) DEFAULT NULL COMMENT '是否启用',
  `createTime` int(32) DEFAULT NULL COMMENT '创建时间',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dec_templates_sub
-- ----------------------------
INSERT INTO `dec_templates_sub` VALUES ('06e8a87782d24666971c3e39b1cf486e', 'e4fa7541ce7d41a6bab501c18bd658f1', '2109a98d343e4155a7dce69dfc686146_e4fa7541ce7d41a6bab501c18bd658f1_1.0', '1.0', 'v1.0版本', '1', '1498563993', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498563993', '0');
INSERT INTO `dec_templates_sub` VALUES ('101980b2eeb24983a653ba9fede8def6', 'ad5d7acd2e9c4a53904feb8f97b2d853', '2109a98d343e4155a7dce69dfc686146_ad5d7acd2e9c4a53904feb8f97b2d853_1.0', '1.0', 'v1.0版本', '1', '1498563993', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498563993', '0');
INSERT INTO `dec_templates_sub` VALUES ('1101d83b6eb24568b0d85f08e735f36f', '779624e2fa3a4d7390c77fc268330bb7', 'd6726823f77a4ddcaedda43a41baf530_779624e2fa3a4d7390c77fc268330bb7_1.0', '1.0', 'v1.0版本', '1', '1499483123', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499483123', '0');
INSERT INTO `dec_templates_sub` VALUES ('1b665f996f1b47249d537b1c8496c075', '618144e176114b4a9f761c9a3f2d4265', 'd6726823f77a4ddcaedda43a41baf530_618144e176114b4a9f761c9a3f2d4265_1.0', '1.0', 'v1.0版本', '1', '1499483123', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499483123', '0');
INSERT INTO `dec_templates_sub` VALUES ('20846c3286004051aa22f8403bcb74a3', '16c246800e7042979735790a980650c1', '2109a98d343e4155a7dce69dfc686146_16c246800e7042979735790a980650c1_1.0', '1.0', 'v1.0版本', '1', '1498563991', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498563991', '0');
INSERT INTO `dec_templates_sub` VALUES ('216272d7263f47359e3f8804f0b5aee5', 'f2d7246227314cab9de8764116a41534', '2109a98d343e4155a7dce69dfc686146_f2d7246227314cab9de8764116a41534_1.0', '1.0', 'v1.0版本', '1', '1498563983', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498563983', '0');
INSERT INTO `dec_templates_sub` VALUES ('2750737c744142378428d68456083531', '23863626df9f469ead117c2fe0c27d42', '2109a98d343e4155a7dce69dfc686146_23863626df9f469ead117c2fe0c27d42_1.0', '1.0', 'v1.0版本', '1', '1498131594', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131594', '0');
INSERT INTO `dec_templates_sub` VALUES ('2aa7763cfab047c48d01b420ac1dfe26', '58dc4025b1384f0ba6809d72995eca45', '2109a98d343e4155a7dce69dfc686146_58dc4025b1384f0ba6809d72995eca45_1.0', '1.0', 'v1.0版本', '1', '1498292844', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498292844', '0');
INSERT INTO `dec_templates_sub` VALUES ('2f12a8efedd444658bb10642692b5989', '6e585ab3b1ee42b4a4d3534fcea16aed', '4d6d9b30b24b4c46a1e3bcc0e87c97fa_6e585ab3b1ee42b4a4d3534fcea16aed_1.0', '1.0', 'v1.0版本', '1', '1499482935', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499482935', '0');
INSERT INTO `dec_templates_sub` VALUES ('391360140f35446d848141a6f6065b61', 'f166e2b6b6624f2abe7db168ebdde567', '2109a98d343e4155a7dce69dfc686146_f166e2b6b6624f2abe7db168ebdde567_1.0', '1.0', 'v1.0版本', '1', '1498563989', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498563989', '0');
INSERT INTO `dec_templates_sub` VALUES ('4b69c07c729e4d89ba9d101c7682d4fd', '4d65108c640a4618997162b74e8f9614', '2109a98d343e4155a7dce69dfc686146_4d65108c640a4618997162b74e8f9614_1.0', '1.0', 'v1.0版本', '1', '1498563991', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498563991', '0');
INSERT INTO `dec_templates_sub` VALUES ('69a8ef9006984287b7c6b80cd5cf04fa', 'a8889969006a43bcbbce280559c9dffd', '4d6d9b30b24b4c46a1e3bcc0e87c97fa_a8889969006a43bcbbce280559c9dffd_1.0', '1.0', 'v1.0版本', '1', '1499482935', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499482935', '0');
INSERT INTO `dec_templates_sub` VALUES ('6ec83ccea91d46d79a64d33bf0a0f2d2', 'e1023f2131ae4936b93142d1bb711a5e', '2109a98d343e4155a7dce69dfc686146_e1023f2131ae4936b93142d1bb711a5e_1.0', '1.0', 'v1.0版本', '1', '1498563989', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498563989', '0');
INSERT INTO `dec_templates_sub` VALUES ('6f94afeeccd343ff86c16f1efe7eb1f0', '5db3b9af99ae48c697e099f7878e85c3', '2109a98d343e4155a7dce69dfc686146_5db3b9af99ae48c697e099f7878e85c3_1.0', '1.0', 'v1.0版本', '1', '1498131594', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131594', '0');
INSERT INTO `dec_templates_sub` VALUES ('815f3fb1d68a435caf71c2c621b1242b', '6c04dbfd2cd14d8c87868d714a3823c2', '2109a98d343e4155a7dce69dfc686146_6c04dbfd2cd14d8c87868d714a3823c2_1.0', '1.0', 'v1.0版本', '1', '1498563982', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498563982', '0');
INSERT INTO `dec_templates_sub` VALUES ('8bde1cf86d294461b787d7f2b94317c6', '22d1a3548f5f4155b413bda8075ed14e', '2109a98d343e4155a7dce69dfc686146_22d1a3548f5f4155b413bda8075ed14e_1.0', '1.0', 'v1.0版本', '1', '1498563991', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498563991', '0');
INSERT INTO `dec_templates_sub` VALUES ('958ac61d26d04d36bca6679f23e808cb', '9c090245de2f41ee860f80d16e43c203', '4d6d9b30b24b4c46a1e3bcc0e87c97fa_9c090245de2f41ee860f80d16e43c203_1.0', '1.0', 'v1.0版本', '1', '1499482935', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499482935', '0');
INSERT INTO `dec_templates_sub` VALUES ('a5164a16d51a468ab4a5aaf168c6c482', 'c435811fb6f44319a71e793483c9664a', '2109a98d343e4155a7dce69dfc686146_c435811fb6f44319a71e793483c9664a_1.0', '1.0', 'v1.0版本', '1', '1498563982', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498563982', '0');
INSERT INTO `dec_templates_sub` VALUES ('b130a53b6e424016b9143f3dd2307366', '7481311fdebc40daa46c5ea6ade3c2dd', 'd6726823f77a4ddcaedda43a41baf530_7481311fdebc40daa46c5ea6ade3c2dd_1.0', '1.0', 'v1.0版本', '1', '1499483123', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499483123', '0');
INSERT INTO `dec_templates_sub` VALUES ('c8e6005610b2408e92587c557c9f3e56', '4e085a434b184c63970c588d6b6deefc', '2109a98d343e4155a7dce69dfc686146_4e085a434b184c63970c588d6b6deefc_1.0', '1.0', 'v1.0版本', '1', '1498563990', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498563990', '0');
INSERT INTO `dec_templates_sub` VALUES ('ccd3fc71bcd74bcda4344c9943367830', 'f6c100eb782d4d9bb5796457f2148786', 'f1f1b935194c41ae933051aa7af80bce_f6c100eb782d4d9bb5796457f2148786_1.0', '1.0', 'v1.0版本', '1', '1498722345', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498722345', '0');
INSERT INTO `dec_templates_sub` VALUES ('ccf8d1f6d12949468f0c81d66db7a254', '786bf78fa95c44c08bc63073d67ef84c', 'f1f1b935194c41ae933051aa7af80bce_786bf78fa95c44c08bc63073d67ef84c_1.0', '1.0', 'v1.0版本', '1', '1498722345', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498722345', '0');
INSERT INTO `dec_templates_sub` VALUES ('d7bbbc1e275f4aea94b5f5e7e78b51b0', '01732e47f92b4c539f877d5beaf583b7', '2109a98d343e4155a7dce69dfc686146_01732e47f92b4c539f877d5beaf583b7_1.0', '1.0', 'v1.0版本', '1', '1498563992', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498563992', '0');
INSERT INTO `dec_templates_sub` VALUES ('ec14de8f1ad940ef9b462a8db578e817', '1ef64a2749ea485da922dd79282de0cd', '2109a98d343e4155a7dce69dfc686146_1ef64a2749ea485da922dd79282de0cd_1.0', '1.0', 'v1.0版本', '1', '1499419533', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499419533', '0');
INSERT INTO `dec_templates_sub` VALUES ('f852318229b842d7a3f4c347e6f2fb3f', 'c462947abf924282b55f6583314f1635', 'f1f1b935194c41ae933051aa7af80bce_c462947abf924282b55f6583314f1635_1.0', '1.0', 'v1.0版本', '1', '1498722345', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498722345', '0');
INSERT INTO `dec_templates_sub` VALUES ('fb3b8b3ac1ed4c809e88f1963be47bcc', '0c9f3b1e50ea468991521b964ed04ba5', '2109a98d343e4155a7dce69dfc686146_0c9f3b1e50ea468991521b964ed04ba5_1.0', '1.0', 'v1.0版本', '1', '1498131595', '25c1fadce5094b6bbc7fa89514a8ac5b', '1498131595', '0');