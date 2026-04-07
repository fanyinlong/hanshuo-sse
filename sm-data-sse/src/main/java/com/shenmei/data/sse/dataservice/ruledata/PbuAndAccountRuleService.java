package com.shenmei.data.sse.dataservice.ruledata;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shenmei.data.sse.domain.SseDataRule;
import com.shenmei.data.sse.domain.SseDataRuleInit;
import com.shenmei.data.sse.domain.SsePbuConfig;
import com.shenmei.data.sse.service.ISseAccountInfoService;
import com.shenmei.data.sse.service.ISseDataRuleService;
import com.shenmei.data.sse.service.ISsePbuConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ description 根据规则配置，提取pbu和账户集合
 */

@Service
public class PbuAndAccountRuleService {


    /**
     * 查找pbu和账户集合
     * 1.根据配置查找，
     *  bizpbu和登录pbu的配置比，30个业务pbu和10个登录pbu，每个登录pbu的业务pbu要大于等于3
     *  每个业务pbu的账户数量配置，20个业务pbu的账户数量要大于等于6，10个登录pbu的账户数量要大于等于1
     *  pbu_account_list
     * 汇总数据结构
     * [
     * {
     *   "loginPbu": "12001",
     *   "bizPbuAccout": [
     *     {
     *       "bizPbu": "12101",
     *       "account": ["A10001", "A10002", "A10003"]
     *     },
     *     {
     *       "bizPbu": "12102",
     *       "account": ["A10001", "A10002", "A10003"]
     *     },
     *     {
     *       "bizPbu": "12103",
     *       "account": ["A10001", "A10002", "A10003"]
     *     }
     *   ]
     * },
     * {
     *   "loginPbu": "12002",
     *   "bizPbuAccout": [
     *     {
     *       "bizPbu": "12101",
     *       "account": ["A10001", "A10002", "A10003"]
     *     },
     *     {
     *       "bizPbu": "12102",
     *       "account": ["A10001", "A10002", "A10003"]
     *     },
     *     {
     *       "bizPbu": "12103",
     *       "account": ["A10001", "A10002", "A10003"]
     *     }
     *   ]
     * }
     * ]
     * @param args
     */

    /**
     * 获取pbu和账户集合
     */
    // pbu_account_number_list

    private static final Logger log = LoggerFactory.getLogger(PbuAndAccountRuleService.class);

    //需要初始化的ruleId
    private Long ruleId;
    //登陆pbu列表
    private List<String> loginPbuList = new ArrayList<>();

    //业务pbu数目和账户数目对应关系列表
    private List<Long[]> pbuAccountList = new ArrayList<>();

    //bizPbu， accountList的Map列表
    private List<Map<String,Object>> pbuAccountsMapList = new ArrayList<>();

    //loginPbu，bizPbu，accountList的Map列表
    private List<Map<String,Object>> pbuPbuAccountMapList = new ArrayList<>();
    @Resource
    private ISsePbuConfigService ssePbuConfigService;
    @Resource
    private ISseAccountInfoService sseAccountInfoService;
    @Resource
    private ISseDataRuleService sseDataRuleService;


