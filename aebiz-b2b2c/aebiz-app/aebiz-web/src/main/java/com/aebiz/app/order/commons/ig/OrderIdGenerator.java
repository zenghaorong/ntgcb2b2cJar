package com.aebiz.app.order.commons.ig;

import org.nutz.el.opt.RunMethod;

public interface OrderIdGenerator extends RunMethod {

    String next() throws Exception;
}
