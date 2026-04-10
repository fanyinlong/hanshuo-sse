package com.shenmei.data.sse.dataservice.ruledata;

import com.shenmei.data.sse.domain.SseOrderInfo;
import com.shenmei.data.sse.domain.SseOrderSecurityAccount;
import com.shenmei.data.sse.service.ISseOrderInfoService;
import com.shenmei.data.sse.service.ISseOrderSecurityAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class OrderSecurityAccountCalService {
    
    @Resource
    private ISseOrderSecurityAccountService sseOrderSecurityAccountService;

    @Resource
    private ISseOrderInfoService orderInfoService;

    public int calOrderSecurityAccount(long ruleId) {
        List<SseOrderSecurityAccount> sseOrderSecurityAccountList = new ArrayList<>();
        List<String> securityList = orderInfoService.selectStatsListByRuleId("security_id", ruleId);
//        log.info("securityList:{}", securityList);
        for(String securityId : securityList){
            List<String> accountList = orderInfoService.selectAccountBySecurityId(securityId, ruleId);
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

    public int calOrderSecurityAccountFromSet(Long ruleId, Set<String> securityAccountSet) {
        List<SseOrderSecurityAccount> accountList = new ArrayList<>(securityAccountSet.size());
        for (String key : securityAccountSet) {
            String[] parts = key.split("_", 2);
            if (parts.length == 2) {
                SseOrderSecurityAccount account = new SseOrderSecurityAccount();
                account.setRuleId(ruleId);
                account.setSecurityId(parts[0]);
                account.setAccountId(parts[1]);
                account.setField1("TRA  ");
                account.setField2("000090000000000");
                accountList.add(account);
            }
        }
        return accountList.isEmpty() ? 0 : sseOrderSecurityAccountService.batchInsertSseOrderSecurityAccount(accountList);
    }
}
