package com.shenmei.data.sse.dataservice.ruledata;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shenmei.data.sse.constant.MatchTypeEnum;
import com.shenmei.data.sse.domain.SseDataRule;
import com.shenmei.data.sse.domain.SseSecurityInfo;
import com.shenmei.data.sse.dto.CountCommonDo;
import com.shenmei.data.sse.service.impl.SseSecurityInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * description: 规则配置验证服务
 * 根据用户配置计算每个 SET 上的股票详细配置和订单数量
 * 同时满足证券子类别分类占比/产品所属 SET 占比，撮合方式占比
 *
 * @author: teabot
 * @date: 2025-08-03
 */
@Service
@Slf4j
public class RuleValidationService {

    @Resource
    private SseSecurityInfoServiceImpl sseSecurityInfoService;

    /**
     * 验证规则配置并计算每个 SET 的股票和订单分配
     *
     * @param sseDataRule 规则配置
     * @return 验证结果，包含每个 SET 的股票分配和订单数量
     */
    public Map<String, Object>
    validateAndCalculateSetAllocation(SseDataRule sseDataRule) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        String errors = new String();

        try {
            // 1. 分配 SET 股票数量和比例
            Map<String, String> setSecurityJson = setSecuritySetQuantity(sseDataRule, errors);

            if (!errors.isEmpty()) {
                result.put("success", false);
                result.put("errors", errors);
                return result;
            }

            // 2. 计算每个 SET 的订单数量（包含撮合方式分布）
            Map<String, Map<String, MatchMethodDistribution>> matchMethodDistributionMap =
                    calculateMatchMethodDistribution(sseDataRule, setSecurityJson);

            // 3. 验证撮合订单数是否满足一撮多配置
            result = validateMatchMultiLegCapacity(sseDataRule, matchMethodDistributionMap);

            // 4. 打印详细信息
            printAllocationDetail(setSecurityJson, matchMethodDistributionMap);

        } catch (Exception e) {
            log.error("规则配置验证失败", e);
            errors = ("验证失败：" + e.getMessage());
            result.put("success", false);
            result.put("errors", errors);
        }

