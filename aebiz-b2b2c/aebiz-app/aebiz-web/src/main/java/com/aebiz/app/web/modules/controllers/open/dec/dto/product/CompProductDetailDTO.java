package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

import java.util.List;

/**
 * 组件商品详细信息DTO
 *
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class CompProductDetailDTO {

    private ProductDetailDTO productDetailDTO;

    private List<SelectedAttributeInValueJsonDTO> selectedAttributeInValueJsonDTOs;


    public CompProductDetailDTO() {
    }

    public CompProductDetailDTO(ProductDetailDTO productDetailDTO, List<SelectedAttributeInValueJsonDTO> selectedAttributeInValueJsonDTOs) {
        this.productDetailDTO = productDetailDTO;
        this.selectedAttributeInValueJsonDTOs = selectedAttributeInValueJsonDTOs;
    }

    public ProductDetailDTO getProductDetailDTO() {
        return productDetailDTO;
    }

    public void setProductDetailDTO(ProductDetailDTO productDetailDTO) {
        this.productDetailDTO = productDetailDTO;
    }

    public List<SelectedAttributeInValueJsonDTO> getSelectedAttributeInValueJsonDTOs() {
        return selectedAttributeInValueJsonDTOs;
    }

    public void setSelectedAttributeInValueJsonDTOs(List<SelectedAttributeInValueJsonDTO> selectedAttributeInValueJsonDTOs) {
        this.selectedAttributeInValueJsonDTOs = selectedAttributeInValueJsonDTOs;
    }

    @Override
    public String toString() {
        return "CompProductDetailDTO{" +
                "productDetailDTO=" + productDetailDTO +
                ", selectedAttributeInValueJsonDTOs=" + selectedAttributeInValueJsonDTOs +
                '}';
    }
}
