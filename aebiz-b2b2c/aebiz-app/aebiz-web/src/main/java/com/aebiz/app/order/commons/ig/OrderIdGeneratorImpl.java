package com.aebiz.app.order.commons.ig;

import com.aebiz.app.order.commons.utils.OrderUtil;
import com.aebiz.app.order.modules.services.OrderMainService;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderIdGeneratorImpl implements OrderIdGenerator{

    @Autowired
    private OrderMainService orderMainService;

    public OrderIdGeneratorImpl() {
    }


    public String next(){
        String key = Times.format("yyMMddHHmmss",Times.now()) + OrderUtil.getRandNum(100000,999999);
        Sql sql = Sqls.queryString("select id from order_main where id=@id");
        sql.setParam("id",key);
        sql.setCallback(Sqls.callback.str());
        orderMainService.dao().execute(sql);
        String result  =  sql.getString();
        if(Strings.isNotBlank(result)){
            next();
        }
        return key;
    }

    public Object run(List<Object> fetchParam){

        return next();
    }

    public String fetchSelf() {
        return "order";
    }

}