        return result;
    }

    /**
     * 验证撮合订单数是否满足一撮多配置
     * 根据前端上送的动态配置计算，如 [{"oneToManyCount":3000,"quantity":1},{"oneToManyCount":5,"quantity":10}]
     * 1 撮 3000=3001 笔订单，1 撮 5=6 笔订单
     *
     * 验证规则：
     * 1. 一撮多总订单数不超过所有 SET 撮合订单之和
     * 2. 每个一撮多必须完整放入同一个证券子类别的同一个 SET，不能拆分
     */
    private Map<String, Object>
    validateMatchMultiLegCapacity(
            SseDataRule sseDataRule,
            Map<String, Map<String, MatchMethodDistribution>> matchMethodDistributionMap) {
        Map<String, Object> result = new HashMap<>();
        List<String> placementLogs = new ArrayList<>();
        JSONArray matchMethodConfig = null;
        if (StringUtils.isNotBlank(sseDataRule.getOneToManyList())) {
            try {
                matchMethodConfig = JSON.parseArray(sseDataRule.getOneToManyList());
            } catch (Exception e) {
                log.error("解析一撮多配置失败，格式应为数组：{}", sseDataRule.getOneToManyList(), e);
                result.put("success", false);
                result.put("errors", "解析一撮多配置失败：" + e.getMessage());
                return result;
            }
        }

        if (matchMethodConfig == null || matchMethodConfig.isEmpty()) {
            log.warn("一撮多配置为空");
            result.put("success", true);
            result.put("errors", "一撮多配置为空");
            return result;
        }


        List<MultiLegInput> multiLegInputs = parseMultiLegInputs(matchMethodConfig);

        if (multiLegInputs.isEmpty()) {
            log.warn("未解析到有效的一撮多配置");
            result.put("success", false);
            result.put("errors", "未解析到有效的一撮多配置");
            return result;
        }

        multiLegInputs.sort((a, b) -> Integer.compare(b.getOrdersPerGroup(), a.getOrdersPerGroup()));

        int totalRequiredOrders = multiLegInputs.stream()
                .mapToInt(MultiLegInput::getTotalOrders)
                .sum();

        int totalMatchOrders = matchMethodDistributionMap.values().stream()
                .flatMap(map -> map.values().stream())
                .mapToInt(dist -> dist.matchOrderNumber)
                .sum();

        boolean totalSufficient = totalMatchOrders >= totalRequiredOrders;

        if (!totalSufficient) {
            log.error("验证失败：一撮多总订单数 ({}) 超过所有 SET 撮合订单总和 ({})",
                    totalRequiredOrders, totalMatchOrders);
            result.put("success", false);
            result.put("errors", String.format("一撮多总订单数 (%d) 超过所有 SET 撮合订单总和 (%d)",
                    totalRequiredOrders, totalMatchOrders));
            return result;
        }

        Map<String, Integer> allocatedMatchOrders = new HashMap<>();

        for (String securityType : matchMethodDistributionMap.keySet()) {
            for (String setId : matchMethodDistributionMap.get(securityType).keySet()) {
                String key = securityType + "_" + setId;
                allocatedMatchOrders.put(key, 0);
            }
        }

        boolean isFirstFailure = true;
        for (MultiLegInput multiLeg : multiLegInputs) {
            int ordersPerSingleUnit = multiLeg.getOrdersPerGroup();

            for (int i = 0; i < multiLeg.getQuantity(); i++) {
                boolean placed = false;

                for (String securityType : matchMethodDistributionMap.keySet()) {
                    if (placed) break;

                    for (String setId : matchMethodDistributionMap.get(securityType).keySet()) {
                        String key = securityType + "_" + setId;
                        int matchOrderNumber = matchMethodDistributionMap.get(securityType).get(setId).matchOrderNumber;

                        int allocated = allocatedMatchOrders.getOrDefault(key, 0);
                        int available = matchOrderNumber - allocated;

                        if (available >= ordersPerSingleUnit) {
                            allocatedMatchOrders.put(key, allocated + ordersPerSingleUnit);
                            placed = true;

                            int remainingAfterPlacement = available - ordersPerSingleUnit;
                            String placementLog = String.format("✓ 一撮多 [%s] 第%d单 (1 撮%d，共%d订单) → 放入 SET[%s](%s), 该 SET 剩余可用=%d",
                                    multiLeg.getConfigKey(), i + 1, multiLeg.getMultiCount(),
                                    ordersPerSingleUnit, setId, securityType, remainingAfterPlacement);
                            placementLogs.add(placementLog);
                            break;
                        }
                    }
                }

                if (!placed) {
                    if (isFirstFailure) {
                        int maxSetOrderNumber = matchMethodDistributionMap.values().stream()
                                .flatMap(map -> map.values().stream())
                                .mapToInt(dist -> dist.matchOrderNumber)
                                .max()
                                .orElse(0);

                        String errorMsg = String.format("无法放置一撮多配置 configKey=%s 的第%d个订单，最大 SET 订单数仅为%d，请减小一撮多数量或增加 SET 容量",
                                multiLeg.getConfigKey(), i + 1, maxSetOrderNumber);
                        log.error(errorMsg);
                        result.put("success", false);
                        result.put("errors", errorMsg);
                        return result;
                    } else {
                        String errorMsg = String.format("无法放置一撮多配置 configKey=%s 的第%d个订单，请调整%s",
                                multiLeg.getConfigKey(), i + 1, multiLeg.getConfigKey());
                        log.error(errorMsg);
                        result.put("success", false);
                        result.put("errors", errorMsg);
                        return result;
                    }
                }
                isFirstFailure = false;

            }
        }


        log.info("==================== 一撮多 SET 分配详情 ====================");
        log.info("总需求订单数：{}, 总供给订单数：{}", totalRequiredOrders, totalMatchOrders);
        log.info("一撮多配置数量：{}", multiLegInputs.size());
        for (String logMsg : placementLogs) {
            log.info(logMsg);
        }
        log.info("===========================================================");

        log.info("验证成功：一撮多配置可以放入 SET，总需求={}, 总供给={}", totalRequiredOrders, totalMatchOrders);
        
        result.put("success", true);
        result.put("message", "验证成功");
        result.put("totalRequiredOrders", String.valueOf(totalRequiredOrders));
        result.put("totalMatchOrders", String.valueOf(totalMatchOrders));

        return result;
    }


    /**
     * 设置 set 股票和比例
     * 照搬 SecuritySetRuleService.setSecuritySetQuantity 方法
     */
    private Map<String, String> setSecuritySetQuantity(SseDataRule sseDataRule, String errors) {
        Map<String, Integer> securityQuantityMap = getSecurityQuantityMap(sseDataRule);
        Map<String, Integer> securitySetQuantityMap = getSecuritySetQuantityMap(sseDataRule);

        // 校验：securityQuantityMap 的 quantity 数量和等于 securitySetQuantityMap 上的 quantity 数量和
        int totalSecurityQuantity = securityQuantityMap.values().stream().mapToInt(Integer::intValue).sum();
        int totalSetQuantity = securitySetQuantityMap.values().stream().mapToInt(Integer::intValue).sum();

        if (securityQuantityMap.isEmpty()) {
            errors = "股票数量分配错误：子类别股票总数量为空！";
            return new HashMap<>();
        }
        if (totalSecurityQuantity != totalSetQuantity) {
            errors= "股票数量分配错误：总数量不匹配。详细信息：子类别股票总数量：" + totalSecurityQuantity + "，set 股票总数量：" + totalSetQuantity;
            return new HashMap<>();
        }

        // 获取第一个股票类型（主板或科创板）
        Iterator<String> keyIterator = securityQuantityMap.keySet().iterator();
        if (!keyIterator.hasNext()) {
            errors = "股票数量分配错误：股票数量分配错误！详细信息：子类别股票数量未设置！";
            return new HashMap<>();
        }
        String firstSecurityType = keyIterator.next();
        int firstSecurityQuantity = securityQuantityMap.get(firstSecurityType);

        // 获取 set 集合大小
        int setCount = securitySetQuantityMap.size();

        // 初始化分配结果
        Map<String, Integer> firstTypeAllocation = new HashMap<>();

        // 为第一个股票类型分配 set
        int remainingQuantity = firstSecurityQuantity;

        // 获取 set 集合列表，按 quantity 倒序排列
        List<String> sortedSetIds = new ArrayList<>(securitySetQuantityMap.keySet());

        // 根据股票类型获取各 set 集合对应的股票数量，用于分配时，不能超过该集合对应的股票数量
        List<CountCommonDo> securitySetCountList = getSecuritySetCountByType(firstSecurityType);
        if (securitySetCountList == null) {
            securitySetCountList = Collections.emptyList();
        }

        // 根据实际可分配股票数量进行分配
        Map<String, Integer> actualSetCountMap = new HashMap<>();
        for (CountCommonDo countCommonDo : securitySetCountList) {
            actualSetCountMap.put(countCommonDo.getValue(), countCommonDo.getCount());
        }

        // 逐一循环分配，确保不超过实际可分配数量，确保每个 set 都能分配到（一笔一笔分配）
        int[] allocatedPerSet = new int[sortedSetIds.size()];
        boolean hasAllocation = true;
        int round = 0;

        while (remainingQuantity > 0 && hasAllocation) {
            hasAllocation = false;
            for (int i = 0; i < sortedSetIds.size() && remainingQuantity > 0; i++) {

                String setId = sortedSetIds.get(i);
                int setCapacity = securitySetQuantityMap.get(setId);
                int actualCount = actualSetCountMap.getOrDefault(setId, 0);

                // 当前已分配数量
                int currentAllocated = allocatedPerSet[i];

                // 检查是否还能继续分配（未达到设定容量且未达到实际数量且还有剩余需分配数量）
                if (currentAllocated < setCapacity &&
                        currentAllocated < actualCount &&
                        remainingQuantity > 0) {

                    // 分配一个单位
                    allocatedPerSet[i] += 1;
                    remainingQuantity -= 1;
                    hasAllocation = true;
                }
            }
            round++;
        }

        log.info("分配次数：{}", round);
        // 将分配结果存入 firstTypeAllocation
        for (int i = 0; i < sortedSetIds.size(); i++) {
            firstTypeAllocation.put(sortedSetIds.get(i), allocatedPerSet[i]);
        }

        // 校验是否分配完毕
        if (remainingQuantity > 0) {
            throw new RuntimeException("第一个股票类型分配失败，剩余数量：" + remainingQuantity);
        }

        // 为第二个股票类型分配（通过从 set 总容量中减去第一个类型的分配量）
        String secondSecurityType = null;
        for (String type : securityQuantityMap.keySet()) {
            if (!type.equals(firstSecurityType)) {
                secondSecurityType = type;
                break;
            }
        }

        Map<String, Integer> secondTypeAllocation = new HashMap<>();
        if (secondSecurityType != null) {
            int secondSecurityQuantity = securityQuantityMap.get(secondSecurityType);
            int secondRemainingQuantity = secondSecurityQuantity;

            // 获取第二个类型的可分配股票数量
            List<CountCommonDo> secondSecuritySetCountList = getSecuritySetCountByType(secondSecurityType);
            if (secondSecuritySetCountList == null) {
                secondSecuritySetCountList = Collections.emptyList();
            }

            Map<String, Integer> secondActualSetCountMap = new HashMap<>();
            for (CountCommonDo countCommonDo : secondSecuritySetCountList) {
                secondActualSetCountMap.put(countCommonDo.getValue(), countCommonDo.getCount());
            }

            for (String setId : sortedSetIds) {
                int setCapacity = securitySetQuantityMap.get(setId);
                int firstAllocated = firstTypeAllocation.get(setId);
                // 第二个类型可分配的数量是 set 总容量减去第一个类型已分配的数量
                int availableForSecond = setCapacity - firstAllocated;
                int secondAllocated = Math.min(secondActualSetCountMap.getOrDefault(setId, 0), availableForSecond);

                secondTypeAllocation.put(setId, secondAllocated);
                secondRemainingQuantity -= secondAllocated;
            }
            log.info("股票类型二分配结果：{}", secondTypeAllocation);

            // 校验第二个类型是否分配完毕
            if (secondRemainingQuantity > 0) {
                throw new RuntimeException("第二个股票类型分配失败，剩余数量：" + secondRemainingQuantity);
            }
        }

        /**
         * 股票比例
         * 计算方式：
         * 循环每个集合，
         * 先提取 set 集合为 0 的数据，如果 securitySetRatioMap 中的 set 集合为 0，则其对应的另外一类型的比例计算方式为：为当前 set 比例
         * 第二步，计算扣除第一步已计算好的值，计算方式为当前股票类型的比例减去为上面第一步已分配的比例，然后乘以当前 set 的类型比例
         */
        Map<String, BigDecimal> securityRatioMap = getSecurityRatioMap(sseDataRule);
        Map<String, BigDecimal> securitySetRatioMap = getSecuritySetRatioMap(sseDataRule);

        // 存储每个 set 的股票类型比例分配结果
        Map<String, Map<String, BigDecimal>> setRatioAllocation = new HashMap<>();

        // 初始化两个股票类型的分配结果
        Map<String, BigDecimal> firstTypeRatioAllocation = new HashMap<>();
        Map<String, BigDecimal> secondTypeRatioAllocation = new HashMap<>();


        Map<String, BigDecimal> firstZeroSetAllocations = new HashMap<>();


        // 第一步：处理 set 的 quantity 集合为 0 的数据，
        // 其对应的另外一类型的比例计算方式为：当前 set 比例
        for (String firstSet : firstTypeAllocation.keySet()) {
            int quantity = firstTypeAllocation.get(firstSet);
            if (quantity == 0) {
                BigDecimal setRatio = securitySetRatioMap.getOrDefault(firstSet, BigDecimal.ZERO);
                BigDecimal secondTypeRatio = securityRatioMap.getOrDefault(secondSecurityType, BigDecimal.ZERO);

                if (setRatio.compareTo(BigDecimal.ZERO) > 0 && secondTypeRatio.compareTo(BigDecimal.ZERO) > 0) {
                    firstTypeRatioAllocation.put(firstSet, BigDecimal.ZERO);
                    firstZeroSetAllocations.put(firstSet, setRatio);
                } else {
                    firstTypeRatioAllocation.put(firstSet, BigDecimal.ZERO);
                }
            }
        }


        //计算各类型为 0 的比例总和
        // 计算第一步已分配的比例总和，待扣除，计算分配比例时，需要扣除该占比
        BigDecimal deduct_firstZeroTotalRatio = BigDecimal.ZERO;
        BigDecimal deduct_secondZeroTotalRatio = BigDecimal.ZERO;
        BigDecimal deduct_totalRatio = BigDecimal.ZERO;
        for (BigDecimal ratio : firstZeroSetAllocations.values()) {
            if (ratio != null) {
                deduct_firstZeroTotalRatio = deduct_firstZeroTotalRatio.add(ratio);
            }
        }

        // 计算第一步已分配的比例总和
        BigDecimal assigned_firstRatio = BigDecimal.ZERO;

        for (BigDecimal ratio : firstTypeRatioAllocation.values()) {
            if (ratio != null) {
                assigned_firstRatio = assigned_firstRatio.add(ratio);
            }
        }

        // 计算剩余比例
        BigDecimal remainingFirstTypeRatio = securityRatioMap.getOrDefault(firstSecurityType, BigDecimal.ZERO)
                .subtract(assigned_firstRatio);

        // 按照 set 比例分配剩余比例
        BigDecimal totalNonZeroSetRatio = securitySetRatioMap.values().stream()
                .filter(ratio -> ratio.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalNonZeroSetRatio.compareTo(BigDecimal.ZERO) > 0) {
            for (String setId : sortedSetIds) {
                BigDecimal setRatio = securitySetRatioMap.getOrDefault(setId, BigDecimal.ZERO);

                // 检查当前 set 的 quantity 是否非 0
                boolean isFirstTypeNonZero = firstTypeAllocation.getOrDefault(setId, 0) > 0;
                boolean isSecondTypeNonZero = secondTypeAllocation.getOrDefault(setId, 0) > 0;

                if (setRatio.compareTo(BigDecimal.ZERO) > 0) {
                    // 只有当股票类型在该 set 中有分配 (quantity>0) 时才进行计算
                    if (isFirstTypeNonZero && !firstTypeRatioAllocation.containsKey(setId)) {
                        BigDecimal firstTypeAllocated = remainingFirstTypeRatio
                                .multiply(setRatio)
                                .divide(totalNonZeroSetRatio.subtract(deduct_firstZeroTotalRatio), 8, RoundingMode.HALF_UP);
                        firstTypeRatioAllocation.put(setId, firstTypeAllocated);
                    }

                }
            }
        }

        for (String secondSet : secondTypeAllocation.keySet()) {
            //set 比例总和减去 firstTypeRatioAllocation 对应分配的 set 比例
            BigDecimal setRatio = securitySetRatioMap.getOrDefault(secondSet, BigDecimal.ZERO);
            secondTypeRatioAllocation.put(secondSet, setRatio.subtract(firstTypeRatioAllocation.getOrDefault(secondSet, BigDecimal.ZERO)));

        }


        // 确保所有 setId 都有对应的值
        for (String setId : sortedSetIds) {
            if (!firstTypeRatioAllocation.containsKey(setId)) {
                firstTypeRatioAllocation.put(setId, BigDecimal.ZERO);
            }
            if (!secondTypeRatioAllocation.containsKey(setId)) {
                secondTypeRatioAllocation.put(setId, BigDecimal.ZERO);
            }
        }

// 合并结果到 setRatioAllocation
        setRatioAllocation.put(firstSecurityType, firstTypeRatioAllocation);
        setRatioAllocation.put(secondSecurityType, secondTypeRatioAllocation);

        log.info("股票类型一比例分配结果：{}", firstTypeRatioAllocation);
        log.info("股票类型二比例分配结果：{}", secondTypeRatioAllocation);

// 验证比例总和是否正确
        BigDecimal firstTypeTotalRatio = firstTypeRatioAllocation.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal secondTypeTotalRatio = secondTypeRatioAllocation.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("股票类型一比例总和：{}", firstTypeTotalRatio);
        log.info("股票类型二比例总和：{}", secondTypeTotalRatio);
        log.info("股票类型一原始比例：{}", securityRatioMap.getOrDefault(firstSecurityType, BigDecimal.ZERO));
        log.info("股票类型二原始比例：{}", securityRatioMap.getOrDefault(secondSecurityType, BigDecimal.ZERO));

        // 合并结果到 setRatioAllocation
        setRatioAllocation.put(firstSecurityType, firstTypeRatioAllocation);
        setRatioAllocation.put(secondSecurityType, secondTypeRatioAllocation);

        // 判断股票类型是否主板还是科创板
        String firstTypeJson = "{}";
        String secondTypeJson = "{}";


        Map<String, Integer> first_orderSetQty = calcOrderSetQty(sseDataRule, firstTypeRatioAllocation, firstSecurityType);
        Map<String, Integer> second_orderSetQty = calcOrderSetQty(sseDataRule, secondTypeRatioAllocation, secondSecurityType);

        // 组合 firstTypeAllocation 和 firstTypeRatioAllocation 为包含 quantity 和 ratio 的结构
        Map<String, Map<String, Object>> firstTypeCombined = new HashMap<>();
        Map<String, Map<String, Object>> secondTypeCombined = new HashMap<>();

        // 处理 firstType 的数据组合
        for (String setId : firstTypeAllocation.keySet()) {
            Map<String, Object> combinedData = new HashMap<>();
            combinedData.put("quantity", firstTypeAllocation.get(setId));
            combinedData.put("ratio", firstTypeRatioAllocation.getOrDefault(setId, BigDecimal.ZERO));
            combinedData.put("orderNumber", first_orderSetQty.get(setId));
            firstTypeCombined.put(setId, combinedData);
        }

        // 处理 secondType 的数据组合
        for (String setId : secondTypeAllocation.keySet()) {
            Map<String, Object> combinedData = new HashMap<>();
            combinedData.put("quantity", secondTypeAllocation.get(setId));
            combinedData.put("ratio", secondTypeRatioAllocation.getOrDefault(setId, BigDecimal.ZERO));
            combinedData.put("orderNumber", second_orderSetQty.get(setId));
            secondTypeCombined.put(setId, combinedData);
        }

        // 处理空值情况，确保不会出现 null
        if (!firstTypeCombined.isEmpty()) {
            firstTypeJson = JSON.toJSONString(firstTypeCombined);
        }

        if (!secondTypeCombined.isEmpty()) {
            secondTypeJson = JSON.toJSONString(secondTypeCombined);
        }

        Map<String, String> result = new HashMap<>();
        final String KSH = "KSH";
        final String ASHR = "ASH";

        if (KSH.equals(firstSecurityType)) {
            result.put("KSH", firstTypeJson);
            result.put("ASH", secondTypeJson);
        } else if (ASHR.equals(firstSecurityType)) {
            result.put("KSH", secondTypeJson);
            result.put("ASH", firstTypeJson);
        }

        return result;
    }

    /**
     * 计算每个 SET 的撮合方式分布（同时满足证券子类别占比、SET 占比、撮合方式占比）
     */
    private Map<String, Map<String, MatchMethodDistribution>> calculateMatchMethodDistribution(
            SseDataRule sseDataRule, Map<String, String> setSecurityJson) {

        Map<String, Map<String, MatchMethodDistribution>> resultMap = new HashMap<>();

        // 解析撮合方式配置
        JSONObject matchMethodConfig = null;
        if (StringUtils.isNotBlank(sseDataRule.getMatchMethod())) {
            matchMethodConfig = JSON.parseObject(sseDataRule.getMatchMethod());
        }

        if (matchMethodConfig == null || matchMethodConfig.isEmpty()) {
            log.warn("撮合方式配置为空");
            return resultMap;
        }

        Long orderCount = sseDataRule.getOrderCount();
        if (orderCount == null || orderCount == 0) {
            log.warn("订单总数未设置或为 0");
            return resultMap;
        }

        // 遍历每个证券子类别
        for (String securityType : setSecurityJson.keySet()) {
            String typeJson = setSecurityJson.get(securityType);
            JSONObject typeData = JSON.parseObject(typeJson);

            Map<String, MatchMethodDistribution> setDistributionMap = new HashMap<>();

            // 获取该证券子类别的配置
            BigDecimal securityRatio = BigDecimal.ZERO;
            if (StringUtils.isNotBlank(sseDataRule.getSecuritySubCategory())) {
                JSONObject securitySubCategoryJson = JSON.parseObject(sseDataRule.getSecuritySubCategory());
                if (securitySubCategoryJson.containsKey(securityType)) {
                    securityRatio = securitySubCategoryJson.getJSONObject(securityType).getBigDecimal("ratio");
                }
            }

            // 计算该证券子类别的订单总数
            BigDecimal totalOrderForSecurityType = new BigDecimal(orderCount)
                    .multiply(securityRatio)
                    .divide(BigDecimal.valueOf(100), 0, RoundingMode.UP);

            // 遍历该证券子类别下的每个 SET
            for (String setId : typeData.keySet()) {
                JSONObject setData = typeData.getJSONObject(setId);
                BigDecimal setRatio = setData.getBigDecimal("ratio");

                // 避免 setRatio 为 null
                if (setRatio == null) {
                    log.warn("SET{} 的比例为 null，跳过", setId);
                    continue;
                }

                // 计算该 SET 的订单数（基于总订单数和 SET 比例）
                int setOrderNumber = new BigDecimal(orderCount)
                        .multiply(setRatio)
                        .divide(BigDecimal.valueOf(100), 0, RoundingMode.UP)
                        .intValue();

                // 计算撮合方式分布
                MatchMethodDistribution distribution = calculateSingleSetMatchMethod(
                        setOrderNumber, matchMethodConfig, setId, securityType);
                setDistributionMap.put(setId, distribution);
            }

            // 调整订单数量，确保总和匹配
            adjustOrderQuantity(setDistributionMap, totalOrderForSecurityType.intValue(), securityType.toString());

            resultMap.put(securityType, setDistributionMap);
        }

        return resultMap;
    }

    /**
     * 计算单个 SET 的撮合方式分布
     */
    private MatchMethodDistribution calculateSingleSetMatchMethod(
            int setOrderNumber, JSONObject matchMethodConfig, String setId, String securityType) {

        MatchTypeEnum matchTypeEnum = "ASH".equals(securityType) ? MatchTypeEnum.ASH : MatchTypeEnum.KSH;

        String matchKey = matchTypeEnum.getMatchKey();
        String cancelKey = matchTypeEnum.getCancelKey();
        String invalidKey = matchTypeEnum.getInvalidKey();
        String noMatchKey = matchTypeEnum.getNoMatchKey();


        // 获取原始比例（已经是百分比，需要除以 100）
        BigDecimal matchOrderRationOri = matchMethodConfig.containsKey(matchKey) ?
                matchMethodConfig.getBigDecimal(matchKey)
                        .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        BigDecimal cancelOrderRationOri = matchMethodConfig.containsKey(cancelKey) ?
                matchMethodConfig.getBigDecimal(cancelKey)
                        .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        BigDecimal invalidOrderRationOri = matchMethodConfig.containsKey(invalidKey) ?
                matchMethodConfig.getBigDecimal(invalidKey)
                        .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        BigDecimal noMatchOrderRationOri = matchMethodConfig.containsKey(noMatchKey) ?
                matchMethodConfig.getBigDecimal(noMatchKey)
                        .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP) : BigDecimal.ZERO;


        // 计算该证券子类别的撮合方式总比例
        BigDecimal totalRatio = matchOrderRationOri.add(cancelOrderRationOri)
                .add(invalidOrderRationOri).add(noMatchOrderRationOri);

        // 避免除以零
        if (totalRatio.compareTo(BigDecimal.ZERO) == 0) {
            log.warn("SET{} ({}) 撮合方式总比例为 0，使用默认平均分配", setId, securityType);
            // 如果总比例为 0，则平均分配
            int avgOrderNumber = setOrderNumber / 4;
            int remainder = setOrderNumber % 4;

            int matchOrderNumber = avgOrderNumber;
            int cancelOrderNumber = avgOrderNumber;
            int invalidOrderNumber = avgOrderNumber;
            int noMatchOrderNumber = avgOrderNumber + remainder;

            return new MatchMethodDistribution(invalidOrderNumber, noMatchOrderNumber,
                    cancelOrderNumber, matchOrderNumber);
        }

        // 归一化比例（确保总和为 1）
        BigDecimal matchOrderRation = matchOrderRationOri.divide(totalRatio, 8, RoundingMode.UP);
        BigDecimal cancelOrderRation = cancelOrderRationOri.divide(totalRatio, 8, RoundingMode.UP);
        BigDecimal invalidOrderRation = invalidOrderRationOri.divide(totalRatio, 8, RoundingMode.UP);
        BigDecimal noMatchOrderRation = noMatchOrderRationOri.divide(totalRatio, 8, RoundingMode.UP);



        // 计算各类订单数量
        BigDecimal matchOrderNumberDecimal = new BigDecimal(setOrderNumber)
                .multiply(matchOrderRation).setScale(0, RoundingMode.HALF_UP);
        BigDecimal cancelOrderNumberDecimal = new BigDecimal(setOrderNumber)
                .multiply(cancelOrderRation).setScale(0, RoundingMode.HALF_UP);
        BigDecimal invalidOrderNumberDecimal = new BigDecimal(setOrderNumber)
                .multiply(invalidOrderRation).setScale(0, RoundingMode.HALF_UP);
        BigDecimal noMatchOrderNumberDecimal = new BigDecimal(setOrderNumber)
                .multiply(noMatchOrderRation).setScale(0, RoundingMode.HALF_UP);

        int matchOrderNumber = matchOrderNumberDecimal.intValue();
        int cancelOrderNumber = cancelOrderNumberDecimal.intValue();
        int invalidOrderNumber = invalidOrderNumberDecimal.intValue();
        int noMatchOrderNumber = noMatchOrderNumberDecimal.intValue();

        // 调整舍入误差
        int calculatedTotal = matchOrderNumber + cancelOrderNumber + invalidOrderNumber + noMatchOrderNumber;
        int difference = setOrderNumber - calculatedTotal;
        if (difference != 0) {
            // 将差异加到撮合订单上
            matchOrderNumber += difference;
        }



        return new MatchMethodDistribution(invalidOrderNumber, noMatchOrderNumber,
                cancelOrderNumber, matchOrderNumber);
    }

    /**
     * 调整订单数量，确保总和匹配
     */
    private void adjustOrderQuantity(Map<String, MatchMethodDistribution> setDistributionMap,
                                     int expectedTotal, String securityType) {
        int actualTotal = setDistributionMap.values().stream()
                .mapToInt(d -> d.invalidOrderNumber + d.noMatchOrderNumber +
                        d.cancelOrderNumber + d.matchOrderNumber)
                .sum();

        int difference = expectedTotal - actualTotal;
        if (difference != 0) {
            log.info("证券子类别 {} 订单总数调整：预期={}, 实际={}, 差额={}",
                    securityType, expectedTotal, actualTotal, difference);

            // 找到订单数最大的 SET，将差异加到该 SET 的撮合订单上
            String maxSetId = null;
            int maxOrderNumber = -1;

            for (String setId : setDistributionMap.keySet()) {
                MatchMethodDistribution dist = setDistributionMap.get(setId);
                int totalOrders = dist.invalidOrderNumber + dist.noMatchOrderNumber +
                        dist.cancelOrderNumber + dist.matchOrderNumber;
                if (totalOrders > maxOrderNumber) {
                    maxOrderNumber = totalOrders;
                    maxSetId = setId;
                }
            }

            if (maxSetId != null) {
                MatchMethodDistribution maxDist = setDistributionMap.get(maxSetId);
                maxDist.matchOrderNumber += difference;
                log.info("调整 SET{} 的撮合订单数量：{} -> {}", maxSetId,
                        maxDist.matchOrderNumber - difference, maxDist.matchOrderNumber);
            }
        }
    }


    /**
     * 查询指定 SET 下的股票列表
     */
    private List<SseSecurityInfo> getSecuritiesInSet(String securitySubType, String securitySet, int limit) {
        SseSecurityInfo query = new SseSecurityInfo();
        query.setSecurityMarket("ASHR");
        query.setSecurityType("ES");
        query.setSecuritySubType(securitySubType);
        query.setSecuritySet(Long.valueOf(securitySet));
        query.setUsageStatus("0");

        // 查询前 N 个股票（N=SET 中的股票数量）
        List<SseSecurityInfo> allSecurities = sseSecurityInfoService.selectSseSecurityInfoList(query);
        if (allSecurities == null || allSecurities.isEmpty()) {
            log.warn("未查询到证券子类别 {}、SET{} 的股票数据", securitySubType, securitySet);
            return Collections.emptyList();
        }

        return allSecurities.stream().limit(limit).collect(java.util.stream.Collectors.toList());
    }


    /**
     * 按子类别计算 set 的订单数量
     */
    private Map<String, Integer> calcOrderSetQty(SseDataRule sseDataRule, Map<String, BigDecimal> setRatioAllocation, String subCategory) {
        BigDecimal security_ratio = BigDecimal.ZERO;
        String securitySubCategory = sseDataRule.getSecuritySubCategory();
        JSONObject securitySubCategoryJson = JSON.parseObject(securitySubCategory);
        if (securitySubCategoryJson.containsKey(subCategory)) {
            security_ratio = securitySubCategoryJson.getJSONObject(subCategory).getBigDecimal("ratio");
        }

        //计算当前 set 订单总数
        int setTotalOrderNumber = new BigDecimal(sseDataRule.getOrderCount())
                .multiply(security_ratio)
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.UP).intValue();

        //获取各子类别的订单数量
        int preSetTotalOrderNumber = 0;

        String maxSetId = null;
        int maxSetOrderNumber = -1;

        HashMap<String, Integer> setQtyMap = new HashMap<>();
        // 处理 firstType 的数据组合
        for (String setId : setRatioAllocation.keySet()) {
            BigDecimal ratio = setRatioAllocation.getOrDefault(setId, BigDecimal.ZERO);
            //计算 SET 订单数 set 订单笔数 = 订单总数* set 比例
            int setOrderNumber = new BigDecimal(sseDataRule.getOrderCount())
                    .multiply(ratio)
                    .divide(BigDecimal.valueOf(100), 0, RoundingMode.UP).intValue();
            preSetTotalOrderNumber = preSetTotalOrderNumber + setOrderNumber;
            setQtyMap.put(setId, setOrderNumber);
            // 记录订单数最大的 SET
            if (setOrderNumber > maxSetOrderNumber) {
                maxSetOrderNumber = setOrderNumber;
                maxSetId = setId;
            }
        }

        // 比较 preSetTotalOrderNumber 和 setTotalOrderNumber 并进行调整
        int difference = setTotalOrderNumber - preSetTotalOrderNumber;
        if (difference > 0) {
            // 如果 setTotalOrderNumber 比 preSetTotalOrderNumber 大，则将多余的数分配给 setOrderNumber 最大的值
            setQtyMap.put(maxSetId, setQtyMap.get(maxSetId) + difference);
        } else if (difference < 0) {
            // 如果 setTotalOrderNumber 比 preSetTotalOrderNumber 小，则从 setOrderNumber 最大的值中减少多余的数
            int adjustment = Math.min(Math.abs(difference), setQtyMap.get(maxSetId));
            setQtyMap.put(maxSetId, setQtyMap.get(maxSetId) - adjustment);
        }

        log.info("子类别：{},set 订单数量:{}", subCategory, setQtyMap);
        return setQtyMap;
    }

    /**
     * 获取股票数量
     */
    private Map<String, Integer> getSecurityQuantityMap(SseDataRule sseDataRule) {
        int ash_security_quantity = 0;
        int ksh_security_quantity = 0;

        Map<String, Integer> securityMap = new LinkedHashMap<>();
        if (StringUtils.isNotBlank(sseDataRule.getSecuritySubCategory())) {
            JSONObject jsonObject = JSON.parseObject(sseDataRule.getSecuritySubCategory());

            if (jsonObject.containsKey("ASH")) {
                JSONObject ashObject = jsonObject.getJSONObject("ASH");
                ash_security_quantity = ashObject.getIntValue("quantity");
            }

            if (jsonObject.containsKey("KSH")) {
                JSONObject kshObject = jsonObject.getJSONObject("KSH");
                ksh_security_quantity = kshObject.getIntValue("quantity");
            }
        }
        // 按数量排序
        if (ash_security_quantity < ksh_security_quantity) {
            securityMap.put("ASH", ash_security_quantity);
            securityMap.put("KSH", ksh_security_quantity);
        } else {
            securityMap.put("KSH", ksh_security_quantity);
            securityMap.put("ASH", ash_security_quantity);
        }

        return securityMap;
    }

    /**
     * 提取 set 集合
     */
    private Map<String, Integer> getSecuritySetQuantityMap(SseDataRule sseDataRule) {
        if (StringUtils.isNotBlank(sseDataRule.getProductSet())) {
            JSONObject setJsonObject = JSON.parseObject(sseDataRule.getProductSet());

            // 创建 Map 存储 setId 和对应的 quantity
            Map<String, Integer> setQuantityMap = new HashMap<>();

            // 遍历所有 set
            for (String setId : setJsonObject.keySet()) {
                JSONObject setInfo = setJsonObject.getJSONObject(setId);
                int quantity = setInfo.getIntValue("quantity");
                setQuantityMap.put(setId, quantity);

            }

            // 按 quantity 进行倒序排序
            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(setQuantityMap.entrySet());
            entryList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

            // 重建 LinkedHashMap 以保持排序顺序
            Map<String, Integer> sortedMap = new LinkedHashMap<>();
            for (Map.Entry<String, Integer> entry : entryList) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
            log.info("setQuantityMap:" + sortedMap);
            return sortedMap;
        }
        return new LinkedHashMap<>();
    }

    /**
     * 获取股票比例
     */
    private Map<String, BigDecimal> getSecurityRatioMap(SseDataRule sseDataRule) {
        BigDecimal ash_security_ratio = BigDecimal.ZERO;
        BigDecimal ksh_security_ratio = BigDecimal.ZERO;

        Map<String, BigDecimal> securityRatioMap = new LinkedHashMap<>();
        if (StringUtils.isNotBlank(sseDataRule.getSecuritySubCategory())) {
            JSONObject jsonObject = JSON.parseObject(sseDataRule.getSecuritySubCategory());

            if (jsonObject.containsKey("ASH")) {
                JSONObject ashObject = jsonObject.getJSONObject("ASH");
                ash_security_ratio = ashObject.getBigDecimal("ratio");
            }

            if (jsonObject.containsKey("KSH")) {
                JSONObject kshObject = jsonObject.getJSONObject("KSH");
                ksh_security_ratio = kshObject.getBigDecimal("ratio");
            }
        }
        securityRatioMap.put("KSH", ksh_security_ratio);
        securityRatioMap.put("ASH", ash_security_ratio);

        return securityRatioMap;
    }

    /**
     * 提取 set 比例
     */
    private Map<String, BigDecimal> getSecuritySetRatioMap(SseDataRule sseDataRule) {
        Map<String, BigDecimal> setRatioMap = new LinkedHashMap<>();
        if (StringUtils.isNotBlank(sseDataRule.getProductSet())) {
            JSONObject setJsonObject = JSON.parseObject(sseDataRule.getProductSet());

            for (String setId : setJsonObject.keySet()) {
                JSONObject setInfo = setJsonObject.getJSONObject(setId);
                BigDecimal ratio = setInfo.getBigDecimal("ratio");
                setRatioMap.put(setId, ratio);
            }
        }
        return setRatioMap;
    }

    //获取 set 集合对应的股票数量方法：
    private List<CountCommonDo> getSecuritySetCountByType(String securityType) {
        SseSecurityInfo sseSecurityInfo = new SseSecurityInfo();
        sseSecurityInfo.setSecurityMarket("ASHR");
        sseSecurityInfo.setSecurityType("ES");
        sseSecurityInfo.setSecuritySubType(securityType);
        sseSecurityInfo.setUsageStatus("0"); // 只统计可用状态的股票
        return sseSecurityInfoService.SecuritySetCount(sseSecurityInfo);
    }

    /**
     * 撮合方式分布内部类
     */
    public static class MatchMethodDistribution {
        public int invalidOrderNumber;      // 废单数量
        public int noMatchOrderNumber;      // 不撮合订单数量
        public int cancelOrderNumber;       // 撤单数量
        public int matchOrderNumber;        // 撮合订单数量

        public MatchMethodDistribution(int invalidOrderNumber, int noMatchOrderNumber,
                                       int cancelOrderNumber, int matchOrderNumber) {
            this.invalidOrderNumber = invalidOrderNumber;
            this.noMatchOrderNumber = noMatchOrderNumber;
            this.cancelOrderNumber = cancelOrderNumber;
            this.matchOrderNumber = matchOrderNumber;
        }
    }

    /**
     * 打印分配详细信息
     */
    private void printAllocationDetail(Map<String, String> setSecurityJson,
                                       Map<String, Map<String, MatchMethodDistribution>> matchMethodDistributionMap) {
        log.info("==================== SET 股票分配详情 ====================");
        for (String securityType : setSecurityJson.keySet()) {
            log.info("证券子类别：{}", securityType);
            log.info("  股票分配 JSON: {}", setSecurityJson.get(securityType));
        }
        log.info("==================== SET 撮合方式分布详情 ====================");
        for (String securityType : matchMethodDistributionMap.keySet()) {
            log.info("证券子类别：{}", securityType);
            Map<String, MatchMethodDistribution> distributionMap = matchMethodDistributionMap.get(securityType);
            for (String setId : distributionMap.keySet()) {
                MatchMethodDistribution dist = distributionMap.get(setId);
                log.info("  SET{}: 废单={}, 不撮合={}, 撤单={}, 撮合={}, 总计={}",
                        setId, dist.invalidOrderNumber, dist.noMatchOrderNumber,
                        dist.cancelOrderNumber, dist.matchOrderNumber,
                        dist.invalidOrderNumber + dist.noMatchOrderNumber +
                                dist.cancelOrderNumber + dist.matchOrderNumber);
            }
        }

    }



    /**
     * 解析前端上送的一撮多配置（数组格式）
     * 前端配置格式：[{"oneToManyCount":3000,"quantity":1},{"oneToManyCount":5,"quantity":10}]
     * oneToManyCount: 一撮多的"多"是多少 (如 3000 表示 1 买 +3000 卖)
     * quantity: 这种一撮多的组数
     */
    private List<MultiLegInput> parseMultiLegInputs(JSONArray multiLegArray) {
        List<MultiLegInput> result = new ArrayList<>();


        // 遍历数组中的每个配置项
        for (int i = 0; i < multiLegArray.size(); i++) {
            JSONObject item = multiLegArray.getJSONObject(i);

            Integer oneToManyCount = item.getInteger("oneToManyCount");
            Integer quantity = item.getInteger("quantity");

            if (oneToManyCount != null && quantity != null && oneToManyCount > 0 && quantity > 0) {
                int ordersPerGroup = 1 + oneToManyCount; // 1 买 + N 卖
                int totalOrders = ordersPerGroup * quantity; // 总订单数

                MultiLegInput input = new MultiLegInput();
                input.setConfigKey("1v" + oneToManyCount);
                input.setMultiCount(oneToManyCount);
                input.setOrdersPerGroup(ordersPerGroup);
                input.setQuantity(quantity);
                input.setTotalOrders(totalOrders);

                result.add(input);

                log.info("解析一撮多配置：1 撮={}, 组数={}, 每组订单数={}, 总订单={}",
                        oneToManyCount, quantity, ordersPerGroup, totalOrders);
            } else {
                log.warn("一撮多配置项无效：index={}, data={}", i, item);
            }
        }

        return result;
    }


    /**
     * 一撮多输入配置内部类
     */
    public static class MultiLegInput {
        private String configKey;          // 配置 key，如 matching1v3000Buyer
        private int multiCount;            // "多"的数量，如 3000
        private int ordersPerGroup;        // 每组订单数 = 1 + multiCount
        private BigDecimal ratio;          // 该撮合方式的比例
        private int quantity;              // 组数（根据比例和撮合订单总数计算）
        private int totalOrders;           // 总订单数 = ordersPerGroup × quantity

        public String getConfigKey() {
            return configKey;
        }

        public void setConfigKey(String configKey) {
            this.configKey = configKey;
        }

        public int getMultiCount() {
            return multiCount;
        }

        public void setMultiCount(int multiCount) {
            this.multiCount = multiCount;
        }

        public int getOrdersPerGroup() {
            return ordersPerGroup;
        }

        public void setOrdersPerGroup(int ordersPerGroup) {
            this.ordersPerGroup = ordersPerGroup;
        }

        public BigDecimal getRatio() {
            return ratio;
        }

        public void setRatio(BigDecimal ratio) {
            this.ratio = ratio;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getTotalOrders() {
            return totalOrders;
        }

        public void setTotalOrders(int totalOrders) {
            this.totalOrders = totalOrders;
        }
    }


}