    public SseDataRuleInit setPbuAccountByRule(SseDataRuleInit sseDataRuleInit) throws JsonProcessingException {

        //反初始化，清除上一次初始化留在sse_pbu_config表中的数据
        ruleId=sseDataRuleInit.getRuleId();
        resetPbuConfig();

        //初始化
        loginPbuList.clear();
        pbuAccountList.clear();
        pbuAccountsMapList.clear();
        pbuPbuAccountMapList.clear();

        // 解析accountType,获取pbu和账户数量比例，保存到pbuAccountList
        SseDataRule sseDataRule = sseDataRuleService.selectSseDataRuleByPkId(sseDataRuleInit.getRuleId());
        String accountType=sseDataRule.getAccountType();
        analyzeAccountType(accountType);
        pbuAccountList.sort(Comparator.<Long[]>comparingLong(arr -> arr[1]).reversed());
        // 计算总的 PBU 个数
        long totalPbuCount = pbuAccountList.stream()
                .mapToLong(arr -> arr[0])
                .sum();
        log.info("配置的 PBU 总个数：{}", totalPbuCount);
        // 验证 PBU 总数是否与规则配置一致
        if (totalPbuCount != sseDataRule.getPbuCount()) {
            throw new RuntimeException(String.format("accountType 中配置的 PBU 总个数 (%d) 与规则配置的业务 PBU 总数 (%d) 不一致",
                    totalPbuCount, sseDataRule.getPbuCount()));
        }
        //获取登陆pbu，保存到loginPbuList
        Long loginPbuNumber=sseDataRule.getLoginPbu();
        loginPbuList= setLoginPbuByRule(loginPbuNumber);

        //获取业务pbuId和对应的账户列表accountList保存到pbuAccountsMapList
        setBizPbuAccountList();

        //分配登陆pbu和业务pbu
        int start=0;
        int end =0;
        for(String loginPbuId:loginPbuList){
            int base=pbuAccountsMapList.size()/loginPbuList.size();
            log.info("loginPbuId:"+loginPbuId+" base:"+base);
            int mod=pbuAccountsMapList.size()%loginPbuList.size();
            log.info("loginPbuId:"+loginPbuId+" mod:"+mod);
            if(mod>0&&loginPbuId.indexOf(loginPbuId)<mod){
                start=(base+1)*loginPbuList.indexOf(loginPbuId);
//                log.info("loginPbuId:"+loginPbuId+" start:"+start);
                end=(base+1)*loginPbuList.indexOf(loginPbuId)+base-1;
//                log.info("loginPbuId:"+loginPbuId+" end:"+end);
            }else{
                start=(base)*loginPbuList.indexOf(loginPbuId);
                end=(base)*loginPbuList.indexOf(loginPbuId)+base-1;
//                log.info("loginPbuId:"+loginPbuId+" start:"+start +"end:"+end);
            }
            List<Map<String,Object>> pbuAccountsMapListShort = new ArrayList<>(pbuAccountsMapList.subList(start,end+1));
//            pbuAccountsMapListShort=pbuAccountsMapList.subList(start,end+1);
//            log.info("loginPbuId:"+loginPbuId+" pbuAccountsMapListShort:"+pbuAccountsMapListShort);
            Map<String,Object> pbuPbuAccountMap=new HashMap<>();
            pbuPbuAccountMap.put("bizPbuAccount",pbuAccountsMapListShort);
            pbuPbuAccountMap.put("loginPbu",loginPbuId);
            pbuPbuAccountMapList.add(pbuPbuAccountMap);
            //获取loginPbu和bizPbu的关联关系，保存到ssePbuConfig
            for(Map<String,Object> pbuAccountMap:pbuAccountsMapListShort){
                String bizPbuId=(String)pbuAccountMap.get("bizPbu");
                SsePbuConfig ssePbuConfig = new SsePbuConfig();
                ssePbuConfig.setPbuId(bizPbuId);
                ssePbuConfig=ssePbuConfigService.selectSsePbuConfigList(ssePbuConfig).get(0);
                ssePbuConfig.setBelongtoLoginpbu(loginPbuId);
                ssePbuConfigService.updateSsePbuConfig(ssePbuConfig);
            }
        }
        ObjectMapper objectMapper = new ObjectMapper ();
        String pbuAccountList = objectMapper.writerWithDefaultPrettyPrinter ().writeValueAsString (pbuPbuAccountMapList);
        log.info("pbuAccountList:"+pbuAccountList);
        sseDataRuleInit.setPbuAccountList(pbuAccountList);
        return sseDataRuleInit;
    }

    /**
     * 解析accountType
     * @param accountType DataRule中的accountType字段，格式如{"A":{"ratio":100,"details":[{"pbuCount":20,"accountsPerPbu":6},{"pbuCount":10,"accountsPerPbu":1}]}}
     */
    private void analyzeAccountType(String accountType) {

        // 解析JSON字符串
        JSONObject jsonObject = JSON.parseObject(accountType);
        // 提取A类:个人投资者账户配置信息
        if (jsonObject.containsKey("A")) {
            JSONObject accountAObject = jsonObject.getJSONObject("A");
            // 提取A类账户与业务pbu的配置信息
            if (accountAObject.containsKey("details")) {
                JSONArray accountADetails = accountAObject.getJSONArray("details");
                for (int i = 0; i < accountADetails.size(); i++) {
                    JSONObject accountADetail = accountADetails.getJSONObject(i);
                    // 提取A类账户与业务pbu的配置信息
                    if (accountADetail.containsKey("pbuCount")) {
                        pbuAccountList.add(new Long[]{accountADetail.getLong("pbuCount"), accountADetail.getLong("accountsPerPbu")});
                    }
                }
            }
        }
    }
    /**
     * 获取loginPbu
     * @param loginPbuNumber 登陆pbu数量
     * @return List<String>loginPbuList</>
     */
    private List<String> setLoginPbuByRule(Long loginPbuNumber){
        List<String> loginPbuList = new ArrayList<>();
        loginPbuList=ssePbuConfigService.selectLoginPbuIdList(loginPbuNumber);
        //更新loginPbuList的使用情况
        for (String loginPbuId:loginPbuList){
            SsePbuConfig ssePbuConfig = new SsePbuConfig();
            ssePbuConfig.setPbuId(loginPbuId);
            ssePbuConfig=ssePbuConfigService.selectSsePbuConfigList(ssePbuConfig).get(0);
            ssePbuConfig.setIsLoginpbu("1");
            ssePbuConfig.setIsBizpbu("0");
            ssePbuConfig.setIsUsed("1");
            ssePbuConfig.setRuleId(ruleId);
            ssePbuConfigService.updateSsePbuConfig(ssePbuConfig);
        }
        return loginPbuList;
    }

