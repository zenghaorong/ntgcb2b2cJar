package com.aebiz.app.shop.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.shop.modules.models.Shop_currency;

public interface ShopCurrencyService extends BaseService<Shop_currency>{

	void clearCache();

	void updateDefaultCurrency(Shop_currency shopCurrency);

	Shop_currency getDefaultCurrency();
}
