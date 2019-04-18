/* updateSysMenu111 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('ea54835bfd1649548ea539f3e5216daa','','0006','商品','Goods','menu','','','','1','0','goods',NULL,'2','1','','1475997459','0', 'platform')
/* updateSysMenu1111 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('5fec95aaa738485da39c1901d47ce2d2','ea54835bfd1649548ea539f3e5216daa','00060001','商品管理','Manager','menu','','','ti-shopping-cart','1','0','goods.manager',NULL,'58','1','','1475997543','0', 'platform')
/* updateSysMenu1112 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('bc24dbcccaa941c8906223072be16a37','5fec95aaa738485da39c1901d47ce2d2','000600010001','商品列表','List','menu','/platform/goods/manager/goods','data-pjax','','1','0','goods.manager.goods',NULL,'59','0','','1475997652','0', 'platform')
/* updateSysMenu1113 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('6d6b51ed76924904b570acfdf2ebcee3','bc24dbcccaa941c8906223072be16a37','0006000100010001','添加商品','Add','data','','','','0','0','goods.manager.goods.add',NULL,'60','0','','1475997721','0', 'platform')
/* updateSysMenu1114 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('055a496251724329a569200e3bf30750','bc24dbcccaa941c8906223072be16a37','0006000100010002','修改商品','Edit','data','','','','0','0','goods.manager.goods.edit',NULL,'61','0','','1475997733','0', 'platform')
/* updateSysMenu1115 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('5723deb629e744edadfec0455175c09a','bc24dbcccaa941c8906223072be16a37','0006000100010003','删除商品','Delete','data','','','','0','0','goods.manager.goods.delete',NULL,'62','0','','1475997710','0', 'platform')
/* updateSysMenu1116 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('baa75b5c09ba485eaf41fb53d64ec2d4','ea54835bfd1649548ea539f3e5216daa','00060002','商品配置','Config','menu','','','ti-settings','1','0','goods.conf',NULL,'63','1','','1475997799','0', 'platform')
/* updateSysMenu1117 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('6776829efb244438b024b177e42b60aa','baa75b5c09ba485eaf41fb53d64ec2d4','000600020001','商品分类','Class','menu','/platform/goods/class','data-pjax','','1','0','goods.conf.class',NULL,'64','0','','1475997849','0', 'platform')
/* updateSysMenu1118 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('55d83cf6d3064747b1328f27472ff682','6776829efb244438b024b177e42b60aa','0006000200010001','添加分类','Add','data','','','','0','0','goods.conf.class.add',NULL,'65','0','','1475997871','0', 'platform')
/* updateSysMenu1119 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('3202a3c02d804137b08d4ec3deac8984','6776829efb244438b024b177e42b60aa','0006000200010002','修改分类','Edit','data','','','','0','0','goods.conf.class.edit',NULL,'66','0','','1475997884','0', 'platform')
/* updateSysMenu11110 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('74d31e653e2c43d6937fb4538ece6a55','6776829efb244438b024b177e42b60aa','0006000200010003','删除分类','Delete','data','','','','0','0','goods.conf.class.delete',NULL,'67','0','','1475997916','0', 'platform')
/* updateSysMenu11111 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('199c032cc9624c5d8ee06bd45dd09f48','baa75b5c09ba485eaf41fb53d64ec2d4','000600020002','商品类型','Type','menu','/platform/goods/type','data-pjax','','1','0','goods.conf.type',NULL,'68','0','','1475998140','0', 'platform')
/* updateSysMenu11112 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('ed374f48b814427484c51dd12481b5e9','199c032cc9624c5d8ee06bd45dd09f48','0006000200020001','添加类型','Add','data','','','','0','0','goods.conf.type.add',NULL,'69','0','','1475998156','0', 'platform')
/* updateSysMenu11113 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('bbdca10769d44f76932b2f9dcd5964d5','199c032cc9624c5d8ee06bd45dd09f48','0006000200020002','修改类型','Edit','data','','','','0','0','goods.conf.type.edit',NULL,'70','0','','1475998166','0', 'platform')
/* updateSysMenu11114 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('33d6d0fe153d4f9092b4f68be5725844','199c032cc9624c5d8ee06bd45dd09f48','0006000200020003','删除类型','Delete','data','','','','0','0','goods.conf.type.delete',NULL,'71','0','','1475998176','0', 'platform')
/* updateSysMenu11115 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('98464e4910d84bcd815023a32e7247ac','baa75b5c09ba485eaf41fb53d64ec2d4','000600020003','商品规格','Spec','menu','/platform/goods/spec','data-pjax','','1','0','goods.conf.spec',NULL,'72','0','','1475998233','0', 'platform')
/* updateSysMenu11116 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('25759929f81a4444976d710932e2a7b8','98464e4910d84bcd815023a32e7247ac','0006000200030001','添加规格','Add','data','','','','0','0','goods.conf.spec.add',NULL,'73','0','','1475998246','0', 'platform')
/* updateSysMenu11117 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('5ef841b114704008a6eeb22ee78bdf0c','98464e4910d84bcd815023a32e7247ac','0006000200030002','修改规格','Edit','data','','','','0','0','goods.conf.spec.edit',NULL,'74','0','','1475998258','0', 'platform')
/* updateSysMenu11118 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('364bc883d5634c8b9c398ddd534165b5','98464e4910d84bcd815023a32e7247ac','0006000200030003','删除规格','Delete','data','','','','0','0','goods.conf.spec.delete',NULL,'75','0','','1475998268','0', 'platform')
/* updateSysMenu11119 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('932317d1c0f94731b17794cabc93cb57','baa75b5c09ba485eaf41fb53d64ec2d4','000600020004','商品品牌',NULL,'menu','/platform/goods/brand','data-pjax','','1','0','goods.conf.brand',NULL,'76','0','','1475998340','0', 'platform')
/* updateSysMenu11120 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('c731aafcaf8d4d208a0d05deca9a9806','932317d1c0f94731b17794cabc93cb57','0006000200040001','添加品牌',NULL,'data','','','','0','0','goods.conf.brand.add',NULL,'77','0','','1475998356','0', 'platform')
/* updateSysMenu11121 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('e6ee9e2eff664f8da5b37a1417427e51','932317d1c0f94731b17794cabc93cb57','0006000200040002','修改品牌',NULL,'data','','','','0','0','goods.conf.brand.edit',NULL,'78','0','','1475998365','0', 'platform')
/* updateSysMenu11122 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('002bf91c98ce4364bdafff334ce0f39f','932317d1c0f94731b17794cabc93cb57','0006000200040003','删除品牌',NULL,'data','','','','0','0','goods.conf.brand.delete',NULL,'79','0','','1475998374','0', 'platform')
/* updateSysMenu11123 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('a57c59bfffdc11e68852c85b7603b1a6','baa75b5c09ba485eaf41fb53d64ec2d4','000600020005','商品标签',NULL,'menu','/platform/goods/tag','data-pjax','','1','0','goods.conf.tag',NULL,'80','0','','1475998340','0', 'platform')
/* updateSysMenu11124 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('b41740abffdc11e68852c85b7603b1a6','a57c59bfffdc11e68852c85b7603b1a6','0006000200050001','添加标签',NULL,'data','','','','0','0','goods.conf.tag.add',NULL,'81','0','','1475998356','0', 'platform')
/* updateSysMenu11125 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('bea2ed65ffdc11e68852c85b7603b1a6','a57c59bfffdc11e68852c85b7603b1a6','0006000200050002','修改标签',NULL,'data','','','','0','0','goods.conf.tag.edit',NULL,'82','0','','1475998365','0', 'platform')
/* updateSysMenu11126 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag, system) values('c8c09db4ffdc11e68852c85b7603b1a6','a57c59bfffdc11e68852c85b7603b1a6','0006000200050003','删除标签',NULL,'data','','','','0','0','goods.conf.tag.delete',NULL,'83','0','','1475998374','0', 'platform')