    /**
     * 获取bizPbu
     * @param bizPbuCount,accountPerPbu， 业务pbu数量，账户数量
     * @return List<String>loginPbuList</>
     */
    private List<String> setBizPbuByRule(Long bizPbuCount, Long accountPerPbu){
        List<String> bizPbuList = new ArrayList<>();
        bizPbuList=ssePbuConfigService.selectBizPbuIdList(bizPbuCount,accountPerPbu);
        //更新bizPbuList的使用情况
        for (String bizPbuId :bizPbuList){
            SsePbuConfig ssePbuConfig = new SsePbuConfig();
            ssePbuConfig.setPbuId(bizPbuId);
            ssePbuConfig=ssePbuConfigService.selectSsePbuConfigList(ssePbuConfig).get(0);
            ssePbuConfig.setIsLoginpbu("0");
            ssePbuConfig.setIsBizpbu("1");
            ssePbuConfig.setIsUsed("1");
            ssePbuConfig.setRuleId(ruleId);
            ssePbuConfigService.updateSsePbuConfig(ssePbuConfig);
        }

        return bizPbuList;
    }

    /**
     * 根据pbuId获取AccountList
     * @param bizPbuId
     * @param accountPerPbu
     * @return accountList
     */
    private List<String> setAccountByRule(String bizPbuId, Long accountPerPbu){
        List<String> accountList = new ArrayList<>();
        accountList=sseAccountInfoService.selectAccountListByPbuId(bizPbuId,accountPerPbu);
        return accountList;
    }

    /**
     *  获取业务pbuId，账户列表accountList保存到pbuAccountsMapList
     */
    private void setBizPbuAccountList(){
        //获取业务pbuId，账户列表accountList保存到pbuAccountsMapList
        for(Long[] pbuAccount:pbuAccountList){
            Long bizPbuCount=pbuAccount[0];
            Long accountPerPbu=pbuAccount[1];
            List<String> bizPbuList= setBizPbuByRule(bizPbuCount,accountPerPbu);
            //获取账户，保存到accountList
            for(String bizPbuId:bizPbuList){
                Map<String,Object> pbuAccountsMap=new HashMap<>();
                List<String> accountList= setAccountByRule(bizPbuId,accountPerPbu);
                pbuAccountsMap.put("bizPbu",bizPbuId);
                pbuAccountsMap.put("account",accountList);
                pbuAccountsMapList.add(pbuAccountsMap);
            }}
    }

    /**
     *
     * @throws JsonProcessingException
     */
    private void resetPbuConfig(){
        SsePbuConfig ssePbuConfig = new SsePbuConfig();
        ssePbuConfig.setRuleId(ruleId);
        List<SsePbuConfig> ssePbuConfigList = new ArrayList<>();
        if(!ssePbuConfigService.selectSsePbuConfigList(ssePbuConfig).isEmpty()) {
            ssePbuConfigList = ssePbuConfigService.selectSsePbuConfigList(ssePbuConfig);
            for (SsePbuConfig ssePbuConfig1 : ssePbuConfigList) {
                ssePbuConfig1.setIsLoginpbu("0");
                ssePbuConfig1.setIsBizpbu("0");
                ssePbuConfig1.setIsUsed("0");
                ssePbuConfig1.setRuleId(null);
                ssePbuConfigService.updateSsePbuConfig(ssePbuConfig1);
            }
        }
    }

    public static void main(String[] args) throws JsonProcessingException {


    }

}
