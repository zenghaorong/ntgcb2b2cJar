/*member1*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('e6e56ff08cc540c5819dcb44a9a58def', '', '0012', '会员', 'member', 'platform', 'menu', '', '', '', '1', '0', 'member', NULL, '316', '1', '42fcbd35f9424a9b859bd256635d7ffa', '1494473270', '0');
/*member2*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('8860d53003e74880bfc939888ba0a421', 'e6e56ff08cc540c5819dcb44a9a58def', '00120002', '会员配置', 'memberConfiguration', 'platform', 'menu', '', '', '', '1', '0', 'member.configuration', NULL, '325', '1', '42fcbd35f9424a9b859bd256635d7ffa', '1494474838', '0');
/*member3*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('0141f4058c9643b1acb391bc0e51ca15', 'e6e56ff08cc540c5819dcb44a9a58def', '00120001', '会员管理', 'memberManager', 'platform', 'menu', '', '', '', '1', '0', 'member.manager', NULL, '317', '1', '42fcbd35f9424a9b859bd256635d7ffa', '1494474427', '0');
/*member4*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('7333c86f92be4c6f98a3bbeb0fed8882', '0141f4058c9643b1acb391bc0e51ca15', '001200010002', '查看账户余额变动记录', NULL, 'platform', 'data', NULL, NULL, NULL, '0', '0', 'platform.member.account.money', NULL, '319', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474373', '0');
/*member5*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('3756284e0ab74a459f735aca448e713e', '0141f4058c9643b1acb391bc0e51ca15', '001200010004', '添加账户余额变动记录', NULL, 'platform', 'data', NULL, NULL, NULL, '0', '0', 'platform.member.account.money.add', NULL, '321', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474424', '0');
/*member6*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('3d1b559dd0554c6fa5995de96b0006c0', '0141f4058c9643b1acb391bc0e51ca15', '001200010001', '查看账户积分变动记录', NULL, 'platform', 'data', NULL, NULL, NULL, '0', '0', 'platform.member.account.score', NULL, '318', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474347', '0');
/*member7*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('2f94f779faea4cf5bf7a0f44b3a54de0', '0141f4058c9643b1acb391bc0e51ca15', '001200010003', '添加账户积分变动记录', NULL, 'platform', 'data', NULL, NULL, NULL, '0', '0', 'platform.member.account.score.add', NULL, '320', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474395', '0');
/*member8*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('1565df44148545bd9f837efbcbd7413d', '8860d53003e74880bfc939888ba0a421', '001200020002', '会员等级', 'memberLevel', 'platform', 'menu', '/platform/member/level', 'data-pajx', '', '1', '0', 'platform.member.level', NULL, '330', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474995', '0');
/*member9*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('5cb4b728e9c64859ae0c294da438a4b4', '1565df44148545bd9f837efbcbd7413d', '0012000200020001', '添加', NULL, 'platform', 'data', NULL, NULL, NULL, '0', '0', 'platform.member.level.add', NULL, '331', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474995', '0');
/*member10*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('984bee5062354f1eb0c90e9eaaabe33e', '1565df44148545bd9f837efbcbd7413d', '0012000200020003', '删除', NULL, 'platform', 'data', NULL, NULL, NULL, '0', '0', 'platform.member.level.delete', NULL, '333', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474996', '0');
/*member11*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('d41cbe72fbf6416493b7b6a4d9080b0e', '1565df44148545bd9f837efbcbd7413d', '0012000200020002', '修改', NULL, 'platform', 'data', NULL, NULL, NULL, '0', '0', 'platform.member.level.edit', NULL, '332', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474996', '0');
/*member12*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('d748d0c737d8424bb347ac73b8d8ffd2', '8860d53003e74880bfc939888ba0a421', '001200020001', '会员类型', 'memberType', 'platform', 'menu', '/platform/member/type', 'data-pajx', '', '1', '0', 'platform.member.type', NULL, '326', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474916', '0');
/*member13*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('10e4532e91ea470c9ef6e6584a654647', 'd748d0c737d8424bb347ac73b8d8ffd2', '0012000200010001', '添加', NULL, 'platform', 'data', NULL, NULL, NULL, '0', '0', 'platform.member.type.add', NULL, '327', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474917', '0');
/*member14*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('c37efdb4aacd4f94be7f3f06dd574c70', 'd748d0c737d8424bb347ac73b8d8ffd2', '0012000200010003', '删除', NULL, 'platform', 'data', NULL, NULL, NULL, '0', '0', 'platform.member.type.delete', NULL, '329', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474917', '0');
/*member15*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('7426b1143c8b4410ad1179f896363642', 'd748d0c737d8424bb347ac73b8d8ffd2', '0012000200010002', '修改', NULL, 'platform', 'data', NULL, NULL, NULL, '0', '0', 'platform.member.type.edit', NULL, '328', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474917', '0');
/*member16*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('3a99bc8749cd42a08d56bfcdb7076398', '0141f4058c9643b1acb391bc0e51ca15', '001200010005', '会员列表', 'memberList', 'platform', 'menu', '/platform/member/user', 'data-pajx', '', '1', '0', 'platform.member.user', NULL, '322', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474745', '0');
/*member17*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('ec8a92bc012249a9925f4a2b252c0a45', '3a99bc8749cd42a08d56bfcdb7076398', '0012000100050001', '添加', NULL, 'platform', 'data', NULL, NULL, NULL, '0', '0', 'platform.member.user.add', NULL, '323', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474745', '0');
/*member18*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('990bcea0071b4ccab3f2204710e00a4f', '3a99bc8749cd42a08d56bfcdb7076398', '0012000100050002', '修改', NULL, 'platform', 'data', NULL, NULL, NULL, '0', '0', 'platform.member.user.edit', NULL, '324', '0', '42fcbd35f9424a9b859bd256635d7ffa', '1494474746', '0');
/*member19*/
INSERT INTO sys_menu (id, parentId, path, name, aliasName, system, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) VALUES ('c7b56d27cab347e29143554c6b3460b9', '3a99bc8749cd42a08d56bfcdb7076398', '0012000100050003', '重置密码', NULL, 'platform', 'data', NULL, NULL, NULL, '0', '0', 'platform.member.user.resetPwd', NULL, '162', '0', '25c1fadce5094b6bbc7fa89514a8ac5b', '1499690544', '0');







