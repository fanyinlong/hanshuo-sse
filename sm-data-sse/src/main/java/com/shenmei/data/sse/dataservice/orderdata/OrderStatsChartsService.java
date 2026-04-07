package com.shenmei.data.sse.dataservice.orderdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shenmei.data.sse.dataservice.ruledata.CreateOrderInfoService;
import com.shenmei.data.sse.service.ISseOrderStatsService;
import com.shenmei.data.sse.util.DictManager;
import com.shenmei.data.sse.util.JsonKeyTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


@Service
public class OrderStatsChartsService {
    private static final Logger log = LoggerFactory.getLogger(CreateOrderInfoService.class);

    @Resource
    ISseOrderStatsService sseOrderStatsService;

    public String getOrderStatsChartsData(String chartType,long ruleId) throws JsonProcessingException {
        String data = sseOrderStatsService.selectOrdersStatsDetailByRuleId(chartType,ruleId).get(0);
        if(chartType.equals("matching_type_orders")){
            data=JsonKeyTranslator.translateJsonKeys(data, DictManager.getDict("matchingType"));
        }
        log.info("getOrderStatsChartsData:{}",data);
        return data;
    }

}
