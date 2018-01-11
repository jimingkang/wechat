package org.ldlood.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.ldlood.dataobject.OrderDetail;
import org.ldlood.dto.OrderDTO;
import org.ldlood.enums.ResultEnum;
import org.ldlood.exception.SellException;
import org.ldlood.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ldlood on 2017/7/23.
 */
@Slf4j
public class OrderFormToOrderDTOConverter {
    public static OrderDTO convert(OrderForm orderForm) {
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>() {
            }.getType());
        } catch (Exception ex) {
            log.error("【格式转换错误】restl={}", orderForm.getItems());
            log.error(ex.getMessage());
            throw new SellException(ResultEnum.PARAM_ERROR);

        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
