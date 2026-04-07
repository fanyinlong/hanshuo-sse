package com.shenmei.data.sse.dataservice.ruledata;

import com.shenmei.data.sse.domain.SseOrderSecurityAccount;
import com.shenmei.data.sse.service.ISseOrderInfoService;
import com.shenmei.data.sse.service.ISseOrderSecurityAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderSecurityAccountCalService {
    private static final Logger log = LoggerFactory.getLogger(OrderSecurityAccountCalService.class);
    @Resource
    private ISseOrderSecurityAccountService sseOrderSecurityAccountService;

    @Resource
    private ISseOrderInfoService sseOrderInfoService;

    public int calOrderSecurityAccount(long ruleId) {
        List<SseOrderSecurityAccount> sseOrderSecurityAccountList = new ArrayList<>();
        List<String> securityList = sseOrderInfoService.selectStatsListByRuleId("security_id", ruleId);
//        log.info("securityList:{}", securityList);
        for(String securityId : securityList){
            List<String> accountList = sseOrderInfoService.selectAccountBySecurityId(securityId, ruleId);
//            log.info("accountList:{}", accountList);
            for(String accountId : accountList){
                SseOrderSecurityAccount sseOrderSecurityAccount = new SseOrderSecurityAccount();
                sseOrderSecurityAccount.setRuleId(ruleId);
                sseOrderSecurityAccount.setSecurityId(securityId);
                sseOrderSecurityAccount.setAccountId(accountId);
                sseOrderSecurityAccount.setField1("TRA  ");
                sseOrderSecurityAccount.setField2("000090000000000");
                sseOrderSecurityAccountList.add(sseOrderSecurityAccount);
            }
        }
        return sseOrderSecurityAccountService.batchInsertSseOrderSecurityAccount(sseOrderSecurityAccountList);
    }
}
