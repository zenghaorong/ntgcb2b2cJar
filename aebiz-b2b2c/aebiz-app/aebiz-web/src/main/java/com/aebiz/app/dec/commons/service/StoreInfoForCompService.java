package com.aebiz.app.dec.commons.service;


import com.aebiz.app.web.modules.controllers.open.dec.dto.store.StoreInfoDTO;

import java.util.List;

/**
 * 店铺相关业务接口
 *
 * Created by Aebiz_yjq on 2016/12/14.
 */
public interface StoreInfoForCompService {


	StoreInfoDTO getStoreInfoForReputation(String storeUuid);
	/**
	 * 根据商品sku获取 店铺id
	 * @param sku
	 * @return
	 */

	String getStoreUuidByProductUuid(String sku);

}
